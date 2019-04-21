package com.fr.design.mainframe.template.info;

import com.fr.base.io.BaseBook;

/**
 * Created by plough on 2017/3/17.
 */
// todo: 重构
// 1. 命名不好，表意不清晰。
// 2. 逻辑混乱，到底是一个 Info 类，还是一个 InfoCollector 类？
// 3. 耦合太强，用组合替代继承
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
