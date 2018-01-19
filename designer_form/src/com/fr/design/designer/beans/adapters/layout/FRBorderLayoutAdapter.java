package com.fr.design.designer.beans.adapters.layout;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import com.fr.general.ComparatorUtils;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.painters.FRBorderLayoutPainter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWBorderLayout;
import com.fr.design.designer.properties.FRBorderLayoutConstraints;
import com.fr.design.form.layout.FRBorderLayout;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.design.utils.gui.LayoutUtils;

public class FRBorderLayoutAdapter extends AbstractLayoutAdapter {

    private HoverPainter painter;

    public FRBorderLayoutAdapter(XLayoutContainer container) {
        super(container);
        painter = new FRBorderLayoutPainter(container);
    }

    @Override
	public HoverPainter getPainter() {
		return painter;
	}

    /**
     * 有的控件在拖拽调整大小后需要根据自身内容重新计算下当前的尺寸是否合适，如果不合适，就需要重新fix一下
     * @param creator 组件
     */
    public void fix(XCreator creator) {
        FRBorderLayout layout = (FRBorderLayout)container.getFRLayout();
        Object constraints = layout.getConstraints(creator);
        if (ComparatorUtils.equals(constraints, BorderLayout.NORTH)) {
            ((XWBorderLayout)container).toData().setNorthSize(creator.getHeight());
        } else if (ComparatorUtils.equals(constraints, BorderLayout.SOUTH)) {
            ((XWBorderLayout)container).toData().setSouthSize(creator.getHeight());
        } else if (ComparatorUtils.equals(constraints, BorderLayout.EAST)) {
            ((XWBorderLayout)container).toData().setEastSize(creator.getWidth());
        } else if (ComparatorUtils.equals(constraints, BorderLayout.WEST)) {
            ((XWBorderLayout)container).toData().setWestSize(creator.getWidth());
        } else {
        	return;
        }
        container.recalculateChildrenPreferredSize();
    }

    /**
     * 增加组件
     * @param child    组件
     * @param x    横坐标
     * @param y    纵坐标
     */
    public void addComp(XCreator child, int x, int y) {
        String placement = getPlacement(child, x, y);     
        container.add(child, placement);
        LayoutUtils.layoutRootContainer(container);
    }

    /**
     * 在添加组件状态时，当鼠标移动到某个容器上方时，如果该容器有布局管理器，则会调用该布局
     * 管理适配器的accept来决定当前位置是否可以放置，并提供特殊的标识，比如红色区域标识。比
     * 如在BorderLayout中，如果某个方位已经放置了组件，则此时应该返回false标识该区域不可以
     * 放置。
     *@param creator 组件
     *@param x 添加的位置x，该位置是相对于container的
     *@param y 添加的位置y，该位置是相对于container的
     *@return 是否可以放置
     */
    public boolean accept(XCreator creator, int x, int y) {
        String placement = getPlacement(creator, x, y);
        FRBorderLayout blayout = (FRBorderLayout) container.getLayout();
        Component comp = blayout.getLayoutComponent(placement);
        return comp == null;
    }

    public Dimension getPreferredSize(XCreator creator) {
        int hw = container.getWidth();
        int hh = container.getHeight();

        Dimension prefSize = creator.getSize();

        if (prefSize.width > (hw / 3)) {
            prefSize.width = hw / 3;
        }

        if (prefSize.height > (hh / 3)) {
            prefSize.height = hh / 3;
        }

        return prefSize;
    }

    private String getPlacement(XCreator creator, int x, int y) {
        int width = container.getWidth();
        int height = container.getHeight();
        WBorderLayout wLayout = ((XWBorderLayout)container).toData();
        int northSize = wLayout.getNorthSize();
        int southSize = wLayout.getSouthSize();
        int eastSize = wLayout.getEastSize();
        int westSize = wLayout.getWestSize();    
        if (y < northSize) {
            return BorderLayout.NORTH;
        } else if ((y >= northSize) && (y < (height - southSize))) {
            if (x < westSize) {
                return BorderLayout.WEST;
            } else if ((x >= westSize) && (x < (width - eastSize))) {
                return BorderLayout.CENTER;
            } else {
                return BorderLayout.EAST;
            }
        } else {
            return BorderLayout.SOUTH;
        }
    }

    /**
     * 增加下一个组件
     * @param dragged 组件
     */
    public void addNextComponent(XCreator dragged) {
        FRBorderLayout layout = (FRBorderLayout) container.getLayout();
        Component north = layout.getLayoutComponent(BorderLayout.NORTH);
        Component south = layout.getLayoutComponent(BorderLayout.SOUTH);
        Component west = layout.getLayoutComponent(BorderLayout.WEST);
        Component east = layout.getLayoutComponent(BorderLayout.EAST);
        Component center = layout.getLayoutComponent(BorderLayout.CENTER);

        if (north == null) {
            container.add(dragged, BorderLayout.NORTH);
        } else if (south == null) {
            container.add(dragged, BorderLayout.SOUTH);
        } else if (west == null) {
            container.add(dragged, BorderLayout.WEST);
        } else if (east == null) {
            container.add(dragged, BorderLayout.EAST);
        } else if (center == null) {
            container.add(dragged, BorderLayout.CENTER);
        }

        LayoutUtils.layoutRootContainer(container);
    }

    /**
     * 目标控件位置插入组件
     * @param target 目标
     * @param added 增加组件
     */
    public void addBefore(XCreator target, XCreator added) {
        addNextComponent(added);
    }

    /**
     * 插在目标组件后面
     * @param target 目标
     * @param added 增加组件
     */
    public void addAfter(XCreator target, XCreator added) {
        addNextComponent(added);
    }

    /**
     * 是否能接收更多的组件
     * @return 能则返回true
     */
    public boolean canAcceptMoreComponent() {
        FRBorderLayout layout = (FRBorderLayout) container.getLayout();
        Component north = layout.getLayoutComponent(BorderLayout.NORTH);
        Component south = layout.getLayoutComponent(BorderLayout.SOUTH);
        Component west = layout.getLayoutComponent(BorderLayout.WEST);
        Component east = layout.getLayoutComponent(BorderLayout.EAST);
        Component center = layout.getLayoutComponent(BorderLayout.CENTER);

        return (north == null) || (south == null) || (west == null) || (east == null) || (center == null);
    }

    @Override
	public ConstraintsGroupModel getLayoutConstraints(XCreator creator) {
		return new FRBorderLayoutConstraints(container, creator);
	}

}