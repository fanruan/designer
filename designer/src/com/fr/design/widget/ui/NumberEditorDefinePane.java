package com.fr.design.widget.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.gui.ilable.UILabel;

import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.NumberEditor;
import com.fr.general.Inter;

public class NumberEditorDefinePane extends FieldEditorDefinePane<NumberEditor> {
    /**
     * FieldEditorDefinePane
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
    private UISpinner decimalLength;
    private JPanel limitNumberPane;
    private WaterMarkDictPane waterMarkDictPane;

    private ActionListener allowDecimalsListener;

    private ActionListener allowNegativeListener ;

    public ActionListener setMaxListener;

    private ActionListener setMinListener;

    private ChangeListener maxValueChangeListener;

    private ChangeListener minValueChangeListener;

    public NumberEditorDefinePane() {
    }


    @Override
    protected String title4PopupWindow() {
        return "number";
    }

    @Override
    protected JPanel setFirstContentPane() {
        JPanel content = FRGUIPaneFactory.createBorderLayout_S_Pane();
        waterMarkDictPane = new WaterMarkDictPane();
        waterMarkDictPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        content.add(waterMarkDictPane, BorderLayout.CENTER);
        return content;
    }


    public JPanel setValidatePane() {
        initListeners();

        this.allowDecimalsCheckBox = new UICheckBox(Inter.getLocText("FR-Designer_Allow_Decimals"));
        allowDecimalsCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.decimalLength = new UISpinner(0, Integer.MAX_VALUE, 1, 16);
        this.allowDecimalsCheckBox.addActionListener(allowDecimalsListener);

        this.allowNegativeCheckBox = new UICheckBox(Inter.getLocText("FR-Designer_Allow_Negative"));
        this.allowNegativeCheckBox.addActionListener(allowNegativeListener);
        allowNegativeCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        this.setMaxValueCheckBox = new UICheckBox(Inter.getLocText("FR-Designer_Max_Value"), false);
        setMaxValueCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.maxValueSpinner = new UIBasicSpinner(maxValueModel = new SpinnerNumberModel(0D, -Double.MAX_VALUE, Double.MAX_VALUE, 1D));
        setNotAllowsInvalid(this.maxValueSpinner);
        this.setMaxValueCheckBox.addActionListener(setMaxListener);
        this.maxValueSpinner.addChangeListener(maxValueChangeListener);

        this.setMinValueCheckBox = new UICheckBox(Inter.getLocText("FR-Designer_Min_Value"), false);
        setMinValueCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.minValueSpinner = new UIBasicSpinner(minValueModel = new SpinnerNumberModel(0D, -Double.MAX_VALUE, Double.MAX_VALUE, 1D));
        setNotAllowsInvalid(this.minValueSpinner);
        this.setMinValueCheckBox.addActionListener(setMinListener);
        this.minValueSpinner.addChangeListener(minValueChangeListener);

        UILabel numberLabel = new UILabel(Inter.getLocText(new String[]{"FR-Designer_Double", "Numbers"}));
        limitNumberPane = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{numberLabel, decimalLength}}, TableLayoutHelper.FILL_LASTCOLUMN, 18, 7);
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{allowDecimalsCheckBox, null},
                new Component[]{limitNumberPane, null},
                new Component[]{allowNegativeCheckBox, null},
                new Component[]{setMaxValueCheckBox, maxValueSpinner},
                new Component[]{setMinValueCheckBox, minValueSpinner},
        };
        double[] rowSize = {p, p, p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}};
        JPanel pane = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM);
        pane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        return pane;
    }


    @Override
    protected void populateSubFieldEditorBean(NumberEditor e) {
        allowDecimalsCheckBox.setSelected(e.isAllowDecimals());
        if (e.isAllowDecimals()) {
            this.decimalLength.setValue(e.getMaxDecimalLength());
        } else {
            this.limitNumberPane.setVisible(false);
            this.limitNumberPane.setPreferredSize(new Dimension(0,0));
        }

        allowNegativeCheckBox.setSelected(e.isAllowNegative());
        if (e.getMaxValue() == Double.MAX_VALUE) {
            setMaxValueCheckBox.setSelected(false);
            maxValueSpinner.setValue(new Double(Double.MAX_VALUE));
            maxValueSpinner.setEnabled(false);
        } else {
            setMaxValueCheckBox.setSelected(true);
            maxValueSpinner.setEnabled(true);
            maxValueSpinner.setValue(new Double(e.getMaxValue()));
        }

        if (e.getMinValue() == -Double.MAX_VALUE) {
            setMinValueCheckBox.setSelected(false);
            minValueSpinner.setValue(new Double(-Double.MAX_VALUE));
            minValueSpinner.setEnabled(false);

        } else {
            minValueSpinner.setEnabled(true);
            setMinValueCheckBox.setSelected(true);
            minValueSpinner.setValue(new Double(e.getMinValue()));
        }
        this.waterMarkDictPane.populate(e);
    }

    @Override
    protected NumberEditor updateSubFieldEditorBean() {

        NumberEditor ob = new NumberEditor();
        ob.setAllowDecimals(allowDecimalsCheckBox.isSelected());
        if (allowDecimalsCheckBox.isSelected()) {
            ob.setMaxDecimalLength((int)this.decimalLength.getValue());
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


        return ob;
    }


    private void initListeners(){      allowDecimalsListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (allowDecimalsCheckBox.isSelected()) {
                limitNumberPane.setVisible(true);
                limitNumberPane.setPreferredSize(new Dimension(215,20));
            } else {
                limitNumberPane.setVisible(false);
                limitNumberPane.setPreferredSize(new Dimension(0,0));
            }
        }
    };

        allowNegativeListener = new ActionListener() {

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


        setMaxListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (setMaxValueCheckBox.isSelected()) {
                    maxValueSpinner.setEnabled(true);
                    Double value = new Double(0);
                    if (setMinValueCheckBox.isSelected()) {
                        Double minValue = Double.parseDouble("" + minValueSpinner.getValue());
                        if (minValue > value) {
                            value = minValue;
                        }
                    }
                    maxValueSpinner.setValue(value);
                } else {
                    maxValueSpinner.setEnabled(false);
                    minValueModel.setMaximum(Double.MAX_VALUE);
                }
            }
        };


        setMinListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (setMinValueCheckBox.isSelected()) {
                    minValueSpinner.setEnabled(true);
                    Double value = new Double(0);
                    if (setMaxValueCheckBox.isSelected()) {
                        Double maxValue = Double.parseDouble("" + maxValueSpinner.getValue());
                        if (maxValue < value) {
                            value = maxValue;
                        }
                    }
                    minValueSpinner.setValue(value);
                } else {
                    minValueSpinner.setEnabled(false);
                    maxValueModel.setMinimum(allowNegativeCheckBox.isSelected() ? (-Double.MAX_VALUE) : new Double(0));
                }
            }
        };

        maxValueChangeListener = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (setMaxValueCheckBox.isSelected()) {
                    if (setMinValueCheckBox.isSelected()) {
                        minValueModel.setMaximum(Double.parseDouble("" + maxValueSpinner.getValue()));
                    }
                }
            }
        };

        minValueChangeListener = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (setMinValueCheckBox.isSelected()) {
                    if (setMaxValueCheckBox.isSelected()) {
                        maxValueModel.setMinimum(Double.parseDouble("" + minValueSpinner.getValue()));
                    }
                }
            }
        };
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