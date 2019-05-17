package com.fr.design.upm.database;

import com.fr.design.dialog.UIDialog;
import com.fr.design.mainframe.DesignerContext;

import javax.swing.*;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-05-16
 */
public class UniverseDatabaseOpener {

    private static UIDialog dialog = null;

    public static UIDialog getDialog() {
        return dialog;
    }

    public static void showUniverseDatabaseDialog() {
        UniverseDatabasePane upmPane = new UniverseDatabasePane();
        if (dialog == null) {
            dialog = new UniverseDatabaseDialog(DesignerContext.getDesignerFrame(), upmPane);
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
