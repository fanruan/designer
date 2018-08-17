package com.fr.design.data.tabledata.wrapper;

import com.fr.base.FRContext;
import com.fr.base.TableData;
import com.fr.data.TableDataSource;
import com.fr.data.impl.ClassTableData;
import com.fr.data.impl.DBTableData;
import com.fr.data.impl.DecoratedTableData;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.data.impl.FileTableData;
import com.fr.data.impl.MultiFieldTableData;
import com.fr.data.impl.MultiTDTableData;
import com.fr.data.impl.RecursionTableData;
import com.fr.data.impl.storeproc.StoreProcedure;
import com.fr.design.data.datapane.TableDataNameObjectCreator;
import com.fr.design.data.tabledata.tabledatapane.AbstractTableDataPane;
import com.fr.design.data.tabledata.tabledatapane.ClassTableDataPane;
import com.fr.design.data.tabledata.tabledatapane.DBTableDataPane;
import com.fr.design.data.tabledata.tabledatapane.DecoratedTableDataPane;
import com.fr.design.data.tabledata.tabledatapane.EmbeddedTableDataPane;
import com.fr.design.data.tabledata.tabledatapane.FileTableDataPane;
import com.fr.design.data.tabledata.tabledatapane.MultiTDTableDataPane;
import com.fr.design.data.tabledata.tabledatapane.ProcedureDataPane;
import com.fr.design.data.tabledata.tabledatapane.TreeTableDataPane;
import com.fr.file.TableDataConfig;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 根据TableData取对应的一些属性
 *
 * @author zhou
 * @since 2012-3-28下午10:03:52
 */
public abstract class TableDataFactory {
    /**
     * 有顺序的,用来排序用
     */
    private static Map<String, TableDataNameObjectCreator> map = new java.util.LinkedHashMap<>();
    
    private static Map<String, TableDataNameObjectCreator> defaultMap = new LinkedHashMap<>();

    /**
     * 同一类型的只能加一次,就加最上层的类,因为要排序。如果将所有的 FileTableData都加进来，那么FileTableData的排序就不正确了
     */
    static {
        defaultMap.put(DBTableData.class.getName(), new TableDataNameObjectCreator(null, "/com/fr/design/images/data/database.png", DBTableData.class, DBTableDataPane.class));
        defaultMap.put(ClassTableData.class.getName(), new TableDataNameObjectCreator(null, "/com/fr/design/images/data/source/classTableData.png", ClassTableData.class, ClassTableDataPane.class));
        defaultMap.put(EmbeddedTableData.class.getName(), new TableDataNameObjectCreator(null, "/com/fr/design/images/data/dataTable.png", EmbeddedTableData.class, EmbeddedTableDataPane.class));
        defaultMap.put(DecoratedTableData.class.getName(), new TableDataNameObjectCreator(null, "/com/fr/design/images/data/multi.png", DecoratedTableData.class, DecoratedTableDataPane.class));
        defaultMap.put(StoreProcedure.class.getName(), new TableDataNameObjectCreator(null, "/com/fr/design/images/data/store_procedure.png", StoreProcedure.class, ProcedureDataPane.class));
        defaultMap.put(MultiTDTableData.class.getName(), new TableDataNameObjectCreator(null, "/com/fr/design/images/data/multi.png", MultiTDTableData.class, MultiTDTableDataPane.class));
        defaultMap.put(FileTableData.class.getName(), new TableDataNameObjectCreator(null, "/com/fr/design/images/data/file.png", FileTableData.class, FileTableDataPane.class));
        defaultMap.put(RecursionTableData.class.getName(), new TableDataNameObjectCreator(null, "/com/fr/design/images/data/tree.png", RecursionTableData.class, TreeTableDataPane.class));
        defaultMap.put(MultiFieldTableData.class.getName(), new TableDataNameObjectCreator(null, "/com/fr/design/images/data/database.png", MultiFieldTableData.class, null));
        map.putAll(defaultMap);
    }

    /**
     * 注册组件
     *
     * @param clazz 数据集类
     * @param creator 组件
     */
    public static void registerExtra(Class<? extends TableData> clazz, TableDataNameObjectCreator creator) {
    
        map.put(clazz.getName(), creator);
    }
    
    public static void removeExtra(Class<? extends TableData> clazz) {
    
        String name = clazz.getName();
        if (defaultMap.containsKey(name)) {
            map.put(name, defaultMap.get(name));
        } else {
            map.remove(name);
        }
    }
    
