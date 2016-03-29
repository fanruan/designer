package com.fr.design.data.tabledata.tabledatapane;

import com.fr.base.FRContext;
import com.fr.design.data.datapane.TableDataListPane;
import com.fr.design.gui.frpane.LoadingBasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.file.DatasourceManagerProvider;
import com.fr.general.Inter;
import com.fr.stable.project.ProjectConstants;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Map;

public class TableDataManagerPane extends LoadingBasicPane {

	private UITextField tableDataTextField;
	private TableDataListPane tableDataListPane;

	@Override
	protected void initComponents(JPanel container) {
		this.initTableDataManagerPane(container);
	}

	private void initTableDataManagerPane(JPanel container) {
		container.setLayout(FRGUIPaneFactory.createBorderLayout());

		JPanel tableDataPathPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
		container.add(tableDataPathPane, BorderLayout.NORTH);


		tableDataPathPane.add(new UILabel(Inter.getLocText("FR-Designer_Save_Path") + ":"), BorderLayout.WEST);
		this.tableDataTextField = new UITextField();
		tableDataPathPane.add(tableDataTextField, BorderLayout.CENTER);
		this.tableDataTextField.setEditable(false);
		tableDataListPane = new TableDataListPane(){
            protected void rename(String oldName,String newName){
                super.rename(oldName,newName);
                renameConnection(oldName, newName);
            }
        };
		container.add(tableDataListPane, BorderLayout.CENTER);
	}


    /**
     * 名字是否允许
     * @return 是则返回true
     */
    public  boolean isNamePermitted(){
        return tableDataListPane.isNamePermitted();
    }

    /**
     * 检查
     * @throws Exception 异常
     */
	public void checkValid() throws Exception {
		tableDataListPane.checkValid();
	}
	
	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("DS-Server_TableData");
	}

	public void populate(DatasourceManagerProvider datasourceManager) {
		this.tableDataTextField.setText(FRContext.getCurrentEnv().getPath() + File.separator + ProjectConstants.RESOURCES_NAME
				+ File.separator + datasourceManager.fileName());
		this.tableDataListPane.populate(datasourceManager);
	}

	public void update(DatasourceManagerProvider datasourceManager) {
		this.tableDataListPane.update(datasourceManager);
	}

    public Map<String, String> getDsChangedNameMap () {
        return this.tableDataListPane.getDsNameChangedMap();
    }

    /**
     * 设置选中项
     *
     * @param index 选中项的序列号
     */
    public void setSelectedIndex(int index) {
        this.tableDataListPane.setSelectedIndex(index);
    }
}