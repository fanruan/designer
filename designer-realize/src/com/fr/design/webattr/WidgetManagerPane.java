package com.fr.design.webattr;

import com.fr.base.FRContext;
import com.fr.design.gui.frpane.LoadingBasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.widget.WidgetConfigPane;
import com.fr.form.ui.WidgetInfoConfig;
import com.fr.general.Inter;
import com.fr.stable.project.ProjectConstants;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class WidgetManagerPane extends LoadingBasicPane {
	private UITextField widgetTextField;
	private WidgetConfigPane widgetConfigPane;

    @Override
	protected void initComponents(JPanel container) {
        container.setLayout(FRGUIPaneFactory.createBorderLayout());

		JPanel widgetPathPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
		container.add(widgetPathPane, BorderLayout.NORTH);


		widgetPathPane.add(new UILabel(Inter.getLocText("FR-Designer_Save_Path") + ":"), BorderLayout.WEST);
		this.widgetTextField = new UITextField();
		widgetPathPane.add(widgetTextField, BorderLayout.CENTER);
		this.widgetTextField.setEditable(false);

		widgetConfigPane = new WidgetConfigPane();
		container.add(widgetConfigPane, BorderLayout.CENTER);
    }
    
    @Override
    protected String title4PopupWindow() {
    	return Inter.getLocText("ServerM-Widget_Manager");
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