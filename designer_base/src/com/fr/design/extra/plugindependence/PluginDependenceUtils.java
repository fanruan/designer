package com.fr.design.extra.plugindependence;


/**
 * Created by hufan on 2016/8/31.
 */


public class PluginDependenceUtils {
    public static boolean installDependenceOnline(String currentID, String dependenceID, String dependenceDir) {
        DownLoadDependenceUI ui = new DownLoadDependenceUI(currentID, dependenceID, dependenceDir);
        return ui.installOnline();
    }
}
