package com.fr.design.icon;

import com.fr.stable.AssistUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;

import com.fr.base.CellBorderStyle;
import com.fr.base.GraphHelper;
import com.fr.design.constants.UIConstants;
import com.fr.stable.Constants;

public class BorderIcon implements Icon {

	private int width = 14;
    private int height = 14;
    public CellBorderStyle cellBorderStyle;

    public BorderIcon(CellBorderStyle cellBorderStyle) {
        this.cellBorderStyle = cellBorderStyle;
    }

    @Override
    public int getIconHeight() {
        return height;
    }

    @Override
    public int getIconWidth() {
        return width;

    }

    private void drawLine(Graphics g, double x1, double y1, double x2,
        double y2, int lineStyle, Color color) {
        g.setColor(color);
        x1--;
        x2--;
        y1--;
        y2--;
        if (lineStyle == Constants.LINE_MEDIUM
            || lineStyle == Constants.LINE_THICK) {
            lineStyle = Constants.LINE_MEDIUM;
            if (AssistUtils.equals(x1, x2)) {
                if (AssistUtils.equals(x1, y1)) {
                    GraphHelper.drawLine(g, x1, y1 - 1, x2, y2 + 1, lineStyle);
                } else {
                    GraphHelper.drawLine(g, x1, y1, x2, y2 + 1, lineStyle);
                }
            } else if (AssistUtils.equals(y1, y2)) {
                GraphHelper.drawLine(g, x1, y1, x2 + 1, y2, lineStyle);
            }
        } else if (lineStyle == Constants.LINE_THIN
            || lineStyle == Constants.LINE_DOUBLE) {
            GraphHelper.drawLine(g, x1, y1, x2, y2, lineStyle);
        } else {
            lineStyle = Constants.LINE_DOT;
            if (AssistUtils.equals(y1, x2) && AssistUtils.equals(x2, y2)) {
                GraphHelper.drawLine(g, x1, y1, x2 + 1, y2, lineStyle);
            } else {
                GraphHelper.drawLine(g, x1, y1, x2, y2, lineStyle);
            }
        }
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        int defaultWidth = c.getWidth();
        int defaultHeight = c.getHeight();
        int x1 = (defaultWidth - width) / 2;
        int x2 = (defaultWidth + width) / 2;
        int y1 = (defaultHeight - height) / 2;
        int y2 = (defaultHeight + height) / 2;
        Graphics2D gr = (Graphics2D) g;
        gr.setColor(UIConstants.NORMAL_BACKGROUND);
        gr.fillRect(x1, y1, width, height);
        drawLine(gr, x1, y1, x2, y1, cellBorderStyle.getTopStyle(),
            cellBorderStyle.getTopColor());
        drawLine(gr, x2, y1, x2, y2, cellBorderStyle.getRightStyle(),
            cellBorderStyle.getRightColor());
        drawLine(gr, x1, y2, x2, y2, cellBorderStyle.getBottomStyle(),
            cellBorderStyle.getBottomColor());
        drawLine(gr, x1, y1, x1, y2, cellBorderStyle.getLeftStyle(),
            cellBorderStyle.getLeftColor());
        drawLine(gr, defaultWidth / 2, y1, defaultWidth / 2, y2,
            cellBorderStyle.getVerticalStyle(), cellBorderStyle.getVerticalColor());
        drawLine(gr, x1, defaultHeight / 2, x2, defaultHeight / 2,
            cellBorderStyle.getHorizontalStyle(), cellBorderStyle.getHorizontalColor());
    }

}