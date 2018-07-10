package com.fr.design.dscolumn;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.icombobox.FunctionComboBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.core.group.*;
import com.fr.stable.StringUtils;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class ResultSetGroupPopUpPane extends ResultSetGroupPane {


	private UIRadioButton groupRadioButton;
	private UIButton advancedButton;
	private UIRadioButton listRadioButton;
	private UIRadioButton summaryRadioButton;
	private FunctionComboBox functionComboBox;


	private String InsertText = StringUtils.BLANK;
	
	public ResultSetGroupPopUpPane() {
		this(true);
	}

	// 将结果集进行分组: 分组，列表，汇总，自定义 
	public ResultSetGroupPopUpPane(boolean isGroup) {
		super();
		this.initComponents(isGroup);
	}

	public void initComponents(boolean isGroup) {
		this.setLayout(FRGUIPaneFactory.create1ColumnGridLayout());

		// 分组
		groupRadioButton = new UIRadioButton(Inter
				.getLocText("BindColumn-Group(Merger_the_Items_Which_Have_The_Same_Value_in_Column)"));
		groupRadioButton.addActionListener(checkEnabledActionListener);

		groupComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				checkButtonEnabled();
			}
		});
		advancedButton = new UIButton(Inter.getLocText("Custom"));
		advancedButton.addActionListener(groupAdvancedListener);
		this.add(GUICoreUtils.createFlowPane(
				new JComponent[]{new UILabel(InsertText), groupRadioButton, groupComboBox, advancedButton}, FlowLayout.LEFT));

		// 列表
		listRadioButton = new UIRadioButton(Inter
				.getLocText("BindColumn-Select(Regardless_of_Having_the_Same_Value,Display_all_Item_in_Column)"));
		listRadioButton.addActionListener(checkEnabledActionListener);
		this.add(GUICoreUtils.createFlowPane(
				new JComponent[]{new UILabel(InsertText), listRadioButton}, FlowLayout.LEFT));

		// 汇总		
		summaryRadioButton = new UIRadioButton(Inter
				.getLocText("BindColumn-Summary(Including_SUM_,_AVERAGE_,_MAX_,_MIN_And_So_On)"), true);
		summaryRadioButton.addActionListener(checkEnabledActionListener);
		functionComboBox = new FunctionComboBox(GUICoreUtils.getFunctionArray());
		this.add(GUICoreUtils.createFlowPane(
				new JComponent[]{new UILabel(InsertText), summaryRadioButton, functionComboBox}, FlowLayout.LEFT));


		ButtonGroup resultSetGroupButtonGroup = new ButtonGroup();
		resultSetGroupButtonGroup.add(groupRadioButton);
		if (isGroup) {
			groupRadioButton.setSelected(true);
		} else {
			listRadioButton.setSelected(true);
		}
		resultSetGroupButtonGroup.add(listRadioButton);
		resultSetGroupButtonGroup.add(summaryRadioButton);
		checkButtonEnabled();
	}

	@Override
	public void populate(TemplateCellElement cellElement) {
		this.cellElement = cellElement;

		if (isNPE(cellElement)) {
            return;
        }
		DSColumn dSColumn = (DSColumn) cellElement.getValue();

		// populate groupPane
		// RecordGrouper
		recordGrouper = dSColumn.getGrouper();
		if (recordGrouper instanceof FunctionGrouper && !((FunctionGrouper) recordGrouper).isCustom()) {
			int mode = ((FunctionGrouper) recordGrouper).getDivideMode();
			if (mode == FunctionGrouper.GROUPING_MODE) {
				this.groupRadioButton.setSelected(true);
				this.groupComboBox.setSelectedIndex(COMMON);
			} else if (mode == FunctionGrouper.CONTINUUM_MODE) {
				this.groupRadioButton.setSelected(true);
				this.groupComboBox.setSelectedIndex(CONTINUUM);
			} else if (mode == FunctionGrouper.LIST_MODE) {
				this.listRadioButton.setSelected(true);
			}
		} else if (recordGrouper instanceof FunctionGrouper && ((FunctionGrouper) recordGrouper).isCustom()) {
			// 这种情况也放到自定义分组里面
			this.groupRadioButton.setSelected(true);
			this.groupComboBox.setSelectedIndex(ADVANCED);
		} else if (recordGrouper instanceof SummaryGrouper) {
			this.summaryRadioButton.setSelected(true);
			this.functionComboBox.setFunction(((SummaryGrouper) recordGrouper).getFunction());
		} else if (recordGrouper instanceof CustomGrouper) {
			// 自定义分组 or 高级分组
			this.groupRadioButton.setSelected(true);
			this.groupComboBox.setSelectedIndex(ADVANCED);
		}

		checkButtonEnabled();
	}

	@Override
	public void update() {
		if (isNPE(cellElement)) {
			return;
		}
		DSColumn dSColumn = (DSColumn) cellElement.getValue();

		if (this.groupRadioButton.isSelected()) {
			recordGrouper = updateGroupCombox();
		} else if (this.listRadioButton.isSelected()) {
			FunctionGrouper valueGrouper = new FunctionGrouper();
			valueGrouper.setDivideMode(FunctionGrouper.LIST_MODE);
			valueGrouper.setCustom(false);
			recordGrouper = valueGrouper;
		} else if (this.summaryRadioButton.isSelected()) {
			SummaryGrouper summaryGrouper = new SummaryGrouper();
			summaryGrouper.setFunction(functionComboBox.getFunction());
			recordGrouper = summaryGrouper;
		} else {
		}

		dSColumn.setGrouper(recordGrouper);
	}

	private void checkButtonEnabled() {
		advancedButton.setEnabled(false);
		functionComboBox.setEnabled(false);
		groupComboBox.setEnabled(false);
		if (summaryRadioButton.isSelected()) {
			functionComboBox.setEnabled(true);
		}

		if (groupRadioButton.isSelected()) {
			groupComboBox.setEnabled(true);
			if (groupComboBox.getSelectedIndex() == 2) {
				advancedButton.setEnabled(true);
			}
		}
	}

	public boolean isSummaryRadioButtonSelected() {
		return summaryRadioButton.isSelected();
	}

	public void addListeners(ActionListener summaryListener, ActionListener otherListener, ActionListener sdcListener) {
		summaryRadioButton.addActionListener(summaryListener);
		groupRadioButton.addActionListener(otherListener);
		listRadioButton.addActionListener(otherListener);
		advancedButton.addActionListener(sdcListener);
	}

	ActionListener checkEnabledActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			checkButtonEnabled();
		}
	};


	@Override
	public void setRecordGrouper(RecordGrouper recordGrouper) {
		this.recordGrouper = recordGrouper;
	}
}