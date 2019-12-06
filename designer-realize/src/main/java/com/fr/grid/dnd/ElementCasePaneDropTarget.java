package com.fr.grid.dnd;

import com.fr.design.actions.ToggleButtonUpdateAction;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.data.TableDataColumn;
import com.fr.grid.Grid;
import com.fr.grid.GridUtils;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.log.FineLoggerFactory;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.report.cell.cellattr.core.group.DSColumn;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.poly.PolyECBlock;
import com.fr.report.worksheet.FormElementCase;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Constants;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;

/**
 * the class used for drop an element to the grid
 *
 * @editor zhou 2012-3-22下午2:04:41
 */
public class ElementCasePaneDropTarget extends DropTargetAdapter {
    private static final int LEFT_2_RIGHT = 0;
    private static final int RIGHT_2_LEFT = 1;
    private static final int TOP_2_BOTTOM = 2;
    private static final int BOTTOM_2_TOP = 3;

    private ElementCasePane ePane;
    // AUGUST:notice 这个cs只是鼠标放下的那个去的那个单元格的副本
    private CellSelection cs;
    private String[][] doubleArray = null;

    public ElementCasePaneDropTarget(ElementCasePane ePane) {
        this.ePane = ePane;
        new DropTarget(ePane.getGrid(), this);
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        Point p = dtde.getLocation();
        DropTargetContext dtc = dtde.getDropTargetContext();
        Grid grid = (Grid) dtc.getComponent();
        Selection selection = grid.getElementCasePane().getSelection();
        TemplateElementCase report = grid.getElementCasePane().getEditingElementCase();
        if (!(selection instanceof CellSelection) || report == null) {
            dtde.rejectDrop();
            return;
        }

        CellSelection cs = (CellSelection) selection;

        try {
            Transferable tr = dtde.getTransferable();
            DataFlavor[] flavors = tr.getTransferDataFlavors();
            for (int i = 0; i < flavors.length; i++) {
                if (!tr.isDataFlavorSupported(flavors[i])) {
                    continue;
                }
                dtde.acceptDrop(dtde.getDropAction());
                Object userObj = tr.getTransferData(flavors[i]);

                if (userObj instanceof Class) {
                    Class cls = (Class) userObj;
                    grid.startCellEditingAt_DEC(cs.getColumn(), cs.getRow(), cls, false);
                } else if (userObj instanceof String[][]) {
                    doubleArray = (String[][]) userObj;
                    // marks:当列数为一时候，不需要菜单
                    if (doubleArray.length > 1) {
                        JPopupMenu popMenu = new JPopupMenu();
                        GUICoreUtils.showPopupMenu(createPopupMenu(popMenu), ePane.getGrid(), (int) p.getX() + 1, (int) p.getY() + 1);
                    } else {
                        new SortAction(LEFT_2_RIGHT).sortCellElement();
                    }
                }
                dtde.dropComplete(true);
                ePane.fireSelectionChangeListener();
            }

            dtde.rejectDrop();
        } catch (Exception e) {
            // dtde.rejectDrop();
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    private JPopupMenu createPopupMenu(JPopupMenu popupMenu) {
        popupMenu = new JPopupMenu();
        popupMenu.add(new SortAction(LEFT_2_RIGHT).createMenuItem());
        popupMenu.add(new SortAction(RIGHT_2_LEFT).createMenuItem());
        popupMenu.add(new SortAction(TOP_2_BOTTOM).createMenuItem());
        popupMenu.add(new SortAction(BOTTOM_2_TOP).createMenuItem());
        return popupMenu;
    }

    private class SortAction extends UpdateAction implements ToggleButtonUpdateAction {

        private int direction;

        public SortAction(int i) {
            direction = i;
            if (i == LEFT_2_RIGHT) {
                this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Left_To_Right_Duplicate"));
            } else if (i == RIGHT_2_LEFT) {
                this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Right_to_Left"));
            } else if (i == TOP_2_BOTTOM) {
                this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Top_To_Bottom_Dupicate"));
            } else if (i == BOTTOM_2_TOP) {
                this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Bottom_To_Top"));
            }
            this.setMnemonic('S');
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            sortCellElement();
        }

        @Override
        public UIToggleButton createToolBarComponent() {
            return GUICoreUtils.createToolBarComponent(this);
        }

        private void sortCellElement() {
            cs = (CellSelection) ePane.getSelection();
            if (doubleArray == null) {
                return;
            }
            if (!canMove()) {
                return;
            }
            int columnCount = doubleArray.length;
            for (int i = 0; i < columnCount; i++) {
                if (i != 0) {
                    CellElement cellElement = null;
                    CellSelection cs = (CellSelection) ePane.getSelection();
                    if (direction == LEFT_2_RIGHT) {
                        cellElement = ePane.getEditingElementCase().getCellElement(cs.getColumn() + cs.getColumnSpan(), cs.getRow());
                        // marks:如果单元格为空，新建一个
                        if (cellElement == null) {
                            ePane.setSelection(new CellSelection(cs.getColumn() + cs.getColumnSpan(), cs.getRow(), 1, 1));
                        }
                    } else if (direction == RIGHT_2_LEFT) {
                        cellElement = ePane.getEditingElementCase().getCellElement(cs.getColumn() - cs.getColumnSpan(), cs.getRow());
                        // marks:如果单元格为空，新建一个
                        if (cellElement == null) {
                            ePane.setSelection(new CellSelection(cs.getColumn() - cs.getColumnSpan(), cs.getRow(), 1, 1));
                        }
                    } else if (direction == TOP_2_BOTTOM) {
                        cellElement = ePane.getEditingElementCase().getCellElement(cs.getColumn(), cs.getRow() + cs.getRowSpan());
                        // marks:如果单元格为空，新建一个
                        if (cellElement == null) {
                            ePane.setSelection(new CellSelection(cs.getColumn(), cs.getRow() + cs.getRowSpan(), 1, 1));
                        }
                    } else if (direction == BOTTOM_2_TOP) {
                        cellElement = ePane.getEditingElementCase().getCellElement(cs.getColumn(), cs.getRow() - cs.getRowSpan());
                        // marks:如果单元格为空，新建一个
                        if (cellElement == null) {
                            ePane.setSelection(new CellSelection(cs.getColumn(), cs.getRow() - cs.getRowSpan(), 1, 1));
                        }
                    }
                    if (cellElement != null) {
                        ePane.setSelection(new CellSelection(cellElement.getColumn(), cellElement.getRow(), cellElement.getColumnSpan(), cellElement.getRowSpan()));
                    }
                }
                paintDropCellElement(i);
            }
        }

        private boolean canMove() {
            int columnCount = doubleArray.length;
            if (direction == RIGHT_2_LEFT) {
                int k = cs.getColumn() - columnCount + 1;
                if (k < 0) {
                    FineJOptionPane.showMessageDialog(ePane, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Beyond_The_Left_Side_Of_Border"));
                    return false;
                }
            } else if (direction == BOTTOM_2_TOP) {
                int k = cs.getRow() - columnCount + 1;
                if (k < 0) {
                    FineJOptionPane.showMessageDialog(ePane, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Beyond_The_Top_Side_Of_Border"));
                    return false;
                }
            }

            if (ePane.mustInVisibleRange()) {
                if (direction == LEFT_2_RIGHT) {
                    if (!GridUtils.canMove(ePane, cs.getColumn() + columnCount - 1, cs.getRow())) {
                        FineJOptionPane.showMessageDialog(ePane, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Beyond_The_Right_Side_Of_Border"));
                        return false;
                    }
                } else if (direction == TOP_2_BOTTOM) {
                    if (!GridUtils.canMove(ePane, cs.getRow(), cs.getColumn() + columnCount - 1)) {
                        FineJOptionPane.showMessageDialog(ePane, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Beyond_The_Bottom_Side_Of_Border"));
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private void paintDropCellElement(int i) {
        TemplateElementCase report = ePane.getEditingElementCase();
        TemplateCellElement curCellElement = new DefaultTemplateCellElement();
        if (report instanceof WorkSheet || report instanceof PolyECBlock || report instanceof FormElementCase) {
            String[] attribute = doubleArray[i];
            if (ArrayUtils.isEmpty(attribute)) {
                return;
            }

            Object newLinearDSColumn = null;

            DSColumn dsColumn = new DSColumn();
            dsColumn.setDSName(attribute[0]);
            String ColumnName = attribute[1];
            TableDataColumn column;
            if (ColumnName.length() > 0 && ColumnName.charAt(0) == '#') {
                int serialNumber = Integer.parseInt(ColumnName.substring(1));
                column = TableDataColumn.createColumn(serialNumber);
            } else {
                column = TableDataColumn.createColumn(ColumnName);
            }
            dsColumn.setColumn(column);
            newLinearDSColumn = dsColumn;
            CellSelection cs = (CellSelection) ePane.getSelection();
            curCellElement = report.getTemplateCellElement(cs.getColumn(), cs.getRow());

            CellExpandAttr cellExPandAttr = new CellExpandAttr();
            cellExPandAttr.setDirection(Constants.TOP_TO_BOTTOM);
            if (curCellElement == null) {
                curCellElement = new DefaultTemplateCellElement(cs.getColumn(), cs.getRow(), cs.getColumnSpan(), cs.getRowSpan(), newLinearDSColumn);

                report.addCellElement(curCellElement);
            } else {
                // marks:直接覆盖值
                curCellElement.setValue(newLinearDSColumn);
                curCellElement.setCellExpandAttr(cellExPandAttr);
            }
            curCellElement.setCellExpandAttr(cellExPandAttr);
        }
        ePane.setSupportDefaultParentCalculate(true);
        ePane.fireTargetModified();
        ePane.setSupportDefaultParentCalculate(false);
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        dtde.acceptDrag(dtde.getDropAction());
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        Point p = dtde.getLocation();
        DropTargetContext dtc = dtde.getDropTargetContext();
        Grid grid = (Grid) dtc.getComponent();
        grid.doMousePress(p.getX(), p.getY());

        dtde.acceptDrag(dtde.getDropAction());
    }

}
