package com.fr.env;

import com.fr.base.EnvException;
import com.fr.base.FRContext;
import com.fr.report.DesignAuthority;
import com.fr.report.util.AuthorityXMLUtils;
import com.fr.stable.EncodeConstants;
import com.fr.third.org.apache.commons.io.IOUtils;
import com.fr.third.org.apache.http.HttpResponse;
import com.fr.third.org.apache.http.HttpStatus;
import com.fr.third.org.apache.http.client.ClientProtocolException;
import com.fr.third.org.apache.http.client.ResponseHandler;
import com.fr.third.org.apache.http.client.methods.HttpUriRequest;
import com.fr.third.org.apache.http.client.methods.RequestBuilder;
import com.fr.third.org.apache.http.entity.ContentType;
import com.fr.third.org.apache.http.entity.InputStreamEntity;
import com.fr.third.org.apache.http.impl.client.CloseableHttpClient;
import com.fr.third.org.apache.http.impl.client.HttpClients;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Map;

public class RemoteEnvUtils {

    private RemoteEnvUtils() {
    }

    private static ResponseHandler<InputStream> responseHandler = new ResponseHandler<InputStream>() {
        @Override
        public InputStream handleResponse(HttpResponse response) throws IOException {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                throw new ClientProtocolException("Method failed: " + response.getStatusLine().toString());
            }
            InputStream in = response.getEntity().getContent();
            if (in == null) {
                return null;
            }
            // 读取并返回
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(in, out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    };

    public static InputStream simulateRPCByHttpPost(byte[] bytes, Map<String, String> parameters, boolean isSignIn, RemoteEnv env) throws EnvException {
        String path = env.getPath();
        RequestBuilder builder = RequestBuilder.post(path);

        InputStream inputStream = null;

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            builder.addParameter(entry.getKey(), entry.getValue());
        }
        if (!isSignIn) {
            builder.addParameter("id", env.getUserID());
        }
        InputStreamEntity reqEntity = new InputStreamEntity(new ByteArrayInputStream(bytes));

        try (CloseableHttpClient httpClient = HttpClients.createSystem()) {
            HttpUriRequest request = builder
                    .setEntity(reqEntity)
                    .build();
            inputStream = httpClient.execute(request, responseHandler);
        } catch (IOException e) {
            FRContext.getLogger().error(e.getMessage());
        }
        return inputStream;
    }

    public static InputStream simulateRPCByHttpPost(Map<String, String> parameters, boolean isSignIn, RemoteEnv env) throws EnvException {
        String path = env.getPath();
        RequestBuilder builder = RequestBuilder.post(path);

        InputStream inputStream = null;

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            builder.addParameter(entry.getKey(), entry.getValue());
        }
        if (!isSignIn) {
            builder.addParameter("id", env.getUserID());
        }

        try (CloseableHttpClient httpClient = HttpClients.createSystem()) {
            HttpUriRequest request = builder
                    .build();
            inputStream = httpClient.execute(request, responseHandler);
        } catch (IOException e) {
            FRContext.getLogger().error(e.getMessage());
        }
        return inputStream;
    }

    public static InputStream simulateRPCByHttpGet(Map<String, String> parameters, boolean isSignIn, RemoteEnv env) throws EnvException {
        String path = env.getPath();
        RequestBuilder builder = RequestBuilder.get(path);

        InputStream inputStream = null;

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            builder.addParameter(entry.getKey(), entry.getValue());
        }
        if (!isSignIn) {
            builder.addParameter("id", env.getUserID());
        }
        try (CloseableHttpClient httpClient = HttpClients.createSystem()) {
            HttpUriRequest request = builder.build();
            inputStream = httpClient.execute(request, responseHandler);

        } catch (IOException e) {
            FRContext.getLogger().error(e.getMessage());
        }
        return inputStream;
    }


    public static InputStream updateAuthorities(DesignAuthority[] authorities, RemoteEnv env) {
        String path = env.getPath();
        // 远程设计临时用户id
        String userID = env.getUserID();
        InputStream inputStream = null;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        AuthorityXMLUtils.writeDesignAuthoritiesXML(authorities, outputStream);
        InputStreamEntity reqEntity = new InputStreamEntity(new ByteArrayInputStream(outputStream.toByteArray()), ContentType.TEXT_XML);

        try (CloseableHttpClient httpClient = HttpClients.createSystem()) {
            HttpUriRequest request = RequestBuilder.post(path)
                    .addParameter("id", userID)
                    .addParameter("op", "remote_design_authority")
                    .addParameter("cmd", "update_authorities")
                    .setEntity(reqEntity)
                    .build();
            inputStream = httpClient.execute(request, responseHandler);
        } catch (IOException e) {
            FRContext.getLogger().error(e.getMessage());
        }

        return inputStream;

    }

    public static InputStream getAuthorities(RemoteEnv env) throws EnvException {
        String path = env.getPath();
        // 远程设计临时用户id
        String userID = env.getUserID();
        InputStream inputStream = null;

        try (CloseableHttpClient httpClient = HttpClients.createSystem();) {
            HttpUriRequest request = RequestBuilder.get(path)
                    .addParameter("id", userID)
                    .addParameter("op", "remote_design_authority")
                    .addParameter("cmd", "get_authorities")
                    .build();
            inputStream = httpClient.execute(request, responseHandler);
        } catch (IOException e) {
            FRContext.getLogger().error(e.getMessage());
        }
        return inputStream;
    }


}
