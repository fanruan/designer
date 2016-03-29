package com.fr.design.mainframe;

import com.fr.base.TableData;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.JSONTableData;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.data.impl.ExcelTableData;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.*;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 * Date: 14/12/3
 * Time: 下午6:53
 */
public class AbstractChartDataPane4Chart extends DataContentsPane implements UIObserver {

    private static final int DATA_SOURCE_GAP = 18;
    private static final int WIDTH = 262;

    protected ChartDataPane parentPane;
    protected ChartDesignDataLoadPane choosePane;
    protected JComponent choose = new UILabel();

    protected UIObserverListener observerListener;

    protected UIComboBox dataSource = new UIComboBox(
            new String[]{"JSON" + Inter.getLocText("Chart-DS_TableData"), Inter.getLocText("Chart-Use_Local") + "EXCEL", Inter.getLocText("Chart-DS_Embedded_TableData")});

    protected ItemListener dsListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            int index = dataSource.getSelectedIndex();
            if (index == 0) {
                initJSON();
            } else if (index == 1) {
                initExcel();
            } else {
                initEmbbed();
            }
            remove(leftContentPane);
            clearTableDataSetting();
            initContentPane();
            AbstractChartDataPane4Chart.this.validate();
        }
    };

    protected AttributeChangeListener attributeChangeListener;

    public AbstractChartDataPane4Chart(final AttributeChangeListener listener, ChartDataPane parent) {
        this.parentPane = parent;
        this.attributeChangeListener = listener;
        initJSON();
        initAll();
    }

    protected void populateChoosePane(TableData tableData) {
        dataSource.removeItemListener(dsListener);
        if (tableData instanceof JSONTableData) {
            initJSON();
            dataSource.setSelectedIndex(0);
        } else if (tableData instanceof ExcelTableData) {
            initExcel();
            dataSource.setSelectedIndex(1);
        } else if (tableData instanceof EmbeddedTableData) {
            initEmbbed();
            dataSource.setSelectedIndex(2);
        }
        choosePane.populateChartTableData(tableData);
        dataSource.addItemListener(dsListener);
    }


    protected void initJSON() {
        choosePane = new JSONDataPane(this);
        UILabel url = new UILabel("URL:");
        url.setHorizontalAlignment(SwingConstants.RIGHT);
        choose = url;
    }

    protected void initExcel() {
        choose = new UIButton(Inter.getLocText("Chart-Select_Path"));
        choosePane = new ExcelDataPane(this, choose);
    }

    protected void initEmbbed() {
        choosePane = new EmbbeddDataPane(this);
        choose = null;
    }

    @Override
    public void setSupportCellData(boolean supportCellData) {

    }

    @Override
    protected JPanel createContentPane() {
        double p = TableLayout.PREFERRED;
        double[] columnSize = {WIDTH};
        double[] rowSize = {p, p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Chart-Data_Import"))},
                new Component[]{createDataImportPane()}
        };

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    private JPanel createDataImportPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = {p, p, p};
        Component[][] components = new Component[][]{
                new Component[]{createDataSourcePane()},
                new Component[]{new JSeparator()},
                new Component[]{createDataSetPane()}
        };

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }


    /**
     * 数据配置面板
     *
     * @return
     */
    protected JPanel createDataSetPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = {p, p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Chart-Data_Configuration"))},
                new Component[]{getDataContentPane()}
        };

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    protected JPanel getDataContentPane() {
        return new JPanel();
    }


    private JPanel createDataSourcePane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {DATA_SOURCE_GAP, f};
        double[] rowSize = {p,};
        Component[][] components = new Component[][]{
                new Component[]{null, createChooseBoxPane()},
        };
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    private JPanel createChooseBoxPane() {
        UILabel dataSourceLabel = new UILabel(Inter.getLocText("Chart-Data_Resource") + ":");
        dataSourceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p};
        Component[][] components = new Component[][]{
                new Component[]{dataSourceLabel, dataSource},
                new Component[]{choose, choosePane}
        };
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }


    /**
     * 注册观察者监听事件
     *
     * @param listener 观察者监听事件
     */
    public void registerChangeListener(UIObserverListener listener) {
        this.observerListener = listener;
    }

    /**
     * 使用应该响应listener
     *
     * @return 应该响应
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }


    protected void initSelfListener(Container parentComponent) {
        for (int i = 0; i < parentComponent.getComponentCount(); i++) {
            Component tmpComp = parentComponent.getComponent(i);
            if (tmpComp instanceof Container) {
                initListener((Container) tmpComp);
            }
            if (tmpComp instanceof UIObserver) {
                ((UIObserver) tmpComp).registerChangeListener(observerListener);
            }
        }
    }


    @Override
    public void populate(ChartCollection collection) {

    }

    @Override
    public void update(ChartCollection collection) {

    }

    /**
     * 清空数据集的设置
     */
    public void clearTableDataSetting() {

    }

    /**
     * 数据集数据改变
     */
    public void fireTableDataChange() {

    }
}