package com.fr.design.upm;

import com.fr.base.FRContext;
import com.fr.decision.webservice.v10.plugin.helper.category.impl.UpmResourceLoader;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.dialog.UIDialog;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.update.ui.dialog.UpdateMainDialog;
import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.Listener;
import com.fr.log.FineLoggerFactory;
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
public class UpmFinder {

    private static final String UPM_DIR = "/upm";
    private static final String MAIN_RESOURCE_PATH = UPM_DIR + "/plugin_design.html";

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

    public static UIDialog getDialog() {
        return dialog;
    }

    public static void showUPMDialog() {
        boolean flag = true;
        try {
            Class.forName("com.teamdev.jxbrowser.chromium.Browser");
        } catch (ClassNotFoundException e) {
            flag = false;
        }
        if (flag) {
            if (!checkUPMResourcesExist()){
                // upm下载
                int val = FineJOptionPane.showConfirmDialog(null, Toolkit.i18nText("Fine-Design_Basic_Plugin_Shop_Need_Install"),
                        Toolkit.i18nText("Fine-Design_Basic_Confirm"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (val == JOptionPane.OK_OPTION){
                    try {
                        UpmResourceLoader.INSTANCE.download();
                        UpmResourceLoader.INSTANCE.install();
                        FineJOptionPane.showMessageDialog(null, Toolkit.i18nText("Fine-Design_Basic_Plugin_Shop_Installed"),
                                Toolkit.i18nText("Fine-Design_Basic_Message"), JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception e){
                        FineLoggerFactory.getLogger().error(e.getMessage(), e);
                        FineJOptionPane.showMessageDialog(null, Toolkit.i18nText("Fine-Design_Updater_Download_Failed"),
                                Toolkit.i18nText("Fine-Design_Basic_Message"), JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
            else {
                UpmShowPane upmPane = new UpmShowPane();
                if (dialog == null) {
                    dialog = new UpmShowDialog(DesignerContext.getDesignerFrame(), upmPane);
                }
                dialog.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Toolkit.i18nText("Fine-Design_Update_Info_Plugin_Message"));
            UpdateMainDialog dialog = new UpdateMainDialog(DesignerContext.getDesignerFrame());
            dialog.setAutoUpdateAfterInit();
            dialog.showDialog();
        }
    }

    public static void closeWindow() {
        if (dialog != null) {
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialog.setVisible(false);
            dialog = null;
        }
    }
}
