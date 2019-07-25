/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.hanlder;

import com.fr.base.ScreenResolution;
import com.fr.base.chart.BaseChartCollection;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.mainframe.DesignerContext;
import com.fr.grid.Grid;
import com.fr.log.FineLoggerFactory;
import com.fr.poly.PolyDesigner;
import com.fr.poly.PolyUtils;
import com.fr.poly.creator.BlockCreator;
import com.fr.poly.model.AddingData;
import com.fr.stable.unit.UnitRectangle;

import javax.swing.JScrollBar;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;

/**
 * @author richer
 * @since 6.5.4 创建于2011-4-1
 */
public class PolyDesignerDropTarget extends DropTargetAdapter {
	
    private static final double SCROLL_POINT = 100;
    private static final int SCROLL_DISTANCE = 15;
	
	private PolyDesigner designer;
	private AddingData addingData;
	private int resolution = ScreenResolution.getScreenResolution();
	//禁止重叠提示窗口
	private BlockForbiddenWindow forbiddenWindow = new BlockForbiddenWindow();

	public PolyDesignerDropTarget(PolyDesigner designer) {
		this.designer = designer;
		new DropTarget(designer, this);
	}

	/**
	 * 拖拽进入事件
	 * 
	 * @param dtde 鼠标事件
	 * 
	 */
	public void dragEnter(DropTargetDragEvent dtde) {

		if (DesignerMode.isAuthorityEditing()) {
			return;
		}
		// richer:避免在一次拖拽过程中重复查找
		if (designer.getAddingData() != null) {
			return;
		}
		try {
			Transferable tr = dtde.getTransferable();
			DataFlavor[] flavors = tr.getTransferDataFlavors();
			for (int i = 0; i < flavors.length; i++) {
				if (!tr.isDataFlavorSupported(flavors[i])) {
					continue;
				}
				Object obj = tr.getTransferData(flavors[i]);
				BlockCreator creator = null;
				if (obj instanceof Class) {
					Class clazz = (Class) obj;
					creator = PolyUtils.createCreator(clazz);
				} else if (obj instanceof BaseChartCollection) {
					creator = PolyUtils.createCreator((BaseChartCollection) obj);
				}
				if (creator == null) {
					return;
				}

				addingData = new AddingData(creator);
				designer.setAddingData(addingData);
			}
		} catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
		}
		Point loc = dtde.getLocation();
		addingData.moveTo(loc);
		designer.repaint();
	}

	/**
	 * 拖拽悬浮事件
	 * 
	 * @param dtde 鼠标事件
	 * 
	 */
	public void dragOver(DropTargetDragEvent dtde) {
		if (addingData != null) {
			Point loc = dtde.getLocation();
			addingData.moveTo(loc);
			//检测是否跟别的组件重叠
			setForbiddenWindowVisibility(loc);
			//检测是否到达角落
			scrollWhileDropCorner(dtde);
			
			designer.repaint();
		}
	}
	
	private void scrollWhileDropCorner(final DropTargetDragEvent dtde){
		Thread tt = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Point location = dtde.getLocation();
				//当block的右下角小于当前面板的-SCROLL_POINT时, 开始滚动
				if(location.x > designer.getWidth() - SCROLL_POINT){
					JScrollBar horizonBar = designer.getHorizontalScrollBar();
					horizonBar.setValue(horizonBar.getValue() + SCROLL_DISTANCE);
				}
				
				if(location.y> designer.getHeight() - SCROLL_POINT){
					JScrollBar verticalBar = designer.getVerticalScrollBar();
					verticalBar.setValue(verticalBar.getValue() + SCROLL_DISTANCE);
				}
				
			}
		});
		tt.start();
	}
	
	//设置是否显示 禁止组件重叠 窗口
	private void setForbiddenWindowVisibility(Point loc){
		BlockCreator creator = addingData.getCreator();
		Rectangle pixRec = getCreatorPixRectangle(creator, loc);
		UnitRectangle rec = new UnitRectangle(pixRec, resolution);
		
		if(designer.intersectsAllBlock(rec, creator.getValue().getBlockName())){
	        int x = (int) (designer.getAreaLocationX() + pixRec.getCenterX() - designer.getHorizontalValue());
	        int y = (int) (designer.getAreaLocationY() + pixRec.getCenterY() - designer.getVerticalValue());
	        forbiddenWindow.showWindow(x, y);
		}else{
			forbiddenWindow.hideWindow();
		}
	}

	/**
	 * 放下事件
	 * 
	 * @param dtde 鼠标事件
	 * 
	 */
	public void drop(DropTargetDropEvent dtde) {
		if (addingData != null) {
			designer.stopAddingState();
			BlockCreator creator = addingData.getCreator();
			Point location = dtde.getLocation();
			Rectangle pixRec = getCreatorPixRectangle(creator, location);
			if(!intersectLocation(pixRec, creator)){
				return;
			}
			
			designer.addBlockCreator(creator);
			designer.stopEditing();
			designer.setSelection(creator);
			//在重新设置了选择之后，要对菜单和工具进行target的重新设置
			DesignerContext.getDesignerFrame().resetToolkitByPlus(DesignerContext.getDesignerFrame().getSelectedJTemplate());
			focusOnSelection();
			designer.fireTargetModified();
			// richer:拖拽完成后需要把addingData重置
			addingData = null;
		}
	}
	
	//聚焦选中块
	private void focusOnSelection(){
		if (designer.getSelection().getEditingElementCasePane() == null) {
			return;
		}
		Grid grid = designer.getSelection().getEditingElementCasePane().getGrid();
		if (!grid.hasFocus() && grid.isRequestFocusEnabled()) {
			grid.requestFocus();
		}
	}
	
	//检测新加入的creator位置是否与老的重叠, 重叠返回false
	private boolean intersectLocation(Rectangle pixRec, BlockCreator creator){
		if (pixRec.getX() < 0 || pixRec.getY() < 0) {
			forbiddenWindow.hideWindow();
			designer.repaint();
			return false;
		}
		UnitRectangle rec = new UnitRectangle(pixRec, resolution);
		if(designer.intersectsAllBlock(rec, creator.getValue().getBlockName())){
			//停止移动后要隐藏掉禁止重叠的标志
			forbiddenWindow.hideWindow();
			designer.repaint();
			return false;
		}
		
		creator.getValue().setBounds(rec);
		
		return true;
	}
	
	private Rectangle getCreatorPixRectangle(BlockCreator creator, Point location){
		int width = creator.getWidth();
		int height = creator.getHeight();
		int resx = location.x - width / 2 + designer.getHorizontalValue();
		int resy = location.y - height / 2 + designer.getVerticalValue();
		return new Rectangle(resx, resy, width, height);
	}

	/**
	 * 拖拽移出去事件
	 * 
	 * @param dte 拖拽事件
	 * 
	 */
	public void dragExit(DropTargetEvent dte) {
		if (addingData != null) {
			addingData.reset();
			designer.repaint();
		}
		
		forbiddenWindow.hideWindow();
	}
	
}