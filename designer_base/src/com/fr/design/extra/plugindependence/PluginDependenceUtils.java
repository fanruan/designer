package com.fr.design.extra.plugindependence;

import com.fr.plugin.dependence.PluginDependenceType;

/**
 * Created by hufan on 2016/8/31.
 */


public class PluginDependenceUtils {
    public static boolean installDependenceOnline(String currentID, String dependenceID, PluginDependenceType dependenceType, String dependenceDir) {
        DownLoadDependenceUI ui = new DownLoadDependenceUI(currentID, dependenceID, dependenceType, dependenceDir);
        return ui.installOnline();
    }
}
