package com.fr.design.data.datapane;

import com.fr.base.BaseUtils;
import com.fr.base.TableData;
import com.fr.data.core.DataCoreUtils;
import com.fr.data.core.db.DBUtils;
import com.fr.data.core.db.TableProcedure;
import com.fr.data.core.db.dialect.DialectFactory;
import com.fr.data.impl.Connection;
import com.fr.data.impl.DBTableData;
import com.fr.data.operator.DataOperator;
import com.fr.design.DesignerEnvManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.RefreshLabel.Refreshable;
import com.fr.design.data.datapane.preview.PreviewLabel;
import com.fr.design.data.datapane.preview.PreviewLabel.Previewable;
import com.fr.design.data.datapane.preview.PreviewTablePane;
import com.fr.design.data.tabledata.Prepare4DataSourceChange;
import com.fr.design.gui.icombobox.FRTreeComboBox;
import com.fr.design.gui.icombobox.FilterableComboBoxModel;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxEditor;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.file.ConnectionConfig;
import com.fr.general.ComparatorUtils;
import com.fr.general.IOUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;
import com.fr.workspace.WorkContext;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @author zhou
 * @since 2012-7-11下午4:49:39
 */
public class ChoosePane extends BasicBeanPane<DataBaseItems> implements Refreshable, Previewable, Prepare4DataSourceChange {
    private static final double COLUMN_SIZE = 24;

    /**
     * 数据库
     */
    protected StringUIComboBox dsNameComboBox;

    /**
     * 模式
     */
    protected StringUIComboBox schemaBox;

    /**
     * 表名
     */
    protected FRTreeComboBox tableNameComboBox;

    private SwingWorker populateWorker;


