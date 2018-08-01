package com.fr.van.chart.gantt.designer.data.data.component;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.general.ComparatorUtils;

import com.fr.plugin.chart.gantt.data.VanGanttTableDefinition;
import com.fr.stable.StringUtils;
import com.fr.van.chart.gantt.designer.data.data.GanttDataPaneHelper;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

/**
 * Created by hufan on 2017/1/10.
 */
public class GanttTableDataContentPane extends AbstractTableDataContentPane{
    private static final String NONE = com.fr.design.i18n.Toolkit.i18nText("Chart-Use_None");

    private UIComboBox seriesComboBox;
    private UIComboBox startTimeComboBox;
    private UIComboBox endTimeComboBox;
    private UIComboBox markerTimeComboBox;
    private UIComboBox processComboBox;
    private UIComboBox taskIDComboBox;

    public GanttTableDataContentPane() {
        this.setLayout(new BorderLayout());
        initAllComponent();
        JPanel panel = getContentPane();
        panel.setBorder(BorderFactory.createEmptyBorder(0,24,0,15));
        this.add(panel, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(246,(int)this.getPreferredSize().getHeight()));

    }

    private void initAllComponent() {
        seriesComboBox = new UIComboBox();

        startTimeComboBox = new UIComboBox();

        endTimeComboBox = new UIComboBox();

        markerTimeComboBox = new UIComboBox();
        markerTimeComboBox.addItem(NONE);

        processComboBox = new UIComboBox();
        processComboBox.addItem(NONE);

        taskIDComboBox = new UIComboBox();
        taskIDComboBox.addItem(NONE);
        taskIDComboBox.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Task_ID_Tooltip"));
    }

    private JPanel getContentPane(){
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p,p,p,p,p,p};
        double[] col = {p,f};

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Chart-Series_Name")), seriesComboBox},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Start_Time")), startTimeComboBox},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_End_Time")), endTimeComboBox},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Marker_Time")), markerTimeComboBox},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Process")), processComboBox},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Task_ID")), taskIDComboBox}
        };

        return TableLayoutHelper.createGapTableLayoutPane(components, row, col,24,6);
    }

    @Override
    public void updateBean(ChartCollection ob) {
        VanGanttTableDefinition ganttDefinition = GanttDataPaneHelper.getGanttDefinition(ob);

        Object seriesName = seriesComboBox.getSelectedItem();
        ganttDefinition.setSeriesName(seriesName == null ? StringUtils.EMPTY : String.valueOf(seriesName));

        Object startTime = startTimeComboBox.getSelectedItem();
        ganttDefinition.setStartTime(startTime == null ? StringUtils.EMPTY : String.valueOf(startTime));

        Object endTime = endTimeComboBox.getSelectedItem();
        ganttDefinition.setEndTime(endTime == null ? StringUtils.EMPTY : String.valueOf(endTime));

        Object markTime = markerTimeComboBox.getSelectedItem();
        ganttDefinition.setMarkTime(isNoneObject(markTime) ? StringUtils.EMPTY : String.valueOf(markTime));

        Object process = processComboBox.getSelectedItem();
        ganttDefinition.setProgress(isNoneObject(process) ? StringUtils.EMPTY : String.valueOf(process));

        Object taskID = taskIDComboBox.getSelectedItem();
        ganttDefinition.setLinkID(isNoneObject(taskID) ? StringUtils.EMPTY : String.valueOf(taskID));
    }

    private boolean isNoneObject(Object markTime) {
        return markTime == null
                || ComparatorUtils.equals(markTime, NONE);
    }

    @Override
    public void populateBean(ChartCollection collection) {
        super.populateBean(collection);
        TopDefinition top = (TopDefinition)collection.getSelectedChart().getFilterDefinition();
        if (top instanceof VanGanttTableDefinition){
            VanGanttTableDefinition ganttDefinition = (VanGanttTableDefinition) top;

            seriesComboBox.setSelectedItem(ganttDefinition.getSeriesName());
            startTimeComboBox.setSelectedItem(ganttDefinition.getStartTime());
            endTimeComboBox.setSelectedItem(ganttDefinition.getEndTime());
            markerTimeComboBox.setSelectedItem(StringUtils.isEmpty(ganttDefinition.getMarkTime()) ? NONE : ganttDefinition.getMarkTime());
            processComboBox.setSelectedItem(StringUtils.isEmpty(ganttDefinition.getProgress()) ? NONE : ganttDefinition.getProgress());
            taskIDComboBox.setSelectedItem(StringUtils.isEmpty(ganttDefinition.getLinkID()) ? NONE : ganttDefinition.getLinkID());
        }

    }

    @Override
    public void clearAllBoxList() {
        clearBoxItems(seriesComboBox);

        clearBoxItems(startTimeComboBox);

        clearBoxItems(endTimeComboBox);

        clearBoxItems(markerTimeComboBox);
        markerTimeComboBox.addItem(NONE);

        clearBoxItems(processComboBox);
        processComboBox.addItem(NONE);

        clearBoxItems(taskIDComboBox);
        taskIDComboBox.addItem(NONE);
    }

    @Override
    public void refreshBoxListWithSelectTableData(List columnNameList) {
        refreshBoxItems(seriesComboBox, columnNameList);

        refreshBoxItems(startTimeComboBox, columnNameList);

        refreshBoxItems(endTimeComboBox, columnNameList);

        refreshBoxItems(markerTimeComboBox, columnNameList);
        markerTimeComboBox.addItem(NONE);

        refreshBoxItems(processComboBox, columnNameList);
        processComboBox.addItem(NONE);

        refreshBoxItems(taskIDComboBox, columnNameList);
        taskIDComboBox.addItem(NONE);
    }

    public void checkBoxUse(boolean hasUse) {
        seriesComboBox.setEnabled(hasUse);
        startTimeComboBox.setEnabled(hasUse);
        endTimeComboBox.setEnabled(hasUse);
        markerTimeComboBox.setEnabled(hasUse);
        processComboBox.setEnabled(hasUse);
        taskIDComboBox.setEnabled(hasUse);
    }
}
