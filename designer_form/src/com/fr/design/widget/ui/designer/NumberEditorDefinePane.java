package com.fr.design.widget.ui.designer;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
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
    private UISpinner decimalLength;
    private JPanel limitNumberPane;
    private WaterMarkDictPane waterMarkDictPane;

    private ActionListener allowDecimalsListener;

    private ActionListener allowNegativeListener ;

    public ActionListener setMaxListener;

    private ActionListener setMinListener;

    private ChangeListener maxValueChangeListener;

    private ChangeListener minValueChangeListener;



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
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Label_Name")), labelNameTextField},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer-Estate_Widget_Value")),  formWidgetValuePane},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_WaterMark")), waterMarkDictPane},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Font-Size")), fontSizePane}
        };
        double[] rowSize = {p, p, p, p, p, p};
        double[] columnSize = {p,f};
        int[][] rowCount = {{1, 1}, {1, 3},{1, 1},{1, 1}};
        JPanel advancePane =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 10, 7);
        JPanel boundsPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        advancePane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        boundsPane.add(advancePane);
        return boundsPane;
    }

    private void initListeners(){
        allowDecimalsListener = new ActionListener() {
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

    public JPanel setValidatePane() {
        initListeners();
        this.allowDecimalsCheckBox = new UICheckBox(Inter.getLocText("Allow_Decimals"));
        this.decimalLength = new UISpinner(0, Integer.MAX_VALUE, 1, 16);
        allowDecimalsCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.allowDecimalsCheckBox.addActionListener(allowDecimalsListener);

        this.allowNegativeCheckBox = new UICheckBox(Inter.getLocText("Allow_Negative"));
        this.allowNegativeCheckBox.addActionListener(allowNegativeListener);
        allowNegativeCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        this.setMaxValueCheckBox = new UICheckBox(Inter.getLocText("Need_Max_Value"), false);
        setMaxValueCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        this.maxValueSpinner = new UIBasicSpinner(maxValueModel = new SpinnerNumberModel(0D, -Double.MAX_VALUE, Double.MAX_VALUE, 1D));

        setNotAllowsInvalid(this.maxValueSpinner);

        this.setMaxValueCheckBox.addActionListener(setMaxListener);
        this.maxValueSpinner.addChangeListener(maxValueChangeListener);

        this.setMinValueCheckBox = new UICheckBox(Inter.getLocText("Need_Min_Value"), false);
        this.minValueSpinner = new UIBasicSpinner(minValueModel = new SpinnerNumberModel(0D, -Double.MAX_VALUE, Double.MAX_VALUE, 1D));
        setNotAllowsInvalid(this.minValueSpinner);
        minValueSpinner.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.setMinValueCheckBox.addActionListener(setMinListener);
        this.minValueSpinner.addChangeListener(minValueChangeListener);
        setMinValueCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        UILabel numberLabel = new UILabel(Inter.getLocText(new String[]{"FR-Designer_Double", "Numbers"}));
        limitNumberPane = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{numberLabel, decimalLength}}, TableLayoutHelper.FILL_LASTCOLUMN, 18, 7);


        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{allowDecimalsCheckBox, null },
                new Component[]{limitNumberPane, null},
                new Component[]{allowNegativeCheckBox, null},
                new Component[]{setMaxValueCheckBox, maxValueSpinner},
                new Component[]{setMinValueCheckBox, minValueSpinner},
        };
        double[] rowSize = {p, p, p, p, p};
        double[] columnSize = {p,f};
        int[][] rowCount = {{1, 1},{1, 1},{1, 1},{1, 1},{1, 1}};
        JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 10, 7);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
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
        this.waterMarkDictPane.populate(e);
    }

    @Override
    protected NumberEditor updateSubFieldEditorBean() {

        NumberEditor ob = (NumberEditor)creator.toData();
        formWidgetValuePane.update(ob);
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

    public Object getValue(UIBasicSpinner jspinner){
        JComponent editor = jspinner.getEditor();
        if (editor instanceof UIBasicSpinner.DefaultEditor) {
            JFormattedTextField ftf = ((UIBasicSpinner.DefaultEditor) editor).getTextField();
            ftf.setColumns(10);
            JFormattedTextField.AbstractFormatter formatter = ftf.getFormatter();
           return ftf.getValue();
        }
        return null;
    }

}