package com.fr.plugin.chart.gantt.designer.data.link;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.gantt.data.VanGanttLinkReportDefinition;
import com.fr.plugin.chart.gantt.designer.data.data.GanttDataPaneHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hufan on 2017/1/12.
 */
public class GanttLinkReportDataContentPane extends AbstractReportDataContentPane {
    private TinyFormulaPane startTaskID;
    private TinyFormulaPane endTaskID;
    private TinyFormulaPane linkType;

    public GanttLinkReportDataContentPane() {
        this.setLayout(new BorderLayout());
        initAllComponent();
        JPanel panel = getContentPane();
        panel.setBorder(BorderFactory.createEmptyBorder(0,24,0,15));
        this.add(panel, BorderLayout.CENTER);    }

    private void initAllComponent() {
        startTaskID = createTinyFormulaPaneWithTitle(Inter.getLocText("Plugin-ChartF_Start_Task_ID"));

        endTaskID = createTinyFormulaPaneWithTitle(Inter.getLocText("Plugin-ChartF_End_Task_ID"));

        linkType = createTinyFormulaPaneWithTitle(Inter.getLocText("Plugin-ChartF_Link_Type"));
    }

    private TinyFormulaPane createTinyFormulaPaneWithTitle(final String title) {
        return new TinyFormulaPane() {
            @Override
            protected void initLayout() {
                this.setLayout(new BorderLayout(4, 0));

                UILabel label = new UILabel(title );
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
        double[] row = {p,p,p};
        double[] col = {f};

        Component[][] components = new Component[][]{
                new Component[]{startTaskID},
                new Component[]{endTaskID},
                new Component[]{linkType}
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
        if (topDefinitionProvider instanceof VanGanttLinkReportDefinition) {

            VanGanttLinkReportDefinition ganttReportDefinition = (VanGanttLinkReportDefinition) topDefinitionProvider;

            populateFormulaPane(startTaskID, ganttReportDefinition.getStartTaskID());
            populateFormulaPane(endTaskID, ganttReportDefinition.getEndTaskID());
            populateFormulaPane(linkType, ganttReportDefinition.getLinkType());
        }
    }

    private void populateFormulaPane(TinyFormulaPane pane, Object o){
        if(o != null){
            pane.populateBean(o.toString());
        }
    }

    public void updateBean(ChartCollection collection) {
        VanGanttLinkReportDefinition ganttReportDefinition = GanttDataPaneHelper.getGanttTaskLinkReportDefinition(collection);

        ganttReportDefinition.setStartTaskID(canBeFormula(startTaskID.getUITextField().getText()));
        ganttReportDefinition.setEndTaskID(canBeFormula(endTaskID.getUITextField().getText()));
        ganttReportDefinition.setLinkType(canBeFormula(linkType.getUITextField().getText()));
    }
}
