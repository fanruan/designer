package com.fr.design.widget;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.fun.WidgetDesignHandler;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.CellWidgetPropertyPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.widget.btn.ButtonConstants;
import com.fr.form.ui.Button;
import com.fr.form.ui.*;
import com.fr.general.ComparatorUtils;

import com.fr.stable.ArrayUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

/**
 * CellEditorDef Pane.
 */
public class WidgetPane extends AbstractAttrNoScrollPane implements ItemListener {

    private EditorTypeComboBox editorTypeComboBox;
    private CellWidgetCardPane cellEditorCardPane;
    private boolean shouldFireSelectedEvent = true;
    private JPanel northPane;

    public WidgetPane() {
        this(null);
    }

    /**
     * Constructor
     */
    public WidgetPane(ElementCasePane pane) {
        this.initComponents(pane);
    }


    public boolean isShouldFireSelectedEvent(){
        return shouldFireSelectedEvent;
    }


    protected void initComponents(ElementCasePane pane) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        editorTypeComboBox = new EditorTypeComboBox(pane != null);
        editorTypeComboBox.setPreferredSize(new Dimension(155, 30));
        editorTypeComboBox.setMaximumRowCount(16);
        northPane = initNorthPane();
        northPane.setBorder(BorderFactory.createEmptyBorder(12, 10, 10, 15));
        this.add(northPane, BorderLayout.NORTH);

        editorTypeComboBox.addItemListener(this);

