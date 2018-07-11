package com.fr.design.designer.beans.location;

import com.fr.design.beans.location.Absorptionline;
import com.fr.design.beans.location.MoveUtils;
import com.fr.design.beans.location.MoveUtils.RectangleDesigner;
import com.fr.design.beans.location.MoveUtils.RectangleIterator;
import com.fr.design.designer.creator.*;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormSelection;
import com.fr.design.utils.ComponentUtils;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.stable.ArrayUtils;
import com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget;

import java.awt.*;
import java.awt.Rectangle;

public class Inner extends AccessDirection {

	public Inner() {
	}

	@Override
	public int getCursor() {
		return Cursor.MOVE_CURSOR;
	}

	@Override
	public int getActual() {
		return Direction.INNER;
	}
	
	protected Point getRelativePoint(int x, int y, Rectangle current_bounds, FormDesigner designer) {
		if (x < 0) {
			x = 0;
		} else if (x + current_bounds.getWidth() > designer.getRootComponent().getWidth()
				&& designer.getSelectionModel().hasSelectionComponent()) {
			x = designer.getRootComponent().getWidth() - current_bounds.width;
		}
		if (y < 0) {
			y = 0;
		} else if (y + current_bounds.getHeight() > (designer.getRootComponent().getHeight() + designer.getParaHeight())
				&& designer.getSelectionModel().hasSelectionComponent()) {
			y = designer.getRootComponent().getHeight() + designer.getParaHeight() - current_bounds.height;
		}
		return new Point(x, y);
	}

	private class RectDesigner implements RectangleDesigner {
		private FormDesigner designer = null;

		public RectDesigner(FormDesigner designer) {
			this.designer = designer;
		}

		public void setXAbsorptionline(Absorptionline line) {
			designer.getStateModel().setXAbsorptionline(line);
		}
		public void setYAbsorptionline(Absorptionline line) {
			designer.getStateModel().setYAbsorptionline(line);
		}

		/**
		 * 获取当前选中块的水平线数组
		 *
		 * @return 块的水平线数组
		 *
		 */
		public int[] getHorizontalLine(){
			return ArrayUtils.EMPTY_INT_ARRAY;
		}

		/**
		 * 获取当前选中块的垂直线数组
		 *
		 * @return 块的垂直线数组
		 *
		 */
		public int[] getVerticalLine(){
			return ArrayUtils.EMPTY_INT_ARRAY;
		}
		public RectangleIterator createRectangleIterator() {
			return getRectangleIterator(designer);
		}

		/**
		 * 设置designer内部组件是否重叠的标志位
		 *
		 * @param isIntersects 是否重叠
		 */
		@Override
		public void setWidgetsIntersected(boolean isIntersects) {
			designer.setWidgetsIntersect(isIntersects);
		}

		/**
		 * 获取designer内部组件是否重叠的标志位
		 *
		 * @return 重叠
		 */
		@Override
		public boolean isWidgetsIntersected() {
			return designer.isWidgetsIntersect();
		}

		/**
		 * 获取designer相对屏幕的位置
		 *
		 * @return 位置
		 */
		@Override
		public Point getDesignerLocationOnScreen() {
			return designer.getLocationOnScreen();
		}

		/**
		 * 设置等距线
		 *
		 * @param line 吸附线
		 */
		@Override
		public void setEquidistantLine(Absorptionline line) {
			designer.getStateModel().setEquidistantLine(line);
		}

		@Override
		public int getDesignerScrollHorizontalValue() {
			return designer.getArea().getHorizontalValue();
		}

		@Override
		public int getDesignerScrollVerticalValue() {
			return designer.getArea().getVerticalValue();
		}
	}

	@Override
	protected void sorptionPoint(Point point, Rectangle current_bounds, final FormDesigner designer) {
		RectDesigner rd = new RectDesigner(designer);
		//判断当前操作的是不是参数面板，要特殊处理
		boolean isParameterLayout = ((XCreator)(designer.getSelectionModel().getSelection().getSelectedCreator().getParent())).acceptType(XWParameterLayout.class);
		point.setLocation(MoveUtils.sorption(point.x, point.y, current_bounds.width, current_bounds.height, rd, isParameterLayout));
	}
	
	private RectangleIterator getRectangleIterator(final FormDesigner designer){

		return new RectangleIterator() {
			private int i;
			private WAbsoluteLayout layout = getLayout(designer);
			private int count = layout.getWidgetCount();
			private FormSelection selection = designer.getSelectionModel().getSelection();

			private Rectangle getWidgetRelativeBounds(Rectangle bounds){
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

			public boolean hasNext() {
				if (i >= count) {
					return false;
				}
				BoundsWidget temp = (BoundsWidget) layout.getWidget(i);
				while (!temp.isVisible() || selection.contains(temp.getWidget())) {
					if (++i >= count) {
						return false;
					}
					temp = (BoundsWidget) layout.getWidget(i);
				}
				return true;
			}
			public int[] getHorizontalLine(){
				return ArrayUtils.EMPTY_INT_ARRAY;
			}
			public int[] getVerticalLine(){
				return ArrayUtils.EMPTY_INT_ARRAY;
			}
			public Rectangle nextRectangle() {
				BoundsWidget temp = (BoundsWidget) layout.getWidget(i++);
				return getWidgetRelativeBounds(temp.getBounds());
			}
		};
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
	
	@Override
	public Rectangle getDraggedBounds(int dx, int dy, Rectangle current_bounds, FormDesigner designer,
			Rectangle oldbounds) {
		int[] xy = sorption(oldbounds.x + dx, oldbounds.y + dy, current_bounds, designer);
		current_bounds.x = xy[0];
		current_bounds.y = xy[1];
		return current_bounds;
	}
}