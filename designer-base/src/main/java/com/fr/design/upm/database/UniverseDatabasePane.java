package com.fr.design.upm.database;

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
public class UniverseDatabasePane extends BasicPane {

    private ModernUIPane<Object> modernUIPane;

    @Override
    protected String title4PopupWindow() {
        return "Database";
    }

    public UniverseDatabasePane() {
        setLayout(new BorderLayout());
        modernUIPane = new ModernUIPane.Builder<Object>()
                .withComponent(UniverseDatabaseComponent.KEY)
                .prepare(new ScriptContextAdapter() {
                    @Override
                    public void onScriptContextCreated(ScriptContextEvent event) {
                        JSValue window = event.getBrowser().executeJavaScriptAndReturnValue("window");
                    }
                })
                .build();
        add(modernUIPane, BorderLayout.CENTER);
    }
}
