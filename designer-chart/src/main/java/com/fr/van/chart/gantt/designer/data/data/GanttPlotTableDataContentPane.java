package com.fr.van.chart.gantt.designer.data.data;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.van.chart.gantt.designer.data.data.component.GanttTableDataContentPane;
import com.fr.van.chart.gantt.designer.data.data.component.GanttTableDataProjectPane;

import javax.swing.BorderFactory;
import javax.swing.JSeparator;
import java.awt.BorderLayout;
import java.util.List;

/**
 * Created by hufan on 2017/1/10.
 */
public class GanttPlotTableDataContentPane extends AbstractTableDataContentPane {
    private GanttTableDataProjectPane projectPane;
    private GanttTableDataContentPane contentPane;
    private static final int V_GAP = 7;

    public GanttPlotTableDataContentPane() {
        initComponent();
        this.setLayout(new BorderLayout(0, V_GAP));
        projectPane.setBorder(BorderFactory.createEmptyBorder(0,17,0,15));
        this.add(projectPane, BorderLayout.NORTH);
        this.add(new JSeparator(), BorderLayout.CENTER);
        this.add(contentPane, BorderLayout.SOUTH);
    }

    private void initComponent() {
        projectPane = new GanttTableDataProjectPane();
        contentPane = new GanttTableDataContentPane();
    }

    @Override
    public void updateBean(ChartCollection ob) {
        projectPane.updateBean(ob);
        contentPane.updateBean(ob);
    }

    public void populateBean(ChartCollection ob){
        projectPane.populateBean(ob);
        contentPane.populateBean(ob);
    }

    @Override
    public void clearAllBoxList() {
        projectPane.clearAllBoxList();
        contentPane.clearAllBoxList();
    }

    @Override
    protected void refreshBoxListWithSelectTableData(List columnNameList) {
        projectPane.refreshBoxListWithSelectTableData(columnNameList);
        contentPane.refreshBoxListWithSelectTableData(columnNameList);
    }

    /**
     * 检查 某些Box是否可用
     * @param hasUse  是否使用.
     */
    public void checkBoxUse(boolean hasUse) {
        projectPane.checkBoxUse(hasUse);
        contentPane.checkBoxUse(hasUse);
    }
}
