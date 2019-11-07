package com.fr.design.chart;

import com.fr.base.GraphHelper;
import com.fr.general.IOUtils;
import com.fr.stable.Constants;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLable;
import com.fr.stable.xml.XMLableReader;

import javax.swing.Icon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;

/**
 * 图表的缩略图Icon, 在选择图表类型界面 用到.
 */
public class ChartIcon implements Icon, XMLable {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 225;
    /**
     * 缩略图中的图片路径
     */
    private String imagePath;

    private String chartName;

    /**
     * 构造Chart的缩略图Icon
     */
    public ChartIcon(String imagePath, String chartName) {
        this.imagePath = imagePath;
        this.chartName = chartName;
    }

    /**
     * 画出缩略图Icon
     *
     * @param g 图形的上下文
     * @param c 所在的Component
     * @param x 缩略图的起始坐标x
     * @param y 缩略图的起始坐标y
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {

        Graphics2D g2d = (Graphics2D) g;

        Paint oldPaint = g2d.getPaint();

        g.translate(x, y);
        g2d.setPaint(Color.white);

        g2d.fillRect(0, 0, getIconWidth(), getIconHeight());
        BufferedImage demoImage = IOUtils.readImageWithCache(imagePath);
        GraphHelper.paintImage(g, getIconWidth(), getIconHeight(), demoImage, Constants.IMAGE_ADJUST, Constants.NULL, Constants.NULL, -1, -1);

        g.translate(-x, -y);
        g2d.setPaint(oldPaint);
    }

    /**
     * 返回缩略图的宽度
     *
     * @return int 缩略图宽度
     */
    public int getIconWidth() {
        return WIDTH;
    }

    /**
     * 返回缩略图的高度
     *
     * @return int 缩略图高度
     */
    public int getIconHeight() {
        return HEIGHT;
    }


    /**
     * 返回缩略图中的图片路径
     *
     * @return 缩略图中的图片路径
     */
    public String getImagePath() {
        return imagePath;
    }

    public String getChartName() {
        return chartName;
    }


    public void readXML(XMLableReader reader) {

    }

    public void writeXML(XMLPrintWriter writer) {

    }

    /**
     * @return 克隆后的对象
     * @throws CloneNotSupportedException 如果克隆失败则抛出此异常
     */
    public Object clone() throws CloneNotSupportedException {
        ChartIcon cloned = (ChartIcon) super.clone();
        cloned.imagePath = this.imagePath;
        cloned.chartName = this.chartName;
        return cloned;
    }

}