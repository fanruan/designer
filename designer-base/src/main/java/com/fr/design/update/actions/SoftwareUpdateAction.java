package com.fr.design.update.actions;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.update.ui.dialog.UpdateMainDialog;
import com.fr.design.utils.DesignUtils;
import com.fr.stable.os.OperatingSystem;

import java.awt.event.ActionEvent;

/**
 * Created by XINZAI on 2018/8/21.
 */
public class SoftwareUpdateAction extends UpdateAction {

    private static String UPDATE_ROUTE = "#management/backup";
    public SoftwareUpdateAction() {
        setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_UpdateAndUpgrade"));
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/update/update_new.png"));

    }

    /**
     * 事件响应
     *
     * @param e 事件
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //说之后更新升级要用jxbrowser,Linux下进平台
        if(!OperatingSystem.isLinux()) {
            UpdateMainDialog dialog = new UpdateMainDialog(DesignerContext.getDesignerFrame());
            dialog.showDialog();
        }else{
            DesignUtils.visitEnvServerByParameters( UPDATE_ROUTE,null,null);
        }
    }
}

