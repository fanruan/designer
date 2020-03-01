package com.fr.design.expand;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.fr.base.BaseUtils;
import com.fr.design.constants.UIConstants;
import com.fr.design.cell.smartaction.AbstractSmartJTablePaneAction;
import com.fr.design.cell.smartaction.SmartJTablePane;
import com.fr.design.cell.smartaction.SmartJTablePaneAction;
import com.fr.design.gui.columnrow.ColumnRowPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.present.ColumnRowTableModel;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.GridUtils;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;
import com.fr.stable.ColumnRow;
import com.fr.stable.Constants;
import com.fr.stable.StableUtils;
import com.fr.design.utils.gui.GUICoreUtils;

public class ParentPane extends BasicPane {
	public static final int LEFT = 0;
	public static final int UP = 1;

	private CardLayout cardLayout;
	private JPanel parentCardPane;
	private UITextField noneParentText;
	private UITextField defaultParentText;
	private ColumnRowPane customParentColumnRowPane;
	private ElementCasePane ePane;

	private int leftOrUp = LEFT;

	private ActionListener cellAttrPaneListener = null;

	private transient TemplateCellElement oldCellElement;

	public ParentPane(int leftOrUp, ActionListener cellActionListener) {
		this.leftOrUp = leftOrUp;
		this.cellAttrPaneListener = cellActionListener;
		this.initComponents();
	}

