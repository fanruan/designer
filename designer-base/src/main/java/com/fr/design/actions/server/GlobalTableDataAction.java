/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.server;

import com.fr.base.BaseUtils;
import com.fr.base.TableData;
import com.fr.config.Configuration;
import com.fr.design.DesignModelAdapter;
import com.fr.design.actions.UpdateAction;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.design.data.tabledata.ResponseDataSourceChange;
import com.fr.design.data.tabledata.tabledatapane.TableDataManagerPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.NameInspector;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.menu.MenuKeySet;
import com.fr.file.ProcedureConfig;
import com.fr.file.TableDataConfig;

import com.fr.transaction.CallBackAdaptor;
import com.fr.transaction.Configurations;
import com.fr.transaction.Worker;
import com.fr.transaction.WorkerFacade;

import javax.swing.KeyStroke;
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
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_DS_Server_TableData");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    /**
     * 动作
     *
     * @param evt 事件
     */
    public void actionPerformed(ActionEvent evt) {
        final DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
        final TableDataConfig tableDataConfig = TableDataConfig.getInstance();
        final TableDataManagerPane globalTableDataPane = new TableDataManagerPane() {
            public void complete() {
    
                TableDataConfig mirror = tableDataConfig.mirror();
                populate(mirror);
            }

            protected void renameConnection(final String oldName, final String newName) {
                tableDataConfig.renameTableData(oldName, newName);
            }
        };
        final BasicDialog globalTableDataDialog = globalTableDataPane.showLargeWindow(designerFrame, null);

        globalTableDataDialog.addDialogActionListener(new DialogActionAdapter() {


            @Override
            public void doOk() {
                if (!globalTableDataPane.isNamePermitted()) {
                    globalTableDataDialog.setDoOKSucceed(false);
                    return;
                }

                DesignTableDataManager.clearGlobalDs();

                Configurations.modify(new WorkerFacade(TableDataConfig.class) {
                    @Override
                    public void run() {
                        globalTableDataPane.update(tableDataConfig);
                    }
                }.addCallBack(new CallBackAdaptor() {
                    @Override
                    public boolean beforeCommit() {
                        //如果更新失败，则不关闭对话框，也不写xml文件，并且将对话框定位在请重命名的那个对象页面
                        return doWithDatasourceManager(tableDataConfig, globalTableDataPane, globalTableDataDialog);
                    }

                    @Override
                    public void afterCommit() {
                        // 刷新共有数据集
                        TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter());
                        fireDSChanged(globalTableDataPane.getDsChangedNameMap());
                    }
                }));
            }
        });
        globalTableDataDialog.setVisible(true);
    }

    private boolean doWithDatasourceManager(TableDataConfig datasourceManager, TableDataManagerPane tableDataManagerPane, BasicDialog databaseListDialog) {
        boolean isFailed = false;
//
        //存在请重命名则不能更新
        int index = isTableDataMapContainsRename(datasourceManager);
        if (index != -1) {
            isFailed = true;
            tableDataManagerPane.setSelectedIndex(index);
        }
        databaseListDialog.setDoOKSucceed(!isFailed);
        return !isFailed;
    }

    /**
     * 是否包含重命名key
     *
     * @return 包含则返回序列 ,若返回-1则说明不包含重命名key
     */
    public int isTableDataMapContainsRename(TableDataConfig datasourceManager) {
        Map<String, TableData> tableDataMap = datasourceManager.getTableDatas();
        if (tableDataMap.containsKey(NameInspector.ILLEGAL_NAME_HOLDER)) {
            return datasourceManager.getTableDataIndex(NameInspector.ILLEGAL_NAME_HOLDER);
        }
        return -1;
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