package com.fr.dependservice;

import com.fr.base.FRContext;
import com.fr.general.FRLogger;
import com.fr.plugin.dependence.PluginServiceCreator;

import java.util.HashMap;

/**
 * Created by hufan on 2016/9/9.
 */
public abstract class PluginService implements PluginServiceCreator {
    @Override
    public String remoteServiceAction(String req) throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_get_plugin_service_data");
        para.put("serviceID", getServiceID());
        para.put("req", req);
        return PluginServiceUtils.getDataFormRemote(para);
    }
    /**
     * 服务ID
     * @return
     */
    protected abstract String getServiceID();

    /**
     * 获取数据
     * @param req
     * @return
     */
    @Override
    public String fetchServiceData(String req) throws Exception {
        return FRContext.getCurrentEnv().pluginServiceAction(this, req);
    }
}
