package com.fr.design.utils.gui;

import com.fr.design.gui.core.UITextComponent;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralContext;
import com.fr.stable.StringUtils;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Locale;

/**
 * 包含 UI 组件相关的工具方法
 * Created by plough on 2019/1/11.
 */

// Noninstantiable utility class
public class UIComponentUtils {
    private static final String HTML_TAG_TPL = "<html><body style='width: %dpx'>";
    private static final String HTML_BODY_TAG = "<html><body>";
    private static final String HTML_TAG = "<html>";
    private static final int MIN_WIDTH = 10;

    // 覆盖缺省构造器，不可实例化
    private UIComponentUtils() {
        throw new AssertionError();
    }

    /**
     * 到达指定宽度后换行
     */
    public static void setLineWrap(UITextComponent comp, int width) {
        if (width < MIN_WIDTH) {
            width = MIN_WIDTH;
        }
        insertPrefixToText(comp, String.format(HTML_TAG_TPL, width));
    }

    /**
     * 自动换行
     */
    public static void setLineWrap(UITextComponent comp) {
        insertPrefixToText(comp, HTML_BODY_TAG);
    }

    private static void insertPrefixToText(UITextComponent comp, String prefix) {
        if (comp == null || ComparatorUtils.equals(Locale.CHINA, GeneralContext.getLocale())) {
            // 最初是为了解决日文国际化显示不全，而增加的换行功能。中文不需要换行。
            // windows 下，字体为宋体时，对于 JLabel、JCheckBox、JButton 等控件，使用<html>换行后，文字会下移，可能导致文字下半部分被截断。
            // 因此中文直接返回，不加换行逻辑。
            return;
        }

        String text = comp.getText();
        if (StringUtils.isEmpty(text) || text.startsWith(HTML_TAG)) {
            return;
        }
        comp.setText(prefix + text);
    }

    /**
     * 将一个组件包装到 BorderLayout 布局的面板中
     * @param comp，待包装的组件
     * @param layoutConstraint，添加的方向，如 BorderLayout.NORTH
     * @return 包装好的 JPanel
     */
    public static JPanel wrapWithBorderLayoutPane(Component comp, String layoutConstraint) {
        JPanel panel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panel.add(comp, layoutConstraint);
        return panel;
    }

    /**
     * 将一个组件包装到 BorderLayout 布局的面板中
     * @param comp，待包装的组件
     * @return 包装好的 JPanel（布局方向为 BorderLayout.NORTH）
     */
    public static JPanel wrapWithBorderLayoutPane(Component comp) {
        return wrapWithBorderLayoutPane(comp, BorderLayout.NORTH);
    }

    public static void setPreferedWidth(JComponent comp, int width) {
        Dimension dim = comp.getPreferredSize();
        dim.setSize(width, dim.getHeight());
        comp.setPreferredSize(dim);
    }
}
