package com.fr.design.mainframe.messagecollect.solid;

import com.fr.general.CloudCenter;
import com.fr.general.http.HttpToolbox;
import com.fr.json.JSON;
import com.fr.json.JSONFactory;
import com.fr.log.FineLoggerFactory;
import com.fr.security.JwtUtils;
import com.fr.stable.CommonUtils;
import com.fr.stable.StringUtils;
import com.fr.workspace.WorkContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.fr.design.mainframe.messagecollect.solid.SolidCollectConstants.REQUEST_SUBJECT;
import static com.fr.design.mainframe.messagecollect.solid.SolidCollectConstants.TIME_OUT;

/**
 * 设计器固化信息回传类
 * Created by alex sung on 2019/8/22.
 */


public class SolidCollector {
    private static final String CONTENT_URL = "/v10/collect/solid";
    private static final String DELETE_URL = "/v10/collect/solid/delete";
    private static final String UNLOCK_URL = "/v10/collect/solid/unlock";
    private static final String ATTR_CIPHER_TEXT = "cipherText";
    private static final String ATTR_SIGNATURE = "signature";
    private static final String SOLID_UPLOAD_URL = CloudCenter.getInstance().acquireUrlByKind("design.solid");

    private static volatile SolidCollector instance;

    public static SolidCollector getInstance() {
        if (instance == null) {
            synchronized (SolidCollector.class) {
                if (instance == null) {
                    instance = new SolidCollector();
                }
            }
        }
        return instance;
    }

    /**
     * 回传文件给云中心，并删除服务端本地文件
     */
    public void sendToCloudCenterAndDeleteFile() {
        if (WorkContext.getCurrent().isLocal()) {
            return;
        }
        FineLoggerFactory.getLogger().info("start to get solid content from server...");
        try {
            String content = requestContent();
            if (StringUtils.isNotEmpty(content)) {
                String cipherText = JSONFactory.createJSON(JSON.OBJECT, content).optString("data");
                if(StringUtils.isNotEmpty(cipherText)){
                    Map<String, Object> params = new HashMap<>();
                    params.put(ATTR_CIPHER_TEXT, cipherText);
                    params.put(ATTR_SIGNATURE, String.valueOf(CommonUtils.signature()));
                    HttpToolbox.post(SOLID_UPLOAD_URL, params);

                    String deleteUrl = WorkContext.getCurrent().getPath() + DELETE_URL;
                    HttpToolbox.post(deleteUrl, getParams());
                }
            }
            FineLoggerFactory.getLogger().info("send solid content to cloud center success.");
        } catch (Exception e) {
            FineLoggerFactory.getLogger().info(e.getMessage(), e);
        } finally {
            String unlockUrl = WorkContext.getCurrent().getPath() + UNLOCK_URL;
            try {
                HttpToolbox.post(unlockUrl, getParams());
            } catch (IOException e) {
                FineLoggerFactory.getLogger().warn(e.getMessage(), e);
            }
        }
    }

    /**
     * 获取服务端固化文件内容
     * @return 回传内容
     */
    public String requestContent() throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", JwtUtils.createDefaultJWT(REQUEST_SUBJECT, TIME_OUT));
        return HttpToolbox.get(WorkContext.getCurrent().getPath() + CONTENT_URL, params);
    }

    private Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("token", JwtUtils.createDefaultJWT(REQUEST_SUBJECT, TIME_OUT));
        return params;
    }
}
