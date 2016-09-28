package com.fr.design.extra;

import javafx.scene.web.WebEngine;

/**
 * Created by vito on 2016/9/28.
 */
public class ReuseWebBridge {
    public static ReuseWebBridge helper;
    private WebEngine webEngine;

    public static ReuseWebBridge getHelper() {
        if (helper != null) {
            return helper;
        }
        synchronized (ReuseWebBridge.class) {
            if (helper == null) {
                helper = new ReuseWebBridge();
            }
            return helper;
        }
    }

    public static ReuseWebBridge getHelper(WebEngine webEngine) {
        getHelper();
        helper.setEngine(webEngine);
        return helper;
    }

    private ReuseWebBridge() {
    }

    public void setEngine(WebEngine webEngine) {
        this.webEngine = webEngine;
    }
}
