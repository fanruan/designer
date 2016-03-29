package com.fr.design.chart.javascript;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.javascript.EmailPane;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.general.Inter;
import com.fr.js.EmailJavaScript;

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
		if (supportExport) {
			double[] rSizes = { preferred, preferred, preferred, preferred, preferred, preferred, fill, preferred, preferred};
			showTplContent = new UICheckBox(Inter.getLocText("Email-Can_Preview_Report_Content"));
			centerPane = TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][]{
					{new UILabel(Inter.getLocText("Name") + ":", SwingConstants.RIGHT), itemNameTextField},
					{new UILabel(), tipsPane1},
					createLinePane(Inter.getLocText("HJS-Mail_to"), maitoEditor = new UITextField()),
					createLinePane(Inter.getLocText("HJS-CC_to"), ccEditor = new UITextField()),
					createLinePane(Inter.getLocText("EmailPane-BCC"), bccEditor = new UITextField()),
					createLinePane(Inter.getLocText("EmailPane-mailSubject"), titleEditor = new UITextField()),
					{mainTextLabel, scrollPane},
					{new UILabel(), showTplContent},
					{new UILabel(), tipsPane2}},rSizes, columnSize, 6);
		} else {
			double[] rSizes = { preferred, preferred, preferred, preferred, preferred, preferred, fill, preferred};
			centerPane = TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][]{
                    {new UILabel(Inter.getLocText("Name") + ":", SwingConstants.RIGHT), itemNameTextField},
                    {new UILabel(), tipsPane1},
                    createLinePane(Inter.getLocText("HJS-Mail_to"), maitoEditor = new UITextField()),
                    createLinePane(Inter.getLocText("HJS-CC_to"), ccEditor = new UITextField()),
                    createLinePane(Inter.getLocText("EmailPane-BCC"), bccEditor = new UITextField()),
                    createLinePane(Inter.getLocText("EmailPane-mailSubject"), titleEditor = new UITextField()),
                    {mainTextLabel, scrollPane},
                    {new UILabel(), tipsPane2}},rSizes, columnSize, 8);
		}
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