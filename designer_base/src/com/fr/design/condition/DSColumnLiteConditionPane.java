package com.fr.design.condition;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultComboBoxModel;

import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.tree.TreePath;

import com.fr.data.condition.CommonCondition;
import com.fr.design.mainframe.JTemplate;
import com.fr.general.ComparatorUtils;
import com.fr.general.data.Condition;
import com.fr.data.condition.JoinCondition;
import com.fr.data.core.Compare;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.formula.CustomVariableResolver;
import com.fr.design.formula.VariableResolver;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.ColumnIndexEditor;
import com.fr.design.editor.editor.ColumnNameEditor;
import com.fr.design.editor.editor.Editor;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

public class DSColumnLiteConditionPane extends LiteConditionPane<CommonCondition> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] displayNames = ArrayUtils.EMPTY_STRING_ARRAY;

	@Override
	protected BasicBeanPane<CommonCondition> createUnFormulaConditionPane() {
		return new CommonConditionPane();
	}

	@Override
	protected VariableResolver variableResolver4FormulaPane() {
		return new CustomVariableResolver(displayNames, false);
	}

    /**
     * 刷新数据列
     * @param displayNames  数据列名称
     */
	public void populateColumns(String[] displayNames) {
		CommonConditionPane ccp = (CommonConditionPane) this.defaultConditionPane;
		this.displayNames = displayNames;
        if(ArrayUtils.isNotEmpty(displayNames)){
            ccp.setEditor(new Editor[] { new ColumnNameEditor(displayNames), new ColumnIndexEditor(displayNames.length)}, displayNames[0]);
        }
		TreePath selectedTreePath = conditionsTree.getSelectionPath();
		if (selectedTreePath != null) {
			ExpandMutableTreeNode selectedTreeNode = (ExpandMutableTreeNode) selectedTreePath.getLastPathComponent();
			JoinCondition joinCondition = (JoinCondition) selectedTreeNode.getUserObject();
			Condition liteCondition = joinCondition.getCondition();

			if(liteCondition instanceof CommonCondition){
				CommonCondition commonCondition = (CommonCondition) liteCondition;
				int columnNumber = commonCondition.getColumnNumber();
				if (columnNumber > 0){
					ccp.keyColumnPane.populate(columnNumber);
				} else {
					String columnName = commonCondition.getColumnName();
					ccp.keyColumnPane.populate(columnName);
				}
			}
		}
	}

	protected class CommonConditionPane extends BasicBeanPane<CommonCondition> {
		private static final long serialVersionUID = 1L;
		private ValueEditorPane keyColumnPane;
		private UIComboBox conditionOPComboBox;
		private ValueEditorPane conditionValuePane;

		CommonConditionPane() {
			initComponents();
		}

		public void setEditor(Editor[] editors, Object obj) {
			keyColumnPane.setEditors(editors, obj);
		}

		protected void initComponents() {
			this.setLayout(FRGUIPaneFactory.createBorderLayout());
			keyColumnPane = ValueEditorPaneFactory.createValueEditorPane(new Editor[] {  new ColumnNameEditor(), new ColumnIndexEditor() });
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
			conditionValuePane = createValueEditorPane();
			keyColumnPane.setPreferredSize(new Dimension(175, keyColumnPane.getPreferredSize().height));
			conditionOPComboBox.setPreferredSize(new Dimension(80, 20));
			Component[][] components = {
					{ new UILabel(Inter.getLocText("Utils-Available_Columns") + ":"), new UILabel(Inter.getLocText("FR-ConditionB_Operator") + ":"),
							new UILabel() }, { keyColumnPane, conditionOPComboBox, conditionValuePane } };

			double p = TableLayout.PREFERRED;
			double rowSize[] = { p, p };
			double columnSize[] = { p, p, TableLayout.FILL };

			JPanel leftPanel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
			this.add(leftPanel, BorderLayout.CENTER);
		}
		
		protected ValueEditorPane createValueEditorPane() {
            JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
            if(jTemplate.isChartBook()){
                return ValueEditorPaneFactory.createBasicEditorWithoutFormulaPane();
            }
			return ValueEditorPaneFactory.createAllValueEditorPane();
		}

		@Override
		public CommonCondition updateBean() {
			Object value = conditionValuePane.update();
			int columnNumber = (Integer) keyColumnPane.update();
			String columnName = StringUtils.EMPTY;

			if(keyColumnPane.getCurrentEditor() instanceof ColumnNameEditor && ArrayUtils.isNotEmpty(displayNames)){
				columnName = DSColumnLiteConditionPane.this.displayNames[columnNumber - 1];
				columnNumber = -1;
			}
				
			return new CommonCondition(columnName, columnNumber, new Compare(
						((Integer) conditionOPComboBox.getSelectedItem()).intValue(), value));
		}

		@Override
		public void populateBean(CommonCondition condition) {

			String selectionColumn = condition.getColumnName();
			int selectionColumnNumber = condition.getColumnNumber();
			if(StringUtils.isNotEmpty(selectionColumn)){
				for (int i = 0; i < DSColumnLiteConditionPane.this.displayNames.length; i++) {
                    if(ComparatorUtils.equals(DSColumnLiteConditionPane.this.displayNames[i],selectionColumn)) {
						//keyColumnPane.populate(i + 1);
						keyColumnPane.populate(selectionColumn);
						break;
					}
				}
			}else{
				keyColumnPane.populate(selectionColumnNumber);
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
		protected String title4PopupWindow() {
			return " ";
		}

		@Override
		public void checkValid() throws Exception {
			conditionOPComboBox.setSelectedIndex(0);
			conditionValuePane.populate(StringUtils.EMPTY);
		}
		
		
	}
}