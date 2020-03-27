package com.fr.design.dcm;

import com.fr.design.dialog.BasicPane;
import com.fr.design.ui.ModernUIPane;
import com.teamdev.jxbrowser.browser.callback.InjectJsCallback;
import com.teamdev.jxbrowser.js.JsObject;

import java.awt.*;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-05-16
 */
public class UniversalDatabasePane extends BasicPane {

    private ModernUIPane<Object> modernUIPane;

    @Override
    protected String title4PopupWindow() {
        return "Database";
    }

    public UniversalDatabasePane() {
        setLayout(new BorderLayout());
        modernUIPane = new ModernUIPane.Builder<>()
                .withComponent(UniversalDatabaseComponent.KEY)
                .prepare(params -> {
                    JsObject window = params.frame().executeJavaScript("window");
                    if (window != null) {
                        window.putProperty("DcmHelper", UniversalDcmBridge.getBridge());
                    }
                    return InjectJsCallback.Response.proceed();
                }).build();
        add(modernUIPane, BorderLayout.CENTER);
    }
}
