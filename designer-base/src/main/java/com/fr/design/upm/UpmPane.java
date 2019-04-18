package com.fr.design.upm;

import com.fr.base.TemplateUtils;
import com.fr.design.dialog.BasicPane;
import com.fr.design.ui.ModernUIPane;
import com.fr.design.upm.event.DownloadEvent;
import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.Listener;
import com.fr.general.GeneralContext;
import com.fr.general.IOUtils;
import com.fr.stable.StringUtils;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.events.ScriptContextAdapter;
import com.teamdev.jxbrowser.chromium.events.ScriptContextEvent;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-04-12
 * Update Plugin Manager容器
 */
public class UpmPane extends BasicPane {

    private ModernUIPane<Object> modernUIPane;

    @Override
    protected String title4PopupWindow() {
        return "UPM";
    }

    public UpmPane() {
        setLayout(new BorderLayout());
        if (UpmFinder.checkUPMResourcesExist()) {
            modernUIPane = new ModernUIPane.Builder<>()
                    .prepare(new ScriptContextAdapter() {
                        @Override
                        public void onScriptContextCreated(ScriptContextEvent event) {
                            JSValue window = event.getBrowser().executeJavaScriptAndReturnValue("window");
                            window.asObject().setProperty("PluginHelper", UpmBridge.getBridge(event.getBrowser()));
                        }
                    })
                    .withURL(UpmFinder.getMainResourcePath())
                    .build();
        } else {
            modernUIPane = new ModernUIPane.Builder<>()
                    .withComponent(WarnComponent.KEY)
                    .prepare(new ScriptContextAdapter() {
                        @Override
                        public void onScriptContextCreated(ScriptContextEvent event) {
                            JSValue window = event.getBrowser().executeJavaScriptAndReturnValue("window");
                            window.asObject().setProperty("PluginHelper", UpmBridge.getBridge(event.getBrowser()));
                        }
                    }).build();
            EventDispatcher.listen(DownloadEvent.FINISH, new Listener<String>() {
                @Override
                public void on(Event event, String param) {
                    modernUIPane.redirect(UpmFinder.getMainResourcePath());
                }
            });
        }
        add(modernUIPane, BorderLayout.CENTER);
    }

}
