package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.chartattr.ChartCommonCondition;
import com.fr.data.condition.CommonCondition;
import com.fr.data.core.Compare;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.LiteConditionPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.formula.CustomVariableResolver;
import com.fr.design.formula.VariableResolver;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.ComparatorUtils;
import com.fr.plugin.chart.type.ConditionKeyType;
import com.fr.stable.StringUtils;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

public class ChartConditionPane extends LiteConditionPane<CommonCondition> {

    public ChartConditionPane() {
        super();
        conditonTypePane.setVisible(false);
    }

    @Override
    protected VariableResolver variableResolver4FormulaPane() {
        return new CustomVariableResolver(new String[]{}, false);
    }

    protected ConditionKeyType[] conditionKeyTypes() {
        return ConditionKeyType.NORMAL_CONDITION_KEY_TYPES;
    }

    @Override
    protected BasicBeanPane<CommonCondition> createUnFormulaConditionPane() {
        return new CommonConditionPane();
    }


    private class CommonConditionPane extends BasicBeanPane<CommonCondition> {

        private UIComboBox conditionKeyComboBox;
        private UIComboBox conditionOPComboBox;
        private ValueEditorPane conditionValuePane;

        public CommonConditionPane() {
            initComponents();
        }

        private void initComponents() {
            this.setLayout(FRGUIPaneFactory.createBorderLayout());
            conditionKeyComboBox = new UIComboBox(conditionKeyTypes());
            conditionOPComboBox = new UIComboBox(new DefaultComboBoxModel());
            DefaultComboBoxModel opComboBoxModel = (DefaultComboBoxModel) conditionOPComboBox.getModel();
            int[] allOperators = Compare.getAllOperators();
            for (int i = 0; i < allOperators.length; i++) {
                opComboBoxModel.addElement(new Integer(allOperators[i]));
            }
            this.conditionOPComboBox.setRenderer(new UIComboBoxRenderer() {

                @Override
                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Integer) {
                        this.setText(Compare.operator2String(((Integer) value).intValue()));
                    }

                    return this;
                }
            });
            conditionValuePane = ValueEditorPaneFactory.createAllValueEditorPane();
            conditionKeyComboBox.setPreferredSize(new Dimension(175, conditionKeyComboBox.getPreferredSize().height));
            conditionOPComboBox.setPreferredSize(new Dimension(80, 20));
            Component[][] components = {
                    {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Available_Columns") + ":"), new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ConditionB_Operator") + ":"),
                            new UILabel()}, {conditionKeyComboBox, conditionOPComboBox, conditionValuePane}};

            double p = TableLayout.PREFERRED;
            double rowSize[] = {p, p};
            double columnSize[] = {p, p, TableLayout.FILL};

            JPanel leftPanel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
            this.add(leftPanel, BorderLayout.CENTER);

        }

        @Override
        public void populateBean(CommonCondition condition) {
            String selectionColumn = condition.getColumnName();
            ConditionKeyType type = ConditionKeyType.find(selectionColumn);
            if (type != null) {
                conditionKeyComboBox.setSelectedItem(type);
            } else {//兼容
                for (ConditionKeyType temp : conditionKeyTypes()) {
                    if (ComparatorUtils.equals(selectionColumn, temp.toString())) {
                        conditionKeyComboBox.setSelectedItem(temp);
                    }
                }
            }

            Compare compare = condition.getCompare();

            if (compare == null) {
                return;
            }
            conditionOPComboBox.setSelectedItem(new Integer(compare.getOp()));

            Object value = compare.getValue();

            conditionValuePane.populate(value);

        }

        @Override
        public CommonCondition updateBean() {
            Object value = conditionValuePane.update();

            int index = conditionKeyComboBox.getSelectedIndex();
            ConditionKeyType conditionKeyType = conditionKeyTypes()[index];
            String name = conditionKeyType.getStringType();

            return new ChartCommonCondition(name, index, new Compare(
                    ((Integer) conditionOPComboBox.getSelectedItem()).intValue(), value));
        }

        @Override
        protected String title4PopupWindow() {
            return StringUtils.EMPTY;
        }

        @Override
        public void checkValid() throws Exception {
            conditionOPComboBox.setSelectedIndex(0);
            conditionValuePane.populate(StringUtils.EMPTY);
        }
    }

}
