package com.fr.design.mainframe.template.info;

import com.fr.design.mainframe.SiteCenterToken;
import com.fr.design.mainframe.burying.point.BasePointInfo;
import com.fr.general.ComparatorUtils;
import com.fr.general.http.HttpToolbox;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 负责向服务器发送信息
 * Created by plough on 2019/4/18.
 */
public class SendHelper {

    public static boolean sendPointInfo(BasePointInfo pointInfo) {
        boolean success = true;
        Map<String, String> sendInfo = pointInfo.getSendInfo();
        for (Map.Entry<String, String> entry : sendInfo.entrySet()) {
            if (!sendSinglePointInfo(entry.getKey(), entry.getValue())) {
                success = false;
            }
        }
        return success;
    }

    private static boolean sendSinglePointInfo(String url, String content) {
        Map<String, Object> para = new HashMap<>();
        para.put("token", SiteCenterToken.generateToken());
        para.put("content", content);

        try {
            String res = HttpToolbox.post(url, para);
            return ComparatorUtils.equals(new JSONObject(res).get("status"), "success");
        } catch (Throwable e) {
            // 客户不需要关心，错误等级为 debug 就行了
            FineLoggerFactory.getLogger().debug(e.getMessage(), e);
        }
        return false;
    }
}
