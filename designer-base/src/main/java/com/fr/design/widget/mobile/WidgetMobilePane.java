package com.fr.design.widget.mobile;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.Widget;


import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * 单元格控件的"移动端"面板。默认显示"无可用配置项"，在子类中扩展
 * Created by plough on 2018/4/25.
 */
public class WidgetMobilePane extends JPanel {
    public static WidgetMobilePane DEFAULT_PANE = new WidgetMobilePane();

    public WidgetMobilePane() {
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        init();
    }

    protected void init() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        UILabel label = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_No_Settings_Available"));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label);
    }

    /**
     * 从 widget 中提取数据展示在属性面板中
     *
     * @param widget
     */
    public void populate(Widget widget) {
        // do nothing
    }

    /**
     * 从属性面板把数据保存到 widget 中
     * @param widget
     */
    public void update(Widget widget) {
        // do nothing
    }
}
