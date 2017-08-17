package com.fr.plugin.chart.gantt.designer.data.data.component;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.plugin.chart.gantt.data.VanGanttTableDefinition;
import com.fr.plugin.chart.gantt.designer.data.data.GanttDataPaneHelper;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by hufan on 2017/1/10.
 */
public class GanttTableDataContentPane extends AbstractTableDataContentPane{
    private static final String NONE = Inter.getLocText("Chart-Use_None");

    private UIComboBox seriesComboBox;
    private UIComboBox startTimeComboBox;
    private UIComboBox endTimeComboBox;
    private UIComboBox markerTimeComboBox;
    private UIComboBox processComboBox;
    private UIComboBox taskIDComboBox;

    public GanttTableDataContentPane() {
        this.setLayout(new BorderLayout());
        initAllComponent();
        this.add(getContentPane(), BorderLayout.CENTER);
    }

    private void initAllComponent() {
        seriesComboBox = new UIComboBox();
        seriesComboBox.setPreferredSize(new Dimension(100,20));

        startTimeComboBox = new UIComboBox();
        startTimeComboBox.setPreferredSize(new Dimension(100,20));

        endTimeComboBox = new UIComboBox();
        endTimeComboBox.setPreferredSize(new Dimension(100,20));

        markerTimeComboBox = new UIComboBox();
        markerTimeComboBox.setPreferredSize(new Dimension(100,20));
        markerTimeComboBox.addItem(NONE);

        processComboBox = new UIComboBox();
        processComboBox.setPreferredSize(new Dimension(100,20));
        processComboBox.addItem(NONE);

        taskIDComboBox = new UIComboBox();
        taskIDComboBox.setPreferredSize(new Dimension(100,20));
        taskIDComboBox.addItem(NONE);
        taskIDComboBox.setToolTipText(Inter.getLocText("Plugin-ChartF_Task_ID_Tooltip"));
    }

    private JPanel getContentPane(){
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p,p,p,p,p,p};
        double[] col = {p,f};

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Chart-Series_Name")+":", SwingConstants.RIGHT), seriesComboBox},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Start_Time")+":", SwingConstants.RIGHT), startTimeComboBox},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_End_Time")+":", SwingConstants.RIGHT), endTimeComboBox},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Marker_Time")+":", SwingConstants.RIGHT), markerTimeComboBox},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Process")+":", SwingConstants.RIGHT), processComboBox},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Task_ID")+":", SwingConstants.RIGHT), taskIDComboBox}
        };

        return TableLayoutHelper.createTableLayoutPane(components, row, col);
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
