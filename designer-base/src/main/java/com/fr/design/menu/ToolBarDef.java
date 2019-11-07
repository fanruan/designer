package com.fr.design.menu;

import com.fr.design.gui.itoolbar.UIToolBarUI;
import com.fr.design.gui.itoolbar.UIToolbar;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Define toolbar..
 */
public class ToolBarDef {

    // item List.
    private List<ShortCut> shortcutList = new ArrayList<ShortCut>();

    /**
     * 一个static的方法生成一个JToolBar
     */
    public static UIToolbar createJToolBar(final Color background) {
        UIToolbar toolbar = new UIToolbar(FlowLayout.LEFT, new UIToolBarUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(background);
                g2.fillRect(0, 0, c.getWidth(), c.getHeight());
            }
        });
        toolbar.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        return toolbar;
    }


    /**
     * 一个static的方法生成一个JToolBar
     */
    public static UIToolbar createJToolBar() {
        UIToolbar toolbar = new UIToolbar(FlowLayout.LEFT);
        toolbar.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        return toolbar;
    }

    public ToolBarDef() {
    }

    public int getShortCutCount() {
        return this.shortcutList.size();
    }

    @Nullable
    public ShortCut getShortCut(int index) {
        return this.shortcutList.get(index);
    }

    /**
     * 用可变参数，方便添加数组
     */
    public void addShortCut(ShortCut... shortcut) {
        Collections.addAll(this.shortcutList, shortcut);
    }

    public void clearShortCuts() {
        this.shortcutList.clear();
    }

    /**
     * 根据当前的ToolBarDef,更新toolBar
     */
    public void updateToolBar(UIToolbar toolBar) {
        toolBar.removeAll();

        int actionCount = this.getShortCutCount();
        for (int i = 0; i < actionCount; i++) {
            ShortCut shortcut = this.getShortCut(i);
            if (shortcut != null) {
                shortcut.intoJToolBar(toolBar);
            }
        }
    }

}