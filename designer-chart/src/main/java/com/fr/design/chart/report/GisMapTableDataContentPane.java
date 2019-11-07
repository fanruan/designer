package com.fr.design.chart.report;

import com.fr.base.Utils;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartdata.GisMapTableDefinition;
import com.fr.chart.chartdata.SeriesDefinition;
import com.fr.design.constants.LayoutConstants;
import com.fr.data.impl.NameTableData;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.data.DatabaseTableDataPane;

import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 属性表gis地图数据集数据源定义面板
 * @author eason
 *
 */
public class GisMapTableDataContentPane extends FurtherBasicBeanPane<GisMapTableDefinition> implements UIObserver {

	private DatabaseTableDataPane fromTableData;
	
	private ArrayList<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
	private String[] initNames = {""};

    private UIButtonGroup<String> addressType;
    private UIButtonGroup<String> lnglatOrder;
	private UIComboBox addressBox;
	private UIComboBox addressNameBox;
	private UICorrelationPane titleValuePane;
    private JPanel orderPane;

	public GisMapTableDataContentPane() {
		this.setLayout(new BorderLayout());

		JPanel northPane = new JPanel();
		this.add(northPane, BorderLayout.NORTH);

		northPane.setLayout(new FlowLayout(FlowLayout.LEFT));

		UILabel label = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Select_Dataset") + ":", SwingConstants.LEFT);

		northPane.add(fromTableData = new DatabaseTableDataPane(label) {
			@Override
			protected void userEvent() {
				refresh2ComboBox();
			}
		});
		fromTableData.setPreferredSize(new Dimension(218, 20));
		northPane.add(fromTableData);

        addressType = new UIButtonGroup<String>(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_GIS_Address"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_LatLng")});
        lnglatOrder = new UIButtonGroup<String>(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Longitude_First"),com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Latitude_First")});
		addressType.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				orderPane.setVisible(addressType.getSelectedIndex() == 1);
			}
		});
        addressBox = new UIComboBox();
		addressNameBox = new UIComboBox();
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = new double[]{p, f};
		double[] rowSize = new double[]{p, p, p};
        orderPane = new JPanel(new BorderLayout(LayoutConstants.VGAP_MEDIUM,0)){
            @Override
            public Dimension getPreferredSize() {
                if(this.isVisible()){
                    return super.getPreferredSize();
                }else{
                    return new Dimension(0,0);
                }
            }
        };
        orderPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_LatLng_Order")), BorderLayout.WEST);
        orderPane.add(lnglatOrder, BorderLayout.CENTER);
        orderPane.setVisible(false);
        lnglatOrder.setSelectedIndex(0);
        addressType.setSelectedIndex(0);

        addressNameBox.removeAllItems();
        addressNameBox.addItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_None"));

		Component[][] components = new Component[][]{
				new Component[]{addressType, addressBox},
                new Component[]{orderPane,null},
				new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Address_Name") + ":", SwingConstants.RIGHT), addressNameBox},
		};
		JPanel centerPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
		
		JPanel pane = new JPanel();
		this.add(pane, BorderLayout.CENTER);
		pane.setLayout(new BorderLayout());
		
		pane.add(centerPane, BorderLayout.NORTH);
		
		String[] titles = {com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Area_Title"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Region_Value")};
		titleValuePane = new UICorrelationPane(titles){
			public UITableEditor createUITableEditor() {
				return new InnerTableEditor();
			}
		};
		
		pane.add(titleValuePane, BorderLayout.CENTER);
	}

	private void refresh2ComboBox() {// 刷新地址  地址名 名称列表
		TableDataWrapper tableDataWrappe = fromTableData.getTableDataWrapper();
		if (tableDataWrappe == null) {
			return;
		}
		List<String> columnNameList = tableDataWrappe.calculateColumnNameList();
		initNames = columnNameList.toArray(new String[columnNameList.size()]);
		
		addressBox.removeAllItems();
		addressNameBox.removeAllItems();
        addressNameBox.addItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_None"));

		for(int i = 0, size = initNames.length; i < size; i++) {
			addressBox.addItem(initNames[i]);
			addressNameBox.addItem(initNames[i]);
		}

		addressBox.getModel().setSelectedItem(null);
		addressNameBox.getModel().setSelectedItem(null);
		
		stopEditing();
	}
	/**
	 * 界面接入
     * @param ob 对象
     * @return true表示接受
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
     * @return  标题
	 */
	public String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_TableData");
	}

	private void stopEditing() {
	}

	@Override
	public void populateBean(GisMapTableDefinition ob) {
		stopEditing();
		if (ob instanceof GisMapTableDefinition) {
			GisMapTableDefinition mapDefinition = (GisMapTableDefinition) ob;
            fromTableData.populateBean(((NameTableData) mapDefinition.getTableData()));

            if(ob.isAddress()){
                addressType.setSelectedIndex(0);
                orderPane.setVisible(false);
            }else{
                addressType.setSelectedIndex(1);
                orderPane.setVisible(true);
            }

            if(ob.isLngFirst()){
                lnglatOrder.setSelectedIndex(0);
            }else{
                lnglatOrder.setSelectedIndex(1);
            }

			addressBox.setSelectedItem(mapDefinition.getAddress());

            if(StringUtils.isEmpty(mapDefinition.getAddressName())) {
			    addressNameBox.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_None"));
            } else {
			    addressNameBox.setSelectedItem(mapDefinition.getAddressName());
            }

			List paneList = new ArrayList();
			int titleValueSize = mapDefinition.getTittleValueSize();
			for(int i = 0; i < titleValueSize; i++) {
				SeriesDefinition definition = mapDefinition.getTittleValueWithIndex(i);
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
	public GisMapTableDefinition updateBean() {// 从一行内容中update
		stopEditing();

		GisMapTableDefinition definition = new GisMapTableDefinition();

		TableDataWrapper tableDataWrappe = fromTableData.getTableDataWrapper();
		if (tableDataWrappe == null || addressBox.getSelectedItem() == null) {
			return null;
		}
		
		definition.setTableData(new NameTableData(tableDataWrappe.getTableDataName()));
		definition.setAddress(Utils.objectToString(addressBox.getSelectedItem()));

        if(this.addressType.getSelectedIndex() == 0){
            definition.setAddressType(true);
            lnglatOrder.setVisible(false);
        }else{
            definition.setAddressType(false);
            lnglatOrder.setVisible(true);
        }

        if(this.lnglatOrder.getSelectedIndex() == 0){
            definition.setLnglatOrder(true);
        }else{
            definition.setLnglatOrder(false);
        }

		if(addressNameBox.getSelectedItem() != null){
            String adName = Utils.objectToString(addressNameBox.getSelectedItem());
            if(ArrayUtils.contains(ChartConstants.getNoneKeys(), adName)) {
                definition.setAddressName(StringUtils.EMPTY);
            } else {
                definition.setAddressName(adName);
            }
        }
		
		List paneList = titleValuePane.updateBean();
		for(int i = 0, size = paneList.size(); i < size; i++) {
			Object[] values = (Object[])paneList.get(i);
			if(values.length == 2) {
				SeriesDefinition seriesDefinition = new SeriesDefinition();
				seriesDefinition.setSeriesName(values[0]);
				seriesDefinition.setValue(values[1]);
				definition.addTittleValue(seriesDefinition);
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
				
				text.addFocusListener(new FocusAdapter() {
					public void focusLost(FocusEvent e) {
						titleValuePane.stopCellEditing();
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

				box.setSelectedItem(value);
				
				this.editorComponent = box;
			}
			return this.editorComponent;
		}
	}
}