package com.fr.design.chart.report;

import com.fr.base.BaseFormula;
import com.fr.base.Utils;
import com.fr.chart.chartdata.BaseSeriesDefinition;
import com.fr.chart.chartdata.GisMapReportDefinition;
import com.fr.chart.chartdata.SeriesDefinition;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;

import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 属性表gis地图单元格数据源设置界面
 *
 * @author eason
 */
public class GisMapReportDataContentPane extends FurtherBasicBeanPane<GisMapReportDefinition> implements UIObserver {
    private UIButtonGroup<String> addressType;
    private UIButtonGroup<String> lnglatOrder;
    private TinyFormulaPane addressPane;
    private TinyFormulaPane addressNamePane;
    private UICorrelationPane seriesPane;
    private JPanel orderPane;
    private ArrayList<ChangeListener> changeListeners = new ArrayList<ChangeListener>();

    public GisMapReportDataContentPane() {
        initCom();
    }

    private void initCom() {
        this.setLayout(new BorderLayout(0, 0));
        addressType = new UIButtonGroup<String>(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Address"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_LatLng")});
        lnglatOrder = new UIButtonGroup<String>(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Longitude_First"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Latitude_First")});
        addressPane = new TinyFormulaPane();
        addressNamePane = new TinyFormulaPane();
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
        orderPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_LatLng_Order")), BorderLayout.WEST);
        orderPane.add(lnglatOrder, BorderLayout.CENTER);
        orderPane.setVisible(false);
        lnglatOrder.setSelectedIndex(0);
        addressType.setSelectedIndex(0);
        Component[][] components = new Component[][]{
                new Component[]{addressType, addressPane},
                new Component[]{orderPane, null},
                new Component[]{new UILabel(" " +com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Address_Name")+":", SwingConstants.RIGHT), addressNamePane},
        };
        JPanel northPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);

        this.add(northPane, BorderLayout.NORTH);

        String[] columnNames = new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Area_Title"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Region_Value")};
        seriesPane = new UICorrelationPane(columnNames) {
            public UITableEditor createUITableEditor() {
                return new InnerTableEditor();
            }
        };

        this.add(seriesPane, BorderLayout.CENTER);
    }

    /**
     * 界面接入.
     * @param ob 对象
     * @return true表示接受
     */
    public boolean accept(Object ob) {
        return true;
    }

    /**
     * 重置
     */
    public void reset() {

    }

    /**
     * 界面弹出标题.
     * @return  标题
     */
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Cell");
    }

    @Override
    public void populateBean(GisMapReportDefinition ob) {
        if (ob.getCategoryName() != null) {
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

            addressPane.populateBean(Utils.objectToString(ob.getCategoryName()));
            if (ob.getAddressName() != null) {
                addressNamePane.populateBean(Utils.objectToString(ob.getAddressName()));
            }
            int size = ob.getTitleValueSize();
            List paneList = new ArrayList();
            for (int i = 0; i < size; i++) {
                BaseSeriesDefinition first = ob.getTitleValueWithIndex(i);
                if (first != null && first.getSeriesName() != null && first.getValue() != null) {
                    paneList.add(new Object[]{first.getSeriesName(), first.getValue()});
                }
            }
            if (!paneList.isEmpty()) {
                seriesPane.populateBean(paneList);
            }
        }
    }

    @Override
    public GisMapReportDefinition updateBean() {
        GisMapReportDefinition reportDefinition = new GisMapReportDefinition();
        if (this.addressType.getSelectedIndex() == 0) {
            reportDefinition.setAddressType(true);
            orderPane.setVisible(false);
        } else {
            reportDefinition.setAddressType(false);
            orderPane.setVisible(true);
        }

        if (this.lnglatOrder.getSelectedIndex() == 0) {
            reportDefinition.setLnglatOrder(true);
        } else {
            reportDefinition.setLnglatOrder(false);
        }

        String address = addressPane.updateBean();
        if (StringUtils.isBlank(address)) {
            return null;
        }
        if (StableUtils.canBeFormula(address)) {
            reportDefinition.setCategoryName(BaseFormula.createFormulaBuilder().build(address));
        } else {
            reportDefinition.setCategoryName(address);
        }

        String addressName = addressNamePane.updateBean();
        if (addressName != null && !StringUtils.isBlank(addressName)) {
            reportDefinition.setAddressName(addressName);
        }
        List values = seriesPane.updateBean();
        if (values != null && !values.isEmpty()) {
            for (int i = 0, size = values.size(); i < size; i++) {
                Object[] objects = (Object[]) values.get(i);
                Object name = objects[0];
                Object value = objects[1];

                if (StableUtils.canBeFormula(value)) {
                    value = BaseFormula.createFormulaBuilder().build(value);
                }
                SeriesDefinition definition = new SeriesDefinition(name, value);
                reportDefinition.addTitleValue(definition);
            }
        }
        return reportDefinition;
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
            if (editorComponent instanceof TinyFormulaPane) {
                return ((TinyFormulaPane) editorComponent).getUITextField().getText();
            } else if (editorComponent instanceof UITextField) {
                return ((UITextField) editorComponent).getText();
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
            if (column == 0) {
                UITextField field = new UITextField();
                addListener4UITextFiled(field);

                if (value != null) {
                    field.setText(Utils.objectToString(value));
                }
                editorComponent = field;
            } else {
                TinyFormulaPane tinyPane = new TinyFormulaPane() {
                    @Override
                    public void okEvent() {
                        seriesPane.stopCellEditing();
                        seriesPane.fireTargetChanged();
                    }
                };
                tinyPane.setBackground(UIConstants.FLESH_BLUE);

                addListener4UITextFiled(tinyPane.getUITextField());

                if (value != null) {
                    tinyPane.getUITextField().setText(Utils.objectToString(value));
                }

                editorComponent = tinyPane;
            }
            return editorComponent;
        }

        private void addListener4UITextFiled(UITextField textField) {

            textField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
//					seriesPane.stopCellEditing();	//kunsnat: 不stop是因为可能直接点击公式编辑按钮, 否则需要点击两次才能弹出.
                    seriesPane.fireTargetChanged();
                }
            });
        }
    }
}