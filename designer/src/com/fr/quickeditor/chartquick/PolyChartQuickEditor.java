package com.fr.quickeditor.chartquick;

import com.fr.base.chart.BaseChartCollection;
import com.fr.design.designer.TargetComponent;
import com.fr.design.gui.chart.BaseChartPropertyPane;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.selection.QuickEditor;
import com.fr.poly.PolyDesigner;
import com.fr.poly.creator.ChartBlockEditor;

import java.awt.*;


public class PolyChartQuickEditor extends QuickEditor<TargetComponent> {
    public PolyChartQuickEditor() {
        setLayout(new BorderLayout());
        setBorder(null);
    }

    @Override
    protected void refresh() {
        BaseChartPropertyPane editingPropertyPane;
        BaseChartCollection collection;
        ChartBlockEditor chartBlockEditor = (ChartBlockEditor) ((PolyDesigner) tc).getSelection().getEditor();
        collection = chartBlockEditor.getValue().getChartCollection();
        add(editingPropertyPane = DesignModuleFactory.getChartPropertyPane(), BorderLayout.CENTER);
        editingPropertyPane.setSupportCellData(false);
        editingPropertyPane.populateChartPropertyPane(collection, tc);
    }

}