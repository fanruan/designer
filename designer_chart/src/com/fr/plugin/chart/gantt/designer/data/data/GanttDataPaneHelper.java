package com.fr.plugin.chart.gantt.designer.data.data;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.plugin.chart.gantt.data.VanGanttLinkReportDefinition;
import com.fr.plugin.chart.gantt.data.VanGanttLinkTableDefinition;
import com.fr.plugin.chart.gantt.data.VanGanttReportDefinition;
import com.fr.plugin.chart.gantt.data.VanGanttTableDefinition;

/**
 * Created by shine on 2017/8/8.
 */
public class GanttDataPaneHelper {
    public static VanGanttTableDefinition getGanttDefinition(ChartCollection collection){
        TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
        if (definition == null) {
            VanGanttTableDefinition ganttDefinition = new VanGanttTableDefinition();
            collection.getSelectedChart().setFilterDefinition(ganttDefinition);
            return ganttDefinition;
        }else if (definition instanceof VanGanttTableDefinition){
            return (VanGanttTableDefinition) definition;
        }else {
            VanGanttTableDefinition ganttDefinition = new VanGanttTableDefinition();
            collection.getSelectedChart().setFilterDefinition(ganttDefinition);
            return ganttDefinition;
        }
    }

    public static VanGanttLinkTableDefinition getGanttTaskLinkDefinition(ChartCollection collection){
        TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
        if (definition == null) {
            VanGanttLinkTableDefinition ganttDefinition = new VanGanttLinkTableDefinition();
            collection.getSelectedChart().setFilterDefinition(ganttDefinition);
            return ganttDefinition;
        }else if (definition instanceof VanGanttLinkTableDefinition){
            return (VanGanttLinkTableDefinition) definition;
        }else {
            VanGanttLinkTableDefinition ganttDefinition = new VanGanttLinkTableDefinition();
            collection.getSelectedChart().setFilterDefinition(ganttDefinition);
            return ganttDefinition;
        }
    }


    public static VanGanttReportDefinition getGanttReportDefinition(ChartCollection collection){
        TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
        if (definition == null) {
            VanGanttReportDefinition ganttDefinition = new VanGanttReportDefinition();
            collection.getSelectedChart().setFilterDefinition(ganttDefinition);
            return ganttDefinition;
        }else if (definition instanceof VanGanttReportDefinition){
            return (VanGanttReportDefinition) definition;
        }else {
            VanGanttReportDefinition ganttDefinition = new VanGanttReportDefinition();
            collection.getSelectedChart().setFilterDefinition(ganttDefinition);
            return ganttDefinition;
        }
    }

    public static VanGanttLinkReportDefinition getGanttTaskLinkReportDefinition(ChartCollection collection){
        TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
        if (definition == null) {
            VanGanttLinkReportDefinition ganttDefinition = new VanGanttLinkReportDefinition();
            collection.getSelectedChart().setFilterDefinition(ganttDefinition);
            return ganttDefinition;
        }else if (definition instanceof VanGanttLinkReportDefinition){
            return (VanGanttLinkReportDefinition) definition;
        }else {
            VanGanttLinkReportDefinition ganttDefinition = new VanGanttLinkReportDefinition();
            collection.getSelectedChart().setFilterDefinition(ganttDefinition);
            return ganttDefinition;
        }
    }

}
