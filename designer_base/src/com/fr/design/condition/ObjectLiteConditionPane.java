package com.fr.design.condition;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.DefaultComboBoxModel;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JList;
import javax.swing.JPanel;

import com.fr.data.condition.ObjectCondition;
import com.fr.data.core.Compare;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.formula.VariableResolver;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.general.Inter;

public class ObjectLiteConditionPane extends LiteConditionPane<ObjectCondition> {
	
	@Override
	protected BasicBeanPane<ObjectCondition> createUnFormulaConditionPane() {
		return new ObjectConditionPane();
	}
	
	@Override
	protected VariableResolver variableResolver4FormulaPane() {
		return VariableResolver.DEFAULT;
	}

	private  class ObjectConditionPane extends BasicBeanPane<ObjectCondition> {

		private UIComboBox conditionOPComboBox;
		private ValueEditorPane conditionValuePane;

		ObjectConditionPane() {
			this.initComponents();
		}

		protected void initComponents() {
			this.setLayout(FRGUIPaneFactory.createBorderLayout());
			// condition operation
			conditionOPComboBox = new UIComboBox();
			DefaultComboBoxModel opComboBoxModel = (DefaultComboBoxModel) conditionOPComboBox.getModel();
			int[] allOperators = Compare.getAllOperators();
			for (int i = 0; i < allOperators.length; i++) {
				opComboBoxModel.addElement(new Integer(allOperators[i]));
			}
			// conditionOPComboBox.setPreferredSize(new Dimension(120, 25));

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

			Component[][] components = { { new UILabel(Inter.getLocText("ConditionB-Operator") + ":"), new UILabel() },
					{ conditionOPComboBox, conditionValuePane } };

			double p = TableLayout.PREFERRED;
			double rowSize[] = { p, p };
			double columnSize[] = { p, TableLayout.FILL };

			JPanel leftPanel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
			this.add(leftPanel, BorderLayout.CENTER);
		}

		@Override
		public ObjectCondition updateBean() {
			Object value = conditionValuePane.update();

			return new ObjectCondition(new Compare(((Integer) conditionOPComboBox.getSelectedItem()).intValue(), value));
		}

		@Override
		public void populateBean(ObjectCondition condition) {

			Compare compare = condition.getCompare();
			conditionOPComboBox.setSelectedItem(new Integer(compare.getOp()));

			this.conditionValuePane.populate(compare.getValue());
		}

		@Override
		protected String title4PopupWindow() {
			return " ";
		}
	}
}