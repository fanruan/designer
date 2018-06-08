package com.fr.design.mainframe.loghandler.socketio;

import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.config.ConfigEvent;
import com.fr.config.Configuration;
import com.fr.core.env.EnvConfig;
import com.fr.core.env.EnvConstants;
import com.fr.core.env.EnvContext;
import com.fr.core.env.EnvEvent;
import com.fr.core.env.impl.LocalEnvConfig;
import com.fr.decision.webservice.utils.DecisionServiceConstants;
import com.fr.design.env.RemoteEnvConfig;
import com.fr.design.mainframe.loghandler.DesignerLogHandler;
import com.fr.env.RemoteEnv;
import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.Listener;
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
            try {
                LogRecordTime[] logRecordTimes = LogUtils.readXMLLogRecords(new ByteArrayInputStream((byte[]) objects[0]));
                for (LogRecordTime logRecordTime : logRecordTimes) {
                    DesignerLogHandler.getInstance().printRemoteLog(logRecordTime);
                }
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
    };

    static {
        EventDispatcher.listen(EnvEvent.AFTER_SIGN_OUT, new Listener<EnvConfig>() {
            @Override
            public void on(Event event, EnvConfig param) {
                if (socketIO.isPresent()) {
                    socketIO.get().close();
                    socketIO = Optional.absent();
                }
            }
        });
        EventDispatcher.listen(EnvEvent.AFTER_SIGN_IN, new Listener<EnvConfig>() {
            @Override
            public void on(Event event, EnvConfig param) {
                updateSocket();
            }
        });
    }

    public static void init() {
        updateSocket();
    }

    private static void updateSocket() {
        Env env = FRContext.getCurrentEnv();
        if (env.isLocalEnv()) {
            return;
        }
        try {
            RemoteEnvConfig config = ((RemoteEnv)env).getEnvConfig();
            String uri = String.format("http://%s:%s%s?%s=%s",
                    config.getHost(),
                    WebSocketConfig.getInstance().getPort(),
                    EnvConstants.WS_NAMESPACE,
                    DecisionServiceConstants.WEB_SOCKET_TOKEN_NAME,
                    EnvContext.currentToken());

            socketIO = Optional.of(IO.socket(new URI(uri)));
            socketIO.get().on(EnvConstants.WS_LOGRECORD, printLog);
//            socketIO.get().on(EnvConstants.CONFIG, new Emitter.Listener() {
//                @Override
//                public void call(Object... objects) {
//                    if (objects == null || objects.length != 1) {
//                        throw new IllegalArgumentException("config should have only one param");
//                    }
//                    Object param = objects[0];
//                    if (param instanceof Class) {
//                        EventDispatcher.fire(ConfigEvent.EDIT, (Class<? extends Configuration>) param);
//                    }
//                }
//            });
            socketIO.get().connect();
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }
}