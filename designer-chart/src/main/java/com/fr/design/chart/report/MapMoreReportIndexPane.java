package com.fr.design.chart.report;

import com.fr.base.BaseFormula;
import com.fr.base.Utils;
import com.fr.chart.chartdata.BaseSeriesDefinition;
import com.fr.chart.chartdata.MapSingleLayerReportDefinition;
import com.fr.chart.chartdata.SeriesDefinition;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.gui.itextfield.UITextField;

import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 多层地图, 单元格, 多层切换 单界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-4-22 下午04:26:10
 */
public class MapMoreReportIndexPane extends BasicBeanPane<MapSingleLayerReportDefinition> implements UIObserver{
	private String title = StringUtils.EMPTY;
	
	private TinyFormulaPane areaNamePane;
	private UICorrelationPane tabPane;
	
	public MapMoreReportIndexPane() {
		initPane();
	}
	
	public MapMoreReportIndexPane(String titleName) {
		title = titleName;
		initPane();
	}
	
	private void initPane() {
		this.setLayout(new BorderLayout(0, 0));
		
		JPanel northPane = new JPanel();
		this.add(northPane, BorderLayout.NORTH);
		northPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		northPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Area_Name") + ":"));
		
		areaNamePane = new TinyFormulaPane();
		areaNamePane.setPreferredSize(new Dimension(120, 20));
		northPane.add(areaNamePane);
		
		tabPane = new UICorrelationPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Area_Title"),
				com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Area_Value")){
			public UITableEditor createUITableEditor() {
				return new InnerTableEditor();
			}
		};
		
		this.add(tabPane, BorderLayout.CENTER);
	}

	@Override
	public void populateBean(MapSingleLayerReportDefinition definition) {
		if(definition != null && definition.getCategoryName() != null) {
			areaNamePane.populateBean(Utils.objectToString(definition.getCategoryName()));
			
			List paneList = new ArrayList();
			int titleValueSize = definition.getTitleValueSize();
			for(int i = 0; i < titleValueSize; i++) {
				BaseSeriesDefinition sd = definition.getTitleValueWithIndex(i);
				if(sd != null && sd.getSeriesName() != null && sd.getValue() != null) {
					paneList.add(new Object[]{sd.getSeriesName(), sd.getValue()});
				}
			}
			
			if(!paneList.isEmpty()) {
				tabPane.populateBean(paneList);
			}
		}
	}
	
	public MapSingleLayerReportDefinition updateBean() {
		MapSingleLayerReportDefinition definition = new MapSingleLayerReportDefinition();
		
		String areaName = areaNamePane.updateBean();
		if(StableUtils.canBeFormula(areaName)) {
			definition.setCategoryName(BaseFormula.createFormulaBuilder().build(areaName));
		} else {
			definition.setCategoryName(areaName);
		}
		
		List paneList = tabPane.updateBean();
		for(int i = 0, size = paneList.size(); i < size; i++) {
			Object[] values = (Object[])paneList.get(i);
			if(values.length == 2) {
				SeriesDefinition seriesDefinition = new SeriesDefinition();
				seriesDefinition.setSeriesName(values[0]);
				seriesDefinition.setValue(values[1]);
				definition.addTitleValue(seriesDefinition);
			}
		}
		
		return definition;
	}

	@Override
	protected String title4PopupWindow() {
		return title;
	}
	
	private class InnerTableEditor extends UITableEditor {
		private JComponent editorComponent;

		/**
		 * 返回当前编辑器的值
		 */
		public Object getCellEditorValue() {
			if(editorComponent instanceof TinyFormulaPane) {
				return ((TinyFormulaPane)editorComponent).getUITextField().getText();
			} else if(editorComponent instanceof UITextField) {
				return ((UITextField)editorComponent).getText();
			}
			
			return super.getCellEditorValue();
		}

		/**
		 * 返回当前编辑器..
		 */
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			if (column == table.getModel().getColumnCount()) {
				return null;
			}
			return getEditorComponent(column, value);
		}

		private JComponent getEditorComponent(int column, Object value) {
			if(column == 0) {
				UITextField field = new UITextField();
				editorComponent = field;
				
				if(value != null) {
					field.setText(Utils.objectToString(value));
				}
			} else {
				TinyFormulaPane tinyPane = new TinyFormulaPane() {
					@Override
					public void okEvent() {
						tabPane.stopCellEditing();
						tabPane.fireTargetChanged();
					}
				};
				tinyPane.setBackground(UIConstants.FLESH_BLUE);
				
				tinyPane.getUITextField().addFocusListener(new FocusAdapter() {
					@Override
					public void focusLost(FocusEvent e) {
//						tabPane.stopCellEditing();//kunsnat: 不stop是因为可能直接点击公式编辑按钮, 否则需要点击两次才能弹出.
						tabPane.fireTargetChanged();
					}
				});
				
				if(value != null) {
					tinyPane.getUITextField().setText(Utils.objectToString(value));
				}
				
				editorComponent = tinyPane;
			}
			return editorComponent;
		}
	}

	@Override
	public void registerChangeListener(UIObserverListener listener) {
		if(tabPane != null) {
			tabPane.registerChangeListener(listener);
		}
		if(areaNamePane != null) {
			areaNamePane.registerChangeListener(listener);
		}
	}

	@Override
	public boolean shouldResponseChangeListener() {
		return true;
	}

}