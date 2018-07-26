package com.fr.van.chart.gantt.designer.data.data.component;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;

import com.fr.plugin.chart.gantt.data.VanGanttReportDefinition;
import com.fr.van.chart.gantt.designer.data.data.GanttDataPaneHelper;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

/**
 * Created by hufan on 2017/1/11.
 */
public class GanttReportDataContentPane extends AbstractReportDataContentPane{
    private TinyFormulaPane seriesName;
    private TinyFormulaPane startTime;
    private TinyFormulaPane endTime;
    private TinyFormulaPane markerTime;
    private TinyFormulaPane progress;
    private TinyFormulaPane linkID;

    public GanttReportDataContentPane() {
        this.setLayout(new BorderLayout());
        initAllComponent();
        JPanel panel = getContentPane();
        panel.setBorder(BorderFactory.createEmptyBorder(10,24,0,15));
        this.add(getJSeparator(), BorderLayout.NORTH);
        this.add(panel, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(246,(int)this.getPreferredSize().getHeight()));

    }

    private void initAllComponent() {
        seriesName = createTinyFormulaPaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Chart-Series_Name"));

        startTime = createTinyFormulaPaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Start_Time"));

        endTime = createTinyFormulaPaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_End_Time"));

        markerTime = createTinyFormulaPaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Marker_Time"));

        progress = createTinyFormulaPaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Process"));

        linkID = createTinyFormulaPaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Task_ID"));
    }

    private TinyFormulaPane createTinyFormulaPaneWithTitle(final String title) {
        return new TinyFormulaPane() {
            @Override
            protected void initLayout() {
                this.setLayout(new BorderLayout(4, 0));

                UILabel label = new UILabel(title);
                label.setPreferredSize(new Dimension(75, 20));
                this.add(label, BorderLayout.WEST);

                formulaTextField.setPreferredSize(new Dimension(100, 20));
                this.add(formulaTextField, BorderLayout.CENTER);
                this.add(formulaTextFieldButton, BorderLayout.EAST);
            }
        };
    }

    private JPanel getContentPane(){
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p,p,p,p,p,p};
        double[] col = {f};

        Component[][] components = new Component[][]{
                new Component[]{seriesName},
                new Component[]{startTime},
                new Component[]{endTime},
                new Component[]{markerTime},
                new Component[]{progress},
                new Component[]{linkID}
        };

        return TableLayoutHelper.createTableLayoutPane(components, row, col);
    }

    @Override
    protected String[] columnNames() {
        return new String[0];
    }

    @Override
    public void populateBean(ChartCollection ob) {
        TopDefinitionProvider topDefinitionProvider = ob.getSelectedChart().getFilterDefinition();
        if (topDefinitionProvider instanceof VanGanttReportDefinition) {

            VanGanttReportDefinition ganttReportDefinition = (VanGanttReportDefinition) topDefinitionProvider;

            populateDefinition(ganttReportDefinition);
        }
    }

    private void populateDefinition(VanGanttReportDefinition ganttReportDefinition) {
        populateFormulaPane(seriesName, ganttReportDefinition.getSeriesName());
        populateFormulaPane(startTime, ganttReportDefinition.getStartTime());
        populateFormulaPane(endTime, ganttReportDefinition.getEndTime());
        populateFormulaPane(markerTime, ganttReportDefinition.getMarkTime());
        populateFormulaPane(progress, ganttReportDefinition.getProgress());
        populateFormulaPane(linkID, ganttReportDefinition.getLinkID());
    }

    private void populateFormulaPane(TinyFormulaPane pane, Object o){
        if(o != null){
            pane.populateBean(o.toString());
        }
    }

    public void updateBean(ChartCollection collection) {
        VanGanttReportDefinition ganttReportDefinition = GanttDataPaneHelper.getGanttReportDefinition(collection);

        ganttReportDefinition.setSeriesName(canBeFormula(seriesName.getUITextField().getText()));

        ganttReportDefinition.setStartTime(canBeFormula(startTime.getUITextField().getText()));

        ganttReportDefinition.setEndTime(canBeFormula(endTime.getUITextField().getText()));

        ganttReportDefinition.setMarkTime(canBeFormula(markerTime.getUITextField().getText()));

        ganttReportDefinition.setProgress(canBeFormula(progress.getUITextField().getText()));

        ganttReportDefinition.setLinkID(canBeFormula(linkID.getUITextField().getText()));

    }
}
