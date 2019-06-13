package com.fr.design.ui;

import com.fr.stable.StringUtils;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserContext;
import com.teamdev.jxbrowser.chromium.ProtocolService;
import com.teamdev.jxbrowser.chromium.URLResponse;

import javax.activation.MimetypesFileTypeMap;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-03-07
 */
public class Assistant {

    public static URLResponse inputStream2Response(InputStream inputStream, String filePath) throws Exception {
        URLResponse response = new URLResponse();
        DataInputStream stream = new DataInputStream(inputStream);
        byte[] data = new byte[stream.available()];
        stream.readFully(data);
        response.setData(data);
        String mimeType = getMimeType(filePath);
        response.getHeaders().setHeader("Content-Type", mimeType);
        return response;
    }


    private static String getMimeType(String path) {
        if (StringUtils.isBlank(path)) {
            return "text/html";
        }
        if (path.endsWith(".html")) {
            return "text/html";
        }
        if (path.endsWith(".css")) {
            return "text/css";
        }
        if (path.endsWith(".js")) {
            return "text/javascript";
        }
        if (path.endsWith(".svg")) {
            return "image/svg+xml";
        }
        Path file = new File(path).toPath();
        try {
            return Files.probeContentType(file);
        } catch (IOException e) {
            return "text/html";
        }
    }

    public static void setEmbProtocolHandler(Browser browser, EmbProtocolHandler handler) {
        BrowserContext browserContext = browser.getContext();
        ProtocolService protocolService = browserContext.getProtocolService();
        // 支持读取jar包中文件的自定义协议————emb:/com/fr/design/images/bbs.png
        protocolService.setProtocolHandler("emb", handler);
        protocolService.setProtocolHandler("file", handler);
    }
}
