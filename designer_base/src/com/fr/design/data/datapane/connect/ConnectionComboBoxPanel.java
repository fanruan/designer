package com.fr.design.data.datapane.connect;

import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.data.impl.AbstractDatabaseConnection;
import com.fr.data.impl.Connection;
import com.fr.data.impl.NameDatabaseConnection;
import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.file.DatasourceManager;
import com.fr.file.DatasourceManagerProvider;
import com.fr.general.ComparatorUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
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
    private java.util.List<String> nameList = new ArrayList<String>();

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
    protected java.util.Iterator<String> items() {
        nameList = new ArrayList<String>();

        DatasourceManagerProvider mgr = DatasourceManager.getProviderInstance();
        java.util.Iterator<String> nameIt = mgr.getConnectionNameIterator();
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
        connectionListPane.populate(datasourceManager);
        BasicDialog connectionListDialog = connectionListPane.showLargeWindow(
                SwingUtilities.getWindowAncestor(ConnectionComboBoxPanel.this), new DialogActionAdapter() {
            public void doOk() {
                connectionListPane.update(datasourceManager);
                // marks:保存数据
                Env currentEnv = FRContext.getCurrentEnv();
                try {
                    currentEnv.writeResource(datasourceManager);
                } catch (Exception ex) {
                    FRContext.getLogger().error(ex.getMessage(), ex);
                }
            }
        });
        connectionListDialog.setVisible(true);
        refreshItems();
    }

    public void populate(com.fr.data.impl.Connection connection) {
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