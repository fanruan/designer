package com.fr.design.widget.mobile;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * 单元格控件的"移动端"面板。默认显示"无可用配置项"，在子类中扩展
 * Created by plough on 2018/4/25.
 */
public class WidgetMobilePane extends JPanel {
    public static WidgetMobilePane DEFAULT_PANE = new WidgetMobilePane();

    public WidgetMobilePane() {
        init();
    }

    public void init() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        UILabel label = new UILabel(Inter.getLocText("FR-Designer_No_Settings_Available"));
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
