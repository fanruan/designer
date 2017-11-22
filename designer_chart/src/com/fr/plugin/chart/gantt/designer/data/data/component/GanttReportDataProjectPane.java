package com.fr.plugin.chart.gantt.designer.data.data.component;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.gantt.data.VanGanttReportDefinition;
import com.fr.plugin.chart.gantt.designer.data.data.GanttDataPaneHelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hufan on 2017/1/11.
 */
public class GanttReportDataProjectPane extends AbstractReportDataContentPane implements UIObserver {
    private TinyFormulaWithButtonPane firstProcessPane;
    private JPanel center;
    private UIObserverListener listener;
    private List<TinyFormulaWithButtonPane> processPaneList = new ArrayList<TinyFormulaWithButtonPane>();

    public GanttReportDataProjectPane() {
        firstProcessPane = new TinyFormulaWithButtonPane(Inter.getLocText("Plugin-ChartF_Project_Name"), "/com/fr/design/images/buttonicon/add.png") {
            @Override
            protected void buttonEvent(TinyFormulaWithButtonPane pane) {
                addProcessPane();
            }

            @Override
            protected void clearAllBackground() {
                clearAllLabelBackground();
            }
        };
        firstProcessPane.registerChangeListener(listener);

        this.setLayout(new BorderLayout(0, 7));

        this.add(firstProcessPane, BorderLayout.NORTH);
        if (center != null) {
            this.add(center, BorderLayout.CENTER);
        }
    }

    private TinyFormulaWithButtonPane createTinyFormulaWithButtonPane(String content) {
        return new TinyFormulaWithButtonPane(content){

            @Override
            protected void buttonEvent(TinyFormulaWithButtonPane pane) {
                deleteProcessPane(pane);
            }

            @Override
            protected void clearAllBackground() {
                clearAllLabelBackground();
            }
        };
    }

    private void clearAllLabelBackground() {
        for(TinyFormulaWithButtonPane pane : processPaneList) {
            pane.clearBackGround();
        }
    }

    private void addProcessPane() {
        int index = processPaneList.size();
        String content = Inter.getLocText("Chart-Use_Items") + (index + 1);
        TinyFormulaWithButtonPane stepPane = createTinyFormulaWithButtonPane(content);
        stepPane.registerChangeListener(listener);

        processPaneList.add(stepPane);

        //触发更新
        fire();
    }

    private void deleteProcessPane(TinyFormulaWithButtonPane pane) {
        processPaneList.remove(pane);

        //触发更新
        fire();
    }

    private void fire() {
        relayOutCenterPane();

        if (listener != null){
            listener.doChange();
        }
    }

    private void relayOutCenterPane() {

        int rows = processPaneList.size();

        if (center != null) {
            this.remove(center);
        }

        if (rows > 0) {
            center = new JPanel();
            center.setLayout(new GridLayout(rows, 1, 0, 7));
            for (int i = 0; i < rows; i++) {
                center.add(processPaneList.get(i));
            }
            this.add(center, BorderLayout.CENTER);
        }

        this.validate();
        this.repaint();
        this.revalidate();
    }


    @Override
    public void populateBean(ChartCollection collection) {
        if (collection == null){
            return;
        }

        TopDefinition top = (TopDefinition)collection.getSelectedChart().getFilterDefinition();
        if (top instanceof VanGanttReportDefinition){
            VanGanttReportDefinition ganttDefinition = (VanGanttReportDefinition) top;

            firstProcessPane.populateFormula(ganttDefinition.getFirstProcess());
            firstProcessPane.setHeaderName(ganttDefinition.getFirstHeaderName());


            List<String> headerNameList = ganttDefinition.getHeaderNameList();
            List<Object> processList = ganttDefinition.getProcessList();
            processPaneList = new ArrayList<TinyFormulaWithButtonPane>();
            for (int i = 0; i < processList.size(); i++){
                TinyFormulaWithButtonPane stepPane = createTinyFormulaWithButtonPane(headerNameList.get(i));
                stepPane.populateFormula(processList.get(i));
                stepPane.registerChangeListener(listener);

                processPaneList.add(i, stepPane);
            }

            relayOutCenterPane();
        }
    }

    @Override
    public void updateBean(ChartCollection ob) {
        VanGanttReportDefinition ganttDefinition = GanttDataPaneHelper.getGanttReportDefinition(ob);

        ganttDefinition.setFirstProcess(canBeFormula(firstProcessPane.updateFormula()));
        ganttDefinition.setFirstHeaderName(firstProcessPane.getHeaderName());

        List<String> headerNameList = new ArrayList<String>();
        List<Object> processList = new ArrayList<Object>();
        for (TinyFormulaWithButtonPane pane : processPaneList){
            processList.add(canBeFormula(pane.updateFormula()));
            headerNameList.add(pane.getHeaderName());
        }
        ganttDefinition.setHeaderNameList(headerNameList);
        ganttDefinition.setProcessList(processList);
    }

    @Override
    protected String[] columnNames() {
        return new String[0];
    }

    @Override
    public void registerChangeListener(UIObserverListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean shouldResponseChangeListener() {
        return true;
    }
}
