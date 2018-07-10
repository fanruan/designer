package com.fr.design.actions.help;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.stable.ProductConstants;

public class SystemInfoPane extends JPanel {
    public SystemInfoPane() {
        super(FRGUIPaneFactory.createBorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableModel.addColumn(Inter.getLocText("Property"));
        tableModel.addColumn(Inter.getLocText("Value"));

        Properties properties = System.getProperties();
        Object[] keys = new Object[properties.size()];

        int index = 0;
        Enumeration enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            keys[index++] = enumeration.nextElement();
        }
        Arrays.sort(keys);

        for (int i = 0; i < keys.length; i++) {
            Object[] tableRowData = new Object[2];
            String keyValue = keys[i].toString();
            // james:屏蔽掉exe4j的内容
            if (keyValue.indexOf("exe4j") != -1) {
            	continue;
            }
            // james：这个也是exe4j的东东
            if ("install4j.exeDir".equals(keyValue)) {
            	continue;
            }
            
        	if(keyValue.indexOf("FineReport") != -1){
        		keys[i] = keyValue.replaceAll("FineReport", ProductConstants.APP_NAME);
        	}
            
            tableRowData[0] = keys[i];
            tableRowData[1] = properties.getProperty((String) keys[i]);
            tableModel.addRow(tableRowData);
        }

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(160);
        columnModel.getColumn(1).setPreferredWidth(240);

        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}