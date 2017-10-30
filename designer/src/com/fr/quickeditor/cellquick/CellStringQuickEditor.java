package com.fr.quickeditor.cellquick;

import com.fr.base.BaseFormula;
import com.fr.base.Style;
import com.fr.base.TextFormat;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.grid.GridKeyListener;
import com.fr.grid.selection.CellSelection;
import com.fr.quickeditor.CellQuickEditor;
import com.fr.report.ReportHelper;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.stable.ColumnRow;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 */
public class CellStringQuickEditor extends CellQuickEditor {
    //文本域 直接可以自适应大小
    private UITextArea stringTextArea;
    //编辑状态
    private boolean isEditing = false;

    //august：如果是原来编辑的是公式,要保留公式里的这些属性,不然在公式和字符串转化时,就会丢失这些属性设置。
    private boolean reserveInResult = false;
    private boolean reserveOnWriteOrAnaly = true;

    private DocumentListener documentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            changeReportPaneCell(stringTextArea.getText().trim());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            changeReportPaneCell(stringTextArea.getText().trim());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            changeReportPaneCell(stringTextArea.getText().trim());
        }

    };

    private CellStringQuickEditor() {
        super();
    }

    /**
     * 详细信息面板
     */
    @Override
    public JComponent createCenterBody() {
        JPanel content = new JPanel(new BorderLayout());
        stringTextArea = new UITextArea();
        stringTextArea.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (tc == null) {
                    return;
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    //todo yaoh.wu:按enter键换至下一个单元格 虽然模仿选中单元格按enter这种场景可以做到,但是原理没有弄清楚。
                    GridKeyListener dispatchListener = new GridKeyListener(tc.getGrid());
                    dispatchListener.keyPressed(e);
                    dispatchListener.keyTyped(e);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (tc != null) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        return;
                    }
                    tc.getGrid().dispatchEvent(e);
                }
            }
        });
        content.add(stringTextArea, BorderLayout.CENTER);
        return content;
    }

    @Override
    public boolean isScrollAll() {
        return true;
    }


    private void changeReportPaneCell(String tmpText) {
        isEditing = true;
        //refresh一下，如果单元格内有新添加的控件，此时并不知道
        CellSelection cs1 = (CellSelection) tc.getSelection();
        ColumnRow columnRow = ColumnRow.valueOf(cs1.getColumn(), cs1.getRow());
        columnRowTextField.setText(columnRow.toString());
        cellElement = tc.getEditingElementCase().getTemplateCellElement(cs1.getColumn(), cs1.getRow());

        if (cellElement == null) {
            CellSelection cs = (CellSelection) tc.getSelection();
            cellElement = new DefaultTemplateCellElement(cs.getColumn(), cs.getRow());
            tc.getEditingElementCase().addCellElement(cellElement, false);
        }
        if (tmpText != null && (tmpText.length() > 0 && tmpText.charAt(0) == '=')) {
            BaseFormula textFormula = BaseFormula.createFormulaBuilder().build(tmpText);
            textFormula.setReserveInResult(reserveInResult);
            textFormula.setReserveOnWriteOrAnaly(reserveOnWriteOrAnaly);
            cellElement.setValue(textFormula);
        } else {
            Style style = cellElement.getStyle();
            if (style != null && style.getFormat() != null && style.getFormat() == TextFormat.getInstance()) {
                cellElement.setValue(tmpText);
            } else {
                cellElement.setValue(ReportHelper.convertGeneralStringAccordingToExcel(tmpText));
            }
        }
        fireTargetModified();
        stringTextArea.requestFocus();
        isEditing = false;
    }

    /**
     * 刷新详细内容
     */
    @Override
    protected void refreshDetails() {
        String str;
        if (cellElement == null) {
            str = StringUtils.EMPTY;
        } else {
            Object value = cellElement.getValue();
            if (value == null) {
                str = StringUtils.EMPTY;
            } else if (value instanceof BaseFormula) {
                BaseFormula formula = (BaseFormula) value;
                str = formula.getContent();
                reserveInResult = formula.isReserveInResult();
                reserveOnWriteOrAnaly = formula.isReserveOnWriteOrAnaly();
            } else {
                str = value.toString();
            }
        }
        showText(str);
        stringTextArea.setEditable(tc.isSelectedOneCell());
    }

    /**
     * 显示文本
     *
     * @param str 文本
     */
    public void showText(String str) {
        // 正在编辑时不处理
        if (isEditing) {
            return;
        }
        stringTextArea.getDocument().removeDocumentListener(documentListener);
        stringTextArea.setText(str);
        stringTextArea.getDocument().addDocumentListener(documentListener);
    }

    @Override
    public Object getComboBoxSelected() {
        return null;
    }

}