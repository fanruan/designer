package com.fr.design.extra;

import com.fr.design.bridge.exec.JSExecutor;
import com.fr.design.bridge.exec.JSUtils;
import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-04-18
 */
public class PluginJavaFxExecutor implements JSExecutor {

    public static PluginJavaFxExecutor create(WebEngine webEngine, JSObject callback) {
        return new PluginJavaFxExecutor(webEngine, callback);
    }

    private WebEngine webEngine;
    private JSObject callback;

    private PluginJavaFxExecutor(WebEngine webEngine, JSObject callback) {
        this.webEngine = webEngine;
        this.callback = callback;
    }

    @Override
    public void executor(final String newValue) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String fun = "(" + callback + ")(\"" + JSUtils.trimText(newValue) + "\")";
                try {
                    webEngine.executeScript(fun);
                } catch (Exception e) {
                    webEngine.executeScript("alert(\"" + e.getMessage() + "\")");
                }
            }
        });
    }
}
