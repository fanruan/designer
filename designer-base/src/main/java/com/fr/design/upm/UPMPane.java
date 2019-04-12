package com.fr.design.upm;

import com.fr.base.FRContext;
import com.fr.design.dialog.BasicPane;
import com.fr.design.ui.ModernUIPane;
import com.fr.design.upm.event.DownloadEvent;
import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.Listener;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.events.ScriptContextAdapter;
import com.teamdev.jxbrowser.chromium.events.ScriptContextEvent;

import java.awt.*;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-04-12
 * Update Plugin Manager容器
 */
public class UPMPane extends BasicPane {

    private ModernUIPane<Object> modernUIPane;

    @Override
    protected String title4PopupWindow() {
        return "UPM";
    }

    public UPMPane() {
        setLayout(new BorderLayout());
        if (false) {
            modernUIPane = new ModernUIPane.Builder<>()
                    .withURL(UPM.getMainResourcePath())
                    .build();
        } else {
            modernUIPane = new ModernUIPane.Builder<>()
                    .withComponent(WarnComponent.KEY)
                    .prepare(new ScriptContextAdapter() {
                        @Override
                        public void onScriptContextCreated(ScriptContextEvent event) {
                            JSValue window = event.getBrowser().executeJavaScriptAndReturnValue("window");
                            window.asObject().setProperty("PluginBridgeTest", UPMBridge.getBridge());
                        }
                    }).build();
            EventDispatcher.listen(DownloadEvent.FINISH, new Listener<String>() {
                @Override
                public void on(Event event, String param) {
                    modernUIPane.redirect(UPM.getMainResourcePath());
                }
            });
        }
        add(modernUIPane, BorderLayout.CENTER);
    }

}
