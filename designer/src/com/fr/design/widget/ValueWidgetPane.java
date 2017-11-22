package com.fr.design.widget;

import javax.swing.*;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.form.ui.DataControl;
import com.fr.form.ui.Widget;
import com.fr.form.ui.WidgetValue;
import com.fr.general.Inter;

import java.awt.*;

public class ValueWidgetPane extends WidgetPane {
	private JPanel widgetValuePane;
	private WidgetValueEditor widgetValueEditor;
	private UILabel label;
	
	@Override
	protected void initComponents(ElementCasePane pane) {
		super.initComponents(pane);
	}

	public JPanel initNorthPane(){
		JPanel northPane = super.initNorthPane();
		label = new UILabel("     " + Inter.getLocText(new String[]{"Widget", "Value"})+ ":");
		label.setVisible(false);
		widgetValuePane = new JPanel();
		widgetValuePane.setLayout(FRGUIPaneFactory.createBorderLayout());
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = {p, p, f};
		double[] rowSize = {p};
		Component[][] components = new Component[][]{
				new Component[]{northPane, label,widgetValuePane},
		};
		JPanel jPanel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
		return jPanel;
	}

	protected CellWidgetCardPane initWidgetCardPane(ElementCasePane pane){
		return new WidgetManageCardPane(pane);
	}

	/**
	 * 初始化所有事件.
	 */
	public void initAllListeners() {

	}

	@Override
	public void populate(Widget widget) {
		super.populate(widget);
		
		populateWidgetValue(widget);
	}
	
	@Override
    protected void populateWidgetConfig(Widget widget) {
    	super.populateWidgetConfig(widget);
    	populateWidgetValue(widget);
    }
	
	private void populateWidgetValue(Widget widget) {
		widgetValuePane.removeAll();
		widgetValueEditor = null;
		label.setVisible(false);
		if (widget instanceof DataControl) {
			label.setVisible(true);
			widgetValueEditor = new WidgetValueEditor(widget, true);
			widgetValueEditor.setValue(((DataControl)widget).getWidgetValue());
			widgetValuePane.add(widgetValueEditor.getCustomEditor());
		}
	}
	
	@Override
	public Widget update() {
		Widget widget = super.update();
		
		if (widget instanceof DataControl && widgetValueEditor != null) {
			((DataControl)widget).setWidgetValue((WidgetValue)widgetValueEditor.getValue());
		}
		return widget;
	}
}