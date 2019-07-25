package com.fr.design.gui.itree.filetree;

import com.fr.base.FRContext;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.mainframe.App;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by alex sung on 2019/7/23.
 */
public class FileNodeConstants {
    public static String[] SUPPORT_FILE_TYPES;

    static {
        List<String> supportFileType = new ArrayList<>(Arrays.asList(FRContext.getFileNodes().getSupportedTypes()));
        //通过插件扩展的
        Set<App> apps = ExtraDesignClassManager.getInstance().getArray(App.MARK_STRING);
        for(App app: apps){
            supportFileType.addAll(Arrays.asList(app.defaultExtensions()));
        }
        SUPPORT_FILE_TYPES = supportFileType.toArray(new String[0]);
    }
}
