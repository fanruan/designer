package com.fr.design.data.datapane.connect;

import com.fr.design.gui.frpane.LoadingBasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.file.ConnectionConfig;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class ConnectionManagerPane extends LoadingBasicPane implements ConnectionShowPane {
    private UITextField connectionTextField;
    private ConnectionListPane connectionListPane;

    protected void initComponents(JPanel container) {
        container.setLayout(FRGUIPaneFactory.createBorderLayout());

        JPanel connectionPathPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        container.add(connectionPathPane, BorderLayout.NORTH);

        connectionPathPane.setBorder(BorderFactory.createEmptyBorder(6, 2, 2, 2));

        connectionPathPane.add(new UILabel(Inter.getLocText("FR-Designer_Save_Path") + ":"), BorderLayout.WEST);
        this.connectionTextField = new UITextField();
        connectionPathPane.add(connectionTextField, BorderLayout.CENTER);
        this.connectionTextField.setEditable(false);
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
        return Inter.getLocText("Server-Define_Data_Connection");
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