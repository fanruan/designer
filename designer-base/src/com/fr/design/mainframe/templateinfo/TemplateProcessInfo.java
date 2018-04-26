package com.fr.design.mainframe.templateinfo;

import com.fr.base.io.BaseBook;

/**
 * Created by plough on 2017/3/17.
 */
public abstract class TemplateProcessInfo<T extends BaseBook> {

    protected T template;

    public TemplateProcessInfo(T template) {
        this.template = template;
    }

    // 获取模板类型。0 代表普通报表，1 代表聚合报表，2 代表表单
    public abstract int getReportType();
    // 获取模板格子数
    public abstract int getCellCount();
    // 获取模板悬浮元素个数
    public abstract int getFloatCount();
    // 获取模板聚合块个数
    public abstract int getBlockCount();
    // 获取模板控件数
    public abstract int getWidgetCount();
}
