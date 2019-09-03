package com.fr.van.chart.designer.style.axis;

import com.fr.base.BaseFormula;
import com.fr.base.Utils;
import com.fr.design.chart.ChartSwingUtils;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.editor.DateEditor;
import com.fr.design.editor.editor.Editor;
import com.fr.design.editor.editor.FormulaEditor;
import com.fr.design.gui.date.UIDatePicker;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.style.FormatPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.DateUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.axis.VanChartTimeAxis;
import com.fr.plugin.chart.base.VanChartConstants;
import com.fr.plugin.chart.type.TimeType;
import com.fr.stable.StringUtils;
import com.fr.van.chart.designer.PlotFactory;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 时间坐标轴
 */
public class VanChartTimeAxisPane extends VanChartBaseAxisPane {
    private JPanel minPane;
    private JPanel maxPane;
    private JPanel mainPane;
    private JPanel secPane;

    private static final long serialVersionUID = 1371126030195384450L;
    private static final String[] TYPES = new String[]{
            TimeType.TIME_YEAR.getLocText(), TimeType.TIME_MONTH.getLocText(), TimeType.TIME_DAY.getLocText(),
            TimeType.TIME_HOUR.getLocText(), TimeType.TIME_MINUTE.getLocText(), TimeType.TIME_SECOND.getLocText()
    };
    private TimeMinMaxValuePane timeMinMaxValuePane;

    public VanChartTimeAxisPane(boolean isXAxis){
        super(isXAxis);
    }

    protected JPanel createContentPane(boolean isXAxis){

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double s = TableLayout4VanChartHelper.SECOND_EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        double[] column = {f, s};
        double[] rowSize = {p,p,p,p,p,p,p,p,p,p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{createTitlePane(new double[]{p, p, p, p, p,p}, columnSize, isXAxis),null},
                new Component[]{createLabelPane(new double[]{p, p}, column),null},
                new Component[]{createValueDefinition(),null},
                new Component[]{createLineStylePane(new double[]{p, p,p,p,p}, columnSize),null},
                new Component[]{createAxisPositionPane(new double[]{p, p, p}, columnSize, isXAxis),null},
                new Component[]{createDisplayStrategy(new double[]{p, p,p}, columnSize),null},
                new Component[]{createValueStylePane(),null},
        };

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    @Override
    protected void addOverlapGroupButton(JPanel panel) {
    }

    private JPanel createValueDefinition(){
        timeMinMaxValuePane = new TimeMinMaxValuePane();
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Value_Definition"), timeMinMaxValuePane);
    }

    @Override
    protected FormatPane createFormatPane(){
        return PlotFactory.createAutoFormatPane();
    }

    protected void checkFormatType() {
        valueFormat.setComboBoxModel(true);
    }

    @Override
    public void updateBean(VanChartAxis axis) {
        VanChartTimeAxis timeAxis = (VanChartTimeAxis)axis;
        super.updateBean(timeAxis);
        timeMinMaxValuePane.update(timeAxis);
    }

    public VanChartTimeAxis updateBean(String axisName, int position) {
        VanChartTimeAxis axis = new VanChartTimeAxis(axisName, VanChartConstants.AXIS_BOTTOM);
        updateBean(axis);
        return axis;
    }

    @Override
    public void populateBean(VanChartAxis axis) {
        VanChartTimeAxis timeAxis = (VanChartTimeAxis)axis;
        super.populateBean(timeAxis);
        timeMinMaxValuePane.populate(timeAxis);
    }

    /**
     * title应该是一个属性，不只是对话框的标题时用到，与其他组件结合时，也会用得到
     * @return 绥化狂标题
     */
    @Override
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_TimeAxis");
    }

    private class TimeMinMaxValuePane extends JPanel{

        private static final long serialVersionUID = 5910309251773119715L;
        private UICheckBox maxCheckBox;
        private ValueEditorPane maxValueField;
        private UICheckBox minCheckBox;
        private ValueEditorPane minValueField;

        private UICheckBox mainTickBox;
        private UITextField mainUnitField;
        private UIComboBox mainType;

        private UICheckBox secondTickBox;
        private UITextField secondUnitField;
        private UIComboBox secondType;