    private PopupMenuListener popupMenuListener = new PopupMenuListener() {

        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            new Thread() {
                @Override
                public void run() {
                    calculateTableDataNames();
                }
            }.start();
        }

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        }

        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {
        }
    };


    private PopupMenuListener listener = new PopupMenuListener() {
        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {
        }

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        }

        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            executePopulateWorker();
        }
    };

    private FocusAdapter focusAdapter = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            if (schemaBox.getSelectedIndex() == -1) {
                schemaBox.updateUI();
                schemaBox.showPopup();
            }
        }
    };

    public ChoosePane() {
        this(null);
    }

    public ChoosePane(Previewable parent) {
        this(parent, -1);
    }

    public ChoosePane(Previewable parent, int labelSize) {
        this.initBasicComponet();
        this.initComponentsLayout(new PreviewLabel(parent == null ? this : parent), labelSize);
    }

    private void initBasicComponet() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        dsNameComboBox = new StringUIComboBox();
        initDsNameComboBox();

        schemaBox = new StringUIComboBox();
        schemaBox.setEditor(new ComboBoxEditor());

        tableNameComboBox = new FRTreeComboBox(new JTree(new DefaultMutableTreeNode()), tableNameTreeRenderer, false);
        tableNameComboBox.setEditable(true);
        tableNameComboBox.setRenderer(listCellRenderer);
        registerDSChangeListener();
        initBoxListener();
    }

    private void initBoxListener() {
        addDSBoxListener();
        schemaBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent evt) {
                tableNameComboBox.setSelectedItem("");
            }
        });
        schemaBox.addPopupMenuListener(listener);
        addFocusListener();
        this.tableNameComboBox.addPopupMenuListener(popupMenuListener);
    }

    protected void addDSBoxListener() {
        dsNameComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                schemaBox.setSelectedIndex(-1);
                tableNameComboBox.setSelectedItem("");
                JTree tree = tableNameComboBox.getTree();
                if (tree == null) {
                    return;
                }
                DefaultMutableTreeNode rootTreeNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
                rootTreeNode.removeAllChildren();
                rootTreeNode.add(new ExpandMutableTreeNode("Loading..."));
                ((DefaultTreeModel) tree.getModel()).reload();

            }
        });
    }

    protected void addFocusListener() {
    }

    @SuppressWarnings("unchecked")
    protected void initDsNameComboBox() {
        dsNameComboBox.setRefreshingModel(true);
        ConnectionConfig connectionConfig = ConnectionConfig.getInstance();
        List<String> dsList = new ArrayList<>();
        dsList.addAll(connectionConfig.getConnections().keySet());
        FilterableComboBoxModel dsNameComboBoxModel = new FilterableComboBoxModel(dsList);
        dsNameComboBox.setModel(dsNameComboBoxModel);
        dsNameComboBox.setRefreshingModel(false);
    }

    protected void initComponentsLayout(PreviewLabel previewLabel, int labelSize) {
        UILabel l1 = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Database") + ":");
        UILabel l2 = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Model") + ":");
        UILabel l3 = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Table") + ":");

        if (labelSize > 0) {
            Dimension pSize = new Dimension(labelSize, 25);
            l1.setPreferredSize(pSize);
            l2.setPreferredSize(pSize);
            l3.setPreferredSize(pSize);
        }

        Component[][] coms = new Component[][]{{l1, dsNameComboBox, l2, schemaBox,
                l3, this.tableNameComboBox, new RefreshLabel(this), previewLabel}};

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        JPanel northDSPane = TableLayoutHelper.createTableLayoutPane(coms, new double[]{p}, new double[]{p, f, p, f, p, f, COLUMN_SIZE, COLUMN_SIZE});

        this.add(northDSPane, BorderLayout.CENTER);

    }

    @Override
    public void populateBean(final DataBaseItems ob) {
        this.dsNameComboBox.setSelectedItem(ob.getDatabaseName());
        schemaBox.setSelectedItem(ob.getSchemaName());
        this.tableNameComboBox.setSelectedItem(ob.getTableName());
    }


    private void executePopulateWorker() {
        if (populateWorker != null) {
            populateWorker.cancel(true);
        }
        populateWorker = new SwingWorker<com.fr.data.impl.Connection, Void>() {

            @SuppressWarnings("unchecked")
            @Override
            protected com.fr.data.impl.Connection doInBackground() {
                schemaBox.setRefreshingModel(true);
                schemaBox.addItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Loading") + "...");
                schemaBox.setSelectedItem(null);
                schemaBox.setRefreshingModel(false);
                return getConnection();
            }

            @SuppressWarnings("unchecked")
            @Override
            public void done() {
                try {
                    com.fr.data.impl.Connection selectedDatabase = get();
                    String selectedItem = schemaBox.getSelectedItem();
                    schemaBox.setRefreshingModel(true);
                    schemaBox.removeAllItems();
                    schemaBox.updateUI();
                    if (selectedDatabase == null) {
                        return;
                    }
                    String[] schema = DataCoreUtils.getDatabaseSchema(selectedDatabase);
                    schema = schema == null ? new String[]{""} : schema;
                    for (String aa : schema) {
                        schemaBox.addItem(aa);
                    }
                    int index = schemaBox.getSelectedIndex();
                    if (selectedItem != null) {
                        schemaBox.setSelectedItem(selectedItem);
                    } else if (index < schema.length) {
                        schemaBox.setSelectedIndex(index);
                    }
                } catch (Exception e) {
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                }
                schemaBox.setRefreshingModel(false);
                schemaBox.removePopupMenuListener(listener);
                schemaBox.setPopupVisible(true);
                schemaBox.addPopupMenuListener(listener);
                schemaBox.removeFocusListener(focusAdapter);
                schemaBox.addFocusListener(focusAdapter);
            }
        };
        populateWorker.execute();
    }

    @Override
    public DataBaseItems updateBean() {
        return new DataBaseItems(this.getDSName(), this.schemaBox.getSelectedItem(), getTableName());
    }

    /**
     * 重置选中的box
     */
    public void resetComponets() {
        GUICoreUtils.setSelectedItemQuietly(dsNameComboBox, -1);
        GUICoreUtils.setSelectedItemQuietly(schemaBox, -1);
        GUICoreUtils.setSelectedItemQuietly(tableNameComboBox, -1);
    }

    protected com.fr.data.impl.Connection getConnection() {
        String selectedDSName = this.getDSName();
        if (StringUtils.isEmpty(selectedDSName)) {
            return null; // peter:选中了当前的零长度的节点,直接返回.
        }
        ConnectionConfig connectionConfig = ConnectionConfig.getInstance();
        for (Map.Entry<String, Connection> entry : connectionConfig.getConnections().entrySet()) {
            if (ComparatorUtils.equals(selectedDSName, entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * 刷新没多大用。而且要刷新也不是这儿刷新。
     */
    @Override
    public void refresh() {
        DBUtils.refreshDatabase();
        String schema = StringUtils.isEmpty(schemaBox.getSelectedItem()) ? null : schemaBox.getSelectedItem();
        DataCoreUtils.refreshTables(getConnection(), TableProcedure.TABLE, schema);
        JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Refresh_Successfully") + "!", com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Refresh_Database"),
                JOptionPane.INFORMATION_MESSAGE);
    }

    TreeCellRenderer tableNameTreeRenderer = new DefaultTreeCellRenderer() {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                Object userObj = node.getUserObject();
                if (userObj instanceof String) {
                    this.setIcon(IOUtils.readIcon("com/fr/design/images/m_insert/expandCell.gif"));
                } else if (userObj instanceof TableProcedure) {
                    this.setText(((TableProcedure) userObj).getName());
                }
            }
            return this;
        }
    };

    public static UIComboBoxRenderer listCellRenderer = new UIComboBoxRenderer() {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof TreePath) {
                DefaultMutableTreeNode dmt = (DefaultMutableTreeNode) ((TreePath) value).getLastPathComponent();
                if (dmt.getUserObject() instanceof TableProcedure) {
                    this.setText(((TableProcedure) dmt.getUserObject()).getName());
                } else {
                    this.setText(null);
                }
            }
            return this;
        }
    };


    /**
     * 添加项目监听事件
     *
     * @param aListener 事件监听器
     */
    public void addItemListener(ItemListener aListener) {
        this.tableNameComboBox.addItemListener(aListener);
    }

    /**
     * 删除项目监听事件
     *
     * @param aListener 事件监听器
     */
    public void removeItemListener(ItemListener aListener) {
        this.tableNameComboBox.removeItemListener(aListener);
    }

    @Override
    protected String title4PopupWindow() {
        return "choosepane";
    }

    protected void calculateTableDataNames() {
        JTree tree = tableNameComboBox.getTree();
        if (tree == null) {
            return;
        }
        DefaultMutableTreeNode rootTreeNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
        rootTreeNode.removeAllChildren();

        String selectedDSName = this.getDSName();
        com.fr.data.impl.Connection selectedDatabase = this.getConnection();
        if (selectedDatabase == null) {
            return;
        }
        try {
            String schema = StringUtils.isEmpty(this.schemaBox.getSelectedItem()) ? null : this.schemaBox.getSelectedItem();
            TableProcedure[] sqlTableArray = DataCoreUtils.getTables(selectedDatabase, TableProcedure.TABLE, schema, DesignerEnvManager.getEnvManager().isOracleSystemSpace());
            if (sqlTableArray.length > 0) {
                ExpandMutableTreeNode tableTreeNode = new ExpandMutableTreeNode(selectedDSName + "-" + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_SQL_Table"));
                rootTreeNode.add(tableTreeNode);
                for (int i = 0; i < sqlTableArray.length; i++) {
                    ExpandMutableTreeNode tableChildTreeNode = new ExpandMutableTreeNode(sqlTableArray[i]);
                    tableTreeNode.add(tableChildTreeNode);
                }
            }
            TableProcedure[] sqlViewArray = DataCoreUtils.getTables(selectedDatabase, TableProcedure.VIEW, schema, DesignerEnvManager.getEnvManager().isOracleSystemSpace());
            if (sqlViewArray.length > 0) {
                ExpandMutableTreeNode viewTreeNode = new ExpandMutableTreeNode(selectedDSName + "-" + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_SQL_View"));
                rootTreeNode.add(viewTreeNode);
                for (int i = 0; i < sqlViewArray.length; i++) {
                    ExpandMutableTreeNode viewChildTreeNode = new ExpandMutableTreeNode(sqlViewArray[i]);
                    viewTreeNode.add(viewChildTreeNode);
                }
            }
            ((DefaultTreeModel) tree.getModel()).reload();
            // daniel 展开所有tree
            TreeNode root = (TreeNode) tree.getModel().getRoot();
            TreePath parent = new TreePath(root);
            TreeNode node = (TreeNode) parent.getLastPathComponent();
            for (Enumeration e = node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                tree.expandPath(path);
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Database_Connection_Failed"),
                    com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Failed"), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 创建选中的数据集数据
     *
     * @return 数据集数据
     */
    public TableData createSelectTableData() {
        DataBaseItems paras = this.updateBean();
        boolean connect = false;
        com.fr.data.impl.Connection database = DBUtils.checkDBConnection(paras.getDatabaseName());
        if (database == null) {
            failedToFindTable();
            return TableData.EMPTY_TABLEDATA;
        }
        try {
            connect = DataOperator.getInstance().testConnection(database);
        } catch (Exception ignored) {
        }
        if (!connect) {
            DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
            JOptionPane.showMessageDialog(designerFrame, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Database_Connection_Failed"),
                    com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Failed"), JOptionPane.ERROR_MESSAGE);
            failedToFindTable();
            return null;
        }
        // 显示Table数据.

        TableData tableData = null;
        if (WorkContext.getCurrent().isLocal()) {
            tableData = new DBTableData(database, DataCoreUtils.createSelectSQL(paras.getSchemaName(), paras.getTableName(),
                    DialectFactory.getDialectByName(paras.getDatabaseName())));
        } else {
            try {
                TableData tableDataLocal = new DBTableData(database, DataCoreUtils.createSelectSQL(paras.getSchemaName(), paras.getTableName(), DialectFactory.getDialectByName(paras.getDatabaseName())));
                tableData = DataOperator.getInstance().previewTableData(tableDataLocal, java.util.Collections.EMPTY_MAP,
                        DesignerEnvManager.getEnvManager().getMaxNumberOrPreviewRow());
            } catch (Exception e) {
                failedToFindTable();
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }

        return tableData;
    }

    protected String getDSName() {
        return this.dsNameComboBox.getSelectedItem();
    }

    protected void failedToFindTable() {
    }

    protected String getTableName() {
        String tableName = "";
        Object obj = this.tableNameComboBox.getSelectedItemObject();
        if (obj == null) {
            obj = this.tableNameComboBox.getSelectedItem();
            if (obj == null) {
                obj = this.tableNameComboBox.getEditor().getItem();
            }
        }
        if (obj instanceof TreePath) {
            Object tp = ((ExpandMutableTreeNode) ((TreePath) obj).getLastPathComponent()).getUserObject();
            if (tp instanceof TableProcedure) {
                tableName = ((TableProcedure) tp).getName();
            }
        } else if (obj instanceof String) {
            tableName = (String) obj;
        }
        return tableName;
    }

    /**
     * 得到当前的ColumnName[]
     *
     * @return 返回当前的ColumnName[]
     */
    public String[] currentColumnNames() {
        String[] colNames = null;

        DataBaseItems paras = this.updateBean();
        String selectedDSName = paras.getDatabaseName();
        if (StringUtils.isBlank(selectedDSName)) {
            // peter:选中了当前的零长度的节点,直接返回.
            return new String[0];
        }

        String selectedTableObject = paras.getTableName();
        if (StringUtils.isEmpty(selectedTableObject)) {
            return new String[0];
        }
        try {
            // daniel:增加参数
            colNames = DataOperator.getInstance().getColumns(selectedDSName, paras.getSchemaName(), selectedTableObject);
        } catch (Exception e2) {
            FineLoggerFactory.getLogger().error(e2.getMessage(), e2);
        }

        if (colNames == null) {
            colNames = new String[0];
        }
        return colNames;
    }

    /**
     * 预览key value对应的数据
     *
     * @param key   键
     * @param value 值
     */
    public void preview(int key, int value) {
        PreviewTablePane.previewTableData(createSelectTableData(), key, value);
    }

    /**
     * 默认预览
     */
    @Override
    public void preview() {
        preview(-1, -1);
    }

    /**
     * 设置数据集名.
     */
    public void setTableNameComboBoxPopSize(int width, int height) {
        tableNameComboBox.setPopSize(width, height);
    }

    /**
     * 注册listener,相应数据集改变
     */
    @Override
    public void registerDSChangeListener() {
        DesignTableDataManager.addDsChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                initDsNameComboBox();
            }
        });
    }


    protected class StringUIComboBox extends UIComboBox {
        private boolean refreshingModel = false;

        @Override
        protected void fireItemStateChanged(ItemEvent e) {
            if (!isRefreshingModel()) {
                super.fireItemStateChanged(e);
            }
        }

        @Override
        public String getSelectedItem() {
            Object ob = super.getSelectedItem();
            if (ob instanceof String) {
                return (String) ob;
            } else
                return StringUtils.EMPTY;
        }

        public boolean isRefreshingModel() {
            return refreshingModel;
        }

        public void setRefreshingModel(boolean refreshingModel) {
            this.refreshingModel = refreshingModel;
        }

        @Override
        public void setSelectedItem(Object ob) {
            this.getModel().setSelectedItem(ob);
            if (ob != null && StringUtils.isEmpty(ob.toString())) {
                super.setSelectedIndex(-1);
            } else {
                super.setSelectedItem(ob);
            }
        }
    }

    private class ComboBoxEditor extends UIComboBoxEditor {
        private Object item;

        @Override
        public void setItem(Object item) {
            this.item = item;
            textField.setText((item == null) ? "" : item.toString());
        }

        @Override
        public Object getItem() {
            return this.item;
        }
    }

}
