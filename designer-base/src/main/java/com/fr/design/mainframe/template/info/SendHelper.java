package com.fr.design.mainframe.template.info;

import com.fr.design.mainframe.SiteCenterToken;
import com.fr.general.CloudCenter;
import com.fr.general.ComparatorUtils;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONObject;

import java.util.HashMap;

/**
 * 负责向服务器发送信息
 * Created by plough on 2019/4/18.
 */
class SendHelper {
    private static final String CONSUMING_URL = CloudCenter.getInstance().acquireUrlByKind("tempinfo.consuming") + "/single";
    private static final String PROCESS_URL = CloudCenter.getInstance().acquireUrlByKind("tempinfo.process") + "/single";

    private static boolean sendConsumingInfo(String content) {
        return sendSingleTemplateInfo(CONSUMING_URL, content);
    }

    private static boolean sendProcessInfo(String content) {
        return sendSingleTemplateInfo(PROCESS_URL, content);
    }

    static boolean sendTemplateInfo(TemplateInfo templateInfo) {
        return SendHelper.sendConsumingInfo(templateInfo.getConsumingMapJsonString()) && SendHelper.sendProcessInfo(templateInfo.getProcessMapJsonString());
    }

    private static boolean sendSingleTemplateInfo(String url, String content) {
        HashMap<String, String> para = new HashMap<>();
        para.put("token", SiteCenterToken.generateToken());
        para.put("content", content);
        HttpClient httpClient = new HttpClient(url, para, true);
        httpClient.setTimeout(5000);
        httpClient.asGet();

        if (!httpClient.isServerAlive()) {
            return false;
        }

        String res = httpClient.getResponseText();
        boolean success;
        try {
            success = ComparatorUtils.equals(new JSONObject(res).get("status"), "success");
        } catch (Exception ex) {
            success = false;
        }
        return success;
    }
}
