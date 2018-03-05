package com.fr.design.actions.server;


import com.fr.config.Configuration;
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
import com.fr.stable.ListMap;
import com.fr.transaction.Configurations;
import com.fr.transaction.Worker;

import javax.swing.*;
import java.awt.event.ActionEvent;

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
                populate(datasourceManager);
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
//        HashMap<String, Connection> modifyDetails = datasourceManager.getConnectionModifyDetails();
//        modifyDetails.clear();
//        Env currentEnv = FRContext.getCurrentEnv();
//        ModifiedTable localModifiedTable = datasourceManager.checkConnectionModifyTable(backupManager, currentEnv.getUserID());
        boolean isFailed = false;
//        if (currentEnv.isSupportLocalFileOperate() && !((LocalEnv) currentEnv).isNoRemoteUser()) {
//            //如果是本地，并且有远程用户时则更新自己的修改表
//            datasourceManager.updateSelfConnectionTotalModifiedTable(localModifiedTable, ModifiedTable.LOCAL_MODIFIER);
//        } else {
//            if (!currentEnv.isSupportLocalFileOperate()) {
//                //如果是远程，则去取服务器的最新的修改表,检查有没有冲突
//                ModifiedTable currentServerModifyTable = currentEnv.getDataSourceModifiedTables(DatasourceManager.CONNECTION);
//                if (localModifiedTable.checkModifiedTableConflictWithServer(currentServerModifyTable, currentEnv.getUserID())) {
//                    //有冲突，进行提示
//                    String title = Inter.getLocText(new String[]{"Select", "Single", "Setting"});
//                    int returnVal = JOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(), localModifiedTable.getWaringMessage(), title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//                    if (returnVal == JOptionPane.YES_OPTION) {
//                        //点击是，进行相应刷新去冲突
//                        datasourceManager.synchronizedWithServer(backupManager, DatasourceManager.CONNECTION);
//                        //要是有重命名冲突的，则对详细的修改表先进行修改
//                        datasourceManager.doWithConnectionConflict(localModifiedTable);
//                        localModifiedTable.removeConfilct();
//                        modifyDetails.clear();
//                        //更新面板
//                        connectionShowPane.populate(datasourceManager);
//                    } else {
//                        //更新失败，继续停留页面
//                        isFailed = true;
//                    }
//
//                }
//            }
//        }
        //存在请重命名则不能更新
        int index = isConnectionMapContainsRename(datasourceManager);
        if (index != -1) {
            isFailed = true;
            connectionShowPane.setSelectedIndex(index);
        }
        databaseListDialog.setDoOKSucceed(!isFailed);
//        //如果修改成功，则去远程端增量修改修改表
//        if (!isFailed && !currentEnv.isSupportLocalFileOperate()) {
//            currentEnv.writeDataSourceModifiedTables(localModifiedTable, DatasourceManager.CONNECTION);
//            localModifiedTable.clear();
//            modifyDetails.clear();
//        }
        return !isFailed;
    }


    /**
     * 是否包含重命名key
     *
     * @return 包含则返回序列 ,若返回-1则说明不包含重命名key
     */
    public static int isConnectionMapContainsRename(ConnectionConfig datasourceManager) {
        ListMap tableDataMap = (ListMap) datasourceManager.getConnections();
        String rename = InterProviderFactory.getProvider().getLocText("FR-Engine_Please_Rename") + "!";
        if (tableDataMap.containsKey(rename)) {
            return tableDataMap.indexOf(rename);
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