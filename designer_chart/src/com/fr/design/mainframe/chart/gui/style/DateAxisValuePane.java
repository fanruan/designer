package com.fr.design.mainframe.chart.gui.style;

import com.fr.base.BaseFormula;
import com.fr.base.Utils;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.CategoryAxis;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.editor.DateEditor;
import com.fr.design.editor.editor.Editor;
import com.fr.design.editor.editor.FormulaEditor;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.date.UIDatePicker;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.FormatBox;
import com.fr.general.DateUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 属性表, 坐标轴,  日期范围定义界面
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-1-4 上午10:25:15
 */
public class DateAxisValuePane extends FurtherBasicBeanPane<CategoryAxis>{

    private static String[] TYPES = new String[]{
    	Inter.getLocText("Year"), Inter.getLocText("Month"), Inter.getLocText("Sun"), 
    	Inter.getLocText("Sche-Hour"), Inter.getLocText("Sche-Minute"),
    	Inter.getLocText("Sche-Second")
    };
    
    private static Map<String, Integer> VALUES = new HashMap<String, Integer>();
    static {
    	VALUES.put(Inter.getLocText("Year"), ChartConstants.YEAR_TYPE);
    	VALUES.put(Inter.getLocText("Month"), ChartConstants.MONTH_TYPE);
    	VALUES.put(Inter.getLocText("Sun"), ChartConstants.DAY_TYPE);
    	VALUES.put(Inter.getLocText("Sche-Hour"), ChartConstants.HOUR_TYPE);
    	VALUES.put(Inter.getLocText("Sche-Minute"), ChartConstants.MINUTE_TYPE);
    	VALUES.put(Inter.getLocText("Sche-Second"), ChartConstants.SECOND_TYPE);
    }
    
    private static Map<Integer, String> INTS = new HashMap<Integer, String>();
    static {
    	INTS.put(ChartConstants.YEAR_TYPE, Inter.getLocText("Year"));
    	INTS.put(ChartConstants.MONTH_TYPE, Inter.getLocText("Month"));
    	INTS.put(ChartConstants.DAY_TYPE, Inter.getLocText("Sun"));
    	INTS.put(ChartConstants.HOUR_TYPE, Inter.getLocText("Sche-Hour"));
    	INTS.put(ChartConstants.MINUTE_TYPE, Inter.getLocText("Sche-Minute"));
    	INTS.put(ChartConstants.SECOND_TYPE, Inter.getLocText("Sche-Second"));
    }
   
    // 最大最小值
    private UICheckBox maxCheckBox;
    private ValueEditorPane maxValueField;
    private UICheckBox minCheckBox;
    private ValueEditorPane minValueField;

    private UICheckBox mainTickBox;
    private UITextField mainUnitField;
    private UIComboBox mainType;

    private FormatBox formatBox;
    public FormatBox getFormatBox() {
    	return this.formatBox;
    }

    public DateAxisValuePane() {
        this.initComponents();
    }
    
