package com.fr.van.chart.custom.component;

import com.fr.chart.chartattr.Chart;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;

import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.attr.plot.VanChartRectanglePlot;
import com.fr.plugin.chart.custom.CustomPlotFactory;
import com.fr.plugin.chart.custom.VanChartCustomPlot;
import com.fr.plugin.chart.custom.type.CustomPlotType;
import com.fr.plugin.chart.custom.type.CustomStyle;
import com.fr.van.chart.custom.CustomPlotDesignerPaneFactory;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fangjie on 2016/4/19.
 */
public class VanChartCustomPlotSelectPane extends BasicBeanPane<Chart> {

    private static final int NO_DIRTY = -1;
    private static final int NOT_LAST = -1;
    private static final int REC_WIDTH = 58;
    private static final int REC_HEIGHT = 50;
    private static final int CUSTOM_TYPE_NUM = 4;


    public static final String MASSAGE = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_At_Least_One_Chart");

    private List<ChartImageCheckOutPane> customTypeList = new ArrayList<ChartImageCheckOutPane>();

    //该列表记录上次每个checkout的状态
    private List<Boolean> oldSelectedList;

    //保存点击的checkout顺序
    private List<CustomPlotType> selectedPlotTypeList = new ArrayList<CustomPlotType>();

    //将所有面板排布后的面板
    private JPanel content;


    public VanChartCustomPlotSelectPane(){
        initCustomType();
    }

    private void initCustomType() {

        initAllCheckOutPane();

        initContentPane();

        updateOldSelectedList();

        addCheckBoxListener();

        checkoutSelected();
    }

    private void updateOldSelectedList() {
        oldSelectedList = new ArrayList<Boolean>();
        for (int i = 0; i < customTypeList.size(); i++){
            oldSelectedList.add(customTypeList.get(i).getCheckBox().isSelected());
        }
    }

    private void checkoutSelected() {
        for (int i = 0; i < customTypeList.size(); i++){
            //根据是否选中重置背景
            customTypeList.get(i).checkIconImage();
        }
    }

    private void initContentPane() {

        content = FRGUIPaneFactory.createNColumnGridInnerContainer_Pane(CUSTOM_TYPE_NUM, 0, 0);
        for (int i = 0; i < customTypeList.size(); i++){
            customTypeList.get(i).setPreferredSize(new Dimension(REC_WIDTH,REC_HEIGHT));

            //是否画右边线
            boolean isRightLine = (i == customTypeList.size() - 1) ? true : ((i + 1)%CUSTOM_TYPE_NUM == 0) ? true : false;
            //是否画下边线
            int row = i / CUSTOM_TYPE_NUM;
            int column = i % CUSTOM_TYPE_NUM;
            boolean isBottomLine = ((row+1)*CUSTOM_TYPE_NUM + column < customTypeList.size()) ? false : true;

            customTypeList.get(i).setPaneBorder(isRightLine, isBottomLine);

            content.add(customTypeList.get(i));
        }

        this.setLayout(new BorderLayout());
        this.add(content, BorderLayout.CENTER);
    }

