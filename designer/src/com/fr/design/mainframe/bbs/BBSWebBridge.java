package com.fr.design.mainframe.bbs;

import com.fr.design.dialog.UIDialog;
import javafx.scene.web.WebEngine;

import javax.swing.*;

/**
 * Created by vito on 16/4/12.
 */
public class BBSWebBridge {
    private static BBSWebBridge bbsWebBridge;
    private WebEngine webEngine;
    private UIDialog uiDialog;

    public static BBSWebBridge getHelper() {
        if (bbsWebBridge != null) {
            return bbsWebBridge;
        }
        synchronized (BBSWebBridge.class) {
            if (bbsWebBridge == null) {
                bbsWebBridge = new BBSWebBridge();
            }
            return bbsWebBridge;
        }
    }

    public static BBSWebBridge getHelper(WebEngine webEngine) {
        getHelper();
        bbsWebBridge.setEngine(webEngine);
        return bbsWebBridge;
    }

    private void setEngine(WebEngine webEngine) {
        this.webEngine = webEngine;
    }

    private BBSWebBridge() {}

    public void closeWindow() {
        if (uiDialog != null) {
            uiDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            uiDialog.setVisible(false);
            uiDialog.dispose();
        }
    }

    public void setDialogHandle(UIDialog uiDialog){
        this.uiDialog = uiDialog;
    }
}
