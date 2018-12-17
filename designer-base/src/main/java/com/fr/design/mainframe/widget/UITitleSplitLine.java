package com.fr.design.mainframe.widget;

import com.fr.design.gui.ilable.UILabel;
import com.fr.general.FRFont;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class UITitleSplitLine extends JPanel {
    private static final Color LINE_COLOR = Color.decode("#E0E0E1");
    private static final Color TITLE_COLOR = Color.decode("#333334");
    private static final FRFont TITLE_FONT = FRFont.getInstance("PingFangSC-Regular", 0, 12.0F);
    private static final int OFFSETX = 10;
    private static final int OFFSET = 3;
    private Color color;
    private UILabel label;
    private int width;



    public UITitleSplitLine(String title, int width) {
        this(title, LINE_COLOR, width);
    }

    public UITitleSplitLine(String title, Color color, int width) {
        super();
        this.color = color;
        this.width = width;
        this.label = new UILabel(title);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Dimension size = label.getPreferredSize();
        int labelH = size.height;
        g.setColor(color);
        g.drawLine(0, labelH / 2, OFFSETX, labelH / 2);
        g.drawLine(OFFSETX + size.width + OFFSET * 2, labelH / 2, width, labelH / 2);
        g.translate(OFFSETX + OFFSET, 0);
        label.setFont(TITLE_FONT);
        label.setForeground(TITLE_COLOR);
        label.setSize(size.width, size.height);
        label.paint(g);
        g.translate(-OFFSETX - OFFSET, 0);
    }



}
