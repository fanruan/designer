package com.fr.design.dscolumn;

import com.fr.base.Parameter;
import com.fr.data.SimpleDSColumn;
import com.fr.data.TableDataSource;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.TableDataComboBox;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.LazyComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.Inter;
import com.fr.general.data.TableDataColumn;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.core.group.DSColumn;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 数据集列动态参数设置组件
 *
 * @author yaoh.wu
 * @version 2017年8月3日
 * 复用对话框代码，保留对话框原始布局
 * @since 8.0
 */
public class SelectedDataColumnPane extends BasicPane {
    private UITableEditorPane<ParameterProvider> editorPane;
    private Parameter[] ps;

    TableDataComboBox tableNameComboBox;
    LazyComboBox columnNameComboBox;
    private ItemListener itemListener;

    private UIButton paramButton;
    private ElementCasePane casePane;

    public SelectedDataColumnPane() {
        this(true, false, null);
    }


    public SelectedDataColumnPane(boolean showParameterButton) {
        this(showParameterButton, false, null);
    }

    public SelectedDataColumnPane(boolean showParameterButton, boolean verticalLayout, TemplateCellElement cellElement) {
        if (verticalLayout) {
            initComponentVerticalLayout(cellElement);
        } else {
            initComponent(showParameterButton);
        }
    }

    /**
     * 初始化组件
     *
     * @param showParameterButton 是否显示参数按钮
     */
    public void initComponent(boolean showParameterButton) {
        initTableNameComboBox();
        if (showParameterButton) {
            initWithParameterButton();
        }
        columnNameComboBox = new LazyComboBox() {

            @Override
            public Object[] load() {
                List<String> l = calculateColumnNameList();
                return l.toArray(new String[l.size()]);
            }

        };
        columnNameComboBox.setEditable(true);
        double p = TableLayout.PREFERRED;
        UILabel dsLabel = new UILabel(Inter.getLocText("TableData") + ":");
        UILabel dcLabel = new UILabel(Inter.getLocText("DataColumn") + ":");
        if (showParameterButton) {
            dsLabel.setPreferredSize(new Dimension(200, 25));
            dcLabel.setPreferredSize(new Dimension(200, 25));
        }
        if (showParameterButton) {
            Component[][] comps = {{dsLabel, null, dcLabel}, {tableNameComboBox, paramButton, columnNameComboBox}};
            this.add(TableLayoutHelper.createTableLayoutPane(comps, new double[]{p, p}, new double[]{p, p, p}));
        } else {
            double f = TableLayout.FILL;
            double[] columnSize = {p, f};
            double[] rowSize = {p, p};
            Component[][] components = new Component[][]{
                    new Component[]{dsLabel, tableNameComboBox},
                    new Component[]{dcLabel, columnNameComboBox}
            };
            JPanel jPanel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
            this.setLayout(new BorderLayout());
            this.add(jPanel, BorderLayout.CENTER);
        }
    }


    /**
     * 初始化竖直布局的组件
     */
    private void initComponentVerticalLayout(TemplateCellElement cellElement) {
        initTableNameComboBox();
        initWithParameterButton(cellElement);
        columnNameComboBox = new LazyComboBox() {
            @Override
            public Object[] load() {
                List<String> l = calculateColumnNameList();
                return l.toArray(new String[l.size()]);
            }
        };
        columnNameComboBox.setEditable(true);
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        UILabel dsLabel = new UILabel(Inter.getLocText("TableData"));
        UILabel dpLabel = new UILabel(Inter.getLocText("FR-Designer_Dynamic_Parameter"));
        UILabel dcLabel = new UILabel(Inter.getLocText("DataColumn"));
        dsLabel.setPreferredSize(new Dimension(60, 20));
        dpLabel.setPreferredSize(new Dimension(60, 20));
        dcLabel.setPreferredSize(new Dimension(60, 20));
        Component[][] components = {
                {dsLabel, tableNameComboBox},
                {dpLabel, paramButton},
                {dcLabel, columnNameComboBox}
        };
        this.setLayout(new BorderLayout());
        this.add(TableLayoutHelper.createGapTableLayoutPane(components, new double[]{p, p, p}, new double[]{p, f}, 8, 10));

    }


    public void populate(TableDataSource source, TemplateCellElement cellElement, ElementCasePane casePane) {
        tableNameComboBox.refresh(source);
        this.casePane = casePane;
        if (cellElement == null) {
            return;
        }
        if (itemListener != null) {
            removeListener(itemListener);
        }
        Object value = cellElement.getValue();
        if (!(value instanceof DSColumn)) {
            return;
        }
        DSColumn dsColumn = (DSColumn) value;
        String dsName = dsColumn.getDSName();
        tableNameComboBox.setSelectedTableDataByName(dsName);
        columnNameComboBox.setSelectedItem(TableDataColumn.getColumnName(dsColumn.getColumn()));
        ps = dsColumn.getParameters();

        addListener(itemListener);
    }

