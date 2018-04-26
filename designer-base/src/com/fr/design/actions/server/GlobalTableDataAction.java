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
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.menu.MenuKeySet;
import com.fr.file.ProcedureConfig;
import com.fr.file.TableDataConfig;
import com.fr.general.Inter;
import com.fr.locale.InterProviderFactory;
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
                Configurations.update(new Worker() {
                    @Override
                    public void run() {
                        tableDataConfig.renameTableData(oldName, newName);
                    }

                    @Override
                    public Class<? extends Configuration>[] targets() {
                        return new Class[]{TableDataConfig.class, ProcedureConfig.class};
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
                        globalTableDataPane.update(tableDataConfig);
                        if (!doWithDatasourceManager(tableDataConfig, globalTableDataPane, globalTableDataDialog)) {
                            //如果更新失败，则不关闭对话框，也不写xml文件，并且将对话框定位在请重命名的那个对象页面
                            return;
                        }
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
        });
        globalTableDataDialog.setVisible(true);
    }

    /**
     * 是否正常更新完datasourceManager
     *
     * @param datasourceManager
     * @param tableDataManagerPane
     * @param databaseListDialog
     * @return
     */
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
        String rename = InterProviderFactory.getProvider().getLocText("FR-Engine_Please_Rename") + "!";
        if (tableDataMap.containsKey(rename)) {
            return datasourceManager.getTableDataIndex(rename);
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