package com.fr.design.extra;

import com.fr.base.FRContext;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by vito on 16/4/22.
 */
public class PluginStoreConstants {

    public static final String VERSION = loadAttribute("VERSION", "");

    private static Properties PROP = null;

    private static String loadAttribute(String key, String defaultValue) {
        if (PROP == null) {
            PROP = new Properties();
            try {
                PROP.load(new FileInputStream(StableUtils.pathJoin(FRContext.getCurrentEnv().getWebReportPath(), "scripts/store/web/plugin_store.properties")));
            } catch (Exception e) {
            }
        }
        String p = PROP.getProperty(key);
        if (StringUtils.isEmpty(p)) {
            p = defaultValue;
        }
        return p;
    }

}
