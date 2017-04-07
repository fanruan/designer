package com.fr.design.designer.beans.models;

import java.awt.Rectangle;

import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.adapters.component.CompositeComponentAdapter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.utils.ComponentUtils;
import com.fr.general.ComparatorUtils;

/**
 * 添加状态下的model
 */
public class AddingModel {

    // 当前要添加的组件
    private XCreator creator;
    // 记录当前鼠标的位置信息
    private int currentX;
    private int currentY;
    private boolean added;

    public AddingModel(FormDesigner designer, XCreator xCreator) {
        String creatorName = getXCreatorName(designer, xCreator);
        this.creator = xCreator;
        instantiateCreator(designer, creatorName);
        // 初始的时候隐藏该组件的图标
        currentY = -this.creator.getWidth();
        currentX = -this.creator.getHeight();
    }

    /**
     * 待说明
     *
     * @param designer    设计器
     * @param creatorName 组件名
     */
    public void instantiateCreator(FormDesigner designer, String creatorName) {
        creator.toData().setWidgetName(creatorName);
        ComponentAdapter adapter = new CompositeComponentAdapter(designer, creator);
        adapter.initialize();
        creator.addNotify();
        creator.putClientProperty(AdapterBus.CLIENT_PROPERTIES, adapter);
    }

    public AddingModel(XCreator xCreator, int x, int y) {
        this.creator = xCreator;
        this.creator.backupCurrentSize();
        this.creator.backupParent();
        this.creator.setSize(xCreator.initEditorSize());
        currentX = x - (xCreator.getWidth() / 2);
        currentY = y - (xCreator.getHeight() / 2);
    }

    /**
     * 隐藏当前组件的图标
     */
    public void reset() {
        currentX = -this.creator.getWidth();
        currentY = -this.creator.getHeight();
    }

    public String getXCreatorName(FormDesigner designer, XCreator x) {
        String def = x.createDefaultName();
        if (x.acceptType(XWParameterLayout.class)) {
            return def;
        }
        int i = 0;
        while (designer.getTarget().isNameExist(def + i)) {
            i++;
        }
        return def + i;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }


    /**
     * 移动组件图标到鼠标事件发生的位置
     *
     * @param x 坐标
     * @param y 坐标
     */
    public void moveTo(int x, int y) {
        currentX = x - (this.creator.getWidth() / 2);
        currentY = y - (this.creator.getHeight() / 2);
    }

    public XCreator getXCreator() {
        return this.creator;
    }

    /**
     * 当前组件是否已经添加到某个容器中
     *
     * @return 是返回true
     */
    public boolean isCreatorAdded() {
        return added;
    }

    /**
     * 加入容器
     *
     * @param designer  设计器
     * @param container 容器
     * @param x         坐标
     * @param y         坐标
     * @return 成功返回true
     */
    public boolean add2Container(FormDesigner designer, XLayoutContainer container, int x, int y) {
        //考虑不同布局嵌套的情况，获取顶层容器
        XLayoutContainer xLayoutContainer = container.getTopLayout();
        if (xLayoutContainer != null && xLayoutContainer.acceptType(XWAbsoluteLayout.class)) {
            container = xLayoutContainer;
        }

        Rectangle rect = ComponentUtils.getRelativeBounds(container);
        if (!ComparatorUtils.equals(container.getOuterLayout(), container.getBackupParent())) {
            added = container.getLayoutAdapter().addBean(creator,
                    x + designer.getArea().getHorizontalValue(),
                    y + designer.getArea().getVerticalValue());
            return added;
        }
        added = container.getLayoutAdapter().addBean(creator,
                x + designer.getArea().getHorizontalValue() - rect.x,
                y + designer.getArea().getVerticalValue() - rect.y);
        return added;
    }
}