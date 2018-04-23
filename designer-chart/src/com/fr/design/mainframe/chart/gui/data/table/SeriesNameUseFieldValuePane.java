package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.OneValueCDDefinition;
import com.fr.data.util.function.AbstractDataFunction;
import com.fr.data.util.function.NoneFunction;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.CalculateComboBox;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

/**
 * 属性表 数据集 系列名使用 系列值  界面.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-12-26 下午03:29:53
 */
public class SeriesNameUseFieldValuePane extends FurtherBasicBeanPane<ChartCollection> {
    private UIComboBox seriesName;
    private UIComboBox seriesValue;
    private CalculateComboBox calculateCombox;
    private JPanel centerPane;
    private boolean isNeedSummary = true;

    public SeriesNameUseFieldValuePane() {
        seriesName = new UIComboBox();
        seriesValue = new UIComboBox();
        calculateCombox = new CalculateComboBox();
        calculateCombox.reset();
        isNeedSummary = true;
        seriesName.setPreferredSize(new Dimension(100, 75));
        seriesValue.setPreferredSize(new Dimension(100, 75));
        calculateCombox.setPreferredSize(new Dimension(100, 75));
        seriesName.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (seriesName.getSelectedItem() != null) {
                    seriesName.setToolTipText(seriesName.getSelectedItem().toString());
                } else {
                    seriesName.setToolTipText(null);
                }
            }

        });

        seriesValue.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                calculateCombox.setEnabled(seriesValue.getSelectedItem() != null);
                if (seriesValue.getSelectedItem() != null) {
                    seriesValue.setToolTipText(seriesValue.getSelectedItem().toString());
                } else {
                    seriesValue.setToolTipText(null);
                }
            }
        });
        initCenterPane();
    }

    private void initCenterPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = {p, p, p, p, p};
        UILabel Label1 = new UILabel(Inter.getLocText("FR-Chart-Series_Name"));
        Label1.setPreferredSize(new Dimension(ChartDataPane.LABEL_WIDTH, ChartDataPane.LABEL_HEIGHT));
        UILabel Label2 = new UILabel(Inter.getLocText("Chart-Series_Value"));
        Label2.setPreferredSize(new Dimension(ChartDataPane.LABEL_WIDTH, ChartDataPane.LABEL_HEIGHT));
        UILabel Label3 = new UILabel(Inter.getLocText("Chart-Summary_Method"));
        Label3.setPreferredSize(new Dimension(ChartDataPane.LABEL_WIDTH, ChartDataPane.LABEL_HEIGHT));
        Component[][] components = getUseComponent(Label1, Label2, Label3);

        centerPane = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 4, 6);
        centerPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 1));
        this.setLayout(new BorderLayout());
        this.add(centerPane, BorderLayout.CENTER);
    }

    private void initCenterPaneWithOutCaculateSummary(){
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = {p, p, p, p};
        UILabel Label1 = new UILabel(Inter.getLocText("Chart-Series_Name"));
        Label1.setPreferredSize(new Dimension(75, 20));
        UILabel Label2 = new UILabel(Inter.getLocText("Chart-Series_Value"));
        Label2.setPreferredSize(new Dimension(75, 20));
        UILabel Label3 = new UILabel(Inter.getLocText("Chart-Summary_Method"));
        Label3.setPreferredSize(new Dimension(75, 20));
        Component[][] components = getUseComponentWithOutSummary(Label1, Label2, Label3);

        centerPane = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 4, 6);
        centerPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 1));
        this.setLayout(new BorderLayout());
        this.add(centerPane, BorderLayout.CENTER);
    }

    protected Component[][] getUseComponent(UILabel Label1, UILabel Label2, UILabel Label3) {
        return new Component[][]{
                new Component[]{GUICoreUtils.createBorderLayoutPane(new Component[]{seriesName, null, null, Label1, null})},
                new Component[]{GUICoreUtils.createBorderLayoutPane(new Component[]{seriesValue, null, null, Label2, null})},
                new Component[]{GUICoreUtils.createBorderLayoutPane(new Component[]{calculateCombox, null, null, Label3, null})},
        };
    }

    protected Component[][] getUseComponentWithOutFilter(UILabel Label1, UILabel Label2, UILabel Label3) {
        return new Component[][]{
                new Component[]{GUICoreUtils.createBorderLayoutPane(new Component[]{seriesName, null, null, Label1, null})},
                new Component[]{GUICoreUtils.createBorderLayoutPane(new Component[]{seriesValue, null, null, Label2, null})},
                new Component[]{GUICoreUtils.createBorderLayoutPane(new Component[]{calculateCombox, null, null, Label3, null})}
        };
    }

    protected Component[][] getUseComponentWithOutSummary(UILabel Label1, UILabel Label2, UILabel Label3) {
        return new Component[][]{
                new Component[]{GUICoreUtils.createBorderLayoutPane(new Component[]{seriesName, null, null, Label1, null})},
                new Component[]{GUICoreUtils.createBorderLayoutPane(new Component[]{seriesValue, null, null, Label2, null})},

        };
    }

    protected Component[][] getUseComponentWithOutFilterAndSummary(UILabel Label1, UILabel Label2, UILabel Label3) {
        return new Component[][]{
                new Component[]{GUICoreUtils.createBorderLayoutPane(new Component[]{seriesName, null, null, Label1, null})},
                new Component[]{GUICoreUtils.createBorderLayoutPane(new Component[]{seriesValue, null, null, Label2, null})}
        };
    }

    /**
     * 检查box是否已经使用.
     * @param hasUse 是否使用
     */
    public void checkUse(boolean hasUse) {
        seriesName.setEnabled(hasUse);
        seriesValue.setEnabled(hasUse);
        calculateCombox.setEnabled(seriesValue.getSelectedItem() != null);
    }

    /**
     * 数据集变更时, 刷新Box选中项
     * @param list 列表
     */
    public void refreshBoxListWithSelectTableData(List list) {
        refreshBoxItems(seriesName, list);
        refreshBoxItems(seriesValue, list);
    }

    /**
     * 清空所有的box设置
     */
    public void clearAllBoxList(){
        clearBoxItems(seriesName);
        clearBoxItems(seriesValue);
    }

    private void clearBoxItems(UIComboBox box){
        if(box != null){
            box.removeAllItems();
        }
    }

    private boolean boxItemsContainsObject(UIComboBox box,Object item){
        if(box == null){
            return false;
        }

        ComboBoxModel dataModel = box.getModel();
        for (int i = 0; i < dataModel.getSize(); i++) {
            if(ComparatorUtils.equals(dataModel.getElementAt(i),item)){
                return true;
            }
        }
        return false;
    }

    private void refreshBoxItems(UIComboBox box, List list) {
        if (box == null) {
            return;
        }

        Object ob = box.getSelectedItem();
        box.removeAllItems();

        int length = list.size();
        for (int i = 0; i < length; i++) {
            box.addItem(list.get(i));
        }

        box.getModel().setSelectedItem(ob);
    }

    /**
     * 判断界面是否接受
     * @param ob 接受的对象
     * @return 判断是否接受
     */
    public boolean accept(Object ob) {
        ChartCollection collection = (ChartCollection) ob;
        return collection.getSelectedChart().getFilterDefinition() instanceof OneValueCDDefinition;
    }

    /**
     * 界面标题.
     * @return 界面标题
     */
    public String title4PopupWindow() {
        return Inter.getLocText(new String[]{"Use", "Field", "Value"});
    }

    /**
     * 重置
     */
    public void reset() {

    }

    /**
     * 重新布局整个面板
     * @param isNeedSummary 是否需要汇总
     */
    public void relayoutPane(boolean isNeedSummary){
        if(this.isNeedSummary != isNeedSummary){
            this.remove(centerPane);
            if(isNeedSummary){
                initCenterPane();
            }else{
                initCenterPaneWithOutCaculateSummary();
            }
            this.validate();
            this.isNeedSummary = isNeedSummary;
        }
    }

    public void populateBean(ChartCollection ob , boolean isNeedSummary){
        relayoutPane(isNeedSummary);
        TopDefinitionProvider topDefinition = ob.getSelectedChart().getFilterDefinition();
        if (topDefinition instanceof OneValueCDDefinition) {
            OneValueCDDefinition oneDefinition = (OneValueCDDefinition) topDefinition;
            seriesName.setEditable(true);
            seriesName.setSelectedItem(this.boxItemsContainsObject(seriesName,oneDefinition.getSeriesColumnName())
                    ? oneDefinition.getSeriesColumnName() : null);
            seriesName.setEditable(false);
            seriesValue.setEditable(true);
            seriesValue.setSelectedItem(this.boxItemsContainsObject(seriesValue,oneDefinition.getValueColumnName())
                                ? oneDefinition.getValueColumnName() : null);
            seriesValue.setEditable(false);
            if(this.isNeedSummary){
                calculateCombox.populateBean((AbstractDataFunction) oneDefinition.getDataFunction());
            }
        }
    }

    /**
     * 更新界面内容: 字段值
     */
    public void populateBean(ChartCollection ob) {
        populateBean(ob,true);
    }

    /**
     * 保存界面内容 字段值
     */
    public void updateBean(ChartCollection collection) {
        OneValueCDDefinition oneDefinition = createOneValueCDDefinition();

        String seriesName = (String) this.seriesName.getSelectedItem();
        oneDefinition.setSeriesColumnName(seriesName);
        String valueName = (String) this.seriesValue.getSelectedItem();
        oneDefinition.setValueColumnName(valueName);
        if(this.isNeedSummary){
            oneDefinition.setDataFunction(calculateCombox.updateBean());
        }else{
            oneDefinition.setDataFunction(new NoneFunction());
        }
        collection.getSelectedChart().setFilterDefinition(oneDefinition);
    }

    protected OneValueCDDefinition createOneValueCDDefinition(){
        return new OneValueCDDefinition();
    }

    /**
     * 保存界面属性 新的ChartCollection
     */
    public ChartCollection updateBean() {
        return null;
    }

}