    private void addCheckBoxListener() {
        for (int i = 0; i < customTypeList.size(); i++){

            //初始化每个customType的listener

            customTypeList.get(i).getCheckBox().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    int index = isLastSelected();
                    if (index != NOT_LAST) {//如果是最后一个
                        customTypeList.get(index).getCheckBox().setSelected(true);

                        //彈出警告框
                        FineJOptionPane.showMessageDialog(new JPanel(), MASSAGE,com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Alert"), JOptionPane.WARNING_MESSAGE);
                    }

                    //点击后更新已选顺序
                    checkoutSelectedPlotTypeList();

                    //点击后，更新背景
                    checkoutSelected();

                    //响应观察者事件，以便更新chart
                    fireDirty();
                }
            });
        }
    }


    /**
     * 根据选择更新顺序
     */
    private void checkoutSelectedPlotTypeList() {
        int index = getDirtySelectedIndex(oldSelectedList);

        if (index == NO_DIRTY){
            return;
        }

        boolean isAdd = getDirtyState(index);

        //获取选中的自定义图表类型
        CustomPlotType plotType = customTypeList.get(index).getCustomPlotType();

        //根据类型处理增加或者删除
        dealSelectedPlotTypeList(isAdd, plotType, selectedPlotTypeList);
    }

    private void fireDirty() {
        int index = getDirtySelectedIndex(oldSelectedList);
        if (index != NO_DIRTY) {
            customTypeList.get(index).fireStateChange();
        }
    }

    @Override
    public void populateBean(Chart chart) {

        VanChartCustomPlot customPlot = (VanChartCustomPlot) chart.getPlot();

        populateCustomTypeList(customPlot);

        populateSelectedPlotTypeList(customPlot);

        checkoutSelected();
    }

    private void populateSelectedPlotTypeList(VanChartCustomPlot customPlot) {

        selectedPlotTypeList = new ArrayList<CustomPlotType>();

        List<VanChartPlot> customPlotList = customPlot.getCustomPlotList();

        for (int i = 0; i < customPlotList.size(); i++){
            selectedPlotTypeList.add(CustomPlotFactory.getCustomType(customPlotList.get(i)));
        }
    }

    private void populateCustomTypeList(VanChartCustomPlot customPlot) {
        if (customPlot.getCustomStyle() != CustomStyle.CUSTOM){
            return;
        }

        List<VanChartPlot> customPlotList =  customPlot.getCustomPlotList();

        oldSelectedList = new ArrayList<Boolean>();

        for (int i = 0; i < customTypeList.size(); i++){
            boolean isSelected = false;
            CustomPlotType customPlotType = customTypeList.get(i).getCustomPlotType();
            //更新选中项
            if (customPlotTypeContained(customPlotType, customPlotList)) {
                isSelected = true;
            }

            customTypeList.get(i).setSelected(isSelected);

            //同时设置oldList
            oldSelectedList.add(isSelected);
        }

    }

    private boolean customPlotTypeContained(CustomPlotType customPlotType, List<VanChartPlot> customPlotList) {
        boolean contained = false;

        for (int i = 0; i < customPlotList.size(); i++){
            if (ComparatorUtils.equals(customPlotType, CustomPlotFactory.getCustomType(customPlotList.get(i)))){
                contained = true;
                break;
            }
        }

        return contained;
    }

    @Override
    public Chart updateBean() {
        return null;
    }

    @Override
    public void updateBean(Chart chart) {
        VanChartCustomPlot customPlot = (VanChartCustomPlot)chart.getPlot();

        //根据选择的组合图更新,根据保存的顺序更新组合图
        updateCustomPlotList(customPlot);

        //响应完毕后，更新状态
        updateOldSelectedList();
    }

    private void updateCustomPlotList(VanChartCustomPlot customPlot) {
        //根据selectedPlotTypeList更新customPlotList
        //切换之后，给与全新构建的plotList，这回导致其他面板上的设置无效，也是合理的
        List<VanChartPlot> newCustomPlotList = new ArrayList<VanChartPlot>();
        List<VanChartPlot> oldCustomPlotList = customPlot.getCustomPlotList();

        //复制已经设置的plot
        try {
            for (int i = 0; i < oldCustomPlotList.size(); i++){
                VanChartPlot vanChartPlot = oldCustomPlotList.get(i);
                if (selectedPlotTypeList.contains(CustomPlotFactory.getCustomType(vanChartPlot))){
                    newCustomPlotList.add((VanChartPlot) vanChartPlot.clone());
                }
            }
        } catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }


        //没有复制到的plot重创
        for (int i = 0; i < selectedPlotTypeList.size(); i++){
            CustomPlotType customPlotType = selectedPlotTypeList.get(i);
            if (!CustomPlotFactory.customPlotContains(newCustomPlotList, customPlotType)){
                VanChartPlot vanChartPlot = CustomPlotFactory.getCustomPlot(customPlotType);
                vanChartPlot.setCustomType(CustomStyle.CUSTOM.toString());
                //设置公共属性
                setCommonAttr(vanChartPlot, customPlot);

                newCustomPlotList.add(vanChartPlot);
            }
        }

        customPlot.setCustomPlotList(newCustomPlotList);
    }

    private void setCommonAttr(VanChartPlot vanChartPlot, VanChartCustomPlot customPlot) {
        //坐标轴公共属性
        dealAxisAttr(vanChartPlot, customPlot);
    }

    private void dealAxisAttr(VanChartPlot vanChartPlot, VanChartCustomPlot customPlot) {
        if (vanChartPlot.isHaveAxis() && !CustomPlotDesignerPaneFactory.isUseDiffAxisPane(vanChartPlot) && customPlot.isHaveStandardAxis()){
            VanChartRectanglePlot rectanglePlot = (VanChartRectanglePlot)vanChartPlot;

            //指针指向同一个标准坐标轴
            rectanglePlot.setXAxisList(customPlot.getXAxisList());
            rectanglePlot.setYAxisList(customPlot.getYAxisList());
        }
    }

    private void dealSelectedPlotTypeList(boolean isAdd, CustomPlotType plotType, List<CustomPlotType> customPlotTypeList) {

        //根据isAdd删除或者增加
        if (isAdd){
            customPlotTypeList.add(plotType);
        }else {
            //定位选中的是那个plot
            int index = -1;

            for (int i = 0; i < customPlotTypeList.size(); i++){
                if (ComparatorUtils.equals(plotType, customPlotTypeList.get(i))){
                    index = i;
                    break;
                }
            }

            if (index == -1){
                return;
            }

            customPlotTypeList.remove(index);
        }

    }

    /**
     * 获取id为index的图表是选中还是删除
     * 增加则返回true
     * 删除返回false
     * @param index
     * @return
     */
    private boolean getDirtyState(int index) {
        boolean isAdd = true;

        boolean newState = customTypeList.get(index).getCheckBox().isSelected();
        boolean oldState = oldSelectedList.get(index);

        //true->false为删除;false->true为增加
        if (oldState == true && newState == false){
            isAdd = false;
        }else if (oldState == false && newState == true){
            isAdd = true;
        }

        return isAdd;
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }

    /**
     * 如果是最后一个，则返回最后一个checkout的index
     * 否则返回-1
     * @return
     */
    private int isLastSelected() {
        int count = 0;
        int index = NOT_LAST;
        for (int i = 0; i < customTypeList.size(); i++){
            if (customTypeList.get(i).getCheckBox().isSelected()){
                count++;
            }
        }
        if (count == 0){
            //查找是哪一个变化了
            index = getDirtySelectedIndex(oldSelectedList);
        }
        return index;
    }

    /**
     * 获取改变状态的index
     * 如果有两个都改变了，改函数获取的是排在前面的index
     * 如果没找到
     * @param oldSelectedList
     * @return
     */
    private int getDirtySelectedIndex(List<Boolean> oldSelectedList) {
        for (int i = 0; i < oldSelectedList.size(); i++){
            if ((oldSelectedList.get(i)) != customTypeList.get(i).getCheckBox().isSelected()){
                return i;
            }
        }
        return NO_DIRTY;
    }

    /**
     * 构建所有可用的组合图面板
     */
    private void initAllCheckOutPane() {
        //将所有工厂中的图表都加入到可选面板中

        CustomPlotType[] customPlotTypes = CustomPlotType.getTypes();
        for(int i = 0; i < customPlotTypes.length; i++){
            boolean isSelected = false;
            CustomPlotType customPlotType = customPlotTypes[i];
            //前两个默认选中
            if (i == 0 || i == 1) {
                isSelected = true;
                selectedPlotTypeList.add(customPlotType);
            }
            ChartImageCheckOutPane checkOutPane = new ChartImageCheckOutPane(customPlotType, isSelected);

            customTypeList.add(checkOutPane);
        }
    }

}
