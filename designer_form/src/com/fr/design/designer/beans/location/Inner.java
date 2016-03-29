package com.fr.design.designer.beans.location;

import com.fr.design.beans.location.Absorptionline;
import com.fr.design.beans.location.MoveUtils;
import com.fr.design.beans.location.MoveUtils.RectangleDesigner;
import com.fr.design.beans.location.MoveUtils.RectangleIterator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWBorderLayout;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormSelection;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget;
import com.fr.stable.ArrayUtils;

import java.awt.*;

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
		} else if (y + current_bounds.getHeight() > designer.getRootComponent().getHeight()
				&& designer.getSelectionModel().hasSelectionComponent()) {
			y = designer.getRootComponent().getHeight() - current_bounds.height;
		}
		return new Point(x, y);
	}

	@Override
	protected void sorptionPoint(Point point, Rectangle current_bounds, final FormDesigner designer) {
		RectangleDesigner rd = new RectangleDesigner() {
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
		};
		point.setLocation(MoveUtils.sorption(point.x, point.y, current_bounds.width, current_bounds.height, rd));
	}
	
	private RectangleIterator getRectangleIterator(final FormDesigner designer){
		return new RectangleIterator() {
			private int i;
			private WAbsoluteLayout layout = getLayout(designer);
			private int count = layout.getWidgetCount();
			private FormSelection selection = designer.getSelectionModel().getSelection();

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
				return temp.getBounds();
			}
		};
	}

    private WAbsoluteLayout getLayout(final FormDesigner designer){
        XLayoutContainer formLayoutContainer = (XLayoutContainer) XCreatorUtils.createXCreator(
                designer.getTarget().getContainer());
        WAbsoluteLayout layout;
        if (formLayoutContainer.acceptType(XWBorderLayout.class)){
            layout = (WAbsoluteLayout) designer.getParaComponent().toData();
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