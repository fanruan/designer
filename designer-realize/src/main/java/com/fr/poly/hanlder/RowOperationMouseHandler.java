/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.hanlder;

import java.awt.Point;
import java.awt.event.MouseEvent;

import com.fr.base.BaseUtils;
import com.fr.base.ScreenResolution;
import com.fr.general.ComparatorUtils;
import com.fr.poly.PolyDesigner;
import com.fr.poly.PolyDesigner.SelectionType;
import com.fr.poly.creator.BlockEditor;
import com.fr.poly.creator.ChartBlockEditor;
import com.fr.poly.creator.ECBlockEditor;
import com.fr.report.poly.PolyWorkSheet;
import com.fr.report.poly.TemplateBlock;
import com.fr.stable.unit.FU;
import com.fr.stable.unit.UNIT;
import com.fr.stable.unit.UnitRectangle;

/**
 * @author richer
 * @since 6.5.4 创建于2011-4-8 添加删除行
 */
public abstract class RowOperationMouseHandler<T extends BlockEditor> extends BlockOperationMouseHandler {

	protected PolyDesigner designer;

	private T editor;
	private Point startPoint;
	private UnitRectangle oldBounds;
	private int resolution = ScreenResolution.getScreenResolution();

	public RowOperationMouseHandler(PolyDesigner designer, T editor) {
		this.designer = designer;
		this.editor = editor;
	}

	protected T getTargetEditor() {
		return editor;
	}

	protected boolean isEditorResized() {
		return !ComparatorUtils.equals(editor.getValue().getBounds(), oldBounds);
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
		startPoint = e.getPoint();
		designer.setChooseType(SelectionType.BLOCK);
		oldBounds = editor.getValue().getBounds();
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
			return;
		}
		TemplateBlock block = editor.getValue();
		Point p = e.getPoint();
		int delta = p.y - startPoint.y;
		
		UnitRectangle rec = block.getBounds();
		UNIT deltaUnit = FU.valueOfPix(delta, resolution);
		rec.setHeight(rec.getHeight().add(deltaUnit));
		block.setBounds(rec, this.getTarget());
	}
	
	protected PolyWorkSheet getTarget(){
		return designer.getTarget();
	}

	public static class ECBlockRowOperationMouseHandler extends RowOperationMouseHandler<ECBlockEditor> {

		public ECBlockRowOperationMouseHandler(PolyDesigner designer, ECBlockEditor editor) {
			super(designer, editor);
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
			if (isEditorResized()) {
				getTargetEditor().getValue().reCalculateBlockSize();
				designer.fireTargetModified();
			}
		}

	}
	public static class ChartBlockRowOperationMouseHandler extends RowOperationMouseHandler<ChartBlockEditor> {

		public ChartBlockRowOperationMouseHandler(PolyDesigner designer, ChartBlockEditor editor) {
			super(designer, editor);
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
			getTargetEditor().refreshChartComponent();
			super.mousePressed(e);
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
			if (isEditorResized()) {
				designer.fireTargetModified();
			}
		}
	}
}