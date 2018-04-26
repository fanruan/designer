package com.fr.design.designer.beans.adapters.layout;

import java.awt.CardLayout;
import java.awt.LayoutManager;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.design.designer.properties.CardLayoutConstraints;
import com.fr.design.designer.properties.CardLayoutPropertiesGroupModel;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;

public class FRCardLayoutAdapter extends AbstractLayoutAdapter {

    public FRCardLayoutAdapter(XLayoutContainer container) {
        super(container);
    }

    /**
	 * 当前容器是否接受组件creator
	 * 
	 * @param creator 拖入的组件
	 * @param x 坐标x
	 * @param y 坐标y
	 * 
	 * @return 是否接受
	 * 
	 *
	 * @date 2014-12-30-下午5:13:28
	 * 
	 */
    public boolean accept(XCreator creator, int x, int y) {
        return true;
    }

    /**
	 * 将指定组件添加到当前布局
	 * 
	 * @param creator 待添加组件
	 * @param x x坐标
	 * @param y y坐标
	 * 
	 *
	 * @date 2014-12-30-下午5:17:46
	 * 
	 */
    public void addComp(XCreator creator, int x, int y) {
        container.add(creator, creator.toData().getWidgetName());
        LayoutUtils.layoutRootContainer(container);
    }

    /**
	 * 将指定组件添加到当前布局
	 * 
	 * @param dragged 待添加组件
	 * 
	 *
	 * @date 2014-12-30-下午5:17:46
	 * 
	 */
    public void addNextComponent(XCreator dragged) {
    	addComp(dragged, -1, -1);
    }

    /**
	 * 将指定组件添加到目标组件前面
	 * 
	 * @param target 目标组件
	 * @param added 待添加组件
	 * 
	 *
	 * @date 2014-12-30-下午5:17:46
	 * 
	 */
    public void addBefore(XCreator target, XCreator added) {
        int index = ComponentUtils.indexOfComponent(container, target);

        if (index == -1) {
            container.add(added, added.toData().getWidgetName(), 0);
        } else {
            container.add(added, added.toData().getWidgetName(), index);
        }

        LayoutUtils.layoutRootContainer(container);
    }

    /**
	 * 将指定组件添加到目标组件后面
	 * 
	 * @param target 目标组件
	 * @param added 待添加组件
	 * 
	 *
	 * @date 2014-12-30-下午5:17:46
	 * 
	 */
    public void addAfter(XCreator target, XCreator added) {
        int index = ComponentUtils.indexOfComponent(container, target);

        if (index == -1) {
            container.add(added, added.toData().getWidgetName());
        } else {
            index++;

            if (index >= container.getComponentCount()) {
                container.add(added, added.toData().getWidgetName());
            } else {
                container.add(added, added.toData().getWidgetName(), index);
            }
        }

        LayoutUtils.layoutRootContainer(container);
    }
    
    /**
	 * 展示组件
	 * 
	 * @param child 需要展示的组件
	 * 
	 *
	 * @date 2014-12-30-下午5:17:13
	 * 
	 */
    public void showComponent(XCreator child) {
        LayoutManager layout = container.getLayout();
        CardLayout cardLayout = (CardLayout) layout;
        cardLayout.show(container, child.toData().getWidgetName());
    }

    @Override
    public ConstraintsGroupModel getLayoutConstraints(XCreator creator) {
		return new CardLayoutConstraints((XWCardLayout) container, creator);
    }
    @Override
    public GroupModel getLayoutProperties() {
        return new CardLayoutPropertiesGroupModel((XWCardLayout) container);
    }
}