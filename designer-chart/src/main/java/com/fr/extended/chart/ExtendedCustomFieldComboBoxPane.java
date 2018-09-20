package com.fr.extended.chart;

import com.fr.base.Utils;
import com.fr.data.util.function.AbstractDataFunction;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.UITable;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.data.CalculateComboBox;
import com.fr.design.mainframe.chart.gui.data.table.DataPaneHelper;
import com.fr.stable.StringUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

/**
 * Created by shine on 2018/9/12.
 */
public class ExtendedCustomFieldComboBoxPane extends UIComboBoxPane<AbstractDataConfig> {
    private static final String[] HEADS = {com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Field_Name"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Series_Name"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Summary_Method")};

    private UseFieldValuePane useFieldValuePane;

    private boolean hasNoneItem = false;

    private CustomFieldNamePane customFieldNamePane;

    private List<String> fieldList = new ArrayList<String>();

    public ExtendedCustomFieldComboBoxPane(boolean hasNoneItem) {
        this.hasNoneItem = hasNoneItem;
    }

    @Override
    protected void initLayout() {
        this.setLayout(new BorderLayout(0, 6));
        JPanel northPane = new JPanel(new BorderLayout());
        northPane.add(jcb, BorderLayout.CENTER);
        UILabel label = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Series_Name_From"));
        label.setPreferredSize(new Dimension(82, 20));
        northPane.add(label, BorderLayout.WEST);
        this.add(northPane, BorderLayout.NORTH);
        this.add(cardPane, BorderLayout.CENTER);
    }

    @Override
    protected List<FurtherBasicBeanPane<? extends AbstractDataConfig>> initPaneList() {
        useFieldValuePane = new UseFieldValuePane();
        customFieldNamePane = new CustomFieldNamePane();
        List<FurtherBasicBeanPane<? extends AbstractDataConfig>> list = new ArrayList<FurtherBasicBeanPane<? extends AbstractDataConfig>>();
        list.add(useFieldValuePane);
        list.add(customFieldNamePane);
        return list;
    }

    @Override
    protected String title4PopupWindow() {
        return StringUtils.EMPTY;
    }

    public void checkBoxUse(boolean hasUse) {
        jcb.setEnabled(hasUse);
        useFieldValuePane.checkBoxUse(hasUse);
    }

    public void clearAllBoxList() {
        useFieldValuePane.clearAllBoxList();
        fieldList.clear();
    }

    protected void refreshBoxListWithSelectTableData(List columnNameList) {
        useFieldValuePane.refreshBoxListWithSelectTableData(columnNameList);
        fieldList = columnNameList;
    }

    @Override
    public void populateBean(AbstractDataConfig ob) {
        if (ob.isCustomName()) {
            customFieldNamePane.populateBean(ob);
            jcb.setSelectedIndex(1);
        } else {
            useFieldValuePane.populateBean(ob);
            jcb.setSelectedIndex(0);
        }
    }

    @Override
    public void updateBean(AbstractDataConfig ob) {
        if (jcb.getSelectedIndex() == 0) {
            ob.setCustomName(false);
            useFieldValuePane.updateBean(ob);
        } else {
            ob.setCustomName(true);
            customFieldNamePane.updateBean(ob);
        }
    }

    private class UseFieldValuePane extends FurtherBasicBeanPane<AbstractDataConfig> {
        private UIComboBox series;
        private UIComboBox value;
        private CalculateComboBox function;

        private UseFieldValuePane() {
            initComponents();
        }

        private void initComponents() {

            series = new UIComboBox();
            value = new UIComboBox();

            if (hasNoneItem) {
                value.addItem(Toolkit.i18nText("Fine-Design_Chart_Use_None"));
            }

            function = new CalculateComboBox();

            Component[][] components = new Component[][]{
                    new Component[]{new UILabel(HEADS[1], SwingConstants.LEFT), series},
                    new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_Value"), SwingConstants.LEFT), value},
                    new Component[]{new UILabel(HEADS[2], SwingConstants.LEFT), function},
            };

            double p = TableLayout.PREFERRED;
            double[] columnSize = {76, 120};
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
            if (hasNoneItem) {
                value.addItem(Toolkit.i18nText("Fine-Design_Chart_Use_None"));
            }
        }

        public void refreshBoxListWithSelectTableData(List columnNameList) {
            DataPaneHelper.refreshBoxItems(series, columnNameList);
            DataPaneHelper.refreshBoxItems(value, columnNameList);
            if (hasNoneItem) {
                value.addItem(Toolkit.i18nText("Fine-Design_Chart_Use_None"));
            }
        }

        @Override
        public void populateBean(AbstractDataConfig ob) {
            List<ExtendedField> list = ob.getCustomFields();
            if (list.size() == 2) {
                series.setSelectedItem(list.get(0).getFieldName());
                value.setSelectedItem(list.get(1).getFieldName());
                function.populateBean((AbstractDataFunction) list.get(1).getDataFunction());
            }
        }

        @Override
        public void updateBean(AbstractDataConfig ob) {
            List<ExtendedField> list = new ArrayList<ExtendedField>();

            list.add(new ExtendedField(Utils.objectToString(series.getSelectedItem())));
            ExtendedField field = new ExtendedField(Utils.objectToString(value.getSelectedItem()));
            field.setDataFunction(function.updateBean());
            list.add(field);

            ob.setCustomFields(list);
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
        public AbstractDataConfig updateBean() {
            return null;
        }
    }

    private class CustomFieldNamePane extends FurtherBasicBeanPane<AbstractDataConfig> {

        private UICorrelationPane correlationPane;

        public CustomFieldNamePane() {
            initComponents();
        }

        private void initComponents() {

            correlationPane = new UICorrelationPane(HEADS) {
                @Override
                protected ActionListener getAddButtonListener() {
                    return new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            tablePane.addLine(new String[]{StringUtils.EMPTY, StringUtils.EMPTY, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_None")});
                            fireTargetChanged();
                        }
                    };
                }

                public UITableEditor createUITableEditor() {
                    return new Editor() {
                        @Override
                        protected UICorrelationPane getParent() {
                            return correlationPane;
                        }
                    };
                }
            };

            this.setLayout(new BorderLayout());
            this.add(correlationPane, BorderLayout.CENTER);

        }

        @Override
        public void populateBean(AbstractDataConfig ob) {
            List<ExtendedField> customFields = ob.getCustomFields();

            List<Object[]> list = new ArrayList<Object[]>();
            for (ExtendedField field : customFields) {
                String[] array = {field.getFieldName(), field.getCustomName(), DataPaneHelper.getFunctionString(field.getDataFunction())};
                list.add(array);
            }
            correlationPane.populateBean(list);
        }


        @Override
        public void updateBean(AbstractDataConfig ob) {
            List<Object[]> list = correlationPane.updateBean();

            List<ExtendedField> customFields = new ArrayList<ExtendedField>();
            for (Object[] line : list) {
                ExtendedField field = new ExtendedField(Utils.objectToString(line[0]));
                field.setCustomName(Utils.objectToString(line[1]));
                if (line.length > 2) {
                    field.setDataFunction(DataPaneHelper.getFunctionByName(Utils.objectToString(line[2])));
                }
                customFields.add(field);
            }

            ob.setCustomFields(customFields);

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
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Enable_Field_Name");
        }

        @Override
        public AbstractDataConfig updateBean() {
            return null;
        }
    }

    private abstract class Editor extends UITableEditor {
        private JComponent editorComponent;

        protected abstract UICorrelationPane getParent();

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
                    editorComponent = createComboBoxEdit(row, value);
                    break;
                case 1:
                    editorComponent = createTextEdit(value);
                    break;
                default:
                    editorComponent = createCalculateComboBox(value);
                    break;

            }
            return editorComponent;
        }

        private void setDefaultName(int row) {
            UITable table = getParent().getTable();
            Object object = table.getValueAt(row, 0);
            if (object != null) {
                table.setValueAt(object, row, 1);
            }
        }

        private UIComboBox createComboBoxEdit(final int row, Object value) {
            UIComboBox uiComboBox = new UIComboBox(fieldList.toArray());

            uiComboBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    getParent().stopCellEditing();
                    getParent().fireTargetChanged();
                    setDefaultName(row);
                }
            });

            if (value != null && StringUtils.isNotEmpty(value.toString())) {
                uiComboBox.getModel().setSelectedItem(value);
            } else {
                uiComboBox.getModel().setSelectedItem(value);
            }

            return uiComboBox;
        }

        private UITextField createTextEdit(Object value) {
            UITextField uiTextField = new UITextField();
            if (value != null) {
                uiTextField.setText(value.toString());
            }

            uiTextField.registerChangeListener(new UIObserverListener() {
                @Override
                public void doChange() {
                    getParent().fireTargetChanged();
                }
            });

            return uiTextField;
        }

        private CalculateComboBox createCalculateComboBox(Object value) {
            CalculateComboBox calculateComboBox = new CalculateComboBox();
            if (value != null) {
                calculateComboBox.setSelectedItem(value);
            }
            calculateComboBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    getParent().stopCellEditing();
                    getParent().fireTargetChanged();
                }
            });
            return calculateComboBox;
        }
    }

}
