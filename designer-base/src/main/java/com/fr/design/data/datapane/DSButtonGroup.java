package com.fr.design.data.datapane;

import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.mainframe.WestRegionContainerPane;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: pony
 * Date: 13-4-27
 * Time: 上午10:32
 * To change this template use File | Settings | File Templates.
 */
public class DSButtonGroup extends UIButtonGroup<Integer> {

    public DSButtonGroup(Icon[] iconArray, Integer[] objects) {
        super(iconArray, objects);
    }




    protected void paintBorder(Graphics g) {
        int tabledatatreepaneWidth = WestRegionContainerPane.getInstance().getWidth()-10;
        int labelX = 54 - (290 - tabledatatreepaneWidth) / 4 - 1;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(UIConstants.LINE_COLOR);

        int width = 0;

        width += labelButtonList.get(0).getWidth() + 1;
        int height = labelButtonList.get(0).getHeight();
        g.drawLine(width + labelX, 0, width + labelX, height);

        width += labelButtonList.get(labelButtonList.size() - 1).getWidth() + 1;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawRoundRect( labelX, 0, width, getHeight() - 1, UIConstants.ARC, UIConstants.ARC);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);


        g2d.drawLine(labelX / 2, getHeight() / 2, width + 3 * labelX / 2 , getHeight() / 2);
    }

}