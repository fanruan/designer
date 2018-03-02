package com.fr.design.data.datapane.connect;

import com.fr.base.FRContext;
import com.fr.config.Configuration;
import com.fr.data.impl.AbstractDatabaseConnection;
import com.fr.data.impl.Connection;
import com.fr.data.impl.NameDatabaseConnection;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.server.ConnectionListAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.file.ConnectionConfig;
import com.fr.file.DatasourceManager;
import com.fr.file.DatasourceManagerProvider;
import com.fr.general.ComparatorUtils;
import com.fr.stable.StringUtils;
import com.fr.transaction.Configurations;
import com.fr.transaction.Worker;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 选择数据连接的下拉框
 *
 * @editor zhou
 * @since 2012-3-28下午3:02:30
 */
public class ConnectionComboBoxPanel extends ItemEditableComboBoxPanel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Class<? extends Connection> cls; // 所取的Connection都是cls及其子类
    private List<String> nameList = new ArrayList<String>();

    public ConnectionComboBoxPanel(Class<? extends Connection> cls) {
        super();

        this.cls = cls;

        // alex:添加item change监听,当改变时改变DesignerEnvManager中的最近选中的数据连接
        this.itemComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                String selected = ConnectionComboBoxPanel.this.getSelectedItem();
                if (StringUtils.isNotBlank(selected)) {
                    DesignerEnvManager.getEnvManager().setRecentSelectedConnection(selected);
                }
            }
        });
        refreshItems();
    }

    /*
     * 刷新ComboBox.items
     */
    protected Iterator<String> items() {
        nameList = new ArrayList<String>();

        DatasourceManagerProvider mgr = DatasourceManager.getProviderInstance();
        Iterator<String> nameIt = mgr.getConnectionNameIterator();
        while (nameIt.hasNext()) {
            String conName = nameIt.next();
            Connection connection = mgr.getConnection(conName);
            filterConnection(connection, conName, nameList);
        }

        return nameList.iterator();
    }

    protected void filterConnection(Connection connection, String conName, List<String> nameList) {
        connection.addConnection(nameList, conName, new Class[]{AbstractDatabaseConnection.class});
    }

    public int getConnectionSize() {
        return nameList.size();
    }

    public String getConnection(int i) {
        return nameList.get(i);
    }

    /*
     * 弹出对话框编辑Items
     */
    protected void editItems() {
        final ConnectionListPane connectionListPane = new ConnectionListPane();
        final DatasourceManagerProvider datasourceManager = DatasourceManager.getProviderInstance();
        final DatasourceManager backupManager = datasourceManager.getBackUpManager();
        connectionListPane.populate(datasourceManager);
        final BasicDialog connectionListDialog = connectionListPane.showLargeWindow(
                SwingUtilities.getWindowAncestor(ConnectionComboBoxPanel.this), null);
        connectionListDialog.addDialogActionListener(new DialogActionAdapter() {
            public void doOk() {
                if (!connectionListPane.isNamePermitted()) {
                    connectionListDialog.setDoOKSucceed(false);
                    return;
                }
                Configurations.update(new Worker() {
                    @Override
                    public void run() {
                        if (!ConnectionListAction.doWithDatasourceManager(datasourceManager, backupManager, connectionListPane,
                                connectionListDialog)) {
                            //如果更新失败，则不关闭对话框，也不写xml文件，并且将对话框定位在请重命名的那个对象页面
                            return;
                        }
                        // marks:保存数据
                        ConnectionListAction.writeFile(datasourceManager);
                    }

                    @Override
                    public Class<? extends Configuration>[] targets() {
                        return new Class[]{ConnectionConfig.class};
                    }
                });

            }

            public void doCancel() {
                Configurations.update(new Worker() {
                    @Override
                    public void run() {
                        datasourceManager.synchronizedWithServer();
                    }

                    @Override
                    public Class<? extends Configuration>[] targets() {
                        return new Class[]{ConnectionConfig.class};
                    }
                });
            }
        });
        connectionListDialog.setVisible(true);
        refreshItems();
    }

    /**
     * @param connection 数据库链接
     */
    public void populate(Connection connection) {
        editButton.setEnabled(FRContext.getCurrentEnv().isRoot());
        if (connection instanceof NameDatabaseConnection) {
            this.setSelectedItem(((NameDatabaseConnection) connection).getName());
        } else {
            String s = DesignerEnvManager.getEnvManager().getRecentSelectedConnection();
            if (StringUtils.isNotBlank(s)) {
                for (int i = 0; i < this.getConnectionSize(); i++) {
                    String t = this.getConnection(i);
                    if (ComparatorUtils.equals(s, t)) {
                        this.setSelectedItem(s);
                        break;
                    }
                }
            }
            // alex:如果这个ComboBox还是没有选中,那么选中第一个
            if (StringUtils.isBlank(this.getSelectedItem()) && this.getConnectionSize() > 0) {
                this.setSelectedItem(this.getConnection(0));
            }
        }
    }
}