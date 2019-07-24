package com.fr.design.chartx.component;

import com.fr.data.util.function.AbstractDataFunction;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.chartx.component.correlation.AbstractCorrelationPane;
import com.fr.design.chartx.component.correlation.CalculateComboBoxEditorComponent;
import com.fr.design.chartx.component.correlation.FieldEditorComponentWrapper;
import com.fr.design.chartx.component.correlation.UIComboBoxEditorComponent;
import com.fr.design.chartx.component.correlation.UITextFieldEditorComponent;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.CalculateComboBox;
import com.fr.design.mainframe.chart.gui.data.table.DataPaneHelper;
import com.fr.extended.chart.UIComboBoxWithNone;
import com.fr.general.GeneralUtils;
import com.fr.stable.StringUtils;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shine on 2018/9/12.
 * 系列名使用字段名or字段值的抽象的pane 支持多种属性结构的存取
 */
public abstract class AbstractCustomFieldComboBoxPane<T> extends UIComboBoxPane<T> {

    private AbstractUseFieldValuePane useFieldValuePane;

    private AbstractCustomFieldNamePane customFieldNamePane;

    private List<String> fieldList = new ArrayList<String>();

    @Override
    protected void initLayout() {
        this.setLayout(new BorderLayout(0, 6));
        JPanel northPane = new JPanel(new BorderLayout());
        northPane.add(jcb, BorderLayout.CENTER);
        UILabel label = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Series_Name_From"));
        label.setPreferredSize(new Dimension(ChartDataPane.LABEL_WIDTH, 20));
        northPane.add(label, BorderLayout.WEST);
        this.add(northPane, BorderLayout.NORTH);
        this.add(cardPane, BorderLayout.CENTER);
    }

    @Override
    protected List<FurtherBasicBeanPane<? extends T>> initPaneList() {
        useFieldValuePane = createUseFieldValuePane();
        customFieldNamePane = createCustomFieldNamePane();
        List<FurtherBasicBeanPane<? extends T>> list = new ArrayList<FurtherBasicBeanPane<? extends T>>();
        list.add(useFieldValuePane);
        list.add(paneWrapper());
        return list;
    }

    private FurtherBasicBeanPane<? extends T> paneWrapper() {
        FurtherBasicBeanPane pane = new FurtherBasicBeanPane() {
            @Override
            public String title4PopupWindow() {
                return Toolkit.i18nText("Fine-Design_Chart_Enable_Field_Name");
            }

            @Override
            public boolean accept(Object ob) {
                return false;
            }

            @Override
            public void reset() {
            }

            @Override
            public void populateBean(Object ob) {
            }

            @Override
            public Object updateBean() {
                return null;
            }
        };
        pane.setLayout(new BorderLayout(0, 6));
        pane.add(customFieldNamePane, BorderLayout.CENTER);
        return pane;
    }

    protected abstract AbstractUseFieldValuePane createUseFieldValuePane();

    protected abstract AbstractCustomFieldNamePane createCustomFieldNamePane();

    @Override
    protected String title4PopupWindow() {
        return StringUtils.EMPTY;
    }

    protected boolean valueComboBoxHasNone() {
        return false;
    }

    public void checkBoxUse(boolean hasUse) {
        jcb.setEnabled(hasUse);
        useFieldValuePane.checkBoxUse(hasUse);
    }

    public void clearAllBoxList() {
        useFieldValuePane.clearAllBoxList();
        fieldList.clear();
    }

    public void refreshBoxListWithSelectTableData(List columnNameList) {
        useFieldValuePane.refreshBoxListWithSelectTableData(columnNameList);
        fieldList = columnNameList;
    }

    protected void populateCustomFieldNamePane(T t) {
        customFieldNamePane.populateBean(t);
    }

    protected void updateCustomFieldNamePane(T t) {
        customFieldNamePane.updateBean(t);
    }

    protected void populateUseFieldValuePane(T t) {
        useFieldValuePane.populateBean(t);
    }

    protected void updateUseFieldValuePane(T t) {
        useFieldValuePane.updateBean(t);
    }

    protected abstract class AbstractUseFieldValuePane extends FurtherBasicBeanPane<T> {
        private UIComboBox series;
        private UIComboBox value;
        private CalculateComboBox function;

        public AbstractUseFieldValuePane() {
            initComponents();
        }

        private void initComponents() {

            series = new UIComboBox();
            value = valueComboBoxHasNone() ? new UIComboBoxWithNone() : new UIComboBox();

            function = new CalculateComboBox();

            Component[][] components = new Component[][]{
                    new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Series_Name"), SwingConstants.LEFT), series},
                    new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Use_Value"), SwingConstants.LEFT), value},
                    new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Summary_Method"), SwingConstants.LEFT), function},
            };

            double p = TableLayout.PREFERRED;
            double[] columnSize = {78, 122};
            double[] rowSize = {p, p, p};

            JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);

            this.setLayout(new BorderLayout(0, 6));
            this.add(panel, BorderLayout.CENTER);
        }

        public void checkBoxUse(boolean hasUse) {
            series.setEnabled(hasUse);
            value.setEnabled(hasUse);
            function.setEnabled(hasUse);
        }

        public void clearAllBoxList() {
            DataPaneHelper.clearBoxItems(series);
            DataPaneHelper.clearBoxItems(value);
        }

        public void refreshBoxListWithSelectTableData(List columnNameList) {
            DataPaneHelper.refreshBoxItems(series, columnNameList);
            DataPaneHelper.refreshBoxItems(value, columnNameList);
        }

        protected void populateSeries(String item) {
            series.setSelectedItem(item);
        }

        protected void populateValue(String item) {
            value.setSelectedItem(item);
        }

        protected void populateFunction(AbstractDataFunction _function) {
            function.populateBean(_function);
        }

        protected String updateSeries() {
            return GeneralUtils.objectToString(series.getSelectedItem());
        }

        protected String updateValue() {
            return GeneralUtils.objectToString(value.getSelectedItem());
        }

        protected AbstractDataFunction updateFunction() {
            return function.updateBean();
        }

        @Override
        public boolean accept(Object ob) {
            return true;
        }

        @Override
        public void reset() {
        }

        @Override
        public String title4PopupWindow() {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Enable_Field_Value");
        }

        @Override
        public T updateBean() {
            return null;
        }
    }

    protected abstract class AbstractCustomFieldNamePane extends AbstractCorrelationPane<T> {

        @Override
        protected FieldEditorComponentWrapper[] createFieldEditorComponentWrappers() {
            return new FieldEditorComponentWrapper[]{
                    new UIComboBoxEditorComponent(Toolkit.i18nText("Fine-Design_Chart_Field_Name")) {
                        @Override
                        protected List<String> items() {
                            return fieldList;
                        }
                    },
                    new UITextFieldEditorComponent(Toolkit.i18nText("Fine-Design_Chart_Series_Name")),
                    new CalculateComboBoxEditorComponent(Toolkit.i18nText("Fine-Design_Chart_Summary_Method"))
            };
        }

        @Override
        protected Object[] createLine() {
            return new String[]{StringUtils.EMPTY, StringUtils.EMPTY, Toolkit.i18nText("Fine-Design_Chart_Use_None")};
        }
    }
}
