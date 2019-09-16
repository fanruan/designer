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
import com.fr.design.utils.gui.UIComponentUtils;
import com.fr.design.widget.FRWidgetFactory;
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
import java.util.Objects;
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

    /**
     * 参数编辑器面板
     */
    private UITableEditorPane<ParameterProvider> editorPane;
    /**
     * 参数
     */
    private Parameter[] ps;
    /**
     * 数据集下拉框
     */
    TableDataComboBox tableNameComboBox;
    /**
     * 动态参数注入按钮
     */
    private UIButton paramButton;
    /**
     * 数据列下拉框
     */
    LazyComboBox columnNameComboBox;

    /**
     * 数据集下拉框和数据列下拉框监听器
     */
    private ItemListener itemListener;


    /**
     * 当前编辑的模板面板，用于触发保存操作
     */
    private ElementCasePane casePane;
    /**
     * 保存当前选中的 CE
     */
    private CellElement cellElement;

    private static final Pattern COLUMN_NAME_PATTERN = Pattern.compile("[^\\d]");


    /**
     * 数据集下拉框变动后修改数据列下拉框加载状态的监听器
     */
    private ItemListener isNeedReloadListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                columnNameComboBox.setLoaded(false);
            }
        }
    };

    /**
     * 创建横向布局附带显示动态参数注入按钮的数据集数据列选择面板
     */
    SelectedDataColumnPane() {
        this(true, false);
    }


    /**
     * 创建横向布局的数据集数据列选择面板
     *
     * @param showParameterButton 是否显示动态参数注入按钮
     */
    SelectedDataColumnPane(boolean showParameterButton) {
        this(showParameterButton, false);
    }

    /**
     * 创建数据集数据列选择面板
     *
     * @param showParameterButton 是否显示动态参数注入按钮
     * @param verticalLayout      是否是垂直布局
     */
    public SelectedDataColumnPane(boolean showParameterButton, boolean verticalLayout) {
        if (verticalLayout) {
            initComponentVerticalLayout();
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
            initParameterButton();
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
        UILabel dsLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_TableData") + ":");
        UILabel dcLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Data_Column") + ":");
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
    private void initComponentVerticalLayout() {
        initTableNameComboBox();
        initVerticalParameterButton();
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
        UILabel dsLabel = FRWidgetFactory.createLineWrapLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_TableData"));
        UILabel dpLabel = FRWidgetFactory.createLineWrapLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Dynamic_Parameter"));
        UILabel dcLabel = FRWidgetFactory.createLineWrapLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Data_Column"));
        double[] rowSize = new double[]{p, p, p};
        double[] colSize = new double[]{60, f};
        Component[][] components = {
                {dsLabel, UIComponentUtils.wrapWithBorderLayoutPane(tableNameComboBox)},
                {dpLabel, UIComponentUtils.wrapWithBorderLayoutPane(paramButton)},
                {dcLabel, UIComponentUtils.wrapWithBorderLayoutPane(columnNameComboBox)}
        };
        this.setLayout(new BorderLayout());
        this.add(TableLayoutHelper.createGapTableLayoutPane(components, rowSize, colSize, 8, 10));

    }


    /**
     * 更新面板数据
     *
     * @param source      数据源
     * @param cellElement 单元格
     * @param casePane    当前编辑的模板面板
     */
    public void populate(TableDataSource source, TemplateCellElement cellElement, ElementCasePane casePane) {
        tableNameComboBox.refresh(source);
        this.casePane = casePane;
        if (cellElement == null) {
            return;
        }
        this.cellElement = cellElement;
        removeListener();

        Object value = cellElement.getValue();
        if (!(value instanceof DSColumn)) {
            return;
        }
        DSColumn dsColumn = (DSColumn) value;
        String dsName = dsColumn.getDSName();
        tableNameComboBox.setSelectedTableDataByName(dsName);
        columnNameComboBox.setSelectedItem(TableDataColumn.getColumnName(dsColumn.getColumn()));
        ps = dsColumn.getParameters();

        addListener();
        //重新设置需要加载
        columnNameComboBox.setLoaded(false);
    }

    /**
     * 保存数据到单元格对象中
     *
     * @param cellElement 单元格
     */
    public void update(CellElement cellElement) {
        if (cellElement == null) {
            return;
        }
        Object value = cellElement.getValue();
        if (this.tableNameComboBox.getSelectedItem() == null && this.columnNameComboBox.getSelectedItem() == null) {
            return;
        }
        DSColumn dsColumn;
        if (value == null || !(value instanceof DSColumn)) {
            dsColumn = new DSColumn();
            cellElement.setValue(dsColumn);
        }
        dsColumn = (DSColumn) cellElement.getValue();

        SimpleDSColumn simpleDSColumn = updateColumnPane();
        dsColumn.setDSName(Objects.requireNonNull(simpleDSColumn).getDsName());
        dsColumn.setColumn(Objects.requireNonNull(simpleDSColumn).getColumn());

        dsColumn.setParameters((ps != null && ps.length > 0) ? ps : null);
    }

    /**
     * 释放模板对象
     */
    public void release() {
        this.cellElement = null;
        this.casePane = null;
        this.tableNameComboBox.setModel(new DefaultComboBoxModel());
    }

    /**
     * 更新面板
     *
     * @return 更新后的值
     */
    private SimpleDSColumn updateColumnPane() {
        SimpleDSColumn dsColumn = new SimpleDSColumn();
        TableDataWrapper tableDataWrappe = this.tableNameComboBox.getSelectedItem();
        if (tableDataWrappe == null) {
            return null;
        }
        dsColumn.setDsName(tableDataWrappe.getTableDataName());
        TableDataColumn column;
        String columnExp = (String) this.columnNameComboBox.getSelectedItem();
        if (isColumnName(columnExp)) {
            String number = Objects.requireNonNull(columnExp).substring(1);
            if (COLUMN_NAME_PATTERN.matcher(number).find()) {
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

    public void setListener(ItemListener i) {
        this.itemListener = i;
    }

    /**
     * 添加监听事件
     */
    private void addListener() {
        tableNameComboBox.addItemListener(this.itemListener);
        columnNameComboBox.addItemListener(this.itemListener);
        tableNameComboBox.addItemListener(this.isNeedReloadListener);
    }

    /**
     * 移除监听事件
     */
    private void removeListener() {
        tableNameComboBox.removeItemListener(this.itemListener);
        columnNameComboBox.removeItemListener(this.itemListener);
        tableNameComboBox.removeItemListener(this.isNeedReloadListener);
    }


    protected void initTableNameComboBox() {
        tableNameComboBox = new TableDataComboBox(DesignTableDataManager.getEditingTableDataSource());
        tableNameComboBox.setPreferredSize(new Dimension(100, 20));
    }

    @Override
    protected String title4PopupWindow() {
        return "DSColumn";
    }


    private void initParameterButton() {
        editorPane = new UITableEditorPane<>(new ParameterTableModel());
        paramButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_TableData_Dynamic_Parameter_Setting"));
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

    private void initVerticalParameterButton() {
        editorPane = new UITableEditorPane<>(new ParameterTableModel());
        paramButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Dynamic_Parameter_Injection"));
        paramButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BasicDialog paramDialog = editorPane.showSmallWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
                    @Override
                    public void doOk() {
                        List<ParameterProvider> parameterList = editorPane.update();
                        ps = parameterList.toArray(new Parameter[parameterList.size()]);
                        update(SelectedDataColumnPane.this.cellElement);
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
        return new ArrayList<>();
    }
}
