package com.fr.aspectj.designerbase;

import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.help.alphafine.AlphaFineConfigManager;
import com.fr.design.actions.help.alphafine.RemindDialog;
import com.fr.design.mainframe.DesignerContext;

import java.awt.event.ActionEvent;

/**
 * Created by XiaXiang on 2017/5/25.
 */
public aspect AlphaFineReminder {
    pointcut onActionPerformed(ActionEvent e) :
            execution(* actionPerformed(ActionEvent)) && args(e);

    before(ActionEvent e) : onActionPerformed(e) {
        String point = thisJoinPoint.toString();
        if (e != null && e.getSource().toString().contains("javax.swing.Timer")) {
            return;
        }
        if (e != null && e.getSource().getClass().getName().equals("com.fr.design.gui.imenu.UIMenuItem") && point.contains("com.fr.design.actions")) {
            remind();
        }


    }

    /**
     * 判断是否弹出广告框
     */
    private static void remind() {
        AlphaFineConfigManager manager = DesignerEnvManager.getEnvManager().getAlphaFineConfigManager();

        if (manager.isNeedRemind()) {
            if (manager.getOperateCount() > 4) {
                showReminderDialog();
            } else {
                manager.setOperateCount(manager.getOperateCount() + 1);
            }
        }
    }

    /**
     * 弹框提醒使用AlphaFine
     */
    private static void showReminderDialog() {
        RemindDialog remindDialog = new RemindDialog(DesignerContext.getDesignerFrame());
        remindDialog.setVisible(true);
    }


}
