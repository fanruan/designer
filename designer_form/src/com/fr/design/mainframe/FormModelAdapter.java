package com.fr.design.mainframe;

import com.fr.base.Parameter;
import com.fr.design.DesignModelAdapter;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.form.main.Form;
import com.fr.form.main.WidgetGatherAdapter;
import com.fr.form.ui.*;
import com.fr.stable.js.WidgetName;

import java.util.ArrayList;
import java.util.List;

public class FormModelAdapter extends DesignModelAdapter<Form, BaseJForm> {

	public FormModelAdapter(BaseJForm jForm) {
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
				if (widget instanceof DataControl || widget instanceof MultiFileEditor) {
					list.add(new WidgetName(widget.getWidgetName()));
				}
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
                boolean isSupportAsHypelink = widget.acceptType(ElementCaseEditor.class) || widget.acceptType(ChartEditorProvider.class);
                //可以超链的对象不包含本身; 目前只有图表和报表块可以
                // bug66182 删了条件：!ComparatorUtils.equals(editingECName, widget.getWidgetName())  让当前表单对象可以选到自己
                 if (isSupportAsHypelink){
			    	linkAbleList.add( widget);
                }
			}
		}, Widget.class);
		
		return linkAbleList.toArray(new Widget[linkAbleList.size()]);
	}

	@Override
	public Parameter[] getParameters() {
		return this.getBook().refreshParas();
	}
}