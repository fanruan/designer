package com.fr.design.mainframe.messagecollect.solid;

import com.fr.general.http.HttpToolbox;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;
import com.fr.workspace.WorkContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 设计器固化信息回传类
 * Created by alex sung on 2019/8/22.
 */
public class SolidCollector {
    private static final String CONTENT_URL = "/v10/collect/solid";
    private static final String DELETE_URL = "/v10/collect/solid/delete";

    /**
     * 回传文件给云中心，并删除服务端本地文件
     */
    public void sendToCloudCenterAndDeleteFile() {
        try {
            String content = requestContent();
            if(StringUtils.isNotEmpty(content)){
                String url = "";
                Map<String, Object> params = new HashMap<>();
                params.put("content", requestContent());
                HttpToolbox.post(url, params);

                String deleteUrl = WorkContext.getCurrent().getPath() + DELETE_URL;
                HttpToolbox.post(deleteUrl, new HashMap<String, Object>());
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    /**
     * 获取服务端固化文件内容
     * @return 回传内容
     */
    public String requestContent() {
        String content = null;
        try {
            String url = WorkContext.getCurrent().getPath() + CONTENT_URL;
            content = HttpToolbox.get(url);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return content;
    }
}
