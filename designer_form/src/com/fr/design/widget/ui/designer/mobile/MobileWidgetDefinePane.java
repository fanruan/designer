package com.fr.design.widget.ui.designer.mobile;

import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.mainframe.FormDesigner;

/**
 * 所有移动端需要拓展的属性面板均继承此类
 *
 * Created by fanglei on 2017/8/8.
 */
public abstract class MobileWidgetDefinePane extends AbstractAttrNoScrollPane{
    //初始化panel数据再repaint
    public abstract void initPropertyGroups(Object source);

    /**
     * 从xCreator中提取数据展示在属性面板中
     *
     * @param designer
     */
    public abstract void populate(FormDesigner designer);

    /**
     * 从属性面板把数据传到后台
     */
    public abstract void update();
}
