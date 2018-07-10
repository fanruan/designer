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
        this.mainJS = setMainJS();
        this.scriptsId = setScriptsId();
        this.webPane = setWebPane();
    }

    abstract String setMainJS();

    abstract String setScriptsId();

    abstract JFXPanel setWebPane();

    public String getMainJS() {
        return mainJS;
    }

    public String getScriptsId() {
        return scriptsId;
    }

    public JFXPanel getWebPane() {
        return webPane;
    }
}
