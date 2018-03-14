/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import com.fr.base.FRContext;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.DesignState;
import com.fr.design.actions.AllowAuthorityEditAction;
import com.fr.design.actions.ExitAuthorityEditAction;
import com.fr.design.designer.EditingState;
import com.fr.design.designer.TargetComponent;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.*;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.NameSeparator;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.present.ConditionAttributesGroupPane;
import com.fr.form.FormElementCaseProvider;
import com.fr.form.main.Form;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.CellElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.worksheet.FormElementCase;
import com.fr.report.worksheet.WorkSheet;
import com.fr.design.selection.SelectableElement;
import com.fr.design.selection.Selectedable;
import com.fr.design.selection.SelectionListener;

/**
 * 表单中的ElementCase编辑面板
 */
public class FormElementCaseDesigner<T extends FormElementCaseProvider, E extends ElementCasePane, S extends SelectableElement> extends TargetComponent<T> implements Selectedable<S>, FormECDesignerProvider{
	protected FormElementCasePaneDelegate elementCasePane;
	public FormElementCasePaneDelegate getEditingElementCasePane() {
		return elementCasePane;
	}
	
    public FormElementCaseDesigner(T sheet, Form form) {
        super(sheet);

        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        elementCasePane = new FormElementCasePaneDelegate((FormElementCase) sheet, form);
        elementCasePane.setSelection(getDefaultSelectElement());
        this.add(elementCasePane, BorderLayout.CENTER);
        elementCasePane.addTargetModifiedListener(new TargetModifiedListener() {

            @Override
            public void targetModified(TargetModifiedEvent e) {
            	FormElementCaseDesigner.this.fireTargetModified();
            }
        });

    }

    @Override
    public void setTarget(T t) {
        super.setTarget(t);

        this.elementCasePane.setTarget((FormElementCase) t);
    }

    public int getMenuState() {
        return DesignState.WORK_SHEET;
    }

    /**
     * 权限细粒度情况下的子菜单
     *
     * @return 子菜单
     */
	public ShortCut[] shortCuts4Authority() {
		return new ShortCut[]{
				new NameSeparator(Inter.getLocText(new String[]{"DashBoard-Potence", "Edit"})),
				DesignerMode.isAuthorityEditing() ? new ExitAuthorityEditAction(this) : new AllowAuthorityEditAction(this),
		};

	}

    /**
     * 创建权限细粒度面板
     *
     * @return 返回权限细粒度面板
     */
    public AuthorityEditPane createAuthorityEditPane() {
        ElementCasePaneAuthorityEditPane elementCasePaneAuthorityEditPane = new ElementCasePaneAuthorityEditPane(elementCasePane);
        elementCasePaneAuthorityEditPane.populateDetials();
        return elementCasePaneAuthorityEditPane;
    }
    
    /**
	 * 获取当前ElementCase的缩略图
	 * 
	 * @param size 缩略图的大小
	 */
    public BufferedImage getElementCaseImage(Dimension size){
	    BufferedImage image = null;
    	try{
	        image = new java.awt.image.BufferedImage(size.width, size.height, 
	        java.awt.image.BufferedImage.TYPE_INT_RGB);
	        Graphics g = image.getGraphics();
	        
	        //填充白色背景, 不然有黑框
			Color oldColor = g.getColor();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, size.width, size.height);
			g.setColor(oldColor);
	        
	        this.elementCasePane.paintComponents(g);
	        
    	}catch (Exception e) {
    		FRContext.getLogger().error(e.getMessage());
		}
    	
    	return image;
    }

    /**
     * 创建正在编辑的状态.
     *
     * @return 返回正在编辑的状态.
     */
    public EditingState createEditingState() {
        return this.elementCasePane.createEditingState();
    }

