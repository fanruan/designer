package com.fr.design.style.color;

import com.fr.base.FRContext;
import com.fr.file.XMLFileManager;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.GeneralContext;
import com.fr.stable.EnvChangedListener;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLTools;
import com.fr.stable.xml.XMLableReader;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 最近使用颜色
 *
 * @author focus
 */
public class ColorSelectConfigManager extends XMLFileManager implements ColorSelectConfigManagerProvider {

    // 最近使用的颜色个数
    private int colorNums = 20;

    private static ColorSelectConfigManagerProvider configManager = null;
    private static ColorSelectConfigManager colorSelectConfigManager = null;
    private boolean init = true;
    // 最近使用颜色
    private List<Color> colors = new ArrayList<Color>();
    private static final String RECENT_COLOR_TAG = "RecentColors";
    private static final String COLOR_TAG = "Color";

    static {
        GeneralContext.addEnvChangedListener(new EnvChangedListener() {
            public void envChanged() {
                ColorSelectConfigManager.envChanged();
            }
        });
    }

    private static void envChanged() {
        configManager = null;
    }

    public Color[] getColors() {

        //初次打开软件时从xml文件中获取历史颜色信息
        if (init) {
            ColorSelectConfigManagerProvider manager = ColorSelectConfigManager.getProviderInstance();
            this.colors = manager.getColorsFromFile();
            init = false;
        }
        if (colors == null) {
            colors = new ArrayList<Color>();
        }
        return colors.toArray(new Color[colors.size()]);
    }

    public int getColorNum() {
        return colorNums;
    }

    public void setColorNum(int colorNums) {
        this.colorNums = colorNums;
    }

    public synchronized static ColorSelectConfigManager getInstance() {
        if (colorSelectConfigManager == null) {
            colorSelectConfigManager = new ColorSelectConfigManager();
        }
        return colorSelectConfigManager;
    }

    /**
     * 添加颜色到最近使用队列中
     *
     * @param color 颜色
     */
    public void addToColorQueue(Color color) {
        if(color == null){
            return;
        }
        // 过滤重复的最近使用颜色
        // 因为有个后进先出的问题，最近使用的颜色需要放到最前面所以没用set
        if (colors.contains(color)) {
            colors.remove(color);
        }
        colors.add(color);

        /*@author yaohwu*/
        //将历史颜色信息保存到xml文件中去
        ColorSelectConfigManagerProvider manager = ColorSelectConfigManager.getProviderInstance();
        if (colors != null && !colors.isEmpty()) {
            manager.setColorsToFile(colors);
        }
        try {
            FRContext.getCurrentEnv().writeResource(manager);
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }
    }


    /**
     * 读取配置文件流
     *
     * @param input 流
     * @throws Exception 异常
     */
    @Override
    public void readFromInputStream(InputStream input) throws Exception {
        ColorSelectConfigManager manager = new ColorSelectConfigManager();
        XMLTools.readInputStreamXML(manager, input);
        configManager = manager;
        FRContext.getCurrentEnv().writeResource(configManager);
    }


    /**
     * 获取配置管理接口
     *
     * @return 配置管理接口ConfigManagerProvider
     */
    public synchronized static ColorSelectConfigManagerProvider getProviderInstance() {
        if (configManager == null) {
            configManager = new ColorSelectConfigManager();
            configManager.readXMLFile();
        }
        return configManager;
    }
    
    
    public boolean writeResource() throws Exception {
        return FRContext.getCurrentEnv().writeResource(ColorSelectConfigManager.getProviderInstance());
    }

    public String fileName() {
        return "recentcolors.xml";
    }

    public void readXML(XMLableReader reader) {
        String name = reader.getTagName();
        if (reader.isChildNode()) {
            if (ComparatorUtils.equals(COLOR_TAG, name)) {
                Color color = null;
                colors.add(reader.getAttrAsColor("colors", color));
            }
        }
    }

    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(RECENT_COLOR_TAG);
        if (this.colors != null && !this.colors.isEmpty()) {
            for (int i = 0; i < this.colors.size(); i++) {
                writer.startTAG(COLOR_TAG);
                writer.attr("colors", colors.get(i).getRGB());
                writer.end();
            }
        }
        writer.end();
    }

    public List<Color> getColorsFromFile() {
        return this.colors;
    }

    public void setColorsToFile(List<Color> colors) {
        this.colors = colors;
    }
}
