package com.fr.design.ui;

import com.fr.base.TemplateUtils;
import com.fr.general.IOUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.EncodeConstants;
import com.fr.stable.StringUtils;
import com.fr.third.org.apache.commons.codec.net.URLCodec;
import com.fr.third.org.apache.commons.io.FileUtils;
import com.fr.third.org.apache.commons.io.FilenameUtils;
import com.fr.web.struct.AssembleComponent;
import com.fr.web.struct.AtomBuilder;
import com.fr.web.struct.PathGroup;
import com.fr.web.struct.category.ScriptPath;
import com.fr.web.struct.category.StylePath;
import com.teamdev.jxbrowser.chromium.ProtocolHandler;
import com.teamdev.jxbrowser.chromium.URLRequest;
import com.teamdev.jxbrowser.chromium.URLResponse;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-03-07
 */
public class EmbProtocolHandler implements ProtocolHandler {

    private AssembleComponent component;
    private Map<String, String> map;

    public EmbProtocolHandler() {

    }

    public EmbProtocolHandler(AssembleComponent component) {
        this.component = component;
    }

    public EmbProtocolHandler(AssembleComponent component, Map<String, String> map) {
        this.component = component;
        this.map = map;
    }

    public EmbProtocolHandler(Map<String, String> map) {
        this.map = map;
    }

    @Override
    public URLResponse onRequest(URLRequest req) {
        InputStream inputStream = null;
        try {
            String path = req.getURL();
            if (path.startsWith("file:")) {
                String url = new URLCodec().decode(path);
                String filePath = TemplateUtils.renderParameter4Tpl(url, map);
                File file = new File(URI.create(filePath).getPath());
                inputStream = IOUtils.readResource(file.getAbsolutePath());
                String text = IOUtils.inputStream2String(inputStream, EncodeConstants.ENCODING_UTF_8);
                text = TemplateUtils.renderParameter4Tpl(text, map);
                return Assistant.inputStream2Response(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)), path);
            } else if (path.startsWith("emb:dynamic")) {
                URLResponse response = new URLResponse();
                response.setData(htmlText(map).getBytes());
                response.getHeaders().setHeader("Content-Type", "text/html");
                return response;
            }  else {
                int index = path.indexOf("=");
                if (index > 0) {
                    path = path.substring(index + 1);
                } else {
                    path = path.substring(4);
                }
                inputStream = IOUtils.readResource(path);
                return Assistant.inputStream2Response(inputStream, path);
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().info(e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                }
            }
        }
        return null;
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
