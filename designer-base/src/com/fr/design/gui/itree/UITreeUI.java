package com.fr.design.gui.itree;

import com.fr.base.BaseUtils;
import com.fr.design.utils.ThemeUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalTreeUI;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 13-12-31
 * Time: 下午4:58
 */
public class UITreeUI extends MetalTreeUI {
    /**
     * 创建组件UI
     * @param x 组件
     * @return 返回组件UI
     */
    public static ComponentUI createUI(JComponent x) {
        return new UITreeUI();
    }

    protected void installDefaults() {
        super.installDefaults();
        setExpandedIcon(BaseUtils.readIcon("/com/fr/design/images/buttonicon/minus.png"));
        setCollapsedIcon(BaseUtils.readIcon("/com/fr/design/images/buttonicon/plus.png"));
        if (tree.getCellRenderer() instanceof DefaultTreeCellRenderer) {
            DefaultTreeCellRenderer r = (DefaultTreeCellRenderer) tree.getCellRenderer();
            r.setBackgroundNonSelectionColor(ThemeUtils.TEXT_BG_COLOR);
            r.setBackgroundSelectionColor(ThemeUtils.TEXT_SELECTED_BG_COLOR);
            r.setTextNonSelectionColor(ThemeUtils.NORMAL_FOREGROUND);
            r.setTextSelectionColor(ThemeUtils.TEXT_BG_COLOR);
        }
    }
}