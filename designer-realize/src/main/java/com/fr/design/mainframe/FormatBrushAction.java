package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.design.actions.ElementCaseAction;

import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

/**
 * Author : daisy
 * Date: 13-8-7
 * Time: 上午11:05
 */
public class FormatBrushAction extends ElementCaseAction {

    private ElementCasePane ePane;
    private CellSelection oldSelection;


    public FormatBrushAction(ElementCasePane t) {
        super(t);
        this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_M_Edit_FormatBrush"));
        this.setMnemonic('B');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/formatBrush.png"));
        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, DEFAULT_MODIFIER));
    }

    public boolean executeActionReturnUndoRecordNeeded() {
        ePane = (ElementCasePane) getEditingComponent();
        if (ePane != null) {
            Selection selection = ePane.getSelection();
            if (!(selection instanceof CellSelection)) {
                return false;
            }
            oldSelection = ((CellSelection) selection).clone();
            ePane.setFormatReferencedCell(oldSelection);
            int cellRectangleCount = oldSelection.getCellRectangleCount();
            if (cellRectangleCount > 1) {
                //格式刷只支持单次选择的区域，如果用ctrl复选选中了多片区域，点击格式刷按钮时弹出提示
                //判断是不是连续区域
                //荣国是连续区域，那么这些长方形的长加起来应该等于
                if (!isContinueArea()) {
                    JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Can_Not_Use_Format_Brush"));
                    ePane.setFormatState(DesignerContext.FORMAT_STATE_NULL);
                    ePane.getFormatBrush().setSelected(false);
                    return false;
                }
            }
            //只对单个区域进行格式刷操作
            ((ElementCasePane) DesignerContext.getReferencedElementCasePane()).getGrid().setNotShowingTableSelectPane(false);
            ePane.repaint();
            return true;
        }
        return false;
    }


    /**
     * 判断是不是连续区域
     *
     * @return
     */
    private boolean isContinueArea() {
        int xStart = oldSelection.getCellRectangle(1).x;
        int xend = 0;
        int yStrat = oldSelection.getCellRectangle(1).y;
        int yend = 0;
        int totalNum = 0;
        for (int i = 0; i < oldSelection.getCellRectangleCount(); i++) {
            Rectangle temp = oldSelection.getCellRectangle(i);
            if (temp.getX() < xStart) {
                xStart = temp.x;
            }
            if (temp.getX() + temp.getWidth() > xend) {
                xend = (int) (temp.getX() + temp.getWidth());
            }
            if (temp.getY() < yStrat) {
                yStrat = temp.y;
            }
            if (temp.getY() + temp.getHeight() > yend) {
                yend = (int) (temp.getY() + temp.getHeight());
            }
            totalNum += (int) (temp.getWidth() * temp.getHeight());
        }

        if ((xend - xStart) * (yend - yStrat) == totalNum) {
            oldSelection = new CellSelection(xStart, yStrat, (xend - xStart), (yend - yStrat));
            ePane.setSelection(oldSelection);
            ePane.setFormatReferencedCell(oldSelection);
            return true;
        }
        return false;
    }


    public void updateFormatBrush(Style[][] referencedStyle, CellSelection cs, ElementCasePane reportPane) {
        //得到被参照的单元格的行列数
        if (referencedStyle == null) {
            return;
        }
        int rowSpan = referencedStyle[0].length;
        int columnSpan = referencedStyle.length;

        //开始进行格式刷样式复制
        TemplateElementCase elementCase = reportPane.getEditingElementCase();
        int rowNum = cs.getRowSpan();
        int columnNum = cs.getColumnSpan();


        //如果只点选了一个，则自动补足
        if (cs.getColumnSpan() * cs.getRowSpan() == 1) {
            rowNum = rowSpan;
            columnNum = columnSpan;
        }

        for (int j = 0; j < rowNum; j++) {
            for (int i = 0; i < columnNum; i++) {
                int column = i + cs.getColumn();
                int row = j + cs.getRow();
                TemplateCellElement cellElement = elementCase.getTemplateCellElement(column, row);
                if (cellElement == null) {
                    cellElement = new DefaultTemplateCellElement(column, row);
                    elementCase.addCellElement(cellElement);
                }
                cellElement.setStyle(referencedStyle[i % columnSpan][j % rowSpan]);
            }
        }


    }


    private Style[][] getOldStyles(CellSelection oldSelection) {
        Style[][] referencedStyle = new Style[oldSelection.getColumnSpan()][oldSelection.getRowSpan()];
        int cellRectangleCount = oldSelection.getCellRectangleCount();
        TemplateElementCase elementCase = ePane.getEditingElementCase();
        for (int rect = 0; rect < cellRectangleCount; rect++) {
            Rectangle cellRectangle = oldSelection.getCellRectangle(rect);
            for (int j = 0; j < cellRectangle.height; j++) {
                for (int i = 0; i < cellRectangle.width; i++) {
                    int column = i + cellRectangle.x;
                    int row = j + cellRectangle.y;
                    TemplateCellElement cellElement = elementCase.getTemplateCellElement(column, row);
                    if (cellElement == null) {
                        cellElement = new DefaultTemplateCellElement(column, row);
                        elementCase.addCellElement(cellElement);
                    }
                    Style style = cellElement.getStyle();
                    if (style == null) {
                        style = Style.DEFAULT_STYLE;
                    }

                    referencedStyle[i][j] = style;
                }
            }
        }

        return referencedStyle;
    }

}
