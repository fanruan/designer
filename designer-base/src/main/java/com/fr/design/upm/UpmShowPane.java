package com.fr.design.upm;

import com.fr.design.dialog.BasicPane;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.ui.ModernUIPane;
import com.fr.design.update.domain.UpdateConstants;
import com.fr.design.update.ui.dialog.UpdateMainDialog;
import com.fr.design.upm.event.DownloadEvent;
import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.Listener;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StableUtils;
import com.fr.stable.project.ProjectConstants;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.events.ScriptContextAdapter;
import com.teamdev.jxbrowser.chromium.events.ScriptContextEvent;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-04-12
 * Update Plugin Manager容器
 */
public class UpmShowPane extends BasicPane {

    private ModernUIPane<Object> modernUIPane;

    @Override
    protected String title4PopupWindow() {
        return "UPM";
    }

    UpmShowPane() {
        setLayout(new BorderLayout());
        boolean flag = false;
        File file = new File(StableUtils.pathJoin(StableUtils.getInstallHome(),ProjectConstants.LIB_NAME));
        File[] files = file.listFiles();
        if (files != null) {
            for (File file1 : files) {
                if (file1.getName().contains(UpdateConstants.JXBROWSER)) {
                    flag = true;
                    break;
                }
            }
        }else {
            FineLoggerFactory.getLogger().error("Designer lib can not be null");
        }
        if (flag) {
            if (UpmFinder.checkUPMResourcesExist()) {
                modernUIPane = new ModernUIPane.Builder<>()
                        .prepare(new ScriptContextAdapter() {
                            @Override
                            public void onScriptContextCreated(ScriptContextEvent event) {
                                JSValue window = event.getBrowser().executeJavaScriptAndReturnValue("window");
                                window.asObject().setProperty("PluginHelper", UpmBridge.getBridge(event.getBrowser()));
                            }
                        })
                        .withURL(UpmFinder.getMainResourcePath(), UpmUtils.renderMap())
                        .build();
                EventDispatcher.listen(DownloadEvent.UPDATE, new Listener<String>() {
                    @Override
                    public void on(Event event, String param) {
                        modernUIPane.redirect(UpmFinder.getMainResourcePath(), UpmUtils.renderMap());
                    }
                });
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
                EventDispatcher.listen(DownloadEvent.SUCCESS, new Listener<String>() {
                    @Override
                    public void on(Event event, String param) {
                        modernUIPane.redirect(UpmFinder.getMainResourcePath(), UpmUtils.renderMap());
                    }
                });
            }
        } else {
            JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Toolkit.i18nText("Fine-Design_Update_Info_Plugin_Message"));
            UpdateMainDialog dialog = new UpdateMainDialog(DesignerContext.getDesignerFrame());
            dialog.setAutoUpdateAfterInit();
            dialog.showDialog();
        }
        add(modernUIPane, BorderLayout.CENTER);
    }

}
