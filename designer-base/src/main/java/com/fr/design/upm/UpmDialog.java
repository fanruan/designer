package com.fr.design.upm;

import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.UIDialog;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.stable.StableUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-04-12
 */
public class UpmDialog extends UIDialog {

    private static final Dimension DEFAULT_SHOP = new Dimension(900, 700);

    public UpmDialog(Frame frame, BasicPane pane) {
        super(frame);
        setUndecorated(true);
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