    private void initMin() {
    	// 最小值.
        minCheckBox = new UICheckBox(Inter.getLocText("Min_Value"));
        Date tmp = null;
        DateEditor dateEditor = new DateEditor(tmp, true, Inter.getLocText("Date"), UIDatePicker.STYLE_CN_DATETIME1);
        Editor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        Editor[] editor = new Editor[]{dateEditor, formulaEditor};
        minValueField = new ValueEditorPane(editor);
        minValueField.setEnabled(false);
        minCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	checkBoxUse();
            }
        });
    }
    
    private void initMax() {
    	 // 最大值
        maxCheckBox = new UICheckBox(Inter.getLocText("Max_Value"));
        Date tmp = null;
        DateEditor dateEditor = new DateEditor(tmp, true, Inter.getLocText("Date"), UIDatePicker.STYLE_CN_DATETIME1);
        Editor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        Editor[] editor = new Editor[]{dateEditor, formulaEditor};
        maxValueField = new ValueEditorPane(editor);
        maxValueField.setEnabled(false);
        maxCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	checkBoxUse();
            }
        });
    }
    
    private void initMain() {
    	// 主要刻度单位
        mainTickBox = new UICheckBox(Inter.getLocText("MainGraduationUnit"));
        mainUnitField = new UITextField();
        mainUnitField.setPreferredSize(new Dimension(30, 20));
        mainUnitField.setEditable(false);
        mainType = new UIComboBox(TYPES);
        mainType.setEnabled(false);

        mainTickBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	checkBoxUse();
            }
        });

        addListener(mainTickBox, mainUnitField);
    }
    
    private void initComponents() {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        formatBox = new FormatBox();
        JPanel pane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
		add(pane, BorderLayout.NORTH);

        initMin();
        initMax();
        initMain();

        JPanel mainTickPane = new JPanel();
        mainTickPane.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 0));
        
        mainTickPane.add(mainUnitField);
        mainTickPane.add(mainType);
        
        JPanel secTickPane = new JPanel();
        secTickPane.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 0));
        
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] rowSize = {p, p, p};
        double[] columnSize = {p, f};
        Component[][] maxMin = {
                {minCheckBox, minValueField},
                {maxCheckBox, maxValueField},

        };
        JPanel maxMinPane = TableLayoutHelper.createTableLayoutPane(maxMin, rowSize, columnSize);
        Component[][] components = {
                {maxMinPane, null},
                {mainTickBox, mainTickPane}

        };
        pane.add(TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize));
    }

    private void addListener(final UICheckBox box, final UITextField textField) {
        textField.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (box.isSelected()) {
                    showFormulaPane(textField);
                }
            }
        });
        textField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (box.isSelected()) {
                    e.consume();
                    showFormulaPane(textField);
                }
            }
        });
    }

    private void showFormulaPane(final UITextField jTextField) {
        final UIFormula formulaPane = FormulaFactory.createFormulaPane();
        formulaPane.populate(BaseFormula.createFormulaBuilder().build(jTextField.getText()));
        BasicDialog dlg = formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor(DateAxisValuePane.this), new DialogActionAdapter() {
            public void doOk() {
                jTextField.setText(Utils.objectToString(formulaPane.update()));
            }
        });
        dlg.setVisible(true);
    }

    private void populateMain(CategoryAxis axis) {
        if (axis.isCustomMainUnit() && axis.getMainUnit() != null) {
            mainTickBox.setSelected(true);
            mainUnitField.setText(Utils.objectToString(axis.getMainUnit()));
            mainType.setSelectedItem(INTS.get(axis.getMainType()));
        }
    }

    private void updateMain(CategoryAxis axis) {
        if (mainTickBox.isSelected() && StringUtils.isNotEmpty(mainUnitField.getText())) {
            axis.setCustomMainUnit(true);
            axis.setMainUnit(BaseFormula.createFormulaBuilder().build(mainUnitField.getText()));
            axis.setMainType(VALUES.get(mainType.getSelectedItem()));
        } else {
            axis.setCustomMainUnit(false);
        }
    }

	@Override
	public boolean accept(Object ob) {
		return ob instanceof CategoryAxis;
	}

	@Override
	public void reset() {
		
	}

	@Override
	public String title4PopupWindow() {
		return Inter.getLocText("Chart_Date_Axis");
	}
	
	private void checkBoxUse() {
		minValueField.setEnabled(minCheckBox.isSelected());
		maxValueField.setEnabled(maxCheckBox.isSelected());
		if(minValueField.getCurrentEditor() instanceof FormulaEditor){
			FormulaEditor tmpEditor = (FormulaEditor)minValueField.getCurrentEditor();
			tmpEditor.enableEditor(minCheckBox.isSelected());
		}
		
		if(maxValueField.getCurrentEditor() instanceof FormulaEditor){
			FormulaEditor tmpEditor = (FormulaEditor)maxValueField.getCurrentEditor();
			tmpEditor.enableEditor(maxCheckBox.isSelected());
		}
		
		mainType.setEnabled(mainTickBox.isSelected());
		mainUnitField.setEnabled(mainTickBox.isSelected());
	}

	@Override
	public void populateBean(CategoryAxis axis) {
        if (axis == null) {
            return;
        }
        if (!axis.isDate()) {
            return;
        }
        // 最小值
        if (axis.isCustomMinValue() && axis.getMinValue() != null) {
            minCheckBox.setSelected(true);
            String dateStr = axis.getMinValue().getPureContent();
            if(!isDateForm(dateStr)){
            	minValueField.populate(axis.getMinValue());
            }else{
            	Date tmpDate = getDateFromFormula(axis.getMinValue());
            	minValueField.populate(tmpDate);
            }
            
        }
            
        
        // 最大值
        if (axis.isCustomMaxValue() && axis.getMaxValue() != null) {
            maxCheckBox.setSelected(true);
            String dateStr = axis.getMaxValue().getPureContent();
            if(!isDateForm(dateStr)){
            	maxValueField.populate(axis.getMaxValue());
            }else{
            	Date tmpDate = getDateFromFormula(axis.getMaxValue());
            	maxValueField.populate(tmpDate);
            }
        }
        populateMain(axis);
        
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
	
	public void updateBean(CategoryAxis axis) {
        updateMain(axis);
        //最小值
        if (minCheckBox.isSelected()) {
        	if(minValueField.getCurrentEditor() instanceof FormulaEditor){
        		BaseFormula min = (BaseFormula) minValueField.update();
        		axis.setMinValue(min);
        		axis.setCustomMinValue(!StringUtils.isEmpty(min.getPureContent()));
        	}else{
        		Date datetmp = (Date)minValueField.update();
        		DateEditor dateEditor = (DateEditor)minValueField.getCurrentEditor();
        		String dateString = dateEditor.getUIDatePickerFormat().format(datetmp);
        		axis.setCustomMinValue(!StringUtils.isEmpty(dateString));
        		axis.setMinValue(BaseFormula.createFormulaBuilder().build(dateString));
        	}
        } else {
            axis.setCustomMinValue(false);
        }
       
        //最大值
        if (maxCheckBox.isSelected()) {
        	if(maxValueField.getCurrentEditor() instanceof FormulaEditor){
        		BaseFormula max = (BaseFormula) maxValueField.update();
        		axis.setMaxValue(max);
        		axis.setCustomMaxValue(!StringUtils.isEmpty(max.getPureContent()));
        	}else{
        		Date datetmp = (Date)maxValueField.update();
        		DateEditor dateEditor = (DateEditor)maxValueField.getCurrentEditor();
        		String dateString = dateEditor.getUIDatePickerFormat().format(datetmp);
        		axis.setCustomMaxValue(!StringUtils.isEmpty(dateString));
        		axis.setMaxValue(BaseFormula.createFormulaBuilder().build(dateString));
        	}
        } else {
            axis.setCustomMaxValue(false);
        }
        
        this.checkBoxUse();
	}

	@Override
	public CategoryAxis updateBean() {
		return null;
	}
	
	//将从formula读出来的内容转化为指定格式的日期
	private static final Date getDateFromFormula(BaseFormula dateFormula){
		String dateStr = dateFormula.getPureContent();
		dateStr = Pattern.compile("\"").matcher(dateStr).replaceAll(StringUtils.EMPTY);
		Date toDate = DateUtils.string2Date(dateStr, true);
		try {
			String tmp = DateUtils.getDate2LStr(toDate);
			toDate = DateUtils.DATETIMEFORMAT2.parse(tmp);
		} catch (ParseException e) {
			FRLogger.getLogger().error(Inter.getLocText("Cannot_Get_Date"));
		}
		return toDate;
	}
}