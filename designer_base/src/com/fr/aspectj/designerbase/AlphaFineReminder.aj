package com.fr.aspectj.designerbase;

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
            //System.out.print(thisJoinPoint + "\n");
        }


    }
}
