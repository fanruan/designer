package com.fr.design.extra.plugindependence;

/**
 * Created by hufan on 2016/8/31.
 */
import com.fr.design.extra.PluginConstants;
import com.fr.design.extra.PluginHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.general.SiteCenter;
import com.fr.general.http.HttpClient;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import javax.swing.*;


public class DownLoadDependenceUtils {
    public static boolean preDependenceOnline(String dependenceID, String dir) {
        DownLoadDependenceUI ui = new DownLoadDependenceUI(dependenceID, dir);
        return ui.preOnline();
    }
}
