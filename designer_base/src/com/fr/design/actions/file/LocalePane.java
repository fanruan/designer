/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.actions.file;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.dialog.BasicPane;
import com.fr.file.filetree.FileNode;
import com.fr.general.FRLogger;
import com.fr.general.GeneralUtils;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.project.ProjectConstants;

/**
 * @author : richie
 * @since : 8.0
 */
public class LocalePane extends BasicPane {
    private static final String FR = "fr.properties";
    private static final String US = "fr_en_US.properties";
    private static final String CN = "fr_zh_CN.properties";
    private static final String JP = "fr_ja_JP.properties";
    private static final String TW = "fr_zh_TW.properties";
    private static final String KR = "fr_ko_KR.properties";
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
            public boolean isCellEditable(int col, int row) {
                return false;
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


        tabbedPane.addTab(Inter.getLocText("Preference-Predefined"), new UIScrollPane(predefinedTable));
        tabbedPane.addTab(Inter.getLocText("Preference-Custom"), new UIScrollPane(customTable));

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
        Properties fr = loadLocaleProperties(FR);
        Properties us = loadLocaleProperties(US);
        Properties cn = loadLocaleProperties(CN);
        Properties jp = loadLocaleProperties(JP);
        Properties tw = loadLocaleProperties(TW);
        Properties kr = loadLocaleProperties(KR);

        Set<String> keys = fr.stringPropertyNames();
        List<String> sortKeys = new ArrayList<String>(keys);
        Collections.sort(sortKeys);
        Vector<String> keyVector = new Vector<String>();
        Vector<String> valueVector = new Vector<String>();
        Vector<String> usVector = new Vector<String>();
        Vector<String> cnVector = new Vector<String>();
        Vector<String> jpVector = new Vector<String>();
        Vector<String> twVector = new Vector<String>();
        Vector<String> krVector = new Vector<String>();
        for (String key : sortKeys) {
            keyVector.add(key);
            valueVector.add(fr.getProperty(key));
            usVector.add(us.getProperty(key));
            cnVector.add(cn.getProperty(key));
            jpVector.add(jp.getProperty(key));
            twVector.add(tw.getProperty(key));
            krVector.add(kr.getProperty(key));
        }
        predefineTableModel.addColumn(Inter.getLocText("Key"), keyVector);
        predefineTableModel.addColumn(Inter.getLocText("Value"), valueVector);
        predefineTableModel.addColumn("en_US", usVector);
        predefineTableModel.addColumn("zh_CN", cnVector);
        predefineTableModel.addColumn("ja_JP", jpVector);
        predefineTableModel.addColumn("zh_TW", twVector);
        predefineTableModel.addColumn("ko_KR", krVector);
    }

    private void initCustomProperties() throws Exception {
        Env env = FRContext.getCurrentEnv();
        if (env == null) {
            return;
        }
        FileNode[] fileNodes = env.listFile(ProjectConstants.LOCALE_NAME);
        if (ArrayUtils.getLength(fileNodes) == 0) {
            return;
        }

        List<Properties> list = new ArrayList<Properties>();
        Set<String> keys = new HashSet<String>();
        customTableModel.addColumn(Inter.getLocText("Key"));
        for (FileNode fileNode : fileNodes) {
            String fileName = fileNode.getName();
            if (fileName.endsWith(".properties")) {
                InputStream in = env.readBean(fileName, ProjectConstants.LOCALE_NAME);
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
            for (int i = 0; i < list.size(); i ++) {
                vector.add(list.get(i).getProperty(key));
            }
            customTableModel.addRow(vector);
        }
    }

    private Properties loadLocaleProperties(String name) {
        Properties properties = new Properties();
        InputStream inputStream = IOUtils.readResource("/com/fr/general/locale/" + name);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        return properties;
    }

    /**
	 * 保存当前编辑的国际化
	 * 
	 *
	 * @date 2014-9-30-下午3:10:30
	 */
    public void save() {
        Env env = FRContext.getCurrentEnv();
        if (env == null) {
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

            OutputStream out = null;
            try {
                out = env.writeBean(PREFIX + fileName + ".properties", ProjectConstants.LOCALE_NAME);
                properties.store(out, null);

                out.flush();
                out.close();
            } catch (Exception e) {
                FRLogger.getLogger().info(e.getMessage());
            }
        }
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("Preference-Locale");
    }
}