        public TimeMinMaxValuePane(){
            setLayout(FRGUIPaneFactory.createBorderLayout());

            initMin();
            initMax();
            initMain();
            initSecond();

            double p = TableLayout.PREFERRED;
            double f = TableLayout.FILL;
            double[] rowSize = {p, p, p, p};
            double[] columnSize = {f};


            JPanel mainTickPane = new JPanel();
            mainTickPane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            mainUnitField.setPreferredSize(new Dimension(100,20));
            secondUnitField.setPreferredSize(new Dimension(100,20));
            mainTickPane.add(mainUnitField);
            mainTickPane.add(mainType);

            JPanel secTickPane = new JPanel();
            secTickPane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            secTickPane.add(secondUnitField);
            secTickPane.add(secondType);

            minPane = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Data_Min"),minValueField, TableLayout4VanChartHelper.SECOND_EDIT_AREA_WIDTH);
            maxPane = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Data_Max"),maxValueField, TableLayout4VanChartHelper.SECOND_EDIT_AREA_WIDTH);
            mainPane = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Main_Type"),mainTickPane, TableLayout4VanChartHelper.SECOND_EDIT_AREA_WIDTH);
            secPane = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_SecType"),secTickPane, TableLayout4VanChartHelper.SECOND_EDIT_AREA_WIDTH);

            minPane.setBorder(BorderFactory.createEmptyBorder(0,TableLayout4VanChartHelper.COMPONENT_INTERVAL,0,0));
            maxPane.setBorder(BorderFactory.createEmptyBorder(0,TableLayout4VanChartHelper.COMPONENT_INTERVAL,0,0));
            mainPane.setBorder(BorderFactory.createEmptyBorder(0,TableLayout4VanChartHelper.COMPONENT_INTERVAL,0,0));
            secPane.setBorder(BorderFactory.createEmptyBorder(0,TableLayout4VanChartHelper.COMPONENT_INTERVAL,0,0));

            JPanel minPaneWithCheckBox = new JPanel(new BorderLayout());
            JPanel maxPaneWithCheckBox = new JPanel(new BorderLayout());
            JPanel mainPaneWithCheckBox = new JPanel(new BorderLayout());
            JPanel secPaneWithCheckBox = new JPanel(new BorderLayout());

            minPaneWithCheckBox.add(minCheckBox, BorderLayout.NORTH);
            minPaneWithCheckBox.add(minPane, BorderLayout.CENTER);
            maxPaneWithCheckBox.add(maxCheckBox, BorderLayout.NORTH);
            maxPaneWithCheckBox.add(maxPane, BorderLayout.CENTER);
            mainPaneWithCheckBox.add(mainTickBox, BorderLayout.NORTH);
            mainPaneWithCheckBox.add(mainPane, BorderLayout.CENTER);
            secPaneWithCheckBox.add(secondTickBox, BorderLayout.NORTH);
            secPaneWithCheckBox.add(secPane, BorderLayout.CENTER);
            Component[][] components = {
                    {minPaneWithCheckBox},
                    {maxPaneWithCheckBox},
                    {mainPaneWithCheckBox},
                    {secPaneWithCheckBox},
            };
            this.add(TableLayout4VanChartHelper.createGapTableLayoutPane(components, rowSize, columnSize));
        }

        private void initMin() {
            // 最小值.
            minCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Custom_Min_Value"));
            Date tmp = null;
            DateEditor dateEditor = new DateEditor(tmp, true, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Date"), UIDatePicker.STYLE_CN_DATETIME1);
            Editor formulaEditor = new FormulaEditor(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Formula"));
            Editor[] editor = new Editor[]{dateEditor, formulaEditor};
            minValueField = new ValueEditorPane(editor);
            minCheckBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    checkBoxUse();
                }
            });
        }

