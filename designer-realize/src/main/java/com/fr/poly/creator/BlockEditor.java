/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.creator;

import java.awt.*;

import javax.swing.JComponent;
import javax.swing.plaf.ButtonUI;

import com.fr.base.ScreenResolution;
import com.fr.design.beans.location.Absorptionline;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIButtonUI;
import com.fr.design.utils.gui.GUIPaintUtils;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.general.ComparatorUtils;
import com.fr.poly.PolyConstants;
import com.fr.poly.PolyDesigner;
import com.fr.poly.hanlder.BlockForbiddenWindow;
import com.fr.poly.hanlder.BottomCornerMouseHanlder;
import com.fr.poly.hanlder.ColumnOperationMouseHandler;
import com.fr.poly.hanlder.RowOperationMouseHandler;
import com.fr.report.poly.TemplateBlock;
import com.fr.stable.core.PropertyChangeAdapter;
import com.fr.stable.unit.UnitRectangle;

/**
 * @author richer
 * @since 6.5.4 创建于2011-5-5 聚合报表块编辑器
 */
public abstract class BlockEditor<T extends JComponent,U extends TemplateBlock> extends JComponent{

	protected PolyDesigner designer;
	protected BlockCreator<U> creator;
	protected int resolution = ScreenResolution.getScreenResolution();
	protected T editComponent;

	private JComponent addHeightTool;// 改变高度的组件
	private JComponent addWidthTool; // 改变宽度的组件
	private JComponent moveTool; // 拖动聚合块的组件

	private boolean isDragging;
	private Absorptionline lineInX;
	private Absorptionline lineInY;

	private BlockForbiddenWindow forbiddenWindow;

	public BlockEditor(PolyDesigner designer, BlockCreator<U> creator) {
		this.designer = designer;
		this.creator = creator;
		this.resolution = creator.resolution;
		this.initComponets();
		this.addColumnRowListeners();

		this.addBoundsListener();

		this.initDataChangeListener();
	}
	protected void initComponets(){
		this.setBackground(Color.WHITE);
		this.setOpaque(false);
		this.setLayout(new BlockEditorLayout());
		editComponent = createEffective();
		this.add(BlockEditorLayout.CENTER, editComponent);

		this.addHeightTool = new BlockControlButton();
		this.add(BlockEditorLayout.LEFTBOTTOM, this.addHeightTool);
		this.addHeightTool.setPreferredSize(getAddHeigthPreferredSize());

		this.addWidthTool = new BlockControlButton();
		this.add(BlockEditorLayout.RIGHTTOP, this.addWidthTool);
		this.addWidthTool.setPreferredSize(getAddWidthPreferredSize());

		this.moveTool = new BlockControlButton();
		this.add(BlockEditorLayout.BOTTOMCORNER, this.moveTool);

		this.forbiddenWindow = new BlockForbiddenWindow();
	}

	/**
	 * 重置当前鼠标选中状态
	 *
	 */
	public abstract void resetSelectionAndChooseState();

	protected abstract T createEffective();

	protected abstract Dimension getAddHeigthPreferredSize();

	protected abstract Dimension getAddWidthPreferredSize();

	protected abstract void initDataChangeListener();

	protected abstract RowOperationMouseHandler createRowOperationMouseHandler();

	protected abstract ColumnOperationMouseHandler createColumnOperationMouseHandler();

	protected void addColumnRowListeners() {
		RowOperationMouseHandler rowHandler = createRowOperationMouseHandler();
		addHeightTool.addMouseListener(rowHandler);
		addHeightTool.addMouseMotionListener(rowHandler);
		addHeightTool.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));

		ColumnOperationMouseHandler columnHandler = createColumnOperationMouseHandler();
		addWidthTool.addMouseListener(columnHandler);
		addWidthTool.addMouseMotionListener(columnHandler);
		addWidthTool.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));

		BottomCornerMouseHanlder bottomCorner = new BottomCornerMouseHanlder(designer, this);
		moveTool.addMouseListener(bottomCorner);
		moveTool.addMouseMotionListener(bottomCorner);
		moveTool.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
	}

	protected void addBoundsListener() {
		TemplateBlock tb = getValue();
		tb.addPropertyListener(new PropertyChangeAdapter() {

			@Override
			public void propertyChange() {
				initSize();
				LayoutUtils.layoutRootContainer(BlockEditor.this);
			}

			@Override
			public boolean equals(Object o) {
				if (o == null) {
					return false;
				}
				return ComparatorUtils.equals(o.getClass().getName(), this.getClass().getName());
			}
		});
	}

	public void setDragging(boolean isDragging) {
		this.isDragging = isDragging;
	}

	/**
	 * 当前是否处于拖动状态
	 *
	 * @return 是否处于拖动状态
	 *
	 */
	public boolean isDragging() {
		return this.isDragging;
	}

	public void setXAbsorptionline(Absorptionline line) {
		this.lineInX = line;
	}

	public void setYAbsorptionline(Absorptionline line) {
		this.lineInY = line;
	}


	/**
	 * 显示禁止重叠窗口
	 *
	 * @param x x坐标
	 * @param y y坐标
	 *
	 */
	public void showForbiddenWindow(int x, int y){
		this.forbiddenWindow.showWindow(x, y);
	}

	/**
	 * 隐藏禁止重叠窗口
	 *
	 */
	public void hideForbiddenWindow(){
		this.forbiddenWindow.hideWindow();
	}

	public void paintAbsorptionline(Graphics g) {
		if(lineInX != null) {
			lineInX.paint(g,designer);
		}
		if(lineInY != null) {
			lineInY.paint(g,designer);
		}
	}

	// 根据TemplateBlock的边界确定BlockEditor的边界  notice： chart的setbounds被覆盖了
	protected void initSize() {
		Dimension cornerSize = getCornerSize();
		TemplateBlock block = getValue();
		UnitRectangle ub = block.getBounds();
		int x = ub.x.toPixI(resolution) - cornerSize.width - designer.getHorizontalValue();
		int y = ub.y.toPixI(resolution) - cornerSize.height - designer.getVerticalValue();
		int w = ub.width.toPixI(resolution) + cornerSize.width + PolyConstants.OPERATION_SIZE;
		int h = ub.height.toPixI(resolution) + cornerSize.height + PolyConstants.OPERATION_SIZE;
		setBounds(new Rectangle(x, y, w, h));
	}


	public U getValue() {
		return creator.getValue();
	}


	public Dimension getCornerSize() {
		return new Dimension();
	}

	private class BlockControlButton extends UIButton {
        @Override
        public ButtonUI getUI() {
            return new UIButtonUI() {
                // 调换 normal 和 rollover 状态的填充色
                protected void doExtraPainting(UIButton b, Graphics2D g2d, int w, int h, String selectedRoles) {
                    if (isPressed(b) && b.isPressedPainted()) {
                        GUIPaintUtils.fillPressed(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles));
                    } else if (isRollOver(b)) {
                        GUIPaintUtils.fillNormal(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), b.isPressedPainted());
                    } else if (b.isNormalPainted()) {
                        GUIPaintUtils.fillRollOver(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), b.isPressedPainted());
                    }
                }
            };
        }
	}
}
