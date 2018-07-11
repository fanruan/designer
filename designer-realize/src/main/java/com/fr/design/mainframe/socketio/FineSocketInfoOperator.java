package com.fr.design.mainframe.socketio;

import com.fr.web.WebSocketConfig;

public class FineSocketInfoOperator implements SocketInfoOperator {

    @Override
    public int getPort() {
        return WebSocketConfig.getInstance().getPort();
    }
}
