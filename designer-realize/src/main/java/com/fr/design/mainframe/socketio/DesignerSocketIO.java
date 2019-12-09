package com.fr.design.mainframe.socketio;

import com.fr.config.RemoteConfigEvent;
import com.fr.decision.webservice.utils.DecisionServiceConstants;
import com.fr.design.EnvChangeEntrance;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.loghandler.DesignerLogger;
import com.fr.design.ui.util.UIUtil;
import com.fr.event.EventDispatcher;
import com.fr.log.FineLoggerFactory;
import com.fr.report.RemoteDesignConstants;
import com.fr.serialization.SerializerHelper;
import com.fr.stable.ArrayUtils;
import com.fr.third.apache.log4j.spi.LoggingEvent;
import com.fr.workspace.WorkContext;
import com.fr.workspace.Workspace;
import com.fr.workspace.base.WorkspaceConstants;
import com.fr.workspace.connect.WorkspaceConnection;
import com.fr.workspace.server.socket.SocketInfoOperator;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

public class DesignerSocketIO {

    enum Status {
        Connected,
        Disconnected,
        Disconnecting
    }

    private static Socket socket = null;
    private static Status status = Status.Disconnected;
    //维护一个当前工作环境的uri列表
    private static String[] uri;
    //维护一个关于uri列表的计数器
    private static int count;


    public static void close() {
        if (socket != null) {
            status = Status.Disconnecting;
            socket.close();
            socket = null;
        }
    }

    public static void update() {
        Workspace current = WorkContext.getCurrent();
        if (current.isLocal()) {
            return;
        }
        //每当更换工作环境，更新uri列表，同时更新计数器count
        try {
            uri = getSocketUri();
        } catch (IOException e) {
            e.printStackTrace();
        }
        count = 0;
        //建立socket并注册监听
        CreateSocket();
    }

    private static void CreateSocket(){
        //根据uri和计数器建立连接，并注册监听
        try {
            if(count<uri.length) {
                socket = IO.socket(new URI(uri[count]));
                socket.on(WorkspaceConstants.WS_LOGRECORD, printLog);
                socket.on(WorkspaceConstants.CONFIG_MODIFY, modifyConfig);
                socket.on(Socket.EVENT_CONNECT_ERROR, failRetry);
                socket.on(Socket.EVENT_DISCONNECT, disConnectHint);
                socket.connect();
                status = Status.Connected;
            }else {
                //表示所有的uri都连接不成功
                FineLoggerFactory.getLogger().warn("所有URI均连接失败");
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    private static String[] getSocketUri() throws IOException {
        Workspace current = WorkContext.getCurrent();
        URL url = new URL(current.getPath());
        Integer[] ports = current.get(SocketInfoOperator.class).getPort();
        WorkspaceConnection connection = current.getConnection();
        String[] result = new String[ports.length];
        for (int i = 0; i < ports.length; i++) {
            result[i] = String.format("%s://%s:%s%s?%s=%s&%s=%s",
                    url.getProtocol(),
                    url.getHost(),
                    ports[i],
                    WorkspaceConstants.WS_NAMESPACE,
                    DecisionServiceConstants.WEB_SOCKET_TOKEN_NAME,
                    connection.getToken(),
                    RemoteDesignConstants.USER_LOCK_ID,
                    connection.getId());
        }
        return result;
    }

    //失败重试监听器：1、关闭失败的socket 2、计数器加1 3、调用创建socket方法
    private static final Emitter.Listener failRetry = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            status = Status.Disconnecting;
            socket.close();
            count++;
            CreateSocket();
        }
    };

    //日志输出监听器
    private static final Emitter.Listener printLog = new Emitter.Listener() {
        @Override
        public void call(Object... objects) {
            if (ArrayUtils.isNotEmpty(objects)) {
                try {
                    LoggingEvent event = SerializerHelper.deserialize((byte[]) objects[0]);
                    DesignerLogger.log(event);
                } catch (Exception e) {
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                }
            }
        }
    };

    //断开连接提醒监听器
    private static final Emitter.Listener disConnectHint = new Emitter.Listener() {
        @Override
        public void call(Object... objects) {
            /*
            * todo 远程心跳断开不一定 socketio 断开 和远程紧密相关的业务都绑定在心跳上，切换成心跳断开之后进行提醒，
            * socket 只用推日志和通知配置变更
            */
            if (status != Status.Disconnecting) {
                try {
                    UIUtil.invokeAndWaitIfNeeded(new Runnable() {
                    @Override
                        public void run() {
                            JOptionPane.showMessageDialog(
                            DesignerContext.getDesignerFrame(),
                            Toolkit.i18nText("Fine-Design_Basic_Remote_Disconnected"),
                            UIManager.getString("OptionPane.messageDialogTitle"),
                            JOptionPane.ERROR_MESSAGE,
                            UIManager.getIcon("OptionPane.errorIcon"));
                            EnvChangeEntrance.getInstance().chooseEnv();
                        }
                    });
                } catch (Exception e) {
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                }
            }
            status = Status.Disconnected;
        }
    };

    //配置变更监听器
    private static final Emitter.Listener modifyConfig = new Emitter.Listener() {
        @Override
        public void call(Object... objects) {
            assert objects != null && objects.length == 1;
            String param = (String) objects[0];
            EventDispatcher.fire(RemoteConfigEvent.EDIT, param);
        }
    };

}
