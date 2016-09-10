package com.fr.design.extra.plugindependence;


import com.fr.plugin.dependence.PluginDependenceException;
import com.fr.plugin.dependence.PluginDependenceUnit;

import java.util.List;

/**
 * Created by hufan on 2016/8/31.
 */


public class PluginDependenceUtils {
    public static void installDependenceOnline(String currentID, List<PluginDependenceUnit> list) throws PluginDependenceException{
        DownLoadDependenceUI ui = new DownLoadDependenceUI(currentID, list);
        ui.installOnline();
    }
}
