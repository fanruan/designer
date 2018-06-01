package com.fr.env;


import com.fr.base.FRContext;
import com.fr.base.env.EnvContext;
import com.fr.base.env.resource.EnvConfigUtils;
import com.fr.base.env.resource.RemoteEnvConfig;
import com.fr.core.env.EnvConfig;
import com.fr.core.env.EnvEvents;
import com.fr.dav.LocalEnv;
import com.fr.design.utils.DesignUtils;
import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.Listener;
import com.fr.event.Null;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

import javax.swing.JOptionPane;
import javax.swing.UIManager;


public class SignIn {

    static {
        EventDispatcher.listen(EnvEvents.CONNECTION_ERROR, new Listener<Null>() {
            @Override
            public void on(Event event, Null param) {
                if (JOptionPane.showConfirmDialog(null, Inter.getLocText("FR-Remote_Connect2Server_Again"), UIManager.getString("OptionPane.titleText"), JOptionPane.YES_NO_OPTION)
                        == JOptionPane.OK_OPTION) {
                    try {
                        EnvContext.signIn(EnvContext.currentEnv());
                    } catch (Exception e) {
                        FRContext.getLogger().error(e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 注册入环境
     * @param selectedEnv 选择的环境
     * @throws Exception 异常
     */
    public static void signIn(EnvConfig selectedEnv) throws Exception {
        if (EnvContext.currentEnv() != null && !ComparatorUtils.equals(EnvContext.currentEnv(), selectedEnv)) {
            EnvContext.signOut();
        }
        EnvContext.signIn(selectedEnv);
        DesignUtils.switchToEnv(trans(selectedEnv));
    }

    private static com.fr.base.Env trans(EnvConfig env) {
        if (env instanceof RemoteEnvConfig) {
            return new RemoteEnv(env.getPath(), EnvConfigUtils.getUsername(env), EnvConfigUtils.getPassword(env));
        } else {
            return new LocalEnv();
        }
    }
}