package com.fr.design.extra.exe.callback;

import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

/**
 * Created by ibm on 2017/5/27.
 */
public class JSCallback {
    private WebEngine webEngine;
    private JSObject callback;

    public JSCallback(final WebEngine webEngine, final JSObject callback) {
        this.webEngine = webEngine;
        this.callback = callback;
    }

    public void execute(String newValue) {
        String fun = "(" + callback + ")(\"" + newValue + "\")";
        try {
            webEngine.executeScript(fun);
        } catch (Exception e) {
            webEngine.executeScript("alert(\"" + e.getMessage() + "\")");
        }
    }


}

