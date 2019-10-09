package com.fr.design.os.impl;

import com.fr.config.ServerPreferenceConfig;
import com.fr.design.data.datapane.connect.ConnectionManagerPane;
import com.fr.design.dcm.UniversalDatabaseOpener;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.os.OSBasedAction;
import com.fr.file.ConnectionConfig;
import com.fr.stable.os.OperatingSystem;
import com.fr.transaction.CallBackAdaptor;
import com.fr.transaction.Configurations;
import com.fr.transaction.WorkerFacade;
import static com.fr.design.actions.server.ConnectionListAction.doWithDatasourceManager;

//数据连接窗口
public class DatabaseDialogAction implements OSBasedAction {

    @Override
    public void execute() {
        if (ServerPreferenceConfig.getInstance().isUseUniverseDBM() && !OperatingSystem.isLinux()) {
            UniversalDatabaseOpener.showUniverseDatabaseDialog();
        } else {
            openDesignDatabaseManager();
        }
    }

    private void openDesignDatabaseManager() {
        DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
        final ConnectionConfig datasourceManager = ConnectionConfig.getInstance();
        final ConnectionManagerPane databaseManagerPane = new ConnectionManagerPane() {
            public void complete() {
                ConnectionConfig connectionConfig = datasourceManager.mirror();
                populate(connectionConfig);
            }

            protected void renameConnection(String oldName, String newName) {
                datasourceManager.renameConnection(oldName, newName);
            }
        };
        final BasicDialog databaseListDialog = databaseManagerPane.showLargeWindow(designerFrame, null);
        databaseListDialog.addDialogActionListener(new DialogActionAdapter() {
            public void doOk() {
                if (!databaseManagerPane.isNamePermitted()) {
                    databaseListDialog.setDoOKSucceed(false);
                    return;
                }
                Configurations.modify(new WorkerFacade(ConnectionConfig.class) {
                    @Override
                    public void run() {
                        databaseManagerPane.update(datasourceManager);
                    }
                }.addCallBack(new CallBackAdaptor() {
                    @Override
                    public boolean beforeCommit() {
                        //如果更新失败，则不关闭对话框，也不写xml文件，并且将对话框定位在请重命名的那个对象页面
                        return doWithDatasourceManager(datasourceManager, databaseManagerPane, databaseListDialog);
                    }

                    @Override
                    public void afterCommit() {
                        DesignerContext.getDesignerBean("databasename").refreshBeanElement();
                    }
                }));
            }
        });
        databaseListDialog.setVisible(true);
    }


}
