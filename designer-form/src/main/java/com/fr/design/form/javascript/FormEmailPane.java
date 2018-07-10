package com.fr.design.form.javascript;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.javascript.EmailPane;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;

/**
 * 表单的邮件pane
 * 
 * @author jim
 *
 */
public class FormEmailPane extends EmailPane{
	
	@Override
	protected void initCenterPane(UILabel mainTextLabel, JScrollPane scrollPane, double fill, double preferred) {
		double[] rowSize = { preferred, preferred, preferred, preferred, preferred, fill, preferred};
        double[] columnSize = { preferred, fill};
        centerPane = TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][]{
                {new UILabel(), tipsPane1},
                createLinePane(Inter.getLocText("HJS-Mail_to"), maitoEditor = new UITextField()),
                createLinePane(Inter.getLocText("HJS-CC_to"), ccEditor = new UITextField()),
                createLinePane(Inter.getLocText("EmailPane-BCC"), bccEditor = new UITextField()),
                createLinePane(Inter.getLocText("EmailPane-mailSubject"), titleEditor = new UITextField()),
                {mainTextLabel, scrollPane},
                {new UILabel(), tipsPane2}},rowSize, columnSize, 7);
	}

}