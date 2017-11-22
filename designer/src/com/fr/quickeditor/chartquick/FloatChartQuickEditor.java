package com.fr.quickeditor.chartquick;

import com.fr.base.chart.BaseChartCollection;
import com.fr.design.gui.chart.BaseChartPropertyPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.selection.QuickEditor;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.Elem;

import java.awt.*;


public class FloatChartQuickEditor extends QuickEditor<ElementCasePane> {
    public FloatChartQuickEditor() {
        setLayout(new BorderLayout());
        setBorder(null);
    }

    @Override
    protected void refresh() {
        BaseChartPropertyPane editingPropertyPane;
        BaseChartCollection collection;
        Selection selection = tc.getSelection();
        Elem element;
        FloatSelection fs = (FloatSelection) selection;
        element = tc.getEditingElementCase().getFloatElement(fs.getSelectedFloatName());
        collection = (BaseChartCollection) element.getValue();
        add(editingPropertyPane = DesignModuleFactory.getChartPropertyPane(), BorderLayout.CENTER);
        editingPropertyPane.populateChartPropertyPane(collection, tc);
    }

}