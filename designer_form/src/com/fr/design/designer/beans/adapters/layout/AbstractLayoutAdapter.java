package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.painters.NullPainter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.general.ComparatorUtils;

import java.awt.*;

public abstract class AbstractLayoutAdapter implements LayoutAdapter {

    protected XLayoutContainer container;
    protected LayoutManager layout;

    public AbstractLayoutAdapter(XLayoutContainer container) {
        this.container = container;
        this.layout = container.getLayout();
    }

    /**
     * 是否使用控件备份大小
     *
     * @param xCreator 控件
     * @return 所在容器相同，且支持备份的话返回true
     */
    public boolean whetherUseBackupSize(XCreator xCreator) {
        Class clazz = container.getClass();
        Class bkClazz = null;
        if (xCreator.getBackupParent() != null) {
            bkClazz = xCreator.getBackupParent().getClass();
        }
        return ComparatorUtils.equals(bkClazz, clazz)
                && supportBackupSize();
    }

    /**
     * 是否支持用备份大小
     *
     * @return 否
     */
    public boolean supportBackupSize() {
        return false;
    }

    /**
     * 有的控件在拖拽调整大小后需要根据自身内容重新计算下当前的尺寸是否合适，如果不合适，就需要重新fix一下
     *
     * @param creator 组件
     */
    public void fix(XCreator creator) {
    }

    /**
     * 显示parent的字组件child，解决CardLayout中显示某个非显示组件的特殊情况
     *
     * @param child 组件
     */
    @Override
    public void showComponent(XCreator child) {
        child.setVisible(true);
    }

    /**
     * 组件的ComponentAdapter在添加组件时，如果发现布局管理器不为空，会继而调用该布局管理器的
     * addComp方法来完成组件的具体添加。在该方法内，布局管理器可以提供额外的功能。
     *
     * @param creator 被添加的新组件
     * @param x       添加的位置x，该位置是相对于container的
     * @param y       添加的位置y，该位置是相对于container的
     * @return 是否添加成功，成功返回true，否则false
     */
    @Override
    public boolean addBean(XCreator creator, int x, int y) {
        if (!accept(creator, x, y)) {
            return false;
        }
        addComp(creator, x, y);
        ((XWidgetCreator) creator).recalculateChildrenSize();
        return true;
    }

    /**
     * 删除组件
     *
     * @param creator       组件
     * @param creatorWidth
     * @param creatorHeight
     */
    public void removeBean(XCreator creator, int creatorWidth, int creatorHeight) {
        delete(creator, creatorWidth, creatorHeight);
    }

    protected void delete(XCreator creator, int creatorWidth, int creatorHeight) {
    }

    protected abstract void addComp(XCreator creator, int x, int y);

    /**
     * 增加下一个组件
     *
     * @param dragged 组件
     */
    @Override
    public void addNextComponent(XCreator dragged) {
        container.add(dragged);
        LayoutUtils.layoutRootContainer(container);
    }

    /**
     * 目标控件位置插入组件
     *
     * @param target 目标
     * @param added  增加组件
     */
    @Override
    public void addBefore(XCreator target, XCreator added) {
        int index = ComponentUtils.indexOfComponent(container, target);

        if (index == -1) {
            container.add(added, 0);
        } else {
            container.add(added, index);
        }

        LayoutUtils.layoutRootContainer(container);
    }

    /**
     * 插在目标组件后面
     *
     * @param target 目标
     * @param added  增加组件
     */
    @Override
    public void addAfter(XCreator target, XCreator added) {
        int index = ComponentUtils.indexOfComponent(container, target);

        if (index == -1) {
            container.add(added);
        } else {
            index++;

            if (index >= container.getComponentCount()) {
                container.add(added);
            } else {
                container.add(added, index);
            }
        }

        LayoutUtils.layoutRootContainer(container);
    }

    @Override
    public HoverPainter getPainter() {
        return new NullPainter(container);
    }

    /**
     * 是否能接收更多的组件
     *
     * @return 能则返回true
     */
    @Override
    public boolean canAcceptMoreComponent() {
        return true;
    }

    @Override
    public ConstraintsGroupModel getLayoutConstraints(XCreator creator) {
        return null;
    }

    @Override
    public GroupModel getLayoutProperties() {
        return null;
    }


    public XLayoutContainer getContainer() {
        return this.container;
    }
}