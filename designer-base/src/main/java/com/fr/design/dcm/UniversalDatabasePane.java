package com.fr.design.dcm;

import com.fr.design.dialog.BasicPane;
import com.fr.design.ui.ModernUIPane;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.events.ScriptContextAdapter;
import com.teamdev.jxbrowser.chromium.events.ScriptContextEvent;

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
                .prepare(new ScriptContextAdapter() {
                    @Override
                    public void onScriptContextCreated(ScriptContextEvent event) {
                        JSValue window = event.getBrowser().executeJavaScriptAndReturnValue("window");
                        window.asObject().setProperty("DcmHelper", UniversalDcmBridge.getBridge(event.getBrowser()));
                    }
                })
                .build();
        add(modernUIPane, BorderLayout.CENTER);
    }
}
