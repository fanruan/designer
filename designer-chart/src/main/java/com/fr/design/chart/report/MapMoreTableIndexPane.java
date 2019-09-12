package com.fr.design.chart.report;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;

import com.fr.base.Utils;
import com.fr.chart.chartdata.MapSingleLayerTableDefinition;
import com.fr.chart.chartdata.SeriesDefinition;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.gui.itextfield.UITextField;

import com.fr.stable.StringUtils;

/**
 * 多层地图, 数据集定义, 多层切换 界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-4-22 下午03:27:51
 */
public class MapMoreTableIndexPane extends BasicBeanPane<MapSingleLayerTableDefinition> implements UIObserver{
	private static final long serialVersionUID = 8135457041761804584L;
	
	private String title = StringUtils.EMPTY;
	
	private UIComboBox areaNameBox;
	private UICorrelationPane tabPane;// 文本框 + 下拉框
	
	private Object[] boxItems = new Object[]{""};
	
	public MapMoreTableIndexPane() {
		initPane();
	}
	
	public MapMoreTableIndexPane(String titleName) {
		title = titleName;
		initPane();
	}
	
	@Override
	protected String title4PopupWindow() {
		return title;
	}

	private void initPane() {
		this.setLayout(new BorderLayout());
		
		JPanel northPane = new JPanel();
		this.add(northPane, BorderLayout.NORTH);
		northPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		northPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Area_Name") + ":"));
		
		areaNameBox = new UIComboBox();
		areaNameBox.setPreferredSize(new Dimension(120, 20));
		northPane.add(areaNameBox);
		
		tabPane = new UICorrelationPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Area_Title"),
				com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Region_Value")){
			public UITableEditor createUITableEditor() {
				return new InnerTableEditor();
			}
		};
		
		this.add(tabPane, BorderLayout.CENTER);
	}
	
	/**
	 * 初始化 Box的选项.
	 */
	public void initAreaComBox(Object[] values) {
		Object oldSelected = areaNameBox.getSelectedItem();
		areaNameBox.removeAllItems();
		boxItems = values;
		if(values != null) {
			for(int i = 0, size = values.length; i < size; i++) {
				areaNameBox.addItem(values[i]);
			}
		}
		areaNameBox.getModel().setSelectedItem(oldSelected);
	}

	@Override
	public void populateBean(MapSingleLayerTableDefinition ob) {
		if(ob != null) {
			areaNameBox.setSelectedItem(ob.getAreaName());
			
			List paneList = new ArrayList();
			int titleValueSize = ob.getTitleValueSize();
			for(int i = 0; i < titleValueSize; i++) {
				SeriesDefinition definition = ob.getTitleValueWithIndex(i);
				if(definition != null && definition.getSeriesName() != null && definition.getValue() != null) {
					paneList.add(new Object[]{definition.getSeriesName(), definition.getValue()});
				}
			}
			
			if(!paneList.isEmpty()) {
				tabPane.populateBean(paneList);
			}
		}
	}
	
	public MapSingleLayerTableDefinition updateBean() {// 不需要数据集, 
		MapSingleLayerTableDefinition definition = new MapSingleLayerTableDefinition();
		
		if(areaNameBox.getSelectedItem() != null) {
			definition.setAreaName(Utils.objectToString(areaNameBox.getSelectedItem()));
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
	
	private class InnerTableEditor extends UITableEditor {// 文本框  + 下拉框
		private JComponent editorComponent;

		/**
		 * 返回当前编辑器的值
		 */
		public Object getCellEditorValue() {
			if(editorComponent instanceof UIComboBox) {
				return ((UIComboBox)editorComponent).getSelectedItem();
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
					field.setText(value.toString());
				}
			} else {
				UIComboBox box = new UIComboBox(boxItems);
				
				box.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						tabPane.fireTargetChanged();
						tabPane.stopCellEditing();
					}
				});

				box.setSelectedItem(value);
				
				editorComponent = box;
			}
			return editorComponent;
		}
	}

	@Override
	public void registerChangeListener(UIObserverListener listener) {
		if(areaNameBox != null) {
			areaNameBox.registerChangeListener(listener);
		}
		if(tabPane != null) {
			tabPane.registerChangeListener(listener);
		}
	}

	@Override
	public boolean shouldResponseChangeListener() {
		return true;
	}

}