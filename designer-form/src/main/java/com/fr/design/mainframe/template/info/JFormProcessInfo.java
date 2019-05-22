package com.fr.design.mainframe.template.info;

import com.fr.form.main.Form;
import com.fr.form.ui.container.WLayout;

/**
 * Created by plough on 2017/3/17.
 */
public class JFormProcessInfo extends TemplateProcessInfo<Form> {
    public JFormProcessInfo(Form form) {
        super(form);
    }

    // 获取模板类型
    public int getReportType() {
        return 2;
    }

    // 获取模板格子数
    public int getCellCount() {
        return 0;
    }
    // 获取模板悬浮元素个数
    public int getFloatCount() {
        return 0;
    }
    // 获取模板聚合块个数
    public int getBlockCount() {
        return 0;
    }
    // 获取模板控件数
    public int getWidgetCount() {
        int widgetCount = 0;
        for (int i = 0; i < template.getContainer().getWidgetCount(); i++) {
            WLayout wl = (WLayout) template.getContainer().getWidget(i);
            widgetCount += wl.getWidgetCount();
        }
        return widgetCount;
    }
}
