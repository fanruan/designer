package com.fr.design.mainframe.loghandler.socketio;

import com.fr.base.env.EnvContext;
import com.fr.base.env.resource.LocalEnvConfig;
import com.fr.core.env.EnvConfig;
import com.fr.core.env.EnvConstants;
import com.fr.core.env.EnvEvents;
import com.fr.decision.webservice.utils.DecisionServiceConstants;
import com.fr.design.mainframe.loghandler.DesignerLogHandler;
import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.Listener;
import com.fr.event.Null;
import com.fr.general.LogRecordTime;
import com.fr.general.LogUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.third.guava.base.Optional;
import com.fr.web.WebSocketConfig;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URL;

public class DesignerSocketIO {

    private static Optional<Socket> socketIO = Optional.absent();

    private static final Emitter.Listener printLog = new Emitter.Listener() {
        @Override
        public void call(Object... objects) {
            //TODO 这里要测试一下类型
            String object = (String) objects[0];
            try {
                LogRecordTime[] logRecordTimes = LogUtils.readXMLLogRecords(new ByteArrayInputStream(object.getBytes()));
                for (LogRecordTime logRecordTime : logRecordTimes) {
                    DesignerLogHandler.getInstance().printRemoteLog(logRecordTime);
                }
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
    };

    static {
        EventDispatcher.listen(EnvEvents.AFTER_SIGN_OUT, new Listener<Null>() {
            @Override
            public void on(Event event, Null param) {
                if (socketIO.isPresent()) {
                    socketIO.get().close();
                    socketIO = Optional.absent();
                }
            }
        });
        EventDispatcher.listen(EnvEvents.AFTER_SIGN_IN, new Listener<Null>() {
            @Override
            public void on(Event event, Null param) {
                updateSocket();
            }
        });
    }

    public static void init() {
        updateSocket();
    }

    private static void updateSocket() {
        EnvConfig env = EnvContext.currentEnv();
        if (env instanceof LocalEnvConfig) {
            return;
        }

        try {
            String uri = String.format("http://%s:%s/%s?%s=%s",
                    new URL(env.getPath()).getHost(),
                    WebSocketConfig.getInstance().getPort(),
                    EnvConstants.WS_NAMESPACE,
                    DecisionServiceConstants.WEB_SOCKET_TOKEN_NAME,
                    EnvContext.currentToken());

            socketIO = Optional.of(IO.socket(new URI(uri)));
            socketIO.get().on(EnvConstants.WS_LOGRECORD, printLog);
            socketIO.get().connect();
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }
}