package com.fr.env;

import com.fr.base.FRContext;
import com.fr.general.IOUtils;
import com.fr.report.DesignAuthority;
import com.fr.stable.EncodeConstants;
import com.fr.third.org.apache.http.HttpEntity;
import com.fr.third.org.apache.http.client.methods.CloseableHttpResponse;
import com.fr.third.org.apache.http.client.methods.HttpUriRequest;
import com.fr.third.org.apache.http.client.methods.RequestBuilder;
import com.fr.third.org.apache.http.entity.ContentType;
import com.fr.third.org.apache.http.entity.InputStreamEntity;
import com.fr.third.org.apache.http.impl.client.CloseableHttpClient;
import com.fr.third.org.apache.http.impl.client.HttpClients;
import com.fr.third.org.apache.http.util.EntityUtils;
import com.fr.web.utils.AuthorityXMLUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RemoteEnvUtils {

    private RemoteEnvUtils() {
    }

    public static boolean updateAuthorities(DesignAuthority[] authorities, RemoteEnv env) {
        String path = env.getPath();
        String userID = env.getUserID();

        String res = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        AuthorityXMLUtils.writeDesignAuthoritiesXML(authorities, outputStream);


        InputStreamEntity reqEntity = new InputStreamEntity(new ByteArrayInputStream(outputStream.toByteArray()), ContentType.TEXT_XML);

        HttpUriRequest request = RequestBuilder.post(path)
                .addParameter("id", userID)
                .addParameter("op", "remote_design_authority")
                .addParameter("cmd", "update_authorities")
                .setEntity(reqEntity)
                .build();

        try {
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            res = IOUtils.inputStream2String(entity.getContent(), EncodeConstants.ENCODING_UTF_8);
            EntityUtils.consume(entity);
        } catch (IOException e) {
            FRContext.getLogger().error(e.getMessage());
        }

        return res != null && Boolean.valueOf(res);

    }

    public static DesignAuthority[] getAuthorities(RemoteEnv env) {
        String path = env.getPath();
        String userID = env.getUserID();
        DesignAuthority[] authorities = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpUriRequest request = RequestBuilder.get(path)
                .addParameter("id", userID)
                .addParameter("op", "remote_design_authority")
                .addParameter("cmd", "get_authorities")
                .build();

        try {
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            authorities = AuthorityXMLUtils.readDesignAuthoritiesXML(entity.getContent());
            EntityUtils.consume(entity);
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
        return authorities;

    }

}
