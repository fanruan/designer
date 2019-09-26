package com.fr.design.mainframe;

import com.fr.base.Parameter;
import com.fr.base.TableData;
import com.fr.data.TableDataSource;
import com.fr.design.DesignModelAdapter;
import com.fr.design.bridge.DesignToolbarProvider;
import com.fr.form.ui.Widget;
import com.fr.main.impl.WorkBook;
import com.fr.main.impl.WorkBookHelper;
import com.fr.main.parameter.ReportParameterAttr;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.script.Calculator;
import com.fr.stable.Filter;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.js.WidgetName;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author zhou
 * @since 2012-7-26下午2:03:12
 */
public class WorkBookModelAdapter extends DesignModelAdapter<WorkBook, JWorkBook> {


    public WorkBookModelAdapter(JWorkBook jworkbook) {
        super(jworkbook);
    }


    /**
     * 重命名TableData后的一些操作
     *
     * @param oldName 旧名字
     * @param newName 新名字
     * @return 返回是否刷新.
     */
    public boolean renameTableData(String oldName, String newName) {
        if (super.renameTableData(oldName, newName)) {
            if (this.getBook().getTableData(oldName) == null) {
                jTemplate.refreshParameterPane4TableData(oldName, newName);
            }
            return true;
        }
        return false;
    }

    /**
     * 环境改变.
     */
    public void envChanged() {
        DesignToolbarProvider provider = StableFactory.getMarkedObject(DesignToolbarProvider.STRING_MARKED, DesignToolbarProvider.class);
        if (provider != null) {
            provider.refreshToolbar();
        }
        jTemplate.refreshAllNameWidgets();
    }

    /**
     * 参数改变.
     */
    public void parameterChanged() {
        jTemplate.updateReportParameterAttr();
        jTemplate.populateReportParameterAttr();
        updateCachedParameter();
    }

    /**
     * 控件改变.
     */
    public void widgetConfigChanged() {
        DesignToolbarProvider provider = StableFactory.getMarkedObject(DesignToolbarProvider.STRING_MARKED, DesignToolbarProvider.class);
        if (provider != null) {
            provider.refreshToolbar();
        }
        jTemplate.refreshAllNameWidgets();
    }

    @Override
    protected Parameter[] getLatestTemplateParameters() {
        ReportParameterAttr rpa = this.getBook().getReportParameterAttr();
        return rpa == null ? new Parameter[0] : rpa.getParameters();
    }

    @Override
    protected Parameter[] getLatestTableDataParameters() {
        TableDataSource source = this.getBook();
        Calculator c = Calculator.createCalculator();
        c.setAttribute(TableDataSource.KEY, source);
        java.util.List<ParameterProvider> list = new java.util.ArrayList<ParameterProvider>();
        java.util.Iterator<String> nameIt = this.getBook().getTableDataNameIterator();
        while (nameIt.hasNext()) {
            TableData td = source.getTableData(nameIt.next());
            if (td.getParameters(c) != null) {
                list.addAll(java.util.Arrays.asList(td.getParameters(c)));
            }
        }
        return list.toArray(new Parameter[list.size()]);
    }

    @Override
    protected Parameter[] getLatestParameters() {
        return this.getBook().getParameters();
    }

    /**
     * 返回控件的名字
     *
     * @return widgetName 控件列表.
     */
    @Override
    public List<WidgetName> getWidgetsName() {
        WorkBook wb = this.getBook();
        return WorkBookHelper.listWidgetNamesInWorkBook(wb, new Filter<Widget>() {
            @Override
            public boolean accept(Widget widget) {
                return widgetAccepted(widget);
            }
        }, new Filter<Widget>() {
            @Override
            public boolean accept(Widget widget) {
                //todo 这边有没有必要统一改成widgetAccepted？暂时不改，插件那边可能会有影响，因为插件有的控件并没有实现DataControl的
                return widget != null && StringUtils.isNotEmpty(widget.getWidgetName());
            }
        });
    }

    /**
     * 返回悬浮元素的名字数组
     *
     * @return 返回数组.
     */
    public String[] getFloatNames() {
        TemplateElementCase elementCase = jTemplate.getEditingElementCase();

        List<String> nameList = new ArrayList<String>();
        Iterator<FloatElement> it = elementCase.floatIterator();
        while (it.hasNext()) {
            nameList.add(it.next().getName());
        }
        return nameList.toArray(new String[nameList.size()]);
    }


    public Widget[] getLinkableWidgets() {
        return new Widget[0];
    }
}