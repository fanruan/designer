package com.fr.design.data;

import com.fr.base.StoreProcedureParameter;
import com.fr.base.TableData;
import com.fr.concurrent.NamedThreadFactory;
import com.fr.data.TableDataSource;
import com.fr.data.TableDataSourceTailor;
import com.fr.data.core.DataCoreXmlUtils;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.data.impl.storeproc.ProcedureDataModel;
import com.fr.data.impl.storeproc.StoreProcedure;
import com.fr.data.impl.storeproc.StoreProcedureConstants;
import com.fr.data.operator.DataOperator;
import com.fr.design.DesignModelAdapter;
import com.fr.design.data.datapane.preview.PreviewTablePane;
import com.fr.design.data.tabledata.wrapper.ServerTableDataWrapper;
import com.fr.design.data.tabledata.wrapper.StoreProcedureDataWrapper;
import com.fr.design.data.tabledata.wrapper.StoreProcedureNameWrapper;
import com.fr.design.data.tabledata.wrapper.TableDataFactory;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.data.tabledata.wrapper.TemplateTableDataWrapper;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.gui.iprogressbar.AutoProgressBar;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.parameter.ParameterInputPane;
import com.fr.file.ProcedureConfig;
import com.fr.file.TableDataConfig;
import com.fr.general.ComparatorUtils;
import com.fr.general.data.DataModel;
import com.fr.general.data.TableDataException;
import com.fr.log.FineLoggerFactory;
import com.fr.module.ModuleContext;
import com.fr.script.Calculator;
import com.fr.stable.ArrayUtils;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;

