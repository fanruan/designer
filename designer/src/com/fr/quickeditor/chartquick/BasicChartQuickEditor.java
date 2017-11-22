package com.fr.quickeditor.chartquick;

import com.fr.base.chart.BaseChartCollection;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.actions.insert.cell.ChartCellAction;
import com.fr.design.gui.chart.BaseChartPropertyPane;
import com.fr.design.module.DesignModuleFactory;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.quickeditor.CellQuickEditor;
import com.fr.report.cell.Elem;

import javax.swing.*;


public class BasicChartQuickEditor extends CellQuickEditor {
    private BaseChartPropertyPane editingPropertyPane;

    public BasicChartQuickEditor() {
        super();
    }

    @Override
    public JComponent createCenterBody() {
        editingPropertyPane = DesignModuleFactory.getChartPropertyPane();
        editingPropertyPane.setBorder(BorderFactory.createEmptyBorder());
        return editingPropertyPane;
    }

    @Override
    public boolean isScrollAll() {
        return false;
    }

    @Override
    public Object getComboBoxSelected() {
        return ActionFactory.createAction(ChartCellAction.class);
    }

    @Override
    protected void refreshDetails() {
        BaseChartCollection collection;
        Selection selection = tc.getSelection();
        Elem element;
        CellSelection cs = (CellSelection) selection;
        element = tc.getEditingElementCase().getCellElement(cs.getColumn(), cs.getRow());
        collection = (BaseChartCollection) element.getValue();
        editingPropertyPane.populateChartPropertyPane(collection, tc);
    }

}