package com.fr.design.extra.pre4plugin;

import com.fr.general.FRLogger;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hufan on 2016/9/1.
 */
public class PluginFactory {
    /**
     *
     * 标签Map
     */
    private static Map<String, Class<? extends PreEnv4Plugin>> pluginMap = new HashMap<String, Class<? extends PreEnv4Plugin>>();
    static {
        pluginMap.put("com.fr.plugin.chart.vancharts", InstallPhantomJs.class);
    }

    public static PreEnv4Plugin createPreEnv(String pluginID) {
        if(pluginMap.containsKey(pluginID)){
            try{
                Class<? extends PreEnv4Plugin> cl = pluginMap.get(pluginID);
                Constructor<? extends PreEnv4Plugin > constructor = cl.getConstructor();
                return constructor.newInstance();
            } catch (Exception e){
                FRLogger.getLogger().error(e.getMessage());
            }
        }
        return new NoneEnv4Plugin();
    }
}
