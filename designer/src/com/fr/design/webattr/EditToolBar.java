package com.fr.design.webattr;

import com.fr.base.BaseUtils;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
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
import com.fr.general.Inter;
import com.fr.report.web.button.Export;
import com.fr.report.web.button.PDFPrint;
import com.fr.report.web.button.Print;
import com.fr.report.web.button.write.AppendColumnRow;
import com.fr.report.web.button.write.Submit;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EditToolBar extends BasicPane {

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
		UIButton bgButton = new UIButton(Inter.getLocText(new String[]{"Background", "Set"}));
		defaultCheckBox = new UICheckBox(Inter.getLocText(new String[]{"Default", "Background"}));
		bgButton.addActionListener(actioner);
		backgroundPane.add(defaultCheckBox);
		backgroundPane.add(bgButton);
		backgroundPane.setBorder(BorderFactory.createTitledBorder(Inter.getLocText(new String[]{"Background", "Set"})));
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
		return Inter.getLocText("FR-Designer_Edit");
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
			this.setName(Inter.getLocText("Utils-Move_Up"));
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
			this.setName(Inter.getLocText("Utils-Move_Down"));
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
			this.setName(Inter.getLocText("FR-Designer_Delete"));
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
			int val = JOptionPane.showConfirmDialog(EditToolBar.this, Inter.getLocText("FR-Designer_Are_You_Sure_To_Delete_The_Data") + "?", "Message", JOptionPane.YES_NO_OPTION);
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
				flashPrint, pdfPrint, appletPrint, serverPrint, isPopup, isVerify, failSubmit, isCurSheet;
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
			icon = new UICheckBox(Inter.getLocText("FR-Designer_Show_Icon"));
			text = new UICheckBox(Inter.getLocText("FR-Designer_Show_Text"));

			north.add(icon, BorderLayout.NORTH);
			north.add(text, BorderLayout.CENTER);

			nameField = new UITextField(8);
			iconPane = new IconDefinePane();
			javaScriptPane = JavaScriptActionPane.createDefault();

			double p = TableLayout.PREFERRED;
			double rowSize[] = {p, p};
			double columnSize[] = {p, p};

			Component[][] coms = new Component[][]{{new UILabel(Inter.getLocText(new String[]{"Widget", "Printer-Alias"}) + ":"), nameField}, {new UILabel(Inter.getLocText(new String[]{"Widget", "Icon"}) + ":"), iconPane}};

			JPanel nameIconPane = TableLayoutHelper.createTableLayoutPane(coms, rowSize, columnSize);

			north.add(nameIconPane, BorderLayout.SOUTH);

			north.setBorder(BorderFactory.createTitledBorder(Inter.getLocText(new String[]{"Form-Button", "Property", "Set"})));
			this.add(north, BorderLayout.NORTH);
			JPanel none = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
			centerPane = FRGUIPaneFactory.createCardLayout_S_Pane();
			card = new CardLayout();
			centerPane.setLayout(card);
			centerPane.add("custom", getCustomPane());
			centerPane.add("export", getExport());
			centerPane.add("print", getPrint());
			centerPane.add("none", none);
			centerPane.add("pdfprint", getPdfPrintSetting());
			// centerPane.add("editexcel", editExcel);
			centerPane.add(getCpane(), "appendcount");
			centerPane.add(getSubmitPane(), "submit");

            Set<ExtraButtonToolBarProvider> extraButtonSet = ExtraDesignClassManager.getInstance().getArray(ExtraButtonToolBarProvider.XML_TAG);
            for (ExtraButtonToolBarProvider provider : extraButtonSet) {
                provider.updateCenterPane(centerPane);
            }

			this.add(centerPane, BorderLayout.CENTER);
		}


		private JPanel getCustomPane() {
			JPanel customPane = FRGUIPaneFactory.createCenterFlowInnerContainer_S_Pane();

			button = new UIButton(Inter.getLocText("FR-Designer_User_Defined_Event"));
			customPane.add(button);
			customPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Designer_Edit") + "JS", null));
			button.addActionListener(l);
			return customPane;
		}

		private JPanel getExport() {
			JPanel export = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
			// export.setLayout(new BoxLayout(export, BoxLayout.Y_AXIS));
			pdf = new UICheckBox(Inter.getLocText("FR-Designer_Output_PDF"));
			excelP = new UICheckBox(Inter.getLocText("FR-Designer-Output_Excel_Page"));
			excelO = new UICheckBox(Inter.getLocText("FR-Designer-Output_Excel_Simple"));
			excelS = new UICheckBox(Inter.getLocText("FR-Designer-Output_Excel_Sheet"));
			word = new UICheckBox(Inter.getLocText("FR-Designer_Output_Word"));
			image = new UICheckBox(Inter.getLocText("FR-Designer_Image"));
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

			export.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText(new String[]{"Form-Button", "Property", "Set"}), null));
			return export;
		}

		private JPanel getPrint() {
			JPanel print = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
			// print.setLayout(new BoxLayout(print, BoxLayout.Y_AXIS));
			flashPrint = new UICheckBox(Inter.getLocText("FR-Designer_Flash_Print"));
			pdfPrint = new UICheckBox(Inter.getLocText("FR-Designer_PDF_Print"));
			appletPrint = new UICheckBox(Inter.getLocText("FR-Designer_Applet_Print"));
			serverPrint = new UICheckBox(Inter.getLocText("FR-Designer_Server_Print"));
			print.add(flashPrint);
			print.add(pdfPrint);
			print.add(appletPrint);
			print.add(serverPrint);
			print.setBorder(BorderFactory.createTitledBorder(Inter.getLocText(new String[]{"Form-Button", "Property", "Set"})));
			return print;
		}


		private JPanel getCpane() {
			JPanel appendCountPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
			count = new UIBasicSpinner(new SpinnerNumberModel(1, 0, Integer.MAX_VALUE, 1));
			UILabel countLabel = new UILabel(Inter.getLocText(new String[]{"Add", "Row", "Column", "Numbers"}) + ":");
			JPanel cpane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
			cpane.add(countLabel);
			cpane.add(count);
			appendCountPane.add(cpane);
			return cpane;
		}


		private JPanel getPdfPrintSetting() {
			// richer:pdf打印按钮设置
			JPanel pdfPrintSetting = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
			isPopup = new UICheckBox(Inter.getLocText("PDF-Print_isPopup"));
			pdfPrintSetting.add(isPopup);
			pdfPrintSetting.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("PDF-Print_Setting")));
			return pdfPrintSetting;
		}


		private JPanel getSubmitPane() {
			isVerify = new UICheckBox(Inter.getLocText("Verify-Data_Verify"));
			failSubmit = new UICheckBox(Inter.getLocText(new String[]{"Verify_Fail", "Still", "Submit"}));
			isCurSheet = new UICheckBox(Inter.getLocText("FR-Designer-Basic_Only_Submit_Current_Sheet"));
			JPanel submitPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
			submitPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText(new String[]{"Form-Button", "Property", "Set"}), null));
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
			} else if (widget instanceof Print) {
				populatePrint();
			} else if (widget instanceof PDFPrint) {
				populatePDFPrint();
			} else if (widget instanceof AppendColumnRow) {
				populateAppendColumnRow();
			} else if (widget instanceof Submit) {
				populateSubmit();
			} else if (widget instanceof CustomToolBarButton) {
				populateCustomToolBarButton();
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

		private void populatePDFPrint(){
			card.show(centerPane, "pdfprint");
			PDFPrint pdfPrint = (PDFPrint) widget;
			this.isPopup.setSelected(pdfPrint.isPopup());
		}

		private void populatePrint(){
			card.show(centerPane, "print");
			Print print = (Print) widget;
			this.pdfPrint.setSelected(print.isPDFPrint());
			this.appletPrint.setSelected(print.isAppletPrint());
			this.flashPrint.setSelected(print.isFlashPrint());
			this.serverPrint.setSelected(print.isServerPrint());
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
			} else if (widget instanceof Print) {
				updatePrint();
			} else if (widget instanceof PDFPrint) {
				PDFPrint pdfPrint = (PDFPrint) widget;
				pdfPrint.setPopup(this.isPopup.isSelected());
			} else if (widget instanceof AppendColumnRow) {
				((AppendColumnRow) widget).setCount(((Integer) count.getValue()).intValue());
			} else if (widget instanceof Submit) {
				updateSubmit();
			} else if (widget instanceof CustomToolBarButton) {
				((CustomToolBarButton) widget).setJSImpl(this.javaScriptPane.updateBean());
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

		private void updatePrint(){
			Print print = (Print) widget;
			print.setAppletPrint(this.appletPrint.isSelected());
			print.setFlashPrint(this.flashPrint.isSelected());
			print.setPDFPrint(this.pdfPrint.isSelected());
			print.setServerPrint(this.serverPrint.isSelected());
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
	}

}