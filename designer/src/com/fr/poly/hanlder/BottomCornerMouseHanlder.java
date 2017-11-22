/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.hanlder;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import com.fr.base.BaseUtils;
import com.fr.base.ScreenResolution;
import com.fr.design.beans.location.Absorptionline;
import com.fr.design.beans.location.MoveUtils;
import com.fr.design.beans.location.MoveUtils.RectangleDesigner;
import com.fr.design.beans.location.MoveUtils.RectangleIterator;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.general.ComparatorUtils;
import com.fr.poly.PolyDesigner;
import com.fr.poly.PolyDesigner.SelectionType;
import com.fr.poly.creator.BlockEditor;
import com.fr.report.poly.TemplateBlock;
import com.fr.stable.ArrayUtils;
import com.fr.stable.unit.UnitRectangle;

/**
 * @author richer
 * @since 6.5.4 创建于2011-4-11
 */
public class BottomCornerMouseHanlder extends MouseInputAdapter {
	private PolyDesigner designer;
	private BlockEditor editor;
	private Point pressed;
	private UnitRectangle oldBounds;
	private int resolution = ScreenResolution.getScreenResolution();

	public BottomCornerMouseHanlder(PolyDesigner designer, BlockEditor editor) {
		this.designer = designer;
		this.editor = editor;
	}

	/**
	 * 鼠标按下事件
	 * 
	 * @param e 鼠标事件
	 * 
	 * @date 2015-2-12-下午2:32:04
	 * 
	 */
	public void mousePressed(MouseEvent e) {
		if (BaseUtils.isAuthorityEditing()) {
			designer.noAuthorityEdit();
		}
		pressed = e.getPoint();
		oldBounds = editor.getValue().getBounds();
		designer.setChooseType(SelectionType.BLOCK);
	}

	/**
	 * 鼠标释放事件
	 * 
	 * @param e 鼠标事件
	 * 
	 * @date 2015-2-12-下午2:32:04
	 * 
	 */
	public void mouseReleased(MouseEvent e) {
		if (BaseUtils.isAuthorityEditing()) {
			designer.noAuthorityEdit();
		}
		editor.setDragging(false);

		if(designer.intersectsAllBlock(editor.getValue())){
			//检测重叠
			editor.getValue().setBounds(oldBounds);
			return;
		}
		
		if (!ComparatorUtils.equals(editor.getValue().getBounds(), oldBounds)) {
			designer.fireTargetModified();
		}
		designer.repaint();
	}

	/**
	 * 鼠标拖拽事件
	 * 
	 * @param e 鼠标事件
	 * 
	 * @date 2015-2-12-下午2:32:04
	 * 
	 */
	public void mouseDragged(MouseEvent e) {
		if (BaseUtils.isAuthorityEditing()) {
			designer.noAuthorityEdit();
			return;
		}
		editor.setDragging(true);
		Point dragStart = e.getPoint();
		dragStart.x -= pressed.x;
		dragStart.y -= pressed.y;
		TemplateBlock block = editor.getValue();

		resolution = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().getJTemplateResolution();
		Rectangle bounds = block.getBounds().toRectangle(resolution);
		Point resultPoint = MoveUtils.sorption(bounds.x + dragStart.x < 0 ? 0 : bounds.x + dragStart.x, bounds.y
				+ dragStart.y < 0 ? 0 : bounds.y + dragStart.y, bounds.width, bounds.height, rectDesigner, false);
		block.setBounds(new UnitRectangle(new Rectangle(resultPoint.x, resultPoint.y, bounds.width, bounds.height),
				resolution));
		designer.repaint();
	}
	
	private RectangleDesigner rectDesigner = new RectangleDesigner() {
		@Override
		public void setXAbsorptionline(Absorptionline line) {
			editor.setXAbsorptionline(line);
		}

		@Override
		public void setYAbsorptionline(Absorptionline line) {
			editor.setYAbsorptionline(line);
		}

		@Override
		public RectangleIterator createRectangleIterator() {
			return getRectangleIt();
		}

		/**
		 * 设置等距线
		 *
		 * @param line 吸附线
		 */
		@Override
		public void setEquidistantLine(Absorptionline line) {

		}

		/**
		 * 获取当前选中块的垂直线数组
		 * 
		 * @return 块的垂直线数组
		 * 
		 */
		public int[] getVerticalLine(){
			return editor.getValue().getVerticalLine();
		}
		
		/**
		 * 获取当前选中块的水平线数组
		 * 
		 * @return 块的水平线数组
		 * 
		 */
		public int[] getHorizontalLine(){
			return editor.getValue().getHorizontalLine();
		}

		/**
		 * 设置designer内部组件是否重叠的标志位
		 *
		 * @param isIntersects 是否重叠
		 */
		@Override
		public void setWidgetsIntersected(boolean isIntersects) {
		}

		/**
		 * 获取designer内部组件是否重叠的标志位
		 *
		 * @return 重叠
		 */
		@Override
		public boolean isWidgetsIntersected() {
			return false;
		}

		/**
		 * 获取designer相对屏幕的位置
		 *
		 * @return 位置
		 */
		@Override
		public Point getDesignerLocationOnScreen() {
			return null;
		}

		@Override
		public int getDesignerScrollVerticalValue() {
			return 0;
		}

		@Override
		public int getDesignerScrollHorizontalValue() {
			return 0;
		}
	};
	
	private RectangleIterator getRectangleIt(){
		return new RectangleIterator() {
			private int i;
	
			@Override
			public boolean hasNext() {
				//是否超了
				boolean isOverFlow = i >= designer.getTarget().getBlockCount();
				if(isOverFlow){
					return false;
				}
				
				//是否为当前已经选中的块
				boolean isSelf = designer.getTarget().getBlock(i) == editor.getValue();
				if(!isSelf){
					return true;
				}
				
				//如果是自身, 则判断一下一个块
				boolean isNextOverFlow = ++i < designer.getTarget().getBlockCount();
				return isNextOverFlow;
			}
			
			public int[] getHorizontalLine(){
				//因为取next的时候已经i++了, 所以这边获取块的值要-1
				TemplateBlock block = (TemplateBlock) designer.getTarget().getBlock(i - 1);
				if(block == null){
					return ArrayUtils.EMPTY_INT_ARRAY;
				}
				
				return block.getHorizontalLine();
			}
			
			public int[] getVerticalLine(){
				TemplateBlock block = (TemplateBlock) designer.getTarget().getBlock(i - 1);
				if(block == null){
					return ArrayUtils.EMPTY_INT_ARRAY;
				}
				
				return block.getVerticalLine();
			}
			
			@Override
			public Rectangle nextRectangle() {
				UnitRectangle UnitBounds = designer.getTarget().getBlock(i++).getBounds();
				return UnitBounds.toRectangle(resolution);
			}
		};
	}
}