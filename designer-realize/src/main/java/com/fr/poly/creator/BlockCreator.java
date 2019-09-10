/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.creator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JComponent;

import com.fr.base.ScreenResolution;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.selection.SelectableElement;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.general.ComparatorUtils;
import com.fr.poly.PolyConstants;
import com.fr.poly.PolyDesigner;
import com.fr.report.poly.TemplateBlock;
import com.fr.stable.core.PropertyChangeAdapter;
import com.fr.stable.unit.UNITConstants;
import com.fr.stable.unit.UnitRectangle;

/**
 * @author richer
 * @since 6.5.4 创建于2011-5-5 聚合块编辑器
 */
public abstract class BlockCreator<T extends TemplateBlock> extends JComponent implements java.io.Serializable, SelectableElement {
	protected PolyDesigner designer;
	protected T block;

	protected int resolution = ScreenResolution.getScreenResolution();

	public BlockCreator() {

	}

	public BlockCreator(T block) {
		cal(block);
	}

	protected void cal(T block) {
		if (this.block != block) {
			this.block = block;
			block.addPropertyListener(new PropertyChangeAdapter() {

				@Override
				public void propertyChange() {
					calculateMonitorSize();
				}

				@Override
				public boolean equals(Object o) {
					if (o == null) {
						return false;
					}
					return ComparatorUtils.equals(o.getClass().getName(), this.getClass().getName());
				}
			});
			this.removeAll();
			this.setLayout(FRGUIPaneFactory.createBorderLayout());
			this.add(initMonitor(), BorderLayout.CENTER);
		}
		this.calculateMonitorSize();
	}

	protected void calculateMonitorSize() {
		UnitRectangle bounds = block.getBounds();
		if (bounds == null) {
			bounds = this.getDefaultBlockBounds();
			block.setBounds(bounds);
		}
		this.setBounds(bounds.x.toPixI(resolution), bounds.y.toPixI(resolution), bounds.width.toPixI(resolution) + UNITConstants.DELTA.toPixI(resolution),
				bounds.height.toPixI(resolution) + UNITConstants.DELTA.toPixI(resolution));
		LayoutUtils.layoutContainer(this);
	}

	public void setResolution(int resolution){
		this.resolution = resolution;
	}

	//默认大小, 报表块默认3列6行, 图表块默认330*240
	public abstract UnitRectangle getDefaultBlockBounds();

	// 不同的Block有不同的显示器
	protected abstract JComponent initMonitor();

	public abstract PolyElementCasePane getEditingElementCasePane();

	public abstract BlockEditor getEditor();

	public abstract int getX(float time);

	public abstract int getY(float time);



	/**
	 * 检测按钮状态
	 *
	 * @date 2015-2-5-上午11:33:46
	 *
	 */
	public abstract void checkButtonEnable();

	public PolyDesigner getDesigner() {
		return designer;
	}

	public void setDesigner(PolyDesigner designer) {
		this.designer = designer;
	}

	public Rectangle getEditorBounds() {
		Rectangle bounds = this.getBounds();
		Dimension d = getEditor().getCornerSize();
		//ECBlockCreator缩放的时候边框需要重新算下
		double times = this.designer.getResolution() / (double)resolution;
		bounds.x -= d.width/times + designer.getHorizontalValue();
		bounds.y -= d.height/times + designer.getVerticalValue();
		bounds.width += Math.ceil(d.width/times + PolyConstants.OPERATION_SIZE/times);
		bounds.height += Math.ceil(d.height/times + PolyConstants.OPERATION_SIZE/times);
		return bounds;
	}

	public T getValue() {
		return block;
	}

	public abstract void setValue(T block);

	// /////////////////////////////////////ToolBarMenuDock//////////////


	/**
	 * 获取当前工具栏组
	 *
	 * @return 工具栏组
	 *
	 * @date 2015-2-5-上午11:29:07
	 *
	 */
	public abstract ToolBarDef[] toolbars4Target();

	/**
	 * 在Form的工具栏组
	 *
	 * @return 组件数组
	 *
	 * @date 2015-2-5-上午11:31:46
	 *
	 */
	public abstract JComponent[] toolBarButton4Form();

	/**
	 * 获取当前菜单栏组
	 *
	 * @return 菜单栏组
	 *
	 * @date 2015-2-5-上午11:29:07
	 *
	 */
	public abstract MenuDef[] menus4Target();

    public  abstract int  getMenuState();


    /**
	 * 获取菜单里的快捷方式数组
	 *
	 * @return 菜单里的快捷方式数组
	 *
	 * @date 2015-2-5-上午11:27:08
	 *
	 */
	public abstract ShortCut[] shortcut4TemplateMenu();

}
