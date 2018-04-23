package com.fr.design.gui.itableeditorpane;

import com.fr.design.border.UIRoundedBorder;
import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.util.List;

/**
 * 表格编辑面板,一般是两列.键-值 用泛型实现，用的时候请定义T.model里面的T要一样
 *
 * @editor zhou
 * @since 2012-3-28下午3:06:30
 */
public class UITableEditorPane<T> extends BasicPane {
    /**
     *
     */
    private static final long serialVersionUID = 6855793816972735815L;
    private JTable editTable;
    // 放置action 的按钮.
    private UITableModelAdapter<T> tableModel;
    private String leftLabelName;
    private JPanel buttonPane;

	public UITableEditorPane(UITableModelAdapter<T> model) {
		this.tableModel = model;
		this.initComponent(model.createAction());
	}

	public UITableEditorPane(UITableModelAdapter<T> model, String s) {
		leftLabelName = s;
		this.tableModel = model;
		this.initComponent(model.createAction());
	}

	private void initComponent(UITableEditAction[] action) {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		JPanel pane = new JPanel(new BorderLayout(4, 4));
		this.add(pane, BorderLayout.CENTER);

		UILabel l = new UILabel(leftLabelName);
		editTable = tableModel.createTable();
		editTable.getTableHeader().setBackground(UIConstants.DEFAULT_BG_RULER);

		UIScrollPane scrollPane = new UIScrollPane(editTable);
		scrollPane.setBorder(new UIRoundedBorder(UIConstants.TITLED_BORDER_COLOR, 1, UIConstants.ARC));
		pane.add(scrollPane, BorderLayout.CENTER);
		initbuttonPane(action);
		JPanel controlPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		controlPane.add(buttonPane, BorderLayout.EAST);
		controlPane.add(l, BorderLayout.WEST);
		pane.add(controlPane, BorderLayout.NORTH);

	}

    public  UITableModelAdapter<T> getTableModel(){
        return tableModel;
    }

    private void initbuttonPane(UITableEditAction[] action) {
        buttonPane = new JPanel();

        if (action != null) {
            buttonPane.setLayout(new GridLayout(1, action.length, 3, 3));
            for (int i = 0; i < action.length; i++) {
                final UIButton newButton = new UIButton(action[i]);
                newButton.set4ToolbarButton();
                newButton.setMargin(new Insets(0, 0, 0, 0));
                newButton.setText("");
                newButton.setName(action[i].getName());
                newButton.setToolTipText(action[i].getName());
                newButton.setBorder(null);
                newButton.setMargin(null);
                newButton.setOpaque(false);
                buttonPane.add(newButton);
            }
        }
    }

    /**
     * 增加事件监听
     *
     * @param l 加的东东
     */
    public void addTableListener(TableModelListener l) {
        tableModel.addTableModelListener(l);
    }

    /**
     * 移除事件监听
     *
     * @param l 去的东东
     */
    public void removeTableListener(TableModelListener l) {
        tableModel.removeTableModelListener(l);
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("TableData_Dynamic_Parameter_Setting");
    }

    public void populate(T[] objs) {
        tableModel.clear();
        if (objs == null) {
            return;
        }
        for (T obj : objs) {
            tableModel.addRow(obj);
        }
        this.tableModel.fireTableDataChanged();
        if (objs.length > 0) {
            this.editTable.getSelectionModel().setSelectionInterval(0, 0);
        }
    }

    // TODO:august这个最好还是返回数组
    public List<T> update() {
        tableModel.stopCellEditing();
        return tableModel.getList();
    }

    public void update(List list) {
        tableModel.stopCellEditing();
        tableModel.setList(list);
    }

    public int getSelectedRow() {
        return this.editTable.getSelectedRow();
    }

    public int getSelectedColumn() {
        return this.editTable.getSelectedColumn();
    }

    public JPanel getbuttonPane() {
        return buttonPane;
    }

    /**
     * 停止编辑
     */
    public void stopEditing() {
        tableModel.stopCellEditing();
    }

}