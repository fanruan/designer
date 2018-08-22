package com.fr.design.onlineupdate.actions;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.onlineupdate.ui.dialog.UpdateMainDialog;
import com.fr.locale.InterProviderFactory;

import java.awt.event.ActionEvent;

/**
 * Created by XINZAI on 2018/8/21.
 */
public class SoftwareUpdateAction extends UpdateAction {


    public SoftwareUpdateAction() {
        setName(com.fr.design.i18n.Toolkit.i18nText("FR-Desinger-Updater_UpdateAndUpgrade"));
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/update/update_new.png"));

    }

    /**
     * 事件响应
     *
     * @param e 事件
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        UpdateMainDialog dialog = new UpdateMainDialog(DesignerContext.getDesignerFrame());
        dialog.showDialog();
    }
}

