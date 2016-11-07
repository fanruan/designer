package com.fr.design.designer.creator;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRAbsoluteBodyLayoutAdapter;
import com.fr.form.ui.container.WAbsoluteBodyLayout;

import java.awt.*;

/**
 * Created by zhouping on 2016/10/14.
 * 用作body的绝对布局
 */
public class XWAbsoluteBodyLayout extends XWAbsoluteLayout {
    public XWAbsoluteBodyLayout(WAbsoluteBodyLayout widget, Dimension initSize) {
        super(widget, initSize);
        this.editable = true;
    }

    /**
     * 返回对应的widget容器
     *
     * @return 返回WAbsoluteLayout
     */
    @Override
    public WAbsoluteBodyLayout toData() {
        return (WAbsoluteBodyLayout)data;
    }

    @Override
    public LayoutAdapter getLayoutAdapter() {
        return new FRAbsoluteBodyLayoutAdapter(this);
    }



    /**
     * 假如是body的话，始终要能编辑，不会出现蒙层
     *
     * @param isEditable 可否编辑
     */
    @Override
    public void setEditable(boolean isEditable) {
        super.setEditable(true);
    }

    /**
     * 该组件是否可以拖拽(表单中绝对布局不可以拖拽)
     *
     * @return 是则返回true
     */
    @Override
    public boolean isSupportDrag() {
        return false;
    }
}
