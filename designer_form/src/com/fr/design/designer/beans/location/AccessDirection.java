/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.beans.location;

import com.fr.design.beans.location.Absorptionline;
import com.fr.design.beans.location.MoveUtils;
import com.fr.design.designer.creator.*;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormSelection;
import com.fr.design.utils.ComponentUtils;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget;
import com.fr.form.ui.container.WParameterLayout;

import java.awt.*;

/**
 * @author richer
 * @since 6.5.3
 */
public abstract class AccessDirection implements Direction {
    private static final int MINHEIGHT = 21;
    private static final int MINWIDTH = 36;
    private int ymin;
    private int xmin;

	abstract int getCursor();

	protected abstract Rectangle getDraggedBounds(int dx, int dy, Rectangle current_bounds, FormDesigner designer,
			Rectangle oldbounds);

	protected int[] sorption(int x, int y,Rectangle current_bounds, FormDesigner designer) {
		// 自适应布局不需要吸附线，但需要对齐线，对齐线后面处理
		if (!designer.hasWAbsoluteLayout()) {
			designer.getStateModel().setEquidistantLine(null);
			designer.getStateModel().setXAbsorptionline(null);
			designer.getStateModel().setYAbsorptionline(null);
			return new int[] { x, y };
		} else {
			int posy = current_bounds.y;

			Point relativePoint = getRelativePoint(x, y, current_bounds,designer);
			sorptionPoint(relativePoint,current_bounds, designer);
			return new int[] { relativePoint.x, relativePoint.y };
		}

	}

	protected Point getRelativePoint(int x, int y, Rectangle current_bounds,FormDesigner designer) {
		if (x < 0) {
			x = 0;
		} else if (x > designer.getRootComponent().getWidth() && designer.getSelectionModel().hasSelectionComponent()) {
			x = designer.getRootComponent().getWidth();
		}
        //参数面板可以无下限拉长
		if (y < 0) {
			y = 0;
		} else if (y > designer.getRootComponent().getHeight() + designer.getParaHeight() && designer.getSelectionModel().hasSelectionComponent()
                && !designer.getSelectionModel().getSelection().getSelectedCreator().acceptType(XWParameterLayout.class)) {
			y = designer.getRootComponent().getHeight() + designer.getParaHeight();
		}
		return new Point(x, y);
	}

	protected void sorptionPoint(Point point, Rectangle current_bounds,FormDesigner designer) {
		boolean findInX = current_bounds.getWidth() <= MoveUtils.SORPTION_UNIT ? true : false;
		boolean findInY = current_bounds.getHeight() <= MoveUtils.SORPTION_UNIT ? true : false;
		WAbsoluteLayout layout =getLayout(designer);
		FormSelection selection = designer.getSelectionModel().getSelection();
		boolean isWidgetsIntersect = false;
		for (int i = 0, count = layout.getWidgetCount(); i < count; i++) {
			BoundsWidget temp = (BoundsWidget) layout.getWidget(i);
			if (!temp.isVisible() || selection.contains(temp.getWidget())) {
				continue;
			}
			Rectangle bounds = getWidgetRelativeBounds(temp.getBounds(), selection);
			if (!findInX) {
				int x1 = bounds.x;
				if (Math.abs(x1 - point.x) <= MoveUtils.SORPTION_UNIT) {
					point.x = x1;
					findInX = true;
				}
				int x2 = bounds.x + bounds.width;
				if (Math.abs(x2 - point.x) <= MoveUtils.SORPTION_UNIT) {
					point.x = x2;
					findInX = true;
				}
			}
			if (!findInY) {
				int y1 = bounds.y;
				if (Math.abs(y1 - point.y) <= MoveUtils.SORPTION_UNIT) {
					point.y = y1;
					findInY = true;
				}
				int y2 = bounds.y + bounds.height;
				if (Math.abs(y2 - point.y) <= MoveUtils.SORPTION_UNIT) {
					point.y = y2;
					findInY = true;
				}

			}
			if (findInX && findInY) {
				break;
			}

			if (current_bounds.intersects(bounds) && !(layout instanceof WParameterLayout)){
				isWidgetsIntersect = true;
			}
		}
		processRectangleIntersects(designer, point.x, point.y, isWidgetsIntersect);
		setDesignerStateModelProperties(designer, findInX, findInY, current_bounds, point);
	}

