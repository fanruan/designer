package com.fr.design.gui.icontainer;

import com.fr.design.constants.UIConstants;
import com.fr.design.gui.iscrollbar.UIScrollBar;

import javax.swing.*;
import java.awt.*;

/**
 * @author zhou
 * @since 2012-5-9下午4:39:33
 */
public class UIScrollPane extends JScrollPane {

    private static final long serialVersionUID = 1L;
    private static final int INCREAMENT = 30;

    public UIScrollPane(Component c) {
        super(c, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.setHorizontalScrollBar(createHorizontalScrollBar());
        this.getVerticalScrollBar().setUnitIncrement(INCREAMENT);
        this.getVerticalScrollBar().setBlockIncrement(INCREAMENT);
        this.getHorizontalScrollBar().setOpaque(true);
        this.getHorizontalScrollBar().setBackground(Color.WHITE);
        this.getVerticalScrollBar().setOpaque(true);
        this.getVerticalScrollBar().setBackground(Color.WHITE);
    }

    @Override
    /**
     * 生成水平滚动条
     */
    public UIScrollBar createHorizontalScrollBar() {
        UIScrollBar sbr = new UIScrollBar(JScrollBar.HORIZONTAL);
        sbr.setBackground(UIConstants.NORMAL_BACKGROUND);
        return sbr;
    }

    @Override
    /**
     * 生成垂直滚动条
     */
    public UIScrollBar createVerticalScrollBar() {
        UIScrollBar sbr = new UIScrollBar(JScrollBar.VERTICAL);
        sbr.setBackground(UIConstants.NORMAL_BACKGROUND);
        return sbr;
    }

}