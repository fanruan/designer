package com.fr.design.utils.gui;

import com.fr.design.gui.core.UITextComponent;
import com.fr.stable.StringUtils;

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
        if (comp == null) {
            return;
        }
        String text = comp.getText();

        if (StringUtils.isEmpty(comp.getText()) || text.startsWith(HTML_TAG)) {
            return;
        }
        comp.setText(prefix + comp.getText());
    }
}
