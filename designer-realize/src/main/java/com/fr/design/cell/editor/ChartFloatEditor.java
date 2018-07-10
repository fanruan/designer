package com.fr.design.cell.editor;

import java.awt.Component;

import javax.swing.SwingUtilities;

import com.fr.base.chart.BaseChartCollection;
import com.fr.design.gui.chart.MiddleChartComponent;
import com.fr.design.gui.chart.MiddleChartDialog;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.dialog.DialogActionListener;
import com.fr.grid.Grid;
import com.fr.report.cell.FloatElement;
import com.fr.stable.StringUtils;
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.core.PropertyChangeAdapter;

public class ChartFloatEditor extends AbstractFloatEditor implements DialogActionListener {

    private MiddleChartDialog chartFloatEditorDialog = null;
    private MiddleChartComponent glyphComponent = null;

    /**
     * Constructor
     */
    public ChartFloatEditor() {
    }

    /**
     * Return the value of the FloatEditor
     */
    @Override
    public Object getFloatEditorValue() throws Exception {
        if (this.glyphComponent != null) {
        	MiddleChartComponent newComponent = this.glyphComponent;
            return newComponent.update();
        }

        BaseChartCollection cc = this.chartFloatEditorDialog.getChartCollection();
        // 判断条件以及返回值
        if (cc != null) {
            return cc;
        } else {
            return StringUtils.EMPTY;
        }
    }

    @Override
    public Component getFloatEditorComponent(final Grid grid, FloatElement floatElement, int resolution) {
        Object valueCell = floatElement.getValue();
        if (valueCell instanceof BaseChartCollection) {
            this.chartFloatEditorDialog = null;
            if (glyphComponent == null) {
            	glyphComponent = DesignModuleFactory.getChartComponent((BaseChartCollection)valueCell);
            	glyphComponent.addStopEditingListener(new PropertyChangeAdapter() {
                    @Override
                    public void propertyChange() {
                        stopFloatEditing();
                        grid.requestFocus();// kunsnat: 补充, 编辑图表reset之后, Grid也算停止编辑 重获焦点 bug20443
                    }
                });
            	
            } else {
                glyphComponent.populate((BaseChartCollection) valueCell);
            }
            return glyphComponent;
        }
        this.chartFloatEditorDialog = DesignModuleFactory.getChartDialog(SwingUtilities.getWindowAncestor(grid));
        this.chartFloatEditorDialog.addDialogActionListener(this);
        BaseChartCollection cc = (BaseChartCollection)StableFactory.createXmlObject(BaseChartCollection.XML_TAG);
        this.chartFloatEditorDialog.populate(cc);
        this.glyphComponent = null;
        return this.chartFloatEditorDialog;
    }

    @Override
    public void doOk() {
        stopFloatEditing();
    }

    @Override
    public void doCancel() {
        cancelFloatEditing();
    }
}