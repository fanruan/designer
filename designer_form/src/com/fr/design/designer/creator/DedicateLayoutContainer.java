/**
 *
 */
package com.fr.design.designer.creator;

import com.fr.form.ui.container.WLayout;

import java.awt.Component;
import java.awt.Dimension;
import java.beans.IntrospectionException;
import java.util.List;

/**
 * 一些控件专属的容器，如标题容器，sclae容器
 *
 * @author jim
 * @date 2014-11-7
 */
public abstract class DedicateLayoutContainer extends XLayoutContainer {

    public DedicateLayoutContainer(WLayout widget, Dimension initSize) {
        super(widget, initSize);
    }

    /**
     * 得到属性名
     *
     * @return 属性名
     * @throws IntrospectionException
     */
    @Override
    public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
        return new CRPropertyDescriptor[0];
    }

    /**
     * 返回容器图标
     *
     * @return
     */
    @Override
    public String getIconPath() {
        if (this.getXCreator(XWScaleLayout.INDEX) != null) {
            return this.getXCreator(XWScaleLayout.INDEX).getIconPath();
        }
        return "/com/fr/web/images/form/resources/text_field_16.png";
    }


    /**
     * 控件树不显示此组件
     *
     * @param path 控件树list
     */
    @Override
    public void notShowInComponentTree(List<Component> path) {
        path.remove(path.size() - 1);
    }

    /**
     * 重置组件的名称
     *
     * @param name 名称
     */
    @Override
    public void resetCreatorName(String name) {
        super.resetCreatorName(name);
        XCreator child = getXCreator(XWScaleLayout.INDEX);
        //实现WTitleLayout的SetWidgetName
        child.toData().setWidgetName(name);
    }

    /**
     * 重置组件的可见性
     * @param visible 可见性
     */
    @Override
    public void resetVisible(boolean visible){
        super.resetVisible(visible);
        XCreator child = getXCreator(XWScaleLayout.INDEX);
        child.toData().setVisible(visible);
    }
    /**
     * 返回对应属性表的组件，scale和title返回其子组件
     *
     * @return 组件
     */
    @Override
    public XCreator getPropertyDescriptorCreator() {
        return getXCreator(XWScaleLayout.INDEX);
    }

    /**
     * 是否作为控件树的叶子节点
     *
     * @return 是则返回true
     */
    @Override
    public boolean isComponentTreeLeaf() {
        return true;
    }

    /**
     * 是否为sclae和title专属容器
     *
     * @return 是则返回true
     */
    @Override
    public boolean isDedicateContainer() {
        return true;
    }

}