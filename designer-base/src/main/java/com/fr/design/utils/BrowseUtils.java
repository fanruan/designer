package com.fr.design.utils;

import com.fr.design.dialog.FineJOptionPane;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.OperatingSystem;
import com.fr.stable.StringUtils;

import javax.swing.JOptionPane;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by XINZAI on 2018/8/23.
 */
public class BrowseUtils {

    /**
     * 处理内存异常，win10下用rundll32打开
     * @param uri 网址
     * @param e
     */
    private static void startBrowserFromCommand(String uri, IOException e) {

        if (OperatingSystem.isWindows()) {
            try {
                // win10 内存用到到80%左右的时候, Desktop.browser经常提示"存储空间不足, 无法处理改命令", 用rundll32可以打开.
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + uri);
            } catch (IOException ee) {
                FineJOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Set_Default_Browser_Duplicate"));
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        } else {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    /**
     * 打开网页,用默认浏览器
     * @param url 网址
     */
    public static void browser(String url) {

        if (StringUtils.isEmpty(url)) {
            FineLoggerFactory.getLogger().info("The URL is empty!");
            return;
        }
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException exp) {
            startBrowserFromCommand(url, exp);
        } catch (URISyntaxException exp) {
            FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
        } catch (Exception exp) {
            FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
            FineLoggerFactory.getLogger().error("Can not open the browser for URL:  " + url);
        }
    }


}
