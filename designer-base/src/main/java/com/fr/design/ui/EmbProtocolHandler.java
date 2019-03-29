package com.fr.design.ui;

import com.fr.general.IOUtils;
import com.fr.stable.StringUtils;
import com.fr.web.struct.AssembleComponent;
import com.fr.web.struct.AtomBuilder;
import com.fr.web.struct.PathGroup;
import com.fr.web.struct.category.ScriptPath;
import com.fr.web.struct.category.StylePath;
import com.teamdev.jxbrowser.chromium.ProtocolHandler;
import com.teamdev.jxbrowser.chromium.URLRequest;
import com.teamdev.jxbrowser.chromium.URLResponse;

import java.io.InputStream;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-03-07
 */
public class EmbProtocolHandler implements ProtocolHandler {

    private AssembleComponent component;

    public EmbProtocolHandler() {

    }

    public EmbProtocolHandler(AssembleComponent component) {
        this.component = component;
    }


    @Override
    public URLResponse onRequest(URLRequest req) {
        try {
            String path = req.getURL();
            if (path.startsWith("emb:dynamic")) {
                URLResponse response = new URLResponse();
                response.setData(htmlText().getBytes());
                response.getHeaders().setHeader("Content-Type", "text/html");
                return response;
            } else {
                int index = path.indexOf("=");
                if (index > 0) {
                    path = path.substring(index + 1);
                } else {
                    path = path.substring(4);
                }
                InputStream inputStream = IOUtils.readResource(path);
                return Assistant.inputStream2Response(inputStream, path);
            }
        } catch (Exception ignore) {

        }
        return null;
    }

    private String htmlText() {
        PathGroup pathGroup = AtomBuilder.create().buildAssembleFilePath(ModernRequestClient.KEY, component);
        StylePath[] stylePaths = pathGroup.toStylePathGroup();
        StringBuilder styleText = new StringBuilder();
        for (StylePath path : stylePaths) {
            if (StringUtils.isNotBlank(path.toFilePath())) {
                styleText.append("<link rel=\"stylesheet\" href=\"emb:");
                styleText.append(path.toFilePath());
                styleText.append("\"/>");
            }
        }
        String result = ModernUIConstants.HTML_TPL.replaceAll("##style##", styleText.toString());
        ScriptPath[] scriptPaths = pathGroup.toScriptPathGroup();
        StringBuilder scriptText = new StringBuilder();
        for (ScriptPath path : scriptPaths) {
            if (StringUtils.isNotBlank(path.toFilePath())) {
                scriptText.append("<script src=\"emb:");
                scriptText.append(path.toFilePath());
                scriptText.append("\"></script>");
            }
        }
        result = result.replaceAll("##script##", scriptText.toString());
        return result;
    }
}
