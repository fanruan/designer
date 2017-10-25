/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.editor.editor;

import com.fr.base.BaseFormula;
import com.fr.base.Formula;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * CellEditor used to edit Formula object.
 *
 * @editor zhou
 * @since 2012-3-29下午6:27:27
 */
public class FormulaEditor extends Editor<BaseFormula> {
    private BaseFormula formula = BaseFormula.createFormulaBuilder().build();
    private UITextField currentTextField;
    private ShowPaneListener listener = new ShowPaneListener();

    /**
     * Constructor.
     */
    public FormulaEditor() {
        this("");
    }

    public FormulaEditor(String name) {
        this(name, null);
    }

    public FormulaEditor(String name, BaseFormula formula) {
        if (formula != null) {
            this.formula = formula;
        }
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        JPanel editPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        currentTextField = new UITextField(28);
        currentTextField.setText(this.formula.getContent());

        editPane.add(currentTextField, BorderLayout.CENTER);
        currentTextField.setEditable(false);
        currentTextField.addMouseListener(listener);
        this.add(editPane, BorderLayout.CENTER);
        this.setName(name);
    }

    private class ShowPaneListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if (currentTextField.isEnabled()) {
                showFormulaPane();
            }
        }
    }

    public void setColumns(int i) {
        this.currentTextField.setColumns(i);
    }


    /**
     * 选中时弹出公式编辑框
     */
    public void selected() {
        showFormulaPane();
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        currentTextField.setEnabled(enabled);
    }


    protected void showFormulaPane() {
        final UIFormula formulaPane = FormulaFactory.createFormulaPaneWhenReserveFormula();
        formulaPane.populate(formula);
        formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor(FormulaEditor.this), new DialogActionAdapter() {

            @Override
            public void doOk() {
                formula = formulaPane.update();
                setValue(formula);
                fireStateChanged();
            }
        }).setVisible(true);
    }

    public BaseFormula getFormula(){
        return formula;
    }

    /**
     * Return the value of the CellEditor.
     */
    @Override
    public BaseFormula getValue() {
        if (formula != null && "=".equals(formula.getContent())) {
            return null;
        }
        return formula;
    }

    /**
     * Set the value to the CellEditor.
     */
    @Override
    public void setValue(BaseFormula value) {
        if (value == null) {
            value = BaseFormula.createFormulaBuilder().build();
        }
        this.formula = value;
        currentTextField.setText(value.toString());
    }

    /**
     * 增加文本监听
     *
     * @param l 监听器
     */
    public void addDocumentListener(DocumentListener l) {
        currentTextField.getDocument().addDocumentListener(l);
    }

    public String getIconName() {
        return "type_formula";
    }

    /**
     * object是否是公司类型对象
     *
     * @param object 需判断的对象
     * @return 是公式类型则返回true
     */
    public boolean accept(Object object) {
        return object instanceof Formula;
    }

    /**
     * 重置
     */
    public void reset() {
        currentTextField.setText("=");
        formula = BaseFormula.createFormulaBuilder().build();
    }

    /**
     * 清楚数据
     */
    public void clearData() {
        reset();
    }

    /**
     * 是否可用
     *
     * @param flag 为true代表可用
     */
    public void enableEditor(boolean flag) {
        this.setEnabled(flag);
        this.currentTextField.setEnabled(flag);
        if (flag == false) {
            this.currentTextField.removeMouseListener(listener);
        } else {
            int listenerSize = this.currentTextField.getMouseListeners().length;
            for (int i = 0; i < listenerSize; i++) {
                this.currentTextField.removeMouseListener(listener);
            }
            this.currentTextField.addMouseListener(listener);
        }
    }
}