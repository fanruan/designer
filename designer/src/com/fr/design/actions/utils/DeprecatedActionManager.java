package com.fr.design.actions.utils;


import com.fr.base.BaseUtils;
import com.fr.base.present.DictPresent;
import com.fr.base.present.FormulaPresent;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.constants.UIConstants;
import com.fr.design.actions.UpdateAction;
import com.fr.design.actions.cell.NewPresentAction;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.actions.edit.clear.*;
import com.fr.design.actions.edit.order.BringFloatElementForwardAction;
import com.fr.design.actions.edit.order.BringFloatElementToFrontAction;
import com.fr.design.actions.edit.order.SendFloatElementBackwardAction;
import com.fr.design.actions.edit.order.SendFloatElementToBackAction;
import com.fr.design.actions.columnrow.DeleteColumnAction;
import com.fr.design.actions.columnrow.DeleteRowAction;
import com.fr.design.actions.columnrow.InsertColumnAction;
import com.fr.design.actions.columnrow.InsertRowAction;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.fun.PresentKindProvider;
import com.fr.design.gui.imenu.UIMenu;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuDef;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;
import com.fr.report.cell.cellattr.BarcodePresent;
import com.fr.report.cell.cellattr.CurrencyLinePresent;

import javax.swing.*;
import java.util.Set;

/**
 * peter: 这个方法只是临时放在这边的, 等插件系统全部完善后,立刻删除.
 */
public class DeprecatedActionManager {
    // TODO ALEX_SEP 这里的所有方法,都和TemplateComponent对应的ToolbarMenuAdapter有重复

    /**
     * Clear menu.
     *
     * @return the clearReportPage menu.
     */
    public static UIMenu getClearMenu(ElementCasePane ePane) {
        UIMenu clearMenu = new UIMenu(Inter.getLocText("M_Edit-Clear"));
        clearMenu.setIcon(BaseUtils.readIcon("/com/fr/design/images/control/clear.png"));
        clearMenu.setMnemonic('a');

        ClearAction ReportComponentAction = new ClearAllAction(ePane);
        clearMenu.add(ReportComponentAction.createMenuItem());

        ReportComponentAction = new ClearFormatsAction(ePane);
        clearMenu.add(ReportComponentAction.createMenuItem());
        ReportComponentAction = new ClearContentsAction(ePane);
        clearMenu.add(ReportComponentAction.createMenuItem());

        JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if (jTemplate.isJWorkBook()){  //表单中报表块编辑屏蔽掉  清除控件
            ReportComponentAction = new ClearWidgetAction(ePane);
            clearMenu.add(ReportComponentAction.createMenuItem());
        }
        return clearMenu;
    }

    /**
     * Cell Style.
     */
    public static MenuDef getCellMenu(final ElementCasePane ePane) {
        // 单元格菜单
        final MenuDef subMenuDef = new MenuDef(KeySetUtils.CELL_ELEMENT.getMenuName());
        subMenuDef.setIconPath("/com/fr/design/images/m_insert/cell.png");

        UpdateAction[] actions = ActionFactory.createCellInsertAction(ElementCasePane.class, ePane);
        for (UpdateAction action : actions) {
            subMenuDef.addShortCut(action);
        }

        ePane.addSelectionChangeListener(new SelectionListener() {

            @Override
            public void selectionChanged(SelectionEvent e) {
                Selection sel = ePane.getSelection();
                if (sel instanceof CellSelection) {
                    subMenuDef.setEnabled(true);
                } else {
                    subMenuDef.setEnabled(false);
                }
            }
        });
        return subMenuDef;
    }

    public static MenuDef getPresentMenu(final ElementCasePane ePane) {
        final MenuDef presentMenu = new MenuDef(KeySetUtils.PRESENT.getMenuKeySetName());
        presentMenu.setIconPath("com/fr/design/images/data/source/dataDictionary.png");
        presentMenu.setMnemonic(KeySetUtils.PRESENT.getMnemonic());
        NewPresentAction dataDictAction = new NewPresentAction(ePane, Inter.getLocText("M_Format-Data_Map"), DictPresent.class.getName());
        dataDictAction.setMnemonic('D');
        NewPresentAction formulaAction = new NewPresentAction(ePane, Inter.getLocText("Present-Formula_Present"), FormulaPresent.class.getName());
        formulaAction.setMnemonic('F');
        NewPresentAction barcodeAction = new NewPresentAction(ePane,  Inter.getLocText("M_Insert-Barcode"), BarcodePresent.class.getName());
        barcodeAction.setMnemonic('B');
        NewPresentAction currencyLineAction = new NewPresentAction(ePane, Inter.getLocText("Currency_Line"), CurrencyLinePresent.class.getName());
        currencyLineAction.setMnemonic('L');
        NewPresentAction nonePresentAction = new NewPresentAction(ePane, Inter.getLocText("Present-No_Present"), "NOPRESENT");
        nonePresentAction.setMnemonic('N');
        presentMenu.addShortCut(dataDictAction);
        presentMenu.addShortCut(formulaAction);
        presentMenu.addShortCut(barcodeAction);
        presentMenu.addShortCut(currencyLineAction);

        Set<PresentKindProvider> providers = ExtraDesignClassManager.getInstance().getArray(PresentKindProvider.MARK_STRING);
        for (PresentKindProvider provider : providers) {
            NewPresentAction action = new NewPresentAction(ePane, provider.title(), provider.kindOfPresent().getName());
            action.setMnemonic(provider.mnemonic());
            presentMenu.addShortCut(action);
        }
        presentMenu.addShortCut(nonePresentAction);

        ePane.addSelectionChangeListener(new SelectionListener() {

            @Override
            public void selectionChanged(SelectionEvent e) {
                Selection sel = ePane.getSelection();
                if (sel instanceof CellSelection) {
                    presentMenu.setEnabled(true);
                } else {
                    presentMenu.setEnabled(false);
                }
            }
        });

        return presentMenu;
    }

    public static UIMenu getDeleteMenu(ElementCasePane ePane) {
        UIMenu deleteMenu = new UIMenu(Inter.getLocText("M_Edit-Delete"));
        deleteMenu.setIcon(BaseUtils.readIcon("/com/fr/design/images/control/remove.png"));
        deleteMenu.setMnemonic('d');
        deleteMenu.add(new DeleteRowAction(ePane).createMenuItem());

        deleteMenu.add(new DeleteColumnAction(ePane).createMenuItem());

        return deleteMenu;
    }

    public static UIMenu getInsertMenu(ElementCasePane ePane) {
        UIMenu deleteMenu = new UIMenu(Inter.getLocText("Insert") + "(I)");
        deleteMenu.setIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/add.png"));
        deleteMenu.setMnemonic('i');
        deleteMenu.add(new InsertRowAction(ePane).createMenuItem());

        deleteMenu.add(new InsertColumnAction(ePane).createMenuItem());

        return deleteMenu;
    }
}