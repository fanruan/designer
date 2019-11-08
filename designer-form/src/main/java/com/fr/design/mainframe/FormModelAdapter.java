package com.fr.design.mainframe;

import com.fr.base.Parameter;
import com.fr.base.TableData;
import com.fr.data.TableDataSource;
import com.fr.design.DesignModelAdapter;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.form.main.Form;
import com.fr.form.main.WidgetGatherAdapter;
import com.fr.form.ui.BaseChartEditor;
import com.fr.form.ui.ElementCaseEditor;
import com.fr.form.ui.Widget;
import com.fr.script.Calculator;
import com.fr.stable.ParameterProvider;
import com.fr.stable.js.WidgetName;

import java.util.ArrayList;
import java.util.List;

public class FormModelAdapter extends DesignModelAdapter<Form, BaseJForm<Form>> {

    public FormModelAdapter(BaseJForm<Form> jForm) {
        super(jForm);
    }

    /**
     * 环境改变.
     */
    public void envChanged() {
        WidgetToolBarPane.refresh();
        jTemplate.refreshAllNameWidgets();

    }

    /**
     * 参数改变.
     */
    public void parameterChanged() {
        //实时更新参数
        jTemplate.populateParameter();
        // 更新缓存的参数
        updateCachedParameter();
    }

    /**
     * 控件配置改变.
     */
    public void widgetConfigChanged() {
        WidgetToolBarPane.refresh();
        jTemplate.refreshAllNameWidgets();
    }

    /**
     * 重命名TableData后的一些操作
     *
     * @param oldName 旧名字
     * @param newName 新名字.
     * @return 返回是否名字一样.
     */
    public boolean renameTableData(String oldName, String newName) {
        if (super.renameTableData(oldName, newName)) {
            jTemplate.refreshSelectedWidget();
            return true;
        }
        return false;
    }

    @Override
    public List<WidgetName> getWidgetsName() {
        final List<WidgetName> list = new ArrayList<WidgetName>();
        Form.traversalFormWidget(this.getBook().getContainer(), new WidgetGatherAdapter() {

            @Override
            public void dealWith(Widget widget) {
                if (widgetAccepted(widget)) {
                    list.add(new WidgetName(widget.getWidgetName()));
                }
            }
            @Override
            public boolean dealWithAllCards() {
                return true;
            }
        });
        return list;
    }

    /**
     * 获取可以被超链的对象,表单主体中的所有控件
     */
    public Widget[] getLinkableWidgets() {
        final ArrayList<Widget> linkAbleList = new ArrayList<Widget>();
        final JForm currentJForm = ((JForm) HistoryTemplateListPane.getInstance().getCurrentEditingTemplate());
        Form.traversalWidget(currentJForm.getRootLayout(), new WidgetGatherAdapter() {

            @Override
            public boolean dealWithAllCards() {
                return true;
            }

            public void dealWith(Widget widget) {
                boolean isSupportAsHypelink = widget.acceptType(ElementCaseEditor.class) || widget.acceptType(BaseChartEditor.class);
                //可以超链的对象不包含本身; 目前只有图表和报表块可以
                // bug66182 删了条件：!ComparatorUtils.equals(editingECName, widget.getWidgetName())  让当前表单对象可以选到自己
                if (isSupportAsHypelink) {
                    linkAbleList.add(widget);
                }
            }
        }, Widget.class);

        return linkAbleList.toArray(new Widget[linkAbleList.size()]);
    }


    @Override
    protected Parameter[] getLatestTemplateParameters() {
        Parameter[] rpa = this.getBook().getTemplateParameters();
        return rpa == null ? new Parameter[0] : rpa;
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
}
