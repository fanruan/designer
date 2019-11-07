package com.fr.van.chart.multilayer.data;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;

import com.fr.plugin.chart.multilayer.data.MultiPieReportDataDefinition;
import com.fr.stable.AssistUtils;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fangjie on 2016/6/15.
 */
public class MultiPiePlotReportDataContentPane extends AbstractReportDataContentPane implements UIObserver {
    private static final int HT = 20;
    private static final int WD = 80;
    private UISpinner levelNumEdit;//层级数
    private int levelNum = 3; //默认三层
    private UITextField nameField;//指标名称
    private TinyFormulaPane value;//值
    private List<TinyFormulaPane> levelNameList = null;//层次名
    //监听器，当重新布局层级名控件时需要用到
    private UIObserverListener listener;

    private JPanel contentPane;
    private JPanel center;
    public MultiPiePlotReportDataContentPane() {
    }

    public MultiPiePlotReportDataContentPane(ChartDataPane parent) {
        this.setLayout(new BorderLayout());
        initContentPane();
        this.add(contentPane, BorderLayout.CENTER);
    }

    private void initContentPane() {
        JPanel north = createNorthPane();

        center = createCenterPane();

        contentPane = new JPanel();

        contentPane.setLayout(new BorderLayout(0, 5));

        contentPane.add(north, BorderLayout.NORTH);
        contentPane.add(center, BorderLayout.CENTER);
        contentPane.setBorder(BorderFactory.createEmptyBorder(0,20,4,10));
    }

    private JPanel createNorthPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize_north = {f, COMPONENT_WIDTH};
        double[] rowSize_north = {p, p, p};

        levelNumEdit = new UISpinner(1, 15, 1, levelNum){
            @Override
            protected void fireStateChanged() {
                //先处理自身的空间布局
                refreshCenterPane();
                //然后更新数据
                super.fireStateChanged();
            }

            @Override
            public void setTextFieldValue(double value) {
                //如果为0，则没有改变值
                if (AssistUtils.equals(0, value)) {
                    return;
                }
                super.setTextFieldValue(value);
            }
        };

        nameField = new UITextField();
        nameField.setPreferredSize(new Dimension(WD, HT));

        Component[][] components_north = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_MultiPie_Series_Name")), nameField},
                new Component[]{new JSeparator(), null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Level_Number")), levelNumEdit},
        };

        return TableLayoutHelper.createTableLayoutPane(components_north, rowSize_north, columnSize_north);
    }

    private void refreshCenterPane(){
        if (levelNumEdit == null){
            return;
        }

        int newLevelNum = (int) levelNumEdit.getValue();
        //更新界面
        if (newLevelNum != levelNum){
            levelNum = newLevelNum;
            contentPane.remove(center);
            center = createCenterPane();
            contentPane.add(center, BorderLayout.CENTER);
        }

        refreshPane();
    }

    private JPanel createCenterPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize_center = {f, COMPONENT_WIDTH};
        double[] rowSize_center = new double[levelNum + 2];

        initLevelNameList();

        for (int i = 0; i < levelNum + 2; i++){
            rowSize_center[i] = p;
        }
        Component[][] components_center = new Component[levelNum + 2][];

        for (int i = 0; i < levelNum; i++){
            components_center[i] = new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Level") + String.valueOf(i+1)), levelNameList.get(i)};
        }

        value = new TinyFormulaPane();

        components_center[levelNum] = new Component[]{new JSeparator(), null};
        components_center[levelNum+1] = new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_Value")), value};

        registerListener4Center();

        return TableLayoutHelper.createTableLayoutPane(components_center,rowSize_center,columnSize_center);
    }

    /**
     * 为每个组件注册一个监听器
     */
    private void registerListener4Center() {
        for (int i = 0; i < levelNameList.size(); i++){
            levelNameList.get(i).registerChangeListener(listener);
        }
        value.registerChangeListener(listener);
    }

    private void initLevelNameList() {
        levelNameList = new ArrayList<TinyFormulaPane>();
        for (int i = 0; i < levelNum; i++){
            levelNameList.add(new TinyFormulaPane());
        }
    }

    @Override
    protected String[] columnNames() {
        return new String[0];
    }

    @Override
    public void populateBean(ChartCollection collection) {
        checkBoxUse();

        TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
        if (definition instanceof MultiPieReportDataDefinition) {
            MultiPieReportDataDefinition reportDefinition = (MultiPieReportDataDefinition) definition;
            //层级数
            levelNumEdit.setValue(reportDefinition.getLevelCount());
            levelNum = (int) levelNumEdit.getValue();
            //指标名
            nameField.setText(reportDefinition.getSeriesName());
            //各个层级名

            if (reportDefinition.getValue() != null) {
                value.getUITextField().setText(reportDefinition.getValue().toString());
            }

            populateAllLevelName(reportDefinition);
        }
    }

    private void populateAllLevelName(MultiPieReportDataDefinition definition) {
        if (levelNameList == null){
            initLevelNameList();
        }
        List<Object> levelColumnNameList = definition.getLevelColumnNameList();

        for (int i = 0; i < levelNameList.size(); i++){
            levelNameList.get(i).getUITextField().setText(levelColumnNameList.get(i) == null ? null : levelColumnNameList.get(i).toString());
        }
    }

    @Override
    public void updateBean(ChartCollection collection) {
        collection.getSelectedChart().setFilterDefinition(new MultiPieReportDataDefinition());

        TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
        if (definition instanceof MultiPieReportDataDefinition) {
            MultiPieReportDataDefinition reportDefinition = (MultiPieReportDataDefinition) definition;
            //层级数
            reportDefinition.setLevelCount((int) levelNumEdit.getValue());
            //指标名
            reportDefinition.setSeriesName(nameField.getText());
            //值
            reportDefinition.setValue(canBeFormula(value.getUITextField().getText()));

            updateAllLevelName(reportDefinition);
        }
    }

    private void updateAllLevelName(MultiPieReportDataDefinition definition) {
        if (levelNameList == null){
            return;
        }

        //按顺序添加，顺序代表层级数
        for (int i = 0; i < levelNameList.size(); i++){
            Object levelName = canBeFormula(levelNameList.get(i).getUITextField().getText());
            definition.addLevelName(levelName);
        }
    }

    private void refreshPane() {
        this.validate();
        this.repaint();
        this.revalidate();
    }

    @Override
    public void registerChangeListener(UIObserverListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean shouldResponseChangeListener() {
        return false;
    }
}
