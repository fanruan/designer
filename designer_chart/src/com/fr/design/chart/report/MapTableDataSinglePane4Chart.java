package com.fr.design.chart.report;

import com.fr.base.Utils;
import com.fr.chart.chartdata.MapSingleLayerTableDefinition;
import com.fr.chart.chartdata.SeriesDefinition;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public class MapTableDataSinglePane4Chart extends FurtherBasicBeanPane<MapSingleLayerTableDefinition> implements UIObserver {

	private static final int LABEL_WIDTH_GAP = 5;
	private static final int COM_HEIGHT = 20;
   	private ArrayList<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
   	private String[] initNames = {""};

   	private UIComboBox areaNameBox;
   	private UICorrelationPane titleValuePane;
    private TableDataWrapper tableDataWrapper;

   	public MapTableDataSinglePane4Chart() {
   		this.setLayout(new BorderLayout());
   		JPanel pane = new JPanel();
   		this.add(pane, BorderLayout.CENTER);
   		pane.setLayout(new BorderLayout());

   		pane.add(getAreaNamePane(), BorderLayout.NORTH);

   		String[] titles = {Inter.getLocText("FR-Chart-Area_Title"), Inter.getLocText("FR-Chart-Area_Value")};
   		titleValuePane = new UICorrelationPane(titles){
   			public UITableEditor createUITableEditor() {
   				return new InnerTableEditor();
   			}
   		};

   		pane.add(titleValuePane, BorderLayout.CENTER);
   	}


	private  JPanel getAreaNamePane(){
		final UILabel label = new BoldFontTextLabel(Inter.getLocText("FR-Chart-Map_ShowWay") + ":");
		UILabel nameLabel = new UILabel(Inter.getLocText("FR-Chart-Area_Name") + ":", SwingConstants.RIGHT){
			public Dimension getPreferredSize() {
				return new Dimension(label.getPreferredSize().width+LABEL_WIDTH_GAP,COM_HEIGHT);
			}
		};
		areaNameBox = new UIComboBox();
		areaNameBox.setPreferredSize(new Dimension(80, 20));
		double p =TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p};
        Component[][] components = new Component[][]{
				new Component[]{nameLabel, areaNameBox},
		};
		return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
	}
	/**
	 *刷新区域名称列表
	 */
   	public void refreshAreaNameBox() {// 刷新区域名称列表
   		TableDataWrapper tableDataWrappe = tableDataWrapper;
   		if (tableDataWrappe == null) {
   			return;
   		}
   		java.util.List<String> columnNameList = tableDataWrappe.calculateColumnNameList();
   		initNames = columnNameList.toArray(new String[columnNameList.size()]);

   		Object oldSelected = areaNameBox.getSelectedItem();
   		areaNameBox.removeAllItems();
   		for(int i = 0, size = initNames.length; i < size; i++) {
   			areaNameBox.addItem(initNames[i]);
   		}
   		areaNameBox.getModel().setSelectedItem(oldSelected);
   		stopEditing();
   	}


    /**
     * 是否接受数据集
     * @param ob  具体变量
     * @return 不是
     */
   	public boolean accept(Object ob) {
   		return false;
   	}

   	/**
   	 * 界面重置
   	 */
   	public void reset() {

   	}

    /**
     * 界面弹出标题
     * @return 标题
     */
   	public String title4PopupWindow() {
   		return Inter.getLocText("FR-Chart-Table_Data");
   	}

   	private void stopEditing() {
   	}

   	@Override
   	public void populateBean(MapSingleLayerTableDefinition ob) {
   		stopEditing();
   		if (ob instanceof MapSingleLayerTableDefinition) {
   			MapSingleLayerTableDefinition mapDefinition = (MapSingleLayerTableDefinition) ob;

//   			fromTableData.populateBean(((NameTableData) mapDefinition.getTableData()));
   			areaNameBox.setSelectedItem(mapDefinition.getAreaName());

   			java.util.List paneList = new ArrayList();
   			int titleValueSize = mapDefinition.getTitleValueSize();
   			for(int i = 0; i < titleValueSize; i++) {
   				SeriesDefinition definition = mapDefinition.getTitleValueWithIndex(i);
   				if(definition != null && definition.getSeriesName() != null && definition.getValue() != null) {
   					paneList.add(new Object[]{definition.getSeriesName(), definition.getValue()});
   				}
   			}

   			if(!paneList.isEmpty()) {
   				titleValuePane.populateBean(paneList);
   			}
   		}
   	}

   	@Override
   	public MapSingleLayerTableDefinition updateBean() {// 从一行内容中update
   		stopEditing();

   		MapSingleLayerTableDefinition definition = new MapSingleLayerTableDefinition();

   		TableDataWrapper tableDataWrappe  = tableDataWrapper;
//        = fromTableData.getTableDataWrapper();
   		if (tableDataWrappe == null || areaNameBox.getSelectedItem() == null) {
   			return null;
   		}

   		definition.setTableData(tableDataWrapper.getTableData());
   		definition.setAreaName(Utils.objectToString(areaNameBox.getSelectedItem()));

   		java.util.List paneList = titleValuePane.updateBean();
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

   	/**
   	 * 给组件登记一个观察者监听事件
   	 *
   	 * @param listener 观察者监听事件
   	 */
   	public void registerChangeListener(final UIObserverListener listener) {
   		changeListeners.add(new ChangeListener() {
   			public void stateChanged(ChangeEvent e) {
   				listener.doChange();
   			}
   		});
   	}

   	/**
   	 * 组件是否需要响应添加的观察者事件
   	 *
   	 * @return 如果需要响应观察者事件则返回true，否则返回false
   	 */
   	public boolean shouldResponseChangeListener() {
   		return true;
   	}

   	private class InnerTableEditor extends UITableEditor {
   		private JComponent editorComponent;

   		/**
   		 * 返回当前编辑器的值
   		 */
   		public Object getCellEditorValue() {
   			if(editorComponent instanceof UITextField) {
   				UITextField textField = (UITextField)editorComponent;
   				return textField.getText();
   			} else if(editorComponent instanceof UIComboBox) {
   				UIComboBox boxPane = (UIComboBox)editorComponent;
   				return boxPane.getSelectedItem();
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
   			if(column == 0 ) {
   				UITextField text = new UITextField();
   				if(value != null) {
   					text.setText(Utils.objectToString(value));
   				}

   				text.registerChangeListener(new UIObserverListener() {
   					@Override
   					public void doChange() {
   						titleValuePane.fireTargetChanged();
   					}
   				});

   				this.editorComponent = text;
   			} else {
   				UIComboBox box = new UIComboBox(initNames);
   				box.addItemListener(new ItemListener() {
   					@Override
   					public void itemStateChanged(ItemEvent e) {
   						titleValuePane.fireTargetChanged();
   						titleValuePane.stopCellEditing();
   					}
   				});

   				if (value != null && StringUtils.isNotEmpty(value.toString())) {
   					box.setSelectedItem(value);
   				} else {
   					box.setSelectedItem(value);
   				}

   				this.editorComponent = box;
   			}
   			return this.editorComponent;
   		}
   	}

	public void setTableDataWrapper(TableDataWrapper wrapper){
		this.tableDataWrapper = wrapper;
	}
}