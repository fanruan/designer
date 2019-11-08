package com.fr.design;

import com.fr.base.Parameter;
import com.fr.base.io.BaseBook;
import com.fr.data.TableDataSource;
import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.mainframe.DesignerFrameFileDealerPane;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.JTemplateProvider;
import com.fr.form.ui.DataControl;
import com.fr.form.ui.MultiFileEditor;
import com.fr.form.ui.Widget;
import com.fr.general.ComparatorUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.js.WidgetName;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 设计器模式 FormModel or WorkBookModel
 * <p>
 * 指的是编辑的模板是普通报表还是决策报表
 *
 * @author zhou
 * @since 2012-7-26上午11:24:54
 */
public abstract class DesignModelAdapter<T extends BaseBook, S extends JTemplateProvider<T>> {

    /**
     * 当前的设计模式 FormModel or WorkBookModel
     */
    private static DesignModelAdapter<?, ?> currentModelAdapter;
    /**
     * 模板
     */
    protected S jTemplate;

    /**
     * 全部的参数，包括全局参数，模板参数和数据集参数
     */
    private Parameter[] parameters;

    /**
     * 数据集参数
     */
    private Parameter[] tableDataParameters;

    /**
     * 模板参数
     */
    private Parameter[] templateParameters;


    public DesignModelAdapter(S jTemplate) {
        this.jTemplate = jTemplate;
        updateCachedParameter();
        setCurrentModelAdapter(this);
    }

    public static void setCurrentModelAdapter(DesignModelAdapter<?, ?> model) {
        currentModelAdapter = model;
    }

    public static DesignModelAdapter<?, ?> getCurrentModelAdapter() {
        return currentModelAdapter;
    }

    /**
     * 获取当前编辑的模板
     *
     * @return template
     * @see DesignerFrameFileDealerPane#setCurrentEditingTemplate(JTemplate)
     * @see HistoryTemplateListCache#getCurrentEditingTemplate()
     * @deprecated use {@link HistoryTemplateListCache#setCurrentEditingTemplate(JTemplate)} instead
     */
    @Deprecated
    public S getjTemplate() {
        return jTemplate;
    }

    /**
     * 设置当前编辑的模板
     * 不要脱离上下文直接调用
     *
     * @param jTemplate jt
     * @see DesignerFrameFileDealerPane#setCurrentEditingTemplate(JTemplate)
     * @deprecated use {@link HistoryTemplateListCache#setCurrentEditingTemplate(JTemplate)} instead
     */
    @Deprecated
    public void setjTemplate(S jTemplate) {
        this.jTemplate = jTemplate;
    }

    public T getBook() {
        return jTemplate.getTarget();
    }


    /**
     * 响应正在编辑的模板改变事件.
     */
    public void fireTargetModified() {
        this.jTemplate.fireTargetModified();
    }

    /**
     * 获取悬浮元素名称数组
     *
     * @return String[] 悬浮元素名称数组
     */
    public String[] getFloatNames() {
        return new String[0];
    }

    /**
     * 获取编辑模板的所有参数
     * <p>
     * 由于在参数面板拖动过程中频繁获取
     * 远程设计时数据集参数rpc 调用需要考虑网络等因素,因此会比较缓慢,引起参数面板拖动卡顿,
     * 所以缓存一下,并且在参数改动时及时缓存
     *
     * @return Parameter[] 模板的所有参数
     */
    public Parameter[] getParameters() {
        return parameters == null ? new Parameter[0] : parameters;
    }

    /**
     * 模板参数（报表参数）
     * <p>
     * 既然全部参数都,那么这个也缓存一下,并且在参数改动时及时缓存
     *
     * @return Parameter[] 模板参数
     * @deprecated use {@link DesignModelAdapter#getTemplateParameters()} instead
     */
    @Deprecated
    public Parameter[] getReportParameters() {
        return getTemplateParameters();
    }

    /**
     * 模板参数（报表参数）
     * <p>
     * 既然全部参数都,那么这个也缓存一下,并且在参数改动时及时缓存
     *
     * @return Parameter[] 模板参数
     */
    public Parameter[] getTemplateParameters() {
        return templateParameters == null ? new Parameter[0] : templateParameters;
    }

    /**
     * 数据源参数
     * <p>
     * 既然全部参数都,那么这个也缓存一下,并且在参数改动时及时缓存
     *
     * @return Parameter[] 数据源参数
     */
    public Parameter[] getTableDataParameters() {
        return tableDataParameters == null ? new Parameter[0] : tableDataParameters;
    }


    /**
     * 重命名TableData后的一些操作
     *
     * @param oldName 旧名字
     * @param newName 新名字.
     * @return 返回是否名字一样.
     */
    public boolean renameTableData(String oldName, String newName) {
        return renameTableData(oldName, newName, true);
    }

    /**
     * 重命名数据集
     *
     * @param oldName            旧名字
     * @param newName            新名字
     * @param isNeedFireModified 是否需要触发保存
     * @return 重命名成功返回True
     */
    public boolean renameTableData(String oldName, String newName, boolean isNeedFireModified) {
        if (!ComparatorUtils.equals(oldName, newName)) {
            TableDataSource tds = getBook();
            boolean b;
            b = tds.renameTableData(oldName, newName);
            if (!b) {
                return b;
            }
            if (isNeedFireModified) {
                fireTargetModified();
            }
        }
        return true;
    }

    /**
     * 重命名tabledata
     *
     * @param map 新名字
     */
    public void renameTableData(Map<String, String> map) {
        if (map.isEmpty()) {
            return;
        }
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            renameTableData(key, map.get(key));
        }
    }

    public abstract Widget[] getLinkableWidgets();

    public abstract List<WidgetName> getWidgetsName();

    /**
     * 判断是否是值编辑器可以设置的控件类型
     * @param widget 控件
     * @return 可以设置返回true，否则返回false
     */
    public boolean widgetAccepted(Widget widget) {
        return widget != null
                && StringUtils.isNotEmpty(widget.getWidgetName())
                && (widget instanceof DataControl || widget instanceof MultiFileEditor);
    }

    /**
     * 更新缓存的参数
     */
    public void updateCachedParameter() {
        // 全部参数
        this.parameters = getLatestParameters();
        // 数据及参数
        this.tableDataParameters = getLatestTableDataParameters();
        // 模板参数
        this.templateParameters = getLatestTemplateParameters();

    }

    /**
     * 环境改变.
     */
    public abstract void envChanged();

    /**
     * 参数改变.
     */
    public abstract void parameterChanged();

    /**
     * 控件配置改变.
     */
    public abstract void widgetConfigChanged();

    /**
     * 获取变更后的模板参数
     */
    protected abstract Parameter[] getLatestTemplateParameters();

    /**
     * 获取变更后的数据集参数
     */
    protected abstract Parameter[] getLatestTableDataParameters();

    /**
     * 获取变更后的全部参数
     */
    protected abstract Parameter[] getLatestParameters();
}