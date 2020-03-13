package com.fr.common.detect;

import com.fr.concurrent.NamedThreadFactory;
import com.fr.design.DesignerEnvManager;
import com.fr.log.FineLoggerFactory;
import com.fr.module.ModuleContext;
import com.fr.web.WebSocketConfig;

import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/3/10
 */
public class CommonPortDetector {

    private static final CommonPortDetector INSTANCE = new CommonPortDetector();
    private ExecutorService service = ModuleContext.getExecutor().newSingleThreadScheduledExecutor(new NamedThreadFactory("CommonDetector"));

    public static CommonPortDetector getInstance() {
        return INSTANCE;
    }

    public void execute() {
        service.submit(new Runnable() {
            @Override
            public void run() {
                detectTomcatPort();
                detectWebSocketPort();
            }
        });
    }

    private void detectTomcatPort() {
        int port = DesignerEnvManager.getEnvManager().getEmbedServerPort();
        if (checkPort(port)) {
            FineLoggerFactory.getLogger().error("EmbedTomcat Port: {} is not available, maybe occupied by other programs, please check it!", port);
        }
    }

    private void detectWebSocketPort() {
        Integer[] ports = WebSocketConfig.getInstance().getPort();
        for (int port : ports) {
            if (checkPort(port)) {
                FineLoggerFactory.getLogger().error("WebSocKet Port: {} is not available, maybe occupied by other programs, please check it!", port);
            }
        }
    }

    private boolean checkPort(int port) {
        try (Socket socket = new Socket("localhost", port)) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
