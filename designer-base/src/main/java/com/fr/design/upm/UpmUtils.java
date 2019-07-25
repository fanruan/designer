package com.fr.design.upm;

import com.fr.common.annotations.Negative;
import com.fr.config.ServerPreferenceConfig;
import com.fr.general.CloudCenter;
import com.fr.stable.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-04-18
 */
public class UpmUtils {

    public static String[] findMatchedExtension(String... extensions) {
        List<String> list = new ArrayList<>();
        for (String ext : extensions) {
            String[] arr = ext.split("\\.");
            list.add(arr[arr.length - 1]);
        }
        return list.toArray(new String[0]);
    }

    @Negative(until = "2019-08-30")
    public static Map<String, String> renderMap() {
        Map<String, String> map4Tpl = new HashMap<>();
        map4Tpl.put("version", ServerPreferenceConfig.getInstance().getOptimizedUPMVersion());
        map4Tpl.put("new_version", fetchLatestVersion());
        return map4Tpl;
    }

    private static String fetchLatestVersion() {
        String version = CloudCenter.getInstance().acquireUrlByKind("upm.script.version");
        if (StringUtils.isBlank(version)) {
            version = "1.0";
        }
        return version;
    }
}