	private void initComponents() {
		this.setLayout(new BorderLayout());
		final UIButton arrowButton = new UIButton(UIConstants.ARROW_DOWN_ICON);
		((UIButton) arrowButton).setRoundBorder(true, Constants.LEFT);
		this.add(arrowButton, BorderLayout.EAST);

		cardLayout = new CardLayout();
		parentCardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
		parentCardPane.setLayout(cardLayout);
		customParentColumnRowPane = new ColumnRowPane();

		UIButton imageButton4ColumnRowPane = new UIButton(BaseUtils.readIcon("com/fr/design/images/buttonicon/select.png")) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(24, 24);
			}
		};
		imageButton4ColumnRowPane.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ePane == null || oldCellElement == null) {
					return;
				}
				// 设置标志，为了不让grid dispose一些东西
				ePane.getGrid().setNotShowingTableSelectPane(false);

				ColumnRowTableModel model = new ColumnRowTableModel();
				model.addColumnRow(ColumnRow.valueOf(0, 0));
				BasicPane smartPane = new SmartJTablePane4CellExpandAttr(model, ePane);
				Container dialog = ParentPane.this;
				while (dialog != null) {
					if (dialog instanceof Dialog) {
						dialog.setVisible(false);
						break;
					}
					dialog = dialog.getParent();
				}

				Window w = DesignerContext.getDesignerFrame();
				BasicDialog dlg = smartPane.showWindow(w);
				ePane.setEditable(false);
				dlg.setModal(false);
				dlg.setVisible(true);
			}
		});

		noneParentText = new UITextField(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_None"));
		noneParentText.setRectDirection(Constants.RIGHT);
		parentCardPane.add("Fine-Design_Report_None", noneParentText);
		defaultParentText = new UITextField(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Default"));
		defaultParentText.setRectDirection(Constants.RIGHT);
		parentCardPane.add("Default", defaultParentText);
		parentCardPane.add("Custom",
				GUICoreUtils.createFlowPane(new JComponent[] { customParentColumnRowPane, imageButton4ColumnRowPane }, FlowLayout.CENTER));

		this.add(parentCardPane, BorderLayout.CENTER);

		final UIPopupMenu popup = createPopMenu();
		noneParentText.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Rectangle leftRec = parentCardPane.getBounds();
				popup.setPopupSize(leftRec.width + arrowButton.getSize().width, popup.getPreferredSize().height);
				popup.show(parentCardPane, 0, leftRec.height);
			}
		});

		defaultParentText.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Rectangle leftRec = parentCardPane.getBounds();
				popup.setPopupSize(leftRec.width + arrowButton.getSize().width, popup.getPreferredSize().height);
				popup.show(parentCardPane, 0, leftRec.height);
			}
		});

		arrowButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Rectangle leftRec = parentCardPane.getBounds();
				popup.setPopupSize(leftRec.width + arrowButton.getSize().width, popup.getPreferredSize().height);
				popup.show(parentCardPane, 0, leftRec.height);
			}
		});
	}

	@Override
	protected String title4PopupWindow() {
		return "parent";
	}

	public void putElementcase(ElementCasePane t) {
		ePane = t;
	}

	public void putCellElement(TemplateCellElement cellElement) {
		oldCellElement = cellElement;
	}

	private UIPopupMenu createPopMenu() {
		UIPopupMenu pop = new UIPopupMenu();
		pop.setOnlyText(true);
		UIMenuItem noneItem = new UIMenuItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_None"));
		if (cellAttrPaneListener != null) {
			noneItem.addActionListener(cellAttrPaneListener);
		}
		noneItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(parentCardPane, "Fine-Design_Report_None");

			}
		});

		UIMenuItem defaultItem = new UIMenuItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Default"));
		if (cellAttrPaneListener != null) {
			defaultItem.addActionListener(cellAttrPaneListener);
		}
		defaultItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(parentCardPane, "Default");
			}
		});

		UIMenuItem customItem = new UIMenuItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Custom"));
		if (cellAttrPaneListener != null) {
			customItem.addActionListener(cellAttrPaneListener);
		}
		customItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(parentCardPane, "Custom");
			}
		});
		pop.add(noneItem);
		pop.addSeparator();
		pop.add(defaultItem);
		pop.addSeparator();
		pop.add(customItem);

		return pop;
	}

	private boolean isLeft() {
		return this.leftOrUp == LEFT;
	}

	public void populate(CellExpandAttr cellExpandAttr) {
		if (cellExpandAttr == null) {
			cellExpandAttr = new CellExpandAttr();
		}

		ColumnRow columnRow = isLeft() ? cellExpandAttr.getLeftParentColumnRow() : cellExpandAttr.getUpParentColumnRow();
		if (isLeft() ? cellExpandAttr.isLeftParentDefault() : cellExpandAttr.isUpParentDefault()) {
			this.cardLayout.show(parentCardPane, "Default");
			this.customParentColumnRowPane.populate(ColumnRow.valueOf(0, 0));
			if (ColumnRow.validate(columnRow)) {
				this.defaultParentText.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Default") + ":" + BaseUtils.convertColumnRowToCellString(columnRow));
			} else {
				this.defaultParentText.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Default"));
			}
		} else if (ColumnRow.validate(columnRow)) {
			this.cardLayout.show(parentCardPane, "Custom");
			this.customParentColumnRowPane.populate(columnRow);
		} else {
			this.cardLayout.show(parentCardPane, "Fine-Design_Report_None");
			this.customParentColumnRowPane.populate(ColumnRow.valueOf(0, 0));
		}
	}

	public void update(CellExpandAttr cellExpandAttr) {
		if (cellExpandAttr == null) {
			cellExpandAttr = new CellExpandAttr();
		}

		if (this.noneParentText.isVisible()) {
			if (isLeft()) {
				cellExpandAttr.setLeftParentDefault(false);
				cellExpandAttr.setLeftParentColumnRow(null);
			} else {
				cellExpandAttr.setUpParentDefault(false);
				cellExpandAttr.setUpParentColumnRow(null);
			}
		} else if (this.defaultParentText.isVisible()) {
			if (isLeft()) {
				cellExpandAttr.setLeftParentDefault(true);
				cellExpandAttr.setLeftParentColumnRow(null);
			} else {
				cellExpandAttr.setUpParentDefault(true);
				cellExpandAttr.setUpParentColumnRow(null);
			}

		} else if (this.customParentColumnRowPane.isVisible()) {
			if (isLeft()) {
				cellExpandAttr.setLeftParentDefault(false);
				cellExpandAttr.setLeftParentColumnRow(this.customParentColumnRowPane.update());
			} else {
				cellExpandAttr.setUpParentDefault(false);
				cellExpandAttr.setUpParentColumnRow(this.customParentColumnRowPane.update());
			}

		}
	}


	private class SmartJTablePane4CellExpandAttr extends SmartJTablePane {
		public SmartJTablePane4CellExpandAttr(ColumnRowTableModel model, ElementCasePane actionReportPane) {
			super(model, actionReportPane);
			this.changeSmartJTablePaneAction(a);
			this.changeGridSelectionChangeListener(listener);
		}

		private SmartJTablePaneAction a = new AbstractSmartJTablePaneAction(this, ParentPane.this) {
			@Override
			public void doOk() {
				GridUtils.doSelectCell(ePane, ParentPane.this.oldCellElement.getColumn(), ParentPane.this.oldCellElement.getRow());
				// 释放标志
				ePane.getGrid().setNotShowingTableSelectPane(true);
				ePane.repaint(10);
				ParentPane.this.cardLayout.show(parentCardPane, "Custom");
				ParentPane.this.customParentColumnRowPane.populate(((ColumnRowTableModel) SmartJTablePane4CellExpandAttr.this.table.getModel())
						.getColumnRow(0));
			}
		};
		private SelectionListener listener = new SelectionListener(){

			@Override
			public void selectionChanged(SelectionEvent e) {
				ElementCasePane currentReportPane = (ElementCasePane) e.getSource();
				Selection selection =  currentReportPane.getSelection();
				if(selection instanceof FloatSelection){
					return;
				}
				CellSelection cellselection = (CellSelection) selection;
				ColumnRowTableModel model = (ColumnRowTableModel) table.getModel();
				ColumnRow cr = ColumnRow.valueOf(cellselection.getColumn(), cellselection.getRow());
				model.setColumnRow(cr, 0);
				model.fireTableDataChanged();
			}
		};

		@Override
		public void setCellRenderer() {
			javax.swing.table.TableColumn column0 = table.getColumnModel().getColumn(0);
			column0.setCellRenderer(new ColumnTableCellRenderer());
			javax.swing.table.TableColumn column1 = table.getColumnModel().getColumn(1);
			column1.setCellRenderer(new RowTableCellRenderer());
		}

		private class ColumnTableCellRenderer extends DefaultTableCellRenderer {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				this.setText(StableUtils.convertIntToABC(((Integer) value) + 1));
				this.setBackground(java.awt.Color.cyan);
				return this;
			}
		}

		private class RowTableCellRenderer extends DefaultTableCellRenderer {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				this.setText((((Integer) value) + 1) + "");
				this.setBackground(java.awt.Color.cyan);
				return this;
			}
		}

		@Override
		protected String title4PopupWindow() {
			return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_RWA_Smart_Add_Cells");
		}
	}
}
