package com.fr.design.actions.cell;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.present.CellWriteAttrPane;
import com.fr.design.dialog.BasicPane;
import com.fr.form.ui.Widget;
import com.fr.general.FRLogger;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.privilege.finegrain.WidgetPrivilegeControl;
import com.fr.report.cell.TemplateCellElement;

/**
 * Cell Widget Attribute.
 */
public class CellWidgetAttrAction extends AbstractCellElementAction {

    public CellWidgetAttrAction(ElementCasePane t) {
        super(t);
        this.setMenuKeySet(KeySetUtils.CELL_WIDGET_ATTR);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/modified.png"));
    }

    @Override
    protected BasicPane populateBasicPane(TemplateCellElement cellElement) {
        CellWriteAttrPane pane = new CellWriteAttrPane(this.getEditingComponent());
        //got simple cell element from column and row.
        pane.populate(cellElement);

        return pane;
    }

    @Override
    protected void updateBasicPane(BasicPane bp, TemplateCellElement cellElement) {
        CellWriteAttrPane pane = (CellWriteAttrPane) bp;
        if (cellElement.getWidget() == null) {
            pane.update(cellElement);
            return;
        }
        try {
            Widget oldWidget = (Widget) cellElement.getWidget().clone();
            pane.update(cellElement);
            //这边需要重新设置权限细粒度的hashset是因为Update是直接生成一个新的来update的，所以以前里面的hashset都没有了
            Widget newWidget = cellElement.getWidget();
            if (newWidget.getClass() == oldWidget.getClass()) {
            	newWidget.setWidgetPrivilegeControl((WidgetPrivilegeControl) oldWidget.getWidgetPrivilegeControl().clone());
            }
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }
    }

    @Override
    protected boolean isNeedShinkToFit() {
        return true;
    }

    @Override
    public void update() {
        ElementCasePane ePane = this.getEditingComponent();
        Selection sel = ePane.getSelection();
        if (sel instanceof CellSelection) {
            this.setEnabled(true);
        } else {
            this.setEnabled(false);
        }
    }
}