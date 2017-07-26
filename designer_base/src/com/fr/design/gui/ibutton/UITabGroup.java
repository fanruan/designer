package com.fr.design.gui.ibutton;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.border.Border;

import com.fr.design.constants.UIConstants;

public class UITabGroup extends UIButtonGroup<Integer> {
    private boolean isOneLineTab = false;
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

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(UIConstants.LINE_COLOR);
        if (!isTwoLine()) {
            int width = isDrawLine() ? ORIGINAL_WIDTH : 0;
            for (int i = 0; i < labelButtonList.size() - 1; i++) {
                width += labelButtonList.get(i).getWidth() + 1;
                int height = labelButtonList.get(i).getHeight();
                g.drawLine(width, 0, width, height);
            }

            width += labelButtonList.get(labelButtonList.size() - 1).getWidth() + 1;
            if (isDrawLine()) {
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.drawRoundRect(ORIGINAL_WIDTH, 0, width - ORIGINAL_WIDTH, getHeight() - 1, UIConstants.ARC, UIConstants.ARC);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            } else {
                g2d.drawRect(0, 0, width, getHeight() - 1);
            }

        } else if (labelButtonList.size() % 2 != 0) {
            paintOddNumberButtons(g, g2d);
        } else {
            int width = ORIGINAL_WIDTH;
            int number = labelButtonList.size() / 2;
            for (int i = 0; i < number - 1; i++) {
                width += labelButtonList.get(i).getWidth() + 1;
                int height = labelButtonList.get(i).getHeight() * 2 + 1;
                g.drawLine(width, 0, width, height);
            }
            width += labelButtonList.get(number - 1).getWidth() + 1;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawRoundRect(ORIGINAL_WIDTH, 0, width - ORIGINAL_WIDTH, getHeight() - 1, UIConstants.ARC, UIConstants.ARC);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        }
        if (isDrawLine()) {
            g2d.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
        }
    }

    private void paintOddNumberButtons(Graphics g, Graphics2D g2d) {
        int width = ORIGINAL_WIDTH;
        int buttonHeight = labelButtonList.get(0).getHeight();
        int upButtonCount = labelButtonList.size() / 2 + 1;
        for (int i = 0; i < upButtonCount - 1; i++) {
            width += labelButtonList.get(i).getWidth() + 1;
            int height = labelButtonList.get(i).getHeight() + 1;
            g.drawLine(width, 0, width, height);
        }
        width += labelButtonList.get(upButtonCount - 1).getWidth() + 1;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawRoundRect(ORIGINAL_WIDTH, 0, width - ORIGINAL_WIDTH, buttonHeight + 1, UIConstants.ARC, UIConstants.ARC);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        int line2X = labelButtonList.get(upButtonCount).getX();
        int line2Y = labelButtonList.get(upButtonCount).getY();
        width = line2X;
        width += labelButtonList.get(upButtonCount).getWidth();
        int height = labelButtonList.get(upButtonCount).getHeight() + 1;
        g.drawLine(width, line2Y, width, line2Y + height);
        for (int i = upButtonCount + 1; i < labelButtonList.size() - 1; i++) {
            width += labelButtonList.get(i).getWidth() + 1;
            height = labelButtonList.get(i).getHeight() + 1;
            g.drawLine(width, line2Y, width, line2Y + height);
        }
        width += labelButtonList.get(upButtonCount).getWidth() + 1;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawRoundRect(line2X - 1, line2Y, width - line2X + 1, buttonHeight, UIConstants.ARC, UIConstants.ARC);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

    }

    protected boolean isTwoLine() {
        return labelButtonList.size() >= BUTTON_NUMBER && !isOneLineTab;
    }

    public void setOneLineTab(boolean isOneLineTab) {
        this.isOneLineTab = isOneLineTab;
    }

    protected boolean isDrawLine() {
        return true;
    }

    @Override
    protected Border getGroupBorder() {
        if (!isDrawLine()) {
            return BorderFactory.createEmptyBorder(1, 1, 1, 1);
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