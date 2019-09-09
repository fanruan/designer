package com.fr.design.widget.component;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.NumberEditor;
import com.fr.stable.AssistUtils;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by kerry on 2017/9/10.
 */
public class NumberEditorValidatePane extends JPanel {

    private UICheckBox allowDecimalsCheckBox;
    private UICheckBox allowNegativeCheckBox;
    private UICheckBox setMaxValueCheckBox;
    private UICheckBox setMinValueCheckBox;
    private UISpinner maxValueSpinner;
    private UISpinner minValueSpinner;
    private UISpinner decimalLength;
    private JPanel limitNumberPane;
    private UITextField errorMsgTextField;
    private JPanel errorMsgTextFieldPane;

    private ActionListener allowDecimalsListener;

    private ActionListener allowNegativeListener;

    public ActionListener setMaxListener;

    private ActionListener setMinListener;

    private ChangeListener maxValueChangeListener;

    private ChangeListener minValueChangeListener;

    public NumberEditorValidatePane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        initComponent();
    }

    private void initComponent() {
        initListeners();
        this.allowDecimalsCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Allow_Decimals"));
        allowDecimalsCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.allowDecimalsCheckBox.addActionListener(allowDecimalsListener);
        this.decimalLength = new UISpinner(0, Integer.MAX_VALUE, 1, 16);
        this.allowNegativeCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Allow_Negative"));
        this.allowNegativeCheckBox.addActionListener(allowNegativeListener);
        allowNegativeCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.setMaxValueCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Need_Max_Value"), false);
        setMaxValueCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.maxValueSpinner = new UISpinner(-Double.MAX_VALUE, Double.MAX_VALUE, 1D, 0D);
        this.setMaxValueCheckBox.addActionListener(setMaxListener);
        this.maxValueSpinner.addChangeListener(maxValueChangeListener);
        this.setMinValueCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Need_Min_Value"), false);
        this.minValueSpinner = new UISpinner(-Double.MAX_VALUE, Double.MAX_VALUE, 1D, 0D);
        minValueSpinner.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.setMinValueCheckBox.addActionListener(setMinListener);
        this.minValueSpinner.addChangeListener(minValueChangeListener);
        setMinValueCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        initErrorMsgPane();
        JPanel errorMsgBorderPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        errorMsgBorderPane.setBorder(BorderFactory.createEmptyBorder(0, IntervalConstants.INTERVAL_L5, IntervalConstants.INTERVAL_L1, 0));
        errorMsgBorderPane.add(errorMsgTextFieldPane, BorderLayout.CENTER);
        UILabel numberLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Decimal_Digits"));
        limitNumberPane = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{numberLabel, decimalLength}}, TableLayoutHelper.FILL_LASTCOLUMN, 18, 7);
        limitNumberPane.setBorder(BorderFactory.createEmptyBorder(0, IntervalConstants.INTERVAL_L5, 0, 0));
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{allowDecimalsCheckBox, null},
                new Component[]{limitNumberPane, null},
                new Component[]{allowNegativeCheckBox, null},
                new Component[]{setMaxValueCheckBox, maxValueSpinner},
                new Component[]{setMinValueCheckBox, minValueSpinner},
                new Component[]{errorMsgBorderPane, null},
        };
        double[] rowSize = {p, p, p, p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}};
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_L2, IntervalConstants.INTERVAL_L1);
        this.add(panel);
    }

    private void initErrorMsgPane() {
        errorMsgTextField = new UITextField();
        errorMsgTextFieldPane = TableLayoutHelper.createGapTableLayoutPane(
                new Component[][]{new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Widget_Error_Tip")), errorMsgTextField}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L2, IntervalConstants.INTERVAL_L1);
    }


    private void initListeners() {
        allowDecimalsListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (allowDecimalsCheckBox.isSelected()) {
                    limitNumberPane.setVisible(true);
                    limitNumberPane.setPreferredSize(new Dimension(215, 20));
                } else {
                    limitNumberPane.setVisible(false);
                    limitNumberPane.setPreferredSize(new Dimension(0, 0));
                }
            }
        };

        allowNegativeListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (allowNegativeCheckBox.isSelected()) {
                    minValueSpinner.getTextField().setMinValue(-Double.MAX_VALUE);
                    if (!setMinValueCheckBox.isSelected()) {
                        maxValueSpinner.getTextField().setMinValue(-Double.MAX_VALUE);
                    }
                } else {
                    minValueSpinner.getTextField().setMinValue(0.0);
                    if (!setMinValueCheckBox.isSelected()) {
                        maxValueSpinner.getTextField().setMinValue(0.0);
                    }
                    double minValue = Double.parseDouble("" + minValueSpinner.getValue());
                    double maxValue = Double.parseDouble("" + maxValueSpinner.getValue());
                    if (minValue < 0.0) {
                        minValueSpinner.setValue(0.0);
                    }
                    if (maxValue < 0.0) {
                        maxValueSpinner.setValue(0.0);
                    }
                }
            }
        };


        setMaxListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (setMaxValueCheckBox.isSelected()) {
                    maxValueSpinner.setEnabled(true);
                    Double value = (double) 0;
                    if (setMinValueCheckBox.isSelected()) {
                        Double minValue = Double.parseDouble("" + minValueSpinner.getValue());
                        if (minValue > value) {
                            value = minValue;
                        }
                    }
                    maxValueSpinner.setValue(value);
                } else {
                    maxValueSpinner.setEnabled(false);
                    minValueSpinner.getTextField().setMaxValue(Double.MAX_VALUE);
                }
            }
        };


        setMinListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (setMinValueCheckBox.isSelected()) {
                    minValueSpinner.setEnabled(true);
                    Double value = (double) 0;
                    if (setMaxValueCheckBox.isSelected()) {
                        Double maxValue = Double.parseDouble("" + maxValueSpinner.getValue());
                        if (maxValue < value) {
                            value = maxValue;
                        }
                    }
                    minValueSpinner.setValue(value);
                    maxValueSpinner.getTextField().setMinValue(value);
                } else {
                    minValueSpinner.setEnabled(false);
                    maxValueSpinner.getTextField().setMinValue(allowNegativeCheckBox.isSelected() ? (-Double.MAX_VALUE) : (double) 0);
                }
            }
        };

        maxValueChangeListener = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (setMinValueCheckBox.isSelected()) {
                    if (maxValueSpinner.getValue() >= minValueSpinner.getValue()) {
                        minValueSpinner.getTextField().setMaxValue(Double.parseDouble("" + maxValueSpinner.getValue()));
                    } else {
                        minValueSpinner.setValue(maxValueSpinner.getValue());
                    }
                }
            }
        };

        minValueChangeListener = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (setMaxValueCheckBox.isSelected()) {
                    if (minValueSpinner.getValue() <= maxValueSpinner.getValue()) {
                        maxValueSpinner.getTextField().setMinValue(Double.parseDouble("" + minValueSpinner.getValue()));
                    } else {
                        maxValueSpinner.setValue(minValueSpinner.getValue());
                    }
                }
            }
        };
    }

    public void populate(NumberEditor e) {
        allowDecimalsCheckBox.setSelected(e.isAllowDecimals());
        if (e.isAllowDecimals()) {
            this.decimalLength.setValue(e.getMaxDecimalLength());
        } else {
            this.limitNumberPane.setVisible(false);
        }

        allowNegativeCheckBox.setSelected(e.isAllowNegative());
        if (AssistUtils.equals(e.getMaxValue(),Double.MAX_VALUE)) {
            setMaxValueCheckBox.setSelected(false);
            maxValueSpinner.setValue(Double.MAX_VALUE);
            maxValueSpinner.setEnabled(false);
        } else {
            setMaxValueCheckBox.setSelected(true);
            maxValueSpinner.setEnabled(true);
            maxValueSpinner.setValue(e.getMaxValue());
        }

        if (AssistUtils.equals(e.getMinValue(),-Double.MAX_VALUE)) {
            setMinValueCheckBox.setSelected(false);
            minValueSpinner.setValue(-Double.MAX_VALUE);
            minValueSpinner.setEnabled(false);

        } else {
            setMinValueCheckBox.setSelected(true);
            minValueSpinner.setEnabled(true);
            minValueSpinner.setValue(e.getMinValue());
        }
        if (setMinValueCheckBox.isSelected() || setMaxValueCheckBox.isSelected()) {
            errorMsgTextFieldPane.setVisible(true);
            errorMsgTextField.setText(e.getRegErrorMessage());
        } else {
            errorMsgTextFieldPane.setVisible(false);
            errorMsgTextField.setText(StringUtils.EMPTY);
        }
    }

    public void update(NumberEditor ob) {
        ob.setAllowDecimals(allowDecimalsCheckBox.isSelected());
        if (allowDecimalsCheckBox.isSelected()) {
            ob.setMaxDecimalLength((int) this.decimalLength.getValue());
        }

        ob.setAllowNegative(allowNegativeCheckBox.isSelected());

        if (setMinValueCheckBox.isSelected()) {
            ob.setMinValue(minValueSpinner.getValue());
        } else {
            ob.setMinValue(-Double.MAX_VALUE);
        }

        if (setMaxValueCheckBox.isSelected()) {
            ob.setMaxValue(maxValueSpinner.getValue());
        } else {
            ob.setMaxValue(Double.MAX_VALUE);
        }

        if (setMinValueCheckBox.isSelected() || setMaxValueCheckBox.isSelected()) {
            errorMsgTextFieldPane.setVisible(true);
        } else {
            errorMsgTextFieldPane.setVisible(false);
            errorMsgTextField.setText(StringUtils.EMPTY);
        }
        ob.setRegErrorMessage(errorMsgTextField.getText());
    }
}
