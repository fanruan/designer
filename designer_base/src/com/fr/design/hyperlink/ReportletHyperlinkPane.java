package com.fr.design.hyperlink;

import com.fr.base.BaseUtils;
import com.fr.base.Parameter;
import com.fr.base.chart.BasePlot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditAction;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.parameter.ParameterReader;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.js.ReportletHyperlink;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ReportletHyperlinkPane extends BasicBeanPane<ReportletHyperlink> {
    private BasePlot plot;
    private ReporletHyperNorthPane northPane;
    private ReportletParameterViewPane parameterViewPane = null;
    private UICheckBox extendParametersCheckBox;

    protected BasePlot getPlot() {
        return plot;
    }

    public ReportletHyperlinkPane(BasePlot plot) {
        super();
        this.plot = plot;
        this.initComponents();
    }

    public ReportletHyperlinkPane() {
        super();
        this.initComponents();
    }

    protected void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        northPane = new ReporletHyperNorthPane(needRenamePane());
        this.add(northPane, BorderLayout.NORTH);

        parameterViewPane = new ReportletParameterViewPane(new UITableEditAction[]{new HyperlinkParametersAction()}, getChartParaType(), getValueEditorPane(), getValueEditorPane());

        this.add(parameterViewPane, BorderLayout.CENTER);

        parameterViewPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Designer_Parameter"), null));

        extendParametersCheckBox = new UICheckBox(Inter.getLocText("Hyperlink-Extends_Report_Parameters"));
        this.add(GUICoreUtils.createFlowPane(extendParametersCheckBox, FlowLayout.LEFT), BorderLayout.SOUTH);
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Hyperlink_Reportlet");
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

    @Override
    public void populateBean(ReportletHyperlink ob) {
        northPane.populateBean(ob);
        // parameter
        List<ParameterProvider> parameterList = this.parameterViewPane.update();
        parameterList.clear();

        ParameterProvider[] parameters = ob.getParameters();
        parameterViewPane.populate(parameters);

        extendParametersCheckBox.setSelected(ob.isExtendParameters());
    }

    @Override
    public ReportletHyperlink updateBean() {
        ReportletHyperlink reportletHyperlink = new ReportletHyperlink();
        updateBean(reportletHyperlink);

        return reportletHyperlink;
    }

    public void updateBean(ReportletHyperlink reportletHyperlink) {
        northPane.updateBean(reportletHyperlink);
        // Parameter.
        List<ParameterProvider> parameterList = this.parameterViewPane.update();
        if (!parameterList.isEmpty()) {
            Parameter[] parameters = new Parameter[parameterList.size()];
            parameterList.toArray(parameters);

            reportletHyperlink.setParameters(parameters);
        } else {
            reportletHyperlink.setParameters(null);
        }

        reportletHyperlink.setExtendParameters(extendParametersCheckBox.isSelected());

    }

    protected class HyperlinkParametersAction extends UITableEditAction {
        public HyperlinkParametersAction() {
            this.setName(Inter.getLocText(new String[]{"Reportlet", "Parameter"}));
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/p.gif"));
        }

        public void actionPerformed(ActionEvent e) {
            String tpl = northPane.getReportletName();
            if (StringUtils.isBlank(tpl)) {
                JOptionPane.showMessageDialog(ReportletHyperlinkPane.this, Inter.getLocText("FR-Hyperlink_Please_Select_Reportlet") + ".", Inter.getLocText("FR-Designer_Message"), JOptionPane.WARNING_MESSAGE);
                northPane.requestButtonFocus();
                return;
            }

            //根据模板路径返回参数
            //与当前模块、当前文件无关
            Parameter[] parameters = new Parameter[0];

            ParameterReader[] readers = DesignModuleFactory.getParameterReaders();
            for (ParameterReader reader : readers) {
                Parameter[] ps = reader.readParameterFromPath(tpl);
                if (ps != null) {
                    parameters = ps;
                }
            }
            parameterViewPane.populate(parameters);
        }

        @Override
        public void checkEnabled() {

        }
    }

    public static class CHART_NO_RENAME extends ReportletHyperlinkPane {
        protected int getChartParaType() {
            return ParameterTableModel.CHART_NORMAL_USE;
        }

        protected boolean needRenamePane() {
            return false;
        }
    }

    public ReporletHyperNorthPane getNorthPane() {
        return northPane;
    }

    public void setNorthPane(ReporletHyperNorthPane northPane) {
        this.northPane = northPane;
    }

    public ReportletParameterViewPane getParameterViewPane() {
        return parameterViewPane;
    }

    public void setParameterViewPane(ReportletParameterViewPane parameterViewPane) {
        this.parameterViewPane = parameterViewPane;
    }

    public UICheckBox getExtendParametersCheckBox() {
        return extendParametersCheckBox;
    }

    public void setExtendParametersCheckBox(UICheckBox extendParametersCheckBox) {
        this.extendParametersCheckBox = extendParametersCheckBox;
    }
}