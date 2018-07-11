package com.fr.design.cell.editor;

import java.awt.Component;

import javax.swing.SwingUtilities;

import com.fr.base.chart.BaseChartCollection;
import com.fr.design.gui.chart.MiddleChartComponent;
import com.fr.design.gui.chart.MiddleChartDialog;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.dialog.DialogActionListener;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.Grid;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.core.PropertyChangeAdapter;

/**
 *  CellEditor used to edit Chart object.
 */
public class ChartCellEditor extends AbstractCellEditor implements DialogActionListener {

    public ChartCellEditor(ElementCasePane<? extends TemplateElementCase> ePane) {
		super(ePane);
	}

	private MiddleChartDialog chartCellEditorDialog = null;
    private MiddleChartComponent glyphComponent = null;
    protected ElementCasePane<? extends TemplateElementCase> ePane;


    /**
     * Return the value of the CellEditor
     */
    @Override
    public Object getCellEditorValue() throws Exception {
        if (this.glyphComponent != null) {
        	MiddleChartComponent newComponent = this.glyphComponent;
            return newComponent.update();
        }

        BaseChartCollection cc = this.chartCellEditorDialog.getChartCollection();
        // 判断条件以及返回值
        if (cc != null) {
            return cc;
        } else {
            return "";
        }
    }
    
    public Component getCellEditorComponent(final Grid grid, TemplateCellElement cellElement, int resolution) {
        Object valueCell = cellElement.getValue();
        if (valueCell instanceof BaseChartCollection) {
            if (glyphComponent == null) {
            	glyphComponent = DesignModuleFactory.getChartComponent((BaseChartCollection)valueCell);
            	glyphComponent.addStopEditingListener(new PropertyChangeAdapter() {
                    @Override
                    public void propertyChange() {
                        stopCellEditing();
                        grid.requestFocus();// kunsnat: 补充, 编辑图表reset之后, Grid也算停止编辑 重获焦点 bug20443SS
                    }
                });
            } else {
                glyphComponent.populate((BaseChartCollection) valueCell);
            }
            this.chartCellEditorDialog = null;
            return glyphComponent;
        }
        this.chartCellEditorDialog = DesignModuleFactory.getChartDialog(SwingUtilities.getWindowAncestor(grid));
        this.chartCellEditorDialog.addDialogActionListener(this);
        BaseChartCollection cc = (BaseChartCollection)StableFactory.createXmlObject(BaseChartCollection.XML_TAG);
        this.chartCellEditorDialog.populate(cc);
        this.glyphComponent = null;
        return this.chartCellEditorDialog;
    }

	@Override
	public void doOk() {
		stopCellEditing();
	}

	@Override
	public void doCancel() {
		cancelCellEditing();
	}
}