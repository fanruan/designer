package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.base.Parameter;
import com.fr.base.chart.BasePlot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.ElementCaseEditorProvider;
import com.fr.general.Inter;
import com.fr.js.FormHyperlinkProvider;
import com.fr.stable.ParameterProvider;
import com.fr.stable.bridge.StableFactory;

import javax.swing.*;
import java.awt.*;

public class FormHyperlinkPane extends BasicBeanPane<FormHyperlinkProvider> {
    private BasePlot plot;

    private ReportletParameterViewPane parameterViewPane;
    private FormHyperlinkNorthPane northPane;

    protected BasePlot getPlot() {
        return plot;
    }

    public FormHyperlinkPane(BasePlot plot) {
        super();
        this.plot = plot;
        this.initComponents();
    }

    public FormHyperlinkPane() {
        super();
        this.initComponents();
    }

    protected void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        northPane = new FormHyperlinkNorthPane(needRenamePane());
        this.add(northPane, BorderLayout.NORTH);

        parameterViewPane = new ReportletParameterViewPane(getChartParaType(), getValueEditorPane(), getValueEditorPane());
        this.add(parameterViewPane, BorderLayout.CENTER);
        parameterViewPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Designer_Parameters"), null));
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("Hyperlink-Form_link");
    }

    protected int getChartParaType() {
        return plot != null ? ParameterTableModel.CHART_NORMAL_USE : ParameterTableModel.NO_CHART_USE;
    }

    protected ValueEditorPane getValueEditorPane() {
        return ValueEditorPaneFactory.createVallueEditorPaneWithUseType(getChartParaType(), plot);
    }

    protected boolean needRenamePane(){
        return plot != null && plot.isNeedRenameHyperLinkPane();
    }
    
    protected int getHyperlinkType() {
    	if (northPane.getEditingEditor() != null){
    		if (northPane.getEditingEditor().acceptType(ElementCaseEditorProvider.class)) {
    			return FormHyperlinkProvider.ELEMENTCASE;
    		}
    	}
    	return FormHyperlinkProvider.CHART;
    }

    @Override
    public void populateBean(FormHyperlinkProvider formHyperlink) {
        northPane.populateBean(formHyperlink);
        //parameter
        java.util.List<ParameterProvider> parameterList = this.parameterViewPane.update();
        parameterList.clear();

        ParameterProvider[] parameters = formHyperlink.getParameters();
        parameterViewPane.populate(parameters);
    }

    @Override
    public FormHyperlinkProvider updateBean() {
    	FormHyperlinkProvider formHyperlink = StableFactory.getMarkedInstanceObjectFromClass(FormHyperlinkProvider.XML_TAG, FormHyperlinkProvider.class);
        formHyperlink.setType(getHyperlinkType());
        updateBean(formHyperlink);

        return formHyperlink;
    }

    public void updateBean(FormHyperlinkProvider formHyperlink) {
    	formHyperlink.setType(getHyperlinkType());
    	
        northPane.updateBean(formHyperlink);
        //Parameter.
        java.util.List<ParameterProvider> parameterList = this.parameterViewPane.update();
        if (!parameterList.isEmpty()) {
            Parameter[] parameters = new Parameter[parameterList.size()];
            parameterList.toArray(parameters);

            formHyperlink.setParameters(parameters);
        } else {
            formHyperlink.setParameters(null);
        }
    }

    public static class CHART_NO_RENAME extends FormHyperlinkPane{
        protected boolean needRenamePane(){
            return false;
        }
        protected int getChartParaType() {
            return ParameterTableModel.CHART_NORMAL_USE;
        }
    }
}