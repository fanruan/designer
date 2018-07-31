/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.creator;

import com.fr.base.BaseUtils;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.actions.edit.HyperlinkAction;
import com.fr.design.menu.KeySetUtils;

import com.fr.page.ReportSettingsProvider;
import com.fr.design.actions.UpdateAction;
import com.fr.design.actions.cell.*;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.actions.edit.merge.MergeCellAction;
import com.fr.design.actions.edit.merge.UnmergeCellAction;
import com.fr.design.actions.columnrow.InsertColumnAction;
import com.fr.design.actions.columnrow.InsertRowAction;
import com.fr.design.actions.utils.DeprecatedActionManager;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.AuthorityPropertyPane;
import com.fr.design.mainframe.CellElementPropertyPane;
import com.fr.design.mainframe.EastRegionContainerPane;
import com.fr.design.DesignState;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.SeparatorDef;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.poly.PolyDesigner;
import com.fr.report.poly.PolyECBlock;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;
import com.fr.stable.ArrayUtils;
import com.fr.design.mainframe.NoSupportAuthorityEdit;

import java.awt.*;

/**
 * @author richer
 * @since 6.5.4 创建于2011-4-2 聚合报表组件编辑器
 */
public class ECBlockPane extends PolyElementCasePane {
    private PolyDesigner designer;
    private BlockEditor be;

    public ECBlockPane(final PolyDesigner designer, PolyECBlock block, BlockEditor be) {
        super(block);
        this.designer = designer;
        this.be = be;
        this.setTarget(block);
        this.addSelectionChangeListener(new SelectionListener() {

            @Override
            public void selectionChanged(SelectionEvent e) {
                if (!isEditable()) {
                    return;
                }
                if (DesignerMode.isAuthorityEditing()) {
                    if (designer.getSelection().getEditingElementCasePane() == null) {
                        EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.AUTHORITY_EDITION_DISABLED);
                        EastRegionContainerPane.getInstance().replaceAuthorityEditionPane(new NoSupportAuthorityEdit());
                        HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().setAuthorityMode(false);
                        return;
                    }
                    AuthorityPropertyPane authorityPropertyPane = new AuthorityPropertyPane(designer);
                    authorityPropertyPane.populate();
                    EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.AUTHORITY_EDITION);
                    EastRegionContainerPane.getInstance().replaceAuthorityEditionPane(authorityPropertyPane);
                    return;
                }

                ECBlockPane.this.be.resetSelectionAndChooseState();
            }
        });
        this.addTargetModifiedListener(new TargetModifiedListener() {

            @Override
            public void targetModified(TargetModifiedEvent e) {
                // kunsnat: 没有找到相关作用,bug 35286 在图表聚合触发重新populate, 导致界面又回到第一层. 故屏蔽.
//				ECBlockPane.this.be.resetSelectionAndChooseState();
            	// bug65880
            	// 聚合报表单元格设置拓展的时候没有触发，普通报表有触发
            	CellElementPropertyPane.getInstance().populate(ECBlockPane.this);
            }
        });
    }

    @Override
    public void setTarget(PolyECBlock block) {
        super.setTarget(block);

        be.creator.setValue(block);
        // this.be.initSize();
        // ComponentUtils.layoutContainer(be);
    }

    public Dimension getCornerSize() {
        int h = getGridColumn().getPreferredSize().height;
        int w = getGridRow().getPreferredSize().width;
        return new Dimension(w, h);
    }

    /**
     * 目标的Menu
     *
     * @return 返回MenuDef数组.
     */
    public MenuDef[] menus4Target() {
        if (DesignerMode.isAuthorityEditing()) {
            return super.menus4Target();
        }
        return (MenuDef[]) ArrayUtils.addAll(super.menus4Target(), new MenuDef[]{createInsertMenuDef(), createCellMenuDef()});
    }

    public int getMenuState() {
        return DesignState.WORK_SHEET;
    }


    // 插入菜单
    private MenuDef createInsertMenuDef() {
        MenuDef menuDef = new MenuDef(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_M-Insert"), 'I');
        // 单元格菜单
        menuDef.addShortCut(DeprecatedActionManager.getCellMenu(this));

        addInsertFloatMenuDef(menuDef);
        menuDef.addShortCut(SeparatorDef.DEFAULT);
        menuDef.addShortCut(new InsertRowAction(this));
        menuDef.addShortCut(new InsertColumnAction(this));
        return menuDef;
    }

    private void addInsertFloatMenuDef(MenuDef menuDef) {
        // 悬浮元素菜单
        MenuDef subMenuDef = new MenuDef(KeySetUtils.INSERT_FLOAT.getMenuKeySetName());
        subMenuDef.setIconPath("/com/fr/design/images/m_insert/float.png");
        menuDef.addShortCut(subMenuDef);
        UpdateAction[] actions = ActionFactory.createFloatInsertAction(ElementCasePane.class, this);
        for (int i = 0; i < actions.length; i++) {
            subMenuDef.addShortCut(actions[i]);
        }
    }

    // 格式菜单
    private MenuDef createCellMenuDef() {
        MenuDef menuDef = new MenuDef(KeySetUtils.CELL.getMenuKeySetName(), KeySetUtils.CELL.getMnemonic());

        menuDef.addShortCut(new CellExpandAttrAction());
        menuDef.addShortCut(new CellWidgetAttrAction());
        menuDef.addShortCut(new GlobalStyleMenuDef(this));
        menuDef.addShortCut(new ConditionAttributesAction());

        // 单元格形态
        menuDef.addShortCut(DeprecatedActionManager.getPresentMenu(this));

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
        return designer.getTemplateReport().getReportSettings();
    }

}