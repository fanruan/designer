package com.fr.design.designer.beans.painters;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;

import com.fr.design.designer.beans.adapters.layout.FRBorderLayoutAdapter;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWBorderLayout;
import com.fr.design.form.layout.FRBorderLayout;
import com.fr.form.ui.container.WBorderLayout;

public class FRBorderLayoutPainter extends AbstractPainter {

    public FRBorderLayoutPainter(XLayoutContainer container) {
        super(container);
    }

    @Override
    public void paint(Graphics g, int startX, int startY) {
        if(hotspot_bounds == null){
            return;
        }
    	super.paint(g, startX, startY);
        int x = hotspot.x;
        int y = hotspot.y;
        int hotspot_x = hotspot_bounds.x;
        int hotspot_y = hotspot_bounds.y;
        int hotspot_w = hotspot_bounds.width;
        int hotspot_h = hotspot_bounds.height;
        FRBorderLayoutAdapter adapter = (FRBorderLayoutAdapter) container.getLayoutAdapter();
        FRBorderLayout layout = (FRBorderLayout) container.getLayout();     
        boolean accept = adapter.accept(creator, x - hotspot_x, y - hotspot_y);
        WBorderLayout wLayout = ((XWBorderLayout)container).toData();
        int northHeight = wLayout.getNorthSize();
        int southHeight = wLayout.getSouthSize();
        int eastWidth = wLayout.getEastSize();
        int westWidth = wLayout.getWestSize();
        
        int really_x = hotspot_x;
        int really_y = hotspot_y;
        int really_w = hotspot_w;
        int really_h = hotspot_h;

        if (y < (hotspot_y + northHeight)) {
            // NORTH
            really_x = hotspot_x;
            really_y = hotspot_y;
            really_w = hotspot_w;
            really_h = northHeight;
        } else if ((y >= (hotspot_y + northHeight)) && (y < ((hotspot_y + hotspot_h) - southHeight))) {
            if (x < (hotspot_x + westWidth)) {
                // WEST
                Component north = layout.getLayoutComponent(BorderLayout.NORTH);
                Component south = layout.getLayoutComponent(BorderLayout.SOUTH);
                really_x = hotspot_x;
                really_y = hotspot_y;

                if (north != null) {
                    really_y += northHeight;
                }

                really_w = westWidth;
                really_h = hotspot_h;

                if (north != null) {
                    really_h -= northHeight;
                }

                if (south != null) {
                    really_h -= southHeight;
                }
            } else if ((x >= (hotspot_x + westWidth)) && (x < ((hotspot_x + hotspot_w) - eastWidth))) {
                // CENTER
                Component north = layout.getLayoutComponent(BorderLayout.NORTH);
                Component south = layout.getLayoutComponent(BorderLayout.SOUTH);
                Component east = layout.getLayoutComponent(BorderLayout.EAST);
                Component west = layout.getLayoutComponent(BorderLayout.WEST);
                really_x = hotspot_x;

                if (west != null) {
                    really_x += westWidth;
                }

                really_y = hotspot_y;

                if (north != null) {
                    really_y += northHeight;
                }

                really_w = hotspot_w;

                if (west != null) {
                    really_w -= westWidth;
                }

                if (east != null) {
                    really_w -= eastWidth;
                }

                really_h = hotspot_h;

                if (north != null) {
                    really_h -= northHeight;
                }

                if (south != null) {
                    really_h -= southHeight;
                }
            } else {
                // EAST
                Component north = layout.getLayoutComponent(BorderLayout.NORTH);
                Component south = layout.getLayoutComponent(BorderLayout.SOUTH);

                really_x = (hotspot_x + hotspot_w) - eastWidth;
                really_y = hotspot_y;

                if (north != null) {
                    really_y += northHeight;
                }

                really_w = eastWidth;
                really_h = hotspot_h;

                if (north != null) {
                    really_h -= northHeight;
                }

                if (south != null) {
                    really_h -= southHeight;
                }
            }
        } else {
            // SOUTH
            really_x = hotspot_x;
            really_y = (hotspot_y + hotspot_h) - southHeight;
            really_w = hotspot_w;
            really_h = southHeight;
        }

        drawHotspot(g, really_x, really_y, really_w, really_h, accept);
    }
}