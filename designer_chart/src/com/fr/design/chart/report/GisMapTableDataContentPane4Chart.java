package com.fr.design.chart.report;

import com.fr.base.Utils;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartdata.GisMapTableDefinition;
import com.fr.chart.chartdata.SeriesDefinition;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
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
import com.fr.general.Inter;
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
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public class GisMapTableDataContentPane4Chart extends FurtherBasicBeanPane<GisMapTableDefinition> implements UIObserver {

    private ArrayList<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
    private String[] initNames = {""};

    private UIButtonGroup<String> addressType;
    private UIButtonGroup<String> lnglatOrder;
    private UIComboBox addressBox;
    private UIComboBox addressNameBox;
    private UICorrelationPane titleValuePane;
    private JPanel orderPane;
    private TableDataWrapper tableDataWrapper;

    public GisMapTableDataContentPane4Chart() {
        this.setLayout(new BorderLayout());

        addressType = new UIButtonGroup<String>(new String[]{Inter.getLocText("Chart-Gis_Address"), Inter.getLocText("Chart-Gis_LatLng")});
        lnglatOrder = new UIButtonGroup<String>(new String[]{Inter.getLocText("Chart-Lng_First"), Inter.getLocText("Chart-Lat_First")});
        addressBox = new UIComboBox();
        addressNameBox = new UIComboBox();
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = new double[]{p, f};
        double[] rowSize = new double[]{p, p, p};
        orderPane = new JPanel(new BorderLayout(LayoutConstants.VGAP_MEDIUM, 0)) {
            @Override
            public Dimension getPreferredSize() {
                if (this.isVisible()) {
                    return super.getPreferredSize();
                } else {
                    return new Dimension(0, 0);
                }
            }
        };
        orderPane.add(new UILabel(Inter.getLocText("Chart-LatLng_Order")), BorderLayout.WEST);
        orderPane.add(lnglatOrder, BorderLayout.CENTER);
        orderPane.setVisible(false);
        lnglatOrder.setSelectedIndex(0);
        addressType.setSelectedIndex(0);

        addressNameBox.removeAllItems();
        addressNameBox.addItem(Inter.getLocText("Chart-Use_None"));

        Component[][] components = new Component[][]{
                new Component[]{addressType, addressBox},
                new Component[]{orderPane, null},
                new Component[]{new UILabel(Inter.getLocText("Chart-Address_Name") + ":", SwingConstants.RIGHT), addressNameBox},
        };
        JPanel centerPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);

        JPanel pane = new JPanel();
        this.add(pane, BorderLayout.CENTER);
        pane.setLayout(new BorderLayout());

        pane.add(centerPane, BorderLayout.NORTH);

        String[] titles = {Inter.getLocText("Chart-Area_Title"), Inter.getLocText("Chart-Area_Value")};
        titleValuePane = new UICorrelationPane(titles) {
            public UITableEditor createUITableEditor() {
                return new InnerTableEditor();
            }
        };

        pane.add(titleValuePane, BorderLayout.CENTER);
    }

    private void refresh2ComboBox() {// 刷新地址  地址名 名称列表
        TableDataWrapper tableDataWrappe =tableDataWrapper;
        if (tableDataWrappe == null) {
            return;
        }
        java.util.List<String> columnNameList = tableDataWrappe.calculateColumnNameList();
        initNames = columnNameList.toArray(new String[columnNameList.size()]);

        addressBox.removeAllItems();
        addressNameBox.removeAllItems();
        addressNameBox.addItem(Inter.getLocText("Chart-Use_None"));

        for (int i = 0, size = initNames.length; i < size; i++) {
            addressBox.addItem(initNames[i]);
            addressNameBox.addItem(initNames[i]);
        }
         if(initNames.length > 0){
            addressBox.setSelectedIndex(0);
         }
        addressNameBox.setSelectedIndex(0);
        stopEditing();
    }

    /**
     * 界面接入
     *
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
     *
     * @return 标题
     */
    public String title4PopupWindow() {
        return Inter.getLocText("Chart-DS_TableData");
    }

    private void stopEditing() {
    }

    @Override
    public void populateBean(GisMapTableDefinition ob) {
        stopEditing();
        if (ob instanceof GisMapTableDefinition) {
            GisMapTableDefinition mapDefinition = (GisMapTableDefinition) ob;
            if (ob.isAddress()) {
                addressType.setSelectedIndex(0);
                orderPane.setVisible(false);
            } else {
                addressType.setSelectedIndex(1);
                orderPane.setVisible(true);
            }

            if (ob.isLngFirst()) {
                lnglatOrder.setSelectedIndex(0);
            } else {
                lnglatOrder.setSelectedIndex(1);
            }

            addressBox.setSelectedItem(mapDefinition.getAddress());

            if (StringUtils.isEmpty(mapDefinition.getAddressName())) {
                addressNameBox.setSelectedItem(Inter.getLocText("Chart-Use_None"));
            } else {
                addressNameBox.setSelectedItem(mapDefinition.getAddressName());
            }

            java.util.List paneList = new ArrayList();
            int titleValueSize = mapDefinition.getTittleValueSize();
            for (int i = 0; i < titleValueSize; i++) {
                SeriesDefinition definition = mapDefinition.getTittleValueWithIndex(i);
                if (definition != null && definition.getSeriesName() != null && definition.getValue() != null) {
                    paneList.add(new Object[]{definition.getSeriesName(), definition.getValue()});
                }
            }

            if (!paneList.isEmpty()) {
                titleValuePane.populateBean(paneList);
            }
        }
    }

    @Override
    public GisMapTableDefinition updateBean() {// 从一行内容中update
        stopEditing();

        GisMapTableDefinition definition = new GisMapTableDefinition();

        TableDataWrapper tableDataWrappe = tableDataWrapper;
        if (tableDataWrappe == null || addressBox.getSelectedItem() == null) {
            return null;
        }

        definition.setTableData(tableDataWrapper.getTableData());
        definition.setAddress(Utils.objectToString(addressBox.getSelectedItem()));

        if (this.addressType.getSelectedIndex() == 0) {
            definition.setAddressType(true);
            lnglatOrder.setVisible(false);
        } else {
            definition.setAddressType(false);
            lnglatOrder.setVisible(true);
        }

        if (this.lnglatOrder.getSelectedIndex() == 0) {
            definition.setLnglatOrder(true);
        } else {
            definition.setLnglatOrder(false);
        }

        if (addressNameBox.getSelectedItem() != null) {
            String adName = Utils.objectToString(addressNameBox.getSelectedItem());
            if (ArrayUtils.contains(ChartConstants.NONE_KEYS, adName)) {
                definition.setAddressName(StringUtils.EMPTY);
            } else {
                definition.setAddressName(adName);
            }
        }

        java.util.List paneList = titleValuePane.updateBean();
        for (int i = 0, size = paneList.size(); i < size; i++) {
            Object[] values = (Object[]) paneList.get(i);
            if (values.length == 2) {
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
            if (editorComponent instanceof UITextField) {
                UITextField textField = (UITextField) editorComponent;
                return textField.getText();
            } else if (editorComponent instanceof UIComboBox) {
                UIComboBox boxPane = (UIComboBox) editorComponent;
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
            if (column == 0) {
                UITextField text = new UITextField();
                if (value != null) {
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

    /**
     * 出发数据集改变
     * @param wrapper 数据集
     */
    public void fireTableDataChange(TableDataWrapper wrapper){
        this.tableDataWrapper = wrapper;
        refresh2ComboBox();
    }

}