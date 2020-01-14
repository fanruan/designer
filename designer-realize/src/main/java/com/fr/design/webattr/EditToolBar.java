package com.fr.design.webattr;

import com.fr.base.BaseUtils;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.fun.ExportToolBarProvider;
import com.fr.design.fun.ExtraButtonToolBarProvider;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.javascript.JavaScriptActionPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.style.background.BackgroundPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.widget.IconDefinePane;
import com.fr.form.ui.Button;
import com.fr.form.ui.CustomToolBarButton;
import com.fr.form.ui.Widget;
import com.fr.form.ui.WidgetInfoConfig;
import com.fr.general.Background;

import com.fr.report.web.button.Email;
import com.fr.report.web.button.Export;
import com.fr.report.web.button.write.AppendColumnRow;
import com.fr.report.web.button.write.Submit;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EditToolBar extends BasicPane {

	private static final String EMAIL = "email";
	private static final String CUSTOM = "custom";
	private static final String EXPORT = "export";
	private static final String NONE = "none";
	private static final String EDIT_EXCEL = "editexcel";
	private static final String APPEND_COUNT = "appendcount";
	private static final String SUBMIT = "submit";

	private JWorkBook jwb;
	private JList list;
	private DefaultListModel listModel;
	private JPanel right;
	private CardLayout card;
	private ButtonPane bp;
	private ToolBarButton lastButton;
	private Background background = null;
	private UICheckBox defaultCheckBox;

	private ListSelectionListener listSelectionListener = new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent evt) {
			if (lastButton != null) {
				lastButton.setWidget(bp.update());
			}
			if (list.getSelectedValue() instanceof ToolBarButton) {
				lastButton = (ToolBarButton) list.getSelectedValue();
				if (lastButton.getWidget() instanceof Button) {
					card.show(right, "button");
					bp.populate(lastButton.getWidget());
				} else {
					bp.populate(lastButton.getWidget());
					card.show(right, "none");
				}
			}
		}
	};


	private ActionListener actioner = new ActionListener() {
		/**
		 *
		 */
		public void actionPerformed(ActionEvent arg0) {
			final BackgroundPane backgroundPane = new BackgroundPane();
			BasicDialog dialog = backgroundPane.showWindow(DesignerContext.getDesignerFrame());
			backgroundPane.populate(EditToolBar.this.background);
			dialog.addDialogActionListener(new DialogActionAdapter() {
				public void doOk() {
					EditToolBar.this.background = backgroundPane.update();
					if (EditToolBar.this.background != null) {
						EditToolBar.this.defaultCheckBox.setSelected(false);
					}
				}
			});
			dialog.setVisible(true);
		}
	};

	public EditToolBar() {
		initComponent();
	}

	/**
	 * 初始化
	 */
	public void initComponent() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		JPanel left = FRGUIPaneFactory.createBorderLayout_S_Pane();
		listModel = new DefaultListModel();
		list = new JList(listModel);
		list.setCellRenderer(render);
		left.add(new JScrollPane(list), BorderLayout.CENTER);
		if (listModel.getSize() > 0) {
			list.setSelectedIndex(0);
		}

		ToolBarDef toolbarDef = new ToolBarDef();
		toolbarDef.addShortCut(new MoveUpItemAction());
		toolbarDef.addShortCut(new MoveDownItemAction());
		toolbarDef.addShortCut(new RemoveAction());
		UIToolbar toolBar = ToolBarDef.createJToolBar();
		toolbarDef.updateToolBar(toolBar);
		left.add(toolBar, BorderLayout.NORTH);

		right = FRGUIPaneFactory.createCardLayout_S_Pane();
		card = new CardLayout();
		right.setLayout(card);
		bp = new ButtonPane();
		right.add("none", FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane());
		right.add("button", bp);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, left, right);
		// splitPane.setDividerLocation(left.getMinimumSize().width);
		splitPane.setDividerLocation(120);
		this.add(splitPane);
		list.addListSelectionListener(listSelectionListener);
		JPanel backgroundPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		UIButton bgButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Set_Background"));
		defaultCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Default_Background"));
		bgButton.addActionListener(actioner);
		backgroundPane.add(defaultCheckBox);
		backgroundPane.add(bgButton);
		backgroundPane.setBorder(BorderFactory.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Set_Background")));
		this.add(backgroundPane, BorderLayout.SOUTH);
	}

	ListCellRenderer render = new DefaultListCellRenderer() {
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if (value instanceof ToolBarButton) {
				ToolBarButton button = (ToolBarButton) value;
				this.setText(button.getNameOption().optionName());
				this.setIcon(button.getNameOption().optionIcon());
			}
			return this;
		}
	};

	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Edit");
	}

	public void populate(FToolBar ftoolbar) {
		this.populate(ftoolbar, null);
	}

	public void populate(FToolBar ftoolbar, ToolBarButton button) {
		if (ftoolbar == null) {
			return;
		}
		for (int i = 0; i < ftoolbar.getButtonlist().size(); i++) {
			listModel.addElement(ftoolbar.getButtonlist().get(i));
		}
		this.list.validate();
		this.list.repaint();
		if (ftoolbar.getButtonlist().size() > 0) {
			this.list.setSelectedIndex(0);
		}
		if (button != null) {
			this.list.setSelectedValue(button, true);
		}
		this.background = ftoolbar.getBackground();

		this.defaultCheckBox.setSelected(ftoolbar.isDefault() ? true : false);
	}

	public FToolBar update() {
		if (this.list.getSelectedIndex() > -1) {
			for (int i = 0; i < listModel.getSize(); i++) {
				this.list.setSelectedIndex(i);
				ToolBarButton toolBarButton = (ToolBarButton) this.list.getSelectedValue();
				Widget widget = this.bp.update();
				toolBarButton.setWidget(widget);
				if (widget instanceof Button) {
					String iconname = ((Button) widget).getIconName();
					if (StringUtils.isNotBlank(iconname)) {
						Image iimage = WidgetInfoConfig.getInstance().getIconManager().getIconImage(iconname);
						toolBarButton.setIcon(new ImageIcon(iimage));
					}
				}
			}
		}
		List<ToolBarButton> list = new ArrayList<ToolBarButton>();
		for (int i = 0; i < listModel.size(); i++) {
			list.add((ToolBarButton) listModel.get(i));
		}
		FToolBar ftoolBar = new FToolBar();
		ftoolBar.setButtonlist(list);

		ftoolBar.setDefault(this.defaultCheckBox.isSelected());
		if (!ftoolBar.isDefault()) {
			ftoolBar.setBackground(this.background);
		}
		return ftoolBar;
	}

	private class MoveUpItemAction extends UpdateAction {
		public MoveUpItemAction() {
			this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Move_Up"));
			this.setMnemonic('U');
			this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/up.png"));
		}

		/**
		 *
		 */
		public void actionPerformed(ActionEvent evt) {
			int selectedIndex = list.getSelectedIndex();
			if (selectedIndex == -1) {
				return;
			}

			// 上移
			if (selectedIndex > 0) {
				DefaultListModel listModel = (DefaultListModel) list.getModel();

				Object selecteObj1 = listModel.get(selectedIndex - 1);
				listModel.set(selectedIndex - 1, listModel.get(selectedIndex));
				listModel.set(selectedIndex, selecteObj1);

				list.setSelectedIndex(selectedIndex - 1);
				list.ensureIndexIsVisible(selectedIndex - 1);
				list.validate();
			}
		}
	}

	private class MoveDownItemAction extends UpdateAction {
		public MoveDownItemAction() {
			this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Move_Down"));
			this.setMnemonic('D');
			this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/down.png"));
		}

		/**
		 *
		 */
		public void actionPerformed(ActionEvent evt) {
			int selectedIndex = list.getSelectedIndex();
			if (selectedIndex == -1) {
				return;
			}

			// 下移
			if (selectedIndex == -1) {
				return;
			}

			if (selectedIndex < list.getModel().getSize() - 1) {
				DefaultListModel listModel = (DefaultListModel) list.getModel();

				Object selecteObj1 = listModel.get(selectedIndex + 1);
				listModel.set(selectedIndex + 1, listModel.get(selectedIndex));
				listModel.set(selectedIndex, selecteObj1);

				list.setSelectedIndex(selectedIndex + 1);
				list.ensureIndexIsVisible(selectedIndex + 1);
				list.validate();
			}
		}
	}

	public class RemoveAction extends UpdateAction {
		public RemoveAction() {
			this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Delete"));
			this.setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/remove.png"));
		}

		/**
		 * 动作
		 * @param e 事件
		 */
		public void actionPerformed(ActionEvent e) {
			int i = list.getSelectedIndex();
			if (i < 0 || !(listModel.getElementAt(i) instanceof ToolBarButton)) {
				return;
			}
			int val = FineJOptionPane.showConfirmDialog(EditToolBar.this, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Are_You_Sure_To_Delete_The_Data") + "?",
					com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Confirm"), JOptionPane.YES_NO_OPTION);
			if (val != JOptionPane.YES_OPTION) {
				return;
			}
			listModel.removeElementAt(i);
			list.validate();
			if (listModel.size() > 0) {
				list.setSelectedIndex(0);
			} else {
				card.show(right, "none");
			}
		}
	}

	public class ButtonPane extends BasicPane {
		private CardLayout card;
		private JPanel centerPane;
		private UICheckBox icon, text, pdf, excelP, excelO, excelS, image, word,
				isPopup, isVerify, failSubmit, isCurSheet, excelImClean,
				excelImCover, excelImAppend, excelImCust,
				customConsignee, consigneeByDepartment, consigneeByRole;
		private UIBasicSpinner count;
		private Widget widget;
		private UITextField nameField;
		private IconDefinePane iconPane;
		private UIButton button;
		private JavaScriptActionPane javaScriptPane;
		private ExportToolBarProvider[] exportToolBarProviders;

		private ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isVerify.isSelected()) {
					failSubmit.setVisible(true);
				} else {
					failSubmit.setVisible(false);
					failSubmit.setSelected(false);
				}
			}
		};

		public ButtonPane() {
			this.initComponents();
		}

		/**
		 * 初始化元素
		 */
		public void initComponents() {
			Set<ExportToolBarProvider> set = ExtraDesignClassManager.getInstance().getArray(ExportToolBarProvider.XML_TAG);
			exportToolBarProviders = set.toArray(new ExportToolBarProvider[set.size()]);
			this.setLayout(FRGUIPaneFactory.createBorderLayout());
			JPanel north = FRGUIPaneFactory.createBorderLayout_S_Pane();
			icon = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Show_Icon"));
			text = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Show_Text"));

			north.add(icon, BorderLayout.NORTH);
			north.add(text, BorderLayout.CENTER);

			nameField = new UITextField(8);
			iconPane = new IconDefinePane();
			javaScriptPane = JavaScriptActionPane.createDefault();

			double p = TableLayout.PREFERRED;
			double rowSize[] = {p, p};
			double columnSize[] = {p, p};

			Component[][] coms = new Component[][]{{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Widget_Printer_Alias") + ":"), nameField}, {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Widget_Icon") + ":"), iconPane}};

			JPanel nameIconPane = TableLayoutHelper.createTableLayoutPane(coms, rowSize, columnSize);

			north.add(nameIconPane, BorderLayout.SOUTH);

			north.setBorder(BorderFactory.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Set_Form_Button_Property")));
			this.add(north, BorderLayout.NORTH);
			JPanel none = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
			centerPane = FRGUIPaneFactory.createCardLayout_S_Pane();
			card = new CardLayout();
			centerPane.setLayout(card);
			centerPane.add(CUSTOM, getCustomPane());
			centerPane.add(EXPORT, getExport());
			centerPane.add(EMAIL, getEmail());
			centerPane.add(NONE, none);
			centerPane.add(getCpane(), APPEND_COUNT);
			centerPane.add(getSubmitPane(), SUBMIT);

			Set<ExtraButtonToolBarProvider> extraButtonSet = ExtraDesignClassManager.getInstance().getArray(ExtraButtonToolBarProvider.XML_TAG);
			for (ExtraButtonToolBarProvider provider : extraButtonSet) {
				provider.updateCenterPane(centerPane);
			}

			this.add(centerPane, BorderLayout.CENTER);
		}


		private JPanel getCustomPane() {
			JPanel customPane = FRGUIPaneFactory.createCenterFlowInnerContainer_S_Pane();

			button = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_User_Defined_Event"));
			customPane.add(button);
			customPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Edit") + "JS", null));
			button.addActionListener(l);
			return customPane;
		}

		private JPanel getExport() {
			JPanel export = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
			// export.setLayout(new BoxLayout(export, BoxLayout.Y_AXIS));
			pdf = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Output_PDF"));
			excelP = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Output_Excel_Page"));
			excelO = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Output_Excel_Simple"));
			excelS = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Output_Excel_Sheet"));
			word = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Output_Word"));
			image = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Image"));
			export.add(pdf);
			export.add(Box.createVerticalStrut(2));
			export.add(excelP);
			export.add(Box.createVerticalStrut(2));
			export.add(excelO);
			export.add(Box.createVerticalStrut(2));
			export.add(excelS);
			export.add(Box.createVerticalStrut(2));
			export.add(word);
			export.add(Box.createVerticalStrut(2));
			export.add(image);
			for(int i=0; i<ArrayUtils.getLength(exportToolBarProviders); i++){
				export = exportToolBarProviders[i].updateCenterPane(export);
			}

			export.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Set_Form_Button_Property"), null));
			return export;
		}

		private JPanel getEmail() {
			JPanel email = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
			customConsignee = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Custom_Consignee"));
			consigneeByDepartment = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Consignee_By_Department"));
			consigneeByRole = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Consignee_By_Role"));
			email.add(customConsignee);
			email.add(Box.createVerticalStrut(2));
			email.add(consigneeByDepartment);
			email.add(Box.createVerticalStrut(2));
			email.add(consigneeByRole);
			email.add(Box.createVerticalStrut(2));

			email.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Set_Form_Button_Property"), null));
			return email;
		}

		private JPanel getCpane() {
			JPanel appendCountPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
			count = new UIBasicSpinner(new SpinnerNumberModel(1, 0, Integer.MAX_VALUE, 1));
			UILabel countLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Add_Row_Column_Numbers") + ":");
			JPanel cpane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
			cpane.add(countLabel);
			cpane.add(count);
			appendCountPane.add(cpane);
			return cpane;
		}


		private JPanel getSubmitPane() {
			isVerify = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Verify_Data_Verify"));
			failSubmit = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Verify_Fail_Still_Submit"));
			isCurSheet = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Only_Submit_Current_Sheet"));
			JPanel submitPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
			submitPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Set_Form_Button_Property"), null));
			submitPane.add(isVerify);
			submitPane.add(failSubmit);
			submitPane.add(isCurSheet);
			isVerify.addActionListener(actionListener);
			return submitPane;
		}

		@Override
		protected String title4PopupWindow() {
			return "Button";
		}

		ActionListener l = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!(widget instanceof CustomToolBarButton)) {
					return;
				}
				if (javaScriptPane == null || ((CustomToolBarButton)widget).getJSImpl() == null) {
					javaScriptPane = JavaScriptActionPane.createDefault();
				}
				javaScriptPane.setPreferredSize(new Dimension(750, 500));
				BasicDialog dialog = javaScriptPane.showWindow(SwingUtilities.getWindowAncestor(ButtonPane.this));
				dialog.addDialogActionListener(new DialogActionAdapter() {

					@Override
					public void doCancel() {
						javaScriptPane.populateBean(((CustomToolBarButton) widget).getJSImpl());
					}

					@Override
					public void doOk() {
						((CustomToolBarButton) widget).setJSImpl(javaScriptPane.updateBean());
					}
				});
				dialog.setVisible(true);
			}
		};

		/**
		 * 更新
		 * @param widget 对应组件
		 */
		public void populate(Widget widget) {
			this.widget = widget;
			card.show(centerPane, "none");
			if (widget instanceof Button) {
				populateDefault();
			}
			if (widget instanceof Export) {
				populateExport();
			} else if (widget instanceof AppendColumnRow) {
				populateAppendColumnRow();
			} else if (widget instanceof Submit) {
				populateSubmit();
			} else if (widget instanceof CustomToolBarButton) {
				populateCustomToolBarButton();
			} else if (widget instanceof Email) {
				populateEmail();
			}

			Set<ExtraButtonToolBarProvider> extraButtonSet = ExtraDesignClassManager.getInstance().getArray(ExtraButtonToolBarProvider.XML_TAG);
			for (ExtraButtonToolBarProvider provider : extraButtonSet) {
				provider.populate(widget, card, centerPane);
			}
		}

		private void populateAppendColumnRow(){
			card.show(centerPane, "appendcount");
			count.setValue(((AppendColumnRow) widget).getCount());
		}

		private void populateExport(){
			card.show(centerPane, "export");
			Export export = (Export) widget;
			this.pdf.setSelected(export.isPdfAvailable());
			this.excelP.setSelected(export.isExcelPAvailable());
			this.excelO.setSelected(export.isExcelOAvailable());
			this.excelS.setSelected(export.isExcelSAvailable());
			this.word.setSelected(export.isWordAvailable());
			this.image.setSelected(export.isImageAvailable());
			if(exportToolBarProviders != null){
				for(int i=0; i<exportToolBarProviders.length; i++){
					exportToolBarProviders[i].populate();;
				}
			}
		}

		private void populateEmail(){
			card.show(centerPane, EMAIL);
			Email email = (Email) widget;
			this.customConsignee.setSelected(email.isCustomConsignee());
			this.consigneeByDepartment.setSelected(email.isConsigneeByDepartment());
			this.consigneeByRole.setSelected(email.isConsigneeByRole());
		}

		private void populateCustomToolBarButton(){
			card.show(centerPane, "custom");
			CustomToolBarButton customToolBarButton = (CustomToolBarButton) widget;
			if (customToolBarButton.getJSImpl() != null) {
				this.javaScriptPane.populateBean(customToolBarButton.getJSImpl());
			}
		}

		private void populateSubmit(){
			card.show(centerPane, "submit");
			Submit submit = ((Submit) widget);
			this.isVerify.setSelected(submit.isVerify());
			if (!submit.isVerify()) {
				this.failSubmit.setVisible(false);
			}
			this.failSubmit.setSelected(submit.isFailVerifySubmit());
			this.isCurSheet.setSelected(submit.isOnlySubmitSelect());
		}

		private void populateDefault(){
			Button button = (Button) widget;
			this.icon.setSelected(button.isShowIcon());
			this.text.setSelected(button.isShowText());
			this.nameField.setText(button.getText());
			this.iconPane.populate(((Button) widget).getIconName());
		}

		/**
		 * 更新
		 *
		 * @return 对应组件
		 */
		public Widget update() {
			if (widget instanceof Export) {
				updateExport();
			} else if (widget instanceof AppendColumnRow) {
				((AppendColumnRow) widget).setCount(((Integer) count.getValue()).intValue());
			} else if (widget instanceof Submit) {
				updateSubmit();
			} else if (widget instanceof CustomToolBarButton) {
				updateCustomToolBarButton();
			} else if (widget instanceof Email) {
				updateEmail();
			}
			if (widget instanceof Button) {
				updateDefault();
			}

			Set<ExtraButtonToolBarProvider> extraButtonSet = ExtraDesignClassManager.getInstance().getArray(ExtraButtonToolBarProvider.XML_TAG);
			for (ExtraButtonToolBarProvider provider : extraButtonSet) {
				provider.update(widget);
			}

			return widget;
		}

		private void updateDefault(){
			((Button) widget).setShowIcon(this.icon.isSelected());
			((Button) widget).setShowText(this.text.isSelected());
			((Button) widget).setText(this.nameField.getText());
			((Button) widget).setIconName(this.iconPane.update());
		}

		private void updateSubmit(){
			Submit submit = ((Submit) widget);
			submit.setVerify(this.isVerify.isSelected());
			submit.setFailVerifySubmit(this.failSubmit.isSelected());
			submit.setOnlySubmitSelect(this.isCurSheet.isSelected());
		}

		private void updateExport(){
			Export export = (Export) widget;
			export.setPdfAvailable(this.pdf.isSelected());
			export.setExcelPAvailable(this.excelP.isSelected());
			export.setExcelOAvailable(this.excelO.isSelected());
			export.setExcelSAvailable(this.excelS.isSelected());
			export.setWordAvailable(this.word.isSelected());
			export.setImageAvailable(this.image.isSelected());
			if(exportToolBarProviders != null){
				for(int i=0; i<exportToolBarProviders.length; i++){
					exportToolBarProviders[i].update();;
				}
			}
		}

		private void updateEmail(){
			Email email = ((Email) widget);
			email.setCustomConsignee(this.customConsignee.isSelected());
			email.setConsigneeByDepartment(this.consigneeByDepartment.isSelected());
			email.setConsigneeByRole(this.consigneeByRole.isSelected());
		}

		private void updateCustomToolBarButton() {
			CustomToolBarButton customToolBarButton = (CustomToolBarButton) widget;
			if (customToolBarButton.getJSImpl() == null) {
				this.javaScriptPane = JavaScriptActionPane.createDefault();
			}
			customToolBarButton.setJSImpl(this.javaScriptPane.updateBean());
		}
	}

}
