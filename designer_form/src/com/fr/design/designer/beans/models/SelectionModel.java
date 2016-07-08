package com.fr.design.designer.beans.models;

import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.beans.location.Location;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormSelection;
import com.fr.design.mainframe.FormSelectionUtils;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.stable.ArrayUtils;

/**
 * 该model保存当前选择的组件和剪切版信息
 */
public class SelectionModel {
    private static final int DELTA_X_Y = 20; //粘贴时候的偏移距离
	private static FormSelection CLIP_BOARD = new FormSelection();
	private FormDesigner designer;
	private FormSelection selection;
	private Rectangle hotspot_bounds;

	public SelectionModel(FormDesigner designer) {
		this.designer = designer;
		selection = new FormSelection();
	}

	/**
	 * 重置。清空formSelction以及选择区域
	 */
	public void reset() {
		selection.reset();
		hotspot_bounds = null;
	}
	
	/**
	 * formSelction是否为空
	 * @return 是否为空
	 */
	public static boolean isEmpty(){
		return CLIP_BOARD.isEmpty();
	}

	/**
	 * 鼠标点击一下，所选中的单个组件。按下Ctrl或者shift键时鼠标可以进行多选
	 * @param e 鼠标事件
	 */
	public void selectACreatorAtMouseEvent(MouseEvent e) {
		if (!e.isControlDown() && !e.isShiftDown()) {
			// 如果Ctrl或者Shift键盘没有按下，则清除已经选择的组件
			selection.reset();
		}

		// 获取e所在的组件
		XCreator comp = designer.getComponentAt(e);

		//布局组件的顶层布局如不可编辑，要获取其顶层布局
		XLayoutContainer topLayout = XCreatorUtils.getHotspotContainer(comp).getTopLayout();
		if(topLayout != null && !topLayout.isEditable()){
			comp = topLayout;
		}

		// 如果父层是scale和title两个专属容器，返回其父层，组件本身是不让被选中的
		if (comp != designer.getRootComponent() && comp != designer.getParaComponent()) {
			XCreator parentContainer = (XCreator) comp.getParent();
			comp = parentContainer.isDedicateContainer() ? parentContainer : comp;
		}
		if (selection.removeSelectedCreator(comp) || selection.addSelectedCreator(comp)) {
			designer.getEditListenerTable().fireCreatorModified(comp, DesignerEvent.CREATOR_SELECTED);
			designer.repaint();
		}
	}

