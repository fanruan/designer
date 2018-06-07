package com.fr.env;


import com.fr.core.env.EnvConfig;
import com.fr.core.env.EnvContext;
import com.fr.core.env.EnvEvent;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.DesignUtils;
import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.Listener;
import com.fr.event.Null;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

import javax.swing.*;


public class SignIn {

    static {
        EventDispatcher.listen(EnvEvent.CONNECTION_ERROR, new Listener<Null>() {
            @Override
            public void on(Event event, Null param) {
                JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("Datasource-Connection_failed"));
            }
        });
    }

    /**
     * 注册入环境
     *
     * @param selectedEnv 选择的环境
     * @throws Exception 异常
     */
    public static void signIn(EnvConfig selectedEnv) throws Exception {
    
        if (EnvContext.currentEnv() != null && !ComparatorUtils.equals(EnvContext.currentEnv(), selectedEnv)) {
            EnvContext.signOut();
        }
        DesignUtils.switchToEnv(selectedEnv);
    }
}