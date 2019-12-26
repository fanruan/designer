/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.webattr;

import com.fr.base.BaseUtils;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.GeneralUtils;
import com.fr.report.web.Printer;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ServerPrinterPane extends BasicPane {
	private JList printerList;

	//Kevin Wang: 添加五个按钮用于以图形形式提供左侧编辑打印机列表的功能。
	private UIButton addButton;
	private UIButton editButton;
	private UIButton removeButton;
	private UIButton moveUpButton;
	private UIButton moveDownButton;

	public ServerPrinterPane() {
		super();
		this.initComponents();
	}

	protected void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(6, 2, 4, 2));

		//Kevin Wang: 为左侧打印机添加五个图形形式的按钮
		JToolBar toolbar = new JToolBar();
		this.add(toolbar, BorderLayout.NORTH);

		Dimension preferDimension = new Dimension(24, 24);
		addButton = new UIButton(BaseUtils.readIcon("/com/fr/base/images/cell/control/add.png"));
		addButton.addActionListener(addActionListener);
		addButton.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Add"));//"add"
		addButton.setPreferredSize(preferDimension);

		editButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/control/edit.png"));
		editButton.addActionListener(editActionListener);
		editButton.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Edit"));//"edit"
		editButton.setPreferredSize(preferDimension);

		removeButton = new UIButton(BaseUtils.readIcon("/com/fr/base/images/cell/control/remove.png"));
		removeButton.addActionListener(this.removeActionListener);
		removeButton.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Remove"));//"remove"
		removeButton.setPreferredSize(preferDimension);

		moveUpButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/control/up.png"));
		moveUpButton.addActionListener(this.moveUpActionListener);
		moveUpButton.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Move_Up"));//"moveUp"
		moveUpButton.setPreferredSize(preferDimension);

		moveDownButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/control/down.png"));
		moveDownButton.addActionListener(this.moveDownActionListener);
		moveDownButton.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Move_Down"));//"moveDown"
		moveDownButton.setPreferredSize(preferDimension);

		toolbar.add(addButton);
		toolbar.add(editButton);
		toolbar.add(removeButton);
		toolbar.add(moveUpButton);
		toolbar.add(moveDownButton);

		printerList = new JList(new DefaultListModel());
		printerList.addListSelectionListener(printerSelectionListener);
		//shark:双击printerlist也可以编辑
		printerList.addMouseListener(mouseClickedListener);
		this.add(new JScrollPane(printerList), BorderLayout.CENTER);

		//
		this.checkButtonEnabled();
	}

	@Override
	protected String title4PopupWindow() {
		return "printer";
	}
	
	private void checkButtonEnabled() {
		this.editButton.setEnabled(false);
		this.removeButton.setEnabled(false);
		this.moveUpButton.setEnabled(false);
		this.moveDownButton.setEnabled(false);

		int selectedIndex = this.printerList.getSelectedIndex();
		if (selectedIndex >= 0) {
			this.editButton.setEnabled(true);
			this.removeButton.setEnabled(true);

			if (selectedIndex > 0) {
				this.moveUpButton.setEnabled(true);
			}

			if (selectedIndex < this.printerList.getModel().getSize() - 1) {
				this.moveDownButton.setEnabled(true);
			}
		}
	}
	
	ActionListener addActionListener = new ActionListener(){
		public void actionPerformed(ActionEvent evt){
			final PrintersPane printersPane = new PrintersPane();
			BasicDialog printerDialog = printersPane.showSmallWindow(SwingUtilities.getWindowAncestor(ServerPrinterPane.this),new DialogActionAdapter() {
				@Override
				public void doOk() {
					String newPrintName = printersPane.update();
					if (StringUtils.isNotBlank(newPrintName)) {
						DefaultListModel defaultListModel = (DefaultListModel) printerList.getModel();
						defaultListModel.addElement(newPrintName);
						printerList.setSelectedIndex(defaultListModel.size() - 1);
					}
				}                
            });
			printerDialog.setTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ReportServerP_Add_Printer") + "...");
			printerDialog.setVisible(true);
		}
	};
	
	ActionListener editActionListener = new ActionListener(){
		public void actionPerformed(ActionEvent evt){
			editPrinterList();
		}
	};

	/**
	 * Remove
	 */
	ActionListener removeActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			int selectedIndex = printerList.getSelectedIndex();
			if (selectedIndex == -1) {
				return;
			}

			int returnVal = FineJOptionPane.showConfirmDialog(ServerPrinterPane.this, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ReportServerP_Are_You_Sure_To_Delete_The_Selected_Printer")
					+ "?", com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Remove"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (returnVal == JOptionPane.OK_OPTION) {
				((DefaultListModel) printerList.getModel()).remove(selectedIndex);

				if (printerList.getModel().getSize() > 0) {
					if (selectedIndex < printerList.getModel().getSize()) {
						printerList.setSelectedIndex(selectedIndex);
					} else {
						printerList.setSelectedIndex(printerList.getModel().getSize() - 1);
					}
				}

				checkButtonEnabled();
			}
		}
	};

	/**
	 * MoveUP
	 */
	ActionListener moveUpActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			int selectedIndex = printerList.getSelectedIndex();
			if (selectedIndex > 0) {
				DefaultListModel listModel = (DefaultListModel) printerList.getModel();

				Object selecteObj1 = listModel.get(selectedIndex - 1);
				listModel.set(selectedIndex - 1, listModel.get(selectedIndex));
				listModel.set(selectedIndex, selecteObj1);

				printerList.setSelectedIndex(selectedIndex - 1);
				checkButtonEnabled();
			}
		}
	};

	/**
	 * MoveDown
	 */
	ActionListener moveDownActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			int selectedIndex = printerList.getSelectedIndex();
			if (selectedIndex == -1) {
				return;
			}

			if (selectedIndex < printerList.getModel().getSize() - 1) {
				DefaultListModel listModel = (DefaultListModel) printerList.getModel();

				Object selecteObj1 = listModel.get(selectedIndex + 1);
				listModel.set(selectedIndex + 1, listModel.get(selectedIndex));
				listModel.set(selectedIndex, selecteObj1);

				printerList.setSelectedIndex(selectedIndex + 1);
				checkButtonEnabled();
			}
		}
	};

	/**
	 * List selection listener
	 */
	ListSelectionListener printerSelectionListener = new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent evt) {
			checkButtonEnabled();
		}
	};
	
	MouseAdapter mouseClickedListener = new MouseAdapter(){
		@Override
		public void mouseClicked(MouseEvent e){
			int clickedNumber = e.getClickCount();
			//shark: 如果点击次数大于2认为发生双击，弹出编辑界面
			if(clickedNumber >= 2){
				editPrinterList();
			}
		}
	};
	//shark:服务器列表编辑
	public void editPrinterList(){
		//sahrk 获取选中列
		final int index = printerList.getSelectedIndex();
		final PrintersPane printersPane = new PrintersPane();
		BasicDialog printerDialog = printersPane.showSmallWindow(SwingUtilities.getWindowAncestor(ServerPrinterPane.this),new DialogActionAdapter() {
			@Override
			public void doOk() {
				String newPrintName = printersPane.update();

				if (StringUtils.isNotBlank(newPrintName)) {
					DefaultListModel defaultListModel = (DefaultListModel) printerList.getModel();
					
					//shark 把该列删除 再在原位置插入新列 相当于替换
	              defaultListModel.remove(index);
	              defaultListModel.add(index, newPrintName);
					printerList.setSelectedIndex(index);
				}
			}                
        });
		printersPane.populate(printerList.getSelectedValue().toString());
		printerDialog.setTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ReportServerP_Edit_Printer") + "...");
		printerDialog.setVisible(true);
	}
	public void populate(Printer printer) {
		if (printer == null) {
			return;
		}
		if (printer.getPrinters() != null) {
			String[] serverPrinterList = printer.getPrinters();
			DefaultListModel defaultListModel = (DefaultListModel) this.printerList.getModel();
			defaultListModel.removeAllElements();

			for (int i = 0; i < serverPrinterList.length; i++) {
				defaultListModel.addElement(serverPrinterList[i]);

			}

			if (defaultListModel.size() > 0) {
				this.printerList.setSelectedIndex(0);
			}
		}
	}

	public Printer update() {
		Printer printer = new Printer();
		
		List serverPrinterList = new ArrayList();

		DefaultListModel defaultListModel = (DefaultListModel) this.printerList.getModel();
		for (int i = 0; i < defaultListModel.size(); i++) {
			serverPrinterList.add(defaultListModel.get(i));
		}
		if (serverPrinterList.size() > 0) {
			int printerCount = serverPrinterList.size();
			String[] printers = new String[printerCount];
			for (int i = 0; i < printerCount; i++) {
				printers[i] = serverPrinterList.get(i).toString();
			}
			printer.setPrinters(printers);
		} else {
			printer.setPrinters(null);
		}
		
		return printer;
	}

	public class PrintersPane extends com.fr.design.dialog.BasicPane {
		private UIComboBox printerCombo;

		public PrintersPane() {
			this.initComponents();
		}


		protected void initComponents() {
			this.setLayout(FRGUIPaneFactory.createBorderLayout());
			this.setBorder(BorderFactory.createEmptyBorder(20, 5, 0, 0));
			JPanel centerPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
			centerPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Printer") + ":"), BorderLayout.WEST);

			DefaultComboBoxModel printerComboModel = new DefaultComboBoxModel();

			printerCombo = new UIComboBox(printerComboModel);
			centerPane.add(printerCombo);

			// populate printer list.
			String[] serverPrinterList = GeneralUtils.getSystemPrinterNameArray();

			for (int d = 0; d < serverPrinterList.length; d++) {
				printerComboModel.addElement(serverPrinterList[d]);
			}
			
			this.add(centerPane);
		}

		/**
		 * Check valid.
		 */
		@Override
		public void checkValid() throws Exception {
			String printerName = printerCombo.getSelectedItem().toString();
			if (StringUtils.isBlank(printerName)) {
				throw new Exception(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ReportServerP_The_Name_Of_Printer_Cannot_Be_Null") + ".");
			}
		}

		/**
		 * Is show help button.
		 */
		protected boolean isShowHelpButton() {
			return false;
		}
		
		@Override
		protected String title4PopupWindow() {
			return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Printer");
		}

		public void populate(String printerName) {
			this.printerCombo.setSelectedItem(printerName);
		}

		public String update() {
			return this.printerCombo.getSelectedItem().toString();
		}
	}

}
