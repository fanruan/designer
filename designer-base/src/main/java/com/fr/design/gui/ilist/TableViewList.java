package com.fr.design.gui.ilist;

import com.fr.base.BaseUtils;
import com.fr.data.core.DataCoreUtils;
import com.fr.data.core.db.TableProcedure;
import com.fr.data.core.db.dialect.base.key.check.DataBaseDetail;
import com.fr.data.core.db.dialect.base.key.check.DataBaseType;
import com.fr.data.impl.Connection;
import com.fr.data.operator.DataOperator;
import com.fr.design.DesignerEnvManager;
import com.fr.design.constants.UIConstants;
import com.fr.design.mainframe.dnd.SerializableTransferable;
import com.fr.file.ConnectionConfig;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import java.awt.Component;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.CancellationException;

/**
 * 表或者视图或者存储过程组成的一个下拉列表
 *
 * @author zhou
 * @since 2012-3-28下午10:07:34
 */
public class TableViewList extends UIList {

    /**
     *
     */
    private static final long serialVersionUID = 2297780743855004708L;
    private SwingWorker refreshList;
    private Object object = null;


    public TableViewList() {
        super();
        this.setBackground(UIConstants.NORMAL_BACKGROUND);
        this.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.setCellRenderer(new TableListCellRenderer());
        new TableProcessorTreeDragSource(this, DnDConstants.ACTION_COPY);
        this.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                TableViewList.this.clearSelection();
            }
        });
        this.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                object = getSelectedValue();
            }

        });

    }

    /**
     * august：databaseName是数据库名字，searchFilter是输入的过滤条件,typesFilter是视图、表、
     * 存储过程中的一者或者几者
     *
     * @param databaseName
     * @param searchFilter
     * @param typesFilter
     */
    public void populate(final String databaseName, final String searchFilter, final String... typesFilter) {
        DefaultListModel defaultListModel = new DefaultListModel();
        defaultListModel.addElement(UIConstants.PENDING);
        final DefaultListModel failed = new DefaultListModel();
        failed.addElement(UIConstants.CONNECTION_FAILED);
        this.setModel(defaultListModel);
        if (refreshList != null) {
            refreshList.cancel(true);
        }
        refreshList = new SwingWorker<DefaultListModel, Void>() {

            @Override
            protected DefaultListModel doInBackground() throws Exception {
                Connection datasource = ConnectionConfig.getInstance().getConnection(databaseName);
                boolean status = false;
                int count = 3;
                //总共给3次连接的机会
                while (!status && count > 0) {
                    status = DataOperator.getInstance().testConnection(datasource);
                    count--;
                }
                if (!status) {
                    throw new Exception(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Database_Connection_Failed"));
                }
                return processDataInAnotherThread(databaseName, searchFilter, typesFilter);
            }

            @Override
            public void done() {
                try {
                    TableViewList.this.setModel(get());
                } catch (Exception e) {
                    if (!(e instanceof InterruptedException) && !(e instanceof CancellationException)) {
                        TableViewList.this.setModel(failed);
                        FineLoggerFactory.getLogger().error(e.getMessage(), e);
                    }
                }
            }
        };
        if (databaseName != null) {
            refreshList.execute();
        }
    }

    /**
     * august：databaseName是数据库名字，searchFilter是输入的过滤条件,typesFilter是视图、表、
     * 存储过程中的一者或者几者
     *
     * @param databaseName
     * @param searchFilter
     * @param typesFilter
     */
    private DefaultListModel processDataInAnotherThread(String databaseName, String searchFilter, String... typesFilter) throws Exception {
        DefaultListModel defaultListModel = new DefaultListModel();
        Connection datasource = ConnectionConfig.getInstance().getConnection(databaseName);
        if (datasource == null) {
            return defaultListModel;
        }
        String[] schemas = DataCoreUtils.getDatabaseSchema(datasource);

        searchFilter = searchFilter.toLowerCase();
        boolean isOracleSystemSpace = DesignerEnvManager.getEnvManager().isOracleSystemSpace();
        // oracle不勾选显示所有表，则只显示用户下的(包括存储过程和table表)
        DataBaseDetail detail = DataOperator.getInstance().getDataBaseDetail(datasource, isOracleSystemSpace);
        if (ArrayUtils.isNotEmpty(detail.getSchemas())) {
            schemas = detail.getSchemas();
        }
        if (typesFilter.length == 1 && ComparatorUtils.equals(typesFilter[0], TableProcedure.PROCEDURE)) {
            return processStoreProcedure(defaultListModel, schemas, datasource, DataBaseType.ORACLE.equals(detail.getType()), searchFilter);
        } else {
            return processTableAndView(defaultListModel, schemas, datasource, searchFilter, DataBaseType.ORACLE.equals(detail.getType()), typesFilter);
        }
    }

    private DefaultListModel processStoreProcedure(DefaultListModel defaultListModel, String[] schemas, Connection datasource, boolean isOracle, String searchFilter) throws Exception {
        boolean isBlank = StringUtils.isBlank(searchFilter);
        @SuppressWarnings("unchecked")
        boolean isOracleSysSpace = DesignerEnvManager.getEnvManager().isOracleSystemSpace();
        List<TableProcedure[]> sqlTablees = DataCoreUtils.getProcedures(datasource, schemas, isOracle, isOracleSysSpace);
        for (TableProcedure[] sqlTables : sqlTablees) {
            if (sqlTables == null) {
                continue;
            }
            for (TableProcedure sqlTable : sqlTables) {
                String name = sqlTable.toString().toLowerCase();
                if (isBlank || name.indexOf(searchFilter) != -1) {
                    defaultListModel.addElement(sqlTable);
                }
            }
        }
        return defaultListModel;
    }

    private DefaultListModel processTableAndView(DefaultListModel defaultListModel, String[] schemas, Connection datasource, String searchFilter, boolean isOracle, String... typesFilter)
            throws Exception {
        boolean isBlank = StringUtils.isBlank(searchFilter);
        boolean isOracleSystemSpace = DesignerEnvManager.getEnvManager().isOracleSystemSpace();
        if (!isOracle) {
            String schema = null;
            for (String type : typesFilter) {
                //非oracle数据库，默认都是显示所有表的，参数为true
                TableProcedure[] sqlTables = DataCoreUtils.getTables(datasource, type, schema, true);
                for (int i = 0; i < sqlTables.length; i++) {
                    if (isBlank || sqlTables[i].getName().toLowerCase().indexOf(searchFilter) != -1) {
                        defaultListModel.addElement(sqlTables[i]);
                    }
                }
            }
        } else {
            for (String type : typesFilter) {
                for (String schema : schemas) {
                    TableProcedure[] sqlTables = DataCoreUtils.getTables(datasource, type, schema, isOracleSystemSpace);
                    // oracle的表名加上模式
                    for (int i = 0; i < sqlTables.length; i++) {
                        TableProcedure ta = sqlTables[i];
                        String name = ta.getSchema() + '.' + ta.getName();
                        if (isBlank || name.toLowerCase().indexOf(searchFilter) != -1) {
                            defaultListModel.addElement(sqlTables[i]);
                        }
                    }
                }
            }
        }
        return defaultListModel;
    }

    /**
     * 显示器
     *
     * @editor zhou
     * @since 2012-3-28下午10:11:58
     */
    private class TableListCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            TableProcedure tableProcedure = null;
            if (value instanceof TableProcedure) {
                tableProcedure = (TableProcedure) value;
                this.setText(tableProcedure.toString());
            } else if (value == UIConstants.PENDING) {
                this.setText(UIConstants.PENDING.toString());
            } else {
                return this;
            }
            String type = tableProcedure == null ? null : tableProcedure.getType();
            Icon icon = null;
            if (ComparatorUtils.equals(type, TableProcedure.TABLE)) {
                icon = BaseUtils.readIcon("/com/fr/design/images/data/tables.png");
            } else if (ComparatorUtils.equals(type, TableProcedure.VIEW)) {
                icon = BaseUtils.readIcon("/com/fr/design/images/data/views.png");
            } else {
                icon = BaseUtils.readIcon("/com/fr/design/images/data/store_procedure.png");
            }
            this.setIcon(icon);

            return this;
        }
    }

    /**
     * 拖拽
     *
     * @editor zhou
     * @since 2012-3-28下午10:11:36
     */
    private class TableProcessorTreeDragSource extends DragSourceAdapter implements DragGestureListener {
        private DragSource source;

        public TableProcessorTreeDragSource(JList jList, int actions) {
            source = new DragSource();
            source.createDefaultDragGestureRecognizer(jList, actions, this);
        }


        /**
         * Drag Gesture Handler
         */
        public void dragGestureRecognized(DragGestureEvent dge) {
            setSelectedValue(object, false);
            Component comp = dge.getComponent();
            if (!(comp instanceof TableViewList)) {
                return;
            }

            TableViewList list = (TableViewList) comp;
            TableProcedure tableProcedure = null;
            Object obj = list.getSelectedValue();
            if (obj instanceof TableProcedure) {
                tableProcedure = (TableProcedure) obj;
            }
            if (tableProcedure != null) {
                source.startDrag(dge, DragSource.DefaultLinkDrop, new SerializableTransferable(tableProcedure), this);
            }
        }
    }

}
