/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.creator;

import javax.swing.JComponent;

import com.fr.design.DesignState;
import com.fr.design.designer.TargetComponent;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.selection.QuickEditor;
import com.fr.poly.JPolyBlockPane;
import com.fr.poly.PolyDesigner;
import com.fr.report.poly.PolyCoreUtils;
import com.fr.report.poly.PolyECBlock;
import com.fr.stable.unit.UnitRectangle;

/**
 * @author richer
 * @since 6.5.4 创建于2011-4-1
 */
public class ECBlockCreator extends BlockCreator<PolyECBlock> {
	private ECBlockEditor editor;


	public ECBlockCreator() {

	}

	public ECBlockCreator(PolyECBlock block) {
		super(block);
	}

	@Override
	protected JComponent initMonitor() {
		return new JPolyBlockPane(block);
	}

	@Override
	public PolyECBlock getValue() {
		return block;
	}

	@Override
	public PolyDesigner getDesigner() {
		return designer;
	}

	@Override
	public void setDesigner(PolyDesigner designer) {
		this.designer = designer;
	}
	
	public UnitRectangle getDefaultBlockBounds() {
		return PolyCoreUtils.getDefaultBlockBounds();
	}

	@Override
	public BlockEditor getEditor() {
		if (editor == null) {
			editor = new ECBlockEditor(designer, this);
		}
		return editor;
	}

	@Override
	public int getX(float time) {
		return Math.round (this.getX() * time);
	}

	@Override
	public int getY(float time) {
		return Math.round (this.getY() * time);
	}

	/**
	 * 检测按钮状态
	 * 
	 * @date 2015-2-5-上午11:33:46
	 * 
	 */
	public void checkButtonEnable() {
		return;
	}


	@Override
	public void setValue(PolyECBlock block) {
		block.setWorksheet(designer.getTarget());
		
		cal(block);
		repaint();
	}

	/**
	 * 当前对象的工具栏数组
	 * 
	 * @return 工具栏数组
	 * 
	 * @date 2015-2-5-上午11:32:10
	 * 
	 */
	public ToolBarDef[] toolbars4Target() {
		return editor.createEffective().toolbars4Target();
	}

	/**
	 * 在Form的工具栏组
	 * 
	 * @return 组件数组
	 * 
	 * @date 2015-2-5-上午11:31:46
	 * 
	 */
	public JComponent[] toolBarButton4Form() {
		return editor.createEffective().toolBarButton4Form();
	}

	/**
	 * 获取当前菜单栏组
	 * 
	 * @return 菜单栏组
	 * 
	 * @date 2015-2-5-上午11:29:07
	 * 
	 */
	public MenuDef[] menus4Target() {
		return editor.createEffective().menus4Target();
	}

    public  int getMenuState(){
        return DesignState.WORK_SHEET;
    }

    /**
	 * 获取菜单里的快捷方式数组
	 * 
	 * @return 菜单里的快捷方式数组
	 * 
	 * @date 2015-2-5-上午11:27:08
	 * 
	 */
	public ShortCut[] shortcut4TemplateMenu() {
		return editor.createEffective().shortcut4TemplateMenu();
	}

	@Override
	public PolyElementCasePane getEditingElementCasePane() {
		return editor.createEffective();
	}

	@Override
	public QuickEditor getQuickEditor(TargetComponent tc) {
		return editor.createEffective().getCurrentEditor();
	}

}