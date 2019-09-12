package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.base.Utils;
import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.ChartSummaryColumn;
import com.fr.chart.chartdata.MoreNameCDDefinition;
import com.fr.data.util.function.NoneFunction;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.itable.UITable;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.data.CalculateComboBox;
import com.fr.stable.StringUtils;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 属性表 数据集界面: 系列名 使用字段名.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-12-26 下午04:39:46
 */
public class SeriesNameUseFieldNamePane extends FurtherBasicBeanPane<ChartCollection> {
    private static final String[] HEADS = {com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Field_Name"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Series_Name"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Summary_Method")};
    private static final String[] HEADS_NO_SUMMARY = {com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Field_Name"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Series_Name")};
    private UICorrelationPane seriesDataPane;
    private List<String> field = new ArrayList<String>();
    private JPanel centerPane;
    private boolean isNeedSummary = true;
    private UIObserverListener observerListener;


    public SeriesNameUseFieldNamePane() {
        isNeedSummary = true;
        initCenterPane(HEADS);
    }

    private void initCenterPane(final String[] heads) {
        seriesDataPane = new UICorrelationPane(heads) {

            @Override
            protected ActionListener getAddButtonListener() {
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String[] blank =heads.length == 3?new String[]{StringUtils.EMPTY, StringUtils.EMPTY, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_None")}:
                                new String[]{StringUtils.EMPTY, StringUtils.EMPTY};
                        tablePane.addLine(blank);
                        fireTargetChanged();
                    }
                };
            }

            public UITableEditor createUITableEditor() {
                return new InnerTableEditor();
            }

            public void stopPaneEditing(ChangeEvent e) {
                fireTargetChanged();
            }

            public void registerChangeListener(UIObserverListener listener) {
                super.registerChangeListener(listener);
                observerListener = listener;
            }

        };
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = {p, p};
        Component[][] components = getUseComponent();
        centerPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(centerPane, BorderLayout.CENTER);
    }

    protected Component[][] getUseComponent() {
        return new Component[][]{
                new Component[]{seriesDataPane},
        };
    }

    protected Component[][] getUseComponentWithOutFilter() {
        return new Component[][]{
                new Component[]{seriesDataPane}
        };
    }

    /**
     * 判断是都接受
     * @param ob 传过来得
     * @return 是则返回true
     */
    public boolean accept(Object ob) {
        ChartCollection collection = (ChartCollection) ob;
        return collection.getSelectedChart().getFilterDefinition() instanceof MoreNameCDDefinition;
    }

    /**
     * 标题
     * @return 标题
     */
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Enable_Field_Name");
    }

    /**
     * 重置.
     */
    public void reset() {

    }


    /**
     * 数据集变更时
     * @param list  数据集
     */
    public void refreshBoxListWithSelectTableData(List list) {
        field.clear();

        for (int i = 0, length = list.size(); i < length; i++) {
            Object ob = list.get(i);
            if (ob != null) {
                field.add(ob.toString());
            }
        }
    }

    /**
     * 清空所有的box设置
     */
    public void clearAllBoxList(){
        field.clear();
    }

    private void fireStop() {
        seriesDataPane.stopCellEditing();
        seriesDataPane.fireTargetChanged();
    }

    /**
     * 重新布局面板
     * @param isNeedSummary 是都需要汇总（图表设计器得时候不需要）
     */
    public void relayoutPane(boolean isNeedSummary){
        if(this.isNeedSummary != isNeedSummary){
            this.remove(centerPane);
            initCenterPane(isNeedSummary?HEADS:HEADS_NO_SUMMARY);
            initListener(this);
            this.validate();
            this.isNeedSummary = isNeedSummary;
        }
    }

    private void initListener(Container parentComponent) {
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

    public void populateBean(ChartCollection collection,boolean isNeedSummary){
        relayoutPane(isNeedSummary);
        TopDefinitionProvider topDefinition = collection.getSelectedChart().getFilterDefinition();
        if (topDefinition instanceof MoreNameCDDefinition) {
            MoreNameCDDefinition moreDefinition = (MoreNameCDDefinition) topDefinition;
            ChartSummaryColumn[] chartSummaryColumnArray = moreDefinition.getChartSummaryColumn();
            if (chartSummaryColumnArray == null || chartSummaryColumnArray.length == 0) {
                return;
            }
            List<Object[]> list = new ArrayList<Object[]>();
            for (int i = 0; i < chartSummaryColumnArray.length; i++) {
                ChartSummaryColumn column = chartSummaryColumnArray[i];
                String[] nameArray = {column.getName(), column.getCustomName(), DataPaneHelper.getFunctionString(column.getFunction())};
                list.add(nameArray);
            }
            seriesDataPane.populateBean(list);
        }
    }

    /**
     * 根据ChartCollection 更新界面
     */
    public void populateBean(ChartCollection collection) {
        this.populateBean(collection,true);
    }

    protected MoreNameCDDefinition createMoreNameCDDefinition() {
        return new MoreNameCDDefinition();
    }

    /**
     * 保存界面属性到ChartCollection
     */
    public void updateBean(ChartCollection collection) {
        TopDefinitionProvider normalDefinition = collection.getSelectedChart().getFilterDefinition();
        MoreNameCDDefinition moreDefinition = null;
        if (normalDefinition instanceof MoreNameCDDefinition) {
            moreDefinition = (MoreNameCDDefinition) normalDefinition;
        } else {
            moreDefinition = createMoreNameCDDefinition();
        }

        List<Object[]> data = seriesDataPane.updateBean();
        ChartSummaryColumn[] dataArray = new ChartSummaryColumn[data.size()];
        for (int i = 0; i < dataArray.length; i++) {
            Object[] line = data.get(i);
            String first = Utils.objectToString(line[0]);
            String second = Utils.objectToString(line[1]);
            if(isNeedSummary){
                String third = Utils.objectToString(line[2]);
                dataArray[i] = new ChartSummaryColumn(first, second, DataPaneHelper.getFunctionByName(third));
            } else{
                dataArray[i] = new ChartSummaryColumn(first,second,new NoneFunction());
            }
        }
        moreDefinition.setChartSummaryColumn(dataArray);
        collection.getSelectedChart().setFilterDefinition(moreDefinition);
    }

    /**
     * 保存为新的ChartCollection
     */
    public ChartCollection updateBean() {
        return null;
    }

    private void checkRow(int row) {
        UITable table = seriesDataPane.getTable();
        Object object = table.getValueAt(row, 0);
        if (object != null) {
            table.setValueAt(object, row, 1);
        }
    }

    private class InnerTableEditor extends UITableEditor {
        private JComponent editorComponent;

        @Override
        public Object getCellEditorValue() {
            if (editorComponent instanceof UIComboBox) {
                return ((UIComboBox) editorComponent).getSelectedItem();
            } else if (editorComponent instanceof UITextField) {
                return ((UITextField) editorComponent).getText();
            } else if (editorComponent instanceof CalculateComboBox) {
                return ((CalculateComboBox) editorComponent).getSelectedItem();
            }
            return super.getCellEditorValue();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, final int row, int column) {

            switch (column) {
                case 0:
                    return createComboxEdit(row, value);
                case 1:
                    return createTextEdit(value);
                default:
                    CalculateComboBox calculateComboBox = new CalculateComboBox();
                    if (value != null) {
                        calculateComboBox.setSelectedItem(value);
                    }
                    calculateComboBox.addItemListener(new ItemListener() {
                        public void itemStateChanged(ItemEvent e) {
                            fireStop();
                        }
                    });
                    editorComponent = calculateComboBox;
                    return calculateComboBox;
            }
        }

        private UIComboBox createComboxEdit(final int row, Object value) {
            UIComboBox uiComboBox = new UIComboBox(field.toArray());

            uiComboBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    fireStop();
                    checkRow(row);
                    seriesDataPane.fireTargetChanged();
                }
            });
            editorComponent = uiComboBox;

            uiComboBox.getModel().setSelectedItem(value);

            return uiComboBox;
        }

        private UITextField createTextEdit(Object value) {
            UITextField uiTextField = new UITextField();
            editorComponent = uiTextField;
            if (value != null) {
                uiTextField.setText(value.toString());
            }

            uiTextField.registerChangeListener(new UIObserverListener() {
                @Override
                public void doChange() {
                    seriesDataPane.fireTargetChanged();// kunsnat: 不能停止编辑, 因为输入会中断.
                }
            });

            return uiTextField;
        }
    }


}