package com.fr.design.mainframe.bbs;

import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.UIDialog;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.general.SiteCenter;
import com.fr.general.http.HttpClient;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by zhaohehe on 16/7/26.
 */
public class LoginDialog extends UIDialog {
    private static final Dimension DEFAULT_SHOP = new Dimension(404, 204);
    
    public LoginDialog(Frame frame, BasicPane pane) {
        super(frame);
        setUndecorated(true);
        JPanel panel = (JPanel) getContentPane();
        panel.setLayout(new BorderLayout());
        add(pane, BorderLayout.CENTER);
        setSize(DEFAULT_SHOP);
        GUICoreUtils.centerWindow(this);
        setResizable(false);
        setTitle(Inter.getLocText("FR-Designer-Plugin_Manager"));
    }

    @Override
    public void checkValid() throws Exception {
    }
}
