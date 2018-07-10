package com.fr.design.style.color;

import com.fr.general.ComparatorUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLableReader;

import java.awt.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 最近使用颜色
 *
 * @author focus
 */
public class ColorSelectConfigManager implements XMLReadable {

    // 最近使用的颜色个数
    private int colorNums = 20;

    private static ColorSelectConfigManager colorSelectConfigManager = null;
    private boolean init = true;
    // 最近使用颜色
    private List<Color> colors = new ArrayList<Color>();
    private static final String RECENT_COLOR_TAG = "RecentColors";
    private static final String COLOR_TAG = "Color";


    public Color[] getColors() {

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
        ColorSelectConfigManager manager = ColorSelectConfigManager.getInstance();
        if (colors != null && !colors.isEmpty()) {
            manager.setColorsToFile(colors);
        }
    }

    public void readXML(XMLableReader reader) {
        reader.readXMLObject(new XMLReadable() {
            @Override
            public void readXML(XMLableReader reader) {
                String tagName = reader.getTagName();
                if (reader.isChildNode()) {
                    if (ComparatorUtils.equals(COLOR_TAG, tagName)) {
                        Color color = null;
                        colors.add(reader.getAttrAsColor("colors", color));
                    }
                }
            }
        });
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


    public void setColorsToFile(List<Color> colors) {
        this.colors = colors;
    }
}
