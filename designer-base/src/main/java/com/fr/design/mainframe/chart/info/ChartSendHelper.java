package com.fr.design.mainframe.chart.info;

import com.fr.design.mainframe.SiteCenterToken;
import com.fr.general.CloudCenter;
import com.fr.general.ComparatorUtils;
import com.fr.general.http.HttpToolbox;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bjorn
 * @version 10.0
 * Created by Bjorn on 2020-02-19
 */
class ChartSendHelper {
    private static final String CHART_CONSUMING_URL = CloudCenter.getInstance().acquireUrlByKind("chartinfo.consuming") + "/single";

    static boolean sendChartInfo(ChartInfo chartInfo) {
        return sendSingleChartInfo(CHART_CONSUMING_URL, chartInfo.getChartConsumingMapJsonString());
    }

    private static boolean sendSingleChartInfo(String url, String content) {
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
