package com.fr.design.actions.server;

import com.fr.base.BaseUtils;
import com.fr.config.Configuration;
import com.fr.design.DesignModelAdapter;
import com.fr.design.actions.UpdateAction;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.design.data.tabledata.tabledatapane.ProcedureManagerPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.file.ProcedureConfig;
import com.fr.general.Inter;
import com.fr.transaction.Configurations;
import com.fr.transaction.Worker;

import java.awt.event.ActionEvent;

public class ProcedureListAction extends UpdateAction {
    public ProcedureListAction() {
        this.setName(Inter.getLocText("Datasource-Stored_Procedure") + "(P)" + "...");
        this.setMnemonic('P');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/data/store_procedure.png"));
    }

    /**
     * 事件触发操作
     *
     * @param evt 事件
     */
    public void actionPerformed(ActionEvent evt) {
        DesignerFrame designerFrame = DesignerContext.getDesignerFrame();

        final ProcedureConfig procedureConfig = ProcedureConfig.getInstance();
        final ProcedureManagerPane databaseManagerPane = new ProcedureManagerPane() {
            public void complete() {
                populate(procedureConfig.copy());
            }
        };
        BasicDialog databaseListDialog = databaseManagerPane.showLargeWindow(designerFrame, null);
        databaseListDialog.addDialogActionListener(new DialogActionAdapter() {
            public void doOk() {
                Configurations.update(new Worker() {
                    @Override
                    public void run() {
                        DesignTableDataManager.clearGlobalDs();
                        databaseManagerPane.update(procedureConfig);
                        TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter());
                    }

                    @Override
                    public Class<? extends Configuration>[] targets() {
                        return new Class[]{ProcedureConfig.class};
                    }
                });

            }
        });
        databaseListDialog.setVisible(true);
    }

    public void update() {
        this.setEnabled(true);
    }
}