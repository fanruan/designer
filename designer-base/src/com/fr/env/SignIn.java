package com.fr.env;


import com.fr.base.operator.connect.ConnectOperator;
import com.fr.common.rpc.RemoteCallServerConfig;
import com.fr.common.rpc.netty.RemoteCallClient;
import com.fr.common.rpc.serialize.SerializeProtocol;
import com.fr.core.env.EnvConfig;
import com.fr.core.env.EnvContext;
import com.fr.core.env.EnvEvent;
import com.fr.core.env.proxy.EnvMessageSendProxy;
import com.fr.core.env.resource.RemoteEnvConfig;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.DesignUtils;
import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.Listener;
import com.fr.event.Null;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.third.guava.reflect.Reflection;

import javax.swing.JOptionPane;


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
        SerializeProtocol serializeProtocol = RemoteCallServerConfig.getInstance().getSerializeProtocol();

        if (EnvContext.currentEnv() != null && !ComparatorUtils.equals(EnvContext.currentEnv(), selectedEnv)) {
            EnvContext.signOut();
        }
        if (selectedEnv instanceof RemoteEnvConfig) {
            RemoteCallClient.getInstance().load(
                    ((RemoteEnvConfig) selectedEnv).getHost(),
                    ((RemoteEnvConfig) selectedEnv).getPort(),
                    serializeProtocol);
        }
        DesignUtils.switchToEnv(selectedEnv);
    }

    public static void main(String[] args) {
        RemoteCallClient.getInstance().load(
                "127.0.0.1",
                39999,
                SerializeProtocol.KRYOSERIALIZE);
        ConnectOperator connectOperator = Reflection.newProxy(ConnectOperator.class, new EnvMessageSendProxy());
        connectOperator.connect("1", "1");
    }
}