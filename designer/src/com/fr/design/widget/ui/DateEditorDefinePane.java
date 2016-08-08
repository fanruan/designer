package com.fr.design.widget.ui;

import com.fr.base.FRContext;
import com.fr.base.Formula;
import com.fr.data.core.FormatField;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.DateEditor;
import com.fr.general.DateUtils;
import com.fr.general.Inter;
import com.fr.script.Calculator;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.UtilEvalError;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateEditorDefinePane extends DirectWriteEditorDefinePane<DateEditor> {
	private UIComboBox returnTypeComboBox;
	private UILabel sampleLabel;// preview
	// content
	private UITextField patternTextField = null;
	private ValueEditorPane startDv;
	private ValueEditorPane endDv;

	public DateEditorDefinePane() {
		this.initComponets();
	}

	private void initComponets() {
		super.initComponents();
	}
	
	@Override
	protected String title4PopupWindow() {
		return "Date";
	}

	@Override
	protected JPanel setSecondContentPane() {
		JPanel otherContentPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
		otherContentPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		JPanel returnTypePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		returnTypePane.add(new UILabel(Inter.getLocText("Widget-Date_Selector_Return_Type") + ":"), BorderLayout.WEST);
		returnTypeComboBox = new UIComboBox(new String[] { Inter.getLocText("String"), Inter.getLocText("Date") });
		returnTypeComboBox.setPreferredSize(new Dimension(70, 20));
		returnTypePane.add(returnTypeComboBox, BorderLayout.CENTER);
		JPanel firstPanel = GUICoreUtils.createFlowPane(new JComponent[]{returnTypePane}, FlowLayout.LEFT, 5);
		firstPanel.setPreferredSize(new Dimension(141,30));
		otherContentPane.add(firstPanel);

		// sample pane
		sampleLabel = new UILabel("");
//		samplePane.add(sampleLabel, BorderLayout.CENTER);
		sampleLabel.setBorder(BorderFactory.createEmptyBorder(2, 4, 4, 4));
		sampleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		sampleLabel.setFont(FRContext.getDefaultValues().getFRFont());

		// content pane
		patternTextField = new UITextField();
		patternTextField.getDocument().addDocumentListener(patternTextDocumentListener);

        String[] arr = getDateFormateArray();
		final UIComboBox comboBox = new UIComboBox(arr);
		comboBox.setPreferredSize(new Dimension(150,20));
		comboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				patternTextField.setText( (String)comboBox.getSelectedItem());
			}

		});
		JPanel secondPanel = GUICoreUtils.createFlowPane(new JComponent[]{new UILabel(Inter.getLocText("FR-Engine_Format") + ":"),comboBox,sampleLabel}, FlowLayout.LEFT, 5);
		secondPanel.setPreferredSize(new Dimension(220,30));
		otherContentPane.add(secondPanel);
		otherContentPane.add(initStartEndDatePane(), BorderLayout.SOUTH);
		return otherContentPane;
	}

	private String[] getDateFormateArray() {
		return FormatField.getInstance().getDateFormatArray();
	}

	protected JPanel initStartEndDatePane() {
		JPanel rangePane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		rangePane.add(new UILabel(Inter.getLocText("FS_Start_Date") + ":"));
		startDv = ValueEditorPaneFactory.createDateValueEditorPane(null, null);
		rangePane.add(startDv);
		rangePane.add(new UILabel(Inter.getLocText("FS_End_Date") + ":"));
		endDv = ValueEditorPaneFactory.createDateValueEditorPane(null, null);
		rangePane.add(endDv);

		return rangePane;
	}

	private DocumentListener patternTextDocumentListener = new DocumentListener() {
		public void insertUpdate(DocumentEvent evt) {
			refreshPreviewLabel();
		}

		public void removeUpdate(DocumentEvent evt) {
			refreshPreviewLabel();
		}

		public void changedUpdate(DocumentEvent evt) {
			refreshPreviewLabel();
		}
	};

	private void refreshPreviewLabel() {
		String text = patternTextField.getText();
		if (text != null && text.length() > 0) {
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(text);
                String sample = simpleDateFormat.format(new Date());
                Color c = Color.black;
                if (!ArrayUtils.contains(FormatField.getInstance().getDateFormatArray(), text)) {
                    sample += " " + Inter.getLocText("DateFormat-Custom_Warning");
                    c = Color.red;
                }
				this.sampleLabel.setText(sample);
                this.sampleLabel.setForeground(c);
			} catch (Exception exp) {
				this.sampleLabel.setForeground(Color.red);
				this.sampleLabel.setText(exp.getMessage());
			}
		} else {
			this.sampleLabel.setText(new Date().toString());
		}
	}

	@Override
	protected void populateSubDirectWriteEditorBean(DateEditor e) {
		String formatText = e.getFormatText();
		patternTextField.setText(formatText);

		returnTypeComboBox.setSelectedIndex(e.isReturnDate() ? 1 : 0);

		populateStartEnd(e);
	};

	@Override
	protected DateEditor updateSubDirectWriteEditorBean() {
		DateEditor ob = new DateEditor();

		ob.setFormatText(this.getSimpleDateFormat().toPattern());
		ob.setReturnDate(returnTypeComboBox.getSelectedIndex() == 1);

		updateStartEnd(ob);

		return ob;
	}

    /**
     * 初始起止日期
     * @param dateWidgetEditor 日期控件
     */
	public void populateStartEnd(DateEditor dateWidgetEditor) {
		Formula startFM = dateWidgetEditor.getStartDateFM();
		Formula endFM = dateWidgetEditor.getEndDateFM();
		if (startFM != null) {
			startDv.populate(startFM);
		} else {
			String startStr = dateWidgetEditor.getStartText();
			startDv.populate(StringUtils.isEmpty(startStr) ? null : DateUtils.string2Date(startStr, true));
		}
		if (endFM != null) {
			endDv.populate(endFM);
		} else {
			String endStr = dateWidgetEditor.getEndText();
			endDv.populate(StringUtils.isEmpty(endStr) ? null : DateUtils.string2Date(endStr, true));
		}
	}

    /**
     * 更新日期控件的起止日期
     * @param dateWidgetEditor 日期控件
     */
	public void updateStartEnd(DateEditor dateWidgetEditor) {
		Object startObject = startDv.update();
		Object endObject = endDv.update();
		// wei : 对公式的处理
		Calculator cal = null;
		if (startObject instanceof Formula) {
			cal = Calculator.createCalculator();
			Formula startFormula = (Formula) startObject;
			try {
				startFormula.setResult(cal.evalValue(startFormula.getContent()));
			} catch (UtilEvalError e) {
				FRContext.getLogger().error(e.getMessage(), e);
			}
			startObject = startFormula.getResult();
			dateWidgetEditor.setStartDateFM(startFormula);
			dateWidgetEditor.setStartText(null);
		} else {
	        try {
	        	dateWidgetEditor.setStartText(startObject == null ? "" : DateUtils.getDate2Str("MM/dd/yyyy", (Date)startObject));
	        } catch(ClassCastException e) {
	        	//wei : TODO 说明应用的公式不能转化成日期格式，应该做些处理。
	        }
		}
		if (endObject instanceof Formula) {
			cal = Calculator.createCalculator();
			Formula endFormula = (Formula) endObject;
			try {
				endFormula.setResult(cal.evalValue(endFormula.getContent()));
			} catch (UtilEvalError e) {
				FRContext.getLogger().error(e.getMessage(), e);
			}
			endObject = endFormula.getResult();
			dateWidgetEditor.setEndDateFM(endFormula);
			dateWidgetEditor.setEndText(null);
		} else {
	        try {
	        	dateWidgetEditor.setEndText(endObject == null ? "" : DateUtils.getDate2Str("MM/dd/yyyy", (Date)endObject));
	        } catch(ClassCastException e) {
	        	
	        }
		}
	}

	private SimpleDateFormat getSimpleDateFormat() {
		String text = patternTextField.getText();
		SimpleDateFormat simpleDateFormat;
		if (text != null && text.length() > 0) {
			try {
				simpleDateFormat = new SimpleDateFormat(patternTextField.getText());
				this.sampleLabel.setText(simpleDateFormat.format(new Date()));
			} catch (Exception exp) {
				simpleDateFormat = new SimpleDateFormat("");
			}
		} else {
			simpleDateFormat = new SimpleDateFormat("");
		}

		return simpleDateFormat;

	}

}