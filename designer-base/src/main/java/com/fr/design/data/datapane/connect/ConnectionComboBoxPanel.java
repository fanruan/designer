package com.fr.design.data.datapane.connect;

import com.fr.data.impl.AbstractDatabaseConnection;
import com.fr.data.impl.Connection;
import com.fr.data.impl.NameDatabaseConnection;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.server.ConnectionListAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.file.ConnectionConfig;
import com.fr.general.ComparatorUtils;
import com.fr.stable.StringUtils;
import com.fr.transaction.CallBackAdaptor;
import com.fr.transaction.Configurations;
import com.fr.transaction.WorkerFacade;
import com.fr.workspace.WorkContext;

import javax.swing.SwingUtilities;
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

        ConnectionConfig mgr = ConnectionConfig.getInstance();
        Iterator<String> nameIt = mgr.getConnections().keySet().iterator();
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
        final ConnectionConfig connectionConfig = ConnectionConfig.getInstance();
        ConnectionConfig cloned = connectionConfig.mirror();
        connectionListPane.populate(cloned);
        final BasicDialog connectionListDialog = connectionListPane.showLargeWindow(
                SwingUtilities.getWindowAncestor(ConnectionComboBoxPanel.this), null);
        connectionListDialog.addDialogActionListener(new DialogActionAdapter() {
            public void doOk() {
                if (!connectionListPane.isNamePermitted()) {
                    connectionListDialog.setDoOKSucceed(false);
                    return;
                }

                Configurations.modify(new WorkerFacade(ConnectionConfig.class) {
                    @Override
                    public void run() {
                        connectionListPane.update(connectionConfig);
                    }
                }.addCallBack(new CallBackAdaptor() {
                    @Override
                    public boolean beforeCommit() {
                        //如果更新失败，则不关闭对话框，也不写xml文件，并且将对话框定位在请重命名的那个对象页面
                        return ConnectionListAction.doWithDatasourceManager(connectionConfig, connectionListPane, connectionListDialog);
                    }

                    @Override
                    public void afterCommit() {
                        DesignerContext.getDesignerBean("databasename").refreshBeanElement();
                    }
                }));

            }
        });
        connectionListDialog.setVisible(true);
        refreshItems();
    }

    /**
     * @param connection 数据库链接
     */
    public void populate(Connection connection) {
        editButton.setEnabled(WorkContext.getCurrent().isRoot());
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