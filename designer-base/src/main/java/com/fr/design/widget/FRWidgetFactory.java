package com.fr.design.widget;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.utils.gui.UIComponentUtils;

/**
 * 创建 widget 的静态工厂
 * Created by plough on 2019/1/15.
 */
public class FRWidgetFactory {
    // 不可实例化
    private FRWidgetFactory() {
        throw new AssertionError();
    }

    /**
     * 创建一个可换行的 UILabel
     * @param text 标签文字
     * @return com.fr.design.gui.ilable.UILabel
     */
    public static UILabel createLineWrapLabel(String text) {
        UILabel label = new UILabel(text);
        UIComponentUtils.setLineWrap(label);
        return label;
    }

    /**
     * 创建一个可换行的 UILabel
     * @param text 标签文字
     * @param lineWidth 最大行宽
     * @return com.fr.design.gui.ilable.UILabel
     */
    public static UILabel createLineWrapLabel(String text, int lineWidth) {
        UILabel label = new UILabel(text);
        UIComponentUtils.setLineWrap(label, lineWidth);
        return label;
    }

    /**
     * 创建一个可换行可调整水平对齐的 UILabel
     * @param text
     * @param lineWidth
     * @param horizontalAlignment
     * @return com.fr.design.gui.ilable.UILabel
     */
    public static UILabel createLineWrapLabel(String text, int lineWidth, int horizontalAlignment) {
        UILabel label = createLineWrapLabel(text, lineWidth);
        label.setHorizontalAlignment(horizontalAlignment);
        return label;
    }
}
