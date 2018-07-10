package com.fr.design.mainframe.chart.gui.data.report;

import com.fr.base.BaseFormula;
import com.fr.base.Utils;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.NormalReportDataDefinition;
import com.fr.chart.chartdata.SeriesDefinition;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.UIObserverListener;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.itable.UITable;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.layout.TableLayout;
import com.fr.stable.StableUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 抽象的 属性表 单元格数据界面.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2012-12-27 上午10:31:53
 */
public abstract class AbstractReportDataContentPane extends BasicBeanPane<ChartCollection>{
	private static final double ROW = 6;
	protected static final double COMPONENT_WIDTH = 124;

	protected UICorrelationPane seriesPane;
	
	protected abstract String[] columnNames();
	
	protected void initEveryPane() {
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { p, TableLayout.LEADING, f };
		double[] rowSize = {p, ROW, p, ROW, p, ROW, p};
		
		this.setLayout(new TableLayout(columnSize, rowSize));

        initSeriesPane();

        JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0,24,0,15));
		panel.add(seriesPane);
		this.add(panel, "0,2,2,2");
	}

    //kunsnat: 用于重载, 甘特图, 股价图 第一列 默认应该不可编辑.
    protected void initSeriesPane() {
        seriesPane = new UICorrelationPane(columnNames()) {
            public UITableEditor createUITableEditor() {
                return new InnerTableEditor();
            }

            protected UITable initUITable() {
                return new UITable(columnCount) {

                    public UITableEditor createTableEditor() {
                        return createUITableEditor();
                    }

                    public void tableCellEditingStopped(ChangeEvent e) {
                        stopPaneEditing(e);
                    }
                };
            }
        };
    }
	
	/**
	 * 检查界面Box 联动 是否可用.
	 */
	public void checkBoxUse() {
		
	}
	
	/**
	 * 覆盖方法 doNothing
	 */
	@Override
	public ChartCollection updateBean() {
		return null;
	}
	
	/**
	 * 用于直接更新 下拉列表之类的界面
	 */
	protected void populateList(List list) {
		seriesPane.populateBean(list);
	}
	
	/**
	 * 用于更新得到最新的界面列表值
	 */
	protected List updateList() {
		return seriesPane.updateBean();
	}

	protected List getEntryList(NormalReportDataDefinition seriesList) {
		List list = new ArrayList();
		for (int i = 0; i < seriesList.size(); i++) {
			SeriesDefinition seriesEntry = (SeriesDefinition) seriesList.get(i);
			Object[] nameAndValue = new Object[2];
			nameAndValue[0] = seriesEntry.getSeriesName();
			nameAndValue[1] = seriesEntry.getValue();
			if (nameAndValue[0] != null || nameAndValue[1] != null) {
				list.add(nameAndValue);
			}
		}
		return list;
	}

	/**
	 * 界面标题: 
	 */
	protected String title4PopupWindow() {
		return "";
	}
	
	protected HashMap<Object, Object> createNameValue(List<Object[]> list) {
		HashMap<Object, Object> values = new HashMap<Object, Object>();
		
		for(int i = 0; i < list.size(); i++) {
			Object[] tmp = list.get(i);
			values.put(tmp[0], tmp[1]);
		}
		
		return values;
	}
	
	protected Object canBeFormula(Object object) {
		if(object == null) {
			return null;
		}
		
		return StableUtils.canBeFormula(object) ? BaseFormula.createFormulaBuilder().build(object) : object.toString();
	}

    protected class InnerTableEditor extends UITableEditor {
		private TinyFormulaPane editorComponent;

		/**
		 * 返回当前编辑器的值
		 */
		public Object getCellEditorValue() {
			return editorComponent.getUITextField().getText();
		}

		/**
		 * 返回当前编辑器..
		 */
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			if(column == table.getModel().getColumnCount()) {
				return null;
			}
			
			seriesPane.stopCellEditing();
			
			TinyFormulaPane editor = getEditorComponent();
			editor.getUITextField().setText(Utils.objectToString(value));
			return editor;
		}

		private TinyFormulaPane getEditorComponent() {
			editorComponent = null;
			if (editorComponent == null) {
				editorComponent = new TinyFormulaPane() {
					@Override
					public void okEvent() {
						seriesPane.stopCellEditing();
						seriesPane.fireTargetChanged();
					}

					@Override
					protected void populateTextField(BaseFormula fm) {
						formulaTextField.setText(fm.getContent());
					}
				};
				editorComponent.setBackground(UIConstants.FLESH_BLUE);
				
				editorComponent.getUITextField().registerChangeListener(new UIObserverListener() {
					@Override
					public void doChange() {
						seriesPane.fireTargetChanged();
					}
				});
			}
			return editorComponent;
		}
	}

	protected JSeparator getJSeparator() {
		JSeparator jSeparator = new JSeparator();
		jSeparator.setPreferredSize(new Dimension(246, 2));
		return jSeparator;
	}

	protected Border getSidesBorder() {
		return BorderFactory.createEmptyBorder(0,5,0,5);
	}
	protected Border getFilterPaneBorder() {
		return BorderFactory.createEmptyBorder(10,5,0,5);
	}

}