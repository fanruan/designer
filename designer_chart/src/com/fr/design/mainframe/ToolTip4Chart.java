package com.fr.design.mainframe;

import com.fr.base.GraphHelper;
import com.fr.base.background.ColorBackground;
import com.fr.stable.CoreConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-25
 * Time: 下午4:11
 */
public class ToolTip4Chart extends JWindow {
    private static ToolTip4Chart instance = new ToolTip4Chart();
    private static final int HGAP = 5;
    private static final int VGAP = 3;
    private static final int FONT_SIZE = 12;
    private ToolTipStringPane stringPane;
    private Font font = new Font("Dialog", Font.PLAIN, FONT_SIZE);

    public ToolTip4Chart() {
        stringPane = new ToolTipStringPane();
        this.getContentPane().add(stringPane);
    }

    public static ToolTip4Chart getInstance() {
        if (instance == null) {
            instance = new ToolTip4Chart();
        }
        return instance;
    }

    /**
     * 现实提示信息
     *
     * @param toolTip 提示信息
     * @param xAbs    绝对位置x
     * @param yAbs    绝对位置Y
     */
    public void showToolTip(String toolTip, int xAbs, int yAbs) {
        stringPane.text = toolTip.trim();
        Dimension2D dim = GraphHelper.stringDimensionWithRotation(toolTip, font, 0, CoreConstants.DEFAULT_FRC);
        this.setSize(new Dimension((int) dim.getWidth() + HGAP * 2, (int) dim.getHeight() + VGAP * 2));
        stringPane.setPreferredSize(new Dimension((int) dim.getWidth(), (int) dim.getHeight()));
        if (!this.isVisible()) {
            this.setVisible(true);
            if (xAbs + this.getWidth() > Toolkit.getDefaultToolkit().getScreenSize().width) {
                xAbs -= this.getWidth();
            }
            this.setLocation(xAbs, yAbs+HGAP*2);
        }
    }

    /**
     * 隐藏弹出框
     */
    public void hideToolTip() {
        this.setVisible(false);
    }


    private class ToolTipStringPane extends JPanel {
        String text;

        public ToolTipStringPane() {

        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!isOpaque()) {
                return;
            }
            g.setFont(font);
            Rectangle r = new Rectangle(0, 0, this.getWidth(), this.getHeight());
            ColorBackground background = ColorBackground.getInstance(Color.white);
            background.paint(g, new RoundRectangle2D.Double(r.getX(), r.getY(), r.getWidth(), r.getHeight(), HGAP, HGAP));
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawString(text, HGAP, this.getHeight() - HGAP);
        }

    }

}