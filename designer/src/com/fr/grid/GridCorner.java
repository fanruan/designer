/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.grid;

import com.fr.base.GraphHelper;
import com.fr.base.ScreenResolution;
import com.fr.design.constants.UIConstants;
import com.fr.design.mainframe.ElementCasePane;

import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * GridCorner used to paint and edit grid cornor.
 */
public class GridCorner extends BaseGridComponent {

    public GridCorner() {
        this.setOpaque(true);
        //james 清除所有的Key Action
        this.getInputMap().clear();
        this.getActionMap().clear();
        MouseInputListener l = new GridCornerMouseHandler(this);
        this.addMouseListener(l);
        this.addMouseMotionListener(l);
    }

    /**
     * Paints component
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        ElementCasePane reportPane = this.getElementCasePane();
        float time = (float) reportPane.getResolution() / ScreenResolution.getScreenResolution();
        //size
        Dimension size = this.getSize();
        Rectangle2D rect2D = new Rectangle2D.Double(0, 0, size.getWidth(), size.getHeight());
        //paint background.
        if (this.getBackground() != null) {
            g2d.setPaint(this.getBackground());
            GraphHelper.fill(g2d, rect2D);
        } else {
            g2d.setPaint(reportPane.getBackground());
            GraphHelper.fill(g2d, rect2D);
        }

        paintArc(g2d, size, time);

        //画左边的边框线.
        g2d.setColor(reportPane.getGridColumn().getSeparatorLineColor());
        GraphHelper.drawLine(g2d, 0, 0, 0, size.getHeight());

        //画上边的边框线.
        g2d.setColor(reportPane.getGridRow().getSeparatorLineColor());
        GraphHelper.drawLine(g2d, 0, 0, size.getWidth(), 0);
    }

    /**
     * Gets the preferred size.
     */
    @Override
    public Dimension getPreferredSize() {
        ElementCasePane reportPane = this.getElementCasePane();

        if (!reportPane.isColumnHeaderVisible() || !reportPane.isRowHeaderVisible()) {
            return new Dimension(0, 0);
        }

        return new Dimension(reportPane.getGridRow().getPreferredSize().width,
                reportPane.getGridColumn().getPreferredSize().height);
    }

    /**
     * Gets corner background.
     */
    @Override
    public Color getBackground() {
        return super.getBackground();
    }


    private void paintArc(Graphics2D g2d, Dimension size, float time) {
        g2d.setColor(UIConstants.LINE_COLOR);
        float height = 2 * time;
        float width = 2 * time;
        float hgap = 4 * time;
        float vgap = 3 * time;

        int x = (int) ((size.width - (hgap * 2 + width * 3)) / 2);
        int y = (int) (size.height - (vgap * 2 + height * 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                GraphHelper.fillRect(g2d, x + (i * hgap), y + (j * vgap), width, height);
            }
        }
    }
}