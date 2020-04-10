package com.fr.design.gui.itable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import com.fr.base.BaseUtils;
import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.mainframe.DesignerContext;

import com.fr.stable.Constants;
import com.fr.design.utils.gui.GUIPaintUtils;

public class UITableUI extends BasicTableUI {
    private static BufferedImage closeIcon = BaseUtils.readImageWithCache("com/fr/design/images/toolbarbtn/close.png");
    protected boolean isReleased = true;
    protected int rollOverRowIndex = -1;
    protected int dragStartRowIndex = -1;
    protected int dragStartY = -1;
    protected int dragEndY = -1;
    private int startColumn;
    private int startRow;
    private boolean draggingRow = false;
    private static final int WIDTH_GAP = 20;
    private int startDragPoint;

    private int dyOffset;


    public UITableUI() {
        super();
    }

    public UITableUI(JComponent table) {
        super();
        this.table = (JTable) table;
        this.table.setRowHeight(20);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g;
        Color oldColor = g2d.getColor();
        Stroke oldStroke = g2d.getStroke();
        //鼠标悬停时，画一层蓝色背景色
        paintRolloverBackground(g2d);
        super.paint(g, c);
        //画虚线，不重写原来的paintGrid因为paintGrid为private
//		paintDotLine(g2d);
        //画X号
        paintDeleteButton(g2d);
        //画拖拽
        paintDragTab(g2d);


        g2d.setStroke(oldStroke);
        g2d.setColor(oldColor);
    }

    protected void paintDragTab(Graphics2D g) {
        if (draggingRow) {
            g.setColor(table.getParent().getBackground());
            Rectangle cellRect = table.getCellRect(table.getSelectedRow(), 0, false);
            g.copyArea(cellRect.x, cellRect.y, table.getWidth(), table.getRowHeight(), cellRect.x, dyOffset);
            if (dyOffset < 0) {
                g.fillRect(cellRect.x, cellRect.y + (table.getRowHeight() + dyOffset), table.getWidth(), (dyOffset * -1));
            } else {
                g.fillRect(cellRect.x, cellRect.y, table.getWidth(), dyOffset);
            }
        }
    }

    protected boolean isDeletable(){
        return true;
    }

    private void paintDeleteButton(Graphics2D g2d) {
        if (!isReleased || rollOverRowIndex == -1) {
            return;
        }
        if(!isDeletable()){
            return;
        }
        g2d.drawImage(closeIcon, table.getWidth() - 20, rollOverRowIndex * table.getRowHeight() + 1, closeIcon.getWidth(), closeIcon.getHeight(), table);
    }

    private void paintRolloverBackground(Graphics2D g2d) {
        if (rollOverRowIndex != -1) {
            g2d.setColor(UIConstants.FLESH_BLUE);
            GUIPaintUtils.fillPaint(g2d, 1, rollOverRowIndex * table.getRowHeight(), table.getWidth() - 2, table.getRowHeight(), true, Constants.NULL, UIConstants.FLESH_BLUE, UIConstants.LARGEARC);
        }
    }

    protected MouseInputListener createMouseInputListener() {
        return new MyMouseListener();
    }

    protected class MyMouseListener extends BasicTableUI.MouseInputHandler {

        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
            int tmp = table.rowAtPoint(e.getPoint());
            if (rollOverRowIndex != tmp && isReleased) {
                rollOverRowIndex = tmp;
                ((UITable)table).dealWithRollOver(rollOverRowIndex);
                table.repaint();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            if (e.getX() >= table.getWidth() - WIDTH_GAP) {
                if (table.isEditing()) {
                    table.getCellEditor(startRow, startColumn).stopCellEditing();
                }

                if(!isDeletable()){
                    return;
                }
                if (!table.isEditing()) {
                    int val = FineJOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Are_You_Sure_To_Remove_The_Selected_Item") + "?",
                            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Remove"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (val == JOptionPane.OK_OPTION) {
                        ((UITable) table).removeLine(table.rowAtPoint(e.getPoint()));
                        ((UITable) table).fireTargetChanged();
                        ((UITable) table).getParent().doLayout();
                        return;
                    }
                }
            }
            startColumn = table.columnAtPoint(e.getPoint());
            startRow = table.rowAtPoint(e.getPoint());
            startDragPoint = (int) e.getPoint().getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            draggingRow = false;
            isReleased = true;
            table.repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int fromRow = table.getSelectedRow();
            TableCellEditor tableCellEditor = table.getCellEditor(startRow, startColumn);
            tableCellEditor.stopCellEditing();

            if (fromRow >= 0) {
                draggingRow = true;
                int rowHeight = table.getRowHeight();
                int middleOfSelectedRow = (rowHeight * fromRow) + (rowHeight / 2);
                int toRow = -1;
                int yMousePoint = (int) e.getPoint().getY();

                if (yMousePoint < (middleOfSelectedRow - rowHeight)) {
                    toRow = fromRow - 1;
                } else if (yMousePoint > (middleOfSelectedRow + rowHeight)) {
                    toRow = fromRow + 1;
                }

                if (toRow >= 0 && toRow < table.getRowCount()) {
                    TableModel model = table.getModel();

                    for (int i = 0; i < model.getColumnCount(); i++) {
                        Object fromValue = model.getValueAt(fromRow, i);
                        Object toValue = model.getValueAt(toRow, i);

                        model.setValueAt(toValue, fromRow, i);
                        model.setValueAt(fromValue, toRow, i);
                    }
                    table.setRowSelectionInterval(toRow, toRow);
                    startDragPoint = yMousePoint;
                    rollOverRowIndex = toRow;
                }
                dyOffset = (startDragPoint - yMousePoint) * -1;
                table.repaint();
            }
        }


    }

    protected void startDragTab(MouseEvent e) {
        rollOverRowIndex = -1;
        dragStartY = e.getY();
        dragStartRowIndex = table.rowAtPoint(e.getPoint());
    }

    protected void resetDragTab() {
        dragStartY = -1;
        dragEndY = -1;
        isReleased = true;
    }

    /**
     * 初始化UI
     *
     * @param c 组件
     */
    public void installUI(JComponent c) {
        table = (JTable) c;
        rendererPane = new CellRendererPane();
        table.add(rendererPane);
        installDefaults();
        installListeners();
        installKeyboardActions();
    }

}