        cellEditorCardPane = initWidgetCardPane(pane);
        this.add(cellEditorCardPane, BorderLayout.CENTER);
        this.addAttributeChangeListener(listener);
    }

    public JPanel initNorthPane() {
        UILabel emptyLabel = new UILabel();
        emptyLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, p, f};
        double[] rowSize = {p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Select_Widget")), emptyLabel, editorTypeComboBox},
        };
        JPanel jPanel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        return jPanel;
    }

    protected CellWidgetCardPane initWidgetCardPane(ElementCasePane pane) {
        return new CellWidgetCardPane(pane);
    }

    protected JPanel createContentPane() {
        return new JPanel();
    }


    AttributeChangeListener listener = new AttributeChangeListener() {
        @Override
        public void attributeChange() {
            if(shouldFireSelectedEvent){
                CellWidgetPropertyPane.getInstance().update();
            }
        }
    };

    /**
     * 状态改变
     *
     * @param e 事件对象
     */
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            Widget oldWidget = update();
            Widget selectedItem = editorTypeComboBox.getCellWidget();
            WidgetDesignHandler handler = ExtraDesignClassManager.getInstance().getSingle(WidgetDesignHandler.XML_TAG);
            if (handler != null) {
                handler.transferWidgetProperties(oldWidget, selectedItem);
            }
            if (e.getItem() instanceof Item && ((Item) e.getItem()).getValue() instanceof WidgetConfig) {
                populate(selectedItem);
                return;
            }
            if (shouldFireSelectedEvent) {
                populateWidgetConfig(selectedItem);
            }
        }
    }

    @Override
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Widget");
    }

    public void populate(Widget widget) {
        if (widget == null) {
            editorTypeComboBox.setSelectedIndex(-1);
            return;
        }
        // 预定义组件
        if (widget instanceof NameWidget) {
            String name = ((NameWidget) widget).getName();
            shouldFireSelectedEvent = false;
            editorTypeComboBox.setSelectedItem(new Item(name, name));
            cellEditorCardPane.populate(widget);
            shouldFireSelectedEvent = true;
        }
        // 内置组件
        else {
            Class clazz = widget.getClass();
            if (ArrayUtils.contains(ButtonConstants.CLASSES4BUTTON, clazz)) {
                clazz = Button.class;
            }
            shouldFireSelectedEvent = false;
            editorTypeComboBox.setSelectedItemByWidgetClass(clazz);
            cellEditorCardPane.populate(widget);
            shouldFireSelectedEvent = true;
        }
        removeAttributeChangeListener();
        initAllListeners();
        this.addAttributeChangeListener(listener);
    }

    public Widget update() {
        return cellEditorCardPane.update();
    }

    protected void populateWidgetConfig(Widget widget) {
        this.populate(widget);
    }


    public void registerListener(){
        initAllListeners();
    }

    private static class EditorTypeComboBox extends UIComboBox {

        private Item item = new Item(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Widget_User_Defined"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Widget_User_Defined"));

        public EditorTypeComboBox(boolean userDefined) {
            this.setEditable(false);
            this.setModel(new DefaultComboBoxModel(getWidgetsName(userDefined)));
            this.setRenderer(new UIComboBoxRenderer() {
                public Component getListCellRendererComponent(JList list,
                                                              Object value, int index, boolean isSelected,
                                                              boolean cellHasFocus) {
                    if (value == item) {
                        UILabel label = new UILabel(Toolkit.i18nText("Fine-Design_Report_Widget_User_Defined")
                                + " ————");
                        label.setEnabled(false);
                        return label;
                    }
                    return super.getListCellRendererComponent(list, value,
                            index, isSelected, cellHasFocus);
                }
            });
            this.setPreferredSize(new Dimension(100, 20));
        }

        public void setSelectedItemByWidgetClass(Class clazz) {
            WidgetOption[] options = getWidgetOptions();
            for (int i = 0, l = this.getModel().getSize(); i < l; i++) {
                Item item = (Item) this.getModel().getElementAt(i);
                if (item.getValue() instanceof Integer
                        && options[(Integer) item.getValue()].widgetClass() == clazz) {
                    this.setSelectedItem(item);
                }
            }
        }

        public void setSelectedItem(Object anObject) {
            if (anObject == item) {
                return;
            }
            super.setSelectedItem(anObject);
        }

        private Vector getWidgetsName(boolean userDefined) {

            WidgetOption[] reportWidgetInstance = getWidgetOptions();
            Vector<Item> items = new Vector<Item>();
            for (int i = 0, l = reportWidgetInstance.length; i < l; i++) {
                items.add(new Item(reportWidgetInstance[i].optionName(), i));
            }
            WidgetInfoConfig manager = WidgetInfoConfig.getInstance();
            java.util.Iterator<String> nameIt = manager.getWidgetConfigNameIterator();
            if (userDefined && nameIt.hasNext()) {
                items.add(item);
                while (nameIt.hasNext()) {
                    String name = nameIt.next();
                    items.add(new Item(name, name));
                }
            }

            return items;
        }

        public Widget getCellWidget() {
            Item item = (Item) this.getSelectedItem();
            if (item.getValue() instanceof Integer) {
                return getWidgetOptions()[(Integer) item.getValue()].createWidget();
            } else if (item.getValue() instanceof String) {
                return getPredefinedWidget((String) item.getValue());
            }
            return null;
        }

        //为了保持预定义控件的配置界面不变，返回类型必须为NameWidget
        private NameWidget getPredefinedWidget(String name) {
            NameWidget nameWidget = new NameWidget(name);
            WidgetInfoConfig mgr = WidgetInfoConfig.getInstance();
            Widget widget = mgr.getWidgetConfig(name).toWidget();
            nameWidget.setEnabled(widget.isEnabled());
            nameWidget.setVisible(widget.isVisible());
            nameWidget.setWidgetName(widget.getWidgetName());
            nameWidget.setWidgetPrivilegeControl(widget.getWidgetPrivilegeControl());
            return nameWidget;
        }

        private WidgetOption[] getWidgetOptions() {
            return (WidgetOption[]) ArrayUtils.addAll(WidgetOption.getReportWidgetInstance(), ExtraDesignClassManager.getInstance().getCellWidgetOptions());
        }
    }

    /**
     * 校验
     *
     * @throws Exception 抛出异常
     */
    public void checkValid() throws Exception {
        this.cellEditorCardPane.checkValid();
    }

    public static class Item {
        private String name;
        private Object value;


        public Item(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public Object getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        /**
         * 转化成字符串形式
         *
         * @return 返回字符串
         */
        public String toString() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Item
                    && ComparatorUtils.equals(((Item) o).value, value)
                    && ComparatorUtils.equals(((Item) o).name, name);
        }
    }

    public String getIconPath() {
        return "";
    }

}
