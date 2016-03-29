package com.fr.design.actions.file.newReport;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JPolyWorkBook;
import com.fr.general.Inter;

public class NewPolyReportAction extends UpdateAction {

    public NewPolyReportAction() {
        this.setName(Inter.getLocText("M-New_Multi_Report"));
        this.setMnemonic('F');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/formExport.png"));
        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_MASK));
    }

    /**
	 * Action触发后
	 * 
	 * @param e 触发的事件
	 * 
	 * @date 2015-2-5-上午11:43:13
	 * 
	 */
    public void actionPerformed(ActionEvent e) {
        DesignerContext.getDesignerFrame().addAndActivateJTemplate(new JPolyWorkBook());
    }
}