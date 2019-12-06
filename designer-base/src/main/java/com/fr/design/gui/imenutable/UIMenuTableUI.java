package com.fr.design.gui.imenutable;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.event.MouseInputListener;

import com.fr.base.GraphHelper;
import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.itable.UITableUI;
import com.fr.design.mainframe.DesignerContext;

import com.fr.stable.StringUtils;

public class UIMenuTableUI extends UITableUI{
	private UIMenuTable uiTable;

	protected void startDragTab(MouseEvent e) {
		rollOverRowIndex = -1;
		dragStartY = e.getY();
		dragStartRowIndex = table.rowAtPoint(e.getPoint());
		uiTable.setDraggingRowIndex(table.rowAtPoint(e.getPoint()));
		uiTable.setRollOverRowIndex(-1);
	}

	protected void resetDragTab() {
		dragStartY = -1;
		dragEndY = -1;
		isReleased = true;
		uiTable.setDraggingRowIndex(-1);
		uiTable.setRollOverRowIndex(-1);
	}

	protected void paintDragTab(Graphics2D g2d) {
		if(dragEndY != -1 && dragEndY <= table.getHeight() + table.getRowHeight()) {
			g2d.setColor(UIConstants.LIGHT_BLUE);
			g2d.fillRect(0, dragEndY, table.getWidth(), table.getRowHeight());
			UIMenuNameableCreator values = uiTable.getLine(dragStartRowIndex);
			if(values == null) {
				return;
			}
			g2d.setColor(UIConstants.LINE_COLOR);
			String text = values.getName();
			if(!StringUtils.isEmpty(text)) {
				GraphHelper.drawString(g2d, text, 0, dragEndY + table.getRowHeight() - 5);
			}
		}
	}

	public void installUI(JComponent c) {
		table = (UIMenuTable)c;
		uiTable = (UIMenuTable) table;
		rendererPane = new CellRendererPane();
		table.add(rendererPane);
		installDefaults();
		installListeners();
		installKeyboardActions();
	}

	protected MouseInputListener createMouseInputListener() {
		return new MyMouseListener() {
			public void mouseMoved(MouseEvent e) {
				int tmp = table.rowAtPoint(e.getPoint());
				if(rollOverRowIndex != tmp && isReleased) {
					rollOverRowIndex = tmp;
					uiTable.setRollOverRowIndex(rollOverRowIndex);
					table.repaint();
				} 
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getX() >= table.getWidth() - 20) {
					int val = FineJOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Are_You_Sure_To_Remove_The_Selected_Item") + "?",
							com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Remove"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (val == JOptionPane.OK_OPTION) {
						uiTable.removeLine(table.rowAtPoint(e.getPoint()));
						uiTable.fireTargetChanged();
						uiTable.getParent().doLayout();
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if(!isReleased) {
					resetDragTab();
				}
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				int rowIndex = table.rowAtPoint(e.getPoint());
				int columnIndex = table.columnAtPoint(e.getPoint());
				if ((rowIndex == -1) || (columnIndex == -1)) {
					return;
				}
				uiTable.editingEvent(rowIndex, e.getLocationOnScreen().y);
			}

			@Override
			public void mouseDragged(MouseEvent e) {

				if(isReleased) {
					startDragTab(e);
				}
				isReleased = false;
				dragEndY = e.getY();
				int dierta = dragEndY - dragStartY;
				if(Math.abs(dierta) >= table.getRowHeight()) {
					uiTable.dragSort(dragStartRowIndex, dierta > 0);
					uiTable.fireTargetChanged();
					startDragTab(e);
					return;
				}

				table.repaint();
			}
		};
	}
}
