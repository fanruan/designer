package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.actions.edit.HyperlinkAction;
import com.fr.design.fun.MenuHandler;
import com.fr.design.gui.frpane.HyperlinkGroupPane;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.present.ConditionAttributesGroupPane;
import com.fr.general.Inter;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.page.ReportSettingsProvider;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.DesignState;
import com.fr.design.actions.UpdateAction;
import com.fr.design.actions.cell.*;
import com.fr.design.actions.columnrow.InsertColumnAction;
import com.fr.design.actions.columnrow.InsertRowAction;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.actions.edit.merge.MergeCellAction;
import com.fr.design.actions.edit.merge.UnmergeCellAction;
import com.fr.design.actions.utils.DeprecatedActionManager;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.mainframe.cell.QuickEditorRegion;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.SeparatorDef;
import com.fr.report.worksheet.WorkSheet;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;
import com.fr.stable.ArrayUtils;

import javax.swing.*;
import java.awt.*;

/**
 * 类说明: 设计面板中最上方的"插入" "单元格"下拉列表Menu模块.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-6-25 上午09:56:57
 */
public class ElementCasePaneDelegate extends ElementCasePane<WorkSheet> {

    public ElementCasePaneDelegate(WorkSheet sheet) {
        super(sheet);
        this.addSelectionChangeListener(new SelectionListener() {
            @Override
            public void selectionChanged(SelectionEvent e) {
                if (!isEditable() && !BaseUtils.isAuthorityEditing()) {
                    return;
                }
                refreshPropertyPanes();
            }
        });
        this.addTargetModifiedListener(new TargetModifiedListener() {
            @Override
            public void targetModified(TargetModifiedEvent e) {
                if (DesignerContext.isRefreshOnTargetModifiedEnabled()) {
                    refreshPropertyPanes();
                } else {
                    CellElementPropertyPane.getInstance().populate(ElementCasePaneDelegate.this);
                }
            }
        });
    }

    private void refreshPropertyPanes() {
        //在编辑权限，所以要更新权限编辑面板
        if (BaseUtils.isAuthorityEditing()) {
            AuthorityPropertyPane authorityPropertyPane = new AuthorityPropertyPane(ElementCasePaneDelegate.this);
            authorityPropertyPane.populate();
            EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.AUTHORITY_EDITION);
            EastRegionContainerPane.getInstance().replaceAuthorityEditionPane(authorityPropertyPane);
            EastRegionContainerPane.getInstance().replaceConfiguredRolesPane(RolesAlreadyEditedPane.getInstance());
            return;
        }
        if (DesignerContext.getDesignerFrame().getSelectedJTemplate() != null) {
            CellWidgetPropertyPane.getInstance().populate(ElementCasePaneDelegate.this);
        }
        CellElementPropertyPane.getInstance().populate(ElementCasePaneDelegate.this);
        QuickEditorRegion.getInstance().populate(getCurrentEditor());
        JTemplate editingTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if (editingTemplate != null && !editingTemplate.isUpMode()) {
            Selection editingSelection = getSelection();
            // 模板初始化完成后，才能初始化超级链接面板
            HyperlinkGroupPane hyperlinkGroupPane = editingTemplate.getHyperLinkPane(HyperlinkGroupPaneActionImpl.getInstance());
            hyperlinkGroupPane.populate(ElementCasePaneDelegate.this);
            if (editingSelection instanceof FloatSelection) {
                EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.REPORT_FLOAT);
                JPanel floatPane = new JPanel(new BorderLayout());
                floatPane.add(ReportFloatPane.getInstance(), BorderLayout.NORTH);
                floatPane.add(QuickEditorRegion.getInstance(), BorderLayout.CENTER);
                EastRegionContainerPane.getInstance().replaceFloatElementPane(floatPane);
            } else {
                // 条件属性
                ConditionAttributesGroupPane conditionAttributesGroupPane = ConditionAttributesGroupPane.getInstance();
                conditionAttributesGroupPane.populate(ElementCasePaneDelegate.this);

                EastRegionContainerPane.getInstance().replaceFloatElementPane(ReportFloatPane.getInstance());
                EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.REPORT);
                EastRegionContainerPane.getInstance().replaceCellAttrPane(CellElementPropertyPane.getInstance());
                EastRegionContainerPane.getInstance().replaceCellElementPane(QuickEditorRegion.getInstance());
                EastRegionContainerPane.getInstance().replaceConditionAttrPane(conditionAttributesGroupPane);
                EastRegionContainerPane.getInstance().replaceWidgetSettingsPane(CellWidgetPropertyPane.getInstance());

