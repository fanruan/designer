package com.fr.design.extra;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.StableUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author richie
 * @date 2015-03-09
 * @since 8.0
 * 应用中心的构建采用JavaScript代码来动态实现,但是不总是依赖于服务器端的HTML
 * 采用JDK提供的JavaScript引擎,实际是用JavaScript语法实现Java端的功能,并通过JavaScript引擎动态调用
 * JavaScript放在安装目录下的scripts/store目录下,检测到新版本的时候,可以通过更新这个目录下的文件实现热更新
 * 不直接嵌入WebView组件的原因是什么呢?
 * 因为如果直接嵌入WebView,和设计器的交互就需要预先设定好,这样灵活性会差很多,而如果使用JavaScript引擎,
 * 就可以直接在JavaScript中和WebView组件做交互,而同时JavaScript中可以调用任何的设计器API.
 */
public class PluginManagerPane extends BasicPane {


    public PluginManagerPane() {
        setLayout(new BorderLayout());
        if (StableUtils.isDebug()) {
            URL url = ClassLoader.getSystemResource("");
            String installHome = url.getPath();
            PluginWebPane webPane = new PluginWebPane(installHome);
            add(webPane, BorderLayout.CENTER);
        } else {
            initTraditionalStore();
        }
    }

    private void initTraditionalStore() {
        UITabbedPane tabbedPane = new UITabbedPane();
        add(tabbedPane, BorderLayout.CENTER);
        PluginInstalledPane installedPane = new PluginInstalledPane();
        tabbedPane.addTab(installedPane.tabTitle(), installedPane);
        tabbedPane.addTab(Inter.getLocText("FR-Designer-Plugin_Update"), new PluginUpdatePane(tabbedPane));
        tabbedPane.addTab(Inter.getLocText("FR-Designer-Plugin_All_Plugins"), new PluginFromStorePane(tabbedPane));
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer-Plugin_Manager");
    }
}