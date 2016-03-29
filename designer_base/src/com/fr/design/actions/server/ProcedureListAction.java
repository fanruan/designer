package com.fr.design.actions.server;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.DesignModelAdapter;
import com.fr.design.actions.UpdateAction;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.design.data.tabledata.tabledatapane.ProcedureManagerPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.file.DatasourceManager;
import com.fr.file.DatasourceManagerProvider;
import com.fr.general.Inter;

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
     *
     */
	public void actionPerformed(ActionEvent evt) {
		DesignerFrame designerFrame = DesignerContext.getDesignerFrame();

		final DatasourceManagerProvider datasourceManager = DatasourceManager.getProviderInstance();
		final ProcedureManagerPane databaseManagerPane = new ProcedureManagerPane() {
			public void complete() {
				populate(datasourceManager);
			}
		};
		BasicDialog databaseListDialog = databaseManagerPane.showLargeWindow(designerFrame,null);
		databaseListDialog.addDialogActionListener(new DialogActionAdapter() {
			public void doOk() {
				DesignTableDataManager.clearGlobalDs();
				databaseManagerPane.update(datasourceManager);

				// marks:保存数据
                try {
                    FRContext.getCurrentEnv().writeResource(datasourceManager);
                } catch (Exception e) {
                    FRContext.getLogger().error(e.getMessage());
                }
                TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter());
			}
		});
		databaseListDialog.setVisible(true);
	}

	public void update() {
		this.setEnabled(true);
	}
}