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
import com.fr.third.guava.base.Optional;
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

    private static Optional<Socket> socketIO = Optional.absent();
    private static Status status = Status.Disconnected;

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

    public static void close() {
        if (socketIO.isPresent()) {
            status = Status.Disconnecting;
            socketIO.get().close();
            socketIO = Optional.absent();
        }
    }

    public static void update() {
        Workspace current = WorkContext.getCurrent();
        if (current.isLocal()) {
            return;
        }
        try {
            String[] uri = getSocketUri(current);
            socketIO = Optional.of(IO.socket(new URI(uri[0])));
            socketIO.get().on(WorkspaceConstants.WS_LOGRECORD, printLog);
            socketIO.get().on(WorkspaceConstants.CONFIG_MODIFY, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    assert objects != null && objects.length == 1;
                    String param = (String) objects[0];
                    EventDispatcher.fire(RemoteConfigEvent.EDIT, param);
                }
            });
            socketIO.get().on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    /*
                     * todo 远程心跳断开不一定 socketio 断开 和远程紧密相关的业务都绑定在心跳上，切换成心跳断开之后进行提醒，
                     * socketio 只用推日志和通知配置变更
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
            });
            socketIO.get().connect();
            status = Status.Connected;
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    private static String[] getSocketUri(Workspace current) throws IOException {
        URL url = new URL(current.getPath());
        Integer[] ports = WorkContext.getCurrent().get(SocketInfoOperator.class).getPort();
        WorkspaceConnection connection = WorkContext.getCurrent().getConnection();
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
}
