package com.fr.design.widget.ui;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.FieldEditor;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public abstract class FieldEditorDefinePane<T extends FieldEditor> extends AbstractDataModify<T> {
    protected UICheckBox allowBlankCheckBox;
    // richer:错误信息，是所有控件共有的属性，所以放到这里来
    protected UITextField errorMsgTextField;
    protected JPanel validatePane;

    public FieldEditorDefinePane() {
        this.initComponents();
    }

    protected void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel contentPane = this.setFirstContentPane();
        JPanel jPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jPanel.add(contentPane, BorderLayout.CENTER);
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        if (contentPane != null) {
            UIExpandablePane uiExpandablePane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Advanced"), 280, 24, jPanel);
            this.add(uiExpandablePane, BorderLayout.NORTH);
        }
        this.addValidatePane();
    }


    protected void initErrorMsgPane() {
        // 错误信息
        errorMsgTextField = new UITextField();
//        // richer:主要为了方便查看比较长的错误信息
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

    }

    @Override
    public void populateBean(T ob) {
        this.allowBlankCheckBox.setSelected(ob.isAllowBlank());
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

    protected void addValidatePane() {
        validatePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        final UILabel uiLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Widget_Error_Tip"));
        initErrorMsgPane();
        allowBlankCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Allow_Null"));
        allowBlankCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        JPanel borderPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        final JPanel errorTipPane = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{uiLabel, errorMsgTextField}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L1, LayoutConstants.VGAP_MEDIUM);
        errorTipPane.setBorder(BorderFactory.createEmptyBorder(0, IntervalConstants.INTERVAL_L5, 0, 0));
        borderPane.add(errorTipPane, BorderLayout.CENTER);
        allowBlankCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean isSelected = allowBlankCheckBox.isSelected();
                errorTipPane.setVisible(!isSelected);
            }
        });

        errorTipPane.setBorder(BorderFactory.createEmptyBorder(0, IntervalConstants.INTERVAL_L5, 0, 0));
        Component[][] components = new Component[][]{
                new Component[]{allowBlankCheckBox},
                new Component[]{borderPane},
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components,TableLayoutHelper.FILL_LASTCOLUMN, 5, 5);
        panel.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L1, 0, IntervalConstants.INTERVAL_L6, 0));
        validatePane.add(panel, BorderLayout.NORTH);

        JPanel contentPane = this.setValidatePane();
        if (contentPane != null) {
            validatePane.add(contentPane, BorderLayout.CENTER);
        }


        UIExpandablePane uiExpandablePane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Validate"), 280, 24, validatePane);
        this.add(uiExpandablePane, BorderLayout.CENTER);
    }

    public JPanel setValidatePane() {
        return null;
    }


}
