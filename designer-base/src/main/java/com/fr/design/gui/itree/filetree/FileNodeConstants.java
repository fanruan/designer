package com.fr.design.gui.itree.filetree;

import com.fr.base.FRContext;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.mainframe.App;
import com.fr.general.GeneralContext;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.injectable.PluginModule;
import com.fr.plugin.manage.PluginFilter;
import com.fr.plugin.observer.PluginEvent;
import com.fr.plugin.observer.PluginEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by alex sung on 2019/7/23.
 */
public class FileNodeConstants {

    private static List<String> supportFileType = new ArrayList<String>(Arrays.asList(FRContext.getFileNodes().getSupportedTypes()));

    static {
        GeneralContext.listenPluginRunningChanged(new PluginEventListener() {
            @Override
            public void on(PluginEvent pluginEvent) {
                supportFileType = new ArrayList<String>(Arrays.asList(FRContext.getFileNodes().getSupportedTypes()));
                //通过插件扩展的
                Set<App> apps = ExtraDesignClassManager.getInstance().getArray(App.MARK_STRING);
                for (App app : apps) {
                    addAppExtensions(app.defaultExtensions());
                }
            }
        }, new PluginFilter() {
            @Override
            public boolean accept(PluginContext pluginContext) {
                return pluginContext.contain(PluginModule.ExtraDesign);
            }
        });
    }

    private static void addAppExtensions(String[] extensions) {
        for (int i = 0, size = extensions.length; i < size; i++) {
            if (!supportFileType.contains(extensions[i])) {
                supportFileType.add(extensions[i]);
            }
        }
    }

    public static String[] getSupportFileTypes() {
        return supportFileType.toArray(new String[0]);
    }
}
