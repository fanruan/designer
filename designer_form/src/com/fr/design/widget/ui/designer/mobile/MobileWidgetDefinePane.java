package com.fr.design.widget.ui.designer.mobile;

import com.fr.design.dialog.BasicPane;

/**
 * 所有移动端需要拓展的属性面板均继承此类
 *
 * Created by fanglei on 2017/8/8.
 */
public abstract class MobileWidgetDefinePane extends BasicPane{
    //初始化panel数据再repaint
    public abstract void initPropertyGroups(Object source);
}
