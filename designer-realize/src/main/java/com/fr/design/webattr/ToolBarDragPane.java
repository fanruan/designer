package com.fr.design.webattr;

import com.fr.base.BaseUtils;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.Widget;
import com.fr.report.web.Location;
import com.fr.report.web.ToolBarManager;
import com.fr.stable.ArrayUtils;
import com.fr.stable.GraphDrawHelper;
import com.fr.stable.StringUtils;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.util.List;

/**
 * 新的拖拽ToolBar button以实现自定义工具栏 报表web设置那儿的.应该不叫ToolBarDragPane，因为实际没有提供drag功能
 * 
 * @editor zhou 2012-3-22下午3:57:22
 */

public class ToolBarDragPane extends WidgetToolBarPane {
	private static final int COLUMN = 4;
	private static final int MIN_COLUMN_WIDTH = 15;
	private int row = 7;
	private DefaultTableModel toolbarButtonTableModel;
	private JTable layoutTable;
	private UICheckBox isUseToolBarCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Use_ToolBar") + ":"); // 是否使用工具栏
	private boolean isEnabled;

	public ToolBarDragPane() {
		WidgetOption[] options = ExtraDesignClassManager.getInstance().getWebWidgetOptions();
		if(options != null){
			double optionNums = options.length;
			row += Math.ceil(optionNums / COLUMN);
		}
		toolbarButtonTableModel = new TableModel(row ,COLUMN);
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		JPanel north = FRGUIPaneFactory.createBorderLayout_S_Pane();
		UIButton defaultButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Restore_Default"));
		// 恢复默认按钮
		defaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				northToolBar.removeButtonList();
				northToolBar.removeAll();
				southToolBar.removeButtonList();
				southToolBar.removeAll();
				if (defaultToolBar == null) {
					return;
				}
				ToolBarManager toolBarManager = defaultToolBar;
				toolBarManager.setToolBarLocation(Location.createTopEmbedLocation());
				ToolBarManager[] tbm = new ToolBarManager[] { toolBarManager };
				populateBean(tbm);
				ToolBarDragPane.this.repaint();
			}
		});

		north.add(isUseToolBarCheckBox, BorderLayout.WEST);
		JPanel aa = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
		aa.add(defaultButton);
		north.add(aa, BorderLayout.CENTER);
		this.add(north, BorderLayout.NORTH);

		northToolBar = new ToolBarPane();
		northToolBar.setPreferredSize(new Dimension(ImageObserver.WIDTH, 26));
		northToolBar.setBackground(Color.lightGray);

		UIButton topButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/arrow/arrow_up.png"));
		topButton.setBorder(null);
		// topButton.setMargin(null);
		topButton.setOpaque(false);
		topButton.setContentAreaFilled(false);
		topButton.setFocusPainted(false);
		topButton.setRequestFocusEnabled(false);
		topButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (isSelectedtable()) {
					WidgetOption no = (WidgetOption)layoutTable.getValueAt(layoutTable.getSelectedRow(), layoutTable.getSelectedColumn());
					Widget widget = no.createWidget();
					ToolBarButton tb = new ToolBarButton(no.optionIcon(), widget);
					tb.setNameOption(no);
					northToolBar.add(tb);
					northToolBar.validate();
					northToolBar.repaint();
				} else {
					JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Choose_One_Button"));
				}
			}
		});
		UIButton downButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/arrow/arrow_down.png"));
		downButton.setBorder(null);
		downButton.setMargin(null);
		downButton.setOpaque(false);
		downButton.setContentAreaFilled(false);
		downButton.setFocusPainted(false);
		downButton.setRequestFocusEnabled(false);
		downButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (isSelectedtable()) {
					WidgetOption no = (WidgetOption)layoutTable.getValueAt(layoutTable.getSelectedRow(), layoutTable.getSelectedColumn());
					Widget widget = no.createWidget();
					ToolBarButton tb = new ToolBarButton(no.optionIcon(), widget);
					tb.setNameOption(no);
					southToolBar.add(tb);
					southToolBar.validate();
					southToolBar.repaint();
				} else {
					JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Choose_One_Button"));
				}
			}
		});

		initLayoutTable();

		JPanel center = FRGUIPaneFactory.createBorderLayout_S_Pane();
		center.setBackground(Color.WHITE);
		center.add(topButton, BorderLayout.NORTH);
		JPanel small = FRGUIPaneFactory.createBorderLayout_S_Pane();
		small.setBackground(Color.WHITE);
		small.add(new UILabel(StringUtils.BLANK), BorderLayout.NORTH);
		small.add(layoutTable, BorderLayout.CENTER);
		center.add(small, BorderLayout.CENTER);
		center.add(downButton, BorderLayout.SOUTH);
		southToolBar = new ToolBarPane();
		southToolBar.setPreferredSize(new Dimension(ImageObserver.WIDTH, 26));
		southToolBar.setBackground(Color.lightGray);
		JPanel movePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		JPanel northContentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		SettingToolBar top = new SettingToolBar(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ToolBar_Top"), northToolBar);
		northContentPane.add(top, BorderLayout.EAST);
		northContentPane.add(northToolBar, BorderLayout.CENTER);
		northContentPane.setBackground(Color.lightGray);

		JPanel southContentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		SettingToolBar bottom = new SettingToolBar(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ToolBar_Bottom"), southToolBar);
		southContentPane.add(bottom, BorderLayout.EAST);
		southContentPane.add(southToolBar, BorderLayout.CENTER);
		southContentPane.setBackground(Color.lightGray);

		movePane.add(northContentPane, BorderLayout.NORTH);
		movePane.add(center, BorderLayout.CENTER);
		movePane.add(southContentPane, BorderLayout.SOUTH);

		this.add(new JScrollPane(movePane), BorderLayout.CENTER);
		isUseToolBarCheckBox.setSelected(true);
	}

	private void initLayoutTable() {
		layoutTable = new JTable(toolbarButtonTableModel);
		layoutTable.setDefaultRenderer(Object.class, tableRenderer);
		layoutTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		layoutTable.setColumnSelectionAllowed(false);
		layoutTable.setRowSelectionAllowed(false);
		layoutTable.setBackground(Color.WHITE);
		layoutTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 1 && !(SwingUtilities.isRightMouseButton(e)) && isEnabled) {
					WidgetOption no = (WidgetOption)layoutTable.getValueAt(layoutTable.getSelectedRow(), layoutTable.getSelectedColumn());
					Widget widget = no.createWidget();
					ToolBarButton tb = new ToolBarButton(no.optionIcon(), widget);
					tb.setNameOption(no);
					northToolBar.add(tb);
					northToolBar.validate();
					northToolBar.repaint();
				}
			}
		});
	}

	// 根据控件名称长度，设置合适的列宽
	private static void resizeColumnWidth(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel();
		for (int column = 0; column < table.getColumnCount(); column++) {
			int width = MIN_COLUMN_WIDTH; // Min width
			for (int row = 0; row < table.getRowCount(); row++) {
				WidgetOption widgetOption = (WidgetOption) table.getValueAt(row, column);
				if (widgetOption == null) {
					continue;
				}
				String optionName = widgetOption.optionName();
				width = Math.max(GraphDrawHelper.getWidth(optionName), width);
			}
			columnModel.getColumn(column).setPreferredWidth(width);
		}
	}


	private boolean isSelectedtable() {
		for (int i = 0; i < layoutTable.getColumnCount(); i++) {
			if (layoutTable.isColumnSelected(i)) {
				return true;
			}
		}
		return false;
	}

	public void setAllEnabled(boolean b) {
		GUICoreUtils.setEnabled(this, b);
		isEnabled = b;
		removeAllListener(northToolBar.getToolBarButtons());
		removeAllListener(southToolBar.getToolBarButtons());
		removeToolBarListener(northToolBar);
		removeToolBarListener(southToolBar);
	}

	private void removeToolBarListener(ToolBarPane toolBarPane) {
		if (!isEnabled) {
			toolBarPane.removeDefaultMouseListener();
		}
	}

	private void removeAllListener(List<ToolBarButton> toolBarButtons) {
		for (ToolBarButton button : toolBarButtons) {
			button.setEnabled(isEnabled);
			if (!isEnabled) {
				button.removeMouseListener(button);
			}
		}
	}

	/**
	 * 是否被选中
	 * @return 同上
	 */
	public boolean isUseToolbar() {
		return this.isUseToolBarCheckBox.isSelected();
	}

	private class TableModel extends DefaultTableModel {
		public TableModel(int i, int j) {
			super(i, j);
		}

		// 禁止jtable的双击编辑功能
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	}

	public void setDefaultToolBar(ToolBarManager defaultToolBar, WidgetOption[] buttonArray) {
		super.setDefaultToolBar(defaultToolBar);
		if (buttonArray != null) {
			for (int i = 0; i < buttonArray.length; i++) {
				toolbarButtonTableModel.setValueAt(buttonArray[i], i % row, i / row);
			}
		}
		resizeColumnWidth(layoutTable);

	}

	DefaultTableCellRenderer tableRenderer = new DefaultTableCellRenderer() {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (value instanceof WidgetOption) {
				WidgetOption nameOption = (WidgetOption)value;
				this.setText(nameOption.optionName());

				Icon icon = nameOption.optionIcon();
				if (icon != null) {
					this.setIcon(icon);
				}
			}
			if (value == null) {
				this.setText(StringUtils.EMPTY);
				this.setIcon(null);
			}
			return this;
		}
	};

	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Set_Toolbar");
	}

	public void setCheckBoxSelected(boolean b) {
		this.isUseToolBarCheckBox.setSelected(b);
	}

	@Override
	public void populateBean(ToolBarManager[] toolBarManager) {
		if (ArrayUtils.isEmpty(toolBarManager)) {
			defaultToolBar.setToolBarLocation(Location.createTopEmbedLocation());
			toolBarManager = new ToolBarManager[] { defaultToolBar };
		}
		super.populateBean(toolBarManager);
	}
	
	@Override
	public ToolBarManager[] updateBean() {
		if(!isUseToolbar()){
			return new ToolBarManager[0];
		}
		return super.updateBean();
	}

}
