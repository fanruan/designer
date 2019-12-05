package com.fr.design.present.dict;

import com.fr.base.BaseFormula;
import com.fr.base.TableData;
import com.fr.data.TableDataSource;
import com.fr.data.impl.DBTableData;
import com.fr.data.impl.DynamicSQLDict;
import com.fr.data.impl.NameTableData;
import com.fr.data.impl.TableDataDictionary;
import com.fr.design.DesignModelAdapter;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.constants.UIConstants;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.TableDataComboBox;
import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.design.data.datapane.preview.PreviewLabel;
import com.fr.design.data.datapane.preview.PreviewLabel.Previewable;
import com.fr.design.data.tabledata.Prepare4DataSourceChange;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.data.tabledata.wrapper.TemplateTableDataWrapper;
import com.fr.design.editor.DoubleDeckValueEditorPane;
import com.fr.design.editor.editor.ColumnIndexEditor;
import com.fr.design.editor.editor.ColumnNameEditor;
import com.fr.design.editor.editor.Editor;
import com.fr.design.editor.editor.FormulaEditor;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;

import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

/**
 * 数据字典的数据查询面板
 *
 * @editor zhou
 * @since 2012-3-29下午1:49:24
 */
public class TableDataDictPane extends FurtherBasicBeanPane<TableDataDictionary> implements Previewable, UIObserver, Prepare4DataSourceChange {
    private static final int BEGIN = 1;
    private static final int END = 10;
    private static final int VGAP = 24;
    private static final long serialVersionUID = -5469742115988153206L;
    private static final int SELECTED_NO_TABLEDATA = -2;
    public TableDataComboBox tableDataNameComboBox;
    private DoubleDeckValueEditorPane keyColumnPane;
    private DoubleDeckValueEditorPane valueDictPane;
    private ItemListener itemListener;
    private UIObserverListener uiObserverListener;

    public TableDataDictPane() {
        initBasicComponets();
        initComponents();
        iniListener();
    }

    public static void main(String[] args) {
        JFrame jf = new JFrame("test");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel content = (JPanel) jf.getContentPane();
        content.setLayout(new BorderLayout());
        content.add(new TableDataDictPane(), BorderLayout.NORTH);
        GUICoreUtils.centerWindow(jf);
        jf.setSize(290, 400);
        jf.setVisible(true);
    }

    private void initBasicComponets() {
        tableDataNameComboBox = new TableDataComboBox(DesignTableDataManager.getEditingTableDataSource());
        tableDataNameComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    tdChange(e);
                }
            }
        });
