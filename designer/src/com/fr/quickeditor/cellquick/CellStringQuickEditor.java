package com.fr.quickeditor.cellquick;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.fr.base.Formula;
import com.fr.base.Style;
import com.fr.base.TextFormat;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.grid.selection.CellSelection;
import com.fr.quickeditor.CellQuickEditor;
import com.fr.report.ReportHelper;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.stable.ColumnRow;
import com.fr.stable.StringUtils;

public class CellStringQuickEditor extends CellQuickEditor {


    private static CellStringQuickEditor THIS;

    private UITextField stringTextField;

	private boolean isEditing = false;

    public static final CellStringQuickEditor getInstance() {
        if (THIS == null) {
            THIS = new CellStringQuickEditor();
        }
        return THIS;
    }

    // august：如果是原来编辑的是公式,要保留公式里的这些属性,不然在公式和字符串转化时,就会丢失这些属性设置
    private boolean reserveInResult = false;
    private boolean reserveOnWriteOrAnaly = true;

    private CellStringQuickEditor() {
        super();
    }

    @Override
    /**
     *
     */
    public JComponent createCenterBody() {
        stringTextField = new UITextField();
        stringTextField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (tc != null) {
                    tc.getGrid().dispatchEvent(e);
                }
            }

        });

        return stringTextField;
    }

    DocumentListener documentListener = new DocumentListener() {

        @Override
        public void insertUpdate(DocumentEvent e) {
            changeReportPaneCell(stringTextField.getText().trim());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            changeReportPaneCell(stringTextField.getText().trim());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            changeReportPaneCell(stringTextField.getText().trim());
        }

    };

    protected void changeReportPaneCell(String tmpText) {
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
            Formula textFormula = new Formula(tmpText);
            textFormula.setReserveInResult(reserveInResult);
            textFormula.setReserveOnWriteOrAnaly(reserveOnWriteOrAnaly);
            cellElement.setValue(textFormula);
        } else {
            Style style = cellElement.getStyle();
            if (cellElement != null && style != null && style.getFormat() != null && style.getFormat() == TextFormat.getInstance()) {
                cellElement.setValue(tmpText);
            } else {
                cellElement.setValue(ReportHelper.convertGeneralStringAccordingToExcel(tmpText));
            }
        }
        fireTargetModified();
        stringTextField.requestFocus();
		isEditing = false;
    }

    @Override
    protected void refreshDetails() {
        String str = null;
        if (cellElement == null) {
            str = StringUtils.EMPTY;
        } else {
            Object value = cellElement.getValue();
            if (value == null) {
                str = StringUtils.EMPTY;
            } else if (value instanceof Formula) {
                Formula formula = (Formula) value;
                str = formula.getContent();
                reserveInResult = formula.isReserveInResult();
                reserveOnWriteOrAnaly = formula.isReserveOnWriteOrAnaly();
            } else {
                str = value.toString();
            }
        }
        showText(str);
        stringTextField.setEditable(tc.isSelectedOneCell());
    }

    /**
     *
     * @param str
     */
    public void showText(String str) {
		// 本编辑框在输入过程中引发的后续事件如果还调用了本框的setText方法不能执行
		if (isEditing) {
			return;
		}
        stringTextField.getDocument().removeDocumentListener(documentListener);
        stringTextField.setText(str);
        stringTextField.getDocument().addDocumentListener(documentListener);
    }

}