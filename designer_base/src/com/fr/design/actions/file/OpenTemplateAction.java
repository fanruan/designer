/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.file;

import java.awt.event.ActionEvent;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.menu.KeySetUtils;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;


/**
 * Open Template.
 */
public class OpenTemplateAction extends UpdateAction {
	
    public OpenTemplateAction() {
        this.setMenuKeySet(KeySetUtils.OPEN_TEMPLATE);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/buttonicon/open.png"));
        this.setAccelerator(getMenuKeySet().getKeyStroke());
    }

    /**
     * 动作
     * @param evt 事件
     */
    public void actionPerformed(ActionEvent evt) {
        FILEChooserPane fileChooser = FILEChooserPane.getInstance(true, true);

        if (fileChooser.showOpenDialog(DesignerContext.getDesignerFrame())
                == FILEChooserPane.OK_OPTION) {
            final FILE file = fileChooser.getSelectedFILE();
            if (file == null) {//选择的文件不能是 null
                return;
            }
            DesignerContext.getDesignerFrame().openTemplate(file);
        }
    }
}