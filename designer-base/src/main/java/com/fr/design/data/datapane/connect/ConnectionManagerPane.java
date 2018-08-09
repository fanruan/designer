package com.fr.design.data.datapane.connect;

import com.fr.design.gui.frpane.LoadingBasicPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.file.ConnectionConfig;


import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class ConnectionManagerPane extends LoadingBasicPane implements ConnectionShowPane {

    private ConnectionListPane connectionListPane;

    protected void initComponents(JPanel container) {
        container.setLayout(FRGUIPaneFactory.createBorderLayout());
        connectionListPane = new ConnectionListPane() {
            protected void rename(String oldName, String newName) {
                super.rename(oldName, newName);
                renameConnection(oldName, newName);
            }
        };
        container.add(connectionListPane, BorderLayout.CENTER);
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Server_Define_Data_Connection");
    }

    public HashMap<String, String> getRenameMap() {
        return connectionListPane.getRenameMap();
    }

    public void populate(ConnectionConfig datasourceManager) {
//        this.connectionTextField.setText(WorkContext.getCurrent().getPath() + File.separator + ProjectConstants.RESOURCES_NAME
//                + File.separator + datasourceManager.fileName());
        this.connectionListPane.populate(datasourceManager);
    }

    public void update(ConnectionConfig datasourceManager) {
        this.connectionListPane.update(datasourceManager);
    }

    /**
     * 设置选中项
     *
     * @param index 选中项的序列号
     */
    public void setSelectedIndex(int index) {
        this.connectionListPane.setSelectedIndex(index);
    }

    /**
     * 名字是否允许
     *
     * @return 允许返回true
     */
    public boolean isNamePermitted() {
        return connectionListPane.isNamePermitted();
    }

}