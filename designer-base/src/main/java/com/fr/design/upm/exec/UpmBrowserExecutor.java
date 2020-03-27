package com.fr.design.upm.exec;

import com.fr.design.bridge.exec.JSExecutor;
import com.teamdev.jxbrowser.js.JsObject;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-04-18
 */
public class UpmBrowserExecutor implements JSExecutor {

    public static UpmBrowserExecutor create(JsObject callback) {
        return new UpmBrowserExecutor(callback);
    }

    private JsObject callback;

    private UpmBrowserExecutor(JsObject callback) {
        this.callback = callback;
    }

    @Override
    public void executor(String newValue) {
        callback.call(JSExecutor.CALLBACK_FUNCTION_NAME, newValue);
    }
}
