package com.fr.quickeditor.floatquick;

import com.fr.base.BaseFormula;
import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.base.TextFormat;
import com.fr.design.constants.UIConstants;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.quickeditor.FloatQuickEditor;
import com.fr.report.ReportHelper;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FloatStringQuickEditor extends FloatQuickEditor {
    private JTextArea stringTextField;
    private UIButton formulaButton;

    // august：如果是原来编辑的是公式,要保留公式里的这些属性,不然在公式和字符串转化时,就会丢失这些属性设置
    private boolean reserveInResult = false;
    private boolean reserveOnWriteOrAnaly = true;

    public FloatStringQuickEditor() {
        super();
        stringTextField = new JTextArea();
        initTextField();
        formulaButton = new UIButton();
        formulaButton.setPreferredSize(new Dimension(25, 23));
        formulaButton.setIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/formula.png"));
        formulaButton.addActionListener(getFormulaActionListener);
        JPanel pane = new JPanel(new BorderLayout(5, 0));
        pane.add(stringTextField, BorderLayout.CENTER);
        pane.add(formulaButton, BorderLayout.EAST);
        pane.setBorder(BorderFactory.createEmptyBorder(0,0,0,5));
        formulaButton.setVisible(false);
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(pane, BorderLayout.NORTH);
    }

    private void initTextField() {
        stringTextField.setFont(new Font("Dialog", Font.PLAIN, 12));
        stringTextField.setOpaque(true);
        stringTextField.setLineWrap(true);
        stringTextField.setWrapStyleWord(true);
        stringTextField.setMargin(new Insets(5, 5, 5, 5));
        stringTextField.setBorder(BorderFactory.createLineBorder(UIConstants.POP_DIALOG_BORDER));
        stringTextField.setBackground(Color.WHITE);
    }

    ActionListener getFormulaActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ((ElementCasePane) HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().getCurrentElementCasePane()).getGrid().startEditing();
        }
    };

    @Override
    protected void refreshDetails() {
        String str = null;
        Object value = floatElement.getValue();
        if (value == null) {
            str = StringUtils.EMPTY;
        } else if (value instanceof BaseFormula) {
            //MoMeak: 没拆文本框和公式所以需要这么个玩意
            formulaButton.setVisible(true);
            BaseFormula formula = (BaseFormula) value;
            str = formula.getContent();
            stringTextField.setLineWrap(false);
            this.setBorder(BorderFactory.createEmptyBorder(10, 75, 10, 10));
            reserveInResult = formula.isReserveInResult();
            reserveOnWriteOrAnaly = formula.isReserveOnWriteOrAnaly();
        } else {
            str = value.toString();
        }
        showText(str);
    }

    public void showText(String str) {
        stringTextField.getDocument().removeDocumentListener(documentListener);
        stringTextField.setText(str);
        stringTextField.getDocument().addDocumentListener(documentListener);
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
        if (tmpText != null && (tmpText.length() > 0 && tmpText.charAt(0) == '=')) {
            BaseFormula textFormula = BaseFormula.createFormulaBuilder().build(tmpText);
            textFormula.setReserveInResult(reserveInResult);
            textFormula.setReserveOnWriteOrAnaly(reserveOnWriteOrAnaly);
            floatElement.setValue(textFormula);
        } else {
            Style style = floatElement.getStyle();
            if (floatElement != null && style != null && style.getFormat() != null && style.getFormat() == TextFormat.getInstance()) {
                floatElement.setValue(tmpText);
            } else {
                floatElement.setValue(ReportHelper.convertGeneralStringAccordingToExcel(tmpText));
            }
        }
        fireTargetModified();
        stringTextField.requestFocus();
    }

}