package com.fr.design.mainframe.messagecollect;

import com.fr.concurrent.NamedThreadFactory;
import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.SiteCenterToken;
import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.Listener;
import com.fr.general.CloudCenter;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralUtils;
import com.fr.general.http.HttpToolbox;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.module.ModuleContext;
import com.fr.module.engine.FineModule;
import com.fr.runtime.FineRuntime;
import com.fr.stable.StringUtils;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;

/**
 * 启动信息收集
 *
 * @author vito
 * @version 10.0
 * Created by vito on 2019/9/4
 */
public class StartupMessageCollector {

    private static final String XML_STARTUP_TIME = "t";
    private static final String XML_STARTUP_LOG = "startupLog";
    private static final String XML_STARTUP_MEMORY = "designerMemory";
    private static final String XML_STARTUP_COST = "cost";
    private static final String XML_UUID = "UUID";
    private static final String XML_BUILD_NO = "buildNO";
    private static final String STARTUP_URL_KEY = "user.info.v10.startup";
    private static final String LOG_TYPE = "single";
    private static final int BYTE_TO_MB = 1024 * 1024;

    private static final StartupMessageCollector INSTANCE = new StartupMessageCollector();

    private StartupMessageCollector() {
    }

    public static StartupMessageCollector getInstance() {
        return INSTANCE;
    }

    public void recordStartupLog() {
        EventDispatcher.listen(FineRuntime.ApplicationEvent.AFTER_START, new Listener<Long>() {

            @Override
            public void on(Event event, Long param) {
                final String url = CloudCenter.getInstance().acquireUrlByKind(STARTUP_URL_KEY);
                if (StringUtils.isEmpty(url)) {
                    return;
                }
                ExecutorService es = ModuleContext.getExecutor()
                        .newSingleThreadExecutor(new NamedThreadFactory("StartupMessageCollector"));
                es.submit(new Runnable() {
                    @Override
                    public void run() {
                        FineModule root = (FineModule) ModuleContext.getRoot().getRoot();
                        JSONObject profile = root.profile();
                        if (profile.isEmpty()) {
                            return;
                        }
                        JSONObject json = JSONObject.create()
                                .put(XML_UUID, DesignerEnvManager.getEnvManager().getUUID())
                                .put(XML_BUILD_NO, GeneralUtils.readBuildNO())
                                .put(XML_STARTUP_TIME, FineRuntime.getAppStartTime() + FineRuntime.getStartingTime())
                                .put(XML_STARTUP_COST, FineRuntime.getStartingTime())
                                .put(XML_STARTUP_LOG, profile)
                                .put(XML_STARTUP_MEMORY, Runtime.getRuntime().maxMemory() / BYTE_TO_MB);
                        sendInfo(json, url + LOG_TYPE);
                    }
                });
                es.shutdown();
            }
        });
    }

    private boolean sendInfo(JSONObject content, String url) {
        boolean success = false;
        try {
            HashMap<String, Object> para = new HashMap<>();
            para.put("token", SiteCenterToken.generateToken());
            para.put("content", content);
            String res = HttpToolbox.post(url, para);
            success = ComparatorUtils.equals(new JSONObject(res).get("status"), "success");
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return success;
    }
}
