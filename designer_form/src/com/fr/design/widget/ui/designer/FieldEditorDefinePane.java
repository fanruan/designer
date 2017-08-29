package com.fr.design.widget.ui.designer;

import com.fr.design.designer.creator.*;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.FieldEditor;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public abstract class FieldEditorDefinePane<T extends FieldEditor> extends AbstractDataModify<T> {
    protected UICheckBox allowBlankCheckBox;
    // richer:错误信息，是所有控件共有的属性，所以放到这里来
    protected UITextField errorMsgTextField;
    protected JPanel validatePane;
    protected UISpinner fontSizePane;
    protected UITextField labelNameTextField;


    public FieldEditorDefinePane(XCreator xCreator) {
        super(xCreator);
        this.initComponents();
    }

    public FieldEditorDefinePane() {
        this.initComponents();
    }

    protected void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        labelNameTextField = new UITextField();
        allowBlankCheckBox = new UICheckBox(Inter.getLocText("FR-Designer_Allow_Null"));
        allowBlankCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        fontSizePane = new UISpinner(0, 20, 1, 0);
        errorMsgTextField = new UITextField();
        JPanel contentPane = this.setFirstContentPane();
        if (contentPane != null) {
            UIExpandablePane uiExpandablePane = new UIExpandablePane(Inter.getLocText("FR-Designer_Advanced"), 280, 20, contentPane);
            this.add(uiExpandablePane, BorderLayout.NORTH);
        }
        this.addValidatePane();
    }

    @Override
    public void populateBean(T ob) {
        this.allowBlankCheckBox.setSelected(ob.isAllowBlank());
        this.errorMsgTextField.setText(ob.getErrorMessage());
        this.fontSizePane.setValue(ob.getFontSize());
        this.labelNameTextField.setText(ob.getLabelName());
        populateSubFieldEditorBean(ob);
    }

    protected abstract void populateSubFieldEditorBean(T ob);

    @Override
    public T updateBean() {
        T e = updateSubFieldEditorBean();

        e.setAllowBlank(this.allowBlankCheckBox.isSelected());
        e.setErrorMessage(this.errorMsgTextField.getText());
        e.setFontSize((int)fontSizePane.getValue());
        e.setLabelName(labelNameTextField.getText());
        return e;
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


    protected abstract T updateSubFieldEditorBean();

    protected abstract JPanel setFirstContentPane();


    @Override
    public void checkValid() throws Exception {

    }

    protected void addValidatePane() {
        initErrorMsgPane();
        validatePane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        final UILabel uiLabel = new UILabel(Inter.getLocText("FR-Designer_Widget_Error_Tip"));
        allowBlankCheckBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean isSelected = allowBlankCheckBox.isSelected();
                uiLabel.setVisible(!isSelected);
                errorMsgTextField.setVisible(!isSelected);
                if (isSelected) {
                    uiLabel.setPreferredSize(new Dimension(0, 0));
                    errorMsgTextField.setPreferredSize(new Dimension(0, 0));
                } else {
                    uiLabel.setPreferredSize(new Dimension(66, 20));
                    errorMsgTextField.setPreferredSize(new Dimension(150, 20));
                }
            }
        });


        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{allowBlankCheckBox, null},
                new Component[]{uiLabel, errorMsgTextField},
        };
        double[] rowSize = {p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}};
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 5, 5);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        validatePane.add(panel, BorderLayout.NORTH);
        JPanel contentPane = this.setValidatePane();
        if (contentPane != null) {
            validatePane.add(contentPane, BorderLayout.CENTER);
        }

        UIExpandablePane uiExpandablePane = new UIExpandablePane(Inter.getLocText("FR-Designer_Validate"), 280, 20, validatePane);
        this.add(uiExpandablePane, BorderLayout.CENTER);

    }

    public XLayoutContainer getParent(XCreator source) {
        XLayoutContainer container = XCreatorUtils.getParentXLayoutContainer(source);
        if (source.acceptType(XWFitLayout.class) || source.acceptType(XWParameterLayout.class)) {
            container = null;
        }
        return container;
    }

    public JPanel setValidatePane() {
        return null;
    }

}