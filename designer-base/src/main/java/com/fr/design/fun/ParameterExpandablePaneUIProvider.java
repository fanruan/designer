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
     * 如果面板不存在，则创建可展开面板并添加子容器；否则在某个tabPane下的UIExpandablePane实例中增加子容器
     *
     * @param propertyTab 可展开面板放在哪个propertyTab下，例如属性或者移动端
     * @param uiExpandablePaneName 可扩展面板名称
     * @return UIExpandablePane
     */
    UIExpandablePane createUIExpandablePane(PropertyTab propertyTab, String uiExpandablePaneName);

}
