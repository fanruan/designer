package com.fr.design.ui;

import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.i18n.Toolkit;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.web.struct.AssembleComponent;
import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.browser.callback.InjectJsCallback;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.js.JsObject;
import com.teamdev.jxbrowser.net.Network;
import com.teamdev.jxbrowser.net.callback.InterceptRequestCallback;
import com.teamdev.jxbrowser.view.swing.BrowserView;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

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
        setLayout(new BorderLayout());
        if (browser == null) {
            if (DesignerEnvManager.getEnvManager().isOpenDebug()) {
                UIToolbar toolbar = new UIToolbar();
                add(toolbar, BorderLayout.NORTH);
                UIButton openDebugButton = new UIButton(Toolkit.i18nText("Fine-Design_Basic_Open_Debug_Window"));
                toolbar.add(openDebugButton);
                UIButton reloadButton = new UIButton(Toolkit.i18nText("Fine-Design_Basic_Reload"));
                toolbar.add(reloadButton);
                UIButton closeButton = new UIButton(Toolkit.i18nText("Fine-Design_Basic_Close_Window"));
                toolbar.add(closeButton);

                openDebugButton.addActionListener(e -> showDebuggerDialog());

                reloadButton.addActionListener(e -> browser.navigation().reloadIgnoringCache());

                closeButton.addActionListener(e -> SwingUtilities.getWindowAncestor(ModernUIPane.this).setVisible(false));
                initializeBrowser();
                add(BrowserView.newInstance(browser), BorderLayout.CENTER);
            } else {
                initializeBrowser();
                add(BrowserView.newInstance(browser), BorderLayout.CENTER);
            }
        }
    }

    private void showDebuggerDialog() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this));
        Engine engine = Engine.newInstance(
                EngineOptions.newBuilder(RenderingMode.HARDWARE_ACCELERATED)
                        .addSwitch("--disable-google-traffic")
                        .remoteDebuggingPort(9222).build());
        Browser debugger = engine.newBrowser();
        BrowserView debuggerView = BrowserView.newInstance(debugger);
        dialog.add(debuggerView, BorderLayout.CENTER);
        dialog.setSize(new Dimension(800, 400));
        GUICoreUtils.centerWindow(dialog);
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        browser.devTools().remoteDebuggingUrl().ifPresent(url -> {
            debugger.navigation().loadUrl(url);
        });
    }

    private void initializeBrowser() {
        Engine engine = Engine.newInstance(EngineOptions.newBuilder(RenderingMode.HARDWARE_ACCELERATED).addSwitch("--disable-google-traffic").build());
        browser = engine.newBrowser();
        // 初始化的时候，就把命名空间对象初始化好，确保window.a.b.c（"a.b.c"为命名空间）对象都是初始化过的
        browser.set(InjectJsCallback.class, params -> {
            params.frame().executeJavaScript(String.format(ModernUIConstants.SCRIPT_INIT_NAME_SPACE, namespace));
            return InjectJsCallback.Response.proceed();
        });
    }

    /**
     * 转向一个新的地址，相当于重新加载
     *
     * @param url 新的地址
     */
    public void redirect(String url) {
        browser.navigation().loadUrl(url);
    }

    /**
     * 转向一个新的地址，相当于重新加载
     *
     * @param url 新的地址
     * @param map 初始化参数
     */
    public void redirect(String url, Map<String, String> map) {
        Network network = browser.engine().network();
        network.set(InterceptRequestCallback.class, new NxInterceptRequestCallback(network, map));
        browser.navigation().loadUrl(url);
    }

    @Override
    protected String title4PopupWindow() {
        return "Modern";
    }


    public void populate(final T t) {
        browser.set(InjectJsCallback.class, params -> {
            JsObject ns = params.frame().executeJavaScript("window." + namespace);
            if (ns != null) {
                ns.putProperty(variable, t);
            }
            return InjectJsCallback.Response.proceed();
        });
    }

    public T update() {
        if (browser.mainFrame().isPresent()) {
            return browser.mainFrame().get().executeJavaScript("window." + namespace + "." + expression);
        }
        return null;
    }

    public static class Builder<T> {

        private ModernUIPane<T> pane = new ModernUIPane<>();

        public Builder<T> prepare(InjectJsCallback callback) {
            pane.browser.set(InjectJsCallback.class, callback);
            return this;
        }

        /**
         * 加载jar包中的资源
         *
         * @param path 资源路径
         */
        public Builder<T> withEMB(final String path) {
            Network network = pane.browser.engine().network();
            network.set(InterceptRequestCallback.class, new NxInterceptRequestCallback(network));
            pane.browser.navigation().loadUrl("emb:" + path);
            return this;
        }

        /**
         * 加载url指向的资源
         *
         * @param url 文件的地址
         */
        public Builder<T> withURL(final String url) {
            Network network = pane.browser.engine().network();
            network.set(InterceptRequestCallback.class, new NxInterceptRequestCallback(network));
            pane.browser.navigation().loadUrl(url);
            return this;
        }

        /**
         * 加载url指向的资源
         *
         * @param url 文件的地址
         */
        public Builder<T> withURL(final String url, Map<String, String> map) {
            Network network = pane.browser.engine().network();
            network.set(InterceptRequestCallback.class, new NxInterceptRequestCallback(network, map));
            pane.browser.navigation().loadUrl(url);
            return this;
        }

        /**
         * 加载Atom组件
         *
         * @param component Atom组件
         */
        public Builder<T> withComponent(AssembleComponent component) {
            Network network = pane.browser.engine().network();
            network.set(InterceptRequestCallback.class, new NxComplexInterceptRequestCallback(network, component));
            pane.browser.navigation().loadUrl("emb:dynamic");
            return this;
        }

        /**
         * 加载Atom组件
         *
         * @param component Atom组件
         */
        public Builder<T> withComponent(AssembleComponent component, Map<String, String> map) {
            Network network = pane.browser.engine().network();
            network.set(InterceptRequestCallback.class, new NxComplexInterceptRequestCallback(network, component, map));
            pane.browser.navigation().loadUrl("emb:dynamic");
            return this;
        }


        /**
         * 加载html文本内容
         *
         * @param html 要加载html文本内容
         */
        public Builder<T> withHTML(String html) {
            Network network = pane.browser.engine().network();
            network.set(InterceptRequestCallback.class, new NxInterceptRequestCallback(network));
            pane.browser.mainFrame().ifPresent(frame -> {
                frame.loadHtml(html);
            });
            return this;
        }

        /**
         * 设置该前端页面做数据交换所使用的对象
         *
         * @param namespace 对象名
         */
        public Builder<T> namespace(String namespace) {
            pane.namespace = namespace;
            return this;
        }

        /**
         * java端往js端传数据时使用的变量名字
         *
         * @param name 变量的名字
         */
        public Builder<T> variable(String name) {
            pane.variable = name;
            return this;
        }

        /**
         * js端往java端传数据时执行的函数表达式
         *
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
