package com.fr.design.ui;

import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.BasicPane;
import com.fr.web.struct.AssembleComponent;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserPreferences;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.events.LoadListener;
import com.teamdev.jxbrowser.chromium.events.ScriptContextAdapter;
import com.teamdev.jxbrowser.chromium.events.ScriptContextEvent;
import com.teamdev.jxbrowser.chromium.events.ScriptContextListener;
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
    private String namespace = "Pool";
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
        // 初始化的时候，就把命名空间对象初始化好，确保window.a.b.c（"a.b.c"为命名空间）对象都是初始化过的
        browser.addScriptContextListener(new ScriptContextAdapter() {
            @Override
            public void onScriptContextCreated(ScriptContextEvent event) {
                event.getBrowser().executeJavaScript(String.format(ModernUIConstants.SCRIPT_INIT_NAME_SPACE, namespace));
            }
        });
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

        public Builder<T> prepare(ScriptContextListener contextListener) {
            pane.browser.addScriptContextListener(contextListener);
            return this;
        }

        public Builder<T> prepare(LoadListener loadListener) {
            pane.browser.addLoadListener(loadListener);
            return this;
        }

        /**
         * 加载jar包中的资源
         * @param path 资源路径
         */
        public Builder<T> withEMB(final String path) {
            Assistant.setEmbProtocolHandler(pane.browser, new EmbProtocolHandler());
            pane.browser.loadURL("emb:" + path);
            return this;
        }

        /**
         * 加载url指向的资源
         * @param url 文件的地址
         */
        public Builder<T> withURL(final String url) {
            Assistant.setEmbProtocolHandler(pane.browser, new EmbProtocolHandler());
            pane.browser.loadURL(url);
            return this;
        }

        /**
         * 加载Atom组件
         * @param component Atom组件
         */
        public Builder<T> withComponent(AssembleComponent component) {
            Assistant.setEmbProtocolHandler(pane.browser, new EmbProtocolHandler(component));
            pane.browser.loadURL("emb:dynamic");
            return this;
        }

        /**
         * 加载html文本内容
         * @param html 要加载html文本内容
         */
        public Builder<T> withHTML(String html) {
            Assistant.setEmbProtocolHandler(pane.browser, new EmbProtocolHandler());
            pane.browser.loadHTML(html);
            return this;
        }

        /**
         * 设置该前端页面做数据交换所使用的对象
         * @param namespace 对象名
         */
        public Builder<T> namespace(String namespace) {
            pane.namespace = namespace;
            return this;
        }

        /**
         * java端往js端传数据时使用的变量名字
         * @param name 变量的名字
         */
        public Builder<T> variable(String name) {
            pane.variable = name;
            return this;
        }

        /**
         * js端往java端传数据时执行的函数表达式
         * @param expression 函数表达式
         */
        public Builder<T> expression(String expression) {
            pane.expression = expression;
            return this;
        }

        public ModernUIPane<T> build() {
            return pane;
        }
    }
}
