package com.fr.design.mainframe.bbs;

import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.UIDialog;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.stable.StableUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by zhaohehe on 16/7/26.
 */
public class LoginDialog extends UIDialog {
    private static final Dimension DEFAULT_SHOP = new Dimension(401, 201);
    
    public LoginDialog(Frame frame, BasicPane pane) {
        super(frame);
        if (StableUtils.getMajorJavaVersion() == 8) {
            setUndecorated(true);
        }
        JPanel panel = (JPanel) getContentPane();
        panel.setLayout(new BorderLayout());
        add(pane, BorderLayout.CENTER);
        setSize(DEFAULT_SHOP);
        GUICoreUtils.centerWindow(this);
        setResizable(false);
    }

    @Override
    public void checkValid() throws Exception {
    }
}
