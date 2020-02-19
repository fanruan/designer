package com.fr.design.mainframe.template.info;

import com.fr.design.mainframe.SiteCenterToken;
import com.fr.design.mainframe.chart.info.ChartInfo;
import com.fr.general.CloudCenter;
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
    private static final String CONSUMING_URL = CloudCenter.getInstance().acquireUrlByKind("tempinfo.consuming") + "/single";
    private static final String PROCESS_URL = CloudCenter.getInstance().acquireUrlByKind("tempinfo.process") + "/single";
    private static final String CHART_CONSUMING_URL = CloudCenter.getInstance().acquireUrlByKind("chartinfo.consuming") + "/single";

    private static boolean sendConsumingInfo(String content) {
        return sendSingleTemplateInfo(CONSUMING_URL, content);
    }

    private static boolean sendProcessInfo(String content) {
        return sendSingleTemplateInfo(PROCESS_URL, content);
    }

    static boolean sendTemplateInfo(TemplateInfo templateInfo) {
        return SendHelper.sendConsumingInfo(templateInfo.getConsumingMapJsonString()) && SendHelper.sendProcessInfo(templateInfo.getProcessMapJsonString());
    }

    public static boolean sendChartInfo(ChartInfo chartInfo) {
        return sendSingleTemplateInfo(CHART_CONSUMING_URL, chartInfo.getChartConsumingMapJsonString());
    }

    private static boolean sendSingleTemplateInfo(String url, String content) {
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
