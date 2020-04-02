package com.fr.design.mainframe.messagecollect;

import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.errorinfo.ErrorInfo;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;

/**
 *
 * @author hades
 * @version 10.0
 * Created by hades on 2020/1/8
 */
public class StartErrorMessageCollector {

    private static final StartErrorMessageCollector INSTANCE = new StartErrorMessageCollector();

    private String uuid;
    private String activeKey;


    public static StartErrorMessageCollector getInstance() {
        return INSTANCE;
    }

    private StartErrorMessageCollector() {
        DesignerEnvManager envManager = DesignerEnvManager.getEnvManager();
        this.uuid = envManager.getUUID();
        this.activeKey = envManager.getActivationKey();
    }

    public void record(String id, String msg, String detail) {
        FineLoggerFactory.getLogger().error(id +  ": " + msg + ", detail: " + detail);
        ErrorInfo errorInfo = new ErrorInfo(StringUtils.EMPTY, uuid, activeKey);
        errorInfo.setLogid(id);
        errorInfo.setLog(msg);
        errorInfo.setStackTrace(detail);
        errorInfo.saveAsJSON();
    }

    public void record(String id, String msg) {
        record(id, msg, StringUtils.EMPTY);
    }
}
