/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.data.datapane.preview;

import com.fr.base.BaseUtils;
import com.fr.base.TableData;
import com.fr.data.TableDataSource;
import com.fr.data.impl.DBTableData;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.data.impl.storeproc.ProcedureDataModel;
import com.fr.data.operator.DataOperator;
import com.fr.design.DesignerEnvManager;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.iprogressbar.AutoProgressBar;
import com.fr.design.gui.itable.SortableJTable;
import com.fr.design.gui.itable.TableSorter;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.function.TIME;
import com.fr.general.FRFont;
import com.fr.log.FineLoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CancellationException;

/**
 * august： PreviewTablePane一共提供5个共有的静态方法，用来预览。
 */
public class PreviewTablePane extends BasicPane {
    private TableData tableData;
    private ProcedureDataModel storeProcedureDataModel;
    private static UINumberField maxPreviewNumberField;
    private UINumberField currentRowsField;
    private JTable preveiwTable;
    private static AutoProgressBar progressBar;
    private AutoProgressBar connectionBar;
    private java.util.List<LoadedEventListener> listeners = new ArrayList<LoadedEventListener>();
    private BasicDialog dialog;
    private SwingWorker worker;

    private UILabel refreshLabel;
    private static PreviewTablePane THIS;
    private EmbeddedTableData previewTableData;

    public static final PreviewTablePane getInstance() {
        if (THIS == null) {
            THIS = new PreviewTablePane();
        }
        return THIS;
    }

    private PreviewTablePane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        // elalke:预览行数
        JPanel previewNumberPanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        this.add(previewNumberPanel, BorderLayout.NORTH);

        JPanel currentPreviewPanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        previewNumberPanel.add(currentPreviewPanel);
        currentPreviewPanel.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Current_Preview_Rows") + ":"));

        currentRowsField = new UINumberField();
        currentPreviewPanel.add(currentRowsField);
        currentRowsField.setEditable(false);
        currentRowsField.setColumns(4);
        currentRowsField.setInteger(true);

        JPanel maxPanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        previewNumberPanel.add(maxPanel);
        maxPanel.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Datasource_Maximum_Number_of_Preview_Rows") + ":"));

        maxPreviewNumberField = new UINumberField();
        maxPanel.add(maxPreviewNumberField);
        maxPreviewNumberField.setColumns(4);
        maxPreviewNumberField.setInteger(true);

        DesignerEnvManager designerEnvManager = DesignerEnvManager.getEnvManager();
        maxPreviewNumberField.setValue(designerEnvManager.getMaxNumberOrPreviewRow());

