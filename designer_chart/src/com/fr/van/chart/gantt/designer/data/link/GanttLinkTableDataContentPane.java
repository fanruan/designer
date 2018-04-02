package com.fr.van.chart.gantt.designer.data.link;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.gantt.data.VanGanttLinkTableDefinition;
import com.fr.stable.StringUtils;
import com.fr.van.chart.gantt.designer.data.data.GanttDataPaneHelper;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

/**
 * Created by hufan on 2017/1/11.
 */
public class GanttLinkTableDataContentPane extends AbstractTableDataContentPane {
    private UIComboBox startTaskIDComboBox;
    private UIComboBox endTaskIDComboBox;
    private UIComboBox linkTypeComboBox;

    public GanttLinkTableDataContentPane() {
        this.setLayout(new BorderLayout());
        initAllComponent();
        JPanel panel = getContentPane();
        panel.setBorder(BorderFactory.createEmptyBorder(0,24,0,15));
        this.add(panel, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(246,(int)this.getPreferredSize().getHeight()));

    }

    private void initAllComponent() {
        startTaskIDComboBox = new UIComboBox();

        endTaskIDComboBox = new UIComboBox();

        linkTypeComboBox = new UIComboBox();

    }

    private JPanel getContentPane(){
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p,p,p};
        double[] col = {p,f};

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Start_Task_ID")), startTaskIDComboBox},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_End_Task_ID")), endTaskIDComboBox},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Link_Type")), linkTypeComboBox}
        };

        return TableLayoutHelper.createGapTableLayoutPane(components, row, col,24,6);
    }

    @Override
    public void updateBean(ChartCollection ob) {
        VanGanttLinkTableDefinition taskLinkDefinition = GanttDataPaneHelper.getGanttTaskLinkDefinition(ob);

        Object startTaskID = startTaskIDComboBox.getSelectedItem();
        taskLinkDefinition.setStartTaskID(startTaskID == null ? StringUtils.EMPTY : String.valueOf(startTaskID));

        Object endTaskID = endTaskIDComboBox.getSelectedItem();
        taskLinkDefinition.setEndTaskID(endTaskID == null ? StringUtils.EMPTY : String.valueOf(endTaskID));

        Object linkType = linkTypeComboBox.getSelectedItem();
        taskLinkDefinition.setLinkType(linkType == null ? StringUtils.EMPTY : String.valueOf(linkType));
    }
    @Override
    public void populateBean(ChartCollection collection) {
        super.populateBean(collection);
        TopDefinition top = (TopDefinition)collection.getSelectedChart().getFilterDefinition();
        if (top instanceof VanGanttLinkTableDefinition){
            VanGanttLinkTableDefinition taskLinkTableDefinition = (VanGanttLinkTableDefinition) top;

            startTaskIDComboBox.setSelectedItem(taskLinkTableDefinition.getStartTaskID());
            endTaskIDComboBox.setSelectedItem(taskLinkTableDefinition.getEndTaskID());
            linkTypeComboBox.setSelectedItem(taskLinkTableDefinition.getLinkType());

        }

    }
    @Override
    public void clearAllBoxList() {
        clearBoxItems(startTaskIDComboBox);

        clearBoxItems(endTaskIDComboBox);

        clearBoxItems(linkTypeComboBox);

    }

    @Override
    public void refreshBoxListWithSelectTableData(List columnNameList) {
        refreshBoxItems(startTaskIDComboBox, columnNameList);

        refreshBoxItems(endTaskIDComboBox, columnNameList);

        refreshBoxItems(linkTypeComboBox, columnNameList);

    }

    public void checkBoxUse(boolean hasUse) {
        startTaskIDComboBox.setEnabled(hasUse);
        endTaskIDComboBox.setEnabled(hasUse);
        linkTypeComboBox.setEnabled(hasUse);
    }
}
