package com.fr.design.ui;

import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.BasicPane;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserPreferences;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import javax.swing.*;
import java.awt.*;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-03-04
 * 用于加载html5的Swing容器，可以在设计选项设置中打开调试窗口，示例可查看：com.fr.design.ui.ModernUIPaneTest
 */
public class ModernUIPane<T> extends BasicPane {

    private Browser browser;
    private String namespace = "NS";
    private String variable = "data";
    private String expression = "update()";

    private ModernUIPane() {
        initialize();
    }

    private void initialize() {
        if (browser == null) {
            setLayout(new BorderLayout());
            BrowserPreferences.setChromiumSwitches("--disable-google-traffic");
            if (DesignerEnvManager.getEnvManager().isOpenDebug()) {
                JSplitPane splitPane = new JSplitPane();
                add(splitPane, BorderLayout.CENTER);
                splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                splitPane.setDividerLocation(500);
                BrowserPreferences.setChromiumSwitches("--remote-debugging-port=9222");
                initializeBrowser();
                splitPane.setLeftComponent(new BrowserView(browser));
                Browser debugger = new Browser();
                debugger.loadURL(browser.getRemoteDebuggingURL());
                BrowserView debuggerView = new BrowserView(debugger);
                splitPane.setRightComponent(debuggerView);
            } else {
                initializeBrowser();
                add(new BrowserView(browser), BorderLayout.CENTER);
            }
        }
    }

    private void initializeBrowser() {
        browser = new Browser();
    }

    @Override
    protected String title4PopupWindow() {
        return "Modern";
    }


    public void populate(final T t) {
        browser.addLoadListener(new LoadAdapter() {
            @Override
            public void onFinishLoadingFrame(FinishLoadingEvent event) {
                if (event.isMainFrame()) {
                    event.getBrowser().executeJavaScript(String.format(ModernUI.SCRIPT_STRING, namespace));
                    JSValue ns = event.getBrowser().executeJavaScriptAndReturnValue("window." + namespace);
                    ns.asObject().setProperty(variable, t);
                }
            }
        });
    }

    public T update() {
        JSValue jsValue = browser.executeJavaScriptAndReturnValue("window." + namespace + "." + expression);
        if (jsValue.isObject()) {
            return (T)jsValue.asJavaObject();
        }
        return null;
    }

    public static class Builder<T> {

        private ModernUIPane<T> pane = new ModernUIPane<>();

        public Builder<T> withURL(String url) {
            pane.browser.loadURL(url);
            return this;
        }

        public Builder<T> withHTML(String html) {
            pane.browser.loadHTML(html);
            return this;
        }

        public Builder<T> namespace(String namespace) {
            pane.namespace = namespace;
            return this;
        }

        public Builder<T> variable(String name) {
            pane.variable = name;
            return this;
        }

        public Builder<T> expression(String expression) {
            pane.expression = expression;
            return this;
        }

        public ModernUIPane<T> build() {
            return pane;
        }
    }
}
