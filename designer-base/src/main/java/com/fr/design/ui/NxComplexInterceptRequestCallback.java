package com.fr.design.ui;

import com.fr.general.IOUtils;
import com.fr.stable.StringUtils;
import com.fr.web.struct.AssembleComponent;
import com.fr.web.struct.AtomBuilder;
import com.fr.web.struct.PathGroup;
import com.fr.web.struct.category.ScriptPath;
import com.fr.web.struct.category.StylePath;
import com.teamdev.jxbrowser.net.Network;
import com.teamdev.jxbrowser.net.UrlRequest;
import com.teamdev.jxbrowser.net.callback.InterceptRequestCallback;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2020/3/25
 */
public class NxComplexInterceptRequestCallback extends NxInterceptRequestCallback {

    private AssembleComponent component;

    public NxComplexInterceptRequestCallback(Network network, AssembleComponent component) {
        super(network);
        this.component = component;
    }

    public NxComplexInterceptRequestCallback(Network network, AssembleComponent component, Map<String, String> map) {
        super(network, map);
        this.component = component;
    }

    @Override
    protected Response next(UrlRequest urlRequest, String path) {
        if (path.startsWith("emb:dynamic")) {
            String text = htmlText(map);
            return InterceptRequestCallback.Response.intercept(generateBasicUrlRequestJob(urlRequest, "text/html", text.getBytes(StandardCharsets.UTF_8)));
        } else {
            int index = path.indexOf("=");
            if (index > 0) {
                path = path.substring(index + 1);
            } else {
                path = path.substring(4);
            }
            InputStream inputStream = IOUtils.readResource(path);
            return InterceptRequestCallback.Response.intercept(generateBasicUrlRequestJob(urlRequest, getMimeType(path), IOUtils.inputStream2Bytes(inputStream)));
        }
    }

    private String htmlText(Map<String, String> map) {
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
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                result = result.replaceAll("\\$\\{" + key + "}", value);
            }
        }
        return result;
    }
}