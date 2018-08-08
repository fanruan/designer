/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.actions.file;

import com.fr.base.FRContext;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.file.filetree.FileNode;
import com.fr.general.GeneralUtils;

import com.fr.locale.InterProviderFactory;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StableUtils;
import com.fr.stable.project.ProjectConstants;
import com.fr.workspace.WorkContext;
import com.fr.workspace.resource.WorkResourceOutputStream;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

/**
 * @author : richie
 * @since : 8.0
 */
public class LocalePane extends BasicPane {
    private static final String PREFIX = "fr_";
    private static final int LOCALE_NAME_LEN = 5;

    private UITabbedPane tabbedPane;
    private JTable predefinedTable;
    private JTable customTable;
    private DefaultTableModel predefineTableModel;
    private DefaultTableModel customTableModel;

    public LocalePane() {
        tabbedPane = new UITabbedPane();
        setLayout(new BorderLayout());
        final UITextField searchTextField = new UITextField();
        add(searchTextField, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        predefineTableModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        predefinedTable = new JTable(predefineTableModel);
        final TableRowSorter sorter = new TableRowSorter(predefineTableModel);
        predefinedTable.setRowSorter(sorter);

        customTableModel = new DefaultTableModel();
        customTable = new JTable(customTableModel);
        final TableRowSorter customSorter = new TableRowSorter(customTableModel);
        customTable.setRowSorter(customSorter);


        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                customSorter.setRowFilter(RowFilter.regexFilter(searchTextField.getText()));
                sorter.setRowFilter(RowFilter.regexFilter(searchTextField.getText()));
            }

            public void removeUpdate(DocumentEvent e) {
                customSorter.setRowFilter(RowFilter.regexFilter(searchTextField.getText()));
                sorter.setRowFilter(RowFilter.regexFilter(searchTextField.getText()));
            }

            public void changedUpdate(DocumentEvent e) {
                customSorter.setRowFilter(RowFilter.regexFilter(searchTextField.getText()));
                sorter.setRowFilter(RowFilter.regexFilter(searchTextField.getText()));
            }
        });


        tabbedPane.addTab(com.fr.design.i18n.Toolkit.i18nText("Preference-Predefined"), new UIScrollPane(predefinedTable));
        tabbedPane.addTab(com.fr.design.i18n.Toolkit.i18nText("Preference-Custom"), new UIScrollPane(customTable));

        loadData();
    }

    private void loadData() {
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                initPredefinedProperties();
                initCustomProperties();

                return null;
            }

            public void done() {
                predefineTableModel.fireTableDataChanged();
                customTableModel.fireTableDataChanged();
            }
        }.execute();
    }

    private void initPredefinedProperties() {
    
        Map<Locale, String> supportLocaleMap = InterProviderFactory.getProvider().getSupportLocaleMap();

        // richie:暂时去掉设计器中国际化键值对的显示
        Map<String, String> map = new HashMap<>();
        List<String> sortKeys = new ArrayList<String>();
    

        //map = InterProviderFactory.getProvider().getLocalBundle(Locale.SIMPLIFIED_CHINESE).getMap();

        sortKeys.addAll(map.keySet());
        Collections.sort(sortKeys);
    
        Map<Locale, Vector<String>> data = new HashMap<Locale, Vector<String>>();
        for (Locale locale : supportLocaleMap.keySet()) {
            Vector<String> column = new Vector<String>();
            for (String key : sortKeys) {
                column.add(InterProviderFactory.getProvider().getLocText(key));
            }
            data.put(locale, column);
        
        }
        Vector<String> keyVector = new Vector<String>();
        keyVector.addAll(sortKeys);


        predefineTableModel.addColumn(com.fr.design.i18n.Toolkit.i18nText("Key"), keyVector);
        for (Map.Entry<Locale, Vector<String>> entry : data.entrySet()) {
            predefineTableModel.addColumn(entry.getKey().getDisplayName(), entry.getValue());
        }
    }
    
    private void initCustomProperties() throws Exception {
    
        FileNode[] fileNodes = FRContext.getFileNodes().list(ProjectConstants.LOCALE_NAME);
        if (ArrayUtils.getLength(fileNodes) == 0) {
            return;
        }

        List<Properties> list = new ArrayList<Properties>();
        Set<String> keys = new HashSet<String>();
        customTableModel.addColumn(com.fr.design.i18n.Toolkit.i18nText("Key"));
        for (FileNode fileNode : fileNodes) {
            String fileName = fileNode.getName();
            if (fileName.endsWith(".properties")) {
                InputStream in = new ByteArrayInputStream(WorkContext.getWorkResource().readFully(StableUtils.pathJoin(ProjectConstants.LOCALE_NAME, fileName)));
                Properties properties = new Properties();
                properties.load(in);
                keys.addAll(properties.stringPropertyNames());
                list.add(properties);
                customTableModel.addColumn(fileName.substring(PREFIX.length(), LOCALE_NAME_LEN + PREFIX.length()));
            }
        }
        List<String> sortKeys = new ArrayList<String>(keys);
        Collections.sort(sortKeys);
        for (String key : sortKeys) {
            Vector<String> vector = new Vector<String>();
            vector.add(key);
            for (Properties aList : list) {
                vector.add(aList.getProperty(key));
            }
            customTableModel.addRow(vector);
        }
    }

    /**
	 * 保存当前编辑的国际化
	 * 
	 */
    public void save() {
    
        if (WorkContext.getCurrent() == null) {
            return;
        }
        if (customTable.getCellEditor() == null) {
            return;
        }
        customTable.getCellEditor().stopCellEditing();
        for (int i = 1, columnCount = customTableModel.getColumnCount(); i < columnCount; i ++) {
            String fileName = customTableModel.getColumnName(i);
            Properties properties = new Properties();
            for (int j = 0, rowCount = customTableModel.getRowCount(); j < rowCount; j ++) {
                properties.setProperty(GeneralUtils.objectToString(customTableModel.getValueAt(j, 0)), GeneralUtils.objectToString(customTableModel.getValueAt(j, i)));
            }
    
            try {
                OutputStream out = new WorkResourceOutputStream(StableUtils.pathJoin(ProjectConstants.LOCALE_NAME, PREFIX + fileName + ".properties"));
                properties.store(out, null);
                out.flush();
                out.close();
            } catch (Exception e) {
                FineLoggerFactory.getLogger().info(e.getMessage());
            }
        }
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Preference-Locale");
    }
}