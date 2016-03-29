package com.fr.design.widget.ui;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.form.ui.NoneWidget;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.*;
import java.awt.*;

public class BasicWidgetPropertySettingPane extends BasicPane {
	private ParameterTreeComboBox widgetNameComboBox;
	private UICheckBox enableCheckBox;
	private UICheckBox visibleCheckBox;
	private Widget widget;
	
	public BasicWidgetPropertySettingPane() {
		this.setLayout(FRGUIPaneFactory.createLabelFlowLayout());
		JPanel pane1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
		pane1.setBorder(BorderFactory.createEmptyBorder(0, -2, 0, 0));
		this.add(pane1);
		
		JPanel pane2 = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
		enableCheckBox = new UICheckBox(Inter.getLocText("Enabled"), true);
		pane2.add(enableCheckBox);
		visibleCheckBox = new UICheckBox(Inter.getLocText("Widget-Visible"), true);
		pane2.add(visibleCheckBox);
		pane1.add(pane2, BorderLayout.NORTH);
		
		JPanel pane3 = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
		widgetNameComboBox = new ParameterTreeComboBox();
		widgetNameComboBox.refreshTree();
		pane3.add(new UILabel(Inter.getLocText("Form-Widget_Name") + ":"));
		pane3.add(widgetNameComboBox);
		pane1.add(pane3, BorderLayout.CENTER);
		
	}
	
	@Override
	protected String title4PopupWindow() {
		return "property";
	}
	
	public void populate(Widget widget){
		//:jackie
		if(widget instanceof NoneWidget){
			this.widget = null;
			GUICoreUtils.setEnabled(this, false);
			return;
		} else{
			GUICoreUtils.setEnabled(this, true);
			this.widget = widget;
			widgetNameComboBox.setSelectedItem(widget.getWidgetName());
			enableCheckBox.setSelected(this.widget.isEnabled());
			visibleCheckBox.setSelected(this.widget.isVisible());
		}
	}
	
	public void update(Widget widget){
		if(this.widget == null)
			return ;
		widget.setWidgetName(widgetNameComboBox.getSelectedParameterName());
		widget.setEnabled(enableCheckBox.isSelected());
		widget.setVisible(visibleCheckBox.isSelected());
	}
}