//        keyColumnPane = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ColumnNameEditor(), new ColumnIndexEditor()});
        keyColumnPane = new DoubleDeckValueEditorPane(new Editor[]{new ColumnNameEditor(), new ColumnIndexEditor()});
        FormulaEditor formulaEditor = new FormulaEditor(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameter_Formula"));
        formulaEditor.setEnabled(true);
//        valueDictPane = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ColumnNameEditor(), new ColumnIndexEditor(), formulaEditor});
        valueDictPane = new DoubleDeckValueEditorPane(new Editor[]{new ColumnNameEditor(), new ColumnIndexEditor(), formulaEditor});
    }

    private void initComponents() {

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p, p, p, p};
        int[][] rowCount = {{1, 1}, {1, 3}, {1, 3}};

        JPanel firstLine = new JPanel(new BorderLayout(4, 0));
        firstLine.add(tableDataNameComboBox, BorderLayout.CENTER);
        firstLine.add(new PreviewLabel(this), BorderLayout.EAST);

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_DS_Table_Data") + "  ", UILabel.LEFT), firstLine},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Actual_Value") + "  ", UILabel.LEFT), keyColumnPane},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Display_Value") + "  ", UILabel.LEFT), valueDictPane},
        };

        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, VGAP, LayoutConstants.VGAP_MEDIUM);
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
    }


    private void iniListener() {
        if (shouldResponseChangeListener()) {
            this.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (uiObserverListener == null) {
                        return;
                    }
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        uiObserverListener.doChange();
                    }
                }
            });
        }
    }

    /**
     * 增加Listener
     *
     * @param aListener 将本对象的Listener指向该listener
     */
    public void addItemListener(ItemListener aListener) {
        this.itemListener = aListener;
    }

    /**
     * 该面板标题
     *
     * @return 返回是窗口显示的标题
     */
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Dic_Data_Query");
    }

    private void tdChange(final ItemEvent e) {
        TableDataWrapper tableDataWrappe = this.tableDataNameComboBox.getSelectedItem();
        if (tableDataWrappe == null) {
            return;
        }

        List<String> namelist = tableDataWrappe.calculateColumnNameList();
        String[] columnNames = null;
        if (!namelist.isEmpty()) {
            columnNames = namelist.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
        } else {
            columnNames = new String[]{""};
        }
        ColumnNameEditor columnNameEditor1 = new ColumnNameEditor(columnNames);
        columnNameEditor1.addItemListener(itemListener);
        ColumnIndexEditor columnIndexEditor1 = new ColumnIndexEditor(columnNames.length);
        columnIndexEditor1.addItemListener(itemListener);
        keyColumnPane.setEditors(new Editor[]{columnNameEditor1, columnIndexEditor1}, columnNames[0]);

        FormulaEditor formulaEditor = new FormulaEditor(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameter_Formula"));
        formulaEditor.setEnabled(true);
        formulaEditor.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent ee) {
                if (itemListener != null) {
                    itemListener.itemStateChanged(e);
                }
            }
        });
        ColumnNameEditor columnNameEditor2 = new ColumnNameEditor(columnNames);
        columnNameEditor2.addItemListener(itemListener);
        ColumnIndexEditor columnIndexEditor2 = new ColumnIndexEditor(columnNames.length);
        columnIndexEditor2.addItemListener(itemListener);
        valueDictPane.setEditors(new Editor[]{columnNameEditor2, columnIndexEditor2, formulaEditor}, columnNames[0]);
        if (itemListener != null) {
            itemListener.itemStateChanged(e);
        }
    }

    @Override
    /**
     *
     */
    public void populateBean(TableDataDictionary tableDataDict) {
        populate(tableDataDict, "");
    }

    private void populate(TableDataDictionary tableDataDict, String name) {
        if (tableDataDict == null || tableDataDict.getTableData() == TableData.EMPTY_TABLEDATA) {
            this.tableDataNameComboBox.setSelectedIndex(-1);
            this.keyColumnPane.populate(StringUtils.EMPTY);
            this.valueDictPane.populate(StringUtils.EMPTY);
            return;
        }
        if (tableDataDict.getTableData() instanceof DBTableData && !"".equals(name)) {
            this.tableDataNameComboBox.putTableDataIntoMap(name, new TemplateTableDataWrapper(
                    (DBTableData) tableDataDict.getTableData(), name));
            this.tableDataNameComboBox.setSelectedTableDataByName(name);
        } else if (tableDataDict.getTableData() instanceof NameTableData) {
            this.tableDataNameComboBox.setSelectedTableDataByName(((NameTableData) tableDataDict.getTableData())
                    .getName());
        }
        // alex:因为显示到界面上的index是以1为始的,所以要加1

        TableDataWrapper tableDataWrappe = this.tableDataNameComboBox.getSelectedItem();
        if (tableDataWrappe == null) {
            keyColumnPane.resetComponets();
            valueDictPane.resetComponets();
        } else {
            if (StringUtils.isNotEmpty(tableDataDict.getKeyColumnName())) {
                this.keyColumnPane.populate(tableDataDict.getKeyColumnName());
            } else {
                this.keyColumnPane.populate(tableDataDict.getKeyColumnIndex() + 1);
            }

            Object value = null;
            if (tableDataDict.getFormula() != null) {
                value = tableDataDict.getFormula();
            } else {
                if (StringUtils.isNotEmpty(tableDataDict.getValueColumnName())) {
                    value = tableDataDict.getValueColumnName();
                } else {
                    value = tableDataDict.getValueColumnIndex() + 1;
                }
            }

            this.valueDictPane.populate(value);
        }
    }

    /**
     * @param dsql
     */
    public void populateBean(DynamicSQLDict dsql) {
        DBTableData db = new DBTableData(dsql.getDatabaseConnection(), dsql.getSqlFormula());
        String name = "";
        TableDataSource dataSource = DesignTableDataManager.getEditingTableDataSource();
        if (dataSource != null) {
            for (int i = BEGIN; i < END; i++) {
                TableData td = dataSource.getTableData(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Dictionary_Dynamic_SQL") + i);
                if (td == null) {
                    name = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Dictionary_Dynamic_SQL") + i;
                    dataSource.putTableData(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Dictionary_Dynamic_SQL") + i, db);
                    break;
                } else {
                    if (ComparatorUtils.equals(td, db)) {
                        name = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Dictionary_Dynamic_SQL") + i;
                        break;
                    } else {
                        continue;
                    }
                }
            }
        }

        TableDataDictionary tdd = new TableDataDictionary(db, dsql.getKeyColumnIndex(), dsql.getValueColumnIndex());
        if (dsql.getFormula() != null) {
            tdd.setFormula(dsql.getFormula());
        }
        this.populate(tdd, name);
        TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter());
    }

    /**
     * @return
     */
    public TableDataDictionary updateBean() {
        TableDataDictionary tableDataDict = new TableDataDictionary();
        Object object = this.valueDictPane.update();
        // alex:因为显示到界面上的index是以1为始的,所以要减1
        // carl:假如这里的序号要变，请考虑6.2的兼容
        if (object instanceof Integer) {
            int valuleColumnIndex = (Integer) object - 1;
            String valueColumnName = StringUtils.EMPTY;

            if (this.valueDictPane.getCurrentEditor() instanceof ColumnNameEditor) {
                valueColumnName = ((ColumnNameEditor) this.valueDictPane.getCurrentEditor()).getColumnName();
                valuleColumnIndex = -1;
            }
            tableDataDict.setValueColumnIndex(valuleColumnIndex);
            tableDataDict.setValueColumnName(valueColumnName);
        } else {
            tableDataDict.setFormula(((BaseFormula) object));
        }
        TableDataWrapper tableDataWrappe = this.tableDataNameComboBox.getSelectedItem();
        if (tableDataWrappe != null) {
            tableDataDict.setTableData(new NameTableData(tableDataWrappe.getTableDataName()));
            int keyColumnIndex = (Integer) this.keyColumnPane.update() - 1;
            String keyColumnName = StringUtils.EMPTY;

            if (keyColumnPane.getCurrentEditor() instanceof ColumnNameEditor) {
                keyColumnName = ((ColumnNameEditor) this.keyColumnPane.getCurrentEditor()).getColumnName();
                keyColumnIndex = -1;
            }

            tableDataDict.setKeyColumnIndex(keyColumnIndex);
            tableDataDict.setKeyColumnName(keyColumnName);
        }

        return tableDataDict;
    }

    /**
     * 预览
     */
    public void preview() {
        TableDataWrapper tableDataWrappe = tableDataNameComboBox.getSelectedItem();
        if (tableDataWrappe == null) {
            return;
        }
        Object object = this.valueDictPane.update();
        if (object instanceof Integer) {
            tableDataWrappe.previewData((Integer) this.keyColumnPane.update() - 1, (Integer) object - 1);
        }

    }

    /**
     * 判断ob是否是TableDataDictionary类型
     *
     * @param ob 用于判断的Object
     * @return 如果是TableDataDictionary类型，则返回true
     */
    public boolean accept(Object ob) {
        return ob instanceof TableDataDictionary;
    }

    @Override
    /**
     *重置
     */
    public void reset() {
        GUICoreUtils.setSelectedItemQuietly(tableDataNameComboBox, UIConstants.PENDING);
        keyColumnPane.clearComponentsData();
        valueDictPane.clearComponentsData();
    }


    /**
     * 给组件登记一个观察者监听事件
     *
     * @param listener 观察者监听事件
     */
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;
    }

    /**
     * 是否应该相应listener事件
     *
     * @return 要是响应listener事件，则返回true
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }

    @Override
    public void registerDSChangeListener() {
        tableDataNameComboBox.registerGlobalDSChangeListener();
    }
}
