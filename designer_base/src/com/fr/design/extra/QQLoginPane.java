package com.fr.design.extra;

import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;
import com.fr.stable.StableUtils;

import java.awt.*;
import java.io.File;
import java.net.URL;

/**
 * Created by zhaohehe on 16/7/28.
 */
public class QQLoginPane extends BasicPane {
    private static final String LATEST = "latest";

    public QQLoginPane() {
        setLayout(new BorderLayout());
        if (System.getProperty("java.version").startsWith("1.8")) {
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
        QQLoginWebPane webPane = new QQLoginWebPane(new File(installHome).getAbsolutePath());
        add(webPane, BorderLayout.CENTER);
    }


    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer-Plugin_Manager");
    }
}