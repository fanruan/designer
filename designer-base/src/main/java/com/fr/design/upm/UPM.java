package com.fr.design.upm;

import com.fr.base.FRContext;
import com.fr.design.dialog.UIDialog;
import com.fr.design.mainframe.DesignerContext;
import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.Listener;
import com.fr.stable.StableUtils;
import com.fr.workspace.Workspace;
import com.fr.workspace.WorkspaceEvent;

import javax.swing.*;
import java.io.File;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-04-12
 */
public class UPM {

    private static final String MAIN_RESOURCE_PATH = "/upm/plugin.html";

    public static String installHome = FRContext.getCommonOperator().getWebRootPath();

    private static UIDialog dialog = null;

    static {
        EventDispatcher.listen(WorkspaceEvent.AfterSwitch, new Listener<Workspace>() {
            @Override
            public void on(Event event, Workspace param) {
                installHome = FRContext.getCommonOperator().getWebRootPath();
            }
        });
    }

    public static boolean checkUPMResourcesExist() {
        String mainJsPath = StableUtils.pathJoin(installHome, MAIN_RESOURCE_PATH);
        File file = new File(mainJsPath);
        return file.exists();
    }

    public static String getMainResourcePath() {
        return "file:///" + StableUtils.pathJoin(installHome, MAIN_RESOURCE_PATH);
    }

    public static void showUPMDialog() {
        UPMPane upmPane = new UPMPane();
        if (dialog == null) {
            dialog = new UPMDialog(DesignerContext.getDesignerFrame(), upmPane);
        }
        dialog.setVisible(true);
    }

    public static void closeWindow() {
        if (dialog != null) {
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialog.setVisible(false);
            dialog = null;
        }
    }
}
