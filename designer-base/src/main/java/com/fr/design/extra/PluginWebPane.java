package com.fr.design.extra;

import com.fr.base.TemplateUtils;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.general.GeneralContext;
import com.fr.general.IOUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.EncodeConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by richie on 16/3/19.
 */
public class PluginWebPane extends JFXPanel {
    private static final String RESOURCE_URL = "resourceURL";
    private static final String LANGUAGE = "language";
    private static final String URL_PLUS = "+";
    private static final String URL_SPACING = "%20";
    private static final String URL_PREFIX = "file:///";
    private WebEngine webEngine;

    public PluginWebPane(final String installHome, final String mainJs) {
        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                BorderPane root = new BorderPane();
                Scene scene = new Scene(root);
                PluginWebPane.this.setScene(scene);
                WebView webView = new WebView();
                webEngine = webView.getEngine();
                try{
                    String htmlString = getRenderedHtml(installHome, mainJs);
                    webEngine.loadContent(htmlString);
                    webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
                        @Override
                        public void handle(WebEvent<String> event) {
                            showAlert(event.getData());
                        }
                    });
                    JSObject obj = (JSObject) webEngine.executeScript("window");
                    obj.setMember("PluginHelper", PluginWebBridge.getHelper(webEngine));
                    webView.setContextMenuEnabled(false);//屏蔽右键
                    root.setCenter(webView);
                }catch (Exception e){
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                }

            }
        });
    }

    private String getRenderedHtml(String installHome, String mainJs) throws IOException {
        InputStream inp = IOUtils.readResource(StableUtils.pathJoin(installHome, mainJs));
        if (inp == null) {
            throw new IOException("Not found template: " +  mainJs);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inp, StableUtils.RESOURCE_ENCODER));
        BufferedReader read = new BufferedReader(reader);
        StringBuffer sb = new StringBuffer();
        String line;
        Map<String, Object> map4Tpl = new HashMap<String, Object>();
        //URL中关于空格的编码与空格所在位置相关：空格被编码成+的情况只能在查询字符串部分出现，而被编码成%20则可以出现在路径和查询字符串中
        //URLEncoder会将空格转成+,这边需要+转成%20
        map4Tpl.put(RESOURCE_URL, URL_PREFIX + URLEncoder.encode(installHome, EncodeConstants.ENCODING_UTF_8).replace(URL_PLUS, URL_SPACING));
        map4Tpl.put(LANGUAGE, GeneralContext.getLocale().toString());
        while ((line = read.readLine()) != null) {
            if (sb.length() > 0) {
                sb.append('\n');
            }
            sb.append(line);
        }
        String htmlString = StringUtils.EMPTY;
        try{
            htmlString = TemplateUtils.renderParameter4Tpl(sb.toString(), map4Tpl);
        }catch (Exception e){
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        reader.close();
        inp.close();
        return htmlString;
    }

    private void showAlert(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FineJOptionPane.showMessageDialog(PluginWebPane.this, message);
            }
        });
    }
}
