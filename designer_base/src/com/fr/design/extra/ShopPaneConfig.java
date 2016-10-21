package com.fr.design.extra;

import javafx.embed.swing.JFXPanel;

/**
 * Created by vito on 2016/9/28.
 */
public abstract class ShopPaneConfig {
    private String mainJS;
    private String scriptsId;
    private JFXPanel webPane;

    public ShopPaneConfig() {
        this.mainJS = getMainJS();
        this.scriptsId = getScriptsId();
        this.webPane = getWebPane();
    }

    abstract String getMainJS();

    abstract String getScriptsId();

    abstract JFXPanel getWebPane();
}