import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.ByteArrayOutputStream;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 设计器管理操作数据集的类:
 * 1.对于每个TableData,会生成相应的TableDataWrapper.TableDataWrapper里面有TableData的数据列缓存
 * 2.TableDataWrapper是不支持对TableData的修改查询语句 重命名等修改删除操作
 * 如果TableData变化了，那么TableDataWrapper 也会重新生成,然后存进缓存.这样保证缓存是正确的,不会取到错误的数据列。
 * 3.对于模板数据集，关键词应该保证各个模板数据集之间不同，所以默认有个加上模板名的操作。
 * 4.个人觉得完全没有必要做成那种一个SQL语句对应一个数据列的情况，那样子太复杂了。而且每次比较关键词都很慢
 * <p/>
 * !!!Notice: 除了预览数据集的操作，其他所有涉及到数据集的界面操作，都要经过这个类(因为它有数据集的缓存,不需要重复计算）
 *
 * @author zhou
 */
public abstract class DesignTableDataManager {
    /**
     * 其实globalDsCache没有绝对的必要，只是为了操作方便。如果没有它，那么每次清空服务器数据集或者存储过程的时候，还要去遍历找一下，
     * 这个操作可能比较复杂 。 从减少代码复杂度的角度看，还是很有必要的
     */
    private static java.util.Map<String, TableDataWrapper> globalDsCache = new java.util.HashMap<String, TableDataWrapper>();
    private static java.util.Map<String, String> dsNameChangedMap = new HashMap<String, String>();
    private static List<ChangeListener> globalDsListeners = new ArrayList<>();

    private static Map<String, List<ChangeListener>> dsListenersMap = new HashMap<String, List<ChangeListener>>();


    public static String NO_PARAMETER = "no_paramater_pane";

    //用于记录是否要弹出参数框
    private static ThreadLocal<String> threadLocal = new ThreadLocal<String>();


    /**
     * 清除全局 数据集缓存.
     */
    public static void clearGlobalDs() {
        globalDsCache.clear();
    }

    /**
     * 响应数据集改变.
     */
    private static void fireDsChanged() {
        fireDsChanged(globalDsListeners);
        for (Entry<String, List<ChangeListener>> listenerEntry : dsListenersMap.entrySet()) {
            List<ChangeListener> dsListeners = listenerEntry.getValue();
            fireDsChanged(dsListeners);
        }
    }

    private static void fireDsChanged(List<ChangeListener> dsListeners) {
        for (int i = 0; i < dsListeners.size(); i++) {
            //增强for循环用的iterator实现的, 如果中间哪个listener修改或删除了(如ChartEditPane.dsChangeListener),
            // 由于dsListeners是arraylist, 此时会ConcurrentModifyException
            ChangeEvent e = null;
            dsListeners.get(i).stateChanged(e);
        }
    }

    public static void closeTemplate(JTemplate<?, ?> template) {
        if (template != null) {
            dsListenersMap.remove(template.getPath());
        }
    }

    public static void envChange() {
        dsListenersMap.clear();
        dsNameChangedMap.clear();
        clearGlobalDs();
    }


    /**
     * 响应数据集改变
     *
     * @param dsNameChangedMap 改变名字的数据集
     */
    public static void fireDSChanged(Map<String, String> dsNameChangedMap) {
        clearGlobalDs();
        if (!dsNameChangedMap.isEmpty()) {
            setDsNameChangedMap(dsNameChangedMap);
        }
        fireDsChanged();
        dsNameChangedMap.clear();
    }

    private static void setDsNameChangedMap(Map<String, String> map) {
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            dsNameChangedMap.put(key, map.get(key));
        }
    }

    /**
     * 数据库是否改变
     *
     * @param oldDsName 旧名字
     * @return 是则返回true
     */
    public static boolean isDsNameChanged(String oldDsName) {
        return dsNameChangedMap.containsKey(oldDsName);
    }

    public static String getChangedDsNameByOldDsName(String oldDsName) {
        if (isDsNameChanged(oldDsName)) {
            return dsNameChangedMap.get(oldDsName);
        } else {
            return StringUtils.EMPTY;
        }
    }

    public static void addGlobalDsChangeListener(ChangeListener l) {
        globalDsListeners.add(l);
    }

    /**
     * 添加模板数据集改变 监听事件.
     *
     * @param l ChangeListener监听器
     */
    public static void addDsChangeListener(ChangeListener l) {
        JTemplate<?, ?> template = HistoryTemplateListCache.getInstance().getCurrentEditingTemplate();
        String key = StringUtils.EMPTY;
        if (template != null) {
            key = template.getPath();
        }
        List<ChangeListener> dsListeners = dsListenersMap.get(key);
        if (dsListeners == null) {
            dsListeners = new ArrayList<ChangeListener>();
            dsListenersMap.put(key, dsListeners);
        }
        dsListeners.add(l);
    }
    /**
     * 获取数据源source中dsName的所有字段
     *
     * @param source 数据源
     * @param dsName 数据集名字
     * @return
     */
    public static String[] getSelectedColumnNames(TableDataSource source, String dsName) {
        java.util.Map<String, TableDataWrapper> resMap = getAllEditingDataSet(source);
        java.util.Map<String, TableDataWrapper> dsMap = getAllDataSetIncludingProcedure(resMap);
        TableDataWrapper tabledataWrapper = dsMap.get(dsName);
        return tabledataWrapper == null ? new String[0] : tabledataWrapper.calculateColumnNameList().toArray(new String[0]);
    }

    /**
     * august:返回当前正在编辑的具有报表数据源的模板(基本报表、聚合报表) 包括 : 图表模板
     *
     * @return TableDataSource
     * attention:与这个方法有关系的静态组件（不随着切换模板tab而变化的），应该重新执行该方法，再刷新组件
     */
    public static TableDataSource getEditingTableDataSource() {
        return DesignModelAdapter.getCurrentModelAdapter() == null ? null : DesignModelAdapter.getCurrentModelAdapter().getBook();
    }

    /**
     * 返回当前模板(source)数据集、服务器数据集和存储过程中所有的数据集名字
     *
     * @param source
     * @return
     */
    public static String[] getAllDSNames(TableDataSource source) {
        Iterator<Entry<String, TableDataWrapper>> entryIt = getAllEditingDataSet(source).entrySet().iterator();
        List<String> list = new ArrayList<String>();
        while (entryIt.hasNext()) {
            Entry<String, TableDataWrapper> entry = entryIt.next();
            list.add(entry.getKey());
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * 不根据过滤设置，返回当前模板数据集、服务器数据集、存储过程本身，是有顺序的
     */
    public static java.util.Map<String, TableDataWrapper> getAllEditingDataSet(TableDataSource source) {
        java.util.Map<String, TableDataWrapper> resMap = new java.util.LinkedHashMap<String, TableDataWrapper>();
        // 模板数据集
        addTemplateData(resMap, source);

        // 服务器数据集
        addServerData(resMap);
        // 存储过程
        addStoreProcedureData(resMap);

        return resMap;
    }

    public static java.util.Map<String, TableDataWrapper> getAllDataSetIncludingProcedure(java.util.Map<String, TableDataWrapper> resMap) {
        java.util.LinkedHashMap<String, TableDataWrapper> dsMap = new java.util.LinkedHashMap<String, TableDataWrapper>();
        Iterator<Entry<String, TableDataWrapper>> entryIt = resMap.entrySet().iterator();
        while (entryIt.hasNext()) {
            String key = entryIt.next().getKey();
            TableDataWrapper tableDataWrapper = resMap.get(key);
            if (tableDataWrapper.getTableData() instanceof StoreProcedure) {
                StoreProcedure storeProcedure = (StoreProcedure) tableDataWrapper.getTableData();
                boolean hasSchemaOrResult = false;
                StoreProcedureParameter[] parameters = StoreProcedure.getSortPara(storeProcedure.getParameters());
                String name = tableDataWrapper.getTableDataName();
                List<String> resultNames = storeProcedure.getResultNames();
                TableDataWrapper tdw = new StoreProcedureNameWrapper(name + "_Table", storeProcedure);

                for (StoreProcedureParameter parameter : parameters) {
                    if (parameter.getSchema() != StoreProcedureConstants.IN) {
                        String parameterName = name + "_" + parameter.getName();
                        TableDataWrapper newTwd = new StoreProcedureDataWrapper(storeProcedure, name, parameterName, false);
                        dsMap.put(parameterName, newTwd);
                        hasSchemaOrResult = true;
                    }
                }

                if (!resultNames.isEmpty()) {
                    hasSchemaOrResult = true;
                    for (int i = 0; i < resultNames.size(); i++) {
                        String parameterName = name + "_" + resultNames.get(i);
                        TableDataWrapper newTwd = new StoreProcedureDataWrapper(storeProcedure, name, parameterName, false);
                        dsMap.put(parameterName, newTwd);
                    }
                }

                if (!hasSchemaOrResult) {
                    dsMap.put(name + "_Table", tdw);
                }
            } else {
                dsMap.put(key, tableDataWrapper);
            }
        }
        return dsMap;
    }

    /**
     * 不根据过滤设置，返回当前服务器数据集、存储过程所有的数据集，是有顺序的
     */
    public static java.util.Map<String, TableDataWrapper> getGlobalDataSet() {
        java.util.Map<String, TableDataWrapper> resMap = new java.util.LinkedHashMap<String, TableDataWrapper>();

        // 服务器数据集
        addServerData(resMap);
        // 存储过程
        addStoreProcedureData(resMap);

        return resMap;
    }

    /**
     * 根据过滤设置，返回当前模板数据集、服务器数据集、存储过程所有的数据集，是有顺序的
     */
    public static List<Map<String, TableDataWrapper>> getEditingDataSet(TableDataSource source) {
        Map<String, TableDataWrapper> templateDataMap = new LinkedHashMap<String, TableDataWrapper>();
        Map<String, TableDataWrapper> serverDataMap = new LinkedHashMap<String, TableDataWrapper>();
        Map<String, TableDataWrapper> storeProcedureMap = new LinkedHashMap<String, TableDataWrapper>();

        addTemplateData(templateDataMap, source);
        addServerData(serverDataMap);
        addStoreProcedureData(storeProcedureMap);

        List<Map<String, TableDataWrapper>> list = new ArrayList<Map<String, TableDataWrapper>>();
        list.add(templateDataMap);
        list.add(serverDataMap);
        list.add(storeProcedureMap);
        return list;
    }

    private static void addTemplateData(java.util.Map<String, TableDataWrapper> resMap, TableDataSource source) {
        if (source != null) {
            String[] namearray = TableDataFactory.getSortOfChineseNameOfTemplateData(source);
            for (String tabledataname : namearray) {
                TableData td = source.getTableData(tabledataname);
                TableDataWrapper tdw = new TemplateTableDataWrapper(td, tabledataname);
                resMap.put(tabledataname, tdw);
            }
        }
    }

    private static void addServerData(java.util.Map<String, TableDataWrapper> resMap) {
        TableDataConfig tableDataConfig = TableDataConfig.getInstance();
        String[] namearray = TableDataFactory.getSortOfChineseNameOfServerData(tableDataConfig);
        for (String name : namearray) {
            if (globalDsCache.containsKey(name)) {
                resMap.put(name, globalDsCache.get(name));
            } else {
                TableDataWrapper tdw = new ServerTableDataWrapper(tableDataConfig.getTableData(name), name);
                resMap.put(name, tdw);
                globalDsCache.put(name, tdw);
            }
        }
    }


    private static void addStoreProcedureData(java.util.Map<String, TableDataWrapper> resMap) {
        ProcedureConfig procedureConfig = ProcedureConfig.getInstance();
        List<String> names = new ArrayList<>(procedureConfig.getProcedures().keySet());
        Collections.sort(names, Collator.getInstance(java.util.Locale.CHINA));
        for (String name : names) {
            StoreProcedure storeProcedure = procedureConfig.getProcedure(name);
            if (globalDsCache.containsKey(name)) {
                resMap.put(name, globalDsCache.get(name));
            } else {
                TableDataWrapper tdw = new StoreProcedureNameWrapper(name, storeProcedure);
                resMap.put(name, tdw);
                globalDsCache.put(name, tdw);
            }
        }
    }


    /**
     * 预览需要参数的数据集
     *
     * @param tabledata      数据集
     * @param rowCount       需要预览的行数
     * @param needLoadingBar 是否需要加载进度条
     * @return 数据集
     * @throws Exception 异常
     */
    public static EmbeddedTableData previewTableDataNeedInputParameters(TableData tabledata, int rowCount, boolean needLoadingBar) throws Exception {
        return previewTableData(null, tabledata, rowCount, true, needLoadingBar);
    }

    /**
     * 预览需要参数的数据集
     *
     * @param tabledata      数据集
     * @param rowCount       需要预览的行数
     * @param needLoadingBar 是否需要加载进度条
     * @return 数据集
     * @throws Exception 异常
     */
    public static EmbeddedTableData previewTableDataNeedInputParameters(TableDataSource tableDataSource, TableData tabledata, int rowCount, boolean needLoadingBar) throws Exception {
        return previewTableData(tableDataSource, tabledata, rowCount, true, needLoadingBar);
    }

    /**
     * 预览不需要参数的数据集
     *
     * @param tabledata      数据集
     * @param rowCount       需要预览的行数
     * @param needLoadingBar 是否需要加载进度条
     * @return 数据集
     * @throws Exception 异常
     */
    public static EmbeddedTableData previewTableDataNotNeedInputParameters(TableData tabledata, int rowCount, boolean needLoadingBar) throws Exception {
        return previewTableData(null, tabledata, rowCount, false, needLoadingBar);
    }

    /**
     * 预览不需要参数的数据集
     *
     * @param tabledata      数据集
     * @param rowCount       需要预览的行数
     * @param needLoadingBar 是否需要加载进度条
     * @return 数据集
     * @throws Exception 异常
     */
    public static EmbeddedTableData previewTableDataNotNeedInputParameters(TableDataSource tableDataSource, TableData tabledata, int rowCount, boolean needLoadingBar) throws Exception {
        return previewTableData(tableDataSource, tabledata, rowCount, false, needLoadingBar);
    }

    /**
     * 获取预览后的EmbeddedTableData，考虑到Env
     *
     * @param tabledata
     * @param rowCount
     * @param isMustInputParameters 是否必须输入参数值（不管参数有没有默认值）。一般预览时这个值为true，因为一般预览是要看不同的参数的结果的。
     *                              而获取数据集的字段名字时，则没必要
     * @return
     */
    private static EmbeddedTableData previewTableData(TableDataSource tableDataSource, TableData tabledata, int rowCount, boolean isMustInputParameters, boolean needLoadingBar) throws Exception {
        final AutoProgressBar loadingBar = PreviewTablePane.getInstance().getProgressBar();
        ParameterProvider[] parameters = DataOperator.getInstance().getTableDataParameters(tabledata);
        if (ArrayUtils.isEmpty(parameters)) {
            parameters = tabledata.getParameters(Calculator.createCalculator());
        }
        final Map<String, Object> parameterMap = new HashMap<>();
        if (needInputParams(isMustInputParameters, parameters)) {
            final ParameterInputPane pPane = new ParameterInputPane(parameters);
            pPane.showSmallWindow(new JFrame(), new DialogActionAdapter() {
                @Override
                public void doOk() {
                    parameterMap.putAll(pPane.update());
                }
            }).setVisible(true);
        } else {
            for (ParameterProvider parameter : parameters) {
                parameterMap.put(parameter.getName(), parameter.getValue());
            }
        }
        if (loadingBar != null && needLoadingBar) {
            loadingBar.start();
        }
        try {
            for (ParameterProvider parameter : DataOperator.getInstance().getTableDataParameters(tabledata)) {
                if (parameterMap.containsKey(parameter.getName())) {
                    parameter.setValue(parameterMap.get(parameter.getName()));
                }
            }
            return DataOperator.getInstance().previewTableData(TableDataSourceTailor.extractTableData(tableDataSource), tabledata, parameterMap, rowCount);
        } catch (Exception e) {
            throw new TableDataException(e.getMessage(), e);
        } finally {
            ScheduledExecutorService scheduledExecutorService = ModuleContext
                    .getExecutor()
                    .newSingleThreadScheduledExecutor(new NamedThreadFactory(""));
            scheduledExecutorService.schedule(new Runnable() {
                @Override
                public void run() {
                    if (loadingBar != null) {
                        loadingBar.close();
                    }
                }
            }, 100, TimeUnit.MILLISECONDS);
            scheduledExecutorService.shutdown();
        }
    }

    private static boolean needInputParams(boolean mustInputParameters, ParameterProvider[] parameters) {
        if (mustInputParameters && ArrayUtils.isNotEmpty(parameters)) {
            return true;
        }
        for (ParameterProvider parameter : parameters) {
            if (parameter.getValue() == null || StringUtils.EMPTY.equals(parameter.getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回<code>TableData</code>的数据列,注意<code>TableData</code>
     * 是没有考虑参数的。用于简单的查询语句生成的<code>TableData</code>, 或者
     * <code>EmbeddedTableData</code>. 比如说：数据字典-数据库表生成的<code>TableData</code>。
     * 使用该方法是没有数据集缓存的功能的
     *
     * @return List<String>
     */
    public static List<String> getColumnNamesByTableData(TableData tableData) {
        List<String> columnNames = new ArrayList<String>();
        if (tableData != null) {
            DataModel rs = tableData.createDataModel(Calculator.createCalculator());
            int value;
            try {
                value = rs.getColumnCount();
                for (int i = 0; i < value; i++) {
                    columnNames.add(rs.getColumnName(i));
                }
                rs.release();
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }

        }

        return columnNames;
    }

    /**
     * 该方法主要利用了StoreProcedure里面的“dataModelList有缓存作用”的机制
     * 所以用该方法，不会对一个已经计算了的存储过程重复计算.和分页预览时处理机制一样，这样对有多个返回数据集的存储过程来说很有必要
     *
     * @param needLoadingBar 是否需要进度条
     * @param storeProcedure 存储过程
     * @return 数据
     */
    public static ProcedureDataModel[] createLazyDataModel(StoreProcedure storeProcedure, boolean needLoadingBar) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLPrintWriter writer = XMLPrintWriter.create(out);
        // 把storeProcedure写成xml文件到out
        DataCoreXmlUtils.writeXMLStoreProcedure(writer, storeProcedure, null);
        if (storeProcedure.getDataModelSize() > 0 && !storeProcedure.isFirstExpand()) {
            return storeProcedure.creatLazyDataModel();
        }
        ParameterProvider[] inParameters = DataOperator.getInstance().getStoreProcedureParameters(storeProcedure);
        final Map<String, Object> parameterMap = new HashMap<String, Object>();
        if (inParameters.length > 0 && !ComparatorUtils.equals(threadLocal.get(), NO_PARAMETER)) {// 检查Parameter.
            final ParameterInputPane pPane = new ParameterInputPane(inParameters);
            pPane.showSmallWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
                @Override
                public void doOk() {
                    parameterMap.putAll(pPane.update());
                }
            }).setVisible(true);

        }
        storeProcedure.setFirstExpand(false);
        if (needLoadingBar) {
            StoreProcedureDataWrapper.loadingBar.start();
        }
        return DataOperator.getInstance().previewProcedureDataModel(storeProcedure, parameterMap, 0);
    }

    public static void setThreadLocal(String value) {
        threadLocal.set(value);
    }
}