        private void initMax() {
            // 最大值
            maxCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Custom_Max_Value"));
            Date tmp = null;
            DateEditor dateEditor = new DateEditor(tmp, true, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Date"), UIDatePicker.STYLE_CN_DATETIME1);
            Editor formulaEditor = new FormulaEditor(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Formula"));
            Editor[] editor = new Editor[]{dateEditor, formulaEditor};
            maxValueField = new ValueEditorPane(editor);
            maxCheckBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    checkBoxUse();
                }
            });
        }

        private void initMain() {
            // 主要刻度单位
            mainTickBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Custom_Main_Type"));
            mainUnitField = new UITextField();
            mainUnitField.setPreferredSize(new Dimension(20, 20));
            mainType = new UIComboBox(TYPES);

            mainTickBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    checkBoxUse();
                }
            });

            ChartSwingUtils.addListener(mainTickBox, mainUnitField);
        }

        private void initSecond() {
            // 次要刻度单位
            secondTickBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Custom_Second_Type"));
            secondUnitField = new UITextField();
            secondUnitField.setPreferredSize(new Dimension(20, 20));
            secondType = new UIComboBox(TYPES);

            secondTickBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    checkBoxUse();
                }
            });

            ChartSwingUtils.addListener(secondTickBox, secondUnitField);
        }


        private void checkBoxUse() {

            minPane.setVisible(minCheckBox.isSelected());
            maxPane.setVisible(maxCheckBox.isSelected());
            mainPane.setVisible(mainTickBox.isSelected());
            secPane.setVisible(secondTickBox.isSelected());
        }

        public void update(VanChartTimeAxis timeAxis){
            if (minCheckBox.isSelected()) {//最小值
                if(minValueField.getCurrentEditor() instanceof FormulaEditor){
                    BaseFormula min = (BaseFormula) minValueField.update();
                    timeAxis.setMinValue(min);
                    timeAxis.setCustomMinValue(StringUtils.isNotEmpty(min.getPureContent()));
                }else{
                    Date datetmp = (Date)minValueField.update();
                    DateEditor dateEditor = (DateEditor)minValueField.getCurrentEditor();
                    String dateString = dateEditor.getUIDatePickerFormat().format(datetmp);
                    timeAxis.setCustomMinValue(StringUtils.isNotEmpty(dateString));
                    timeAxis.setMinValue(BaseFormula.createFormulaBuilder().build(dateString));
                }
            } else {
                timeAxis.setCustomMinValue(false);
            }
            if (maxCheckBox.isSelected()) {//最大值
                if(maxValueField.getCurrentEditor() instanceof FormulaEditor){
                    BaseFormula max = (BaseFormula) maxValueField.update();
                    timeAxis.setMaxValue(max);
                    timeAxis.setCustomMaxValue(StringUtils.isNotEmpty(max.getPureContent()));
                }else{
                    Date datetmp = (Date)maxValueField.update();
                    DateEditor dateEditor = (DateEditor)maxValueField.getCurrentEditor();
                    String dateString = dateEditor.getUIDatePickerFormat().format(datetmp);
                    timeAxis.setCustomMaxValue(StringUtils.isNotEmpty(dateString));
                    timeAxis.setMaxValue(BaseFormula.createFormulaBuilder().build(dateString));
                }
            } else {
                timeAxis.setCustomMaxValue(false);
            }
            if (mainTickBox.isSelected() && StringUtils.isNotEmpty(mainUnitField.getText())) {//主要刻度单位
                timeAxis.setCustomMainUnit(true);
                timeAxis.setMainUnit(BaseFormula.createFormulaBuilder().build(mainUnitField.getText()));
                String item = mainType.getSelectedItem().toString();
                timeAxis.setMainType(TimeType.parseString(item));
            } else {
                timeAxis.setCustomMainUnit(false);
            }
            if (secondTickBox.isSelected() && StringUtils.isNotEmpty(secondUnitField.getText())) { //次要刻度单位
                timeAxis.setCustomSecUnit(true);
                timeAxis.setSecUnit(BaseFormula.createFormulaBuilder().build(secondUnitField.getText()));
                String item = secondType.getSelectedItem().toString();
                timeAxis.setSecondType(TimeType.parseString(item));
            } else {
                timeAxis.setCustomSecUnit(false);
            }
            checkBoxUse();
        }

        public void populate(VanChartTimeAxis timeAxis){
            // 最小值
            if (timeAxis.isCustomMinValue() && timeAxis.getMinValue() != null) {
                minCheckBox.setSelected(true);
                String dateStr = timeAxis.getMinValue().getPureContent();
                if(!isDateForm(dateStr)){
                    minValueField.populate(timeAxis.getMinValue());
                }else{
                    Date tmpDate = getDateFromFormula(timeAxis.getMinValue());
                    minValueField.populate(tmpDate);
                }

            }

            // 最大值
            if (timeAxis.isCustomMaxValue() && timeAxis.getMaxValue() != null) {
                maxCheckBox.setSelected(true);
                String dateStr = timeAxis.getMaxValue().getPureContent();
                if(!isDateForm(dateStr)){
                    maxValueField.populate(timeAxis.getMaxValue());
                }else{
                    Date tmpDate = getDateFromFormula(timeAxis.getMaxValue());
                    maxValueField.populate(tmpDate);
                }
            }

            //主要刻度单位
            if (timeAxis.isCustomMainUnit() && timeAxis.getMainUnit() != null) {
                mainTickBox.setSelected(true);
                mainUnitField.setText(Utils.objectToString(timeAxis.getMainUnit()));
                mainType.setSelectedItem(timeAxis.getMainType().getLocText());
            }

            //次要刻度单位
            if (timeAxis.isCustomSecUnit() && timeAxis.getSecUnit() != null) {
                secondTickBox.setSelected(true);
                secondUnitField.setText(Utils.objectToString(timeAxis.getSecUnit()));
                secondType.setSelectedItem(timeAxis.getSecondType().getLocText());
            }

            checkBoxUse();
        }

        private boolean isDateForm(String form){
            form = Pattern.compile("\"").matcher(form).replaceAll(StringUtils.EMPTY);
            //全部是数字的话直接返回，string2Date会把全部是数字也会转化成日期
            if(form.matches("^[+-]?[0-9]*[0-9]$")){
                return false;
            }
            return (DateUtils.string2Date(form, true) != null);
        }

        //将从formula读出来的内容转化为指定格式的日期
        private  Date getDateFromFormula(BaseFormula dateFormula){
            String dateStr = dateFormula.getPureContent();
            dateStr = Pattern.compile("\"").matcher(dateStr).replaceAll(StringUtils.EMPTY);
            Date toDate = DateUtils.string2Date(dateStr, true);
            try {
                String tmp = DateUtils.getDate2LStr(toDate);
                toDate = DateUtils.DATETIMEFORMAT2.parse(tmp);
            } catch (ParseException e) {
                FineLoggerFactory.getLogger().error("cannot get date");
            }
            return toDate;
        }

    }
}
