package com.fr.design.mainframe.messagecollect.solid;

import com.fr.analysis.cloud.solid.SolidRecordExecutor;
import com.fr.general.CloudCenter;
import com.fr.general.http.HttpToolbox;
import com.fr.log.FineLoggerFactory;
import com.fr.rpc.ExceptionHandler;
import com.fr.rpc.RPCInvokerExceptionInfo;
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
            String cipherText = requestContent();
            if (StringUtils.isNotEmpty(cipherText)) {
                Map<String, Object> params = new HashMap<>();
                params.put(ATTR_CIPHER_TEXT, cipherText);
                params.put(ATTR_SIGNATURE, String.valueOf(CommonUtils.signature()));
                HttpToolbox.post(SOLID_UPLOAD_URL, params);

                deleteSolidFile();
            }
            FineLoggerFactory.getLogger().info("send solid content to cloud center success.");
        } catch (Exception e) {
            FineLoggerFactory.getLogger().info(e.getMessage(), e);
        } finally {
            try {
                unlockSolidFile();
            } catch (Exception e) {
                FineLoggerFactory.getLogger().warn(e.getMessage(), e);
            }
        }
    }

    /**
     * 获取服务端固化文件内容
     *
     * @return 回传内容
     */
    private String requestContent() throws Exception {
        return WorkContext.getCurrent().get(SolidRecordExecutor.class, new ExceptionHandler<String>() {
            @Override
            public String callHandler(RPCInvokerExceptionInfo info) {
                FineLoggerFactory.getLogger().error(info.getException().getMessage(), info.getException());
                return StringUtils.EMPTY;
            }
        }).feedBackTask(StringUtils.EMPTY);
    }

    /**
     * 删除服务端固化文件
     */
    private void deleteSolidFile() throws Exception {
        WorkContext.getCurrent().get(SolidRecordExecutor.class, new ExceptionHandler<Void>() {
            @Override
            public Void callHandler(RPCInvokerExceptionInfo info) {
                FineLoggerFactory.getLogger().error(info.getException().getMessage(), info.getException());
                return null;
            }
        }).deleteSolidFile();
    }

    /**
     * unlock
     */
    private void unlockSolidFile() throws Exception {
        WorkContext.getCurrent().get(SolidRecordExecutor.class, new ExceptionHandler<Void>() {
            @Override
            public Void callHandler(RPCInvokerExceptionInfo info) {
                FineLoggerFactory.getLogger().error(info.getException().getMessage(), info.getException());
                return null;
            }
        }).unlockSolidFile();
    }
}