    public void update(CellElement cellElement) {
        if (cellElement == null) {
            return;
        }
        Object value = cellElement.getValue();
        if (this.tableNameComboBox.getSelectedItem() == null && this.columnNameComboBox.getSelectedItem() == null) {
            return;
        }
        DSColumn dsColumn = null;
        if (value == null || !(value instanceof DSColumn)) {
            dsColumn = new DSColumn();
            cellElement.setValue(dsColumn);
        }
        dsColumn = (DSColumn) cellElement.getValue();

        SimpleDSColumn simpleDSColumn = updateColumnPane();
        dsColumn.setDSName(simpleDSColumn.getDsName());
        dsColumn.setColumn(simpleDSColumn.getColumn());

        dsColumn.setParameters((ps != null && ps.length > 0) ? ps : null);
    }

    /**
     * 释放模板对象
     */
    public void release() {
        this.casePane = null;
        this.tableNameComboBox.setModel(new DefaultComboBoxModel());
    }

    /**
     * 更新面板
     *
     * @return 更新后的值
     */
    public SimpleDSColumn updateColumnPane() {
        SimpleDSColumn dsColumn = new SimpleDSColumn();
        TableDataWrapper tableDataWrappe = this.tableNameComboBox.getSelectedItem();
        if (tableDataWrappe == null) {
            return null;
        }
        dsColumn.setDsName(tableDataWrappe.getTableDataName());
        TableDataColumn column;
        String columnExp = (String) this.columnNameComboBox.getSelectedItem();
        if (isColumnName(columnExp)) {
            String number = columnExp.substring(1);
            Pattern pattern = Pattern.compile("[^\\d]");
            if (pattern.matcher(number).find()) {
                column = TableDataColumn.createColumn(columnExp);
            } else {
                int serialNumber = Integer.parseInt(columnExp.substring(1));
                column = TableDataColumn.createColumn(serialNumber);
            }
        } else {
            column = TableDataColumn.createColumn(columnExp);
        }
        dsColumn.setColumn(column);
        return dsColumn;
    }

    /**
     * 添加监听事件
     *
     * @param i 监听事件
     */
    public void addListener(ItemListener i) {
        itemListener = i;
        tableNameComboBox.addItemListener(i);
        columnNameComboBox.addItemListener(i);
    }

    /**
     * 移除监听事件
     *
     * @param i 监听事件
     */
    public void removeListener(ItemListener i) {
        tableNameComboBox.removeItemListener(i);
        columnNameComboBox.removeItemListener(i);
    }


    protected void initTableNameComboBox() {
        tableNameComboBox = new TableDataComboBox(DesignTableDataManager.getEditingTableDataSource());
        tableNameComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                columnNameComboBox.setLoaded(false);
            }
        });
        tableNameComboBox.setPreferredSize(new Dimension(100, 20));
    }

    @Override
    protected String title4PopupWindow() {
        return "DSColumn";
    }


    private void initWithParameterButton() {
        editorPane = new UITableEditorPane<ParameterProvider>(new ParameterTableModel());
        paramButton = new UIButton(Inter.getLocText("TableData_Dynamic_Parameter_Setting"));
        paramButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BasicDialog paramDialog = editorPane.showSmallWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
                    @Override
                    public void doOk() {
                        List<ParameterProvider> parameterList = editorPane.update();
                        ps = parameterList.toArray(new Parameter[parameterList.size()]);
                    }
                });

                editorPane.populate(ps == null ? new Parameter[0] : ps);
                paramDialog.setVisible(true);
            }
        });
    }

    private void initWithParameterButton(final TemplateCellElement cellElement) {
        editorPane = new UITableEditorPane<ParameterProvider>(new ParameterTableModel());
        paramButton = new UIButton(Inter.getLocText("FR-Designer-Basic_Dynamic_Parameter_Injection"));
        paramButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BasicDialog paramDialog = editorPane.showSmallWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
                    @Override
                    public void doOk() {
                        List<ParameterProvider> parameterList = editorPane.update();
                        ps = parameterList.toArray(new Parameter[parameterList.size()]);
                        update(cellElement);
                        casePane.fireTargetModified();
                    }
                });
                editorPane.populate(ps == null ? new Parameter[0] : ps);
                paramDialog.setVisible(true);
            }
        });
    }


    private boolean isColumnName(String columnExp) {
        return StringUtils.isNotBlank(columnExp) && (columnExp.length() > 0 && columnExp.charAt(0) == '#') && !columnExp.endsWith("#");
    }


    private List<String> calculateColumnNameList() {
        if (this.tableNameComboBox.getSelectedItem() != null) {
            return this.tableNameComboBox.getSelectedItem().calculateColumnNameList();
        }
        return new ArrayList<String>();
    }
}