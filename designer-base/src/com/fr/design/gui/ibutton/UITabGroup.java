package com.fr.design.gui.ibutton;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class UITabGroup extends UIButtonGroup<Integer> {
    private boolean isOneLineTab = false;
    private boolean isDrawLine = true;
    private static final int BUTTON_NUMBER = 5;
    private static final int SEVEN_NUMBER = 7;
    private static final int ORIGINAL_WIDTH = 10;
    private static final int GAP = 11;

    /**
     * 标签页改变
     *
     * @param index 序号
     */
    public void tabChanged(int index) {
        return;
    }

    public UITabGroup(Icon[] iconArray) {
        super(iconArray, new Integer[iconArray.length]);
        setSelectedIndex(0);
        tabChanged(0);
    }

    public UITabGroup(String[] textArray) {
        super(textArray);
    }

    @Override
    protected LayoutManager getGridLayout(int number) {
        if (number < BUTTON_NUMBER || isOneLineTab) {
            return super.getGridLayout(number);
        } else if (number == BUTTON_NUMBER || number == SEVEN_NUMBER) {
            return new FiveButtonLayout(2);
        } else {
            return new GridLayout(2, 3, 1, 1);
        }
    }

    public void setOneLineTab(boolean isOneLineTab) {
        this.isOneLineTab = isOneLineTab;
    }

    protected boolean isDrawLine() {
        return isDrawLine;
    }

    public void setDrawLine(boolean isDrawLine) {
        this.isDrawLine = isDrawLine;
    }

    @Override
    protected Border getGroupBorder() {
        if (!isDrawLine) {
            return BorderFactory.createEmptyBorder(0, 0, 0, 0);
        }
        return BorderFactory.createEmptyBorder(1, GAP, 1, GAP);
    }

    @Override
    protected void setSelectedWithFireChanged(int newSelectedIndex) {
        if (selectedIndex != newSelectedIndex) {
            selectedIndex = newSelectedIndex;
            for (int i = 0; i < labelButtonList.size(); i++) {
                labelButtonList.get(i).setSelected(i == selectedIndex);
            }
        }
        tabChanged(newSelectedIndex);
    }
}