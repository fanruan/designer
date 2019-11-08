package com.fr.design.data.datapane.connect;

import com.fr.base.BaseUtils;
import com.fr.data.impl.AbstractDatabaseConnection;
import com.fr.data.impl.Connection;
import com.fr.design.constants.UIConstants;
import com.fr.data.core.db.TableProcedure;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.ilist.TableViewList;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.GeneralContext;

import com.fr.stable.ArrayUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * 数据集编辑面板左边的部分
 *
 * @editor zhou
 * @since 2012-3-28下午10:14:59
 */
public class ConnectionTableProcedurePane extends BasicPane {
	private static int WIDTH = 155;
	private ConnectionComboBoxPanel connectionComboBox;
	private UICheckBox tableCheckBox;
	private UICheckBox viewCheckBox;
	protected UITextField searchField;
	private TableViewList tableViewList;
	private java.util.List<DoubleClickSelectedNodeOnTreeListener> listeners = new java.util.ArrayList<DoubleClickSelectedNodeOnTreeListener>();

	public ConnectionTableProcedurePane() {
		this.setLayout(new BorderLayout(4, 4));
		connectionComboBox = new ConnectionComboBoxPanel(com.fr.data.impl.Connection.class) {

			@Override
			protected void filterConnection(Connection connection, String conName, List<String> nameList) {
				filter(connection, conName, nameList);
			}

			protected void refreshItems() {
				super.refreshItems();
				if (tableViewList != null) {
					search();
				}
			}
		};
		tableViewList = new TableViewList();
		ToolTipManager.sharedInstance().registerComponent(tableViewList);
		connectionComboBox.addComboBoxActionListener(filter);
		tableViewList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() >= 2) {
					Object obj = tableViewList.getSelectedValue();
					TableProcedure tableProcedure = null;
					if (obj instanceof TableProcedure) {
						tableProcedure = (TableProcedure) obj;
					} else {
						return;
					}
					for (int i = 0; i < ConnectionTableProcedurePane.this.listeners.size(); i++) {
						ConnectionTableProcedurePane.this.listeners.get(i).actionPerformed(tableProcedure);
					}
				}
			}
		});
		JPanel filterPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		JPanel checkBoxgroupPane = createCheckBoxgroupPane();
		if (checkBoxgroupPane != null) {
			filterPane.add(createCheckBoxgroupPane(), BorderLayout.NORTH);
		}
		JPanel searchPane = new JPanel(new BorderLayout(10, 0));
		searchField = new UITextField();
		searchPane.add(searchField, BorderLayout.CENTER);
		searchField.getDocument().addDocumentListener(searchListener);
		filterPane.add(searchPane, BorderLayout.CENTER);
		UIScrollPane tableViewListPane = new UIScrollPane(tableViewList);
		tableViewListPane.setBorder(new UIRoundedBorder(UIConstants.LINE_COLOR, 1, UIConstants.ARC));
		this.add(connectionComboBox, BorderLayout.NORTH);
		this.add(tableViewListPane, BorderLayout.CENTER);
		this.add(filterPane, BorderLayout.SOUTH);
		this.setPreferredSize(new Dimension(WIDTH, getPreferredSize().height));
		addKeyMonitor();
	}

	protected void filter(Connection connection, String conName, List<String> nameList) {
		connection.addConnection(nameList, conName, new Class[]{AbstractDatabaseConnection.class});
	}

	protected void addKeyMonitor() {
		//do nothing
	}

	protected JPanel createCheckBoxgroupPane() {
		JPanel checkBoxgroupPane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(2);
		JPanel first = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		tableCheckBox = new UICheckBox();
		tableCheckBox.setSelected(true);
		tableCheckBox.addActionListener(filter);
		first.add(tableCheckBox);

		JPanel second = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		viewCheckBox = new UICheckBox();
		viewCheckBox.setSelected(true);
		viewCheckBox.addActionListener(filter);
		second.add(viewCheckBox);

		// 根据环境是否为中文设置不同的显示
		if (GeneralContext.isChineseEnv()) {
			first.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_SQL_Table"),
					BaseUtils.readIcon("/com/fr/design/images/data/tables.png"), UILabel.LEADING));
			second.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_SQL_View"),
					BaseUtils.readIcon("/com/fr/design/images/data/views.png"), UILabel.LEADING));
		} else {
			UILabel ui1 = new UILabel(BaseUtils.readIcon("/com/fr/design/images/data/tables.png"), UILabel.LEADING);
			UILabel ui2 = new UILabel(BaseUtils.readIcon("/com/fr/design/images/data/views.png"), UILabel.LEADING);
			ui1.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_SQL_Table"));
			ui2.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_SQL_View"));
			first.add(ui1);
			second.add(ui2);
		}
		checkBoxgroupPane.add(first);
		checkBoxgroupPane.add(second);

		return checkBoxgroupPane;
	}

	/**
	 * 给 itemComboBox 加上 itemListener
	 *
	 * @param itemListener
	 */
	public void addItemListener(ItemListener itemListener) {
		connectionComboBox.itemComboBox.addItemListener(itemListener);
	}

	private DocumentListener searchListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			search();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			search();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			search();
		}
	};

	private ActionListener filter = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			search();
		}
	};

	/**
	 * 选项改变，需要重新刷新下拉列表里面的项
	 */
	protected void search() {
		String selectedObj = connectionComboBox.getSelectedItem();

		String[] types = ArrayUtils.EMPTY_STRING_ARRAY;
		if (tableCheckBox != null) {
			if (tableCheckBox.isSelected()) {
				types = (String[]) ArrayUtils.add(types, TableProcedure.TABLE);
			}
			if (viewCheckBox.isSelected()) {
				types = (String[]) ArrayUtils.add(types, TableProcedure.VIEW);
			}
		} else {
			types = (String[]) ArrayUtils.add(types, TableProcedure.PROCEDURE);
		}
		tableViewList.populate(selectedObj, searchField.getText().trim(), types);
	}

	@Override
	protected String title4PopupWindow() {
		return "Connection";
	}

	/**
	 *
	 * @param l
	 */
	public void addDoubleClickListener(DoubleClickSelectedNodeOnTreeListener l) {
		this.listeners.add(l);
	}

	public void setSelectedDatabaseConnection(com.fr.data.impl.Connection db) {
		connectionComboBox.populate(db);
	}

	public String getSelectedDatabaseConnnectonName() {
		return connectionComboBox.getSelectedItem();
	}

	public static interface DoubleClickSelectedNodeOnTreeListener {
		/**
		 * 处理双击事件
		 *
		 * @param target
		 */
		public void actionPerformed(TableProcedure target);
	}
}