/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.server;

import com.fr.base.*;
import com.fr.config.Configuration;
import com.fr.dav.LocalEnv;
import com.fr.design.DesignModelAdapter;
import com.fr.design.actions.UpdateAction;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.design.data.tabledata.ResponseDataSourceChange;
import com.fr.design.data.tabledata.tabledatapane.TableDataManagerPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.menu.MenuKeySet;
import com.fr.file.DatasourceManager;
import com.fr.file.DatasourceManagerProvider;
import com.fr.file.TableDataConfig;
import com.fr.general.Inter;
import com.fr.transaction.Configurations;
import com.fr.transaction.Worker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Global TableData.
 */
public class GlobalTableDataAction extends UpdateAction implements ResponseDataSourceChange {
    //private static TableDataManagerPane globalTableDataPane = new TableDataManagerPane();

    public GlobalTableDataAction() {
        this.setMenuKeySet(SERVER_TABLEDATA);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/data/dock/serverdatabase.png"));
    }

    public static final MenuKeySet SERVER_TABLEDATA = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'S';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("DS-Server_TableData");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    /**
     * 动作
     * @param evt 事件
     */
    public void actionPerformed(ActionEvent evt) {
        final DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
        final DatasourceManagerProvider datasourceManager = DatasourceManager.getProviderInstance();
        final DatasourceManager backupManager = datasourceManager.getBackUpManager();
        final TableDataManagerPane globalTableDataPane = new TableDataManagerPane() {
            public void complete() {
                populate(datasourceManager);
            }

            protected void renameConnection(final String oldName, final String newName) {
                Configurations.update(new Worker() {
                    @Override
                    public void run() {
                        datasourceManager.getConnectionLocalModifyTable().rename(oldName, newName);
                    }

                    @Override
                    public Class<? extends Configuration>[] targets() {
                        return new Class[]{DatasourceManager.class};
                    }
                });
            }
        };
        final BasicDialog globalTableDataDialog = globalTableDataPane.showLargeWindow(designerFrame, null);

        globalTableDataDialog.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                Configurations.update(new Worker() {
                    @Override
                    public void run() {
                        if (!globalTableDataPane.isNamePermitted()) {
                            globalTableDataDialog.setDoOKSucceed(false);
                            return;
                        }

                        DesignTableDataManager.clearGlobalDs();
                        globalTableDataPane.update(datasourceManager);
                        if (!doWithDatasourceManager(datasourceManager, backupManager, globalTableDataPane, globalTableDataDialog)) {
                            //如果更新失败，则不关闭对话框，也不写xml文件，并且将对话框定位在请重命名的那个对象页面
                            return;
                        }

                        writeFile(datasourceManager);
                        // 刷新共有数据集
                        TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter());
                        fireDSChanged(globalTableDataPane.getDsChangedNameMap());
                    }

                    @Override
                    public Class<? extends Configuration>[] targets() {
                        return new Class[]{TableDataConfig.class};
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
                        return new Class[]{TableDataConfig.class
                        };
                    }
                });
            }
        });
        globalTableDataDialog.setVisible(true);
    }

    private void writeFile(DatasourceManagerProvider datasourceManager) {

        Env currentEnv = FRContext.getCurrentEnv();
        try {
            boolean isSuccess = currentEnv.writeResource(datasourceManager);
            if (!isSuccess) {
                throw new RuntimeException(Inter.getLocText("FR-Designer_Already_exist"));
            }
        } catch (Exception e) {
            throw new RuntimeException(Inter.getLocText("FR-Designer_Already_exist"));
        }
    }


    /**
     * 是否正常更新完datasourceManager
     *
     * @param datasourceManager
     * @param databaseManagerPane
     * @return
     */
    private boolean doWithDatasourceManager(DatasourceManagerProvider datasourceManager, DatasourceManager backupManager,
                                            TableDataManagerPane tableDataManagerPane, BasicDialog databaseListDialog) {
        HashMap<String, TableData> modifyDetails = datasourceManager.getTableDataModifyDetails();
        modifyDetails.clear();
        Env currentEnv = FRContext.getCurrentEnv();
        ModifiedTable localModifiedTable = datasourceManager.checkTableDataModifyTable(backupManager, currentEnv.getUserID());
        boolean isFailed = false;
        if (currentEnv.isSupportLocalFileOperate() && !((LocalEnv) currentEnv).isNoRemoteUser()) {
            //如果是本地，并且有远程用户时则更新自己的修改表
            datasourceManager.updateSelfTableDataTotalModifiedTable(localModifiedTable, ModifiedTable.LOCAL_MODIFIER);
        } else {
            if (!currentEnv.isSupportLocalFileOperate()) {
                //如果是远程，则去取服务器的最新的修改表,检查有没有冲突
                ModifiedTable currentServerModifyTable = currentEnv.getDataSourceModifiedTables(DatasourceManager.TABLEDATA);
                if (localModifiedTable.checkModifiedTableConflictWithServer(currentServerModifyTable, currentEnv.getUserID())) {
                    //有冲突，进行提示
                    String title = Inter.getLocText(new String[]{"Select", "Single", "Setting"});
                    int returnVal = JOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(), localModifiedTable.getWaringMessage(), title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (returnVal == JOptionPane.YES_OPTION) {
                        //点击是，进行相应刷新去冲突
                        datasourceManager.synchronizedWithServer(backupManager, DatasourceManager.TABLEDATA);
                        //要是有重命名冲突的，则对详细的修改表先进行修改
                        datasourceManager.doWithTableDataConfilct(localModifiedTable);
                        localModifiedTable.removeConfilct();
                        modifyDetails.clear();
                        //更新面板
                        tableDataManagerPane.populate(datasourceManager);
                    } else {
                        //更新失败，继续停留页面
                        isFailed = true;
                    }
                }
            }
        }
        //存在请重命名则不能更新
        int index = datasourceManager.isTableDataMapContainsRename();
        if (index != -1) {
            isFailed = true;
            tableDataManagerPane.setSelectedIndex(index);
        }
        databaseListDialog.setDoOKSucceed(!isFailed);
        //如果修改成功，则去远程端增量修改修改表
        if (!isFailed && !currentEnv.isSupportLocalFileOperate()) {
            currentEnv.writeDataSourceModifiedTables(localModifiedTable, DatasourceManager.TABLEDATA);
            localModifiedTable.clear();
            modifyDetails.clear();
        }
        return !isFailed;
    }


    public void update() {
        this.setEnabled(true);
    }

    /**
     * 响应数据集改变
     */
    public void fireDSChanged() {
        fireDSChanged(new HashMap<String, String>());
    }

    /**
     * 响应数据集改变
     *
     * @param map 对应集
     */
    public void fireDSChanged(Map<String, String> map) {
        DesignTableDataManager.fireDSChanged(map);
    }
}