package com.fr.design.report;

import com.fr.base.BaseUtils;
import com.fr.base.GraphHelper;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.border.UITitledBorder;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRFont;
import com.fr.report.stable.WorkSheetAttr;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ColumnRow;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReportColumnsPane extends BasicPane{
    public static final int ROW = 0;
    public static final int COLUMN = 1;
    private int rowOrColumn;
    private boolean isRepeate;

    private static final String[] COLUMN_ROW_TEXTS = {
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Base_Rows"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Base_Columns")
    };

    private static final String[] SHOW_BLANK = {
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Show_Blank_Row"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Show_Blank_Column")
    };

    private static final String[] REPORT_COLUMN_RAPEAT = {
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Columns_Repeat_Row"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Columns_Repeat_Column")
    };
    
    private static final String FONT_NAME = "simsun";
    private static final int FONT_SIZE = 14;

    private UIButtonGroup onOffButtonGroup;

    private UIRadioButton rowButton;
    private UIRadioButton colButton;

    // 行分行
    private UIRadioButton maxRadioButton;
    private UIBasicSpinner maxNumberSpinner;
    private UILabel maxUILabel;
    private UIRadioButton toXRadioButton;
    private UIBasicSpinner toXSpinner;
    private UILabel toUILabel;

    private UITextField repeatColDataTextField;
    private UILabel copyLabel;
    private UITextField copyTitleTextField;

    private UICheckBox showBlankCheckBox ;


    public ReportColumnsPane() {
        this.rowOrColumn = ROW;
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        JPanel north = new JPanel(new BorderLayout()) {
            public void paint(Graphics g) {
                super.paint(g);
                super.paintBorder(g);
            }
        };
        north.setPreferredSize(new Dimension(549, 59));
        north.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(11, 23, 6, 23), new UIRoundedBorder(new Color(204, 204, 204), 1, 10)));
        String[] textArray = new String[] {
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Base_TurnOn"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Base_TurnOff")
        };

        onOffButtonGroup = new UIButtonGroup(textArray) {
            protected void initButton(UIToggleButton labelButton) {
                labelButton.setSize(new Dimension(60,20));
                labelButton.setPreferredSize(new Dimension(60, 20));
                super.initButton(labelButton);
            }
        };
        onOffButtonGroup.addActionListener(onOffListener);

        UILabel uiLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ReportColumns_Columns"));
        FRFont uiLableFont = FRFont.getInstance(FONT_NAME, Font.PLAIN, FONT_SIZE);
        uiLabel.setFont(uiLableFont);
        uiLabel.setHorizontalAlignment(SwingConstants.CENTER);
        int uiLabelWidth = GraphHelper.getWidth(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ReportColumns_Columns"), uiLableFont);

        uiLabel.setPreferredSize(new Dimension(uiLabelWidth, 20));
        north.add(uiLabel,BorderLayout.WEST);
        JPanel buttonGroupPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 23,11));
        buttonGroupPane.add(onOffButtonGroup);
        north.add(buttonGroupPane, BorderLayout.EAST);
        this.add(north, BorderLayout.NORTH);
        this.add(createRowColumnPane(), BorderLayout.CENTER);
    }
    
    private ActionListener onOffListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ReportColumnsPane.this.isRepeate = onOffButtonGroup.getSelectedIndex() == 0;
            checkEnable();
        }
    };

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ReportColumns_Report_Columns");
    }

    private void checkEnable() {
        if (isRepeate) {
            rowButton.setSelected(!colButton.isSelected());
            this.maxRadioButton.setSelected(!this.toXRadioButton.isSelected());
        }
        rowButton.setEnabled(isRepeate);
        colButton.setEnabled(isRepeate);
        maxNumberSpinner.setEnabled(isRepeate && maxRadioButton.isSelected());
        toXSpinner.setEnabled(isRepeate && toXRadioButton.isSelected());
        maxRadioButton.setEnabled(isRepeate);
        toXRadioButton.setEnabled(isRepeate);
        repeatColDataTextField.setEnabled(isRepeate);
        copyTitleTextField.setEnabled(isRepeate);
        showBlankCheckBox.setEnabled(isRepeate);
        setAllLableEnabled(this, isRepeate);
    }

    private void setAllLableEnabled (Container container, boolean flag) {
        for (int i = 0, len = container.getComponentCount(); i < len; i++){
            Component child = container.getComponent(i);
            if (child instanceof UILabel) {
                child.setEnabled(flag);
            } else if (child instanceof Container) {
                setAllLableEnabled((Container)child, flag);
            }
        }
    }


    private void colOrRowConvert() {
        maxUILabel.setText(COLUMN_ROW_TEXTS[rowOrColumn]);
        toUILabel.setText(COLUMN_ROW_TEXTS[1 - rowOrColumn]);
        showBlankCheckBox.setText(SHOW_BLANK[rowOrColumn]);
        copyLabel.setText(REPORT_COLUMN_RAPEAT[rowOrColumn] + ":");
    }

    /**
	 * 创建分栏页面
	 */
    private JPanel createRowColumnPane() {
        JPanel divideRowPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        divideRowPane.setBorder(BorderFactory.createEmptyBorder(0,21,0,21));
        JPanel center = FRGUIPaneFactory.createBorderLayout_S_Pane();
        // 行分栏样式
        JPanel north = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Column_Style"));
        north.setPreferredSize(new Dimension(549, 216));
        north.add(createSamplePane());
        north.add(createRowMaxOrSetPane());

        center.add(createRowPane(), BorderLayout.NORTH);
        JPanel xx = new JPanel(new FlowLayout(FlowLayout.RIGHT,0,0));
        showBlankCheckBox = new UICheckBox(SHOW_BLANK[rowOrColumn]);
        xx.add(showBlankCheckBox);
        center.add(xx, BorderLayout.CENTER);

        divideRowPane.add(north, BorderLayout.NORTH);
        divideRowPane.add(center, BorderLayout.CENTER);
        return divideRowPane;
    }

    /**
	 * 创建示例页面
	 */
    private JPanel createSamplePane() {
        JPanel sampleLablePane = new JPanel(new GridLayout(1,2));
        sampleLablePane.setPreferredSize(new Dimension(524, 130));
        JPanel rPane = new JPanel();
        UILabel rLabel = new UILabel(BaseUtils.readIcon("/com/fr/design/images/reportcolumns/" + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Row_Icon_File_Name")));
        rLabel.setBorder(BorderFactory.createEmptyBorder(5,45,0,49));
        rPane.add(rLabel);
        rowButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ReportColumns_Columns_Horizontally"));
        rowButton.addActionListener(rowChangeListener);
        rPane.add(rowButton);
        sampleLablePane.add(rPane);
        JPanel cPane = new JPanel();
        UILabel cLabel = new UILabel(BaseUtils.readIcon("/com/fr/design/images/reportcolumns/col.png"));
        cLabel.setBorder(BorderFactory.createEmptyBorder(5,49,0,49));
        cPane.add(cLabel);
        colButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ReportColumns_Columns_Vertically"));
        colButton.addActionListener(colChangeListener);
        cPane.add(colButton);
        sampleLablePane.add(cPane);
        return sampleLablePane;
    }
    
    private ActionListener rowChangeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!rowButton.isSelected()) {
                rowButton.setSelected(true);
            }else{
            colButton.setSelected(false);
            ReportColumnsPane.this.rowOrColumn = ROW;
            emptyValueConvert();
            }
        }
    };
    
    private ActionListener colChangeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!colButton.isSelected()) {
                colButton.setSelected(true);
            }else{
	            rowButton.setSelected(false);
	            ReportColumnsPane.this.rowOrColumn = COLUMN;
	            emptyValueConvert();
            }
        }
    };

    private void emptyValueConvert(){
        maxNumberSpinner.setValue(0);
        toXSpinner.setValue(0);
        colOrRowConvert();
    }

    private JPanel createRowMaxOrSetPane() {
        JPanel RowMaxOrSetPane = new JPanel();
        RowMaxOrSetPane.setBorder(BorderFactory.createEmptyBorder(8, 5, 0, 0));
        RowMaxOrSetPane.setLayout(new FlowLayout(FlowLayout.LEFT,25,2));
        //marks:在多少行后开始分栏
        maxRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ReportColumns_Columns_After"));
        maxNumberSpinner = new UIBasicSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        GUICoreUtils.setColumnForSpinner(maxNumberSpinner, 6);
        maxRadioButton.addActionListener(maxBtnListener);
        maxUILabel = new UILabel(COLUMN_ROW_TEXTS[rowOrColumn] );
        JPanel maxRowRadioPane = GUICoreUtils.createFlowPane(new JComponent[]{maxRadioButton, maxNumberSpinner, maxUILabel, new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Report_Columns_Columns_Optional"))}, FlowLayout.CENTER);
        RowMaxOrSetPane.add(maxRowRadioPane);
        //marks:分成多少行
        toXRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ReportColumns_Columns_To"));
        toXRadioButton.addActionListener(toXBtnListener);
        toXSpinner = new UIBasicSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        GUICoreUtils.setColumnForSpinner(toXSpinner, 6);
        toUILabel = new UILabel(COLUMN_ROW_TEXTS[1 - rowOrColumn]);
        JPanel setRowRadioPane = GUICoreUtils.createFlowPane(new JComponent[]{toXRadioButton, toXSpinner, toUILabel}, FlowLayout.CENTER);
        RowMaxOrSetPane.add(setRowRadioPane);
        return RowMaxOrSetPane;
    }
    
    private ActionListener toXBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (toXRadioButton.isSelected()) {
                toXSpinner.setEnabled(true);
                maxNumberSpinner.setEnabled(false);
                maxRadioButton.setSelected(false);
            } else {
                toXRadioButton.setSelected(true);
            }
        }
    };
    
    private ActionListener maxBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (maxRadioButton.isSelected()) {
                maxNumberSpinner.setEnabled(true);
                toXRadioButton.setSelected(false);
                toXSpinner.setEnabled(false);
            } else {
                maxRadioButton.setSelected(true);
            }
        }
    };

    /**
	 * 创建行分栏界面
	 */
    private JPanel createRowPane() {
        JPanel rowPane = new JPanel();
        UITitledBorder explainBorder = UITitledBorder.createBorderWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Column_Area"));
        rowPane.setBorder(explainBorder);
        rowPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5,13));
        rowPane.setPreferredSize(new Dimension(500,80));
        rowPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Column_Data") + ":"));
        repeatColDataTextField = new UITextField();
        repeatColDataTextField.setPreferredSize(new Dimension(107,24));
        rowPane.add(repeatColDataTextField);
        rowPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Base_Format") + ": A2:D5 "));
        copyLabel = new UILabel(REPORT_COLUMN_RAPEAT[rowOrColumn] + ":");
        rowPane.add(copyLabel);

        copyTitleTextField = new UITextField();
        copyTitleTextField.setPreferredSize(new Dimension(107,24));
        rowPane.add(copyTitleTextField);
        rowPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Base_Format") + ": 1,2-3,5,18"));


        return rowPane;
    }

    /**
	 * 从worksheetAttr中populate数据给界面
	 * 
	 * @param attr 当前ElementCase的worksheet属性
	 * @param rowCount 总行数
	 * @param colCount 总列数
	 */
    public void populate(WorkSheetAttr attr, int rowCount, int colCount){
        this.isRepeate = true;
        switch (attr.getDirection()) {
		case Constants.TOP_TO_BOTTOM:
	       	populateTopBottom(attr);
            setValueText(attr, rowCount, colCount, repeatColDataTextField, copyTitleTextField, showBlankCheckBox);
			break;
		case Constants.LEFT_TO_RIGHT:
        	populateLeftRight(attr);
            setValueText(attr, rowCount, colCount ,repeatColDataTextField, copyTitleTextField, showBlankCheckBox);
			break;
		default:
            this.onOffButtonGroup.setSelectedIndex(1);
            this.isRepeate = false;
			break;
		}

        colOrRowConvert();
        checkEnable();
    }
    
    /**
	 * populate列分栏数据
	 */
    private void populateLeftRight(WorkSheetAttr attr){
        this.rowOrColumn = COLUMN;
        this.onOffButtonGroup.setSelectedIndex(0);
        this.colButton.setSelected(true);
        this.rowButton.setSelected(false);
        this.maxNumberSpinner.setEnabled(false);
        this.toXSpinner.setEnabled(false);
        if (attr.getCount() > -1 && attr.getCount() < Integer.MAX_VALUE) {
            this.toXRadioButton.setSelected(true);
            this.toXSpinner.setValue(new Integer(attr.getCount()));
            this.toXSpinner.setEnabled(true);
        } else if (attr.getMaxCount() > -1 && attr.getMaxCount() < Integer.MAX_VALUE) {
            this.maxRadioButton.setSelected(true);
            this.maxNumberSpinner.setValue(new Integer(attr.getMaxCount()));
            this.maxNumberSpinner.setEnabled(true);
        }
    }
    
    /**
	 * populate行分栏
	 */
    private void populateTopBottom(WorkSheetAttr attr){
        this.onOffButtonGroup.setSelectedIndex(0);
        this.rowButton.setSelected(true);
        this.colButton.setSelected(false);
        this.rowOrColumn = ROW;
        this.maxNumberSpinner.setEnabled(false);
        this.toXSpinner.setEnabled(false);
        if (attr.getCount() > -1 && attr.getCount() < Integer.MAX_VALUE) {
            this.toXRadioButton.setSelected(true);
            this.toXSpinner.setValue(new Integer(attr.getCount()));
            this.toXSpinner.setEnabled(true);
        } else if (attr.getMaxCount() > -1 && attr.getMaxCount() < Integer.MAX_VALUE) {
            this.maxRadioButton.setSelected(true);
            this.maxNumberSpinner.setValue(new Integer(attr.getMaxCount()));
            this.maxNumberSpinner.setEnabled(true);
        }
    }

    /**
	 * 从worksheet中populate数据
	 * 
	 * @param worksheet 当前worksheet
	 */
    public void populate(WorkSheet worksheet) {
        if (worksheet == null) {
            return;
        }
        WorkSheetAttr attr = worksheet.getWorkSheetAttr();
        if (attr == null) {
            attr = new WorkSheetAttr();
        }
        int rowCount = worksheet.getRowCount();
        int colCount = worksheet.getColumnCount();
        populate(attr, rowCount, colCount);
    }

    /**
	 * 将attr中的数据set到面板上
	 */
    private void setValueText(WorkSheetAttr attr, int rowCount, int colCount, UITextField repeatColDataTextField,
    		UITextField copyTitleTextField, UICheckBox showBlankCheckBox) {
        int startRow,endRow = -1,startColumn,endColumn = -1;
        
        if(attr.getStartIndex() == -1 && attr.getEndIndex() == -1){
        	return;
        }
        
        if (attr.getDirection() == ROW) {
            startRow = attr.getStartIndex();
            endRow = attr.getEndIndex() == -1 ? rowCount - 1 : attr.getEndIndex();
            startColumn = attr.getOppoStartIndex() == -1 ? 0 : attr.getOppoStartIndex();
            endColumn = attr.getOppoEndIndex() == -1 ? colCount - 1: attr.getOppoEndIndex();
        } else {
            startRow = attr.getOppoStartIndex() == -1 ? 0: attr.getOppoStartIndex();
            endRow = attr.getOppoEndIndex() == -1 ? rowCount - 1 : attr.getOppoEndIndex();
            startColumn = attr.getStartIndex();
            endColumn = attr.getEndIndex() == -1 ? colCount - 1 : attr.getEndIndex();
        }
        
        repeatColDataTextField.setText(ColumnRow.valueOf(startColumn, startRow).toString() +":"
                +  ColumnRow.valueOf(endColumn, endRow).toString());
        copyTitleTextField.setText(attr.getIndexsToCopy());
        showBlankCheckBox.setSelected(attr.isShowBlank());
    }
    
    /**
	 * update行数据
	 */
    private void updateRow(WorkSheetAttr attr){
        attr.setDirection(Constants.TOP_TO_BOTTOM);

        if (this.maxRadioButton.isSelected()) {
            int value = ((Integer) (this.maxNumberSpinner.getValue())).intValue();
            if (value > 0 && value < Integer.MAX_VALUE) {
                attr.setMaxCount(value);
            }
        } else {
            int value = ((Integer) this.toXSpinner.getValue()).intValue();
            if (value > 0 && value < Integer.MAX_VALUE) {
                attr.setCount(value);
            }
        }
    }
    
    /**
	 * update列数据
	 */
    private void updateCol(WorkSheetAttr attr){
        attr.setDirection(Constants.LEFT_TO_RIGHT);

        if (this.maxRadioButton.isSelected()) {
            double value = ((Integer) (this.maxNumberSpinner.getValue())).intValue();
            if (value > 0 && value < Integer.MAX_VALUE) {
                attr.setMaxCount((int) value);
            }
        } else {
            double value = ((Integer) this.toXSpinner.getValue()).intValue();
            if (value > 0 && value < Integer.MAX_VALUE) {
                attr.setCount((int) value);
            }
        }
    }
    
    /**
	 * update数据给WorksheetAttr
	 *
	 */
    public void update(WorkSheetAttr attr){
    	if (!isRepeate) {
	    	attr.setDirection(Constants.NONE);
	    	return;
	    } 
    	
    	switch (rowOrColumn) {
		case ROW:
        	updateRow(attr);
            divide(attr, repeatColDataTextField, copyTitleTextField, showBlankCheckBox);
			break;
		case COLUMN:
        	updateCol(attr);
            divide(attr, repeatColDataTextField, copyTitleTextField, showBlankCheckBox);
			break;
		default:
            attr.setDirection(Constants.NONE);
			break;
		}
    }

    /**
	 * update数据给worksheet
	 * 
	 * @param worksheet 当前worksheet
	 */
    public void update(WorkSheet worksheet) {
        if (worksheet == null) {
            return;
        }

        WorkSheetAttr attr = new WorkSheetAttr();
        worksheet.setWorkSheetAttr(attr);

        update(attr);
    }


    /**
     * 从界面中赋值给WorkSheetAttr
     * 
     * @param attr 报表分栏属性
     * @param repeatColDataTextField 从哪行开始复制
     * @param copyTitleTextField 复制列序列
     * @param showBlankCheckBox 是否填充空白
     */
    public void divide(WorkSheetAttr attr, UITextField repeatColDataTextField,
                       UITextField copyTitleTextField, UICheckBox showBlankCheckBox) {
        String[] repeateData = repeatColDataTextField.getText().split(":");
        if (repeateData.length != 2 ) {
            return;
        }
        ColumnRow startCR = ColumnRow.valueOf(repeateData[0]);
        ColumnRow endCR = ColumnRow.valueOf(repeateData[1]);
        int start,oppoStart,end,oppoEnd;
    	if (attr.getDirection() == ROW) {
            start = startCR.getRow();
            end = endCR.getRow();
            oppoStart = startCR.getColumn();
            oppoEnd = endCR.getColumn();
        }else {
            start = startCR.getColumn();
            end = endCR.getColumn();
            oppoStart = startCR.getRow();
            oppoEnd = endCR.getRow();
        }
        attr.setStartIndex(start);
        attr.setOppoStartIndex(oppoStart);
        attr.setEndIndex(end);
        attr.setOppoEndIndex(oppoEnd);
        attr.setIndexsToCopy(copyTitleTextField.getText());
        attr.setShowBlank(showBlankCheckBox.isSelected());
    }
    

    /**
	 * 判断输入是否合法
	 */
    public void checkValid() throws Exception {
        String repeatText = repeatColDataTextField.getText().trim();
        if(StringUtils.isEmpty(repeatText)){
        	return;
        }
        
        boolean valid = true;
    	if(!repeatText.matches("[a-zA-Z]+[0-9]+[:][a-zA-Z]+[0-9]+")){
    		valid = false;
    	}else{
     	    String[] repeateTexts = repeatText.split(":");
            ColumnRow start = ColumnRow.valueOf(repeateTexts[0]);
            ColumnRow end = ColumnRow.valueOf(repeateTexts[1]);
            valid = start.getRow() <= end.getRow() && start.getColumn() <= end.getColumn();
    	}

        if (!valid) {
        	repeatColDataTextField.setText(StringUtils.EMPTY);
            throw new Exception(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Column_Warn_Text"));
        }
    }
}