    private static TableDataNameObjectCreator getTableDataNameObjectCreator(TableData tabledata) {
        
        TableDataNameObjectCreator tableDataNameObjectCreator = map.get(tabledata.getClass().getName());
        if (tableDataNameObjectCreator == null) {
            tableDataNameObjectCreator = map.get(tabledata.getClass().getSuperclass().getName());
            if (tableDataNameObjectCreator == null) {
                tableDataNameObjectCreator = map.get(tabledata.getClass().getSuperclass().getSuperclass().getName());// 最多三层吧，不够再加
                // 不用循环了
            }
        }
        return tableDataNameObjectCreator;
    }
    
    
    /**
     * 获取数据集所对应的编辑面板
     *
     * @param tabledata 数据集
     * @param name      名字
     * @return 返回数据集对应的pane
     */
    public static AbstractTableDataPane<?> creatTableDataPane(TableData tabledata, String name) {
        AbstractTableDataPane datapane = null;
        TableDataNameObjectCreator tableDataNameObjectCreator = getTableDataNameObjectCreator(tabledata);
        Class<? extends AbstractTableDataPane<?>> creatorClass = (Class<AbstractTableDataPane<?>>) tableDataNameObjectCreator.getUpdatePane();
        if (tableDataNameObjectCreator != null && creatorClass != null) {
            try {
                if (ComparatorUtils.equals(creatorClass, MultiTDTableDataPane.class) || ComparatorUtils.equals(creatorClass, TreeTableDataPane.class)) {
                    Constructor constructor = creatorClass.getDeclaredConstructor(new Class[]{String.class});
                    constructor.setAccessible(true);
                    datapane = (AbstractTableDataPane) constructor.newInstance(name);
                } else {
                    datapane = creatorClass.newInstance();
                }
                datapane.populateBean(tabledata); // August:不管tabledata是刚刚新建的还是原来的，一律populate进去，如果出错就是代码写的不好
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
        return datapane;
    }

    /**
     * 创建数据集面板
     *
     * @param tabledata 数据集
     * @return 返回数据集对应的pane
     */
    public static AbstractTableDataPane<?> creatTableDataPane(TableData tabledata) {
        return creatTableDataPane(tabledata, StringUtils.EMPTY);
    }

    /**
     * 获取数据集所对应的图标路径
     *
     * @param tabledata
     * @return
     */
    public static String getIconPath(TableData tabledata) {
        TableDataNameObjectCreator tableDataNameObjectCreator = getTableDataNameObjectCreator(tabledata);
        if (tableDataNameObjectCreator != null && tableDataNameObjectCreator.getIconPath() != null) {
            return tableDataNameObjectCreator.getIconPath();
        }
        return "/com/fr/design/images/data/database.png";
    }

    /**
     * 获取已经排好顺序的数组
     * 先数据库查询，再程序，再内置数据集，再关联数据集，再文件数据集；这些内部按内部按先数字（0-9）、再字母（a-z）、然后汉字（拼音）进行排序。
     *
     * @param source
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String[] getSortOfChineseNameOfTemplateData(TableDataSource source) {
        clearAll();
        java.util.Iterator<String> nameIt = source.getTableDataNameIterator();
        while (nameIt.hasNext()) {
            String name = nameIt.next();
            TableData td = source.getTableData(name);
            addName(name, td);
        }
        return getSortedNameArray();
    }

    @SuppressWarnings("unchecked")
    public static String[] getSortOfChineseNameOfServerData(TableDataConfig tableDataConfig) {
        clearAll();
        try {
            java.util.Iterator<String> nameIt = tableDataConfig.getTableDatas().keySet().iterator();
            while (nameIt.hasNext()) {
                String name = nameIt.next();
                addName(name, tableDataConfig.getTableData(name));
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        return getSortedNameArray();
    }

    private static String[] getSortedNameArray() {
        Iterator<Entry<String, TableDataNameObjectCreator>> entryIt = map.entrySet().iterator();
        List<String> namelist = new ArrayList<String>();
        while (entryIt.hasNext()) {
            Entry<String, TableDataNameObjectCreator> entry = entryIt.next();
            TableDataNameObjectCreator tableDataNameObjectCreator = entry.getValue();
            namelist.addAll(tableDataNameObjectCreator.getNames());
        }
        return namelist.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    private static void addName(String name, TableData tabledata) {
        if (tabledata == null) {
            return;
        }

        TableDataNameObjectCreator tableDataNameObjectCreator = getTableDataNameObjectCreator(tabledata);
        if (tableDataNameObjectCreator == null) {
            return;
        }
        tableDataNameObjectCreator.addNames(name);
    }

    private static void clearAll() {
        Iterator<Entry<String, TableDataNameObjectCreator>> entryIt = map.entrySet().iterator();
        while (entryIt.hasNext()) {
            entryIt.next().getValue().clear();
        }
    }
}