        maxPreviewNumberField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                DesignerEnvManager designerEnvManager = DesignerEnvManager.getEnvManager();
                designerEnvManager.setMaxNumberOrPreviewRow((int) ((UINumberField) evt.getSource()).getValue());
            }
        });

        Icon refreshImage = BaseUtils.readIcon("/com/fr/design/images/control/refresh.png");
        refreshLabel = new UILabel(refreshImage);
        previewNumberPanel.add(refreshLabel);
        refreshLabel.addMouseListener(new MouseAdapter() {
            boolean mouseEntered = false;
            boolean buttonPressed = false;

            public void mouseEntered(MouseEvent e) { // 当鼠标进入时候调用.
                mouseEntered = true;
                if (!buttonPressed) {
                    refreshLabel.setBackground(java.awt.Color.WHITE);
                    refreshLabel.setOpaque(true);
                    refreshLabel.setBorder(BorderFactory.createLineBorder(java.awt.Color.GRAY));
                }
            }

            public void mouseExited(MouseEvent e) {
                mouseEntered = false;
                refreshLabel.setOpaque(false);
                refreshLabel.setBorder(BorderFactory.createEmptyBorder());
            }

            public void mousePressed(MouseEvent e) {
                buttonPressed = true;
                refreshLabel.setBackground(java.awt.Color.lightGray);
            }

            public void mouseReleased(MouseEvent e) {
                buttonPressed = false;
                if (mouseEntered) {
                    refreshLabel.setBackground(java.awt.Color.WHITE);
                    try {
                        populate(tableData);
                        if (storeProcedureDataModel != null) {
                            populateStoreDataSQL();
                        }
                    } catch (Exception e1) {
                    }
                }
            }
        });

        preveiwTable = new SortableJTable(new TableSorter());
        preveiwTable.setRowSelectionAllowed(false);
        preveiwTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        this.add(new JScrollPane(preveiwTable), BorderLayout.CENTER);
        if (this.dialog == null) {
            this.dialog = this.showWindow(new JFrame());
        }
        progressBar = new AutoProgressBar(this, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Loading_Data"), "", 0, 100) {
            public void doMonitorCanceled() {
                if (getWorker() != null) {
                    getWorker().cancel(true);
                }
                getDialog().setVisible(false);
            }
        };
    }

    public AutoProgressBar getProgressBar() {
        return this.progressBar;
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preview");
    }

    private void addLoadedListener(LoadedEventListener l) {
        listeners.add(l);
    }

    private void fireLoadedListener() {
        for (LoadedEventListener l : listeners) {
            l.fireLoaded();
        }
    }

    /**
     * sets current row count.
     *
     * @param currentRows
     */
    private void setCurrentRows(int currentRows) {
        this.currentRowsField.setValue(currentRows);
    }

    private void resetPreviewTableColumnColor() {
        this.listeners.clear();

    }

    public BasicDialog getDialog() {
        return this.dialog;
    }

    public SwingWorker getWorker() {
        return this.worker;
    }

    // elake:为预览表的columnIndex列着c色.
    private void setPreviewTableColumnColor(final int columnIndex, final Color c) {
        addLoadedListener(new LoadedEventListener() {
            @Override
            public void fireLoaded() {
                TableColumn column = preveiwTable.getColumnModel().getColumn(columnIndex);
                DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        JComponent comp = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        comp.setBackground(c);
                        comp.setBorder(BorderFactory.createRaisedBevelBorder());
                        return comp;
                    }
                };
                column.setCellRenderer(cellRenderer);
            }
        });

    }

    /**
     * 重置面板
     */
    public static void resetPreviewTable() {
        getInstance().preveiwTable = new SortableJTable(new TableSorter());
        getInstance().preveiwTable.setRowSelectionAllowed(false);
        getInstance().preveiwTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        getInstance().progressBar.close();
        getInstance().repaint();
    }

    private void setModel(TableModel tableModel) {
        TableSorter tableSorter = (TableSorter) preveiwTable.getModel();

        tableSorter.setTableModel(tableModel);

        preveiwTable.getParent().validate();
        preveiwTable.repaint();
    }


    /**
     * 直接预览数据集，没有实际值和显示值
     *
     * @param tableData tableData
     */
    public static void previewTableData(TableData tableData) {
        previewTableData(tableData, -1, -1);
    }

    /**
     * 预览数据集，keyIndex为实际值、valueIndex为显示值
     *
     * @param tableData  tableData
     * @param keyIndex
     * @param valueIndex
     */
    public static EmbeddedTableData previewTableData(TableData tableData, final int keyIndex, final int valueIndex) {
        PreviewTablePane previewTablePane = new PreviewTablePane();
        previewTablePane.setBorder(BorderFactory.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Data")));
        try {
            previewTablePane.populate(tableData);
            previewTablePane.resetPreviewTableColumnColor();

            if (keyIndex > -1) {
                previewTablePane.setPreviewTableColumnColor(keyIndex, Color.getHSBColor(0, 204, 204));
            }
            if (valueIndex > -1) {
                previewTablePane.setPreviewTableColumnColor(valueIndex, Color.lightGray);
            }

        } catch (Exception exp) {
            previewTablePane.setModel(new PreviewTableModel((int) previewTablePane.maxPreviewNumberField.getValue()));
            previewTablePane.showErrorMessage(exp);
        }
        if (!previewTablePane.dialog.isVisible()) {
            previewTablePane.dialog.setVisible(true);
        }

        return previewTablePane.previewTableData;
    }

    private void showErrorMessage(Exception exp) {
        String errMessage = exp.getLocalizedMessage();
        String columnErrMessage = errMessage.substring(0, errMessage.indexOf(">="));
        String tatolColumnErrMessage = errMessage.substring(errMessage.indexOf(">=") + 2);
        try {
            int choiceColumn = Integer.parseInt(columnErrMessage.trim());
            int tatalColumn = Integer.parseInt(tatolColumnErrMessage.trim());
            columnErrMessage = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Tabledata_Preview_Warn_Text", choiceColumn + 1, tatalColumn);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            return;
        }
        FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
        JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), columnErrMessage, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Error"), JOptionPane.ERROR_MESSAGE);
    }

    private void populate(TableData tableData) throws Exception {
        this.tableData = tableData;

        // p:直接预览.
        if (tableData != null) {
            previewTableDataSQL();
        }
    }

    private void previewTableDataSQL() throws Exception {
        connectionBar = new AutoProgressBar(this, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Now_Create_Connection"), "", 0, 100) {
            public void doMonitorCanceled() {
                getWorker().cancel(true);
                getDialog().setVisible(false);
            }
        };
        setWorker();
        worker.execute();

    }


    private void setPreviewTableColumnValue(final Graphics g) {
        for (int i = 0; i < preveiwTable.getColumnModel().getColumnCount(); i++) {
            TableColumn column = preveiwTable.getColumnModel().getColumn(i);
            DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JComponent comp = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    Font f = table.getFont();

                    //默认在系统不支持 无法显示时  如自造的字 ，字体设置为空.
                    Font defaultShowFont = FRFont.getInstance("", f.getStyle(), f.getSize());
                    if (value instanceof String) {
                        String str = (String) value;
                        for (int j = 0; j < str.length(); j++) {
                            char c = str.charAt(j);
                            if (!f.canDisplay(c)) {
                                table.setFont(defaultShowFont);
                            }
                        }
                    }
                    return comp;
                }
            };
            column.setCellRenderer(cellRenderer);
        }
    }


    private void setWorker() {

        worker = new SwingWorker<PreviewTableModel, Void>() {
            protected PreviewTableModel doInBackground() throws Exception {
                connectionBar.start();
                try {
                    if (tableData instanceof DBTableData) {
                        boolean status = DataOperator.getInstance().testConnection(((DBTableData) tableData).getDatabase());
                        if (!status) {
                            throw new Exception(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Database_Connection_Failed"));
                        }
                    }
                } finally {
                    connectionBar.close();
                }
                TableDataSource dataSource = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().getTarget();
                previewTableData = DesignTableDataManager.previewTableDataNeedInputParameters(dataSource, tableData, (int) maxPreviewNumberField.getValue(), true);
                // parameterInputDialog
                // update之后的parameters,转成一个parameterMap,用于预览TableData
                PreviewTableModel previewModel = new PreviewTableModel(previewTableData.createDataModel(null), (int) maxPreviewNumberField.getValue());
                for (int i = 0; i < previewTableData.getColumnCount(); i++) {
                    Class<?> cls = previewTableData.getColumnClass(i);
                    if (cls == Date.class || cls == TIME.class || cls == Timestamp.class) {
                        previewModel.dateIndexs.add(i);
                    }
                }
                return previewModel;
            }

            public void done() {
                try {
                    PreviewTableModel model = get();
                    setModel(model);
                    setCurrentRows(model.getRowCount());
                    setPreviewTableColumnValue(getParent().getGraphics());
                    fireLoadedListener();
                } catch (Exception e) {
                    if (!(e instanceof CancellationException)) {
                        FineLoggerFactory.getLogger().error(e.getMessage(), e);
                        JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), e.getMessage(),
                                null, 0, UIManager.getIcon("OptionPane.errorIcon"));
                    }
                    dialog.setVisible(false);
                } finally {
                    progressBar.close();
                }
            }
        };
    }

    /**
     * 直接预览存储过程的一个返回数据集，没有实际值和显示值
     *
     * @param storeProcedureDataModel storeProcedureDataModel
     */
    public static void previewStoreData(ProcedureDataModel storeProcedureDataModel) {
        previewStoreData(storeProcedureDataModel, -1, -1);
    }

    /**
     * 预览存储过程的一个返回数据集，keyIndex为实际值、valueIndex为显示值
     *
     * @param storeProcedureDataModel storeProcedureDataModel
     * @param keyIndex                实际值
     * @param valueIndex              显示值
     */
    public static void previewStoreData(final ProcedureDataModel storeProcedureDataModel, final int keyIndex, final int valueIndex) {
        final PreviewTablePane previewTablePane = new PreviewTablePane();
        previewTablePane.storeProcedureDataModel = storeProcedureDataModel;
        previewTablePane.setBorder(BorderFactory.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Data")));

        try {
            previewTablePane.populateStoreDataSQL();
            previewTablePane.resetPreviewTableColumnColor();

            if (keyIndex > -1) {
                previewTablePane.setPreviewTableColumnColor(keyIndex, Color.getHSBColor(0, 204, 204));
            }
            if (valueIndex > -1) {
                previewTablePane.setPreviewTableColumnColor(valueIndex, Color.lightGray);
            }

        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        previewTablePane.fireLoadedListener();
        previewTablePane.showWindow(DesignerContext.getDesignerFrame()).setVisible(true);
    }

    /**
     * 直接预览存储过程的所有返回数据集，没有实际值和显示值
     *
     * @param storeProcedureDataModels storeProcedureDataModels
     */
    public static void previewStoreDataWithAllDs(ProcedureDataModel[] storeProcedureDataModels) {
        UITabbedPane tabPreviewpane = new UITabbedPane();
        int tableSize = storeProcedureDataModels.length;
        for (int i = 0; i < tableSize; i++) {
            PreviewTablePane previewTablePane = new PreviewTablePane();
            previewTablePane.storeProcedureDataModel = storeProcedureDataModels[i];
            previewTablePane.setBorder(BorderFactory.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Data")));
            try {
                previewTablePane.populateStoreDataSQL();
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
            tabPreviewpane.addTab(storeProcedureDataModels[i].getName(), previewTablePane);
        }

        BasicPane prieviewPane = new BasicPane() {

            @Override
            protected String title4PopupWindow() {
                return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preview");
            }

        };
        prieviewPane.setLayout(FRGUIPaneFactory.createBorderLayout());
        prieviewPane.add(tabPreviewpane, BorderLayout.CENTER);
        prieviewPane.showWindow(DesignerContext.getDesignerFrame()).setVisible(true);
    }

    private void populateStoreDataSQL() throws Exception {
        PreviewTableModel previewModel;
        try {
            previewModel = new PreviewTableModel(storeProcedureDataModel, (int) maxPreviewNumberField.getValue());
        } catch (Exception e) {
            previewModel = new PreviewTableModel((int) maxPreviewNumberField.getValue());
        }
        setModel(previewModel);
        setCurrentRows(previewModel.getRowCount());
        fireLoadedListener();

    }
}
