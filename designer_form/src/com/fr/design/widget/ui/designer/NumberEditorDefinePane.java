package com.fr.design.widget.ui.designer;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.ui.designer.component.FormWidgetValuePane;
import com.fr.form.ui.NumberEditor;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NumberEditorDefinePane extends FieldEditorDefinePane<NumberEditor> {
    public NumberEditorDefinePane(XCreator xCreator){
        super(xCreator);
    }
    private FormWidgetValuePane formWidgetValuePane;
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


    private ActionListener actionListener4 = new ActionListener() {
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
        super();
    }


    @Override
    public String title4PopupWindow() {
        return "number";
    }

    @Override
    protected JPanel setFirstContentPane() {
        // richer:数字的允许直接编辑没有意义
        waterMarkDictPane = new WaterMarkDictPane();
        formWidgetValuePane = new FormWidgetValuePane(creator.toData(), false);
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer-Estate_Widget_Value")),  formWidgetValuePane},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_WaterMark")), waterMarkDictPane},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Font-Size")), fontSizePane}
        };
        double[] rowSize = {p, p, p, p, p};
        double[] columnSize = {p,f};
        int[][] rowCount = {{1, 3},{1, 1},{1, 1}};
        JPanel advancePane =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 10, 7);
        advancePane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        return advancePane;
    }


    public JPanel setValidatePane() {
//        super.addValidatePane();

        this.allowDecimalsCheckBox = new UICheckBox(Inter.getLocText("Allow_Decimals"));
        this.decimalLength = new com.fr.design.editor.editor.IntegerEditor();
        this.decimalLength.setColumns(4);

        this.allowDecimalsCheckBox.addActionListener(actionListener1);

        this.allowNegativeCheckBox = new UICheckBox(Inter.getLocText("Allow_Negative"));
        this.allowNegativeCheckBox.addActionListener(actionListener2);

        this.setMaxValueCheckBox = new UICheckBox(Inter.getLocText("Need_Max_Value"), false);

        this.maxValueSpinner = new UIBasicSpinner(maxValueModel = new SpinnerNumberModel(0D, -Double.MAX_VALUE, Double.MAX_VALUE, 1D));
        maxValueSpinner.setPreferredSize(new Dimension(120, 20));
        setNotAllowsInvalid(this.maxValueSpinner);

        this.setMaxValueCheckBox.addActionListener(actionListener3);
        this.maxValueSpinner.addChangeListener(changeListener1);

        this.setMinValueCheckBox = new UICheckBox(Inter.getLocText("Need_Min_Value"), false);
        this.minValueSpinner = new UIBasicSpinner(minValueModel = new SpinnerNumberModel(0D, -Double.MAX_VALUE, Double.MAX_VALUE, 1D));
        minValueSpinner.setPreferredSize(new Dimension(120, 20));
        setNotAllowsInvalid(this.minValueSpinner);

        this.setMinValueCheckBox.addActionListener(actionListener4);
        this.minValueSpinner.addChangeListener(changeListener2);


        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{allowDecimalsCheckBox, null },
                new Component[]{new UILabel(Inter.getLocText(new String[]{"Double", "Numbers"})), decimalLength },
                new Component[]{allowNegativeCheckBox, null},
                new Component[]{setMaxValueCheckBox, maxValueSpinner},
                new Component[]{setMinValueCheckBox, minValueSpinner},
        };
        double[] rowSize = {p, p, p, p, p};
        double[] columnSize = {p,f};
        int[][] rowCount = {{1, 1},{1, 1},{1, 1},{1, 1},{1, 1}};
        JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 10, 3);
        return panel;

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
            setMinValueCheckBox.setSelected(true);
            minValueSpinner.setEnabled(true);
            minValueSpinner.setValue(new Double(e.getMinValue()));
        }
        formWidgetValuePane.populate(e);
//        this.regErrorMsgTextField.setText(e.getRegErrorMessage());
        this.waterMarkDictPane.populate(e);
    }

    @Override
    protected NumberEditor updateSubFieldEditorBean() {

        NumberEditor ob = new NumberEditor();
        formWidgetValuePane.update(ob);
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


        return ob;
    }

    private void checkVisible() {
        if (setMinValueCheckBox.isSelected()) {
            minValueSpinner.setEnabled(true);
        } else {
            minValueSpinner.setEnabled(false);
        }

        if (setMinValueCheckBox.isSelected()) {
            minValueSpinner.setEnabled(true);
        } else {
            minValueSpinner.setEnabled(false);
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