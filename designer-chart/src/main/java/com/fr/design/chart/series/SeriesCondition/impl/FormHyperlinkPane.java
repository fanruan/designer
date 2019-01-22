package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.base.Parameter;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.hyperlink.AbstractHyperLinkPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.ElementCaseEditorProvider;
import com.fr.js.FormHyperlinkProvider;
import com.fr.stable.ParameterProvider;
import com.fr.stable.bridge.StableFactory;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.HashMap;

public class FormHyperlinkPane extends AbstractHyperLinkPane<FormHyperlinkProvider> {
    private static final int BORDER_WIDTH = 4;
    private FormHyperlinkNorthPane northPane;


    public FormHyperlinkPane(HashMap hyperLinkEditorMap, boolean needRenamePane) {
        super(hyperLinkEditorMap, needRenamePane);
        this.initComponents();
    }

    public FormHyperlinkPane() {
        super();
        this.initComponents();
    }

    protected void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH));
        northPane = new FormHyperlinkNorthPane(needRenamePane());
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(northPane, BorderLayout.NORTH);
        if (needAnimatePane()) {
            JPanel animatePane = createAnimateTypeUIButtonGroup();
            animatePane.setBorder(BorderFactory.createEmptyBorder(0, 8, 10, 10));
            panel.add(animatePane, BorderLayout.CENTER);
        }

        this.add(panel, BorderLayout.NORTH);

        parameterViewPane = new ReportletParameterViewPane(getChartParaType(), getValueEditorPane(), getValueEditorPane());
        this.add(parameterViewPane, BorderLayout.CENTER);
        parameterViewPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameters"), null));
    }

    @Override
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Hyperlink_Form_Link");
    }

    protected int getHyperlinkType() {
        if (northPane.getEditingEditor() != null && northPane.getEditingEditor().acceptType(ElementCaseEditorProvider.class)) {
            return FormHyperlinkProvider.ELEMENTCASE;
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
        populateAnimateType(formHyperlink.getAnimateType());
    }

    @Override
    public FormHyperlinkProvider updateBean() {
        FormHyperlinkProvider formHyperlink = StableFactory.getMarkedInstanceObjectFromClass(FormHyperlinkProvider.XML_TAG, FormHyperlinkProvider.class);
        formHyperlink.setType(getHyperlinkType());
        updateBean(formHyperlink);

        formHyperlink.setAnimateType(updateAnimateType());

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

    public static class ChartNoRename extends FormHyperlinkPane {
        public ChartNoRename(HashMap hyperLinkEditorMap, boolean needRenamePane) {
            super(hyperLinkEditorMap, needRenamePane);
        }

        public ChartNoRename() {
        }

        protected boolean needRenamePane() {
            return false;
        }

        protected int getChartParaType() {
            return ParameterTableModel.CHART_NORMAL_USE;
        }
    }

    public static class ChartHasAnimateType extends ChartNoRename {
        public ChartHasAnimateType(HashMap hyperLinkEditorMap, boolean needRenamePane) {
            super(hyperLinkEditorMap, needRenamePane);
        }

        public ChartHasAnimateType() {
        }

        @Override
        protected boolean needAnimatePane() {
            return true;
        }
    }
}
