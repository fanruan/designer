package com.fr.design.chart.report;

import com.fr.base.MapXMLHelper;
import com.fr.chart.base.MapSvgXMLHelper;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.Editor;
import com.fr.design.editor.editor.TextEditor;
import com.fr.design.gui.itableeditorpane.UIArrayTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.design.gui.xcombox.ComboBoxUseEditor;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.List;

/**
 * 地图, 下层钻取设置
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-10-19 下午03:19:53
 */
public class MapCubeSetDataPane extends BasicBeanPane<List>{
	
	private UITableEditorPane tableEditorPane;// 地图名称  +  地图类型列表
	
	private String[] initNames = new String[]{""};
	
	public MapCubeSetDataPane() {
		initCom();
	}

	private void initCom() {
		this.setLayout(new BorderLayout(0, 0));
		
		UIArrayTableModel model = new UIArrayTableModel(new String[]{Inter.getLocText("FR-Chart-Area_Name"), Inter.getLocText("FR-Chart-Drill_Map")}, new int[] {}) {
			public boolean isCellEditable(int row, int col) {
				return col != 0;
			}
		};
		model.setDefaultEditor(Object.class, new DefaultComboBoxEditor());
		model.setDefaultRenderer(Object.class, new DefaultComboBoxRenderer());
		
		tableEditorPane = new UITableEditorPane<Object[]>(model);
		this.add(tableEditorPane);
		
		model.addRow(new Object[]{"", ""});
	}
	
	/**
	 * 刷新下拉列表
	 */
	public void freshComboxNames() {
		initNames = MapSvgXMLHelper.getInstance().mapAllNames();
	}

    /**
     * 位图地图刷新下拉列表
     */
    public void freshBitMapComboxNames() {
        initNames = MapXMLHelper.getInstance().mapAllNames();
    }
	
	// 需要得到地图 所有区域名称. 以及对应好的名字.
	@Override
	public void populateBean(List ob) {
		tableEditorPane.populate(ob.toArray());
	}

	@Override
	public List updateBean() {
		return tableEditorPane.update();
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("FR-Chart-Drill_Setting");
	}
	
	/**
	 * refresh TODO 只是需要随时更新initNames
	* @author kunsnat E-mail:kunsnat@gmail.com
	* @version 创建时间：2012-11-20 下午05:12:20
	 */
	
	private class DefaultComboBoxEditor extends AbstractCellEditor implements TableCellEditor {
		private static final long serialVersionUID = -3239789564820528730L;
		private ValueEditorPane cellEditor;
		
		public DefaultComboBoxEditor() {
			cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ComboBoxUseEditor(initNames)});
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			if(column == 0) {
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
		private static final long serialVersionUID = -695450455731718014L;
		private ValueEditorPane cellEditor;
		
		public DefaultComboBoxRenderer() {
			cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ComboBoxUseEditor(initNames)});
		}
		
    	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    		if(column == 0) {
    			cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new TextEditor()});
			} else {
				cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ComboBoxUseEditor(initNames)});
			}
    		cellEditor.populate(value == null ? "" : value);
			return cellEditor;
    	}
    }
}