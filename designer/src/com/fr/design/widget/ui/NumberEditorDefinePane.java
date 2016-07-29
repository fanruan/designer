package com.fr.design.widget.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

import com.fr.design.gui.ilable.UILabel;

import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultFormatter;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.NumberEditor;
import com.fr.general.Inter;
import com.fr.design.utils.gui.GUICoreUtils;

public class NumberEditorDefinePane extends FieldEditorDefinePane<NumberEditor> {
    /**
     *
     */
    private static final long serialVersionUID = 8011242951911686805L;
    private UICheckBox allowDecimalsCheckBox;
    private UICheckBox allowNegativeCheckBox;
    private UICheckBox setMaxValueCheckBox;
    private UICheckBox setMinValueCheckBox;
    private UIBasicSpinner maxValueSpinner;
    private SpinnerNumberModel maxValueModel;
    private UIBasicSpinner minValueSpinner;
    private SpinnerNumberModel minValueModel;
    private com.fr.design.editor.editor.IntegerEditor decimalLength;
    private JPanel limitNumberPane;
    private WaterMarkDictPane waterMarkDictPane;
    private UITextField regErrorMsgTextField;

    private ActionListener actionListener1 = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (allowDecimalsCheckBox.isSelected()) {
                limitNumberPane.setVisible(true);
            } else {
                limitNumberPane.setVisible(false);
            }
        }
    };

    private ActionListener actionListener2 = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (allowNegativeCheckBox.isSelected()) {
                minValueModel.setMinimum(-Double.MAX_VALUE);
                if (!setMinValueCheckBox.isSelected()) {
                    maxValueModel.setMinimum(-Double.MAX_VALUE);
                }
            } else {
                minValueModel.setMinimum(0.0);
                if (!setMinValueCheckBox.isSelected()) {
                    maxValueModel.setMinimum(0.0);
                }
                Double minValue = Double.parseDouble("" + minValueSpinner.getValue());
                Double maxValue = Double.parseDouble("" + maxValueSpinner.getValue());
                if (minValue < 0.0) {
                    minValueSpinner.setValue(0.0);
                }
                if (maxValue < 0.0) {
                    maxValueSpinner.setValue(0.0);
                }
            }
        }
    };


    private ActionListener actionListener3 = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (setMaxValueCheckBox.isSelected()) {
                maxValueSpinner.setVisible(true);
                Double value = new Double(0);
                if (setMinValueCheckBox.isSelected()) {
                    Double minValue = Double.parseDouble("" + minValueSpinner.getValue());
                    if (minValue > value) {
                        value = minValue;
                    }
                }
                maxValueSpinner.setValue(value);
            } else {
                maxValueSpinner.setVisible(false);
                minValueModel.setMaximum(Double.MAX_VALUE);
            }
        }
    };


    private ActionListener actionListener4 = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (setMinValueCheckBox.isSelected()) {
                minValueSpinner.setVisible(true);
                Double value = new Double(0);
                if (setMaxValueCheckBox.isSelected()) {
                    Double maxValue = Double.parseDouble("" + maxValueSpinner.getValue());
                    if (maxValue < value) {
                        value = maxValue;
                    }
                }
                minValueSpinner.setValue(value);
            } else {
                minValueSpinner.setVisible(false);
                maxValueModel.setMinimum(allowNegativeCheckBox.isSelected() ? (-Double.MAX_VALUE) : new Double(0));
            }
        }
    };

    private ChangeListener changeListener1 = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            if (setMaxValueCheckBox.isSelected()) {
                if (setMinValueCheckBox.isSelected()) {
                    minValueModel.setMaximum(Double.parseDouble("" + maxValueSpinner.getValue()));
                }
            }
        }
    };

    private ChangeListener changeListener2 = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            if (setMinValueCheckBox.isSelected()) {
                if (setMaxValueCheckBox.isSelected()) {
                    maxValueModel.setMinimum(Double.parseDouble("" + minValueSpinner.getValue()));
                }
            }
        }
    };

    public NumberEditorDefinePane() {
        this.initComponents();
    }


    @Override
    protected String title4PopupWindow() {
        return "number";
    }

    @Override
    protected JPanel setFirstContentPane() {
        JPanel content = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
        content.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        content.setLayout(FRGUIPaneFactory.createBorderLayout());
        // richer:数字的允许直接编辑没有意义

        JPanel northPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("Advanced"));
        content.add(northPane, BorderLayout.NORTH);

        waterMarkDictPane = new WaterMarkDictPane();
        northPane.add(waterMarkDictPane);
        JPanel centerPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Designer_Validate"));
        JPanel validatePane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
        validatePane.setPreferredSize(new Dimension(400,200));
        centerPane.add(validatePane);
        content.add(northPane, BorderLayout.NORTH);
        content.add(centerPane, BorderLayout.CENTER);
        validatePane.add(GUICoreUtils.createFlowPane(getAllowBlankCheckBox(), FlowLayout.LEFT));
        validatePane.add(GUICoreUtils.createFlowPane(new JComponent[]{new UILabel(Inter.getLocText(new String[]{"Error", "Tooltips"}) + ":"),getErrorMsgTextField()}, FlowLayout.LEFT,24));

        this.allowDecimalsCheckBox = new UICheckBox(Inter.getLocText("Allow_Decimals"));
        this.decimalLength = new com.fr.design.editor.editor.IntegerEditor();
        this.decimalLength.setColumns(4);
        limitNumberPane = GUICoreUtils.createFlowPane(new JComponent[]{new UILabel(Inter.getLocText(new String[]{"Double", "Numbers"}) + ":"), this.decimalLength},
                FlowLayout.LEFT, 4);
        validatePane.add(GUICoreUtils.createFlowPane(new JComponent[]{this.allowDecimalsCheckBox, limitNumberPane}, FlowLayout.LEFT, 4));
        this.allowDecimalsCheckBox.addActionListener(actionListener1);

        this.allowNegativeCheckBox = new UICheckBox(Inter.getLocText("Allow_Negative"));
        validatePane.add(GUICoreUtils.createFlowPane(new JComponent[]{this.allowNegativeCheckBox}, FlowLayout.LEFT, 4));
        this.allowNegativeCheckBox.addActionListener(actionListener2);

        this.setMaxValueCheckBox = new UICheckBox(Inter.getLocText("Need_Max_Value"), false);

        this.maxValueSpinner = new UIBasicSpinner(maxValueModel = new SpinnerNumberModel(0D, -Double.MAX_VALUE, Double.MAX_VALUE, 1D));
        maxValueSpinner.setPreferredSize(new Dimension(120, 20));
        setNotAllowsInvalid(this.maxValueSpinner);
        validatePane.add(GUICoreUtils.createFlowPane(new JComponent[]{this.setMaxValueCheckBox, this.maxValueSpinner}, FlowLayout.LEFT, 4));
        this.maxValueSpinner.setVisible(false);
        this.setMaxValueCheckBox.addActionListener(actionListener3);
        this.maxValueSpinner.addChangeListener(changeListener1);

        this.setMinValueCheckBox = new UICheckBox(Inter.getLocText("Need_Min_Value"), false);
        this.minValueSpinner = new UIBasicSpinner(minValueModel = new SpinnerNumberModel(0D, -Double.MAX_VALUE, Double.MAX_VALUE, 1D));
        minValueSpinner.setPreferredSize(new Dimension(120, 20));
        setNotAllowsInvalid(this.minValueSpinner);
        validatePane.add(GUICoreUtils.createFlowPane(new JComponent[]{this.setMinValueCheckBox, this.minValueSpinner}, FlowLayout.LEFT, 4));
        this.minValueSpinner.setVisible(false);
        this.setMinValueCheckBox.addActionListener(actionListener4);
        this.minValueSpinner.addChangeListener(changeListener2);

        regErrorMsgTextField = new UITextField(16);
        validatePane.add(GUICoreUtils.createFlowPane(new JComponent[]{new UILabel(Inter.getLocText(new String[]{"Error", "Tooltips"}) + ":"),regErrorMsgTextField}, FlowLayout.LEFT,24));

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

        return content;
    }

    @Override
    protected void populateSubFieldEditorBean(NumberEditor e) {
        allowDecimalsCheckBox.setSelected(e.isAllowDecimals());
        if (e.isAllowDecimals()) {
            this.decimalLength.setValue(e.getMaxDecimalLength());
        } else {
            this.limitNumberPane.setVisible(false);
        }

        allowNegativeCheckBox.setSelected(e.isAllowNegative());
        if (e.getMaxValue() == Double.MAX_VALUE) {
            setMaxValueCheckBox.setSelected(false);
            maxValueSpinner.setValue(new Double(Double.MAX_VALUE));
            maxValueSpinner.setVisible(false);

        } else {
            setMaxValueCheckBox.setSelected(true);
            maxValueSpinner.setVisible(true);
            maxValueSpinner.setValue(new Double(e.getMaxValue()));
        }

        if (e.getMinValue() == -Double.MAX_VALUE) {
            setMinValueCheckBox.setSelected(false);
            minValueSpinner.setValue(new Double(-Double.MAX_VALUE));
            minValueSpinner.setVisible(false);

        } else {
            setMinValueCheckBox.setSelected(true);
            minValueSpinner.setVisible(true);
            minValueSpinner.setValue(new Double(e.getMinValue()));
        }
        this.regErrorMsgTextField.setText(e.getRegErrorMessage());
        this.waterMarkDictPane.populate(e);
    }

    @Override
    protected NumberEditor updateSubFieldEditorBean() {

        NumberEditor ob = new NumberEditor();
        ob.setAllowDecimals(allowDecimalsCheckBox.isSelected());
        if (allowDecimalsCheckBox.isSelected()) {
            ob.setMaxDecimalLength(this.decimalLength.getValue());
        }

        ob.setAllowNegative(allowNegativeCheckBox.isSelected());
        if (setMaxValueCheckBox.isSelected()) {
            ob.setMaxValue(Double.parseDouble("" + maxValueSpinner.getValue()));
        } else {
            ob.setMaxValue(Double.MAX_VALUE);
        }

        if (setMinValueCheckBox.isSelected()) {
            ob.setMinValue(Double.parseDouble("" + minValueSpinner.getValue()));
        } else {
            ob.setMinValue(-Double.MAX_VALUE);
        }

        this.waterMarkDictPane.update(ob);

        ob.setRegErrorMessage(this.regErrorMsgTextField.getText());

        return ob;
    }

    private void checkVisible() {
        if (setMinValueCheckBox.isSelected()) {
            minValueSpinner.setVisible(true);
        } else {
            minValueSpinner.setVisible(false);
        }

        if (setMinValueCheckBox.isSelected()) {
            minValueSpinner.setVisible(true);
        } else {
            minValueSpinner.setVisible(false);
        }
    }

    private void setNotAllowsInvalid(UIBasicSpinner jspinner) {
        JComponent editor = jspinner.getEditor();
        if (editor instanceof UIBasicSpinner.DefaultEditor) {
            JFormattedTextField ftf = ((UIBasicSpinner.DefaultEditor) editor).getTextField();
            ftf.setColumns(10);
            JFormattedTextField.AbstractFormatter formatter = ftf.getFormatter();
            DefaultFormatter df = (DefaultFormatter) formatter;
            df.setAllowsInvalid(false);
        }
    }

}