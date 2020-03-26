package com.fr.design.ui;

import com.fr.base.TemplateUtils;
import com.fr.general.IOUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ArrayUtils;
import com.fr.stable.EncodeConstants;
import com.fr.stable.StringUtils;
import com.fr.third.org.apache.commons.codec.net.URLCodec;
import com.teamdev.jxbrowser.net.HttpHeader;
import com.teamdev.jxbrowser.net.HttpStatus;
import com.teamdev.jxbrowser.net.Network;
import com.teamdev.jxbrowser.net.UrlRequest;
import com.teamdev.jxbrowser.net.UrlRequestJob;
import com.teamdev.jxbrowser.net.callback.InterceptRequestCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2020/3/25
 */
public class NxInterceptRequestCallback implements InterceptRequestCallback {

    Network network;
    Map<String, String> map;

    public NxInterceptRequestCallback(Network network) {
        this.network = network;
    }

    public NxInterceptRequestCallback(Network network, Map<String, String> map) {
        this.network = network;
        this.map = map;
    }

    @Override
    public Response on(Params params) {
        UrlRequest urlRequest = params.urlRequest();
        String path = urlRequest.url();
        if (path.startsWith("file:")) {
            Optional<UrlRequestJob> optional = generateFileProtocolUrlRequestJob(urlRequest, path);
            if (optional.isPresent()) {
                return InterceptRequestCallback.Response.intercept(optional.get());
            }
        } else {
            return next(urlRequest, path);
        }
        return Response.proceed();
    }

    Response next(UrlRequest urlRequest, String path) {
        return Response.proceed();
    }

    private Optional<UrlRequestJob> generateFileProtocolUrlRequestJob(UrlRequest urlRequest, String path) {
        try {
            String url = new URLCodec().decode(path);
            String filePath = TemplateUtils.renderParameter4Tpl(url, map);
            File file = new File(URI.create(filePath).getPath());
            InputStream inputStream = IOUtils.readResource(file.getAbsolutePath());
            String mimeType = getMimeType(path);
            byte[] bytes;
            if (isPlainText(mimeType)) {
                String text = IOUtils.inputStream2String(inputStream, EncodeConstants.ENCODING_UTF_8);
                text = TemplateUtils.renderParameter4Tpl(text, map);
                bytes = text.getBytes(StandardCharsets.UTF_8);
            } else {
                bytes = IOUtils.inputStream2Bytes(inputStream);
            }
            return Optional.of(generateBasicUrlRequestJob(urlRequest, mimeType, bytes));
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    private boolean isPlainText(String mimeType) {
        return ArrayUtils.contains(new String[]{"text/html", "text/javascript", "text/css"}, mimeType);
    }

    UrlRequestJob generateBasicUrlRequestJob(UrlRequest urlRequest, String mimeType, byte[] bytes) {
        UrlRequestJob.Options options = UrlRequestJob.Options
                .newBuilder(urlRequest.id(), HttpStatus.OK)
                .addHttpHeader(HttpHeader.of("Content-Type", mimeType))
                .build();
        UrlRequestJob urlRequestJob = network.newUrlRequestJob(options);
        urlRequestJob.write(bytes);
        urlRequestJob.complete();
        return urlRequestJob;
    }

    String getMimeType(String path) {
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
        if (path.endsWith(".png")) {
            return "image/png";
        }
        if (path.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (path.endsWith(".gif")) {
            return "image/gif";
        }
        Path file = new File(path).toPath();
        try {
            return Files.probeContentType(file);
        } catch (IOException e) {
            return "text/html";
        }
    }
}
