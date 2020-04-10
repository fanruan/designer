package com.fr.design.extra;

import com.fr.design.dialog.UIDialog;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by zhaohehe on 16/7/28.
 */
public class QQLoginDialog extends UIDialog {
    private static final Dimension DEFAULT_SHOP = new Dimension(700, 500);

    public QQLoginDialog(Frame frame, Component pane) {
        super(frame);
        setUndecorated(true);
        JPanel panel = (JPanel) getContentPane();
        panel.setLayout(new BorderLayout());
        add(pane, BorderLayout.CENTER);
        setSize(DEFAULT_SHOP);
        GUICoreUtils.centerWindow(this);
        setResizable(false);
        setTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Manager"));
    }

    @Override
    public void checkValid() throws Exception {
        // do nothing
    }

}