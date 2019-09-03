package com.fr.design.mainframe.messagecollect.solid;

import com.fr.general.CloudCenter;
import com.fr.general.http.HttpToolbox;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.CommonUtils;
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
        try {
            String content = requestContent();
            if(StringUtils.isNotEmpty(content)){
                Map<String, Object> params = new HashMap<>();
                params.put(ATTR_CIPHER_TEXT, requestContent());
                params.put(ATTR_SIGNATURE, String.valueOf(CommonUtils.signature()));
                HttpToolbox.post(SOLID_UPLOAD_URL, params);

                String deleteUrl = WorkContext.getCurrent().getPath() + DELETE_URL;
                HttpToolbox.post(deleteUrl, new HashMap<String, Object>());
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().info(e.getMessage(), e);
        }
    }

    /**
     * 获取服务端固化文件内容
     * @return 回传内容
     */
    public String requestContent() throws Exception{
        return HttpToolbox.get(WorkContext.getCurrent().getPath() + CONTENT_URL);
    }
}
