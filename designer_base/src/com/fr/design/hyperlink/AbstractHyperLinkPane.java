package com.fr.design.hyperlink;

import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.js.JavaScript;

import java.util.HashMap;

/**
 * Created by mengao on 2017/10/12.
 */
public abstract class AbstractHyperLinkPane<T> extends FurtherBasicBeanPane<T> {
    private HashMap hyperLinkEditorMap;
    private boolean needRenamePane = false;
    protected ReportletParameterViewPane parameterViewPane;


    public AbstractHyperLinkPane(HashMap hyperLinkEditorMap, boolean needRenamePane) {
        this.hyperLinkEditorMap = hyperLinkEditorMap;
        this.needRenamePane = needRenamePane;
    }

    public AbstractHyperLinkPane() {
    }

    public ReportletParameterViewPane getParameterViewPane() {
        return parameterViewPane;
    }

    public void setParameterViewPane(ReportletParameterViewPane parameterViewPane) {
        this.parameterViewPane = parameterViewPane;
    }

    public boolean accept(Object ob) {
        return ob instanceof JavaScript;
    }

    public void reset() {
    }

    protected int getChartParaType() {
        return hyperLinkEditorMap != null ? ParameterTableModel.CHART_NORMAL_USE : ParameterTableModel.NO_CHART_USE;
    }

    protected ValueEditorPane getValueEditorPane() {
        return ValueEditorPaneFactory.createVallueEditorPaneWithUseType(getChartParaType(), hyperLinkEditorMap);
    }

    protected boolean needRenamePane() {
        return needRenamePane;
    }

}
