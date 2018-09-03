package com.fr.design.style.color;

import com.fr.general.ComparatorUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLableReader;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * 最近使用颜色
 *
 * @author focus
 */
public class ColorSelectConfigManager implements XMLReadable {

    // 最近使用的颜色个数
    private int colorNum = 20;

    // 最近使用颜色
    private List<Color> colors = new ArrayList<Color>();
    private static final String RECENT_COLOR_TAG = "RecentColors";
    private static final String COLOR_TAG = "Color";


    public Color[] getColors() {

        return colors.toArray(new Color[colors.size()]);
    }

    public int getColorNum() {
        return colorNum;
    }

    public void setColorNum(int colorNum) {
        this.colorNum = colorNum;
    }

    /**
     * 添加颜色到最近使用队列中
     *
     * @param color 颜色
     */
    public void addToColorQueue(Color color) {

        // 将透明度不为 0% 的 颜色去掉
        if (color == null || color.getAlpha() != 0xff) {
            return;
        }

        // 过滤重复的最近使用颜色
        // 最近使用的颜色需要放到最前面
        colors.remove(color);
        colors.add(color);

    }

    public void readXML(XMLableReader reader) {
        reader.readXMLObject(new XMLReadable() {
            @Override
            public void readXML(XMLableReader reader) {
                String tagName = reader.getTagName();
                if (reader.isChildNode()) {
                    if (ComparatorUtils.equals(COLOR_TAG, tagName)) {
                        Color color = reader.getAttrAsColor("colors", null);
                        // 将透明度不为 0% 的 颜色去掉
                        if (color != null && color.getAlpha() == 0xff) {
                            colors.add(color);
                        }
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
