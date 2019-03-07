package com.fr.design.ui;

import com.teamdev.jxbrowser.chromium.URLResponse;

import java.io.DataInputStream;
import java.io.InputStream;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-03-07
 */
public class ModernUIAssist {

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
        if (path.endsWith(".html")) {
            return "text/html";
        }
        if (path.endsWith(".css")) {
            return "text/css";
        }
        if (path.endsWith(".js")) {
            return "text/javascript";
        }
        return "text/html";
    }
}
