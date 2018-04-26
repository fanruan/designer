package com.fr.design;

import com.fr.base.Parameter;
import com.fr.base.io.IOFile;
import com.fr.data.TableDataSource;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.JTemplateProvider;
import com.fr.form.ui.Widget;
import com.fr.general.ComparatorUtils;
import com.fr.stable.js.WidgetName;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 当前的设计器模式
 *
 * @author zhou
 * @since 2012-7-26上午11:24:54
 */
public abstract class DesignModelAdapter<T extends IOFile, S extends JTemplateProvider> {

    private static DesignModelAdapter<?, ?> currentModelAdapter;
    protected S jTemplate;

    public DesignModelAdapter(S jTemplate) {
        this.jTemplate = jTemplate;
            setCurrentModelAdapter(this);
    }

    public S getjTemplate() {
        return jTemplate;
    }

    public void setjTemplate(S jTemplate) {
        this.jTemplate = jTemplate;
    }

    public T getBook() {
        return (T) ((JTemplate) jTemplate).getTarget();
    }

    public static void setCurrentModelAdapter(DesignModelAdapter<?, ?> model) {
        currentModelAdapter = model;
    }

    public static DesignModelAdapter<?, ?> getCurrentModelAdapter() {
        return currentModelAdapter;
    }

    /**
     * 响应目标改变事件.
     */
    public void fireTargetModified() {
        ((JTemplate) this.jTemplate).fireTargetModified();
    }

    public String[] getFloatNames() {
        return new String[0];
    }

    public Parameter[] getParameters() {
        return new Parameter[0];
    }

    // 报表参数
    public Parameter[] getReportParameters() {
        return new Parameter[0];
    }

    /**
     * 数据源参数
     *
     * @return
     */
    public Parameter[] getTableDataParameters() {
        return new Parameter[0];
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

    public abstract Widget[] getLinkableWidgets() ;

    public abstract List<WidgetName> getWidgetsName();

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
}