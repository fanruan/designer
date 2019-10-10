package com.fr.design.update.actions;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.os.impl.UpdateDialogAction;
import com.fr.stable.os.support.OSBasedAction;
import com.fr.stable.os.support.OSSupportCenter;

import java.awt.event.ActionEvent;

/**
 * Created by XINZAI on 2018/8/21.
 */
public class SoftwareUpdateAction extends UpdateAction {

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
        OSBasedAction osBasedAction =  OSSupportCenter.getAction(UpdateDialogAction.class);
        osBasedAction.execute();
    }
}

