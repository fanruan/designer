package com.fr.dependservice;

import com.fr.base.FRContext;
import com.fr.env.RemoteEnv;
import com.fr.general.IOUtils;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by hufan on 2016/9/9.
 */
public class PluginServiceUtils {
    public static String getDataFormRemote(HashMap<String, String> para) throws Exception {
        InputStream inputStream = ((RemoteEnv)FRContext.getCurrentEnv()).getDataFormRemote(para);
        return IOUtils.inputStream2String(inputStream);
    }
}
