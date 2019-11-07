package com.fr.design.extra;

import com.fr.design.dialog.UIDialog;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.stable.StableUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by vito on 2017/5/5.
 */
public class LoginDialog extends UIDialog {
    private static final Dimension DEFAULT_SHOP = new Dimension(401, 301);

    public LoginDialog(Frame frame, Component pane) {
        super(frame);
        init(pane);
    }

    public LoginDialog(Dialog dialog, Component pane) {
        super(dialog);
        init(pane);
    }

    private void init(Component pane) {
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
        // do nothing
    }
}
