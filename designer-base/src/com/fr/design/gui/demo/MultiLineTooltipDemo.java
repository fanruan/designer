package com.fr.design.gui.demo;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JToolTip;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.itooltip.MultiLineToolTip;
import com.fr.design.layout.FRGUIPaneFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Richer
 * Date: 11-6-30
 * Time: 下午9:14
 */
public class MultiLineTooltipDemo extends JPanel {
    public MultiLineTooltipDemo() {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        UIButton button = new UIButton("鼠标移动在此按钮上查看多行提示") {

            @Override
            public JToolTip createToolTip() {
                MultiLineToolTip tip = new MultiLineToolTip();
                tip.setComponent(this);
                tip.setOpaque(false);
                return tip;
            }
        };
        button.setToolTipText("Hello\nworld ");
        add(button, BorderLayout.NORTH);
    }
}