package com.fr.design.extra;

import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.UIDialog;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.stable.StableUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by vito on 16/4/18.
 */
public class ShopDialog extends UIDialog {
    private static final Dimension DEFAULT_SHOP = new Dimension(900, 700);

    public ShopDialog(Frame frame, BasicPane pane) {
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
        // do nothing
    }
}
