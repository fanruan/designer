package com.fr.design.fun;

import com.fr.design.designer.properties.PropertyTab;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.stable.fun.mark.Mutable;

/**
 * created by hades on 18/10/16
 * 该接口支持在设计器cpt&frm参数界面的属性和移动端下添加一个UIExpandablePane（可展开面板）
 */
public interface ParameterExpandablePaneUIProvider extends Mutable {

    String XML_TAG = "ParameterExpandablePaneUIProvider";

    int CURRENT_LEVEL = 1;

    /**
     * 创建可展开面板并添加子容器
     *
     * @return UIExpandablePane
     */
    UIExpandablePane createUIExpandablePane();

    /**
     * 指定添加UIExpandablePane到哪个PropertyTab下，例如属性，移动端
     *
     * @return PropertyTab
     */
    PropertyTab addToWhichPropertyTab();

}