	private void setDesignerStateModelProperties (FormDesigner designer, boolean findInX, boolean findInY, Rectangle current_bounds, Point point) {
		designer.getStateModel().setXAbsorptionline(findInX && current_bounds.getWidth() > MoveUtils.SORPTION_UNIT ? Absorptionline.createXAbsorptionline(point.x) : null);
		designer.getStateModel().setYAbsorptionline(findInY && current_bounds.getHeight() > MoveUtils.SORPTION_UNIT ? Absorptionline.createYAbsorptionline(point.y) : null);
		designer.getStateModel().setEquidistantLine(null);
	}

	private Rectangle getWidgetRelativeBounds(Rectangle bounds, FormSelection selection){
		Rectangle relativeRec = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
		XLayoutContainer parent = XCreatorUtils.getParentXLayoutContainer(selection.getSelectedCreator());
		if (parent == null) {
			return relativeRec;
		}
		Rectangle rec = ComponentUtils.getRelativeBounds(parent);
		relativeRec.x += rec.x;
		relativeRec.y += rec.y;
		return relativeRec;
	}

	private void processRectangleIntersects(FormDesigner designer, int x, int y, boolean isIntersects){
		if(isIntersects){
			if(designer.getLocationOnScreen() != null) {
				MoveUtils.displayForbidWindow(x + designer.getLocationOnScreen().x, y + designer.getLocationOnScreen().y);
			}
			designer.setWidgetsIntersect(true);
		}
		else{
			MoveUtils.hideForbidWindow();
			designer.setWidgetsIntersect(false);
		}
	}

    private WAbsoluteLayout getLayout(final FormDesigner designer){
        XLayoutContainer formLayoutContainer = (XLayoutContainer) XCreatorUtils.createXCreator(
                designer.getTarget().getContainer());
        WAbsoluteLayout layout;
        if (formLayoutContainer.acceptType(XWBorderLayout.class)){//看起来这边的作用应该是为了区别cpt(得到XWParameterLayout)还是frm(得到XWBorderLayout)的参数界面
			Container container = designer.getSelectionModel().getSelection().getSelectedCreator().getParent();
			if(container instanceof XWAbsoluteLayout){
				layout = ((XWAbsoluteLayout)container).toData();
			}
			else {
				layout = (WAbsoluteLayout) designer.getParaComponent().toData();
			}
        } else{
            layout = (WAbsoluteLayout) designer.getTarget().getContainer();
        }
        return  layout;
    }

	/**
	 * 拖拽
	 * @param dx 坐标x
	 * @param dy 坐标y
	 * @param designer 设计界面
	 */
	public void drag(int dx, int dy, FormDesigner designer) {
		Rectangle rec = getDraggedBounds(dx, dy, designer.getSelectionModel().getSelection().getRelativeBounds(), designer, designer.getSelectionModel().getSelection().getBackupBounds());
        //设定控件最小高度21，因每次拖曳至少移动1，防止控件高度等于21时，拖曳导致rec.y的变化使得控件不停的向上或向下移动。
        if(rec.height == MINHEIGHT){
            ymin = rec.y;
        }
        if(rec.height == MINHEIGHT - 1){
            ymin = ymin == rec.y ? rec.y : rec.y - 1;
        }
        if(rec.height < MINHEIGHT){
            rec.height = MINHEIGHT;
            rec.y = ymin;
        }
        // 增加下宽度也设最小为21
        if (rec.width == MINWIDTH) {
        	xmin = rec.x;
        }
        if(rec.width == MINWIDTH - 1){
        	xmin = xmin == rec.x ? rec.x : rec.x - 1;
        }
        if (rec.width < MINWIDTH) {
        	rec.width = MINWIDTH;
        	rec.x = xmin;
        }
		if(rec != null) {
			designer.getSelectionModel().getSelection().setSelectionBounds(rec, designer);
		}
	}

	/**
	 * 更新鼠标指针形状
	 * @param formEditor 设计界面组件
	 */
	public void updateCursor(FormDesigner formEditor) {

		// 调用位置枚举的多态方法getCursor获取鼠标形状
		int type = getCursor();

		if (type != formEditor.getCursor().getType()) {
			// 设置当前形状
			formEditor.setCursor(Cursor.getPredefinedCursor(type));
		}
	}
	
	/**
	 * 生成组件备用的bound
	 * @param formEditor 设计界面组件
	 */
	public void backupBounds(FormDesigner formEditor) {
		formEditor.getSelectionModel().getSelection().backupBounds();
	}
}