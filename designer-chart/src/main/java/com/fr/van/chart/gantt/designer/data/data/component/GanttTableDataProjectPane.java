package com.fr.van.chart.gantt.designer.data.data.component;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;

import com.fr.plugin.chart.gantt.data.VanGanttTableDefinition;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.van.chart.gantt.designer.data.data.GanttDataPaneHelper;

import javax.swing.Icon;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hufan on 2017/1/10.
 */
public class GanttTableDataProjectPane extends AbstractTableDataContentPane implements UIObserver{
    private ComboBoxWithButtonPane projectName;
    private JPanel center;
    private List<ComboBoxWithButtonPane> stepName = new ArrayList<ComboBoxWithButtonPane>();
    //监听器，当重新布局层级名控件时需要用到
    private UIObserverListener listener;
    private static final int V_GAP = 7;
    private List comboBoxList;
    private boolean comboBoxEnable = false;

    public GanttTableDataProjectPane() {

        projectName = new ComboBoxWithButtonPane(){
            @Override
            protected String getTitleText() {
                return com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Project_Name");
            }

            @Override
            protected Icon getButtonIcon(){
                return BaseUtils.readIcon("/com/fr/design/images/buttonicon/add.png");
            }
            @Override
            protected ActionListener getButtonListener(){
                ActionListener addListener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addStepComboBoxPane();
                    }
                };
                return addListener;
            }
        };

        this.setLayout(new BorderLayout(0, V_GAP));

        if (center != null) {
            this.add(center, BorderLayout.CENTER);
            this.add(projectName, BorderLayout.NORTH);
        }else {
            this.add(projectName);
        }
    }

    private void addStepComboBoxPane() {
        int index = stepName.size();
        ComboBoxWithButtonPane stepPane = createComBoxWithButtonPane(index);

        if (comboBoxList != null && comboBoxList.size() > 0) {
            stepPane.refreshBoxItems(comboBoxList);
        }
        stepPane.checkBoxUse(comboBoxEnable);

        stepPane.registerUIObserverListener(listener);

        stepName.add(stepPane);
        //触发更新
        fire();
    }

    private ComboBoxWithButtonPane createComBoxWithButtonPane(int index) {
        return new ComboBoxWithButtonPane(index){
            @Override
            protected String getTitleText() {
                return StringUtils.EMPTY;
            }

            @Override
            protected Icon getButtonIcon() {
                return BaseUtils.readIcon("/com/fr/design/images/buttonicon/delete.png");
            }

            @Override
            protected ActionListener getButtonListener() {
                ActionListener addListener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        deleteStepComboBoxPane(getIndex());
                        //删除之后，重新调整index
                        for (int i = 0; i < stepName.size(); i++){
                            stepName.get(i).setIndex(i);
                        }

                    }
                };
                return addListener;
            }
        };
    }


    private void fire(){
        //更新布局

        relayOutCenterPane();

        if (listener != null){
            listener.doChange();
        }
    }

    private void relayOutCenterPane() {
        int rows = stepName.size();

        if (center != null) {
            this.remove(projectName);
            this.remove(center);
        }

        if (rows > 0) {
            center = new JPanel();
            center.setLayout(new GridLayout(rows, 1, 0, V_GAP));
            for (int i = 0; i < rows; i++) {
                center.add(stepName.get(i));
            }
            this.setLayout(new BorderLayout(0, V_GAP));
            this.add(projectName, BorderLayout.NORTH);
            this.add(center, BorderLayout.CENTER);
        }else {
            this.add(projectName);
        }

        refreshPane();

    }

    private void refreshPane() {
        this.validate();
        this.repaint();
        this.revalidate();
    }

    private void deleteStepComboBoxPane(int index){
        if (index >= stepName.size() || index < -1){
            return;
        }

        stepName.remove(index);

        //触发更新
        fire();
    }

    @Override
    public void updateBean(ChartCollection ob) {
        VanGanttTableDefinition ganttDefinition = GanttDataPaneHelper.getGanttDefinition(ob);

        //项目名
        Object name = projectName.getComboBoxName();
        ganttDefinition.setFirstProcess(name == null ? StringUtils.EMPTY : String.valueOf(name));
        //步骤列表
        updateStepName(ganttDefinition);

    }

    private void updateStepName(VanGanttTableDefinition ganttDefinition) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < stepName.size(); i++){
            Object sName = stepName.get(i).getComboBoxName();
            list.add(sName == null ? StringUtils.EMPTY : String.valueOf(sName));
        }
        ganttDefinition.setProcessList(list);
    }

    @Override
    public void populateBean(ChartCollection collection) {
        super.populateBean(collection);
        TopDefinition top = (TopDefinition)collection.getSelectedChart().getFilterDefinition();
        if (top instanceof VanGanttTableDefinition){
            VanGanttTableDefinition ganttDefinition = (VanGanttTableDefinition) top;

            projectName.setComboBoxName(ganttDefinition.getFirstProcess());

            populateStepName(ganttDefinition.getProcessList());
        }

    }

    private void populateStepName(List<String> stepList) {
        stepName = new ArrayList<ComboBoxWithButtonPane>();
        for (int i = 0; i < stepList.size(); i++){
            ComboBoxWithButtonPane stepPane = createComBoxWithButtonPane(i);

            if (comboBoxList != null && comboBoxList.size() > 0) {
                stepPane.refreshBoxItems(comboBoxList);
            }
            stepPane.setComboBoxName(stepList.get(i));

            stepName.add(i, stepPane);
        }

        relayOutCenterPane();
    }


    @Override
    public void clearAllBoxList() {
        projectName.clearBoxItems();

        for (int i = 0; i < stepName.size(); i++){
            stepName.get(i).clearBoxItems();
        }
    }

    @Override
    public void refreshBoxListWithSelectTableData(List columnNameList) {

        updateList(columnNameList);

        projectName.refreshBoxItems(columnNameList);

        for (int i = 0; i < stepName.size(); i++){
            stepName.get(i).refreshBoxItems(columnNameList);
        }
    }

    private void updateList(List columnNameList) {
        comboBoxList = new ArrayList();
        for (int i = 0; i < columnNameList.size(); i++){
            try {
                comboBoxList.add(StableUtils.cloneObject(columnNameList.get(i)));
            } catch (CloneNotSupportedException e) {
                FRContext.getLogger().info(e.getMessage());
            }
        }
    }

    @Override
    public void registerChangeListener(UIObserverListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean shouldResponseChangeListener() {
        return true;
    }


    public void checkBoxUse(boolean hasUse) {
        comboBoxEnable = hasUse;
        projectName.checkBoxUse(hasUse);
        for (int i = 0; i < stepName.size(); i++){
            stepName.get(i).checkBoxUse(hasUse);
        }
    }
}
