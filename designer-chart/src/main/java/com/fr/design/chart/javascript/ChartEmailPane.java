package com.fr.design.chart.javascript;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.javascript.EmailPane;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;

import com.fr.js.EmailJavaScript;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author jim
 *
 */
public class ChartEmailPane extends EmailPane{

	private UITextField itemNameTextField;

	@Override
	protected void initCenterPane(UILabel mainTextLabel, JScrollPane scrollPane, double fill, double preferred) {
		double[] columnSize = { preferred, fill};
		itemNameTextField = new UITextField();
		JTemplate jTemplate = DesignerContext.getDesignerFrame().getSelectedJTemplate();
		// 是否支持导出控制图表的超链邮件是否显示showTplContent
		boolean supportExport = jTemplate.isJWorkBook();
		JPanel contentPane;
		if (supportExport) {
			double[] rSizes = { preferred, preferred, preferred, preferred, preferred, fill, preferred, preferred};
			showTplContent = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Email_Can_Preview_Report_Content"));
			contentPane = TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][]{
					{new UILabel(), tipsPane1},
					createLinePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_HJS_Mail_to"), maitoEditor = new UITextField()),
					createLinePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_HJS_CC_To"), ccEditor = new UITextField()),
					createLinePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Email_Pane_BCC"), bccEditor = new UITextField()),
					createLinePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Email_Pane_Mail_Subject"), titleEditor = new UITextField()),
					{mainTextLabel, scrollPane},
					{new UILabel(), showTplContent},
					{new UILabel(), tipsPane2}},rSizes, columnSize, 6);
		} else {
			double[] rSizes = { preferred, preferred, preferred, preferred, preferred, fill, preferred};
			contentPane = TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][]{
                    {new UILabel(), tipsPane1},
                    createLinePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_HJS_Mail_to"), maitoEditor = new UITextField()),
                    createLinePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_HJS_CC_To"), ccEditor = new UITextField()),
                    createLinePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Email_Pane_BCC"), bccEditor = new UITextField()),
                    createLinePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Email_Pane_Mail_Subject"), titleEditor = new UITextField()),
                    {mainTextLabel, scrollPane},
                    {new UILabel(), tipsPane2}},rSizes, columnSize, 8);
		}
		centerPane = new JPanel(new BorderLayout());
		if (needRenamePane()) {
			JPanel namePane = TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][]{{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Name") + ":", SwingConstants.RIGHT), itemNameTextField},},
					new double[] {preferred}, columnSize, 6);
			centerPane.add(namePane, BorderLayout.NORTH);
		}
		centerPane.add(contentPane, BorderLayout.CENTER);
	}

	protected boolean needRenamePane() {
		return true;
	}

	protected void checkEmailConfig(boolean valid) {
		super.checkEmailConfig(valid);
		if(itemNameTextField != null){
            itemNameTextField.setEnabled(valid);
        }
	}

	public void populateBean(EmailJavaScript ob) {
        if(itemNameTextField != null){
            itemNameTextField.setName(ob == null ? null : ob.getItemName());
        }
        super.populateBean(ob);
	}

	public void updateBean(EmailJavaScript email) {
        if(itemNameTextField != null){
            email.setItemName(itemNameTextField.getText());
        }
        super.updateBean(email);
	}
	
}