//////////////////////////////////////////////////////////////////////
//////////////////for toolbarMenuAdapter//////////////////////////////  
//////////////////////////////////////////////////////////////////////

    /**
     *  复制
     */
    public void copy() {
        this.elementCasePane.copy();
    }

    /**
     * 粘贴
     * @return   粘贴成功则返回true
     */
    public boolean paste() {
        return this.elementCasePane.paste();
    }

    /**
     * 剪切
     * @return   粘贴成功则返回true
     */
    public boolean cut() {
        return this.elementCasePane.cut();
    }

    /**
     * 停止编辑
     */
    public void stopEditing() {
        this.elementCasePane.stopEditing();
    }

    /**
     * 模板的工具
     *
     * @return 工具
     */
    public ToolBarDef[] toolbars4Target() {
        return this.elementCasePane.toolbars4Target();
    }

    /**
     * 表单的工具按钮
     *
     * @return 工具按钮
     */
    public JComponent[] toolBarButton4Form() {
        return this.elementCasePane.toolBarButton4Form();
    }

    /**
     * 目标的菜单
     *
     * @return 菜单
     */
    public MenuDef[] menus4Target() {
        return this.elementCasePane.menus4Target();
    }

    /**
     * 获取焦点
     */
    public void requestFocus() {
        super.requestFocus();
        elementCasePane.requestFocus();
    }

    public JScrollBar getHorizontalScrollBar() {
        return elementCasePane.getHorizontalScrollBar();
    }

    public JScrollBar getVerticalScrollBar() {
        return elementCasePane.getVerticalScrollBar();
    }

    public JPanel getEastUpPane() {
        return elementCasePane.getEastUpPane();
    }

    public JPanel getEastDownPane() {
        return elementCasePane.getEastDownPane();
    }

    public JPanel getConditionAttrPane() {
        ConditionAttributesGroupPane conditionAttributesGroupPane = ConditionAttributesGroupPane.getInstance();
        conditionAttributesGroupPane.populate(elementCasePane);
        return conditionAttributesGroupPane;
    }

    public JPanel getHyperlinkPane(JTemplate jt) {
        return jt.getHyperLinkPane(HyperlinkGroupPaneActionImpl.getInstance());
    }


    public S getSelection() {
        return (S) elementCasePane.getSelection();
    }

    public void setSelection(S selectElement) {
        if (selectElement == null) {
            selectElement = (S) new CellSelection();
        }
        this.elementCasePane.setSelection((Selection) selectElement);
    }

    /**
     * 移除选择
     */
    public void removeSelection() {
        TemplateElementCase templateElementCase = this.elementCasePane.getEditingElementCase();
        if (templateElementCase instanceof WorkSheet) {
            ((WorkSheet) templateElementCase).setPaintSelection(false);
        }
        elementCasePane.repaint();
    }

    public Selection getDefaultSelectElement() {
        TemplateElementCase tpc = this.elementCasePane.getEditingElementCase();
        CellElement cellElement = tpc.getCellElement(0, 0);
        return cellElement == null ? new CellSelection() : new CellSelection(0, 0, cellElement.getColumnSpan(), cellElement.getRowSpan());
    }

    /**
     * 添加选中的SelectionListener
     *
     * @param selectionListener 选中的listener
     */
    public void addSelectionChangeListener(SelectionListener selectionListener) {
        elementCasePane.addSelectionChangeListener(selectionListener);
    }

    /**
     * 移除选中的SelectionListener
     *
     * @param selectionListener 选中的listener
     */
    public void removeSelectionChangeListener(SelectionListener selectionListener) {
        elementCasePane.removeSelectionChangeListener(selectionListener);

    }

	@Override
	public ToolBarMenuDockPlus getToolBarMenuDockPlus() {
		return new JWorkBook();
	}

    /**
     * 无条件取消格式刷
     */
	public void cancelFormat() {
		return;
	}
	
	public FormElementCase getElementCase(){
		return (FormElementCase) this.getTarget();
	}

    /**
     * 模板的子菜单
     *
     * @return 子菜单
     */
	public ShortCut[] shortcut4TemplateMenu() {
		return new ShortCut[0];
	}

	public FormElementCaseProvider getEditingElementCase(){
		return this.getEditingElementCasePane().getTarget();
	}
}