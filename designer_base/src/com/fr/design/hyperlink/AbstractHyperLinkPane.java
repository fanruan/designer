package com.fr.design.hyperlink;

import com.fr.base.chart.BasePlot;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.js.JavaScript;

/**
 * Created by mengao on 2017/10/12.
 */
public abstract class AbstractHyperLinkPane<T> extends FurtherBasicBeanPane<T> {
    private BasePlot plot;
    protected ReportletParameterViewPane parameterViewPane;


    public AbstractHyperLinkPane(BasePlot plot) {
        this.plot = plot;
    }

    public AbstractHyperLinkPane() {
    }

    public BasePlot getPlot() {
        return plot;
    }

    public ReportletParameterViewPane getParameterViewPane() {
        return parameterViewPane;
    }

    public void setParameterViewPane(ReportletParameterViewPane parameterViewPane) {
        this.parameterViewPane = parameterViewPane;
    }

    public boolean accept(Object ob){
        return ob instanceof JavaScript;
    }

    public void reset() {}

    protected int getChartParaType() {
        return plot != null ? ParameterTableModel.CHART_NORMAL_USE : ParameterTableModel.NO_CHART_USE;
    }

    protected ValueEditorPane getValueEditorPane() {
        return ValueEditorPaneFactory.createVallueEditorPaneWithUseType(getChartParaType(), plot);
    }

    protected boolean needRenamePane(){
        return plot != null && plot.isNeedRenameHyperLinkPane();
    }

}
