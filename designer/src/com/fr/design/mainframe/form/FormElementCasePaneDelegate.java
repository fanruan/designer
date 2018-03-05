package com.fr.design.mainframe.form;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.fr.design.DesignState;
import com.fr.design.actions.UpdateAction;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.actions.form.FormECBackgroundAction;
import com.fr.design.actions.form.FormECColumnsAction;
import com.fr.design.actions.form.FormECFrozenAction;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.frpane.HyperlinkGroupPane;
import com.fr.design.mainframe.*;
import com.fr.design.mainframe.cell.QuickEditorRegion;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.present.ConditionAttributesGroupPane;
import com.fr.form.main.Form;
import com.fr.js.NameJavaScriptGroup;
import com.fr.page.ReportSettingsProvider;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.worksheet.FormElementCase;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;

/**
 */
public class FormElementCasePaneDelegate extends ElementCasePane<FormElementCase>{

	public FormElementCasePaneDelegate(FormElementCase sheet, Form form) {
        super(sheet);
        this.getGrid().setShowPaginateLine(form.getFormMobileAttr().isMobileOnly());
        this.addSelectionChangeListener(new SelectionListener() {
            @Override
            public void selectionChanged(SelectionEvent e) {
                refreshPropertyPanes();
            }
        });
        this.addTargetModifiedListener(new TargetModifiedListener() {
            @Override
            public void targetModified(TargetModifiedEvent e) {
                if (DesignerContext.isRefreshOnTargetModifiedEnabled()) {
                    refreshPropertyPanes();
                } else {
                    CellElementPropertyPane.getInstance().populate(FormElementCasePaneDelegate.this);
                }
            }
        });
    }

    private void refreshPropertyPanes() {
        CellElementPropertyPane.getInstance().populate(FormElementCasePaneDelegate.this);
        QuickEditorRegion.getInstance().populate(getCurrentEditor());
        // 超级链接
        HyperlinkGroupPane hyperlinkGroupPane = DesignerContext.getDesignerFrame().getSelectedJTemplate().
                getHyperLinkPane(HyperlinkGroupPaneActionImpl.getInstance());
        hyperlinkGroupPane.populate(FormElementCasePaneDelegate.this);
        // 条件属性
        ConditionAttributesGroupPane conditionAttributesGroupPane = ConditionAttributesGroupPane.getInstance();
        conditionAttributesGroupPane.populate(FormElementCasePaneDelegate.this);

        EastRegionContainerPane.getInstance().updateCellElementState(isSelectedOneCell());
    }

    @Override
    protected boolean supportRepeatedHeaderFooter() {
        return true;
    }

    /**
     * 选中目标的 对应Menu
     *
     * @return 返回MenuDef数组.
     */
    public MenuDef[] menus4Target() {
        return new MenuDef[0];
    }

    /**
     * 报表块工具栏 快捷键菜单按钮 包括分页冻结
     *
     * @return 返回工具栏数组.
     */
    public ToolBarDef[] toolbars4Target() {
        return new ToolBarDef[]{
                createFontToolBar(),
                createAlignmentToolBar(),
                createStyleToolBar(),
                createCellToolBar(),
                createInsertToolBar(),
                createFrozenColumnsToolBar()};
    }

    public int getMenuState() {
        return DesignState.WORK_SHEET;
    }

    /**
     * 表单的报表块的工具按钮
     * @return 工具按钮
     */
    public JComponent[] toolBarButton4Form() {
        return new JComponent[]{
                formatBrush };
    }

    /**
     * 是否必须为可见范围.
     *
     * @return 不是必须在可见范围.
     */
    public boolean mustInVisibleRange() {
        return false;
    }

    /**
     * 模板菜单
     * @return   模板菜单
     */
    public MenuDef createTemplateShortCuts() {
        MenuDef menuDef = new MenuDef(KeySetUtils.TEMPLATE.getMenuKeySetName(), KeySetUtils.TEMPLATE.getMnemonic());
        menuDef.addShortCut(new FormECBackgroundAction(this));
        menuDef.addShortCut(new FormECFrozenAction(this));
        menuDef.addShortCut(new FormECColumnsAction(this));
        return menuDef;
    }

    protected ToolBarDef createInsertToolBar() {
        UpdateAction[] cellInsertActions = ActionFactory.createCellInsertAction(ElementCasePane.class, this);
        ShortCut[] shortCuts = new ShortCut[cellInsertActions.length];
        System.arraycopy(cellInsertActions, 0, shortCuts, 0, cellInsertActions.length);
        return ShortCut.asToolBarDef(shortCuts);
    }

    private ToolBarDef createFrozenColumnsToolBar() {
       return ShortCut.asToolBarDef(new ShortCut[]{
               new FormECFrozenAction(this),
               new FormECColumnsAction(this),});
    }



    public JPanel getEastUpPane() {
        QuickEditorRegion.getInstance().populate(getCurrentEditor());
        return QuickEditorRegion.getInstance();
    }


    public JPanel getEastDownPane() {
        CellElementPropertyPane.getInstance().populate(FormElementCasePaneDelegate.this);
        return CellElementPropertyPane.getInstance();
    }


    @Override
    public ReportSettingsProvider getReportSettings() {
        return this.getTarget().getReportSettings();
    }

}