                EastRegionContainerPane.getInstance().updateCellElementState(isSelectedOneCell());
            }
            EastRegionContainerPane.getInstance().replaceHyperlinkPane(hyperlinkGroupPane);
            EastRegionContainerPane.getInstance().removeParameterPane();
        }
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
        if (BaseUtils.isAuthorityEditing()) {
            return super.menus4Target();
        }
        return (MenuDef[]) ArrayUtils.addAll(super.menus4Target(), new MenuDef[]{createInsertMenuDef(), createCellMenuDef()});
    }

    public int getMenuState() {
        return DesignState.WORK_SHEET;
    }

    /**
     * 是否必须为可见范围.
     *
     * @return 不是必须在可见范围.
     */
    @Override
    public boolean mustInVisibleRange() {
        return false;
    }

    // 插入菜单
    private MenuDef createInsertMenuDef() {
        MenuDef menuDef = new MenuDef(Inter.getLocText("FR-Designer_M-Insert"), 'I');
        menuDef.setAnchor(MenuHandler.INSERT);
        // 单元格菜单
        menuDef.addShortCut(DeprecatedActionManager.getCellMenu(this));

        addInsertFloatMenuDef(menuDef);
        menuDef.addShortCut(SeparatorDef.DEFAULT);
        menuDef.addShortCut(new InsertRowAction(this));
        menuDef.addShortCut(new InsertColumnAction(this));
        return menuDef;
    }


    public JPanel getEastUpPane() {
        QuickEditorRegion.getInstance().populate(getCurrentEditor());
        return QuickEditorRegion.getInstance();
    }


    public JPanel getEastDownPane() {
        CellElementPropertyPane.getInstance().populate(ElementCasePaneDelegate.this);
        return CellElementPropertyPane.getInstance();
    }


    private void addInsertFloatMenuDef(MenuDef menuDef) {
        // 悬浮元素菜单
        MenuDef subMenuDef = new MenuDef(KeySetUtils.INSERT_FLOAT.getMenuName());
        subMenuDef.setIconPath("/com/fr/design/images/m_insert/float.png");
        menuDef.addShortCut(subMenuDef);
        UpdateAction[] actions = ActionFactory.createFloatInsertAction(ElementCasePane.class, this);
        for (int i = 0; i < actions.length; i++) {
            subMenuDef.addShortCut(actions[i]);
        }

        // 顺便更新右侧属性面板悬浮元素tab中的菜单项
        ReportFloatPane.getInstance().refreshInsertFloatMenu(this);
    }

    // 格式菜单
    private MenuDef createCellMenuDef() {
        MenuDef menuDef = new MenuDef(KeySetUtils.CELL.getMenuKeySetName(), KeySetUtils.CELL.getMnemonic());

        menuDef.addShortCut(new CellExpandAttrAction());
        menuDef.addShortCut(new GlobalStyleMenuDef(this));
        // 单元格形态
        menuDef.addShortCut(DeprecatedActionManager.getPresentMenu(this));
        menuDef.addShortCut(new CellWidgetAttrAction());
        menuDef.addShortCut(new ConditionAttributesAction());

        menuDef.addShortCut(new HyperlinkAction());
        menuDef.addShortCut(SeparatorDef.DEFAULT);
        menuDef.addShortCut(new MergeCellAction(this));
        menuDef.addShortCut(new UnmergeCellAction(this));
        menuDef.addShortCut(SeparatorDef.DEFAULT);
        menuDef.addShortCut(new CellAttributeAction());
        return menuDef;
    }

    @Override
    public ReportSettingsProvider getReportSettings() {
        return this.getTarget().getReportSettings();
    }

}