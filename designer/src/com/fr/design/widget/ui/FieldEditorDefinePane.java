package com.fr.design.widget.ui;

import com.fr.base.GraphHelper;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
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
    private static final int ALLOW_BLANK_CHECK_BOX_WIDTH = GraphHelper.getLocTextWidth("FR-Designer_Allow_Null") + 30;
    private static final int ALLOW_BLANK_CHECK_BOX_HEIGHT = 30;
    protected UICheckBox allowBlankCheckBox;
    // richer:错误信息，是所有控件共有的属性，所以放到这里来
    protected UITextField errorMsgTextField;
    protected JPanel validatePane;

    public FieldEditorDefinePane() {
        this.initComponents();
    }

    protected void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        initErrorMsgPane();
        JPanel contentPane = this.setFirstContentPane();
        if (contentPane != null) {
            UIExpandablePane uiExpandablePane = new UIExpandablePane(Inter.getLocText("FR-Designer_Advanced"), 280, 24, contentPane);
            this.add(uiExpandablePane, BorderLayout.NORTH);
        }
        this.addValidatePane();
    }


    protected void initErrorMsgPane() {
        // 错误信息
        errorMsgTextField = new UITextField(10);

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

    }

    @Override
    public void populateBean(T ob) {
        this.allowBlankCheckBox.setSelected(ob.isAllowBlank());
//        errorMsgTextField.setEnabled(!allowBlankCheckBox.isSelected());
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
        final UILabel uiLabel = new UILabel(Inter.getLocText(new String[]{"FR-Designer_Error", "FR-Designer_Tooltips"}));
        errorMsgTextField = new UITextField(10);
        allowBlankCheckBox = new UICheckBox(Inter.getLocText("FR-Designer_Allow_Null"));
        allowBlankCheckBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
        allowBlankCheckBox.setPreferredSize(new Dimension(ALLOW_BLANK_CHECK_BOX_WIDTH, ALLOW_BLANK_CHECK_BOX_HEIGHT));
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
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_SMALL, 1);
        validatePane.add(panel, BorderLayout.NORTH);

        JPanel contentPane = this.setValidatePane();
        if (contentPane != null) {
            validatePane.add(contentPane, BorderLayout.CENTER);
        }


        UIExpandablePane uiExpandablePane = new UIExpandablePane(Inter.getLocText("FR-Designer_Validate"), 280, 24, validatePane);
        this.add(uiExpandablePane, BorderLayout.CENTER);

//        JPanel firstPane = GUICoreUtils.createFlowPane(new JComponent[]{allowBlankCheckBox}, FlowLayout.LEFT, 5);
//        validatePane.add(firstPane);
//        JPanel secondPane = new JPanel(FRGUIPaneFactory.createLabelFlowLayout());
//        secondPane.add(new UILabel("错误提示" + ":"));
//        secondPane.add(errorMsgTextField);
//        JPanel secondPane = GUICoreUtils.createFlowPane(new JComponent[]{new UILabel(Inter.getLocText(new String[]{"Error", "Tooltips"}) + ":"), errorMsgTextField}, FlowLayout.LEFT, 24);
//        secondPane.setPreferredSize(new Dimension(400, 23));
//        validatePane.add(secondPane);
    }

    public JPanel setValidatePane() {
        return null;
    }


}