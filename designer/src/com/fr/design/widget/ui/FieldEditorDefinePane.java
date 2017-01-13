package com.fr.design.widget.ui;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;

import com.fr.base.GraphHelper2;
import com.fr.design.gui.ilable.UILabel;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.FieldEditor;
import com.fr.general.Inter;

public abstract class FieldEditorDefinePane<T extends FieldEditor> extends AbstractDataModify<T> {
    private UICheckBox allowBlankCheckBox;
    // richer:错误信息，是所有控件共有的属性，所以放到这里来
    private UITextField errorMsgTextField;
    private UITextField regErrorMsgTextField;
    private JPanel validatePane;

    public FieldEditorDefinePane() {
        this.initComponents();
    }

    protected void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        regErrorMsgTextField = new UITextField(16);
        regErrorMsgTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                regErrorMsgTextField.setToolTipText(regErrorMsgTextField.getText());
            }

            public void insertUpdate(DocumentEvent e) {
                regErrorMsgTextField.setToolTipText(regErrorMsgTextField.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                regErrorMsgTextField.setToolTipText(regErrorMsgTextField.getText());
            }
        });

        //JPanel firstPanel = FRGUIPaneFactory.createBorderLayout_M_Pane();
        allowBlankCheckBox = new UICheckBox(Inter.getLocText("Allow_Blank"));
        allowBlankCheckBox.setPreferredSize(new Dimension(GraphHelper2.locTextStringWidth("Allow_Blank") + 30, 30));
        allowBlankCheckBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                errorMsgTextField.setEnabled(!allowBlankCheckBox.isSelected());
            }
        });

        // 错误信息
        JPanel errorMsgPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        errorMsgPane.add(new UILabel(Inter.getLocText(new String[]{"Error", "Tooltips"}) + ":"));
        errorMsgTextField = new UITextField(16);
        errorMsgPane.add(errorMsgTextField);

        // richer:主要为了方便查看比较长的错误信息
        errorMsgTextField.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                errorMsgTextField.setToolTipText(errorMsgTextField.getText());
            }

            public void insertUpdate(DocumentEvent e) {
                errorMsgTextField.setToolTipText(errorMsgTextField.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                errorMsgTextField.setToolTipText(errorMsgTextField.getText());
            }
        });
        this.addAllowBlankPane(allowBlankCheckBox);
        JPanel contentPane = this.setFirstContentPane();
        if (contentPane != null) {
            //contentPane.add(firstPanel);
            this.add(contentPane, BorderLayout.NORTH);
        } else {
            //this.add(firstPanel, BorderLayout.CENTER);
        }
    }

    @Override
    public void populateBean(T ob) {
        this.allowBlankCheckBox.setSelected(ob.isAllowBlank());
        errorMsgTextField.setEnabled(!allowBlankCheckBox.isSelected());
        this.errorMsgTextField.setText(ob.getErrorMessage());

        populateSubFieldEditorBean(ob);
    }

    protected abstract void populateSubFieldEditorBean(T ob);

    @Override
    public T updateBean() {
        T e = updateSubFieldEditorBean();

        e.setAllowBlank(this.allowBlankCheckBox.isSelected());
        e.setErrorMessage(this.errorMsgTextField.getText());

        return e;
    }

    protected abstract T updateSubFieldEditorBean();

    protected abstract JPanel setFirstContentPane();

    @Override
    public void checkValid() throws Exception {

    }

    public void addAllowBlankPane(UICheckBox allowBlankCheckBox) {
        JPanel northPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Designer_Validate"));
        validatePane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
        validatePane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        northPane.add(validatePane);
        this.add(northPane, BorderLayout.CENTER);
        JPanel firstPane = GUICoreUtils.createFlowPane(new JComponent[]{allowBlankCheckBox}, FlowLayout.LEFT, 5);
        validatePane.add(firstPane);
        JPanel secondPane = GUICoreUtils.createFlowPane(new JComponent[]{new UILabel(Inter.getLocText(new String[]{"Error", "Tooltips"}) + ":"), errorMsgTextField}, FlowLayout.LEFT, 24);
        secondPane.setPreferredSize(new Dimension(400, 23));
        validatePane.add(secondPane);
    }

    public JPanel getValidatePane() {
        return validatePane;
    }

    public UITextField getRegErrorMsgTextField() {
        return regErrorMsgTextField;
    }

}