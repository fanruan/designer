package com.fr.quickeditor;

import com.fr.base.chart.BaseChartCollection;
//import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.designer.TargetComponent;
import com.fr.design.gui.chart.BaseChartPropertyPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.selection.QuickEditor;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.poly.PolyDesigner;
import com.fr.poly.creator.ChartBlockEditor;
import com.fr.report.cell.Elem;

import java.awt.*;

public class ChartQuickEditor extends QuickEditor<TargetComponent>{
    // kunsnat: editingPropertyPane初始化  避开设计器启动, 在用到的时候再初始化.
	//private BaseChartPropertyPane editingPropertyPane = null;
	public ChartQuickEditor() {
		setLayout(new BorderLayout());
		setBorder(null);
	}

	@Override
	protected void refresh() {
		BaseChartPropertyPane editingPropertyPane = null;
		BaseChartCollection collection = null;
		if(tc instanceof PolyDesigner) {
			ChartBlockEditor chartBlockEditor = (ChartBlockEditor)((PolyDesigner)tc).getSelection().getEditor();
			collection = chartBlockEditor.getValue().getChartCollection();
			
			add(editingPropertyPane = DesignModuleFactory.getChartPropertyPane(), BorderLayout.CENTER);
			editingPropertyPane.setSupportCellData(false);
		} else {
			Selection selection = ((ElementCasePane)tc).getSelection();
			Elem element = null;
			if(selection instanceof CellSelection) {
				CellSelection cs = (CellSelection)selection;
				element = ((ElementCasePane)tc).getEditingElementCase().getCellElement(cs.getColumn(), cs.getRow());
			} else if(selection instanceof FloatSelection){
				FloatSelection fs = (FloatSelection)selection;
				element = ((ElementCasePane)tc).getEditingElementCase().getFloatElement(fs.getSelectedFloatName());
			}
			collection = (BaseChartCollection) element.getValue();
			add(editingPropertyPane = DesignModuleFactory.getChartPropertyPane(), BorderLayout.CENTER);

		}
		editingPropertyPane.populateChartPropertyPane(collection, tc);
	}

}