package com.fr.design.dscolumn;

import com.fr.base.Parameter;
import com.fr.design.data.DesignTableDataManager;
import com.fr.data.SimpleDSColumn;
import com.fr.data.TableDataSource;
import com.fr.design.data.datapane.TableDataComboBox;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.design.gui.icombobox.LazyComboBox;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.general.Inter;
import com.fr.general.data.TableDataColumn;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.core.group.DSColumn;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SelectedDataColumnPane extends BasicPane {
	protected UITableEditorPane<ParameterProvider> editorPane;
	protected Parameter[] ps;

	protected TableDataComboBox tableNameComboBox;
	protected LazyComboBox columnNameComboBox;
	private ItemListener itemListener;

	private UIButton paramButton;

	public SelectedDataColumnPane() {
		this(true);
	}

	public SelectedDataColumnPane(boolean showParameterButton) {
		initComponent(showParameterButton);
	}

    /**
     * 初始化组件
     *
     * @param showParameterButton 是否显示参数按钮
     *
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
		UILabel label1 = new UILabel(Inter.getLocText("TableData") + ":");
		UILabel label2 = new UILabel(Inter.getLocText("DataColumn") + ":");
		if (showParameterButton) {
			label1.setPreferredSize(new Dimension(200, 25));
			label2.setPreferredSize(new Dimension(200, 25));
		}
		if (showParameterButton) {
			Component[][] comps = {{label1, null, label2}, {tableNameComboBox, paramButton, columnNameComboBox}};
			this.add(TableLayoutHelper.createTableLayoutPane(comps, new double[]{p, p}, new double[]{p, p, p}));
		} else {
			double f = TableLayout.FILL;
			double[] columnSize = {p, f};
			double[] rowSize = {p, p};
			Component[][] components = new Component[][]{
					new Component[]{label1, tableNameComboBox},
					new Component[]{label2, columnNameComboBox}
			};
			JPanel jPanel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
			this.setLayout(new BorderLayout());
			this.add(jPanel, BorderLayout.CENTER);
		}
	}
	
	protected void initTableNameComboBox() {
		tableNameComboBox = new TableDataComboBox(DesignTableDataManager.getEditingTableDataSource());
		tableNameComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				columnNameComboBox.setLoaded(false);
//				columnNameComboBox.loadList();
			}
		});
		tableNameComboBox.setPreferredSize(new Dimension(100, 20));
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

	@Override
	protected String title4PopupWindow() {
		return "DSColumn";
	}

	public void populate(TableDataSource source, TemplateCellElement cellElement) {
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
     * 更新面板
     *
     * @return 更新后的值
     *
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

    private boolean isColumnName(String columnExp) {
        return StringUtils.isNotBlank(columnExp) && (columnExp.length() > 0 && columnExp.charAt(0) == '#') && !columnExp.endsWith("#");

    }

    /**
     * 添加监听事件
     *
     * @param i 监听事件
     *
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
     *
     */
	public void removeListener(ItemListener i) {
		tableNameComboBox.removeItemListener(i);
		columnNameComboBox.removeItemListener(i);
	}

	private List<String> calculateColumnNameList() {
		if (this.tableNameComboBox.getSelectedItem() != null) {
			return this.tableNameComboBox.getSelectedItem().calculateColumnNameList();
		}
		return new ArrayList<String>();
	}
}