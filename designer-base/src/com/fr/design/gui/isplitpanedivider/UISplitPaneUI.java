package com.fr.design.gui.isplitpanedivider;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.metal.MetalSplitPaneUI;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-12
 * Time: 下午4:59
 */
public class UISplitPaneUI extends MetalSplitPaneUI {

    /**
     * 创建UI
     * @param x 组件
     * @return 返回组件UI
     */
    public static ComponentUI createUI(JComponent x) {
   		return new UISplitPaneUI();
   	}

    /**
     * 创建Divider
     * @return 返回默认的divider
     */
   	public BasicSplitPaneDivider createDefaultDivider() {
   		return new UISplitPaneDivider(this);
   	}
}