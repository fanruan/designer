/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.form;

import com.fr.base.vcs.DesignerMode;
import com.fr.design.DesignState;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.AllowAuthorityEditAction;
import com.fr.design.actions.ExitAuthorityEditAction;
import com.fr.design.base.mode.DesignModeContext;
import com.fr.design.designer.DesignerProxy;
import com.fr.design.designer.EditingState;
import com.fr.design.designer.TargetComponent;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.gui.frpane.HyperlinkGroupPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.AuthorityEditPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.ElementCasePaneAuthorityEditPane;
import com.fr.design.mainframe.HyperlinkGroupPaneActionImpl;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.NameSeparator;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.present.ConditionAttributesGroupPane;
import com.fr.design.report.fit.menupane.FormAdaptiveConfigUI;
import com.fr.design.selection.SelectableElement;
import com.fr.design.selection.Selectedable;
import com.fr.design.selection.SelectionListener;
import com.fr.form.FormElementCaseProvider;
import com.fr.form.main.Form;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.log.FineLoggerFactory;
import com.fr.report.cell.CellElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.worksheet.FormElementCase;
import com.fr.report.worksheet.WorkSheet;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * 表单中的ElementCase编辑面板
 */
public class FormElementCaseDesigner
        <T extends FormElementCaseProvider, E extends ElementCasePane, S extends SelectableElement>
        extends TargetComponent<T>
        implements Selectedable<S>, FormECDesignerProvider, DesignerProxy {
    protected FormElementCasePaneDelegate elementCasePane;

    @Override
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

    @Override
    public int getMenuState() {
        return DesignState.WORK_SHEET;
    }

    /**
     * 权限细粒度情况下的子菜单
     *
     * @return 子菜单
     */
    @Override
    public ShortCut[] shortCuts4Authority() {
        return new ShortCut[]{
                new NameSeparator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Edit_DashBoard_Potence")),
                DesignerMode.isAuthorityEditing() ? new ExitAuthorityEditAction(this) : new AllowAuthorityEditAction(this),
        };

    }

    /**
     * 创建权限细粒度面板
     *
     * @return 返回权限细粒度面板
     */
    @Override
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
    @Override
    public BufferedImage getElementCaseImage(Dimension size) {
        FormAdaptiveConfigUI adaptiveConfigUI = ExtraDesignClassManager.getInstance().getSingle(FormAdaptiveConfigUI.MARK_STRING);
        if (adaptiveConfigUI != null) {
            return adaptiveConfigUI.getElementCaseImage(size, this.elementCasePane);
        }
        BufferedImage image = null;
        try {
            image = new java.awt.image.BufferedImage(size.width, size.height,
                    java.awt.image.BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();

            //填充白色背景, 不然有黑框
            Color oldColor = g.getColor();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, size.width, size.height);
            g.setColor(oldColor);

            this.elementCasePane.paintComponents(g);

        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }

        return image;
    }

    /**
     * 刷新右侧属性面板
     */
    @Override
    public void refreshPropertyPane() {
        this.elementCasePane.fireSelectionChangeListener();
    }

    /**
     * 创建正在编辑的状态.
     *
     * @return 返回正在编辑的状态.
     */
    @Override
    public EditingState createEditingState() {
        return this.elementCasePane.createEditingState();
    }

//////////////////////////////////////////////////////////////////////
//////////////////for toolbarMenuAdapter//////////////////////////////
//////////////////////////////////////////////////////////////////////

    /**
     * 复制
     */
    @Override
    public void copy() {
        DesignModeContext.doCopy(elementCasePane);
    }

    /**
     * 粘贴
     *
     * @return 粘贴成功则返回true
     */
    @Override
    public boolean paste() {
        return DesignModeContext.doPaste(elementCasePane);
    }

    /**
     * 剪切
     *
     * @return 粘贴成功则返回true
     */
    @Override
    public boolean cut() {
        return DesignModeContext.doCut(elementCasePane);
    }

    /**
     * 停止编辑
     */
    @Override
    public void stopEditing() {
        this.elementCasePane.stopEditing();
    }

    /**
     * 模板的工具
     *
     * @return 工具
     */
    @Override
    public ToolBarDef[] toolbars4Target() {
        return this.elementCasePane.toolbars4Target();
    }

    /**
     * 表单的工具按钮
     *
     * @return 工具按钮
     */
    @Override
    public JComponent[] toolBarButton4Form() {
        return this.elementCasePane.toolBarButton4Form();
    }

    /**
     * 目标的菜单
     *
     * @return 菜单
     */
    @Override
    public MenuDef[] menus4Target() {
        return this.elementCasePane.menus4Target();
    }

    /**
     * 获取焦点
     */
    @Override
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

    @Override
    public JPanel getEastUpPane() {
        return elementCasePane.getEastUpPane();
    }

    @Override
    public JPanel getEastDownPane() {
        return elementCasePane.getEastDownPane();
    }

    @Override
    public JPanel getConditionAttrPane() {
        ConditionAttributesGroupPane conditionAttributesGroupPane = ConditionAttributesGroupPane.getInstance();
        conditionAttributesGroupPane.populate(elementCasePane);
        return conditionAttributesGroupPane;
    }

    @Override
    public JPanel getHyperlinkPane(JTemplate jt) {
        HyperlinkGroupPane hyperlinkGroupPane = jt.getHyperLinkPane(HyperlinkGroupPaneActionImpl.getInstance());
        hyperlinkGroupPane.populate(elementCasePane);
        return hyperlinkGroupPane;
    }


    @Override
    public S getSelection() {
        return (S) elementCasePane.getSelection();
    }

    @Override
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
    @Override
    public void addSelectionChangeListener(SelectionListener selectionListener) {
        elementCasePane.addSelectionChangeListener(selectionListener);
    }

    /**
     * 移除选中的SelectionListener
     *
     * @param selectionListener 选中的listener
     */
    @Override
    public void removeSelectionChangeListener(SelectionListener selectionListener) {
        elementCasePane.removeSelectionChangeListener(selectionListener);

    }

    /**
     * 无条件取消格式刷
     */
    @Override
    public void cancelFormat() {
        return;
    }

    public FormElementCase getElementCase() {
        return (FormElementCase) this.getTarget();
    }

    /**
     * 模板的子菜单
     *
     * @return 子菜单
     */
    @Override
    public ShortCut[] shortcut4TemplateMenu() {
        return new ShortCut[0];
    }

    @Override
    public FormElementCaseProvider getEditingElementCase() {
        return this.getEditingElementCasePane().getTarget();
    }
}
