package com.fr.design.extra;

import com.fr.stable.StableUtils;
import com.fr.workspace.WorkContext;

/**
 * Created by ibm on 2017/5/25.
 */
public class PluginConstants {
    public static final int BYTES_NUM = 1024;
    public static final String DOWNLOAD_PATH = StableUtils.pathJoin(WorkContext.getCurrent().getPath() + "/cache");
    public static final String TEMP_FILE = "temp.zip";
    public static final String CONNECTION_404 = "404";

}
