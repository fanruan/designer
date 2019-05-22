package com.fr.design.upm.exec;

import com.fr.design.bridge.exec.JSExecutor;
import com.teamdev.jxbrowser.chromium.JSFunction;
import com.teamdev.jxbrowser.chromium.JSObject;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-04-18
 */
public class UpmBrowserExecutor implements JSExecutor {

    public static UpmBrowserExecutor create(JSObject window, JSFunction callback) {
        return new UpmBrowserExecutor(window, callback);
    }

    private JSObject window;
    private JSFunction callback;

    private UpmBrowserExecutor(JSObject window, JSFunction callback) {
        this.window = window;
        this.callback = callback;
    }

    @Override
    public void executor(String newValue) {
        callback.invoke(window, newValue);
    }
}
