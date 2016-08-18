package com.fr.design.extra;

import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;
import com.fr.stable.StableUtils;

import java.awt.*;
import java.io.File;
import java.net.URL;

/**
 * Created by zhaohehe on 16/7/27.
 */
public class LoginPane extends BasicPane {
    private static final String LATEST = "latest";

    public LoginPane() {
        setLayout(new BorderLayout());
        if (StableUtils.getMajorJavaVersion() == 8) {
            String installHome;
            if (StableUtils.isDebug()) {
                URL url = ClassLoader.getSystemResource("");
                installHome = url.getPath();
                addPane(installHome);
            } else {
                installHome = StableUtils.getInstallHome();
                File file = new File(StableUtils.pathJoin(installHome, "scripts"));
            }
        } else {
        }
    }

    private void addPane(String installHome) {
        LoginWebPane webPane = new LoginWebPane(new File(installHome).getAbsolutePath(),LoginPane.this);
        add(webPane, BorderLayout.CENTER);
    }


    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer-Plugin_Manager");
    }
}