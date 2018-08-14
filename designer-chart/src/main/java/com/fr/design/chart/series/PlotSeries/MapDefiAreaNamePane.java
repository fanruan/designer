package com.fr.design.chart.series.PlotSeries;

import com.fr.base.Utils;
import com.fr.chart.base.MapSvgAttr;
import com.fr.chart.base.MapSvgXMLHelper;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.Editor;
import com.fr.design.editor.editor.TextEditor;
import com.fr.design.gui.icombobox.FilterComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itableeditorpane.UIArrayTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.design.gui.xcombox.ComboBoxUseEditor;
import com.fr.design.mainframe.chart.gui.data.DatabaseTableDataPane;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 地图, 定义区域名.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-10-19 下午03:19:53
 */
public class MapDefiAreaNamePane extends BasicBeanPane<String> implements AbstrctMapAttrEditPane{

	private DatabaseTableDataPane tableDataBox;
	private FilterComboBox columnBox;

	// 双列:  左侧Label, 数据列表(全部都是UIComboBox, 支持自定义).
	private UITableEditorPane tableEditorPane;
	private UIArrayTableModel tableEditorModel;

	private String[] initNames = new String[]{};

	private String editName = "";
	private boolean isNeedDataSource = true;
	private MapSvgAttr currentSvg;

	public MapDefiAreaNamePane(boolean isNeedDataSource){
		this.isNeedDataSource = isNeedDataSource;
		initCom();
	}

	public MapDefiAreaNamePane() {
		initCom();
	}

	private void initCom() {
		this.setLayout(new BorderLayout(0, 0));

		JPanel northPane = new JPanel();
		if(this.isNeedDataSource){
			this.add(northPane, BorderLayout.NORTH);
		}

		northPane.setLayout(new FlowLayout(FlowLayout.LEFT));

		UILabel lable = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Chart-DS_TableData") + ":", SwingConstants.RIGHT);

		// 数据集, 字段, 数据列表, 使用该数据进行自动命名
		tableDataBox = new DatabaseTableDataPane(lable) {
			protected void userEvent() {
				refreshAreaNameBox();
			}
		};

		tableDataBox.setPreferredSize(new Dimension(200, 20));
		northPane.add(tableDataBox);

		columnBox = new FilterComboBox<>();
		columnBox.setPreferredSize(new Dimension(40, 20));
		columnBox.addItemListener(columnChange);

		northPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Map_Field")+":"));
		northPane.add(columnBox);

		tableEditorModel = new UIArrayTableModel(new String[]{com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Map_Use_Field"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Area_Name")}, new int[]{}) {
			public boolean isCellEditable(int row, int col) {
				return col != 0;
			}
		};
		tableEditorModel.setDefaultEditor(Object.class, new DefaultComboBoxEditor());
		tableEditorModel.setDefaultRenderer(Object.class, new DefaultComboBoxRenderer());
		tableEditorPane = new UITableEditorPane<Object[]>(tableEditorModel);
		this.add(tableEditorPane);
	}

	ItemListener columnChange = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			if (columnBox.getSelectedItem() != null) {
				String columnName = Utils.objectToString(columnBox.getSelectedItem());

				TableDataWrapper tableDataWrappe = tableDataBox.getTableDataWrapper();

				List<String> list = MapCustomPane.getColValuesInData(tableDataWrappe, columnName);

				initNames = list.toArray(initNames);

				if (tableEditorModel != null) {
					tableEditorModel.stopCellEditing();// 只是用来刷新列表的combox
				}
			}
		}
	};

	private void refreshAreaNameBox() {// 刷新区域名称列表
		TableDataWrapper tableDataWrappe = tableDataBox.getTableDataWrapper();
		if (tableDataWrappe == null) {
			return;
		}
		List<String> columnNameList = tableDataWrappe.calculateColumnNameList();

		columnBox.setItemList(columnNameList);
	}

	// 对应地图的名称
	public void populateBean(String mapName) {
		if (MapSvgXMLHelper.getInstance().containsMapName(mapName)) {
			MapSvgAttr editingMapAttr = MapSvgXMLHelper.getInstance().getMapAttr(mapName);
			this.editName = mapName;
			this.populateMapAttr(editingMapAttr);
		}
	}

	@Override
	public String updateBean() {
		// 固定存储 下 区域名 对应值 列表  MapHelper
        updateMapAttr();
		MapSvgXMLHelper.getInstance().removeMapAttr(currentSvg.getName());
		MapSvgXMLHelper.getInstance().pushMapAttr(currentSvg.getName(),currentSvg);
        return "";
	}

	private void updateMapAttr(){
		if(currentSvg != null){
			tableEditorModel.stopCellEditing();
			List list = tableEditorPane.update();
			for(int i = 0, size = list.size(); i < size; i++) {
				Object[] tmp = (Object[]) list.get(i);
				String name = Utils.objectToString(tmp[0]);
				String nameTo = Utils.objectToString(tmp[1]);
				currentSvg.setNameTo(name, nameTo);
			}
		}
	}

	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Define_Area_Name");
	}

	/**
     * 更新界面
     * @param  editingMapAttr 地图属性
    */
	public void populateMapAttr(MapSvgAttr editingMapAttr) {
		List popuValues = new ArrayList();
		if(editingMapAttr == null) {
			currentSvg = null;
			tableEditorPane.populate(popuValues.toArray());
			return;
		}
		currentSvg =editingMapAttr;
		List namesList = new ArrayList();
		Iterator shapeNames = editingMapAttr.shapeValuesIterator();
		while (shapeNames.hasNext()) {
			namesList.add(shapeNames.next());// 先得到所有的处理名字, 然后再处理对应关系
		}
		for (int i = 0; i < namesList.size(); i++) {
			Object name = namesList.get(i);
			Object value = editingMapAttr.getNameToValue(Utils.objectToString(name));
			popuValues.add(new Object[]{name, value});
		}
		tableEditorPane.populate(popuValues.toArray());
	}

	/**
     * 更新MapSvgAttr
     * @return  返回属性
    */
	public MapSvgAttr updateCurrentAttr() {
		updateMapAttr();
		return currentSvg;
	}

	private class DefaultComboBoxEditor extends AbstractCellEditor implements TableCellEditor {
		private ValueEditorPane cellEditor;

		public DefaultComboBoxEditor() {
			cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ComboBoxUseEditor(initNames)});
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			if (column == 0) {
				cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new TextEditor()});
			} else {
				cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ComboBoxUseEditor(initNames)});
			}
			cellEditor.populate(value == null ? "" : value);
			return cellEditor;
		}

		public Object getCellEditorValue() {
			return cellEditor.update();
		}
	}

	private class DefaultComboBoxRenderer extends DefaultTableCellRenderer {
		private ValueEditorPane cellEditor;

		public DefaultComboBoxRenderer() {
			cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ComboBoxUseEditor(initNames)});
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (column == 0) {
				cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new TextEditor()});
			} else {
				cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ComboBoxUseEditor(initNames)});
			}
			cellEditor.populate(value == null ? "" : value);
			return cellEditor;
		}
	}
}