	/**
	 * 将所选组件剪切到剪切板上
	 */
	public void cutSelectedCreator2ClipBoard() {
		if (hasSelectionComponent()) {
			selection.cut2ClipBoard(CLIP_BOARD);
			designer.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_CUTED);
			designer.repaint();
		}
	}

	/**
	 * 复制当前选中的组件到剪切板
	 */
	public void copySelectedCreator2ClipBoard() {
		if (!selection.isEmpty()) {
			selection.copy2ClipBoard(CLIP_BOARD);
		}
	}

	/**
	 * 从剪切板粘帖组件
	 * @return 否
	 */
	public boolean pasteFromClipBoard() {
		if (!CLIP_BOARD.isEmpty()) {
			XLayoutContainer parent = null;
			if (!hasSelectionComponent()) {
				FormSelectionUtils.paste2Container(designer, designer.getRootComponent(),CLIP_BOARD, DELTA_X_Y, DELTA_X_Y);
			} else {
				parent = XCreatorUtils.getParentXLayoutContainer(selection.getSelectedCreator());
				if (parent != null) {
					Rectangle rec = selection.getSelctionBounds();
					FormSelectionUtils.paste2Container(designer, parent,CLIP_BOARD, rec.x + DELTA_X_Y, rec.y + DELTA_X_Y);
				}
			}
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
		return false;
	}

	public FormSelection getSelection() {
		return selection;
	}

	/**
	 * 删除当前所有选择的组件
	 */
	public void deleteSelection() {
		XCreator[] roots = selection.getSelectedCreators();

		if (roots.length > 0) {
			for (XCreator creator : roots) {
                if(creator.acceptType(XWParameterLayout.class)){
                    designer.removeParaComponent();
                }
                
				removeCreatorFromContainer(creator, creator.getWidth(), creator.getHeight());
				creator.removeAll();
				// 清除被选中的组件
                selection.reset();
			}
            setSelectedCreator(designer.getRootComponent());
			// 触发事件
			designer.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_DELETED);
			designer.repaint();
		}
	}

	/**
	 * 从选择组件中删除某组件
	 * 
	 * @param creator 组件
	 * @param creatorWidth 组件之前宽度
	 * @param creatorHeight 组件之前高度
	 */
	public void removeCreator(XCreator creator, int creatorWidth, int creatorHeight) {
		selection.removeCreator(creator);
		removeCreatorFromContainer(creator, creatorWidth, creatorHeight);
		designer.repaint();
	}

	/**
	 * 设置选择区域
	 */
	public void setHotspotBounds(Rectangle rect) {
		hotspot_bounds = rect;
	}

	/**
	 * 获得当前选择区域
	 */
	public Rectangle getHotspotBounds() {
		return hotspot_bounds;
	}

	private void removeCreatorFromContainer(XCreator creator, int creatorWidth, int creatorHeight) {
		XLayoutContainer parent = XCreatorUtils.getParentXLayoutContainer(creator);
		if (parent == null) {
			return;
		}
		boolean changeCreator = creator.shouldScaleCreator() || creator.hasTitleStyle();
		if (parent.acceptType(XWFitLayout.class) && changeCreator) {
			creator = (XCreator) creator.getParent();
		}
		parent.getLayoutAdapter().removeBean(creator, creatorWidth, creatorHeight);
		// 删除其根组件，同时就删除了同时被选择的叶子组件
		parent.remove(creator);
		LayoutManager layout = parent.getLayout();

		if (layout != null) {
			// 刷新组件容器的布局
			LayoutUtils.layoutContainer(parent);
		}
	}

	/**
	 * 是否有组件被选择。如果所选组件是最底层容器，也视为无选择
	 * @return 是则返回true
	 */
	public boolean hasSelectionComponent() {
		return !selection.isEmpty() && selection.getSelectedCreator().getParent() != null;
	}

	/**
	 * 移动组件至指定位置
	 * @param x 坐标x
	 * @param y 坐标y
	 */
	public void move(int x, int y) {
		for (XCreator creator : selection.getSelectedCreators()) {
				creator.setLocation(creator.getX() + x, creator.getY() + y);
				LayoutAdapter layoutAdapter = AdapterBus.searchLayoutAdapter(designer, creator);
				if (layoutAdapter != null) {
					layoutAdapter.fix(creator);
				}
		}
		designer.getEditListenerTable().fireCreatorModified(selection.getSelectedCreator(),
					DesignerEvent.CREATOR_SELECTED);
	}

	/**
	 * 释放捕获
	 */
	public void releaseDragging() {
		designer.setPainter(null);
		selection.fixCreator(designer);
		designer.getEditListenerTable().fireCreatorModified(selection.getSelectedCreator(),
				DesignerEvent.CREATOR_RESIZED);
	}

	public Direction getDirectionAt(MouseEvent e) {
		Direction dir;
		if (e.isControlDown() || e.isShiftDown()) {
			XCreator creator = designer.getComponentAt(e.getX(), e.getY(), selection.getSelectedCreators());
			if (creator != designer.getRootComponent() && selection.addedable(creator)) {
				return Location.add;
			}
		}
		if (hasSelectionComponent()) {
			int x = e.getX() + designer.getArea().getHorizontalValue();
			int y = e.getY() + designer.getArea().getVerticalValue();
			dir = getDirection(selection.getRelativeBounds(), x, y);
			if (selection.size() == 1) {
				if (!ArrayUtils.contains(selection.getSelectedCreator().getDirections(), dir.getActual())) {
					dir = Location.outer;
				}
			}
		} else {
			dir = Location.outer;
		}

		if (designer.getDesignerMode().isFormParameterEditor() && dir == Location.outer) {
			dir = designer.getLoc2Root(e);
		}
		return dir;
	}

	private Direction getDirection(Rectangle bounds, int x, int y) {
		if (x < (bounds.x - XCreatorConstants.RESIZE_BOX_SIZ)) {
			return Location.outer;
		} else if ((x >= (bounds.x - XCreatorConstants.RESIZE_BOX_SIZ)) && (x <= bounds.x)) {
			if (y < (bounds.y - XCreatorConstants.RESIZE_BOX_SIZ)) {
				return Location.outer;
			} else if ((y >= (bounds.y - XCreatorConstants.RESIZE_BOX_SIZ)) && (y <= bounds.y)) {
				return Location.left_top;
			} else if ((y > bounds.y) && (y < (bounds.y + bounds.height))) {
				return Location.left;
			} else if ((y >= (bounds.y + bounds.height))
					&& (y <= (bounds.y + bounds.height + XCreatorConstants.RESIZE_BOX_SIZ))) {
				return Location.left_bottom;
			} else {
				return Location.outer;
			}
		} else if ((x > bounds.x) && (x < (bounds.x + bounds.width))) {
			if (y < (bounds.y - XCreatorConstants.RESIZE_BOX_SIZ)) {
				return Location.outer;
			} else if ((y >= (bounds.y - XCreatorConstants.RESIZE_BOX_SIZ)) && (y <= bounds.y)) {
				return Location.top;
			} else if ((y > bounds.y) && (y < (bounds.y + bounds.height))) {
				return Location.inner;
			} else if ((y >= (bounds.y + bounds.height))
					&& (y <= (bounds.y + bounds.height + XCreatorConstants.RESIZE_BOX_SIZ))) {
				return Location.bottom;
			} else {
				return Location.outer;
			}
		} else if ((x >= (bounds.x + bounds.width))
				&& (x <= (bounds.x + bounds.width + XCreatorConstants.RESIZE_BOX_SIZ))) {
			if (y < (bounds.y - XCreatorConstants.RESIZE_BOX_SIZ)) {
				return Location.outer;
			} else if ((y >= (bounds.y - XCreatorConstants.RESIZE_BOX_SIZ)) && (y <= bounds.y)) {
				return Location.right_top;
			} else if ((y > bounds.y) && (y < (bounds.y + bounds.height))) {
				return Location.right;
			} else if ((y >= (bounds.y + bounds.height))
					&& (y <= (bounds.y + bounds.height + XCreatorConstants.RESIZE_BOX_SIZ))) {
				return Location.right_bottom;
			} else {
				return Location.outer;
			}
		} else {
			return Location.outer;
		}
	}

	private void fireCreatorSelected() {
		designer.getEditListenerTable().fireCreatorModified(selection.getSelectedCreator(),
				DesignerEvent.CREATOR_SELECTED);
	}

	public void setSelectedCreator(XCreator rootComponent) {
		selection.setSelectedCreator(rootComponent);
		fireCreatorSelected();
	}

	public void setSelectedCreators(ArrayList<XCreator> rebuildSelection) {
		selection.setSelectedCreators(rebuildSelection);
		fireCreatorSelected();
	}
}