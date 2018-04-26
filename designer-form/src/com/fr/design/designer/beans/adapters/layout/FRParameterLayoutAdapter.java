package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.painters.FRParameterLayoutPainter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.form.parameter.RootDesignGroupModel;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.container.WParameterLayout;

import java.awt.*;

/**
 * 表单参数界面的监听器
 */
public class FRParameterLayoutAdapter extends FRAbsoluteLayoutAdapter {

    private HoverPainter painter;

	public FRParameterLayoutAdapter(XLayoutContainer container) {
		super(container);
        painter = new FRParameterLayoutPainter(container);
	}

    public HoverPainter getPainter() {
        return painter;
    }

    public GroupModel getLayoutProperties() {
        return new RootDesignGroupModel((XWParameterLayout)container);
    }

    /**
     * 待说明
     * @param creator    组件
     */
	public void fix(XCreator creator) {
		super.fix(creator);
		
		WParameterLayout wabs = (WParameterLayout)container.toData();
		wabs.refreshTagList();
    }

    /**
     * 是否能在指定位置添加组件
     *
     * @param creator 组件
     * @param x       坐标x
     * @param y       坐标y
     * @return 能则返回true
     */
    @Override
    public boolean accept(XCreator creator, int x, int y) {
        return x >= 0 && y >= 0 && creator.getHeight() <= container.getHeight()
                && creator.getWidth() <= container.getWidth();
    }

    @Override
    protected void addComp(XCreator creator, int x, int y) {
        if (XCreatorUtils.getParentXLayoutContainer(creator) != null) {
            Rectangle r = ComponentUtils.getRelativeBounds(container);
            Rectangle creatorRectangle = ComponentUtils.getRelativeBounds(creator);
            x = creatorRectangle.x - r.x;
            y = creatorRectangle.y - r.y;
        } else {
            int w = creator.getWidth() / 2;
            int h = creator.getHeight() / 2;
            x = x - w;
            y = y - h;
        }

        fix(creator, x, y);
        container.add(creator);
        LayoutUtils.layoutRootContainer(container);
    }

    /**
     * 调整组件大小到合适尺寸位置
     *
     * @param creator 组件
     * @param x       坐标x
     * @param y       坐标y
     */
    @Override
    public void fix(XCreator creator, int x, int y) {
        if (x < 0) {
            x = 0;
        } else if (x + creator.getWidth() > container.getWidth()) {
            x = container.getWidth() - creator.getWidth();
        }

        if (y < 0) {
            y = 0;
        } else if (y + creator.getHeight() > container.getHeight()) {
            y = container.getHeight() - creator.getHeight();
        }

        creator.setLocation(x, y);
    }

    @Override
    public ConstraintsGroupModel getLayoutConstraints(XCreator creator) {
        return super.getLayoutConstraints(creator);
    }
}