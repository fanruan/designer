package com.fr.design.webattr;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;


public class ErrorTemplatePane extends BasicBeanPane<String> {
	private UITextField templateField = null;
	
	public ErrorTemplatePane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		
		this.templateField = new UITextField(36);
		
		// TableLayout
		double p = TableLayout.PREFERRED;
		double[] rowSize = {p, p, p, p, p};
        double[] columnSize = {p, p};
        
        JPanel reportletNamePane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        reportletNamePane.add(this.templateField);

        Component[][] components = {
        		{new UILabel(Inter.getLocText("Template_Path") + ":"), reportletNamePane},

        		{new UILabel(Inter.getLocText("Template_Parameters") + ":"), null},
        		{new UILabel("message" + ":"), new UILabel(Inter.getLocText("Verify-Message"))},
        		{new UILabel("charset" + ":"), new UILabel(Inter.getLocText("Server_Charset"))},
        		{new UILabel("exception" + ":"), new UILabel(Inter.getLocText("Exception_StackTrace"))}
        };
        JPanel northPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        
        this.add(northPane, BorderLayout.NORTH);
	}
	
	@Override
	protected String title4PopupWindow() {
		return "ErrorTemplate";
	}
	

	@Override
	public void populateBean(String bean) {
		this.templateField.setText(bean);		
	}

	@Override
	public String updateBean() {
		return this.templateField.getText();
	}
	

}