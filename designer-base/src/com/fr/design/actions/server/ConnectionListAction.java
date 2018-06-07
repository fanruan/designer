package com.fr.design.actions.server;


import com.fr.config.Configuration;
import com.fr.data.impl.Connection;
import com.fr.design.actions.UpdateAction;
import com.fr.design.data.datapane.connect.ConnectionManagerPane;
import com.fr.design.data.datapane.connect.ConnectionShowPane;
import com.fr.design.data.datapane.connect.DatabaseConnectionPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.menu.MenuKeySet;
import com.fr.file.ConnectionConfig;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.locale.InterProviderFactory;
import com.fr.transaction.Configurations;
import com.fr.transaction.Worker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Map;

/**
 * DatasourceList Action
 */
public class ConnectionListAction extends UpdateAction {

    public ConnectionListAction() {
        this.setMenuKeySet(DEFINE_DATA_CONNECTION);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(IOUtils.readIcon("/com/fr/design/images/m_web/connection.png"));
        this.setSearchText(new DatabaseConnectionPane.JDBC());
    }

    public static final MenuKeySet DEFINE_DATA_CONNECTION = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'D';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("Server-Define_Data_Connection");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    /**
     * 执行动作
     *
     * @param evt 事件
     */
    public void actionPerformed(ActionEvent evt) {
        DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
        final ConnectionConfig datasourceManager = ConnectionConfig.getInstance();
        final ConnectionManagerPane databaseManagerPane = new ConnectionManagerPane() {
            public void complete() {
                ConnectionConfig clone = datasourceManager.mirror();
                populate(clone);
            }

            protected void renameConnection(String oldName, String newName) {
                datasourceManager.renameConnection(oldName, newName);
            }
        };
        final BasicDialog databaseListDialog = databaseManagerPane.showLargeWindow(designerFrame, null);
        databaseListDialog.addDialogActionListener(new DialogActionAdapter() {
            public void doOk() {
                Configurations.update(new Worker() {
                    @Override
                    public void run() {
                        if (!databaseManagerPane.isNamePermitted()) {
                            databaseListDialog.setDoOKSucceed(false);
                            return;
                        }
                        if (!doWithDatasourceManager(datasourceManager, databaseManagerPane, databaseListDialog)) {
                            //如果更新失败，则不关闭对话框，也不写xml文件，并且将对话框定位在请重命名的那个对象页面
                            return;
                        }
                        DesignerContext.getDesignerBean("databasename").refreshBeanElement();
                    }

                    @Override
                    public Class<? extends Configuration>[] targets() {
                        return new Class[]{ConnectionConfig.class};
                    }
                });

            }
        });
        databaseListDialog.setVisible(true);
    }


    /**
     * 更新datasourceManager
     *
     * @param datasourceManager  datasource管理对象
     * @param connectionShowPane datasource面板
     * @param databaseListDialog datasource管理对话框
     * @return boolean 是否更新成功
     */
    public static boolean doWithDatasourceManager(ConnectionConfig datasourceManager, ConnectionShowPane connectionShowPane, BasicDialog databaseListDialog) {
        connectionShowPane.update(datasourceManager);
        boolean isFailed = false;
        //存在请重命名则不能更新
        int index = isConnectionMapContainsRename(datasourceManager);
        if (index != -1) {
            isFailed = true;
            connectionShowPane.setSelectedIndex(index);
        }
        databaseListDialog.setDoOKSucceed(!isFailed);

        return !isFailed;
    }


    /**
     * 是否包含重命名key
     *
     * @return 包含则返回序列 ,若返回-1则说明不包含重命名key
     */
    public static int isConnectionMapContainsRename(ConnectionConfig datasourceManager) {
        Map<String, Connection> tableDataMap = datasourceManager.getConnections();
        String rename = InterProviderFactory.getProvider().getLocText("FR-Engine_Please_Rename") + "!";
        if (tableDataMap.containsKey(rename)) {
            return datasourceManager.getConnectionIndex(rename);
        }
        //todo  这边同上面和远程修改数据集属性有关先屏蔽
//        for (int i = tableDataRenameIndex; i >= 1; i--) {
//            rename = InterProviderFactory.getProvider().getLocText("FR-Engine_Please_Rename") + i + "!";
//            if (nameTableDataMap.map.containsKey(rename)) {
//                return nameTableDataMap.map.indexOf(rename);
//            }
//        }
        return -1;
    }


    public void update() {
        this.setEnabled(true);
    }
}