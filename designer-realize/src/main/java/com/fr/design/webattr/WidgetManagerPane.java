package com.fr.design.webattr;

import com.fr.base.FRContext;
import com.fr.design.gui.frpane.LoadingBasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.widget.WidgetConfigPane;
import com.fr.form.ui.WidgetInfoConfig;

import com.fr.stable.project.ProjectConstants;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class WidgetManagerPane extends LoadingBasicPane {

	private WidgetConfigPane widgetConfigPane;

    @Override
	protected void initComponents(JPanel container) {
        container.setLayout(FRGUIPaneFactory.createBorderLayout());

		widgetConfigPane = new WidgetConfigPane();
		container.add(widgetConfigPane, BorderLayout.CENTER);
    }
    
    @Override
    protected String title4PopupWindow() {
    	return com.fr.design.i18n.Toolkit.i18nText("ServerM-Widget_Manager");
    }

	public void populate(WidgetInfoConfig widgetManager) {
    	//todo  原来界面上显示的xml路径
//		this.widgetTextField.setText(WorkContext.getCurrent().getPath() + File.separator +
//                ProjectConstants.RESOURCES_NAME +
//                File.separator + widgetManager.fileName());
		this.widgetConfigPane.populate(widgetManager);
	}
	
	public void update(WidgetInfoConfig widgetManager) {
		this.widgetConfigPane.update(widgetManager);
	}
}