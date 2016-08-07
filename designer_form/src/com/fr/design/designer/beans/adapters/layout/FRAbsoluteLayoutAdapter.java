package com.fr.design.designer.beans.adapters.layout;

import java.awt.*;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.painters.FRAbsoluteLayoutPainter;
import com.fr.design.designer.creator.*;
import com.fr.design.designer.properties.BoundsGroupModel;
import com.fr.design.designer.properties.FRAbsoluteLayoutPropertiesGroupModel;
import com.fr.design.designer.properties.FRFitLayoutPropertiesGroupModel;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;

public class FRAbsoluteLayoutAdapter extends AbstractLayoutAdapter {
	private HoverPainter painter;
    public FRAbsoluteLayoutAdapter(XLayoutContainer container) {
        super(container);
		painter = new FRAbsoluteLayoutPainter(container);
    }

	@Override
	public HoverPainter getPainter() {
		return painter;
	}
    
    /**
     * 是否能在指定位置添加组件
     * @param creator 组件
     * @param x 坐标x
     * @param y 坐标y
     * @return 能则返回true
     */
    @Override
	public boolean accept(XCreator creator, int x, int y) {
		Component comp = container.getComponentAt(x, y);
		//布局控件要先判断是不是可编辑
		XLayoutContainer topLayout = XCreatorUtils.getHotspotContainer((XCreator)comp).getTopLayout();
		if(topLayout != null && !topLayout.isEditable()){
			return false;
		}
		boolean isAccept =  x >= 0 && y >= 0 && creator.getHeight() <= container.getHeight()
				&& creator.getWidth() <= container.getWidth();
		return isAccept;
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

		if (creator.shouldScaleCreator() || creator.hasTitleStyle()) {
			addParentCreator(creator);
		} else {
			container.add(creator, creator.toData().getWidgetName());
		}
		XWAbsoluteLayout layout = (XWAbsoluteLayout) container;
		layout.updateBoundsWidget(creator);
		LayoutUtils.layoutRootContainer(container);
	}

	private void addParentCreator(XCreator child) {
		XLayoutContainer parentPanel = child.initCreatorWrapper(child.getHeight());
		container.add(parentPanel, child.toData().getWidgetName());
	}

    /**
     * 组件拖拽后调整大小
     * @param creator 组件
     */
    @Override
	public void fix(XCreator creator) {
    	WAbsoluteLayout wabs = (WAbsoluteLayout)container.toData();
    	fix(creator,creator.getX(),creator.getY());
    	wabs.setBounds(creator.toData(),creator.getBounds());

		XWAbsoluteLayout layout = (XWAbsoluteLayout) container;
		layout.updateBoundsWidget(creator);
    }
    
    /**
     * 调整组件大小到合适尺寸位置
     * @param creator 组件
     * @param x 坐标x
     * @param y 坐标y
     */
    public void fix(XCreator creator ,int x, int y) {
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
        return new BoundsGroupModel((XWAbsoluteLayout)container, creator);
    }

	@Override
	public GroupModel getLayoutProperties() {
		XWAbsoluteLayout xwAbsoluteLayout = (XWAbsoluteLayout) container;
		return new FRAbsoluteLayoutPropertiesGroupModel(xwAbsoluteLayout);
	}
}