package com.fr.design.widget.ui;

import com.fr.base.FRContext;
import com.fr.base.Formula;
import com.fr.data.core.FormatField;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.component.DateValuePane;
import com.fr.design.widget.component.UIComboBoxNoArrow;
import com.fr.form.ui.DateEditor;
import com.fr.general.DateUtils;
import com.fr.general.Inter;
import com.fr.script.Calculator;
import com.fr.stable.ArrayUtils;
import com.fr.stable.FormulaProvider;
import com.fr.stable.UtilEvalError;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateEditorDefinePane extends DirectWriteEditorDefinePane<DateEditor> {
	private UIButtonGroup returnTypeComboBox;

	private DateValuePane startDv;
	private DateValuePane endDv;

	private UIComboBox currentFormatComboBox;
	private UILabel currentSamplelabel;
	private UIButtonGroup fomatHeadGroup;
	private static final int SAMPLE_LABEL_PADDING = 4;


	public DateEditorDefinePane() {
	}

	@Override
	protected String title4PopupWindow() {
		return "Date";
	}

	@Override
	protected JPanel setSecondContentPane() {
		returnTypeComboBox = new UIButtonGroup<>(new String[] {Inter.getLocText("Date") ,  Inter.getLocText("String")});
		JPanel formatHead =  createFormatHead();

		startDv = new DateValuePane();
		endDv = new DateValuePane();

		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		UILabel formatLabel = new UILabel(Inter.getLocText("FR-Engine_Format"));
		formatLabel.setVerticalAlignment(SwingConstants.TOP);
		UILabel startDateLabel = new UILabel(Inter.getLocText("FS_Start_Date"));
		startDateLabel.setVerticalAlignment(SwingConstants.TOP);
		UILabel endDateLabel = new UILabel(Inter.getLocText("FS_End_Date"));
		endDateLabel.setVerticalAlignment(SwingConstants.TOP);

		Component[][] components = new Component[][]{
				new Component[]{formatLabel, formatHead},
				new Component[]{startDateLabel, startDv},
				new Component[]{endDateLabel, endDv},
				new Component[]{waterMarkDictPane, null},
				new Component[]{new UILabel(Inter.getLocText("Widget-Date_Selector_Return_Type")), returnTypeComboBox }
		};
		double[] rowSize = {p, p, p, p, p, p, p};
		double[] columnSize = {p, f};
		int[][] rowCount = {{1, 3},{1, 1},{1, 1},{1, 1},{1, 1}};
		JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_L2, IntervalConstants.INTERVAL_L1);


		return panel;
	}

	private JPanel createFormatPane(UIComboBox formatComboBox, UILabel sampleLabel){
		JPanel previewPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Base_StyleFormat_Sample"));
		previewPane.setLayout(new BorderLayout());

		JPanel sampleLabelWrapper = new JPanel(new BorderLayout());
		sampleLabelWrapper.setBorder(BorderFactory.createEmptyBorder(0, SAMPLE_LABEL_PADDING, SAMPLE_LABEL_PADDING, SAMPLE_LABEL_PADDING));
		sampleLabelWrapper.add(sampleLabel, BorderLayout.CENTER);

		previewPane.add(sampleLabelWrapper, BorderLayout.CENTER);
		JPanel jPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
		jPanel.add(previewPane, BorderLayout.NORTH);
		jPanel.add(formatComboBox, BorderLayout.CENTER);
		return jPanel;
	}

	private UILabel createSamplePane(){
		UILabel sampleLabel = new UILabel("") {
			@Override
			public void setText(String text) {
				// 加上<html>可以自动换行
				super.setText("<html>" + text + "</html>");
			}
		};
		sampleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		sampleLabel.setFont(FRContext.getDefaultValues().getFRFont());
		return sampleLabel;
	}

	private JPanel createFormatHead(){
		String[] dateArray = FormatField.getInstance().getFormatArray(FormatField.FormatContents.DATE);
		String[] timeArray = FormatField.getInstance().getFormatArray(FormatField.FormatContents.TIME);
		final UIComboBox dateFormatComboBox = new UIComboBoxNoArrow(dateArray);
		final UIComboBox timeFormatComboBox = new UIComboBoxNoArrow(timeArray);
		dateFormatComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				refreshPreviewLabel();
			}
		});
		timeFormatComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				refreshPreviewLabel();
			}
		});
		final UILabel dateSampleLabel = createSamplePane();
		final UILabel timeSampleLabel = createSamplePane();
		JPanel fomatHeadPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		final CardLayout cardLayout = new CardLayout();
		final JPanel customPane = new JPanel(cardLayout);
		JPanel dateFormatPane = createFormatPane(dateFormatComboBox, dateSampleLabel);
		JPanel timeFormatPane = createFormatPane(timeFormatComboBox, timeSampleLabel);
		customPane.add(dateFormatPane, Inter.getLocText("StyleFormat-Date"));
		customPane.add(timeFormatPane, Inter.getLocText("StyleFormat-Time"));
		final String[] tabTitles = new String[]{Inter.getLocText("StyleFormat-Date"), Inter.getLocText("StyleFormat-Time")};
		fomatHeadGroup = new UIButtonGroup(new String[]{Inter.getLocText("StyleFormat-Date"), Inter.getLocText("StyleFormat-Time")});
		fomatHeadGroup.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int newSelectedIndex = fomatHeadGroup.getSelectedIndex();
				cardLayout.show(customPane, tabTitles[newSelectedIndex]);
				if(newSelectedIndex == 0){
					currentFormatComboBox = dateFormatComboBox;
					currentSamplelabel = dateSampleLabel;
				}else{
					currentFormatComboBox = timeFormatComboBox;
					currentSamplelabel = timeSampleLabel;
				}
				refreshPreviewLabel();
			}
		});
		fomatHeadPane.add(fomatHeadGroup, BorderLayout.NORTH);
		fomatHeadPane.add(customPane, BorderLayout.CENTER);
		return fomatHeadPane;
	}



	private void refreshPreviewLabel() {
		String text = (String) currentFormatComboBox.getSelectedItem();
		if (text != null && text.length() > 0) {
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(text);
				String sample = simpleDateFormat.format(new Date());
				Color c = Color.black;
				if (!ArrayUtils.contains(FormatField.getInstance().getDateFormatArray(), text)) {
					sample += " " + Inter.getLocText("DateFormat-Custom_Warning");
					c = Color.red;
				}
				currentSamplelabel.setText(sample);
				currentSamplelabel.setForeground(c);
			} catch (Exception exp) {
				currentSamplelabel.setForeground(Color.red);
				currentSamplelabel.setText(exp.getMessage());
			}
		} else {
			currentSamplelabel.setText(new Date().toString());
		}
	}

	private int getDateType(DateEditor e){
		String[] timeArray = FormatField.getInstance().getFormatArray(FormatField.FormatContents.TIME);
		if(e == null){
			return 0;
		}
		String formatText = e.getFormatText();
		if(ArrayUtils.contains(timeArray, formatText)){
			return 1;
		}
		return 0;
	}


	@Override
	protected void populateSubDirectWriteEditorBean(DateEditor e) {
		String formatText = e.getFormatText();
		fomatHeadGroup.setSelectedIndex(getDateType(e));
		fomatHeadGroup.populateBean();
		currentFormatComboBox.setSelectedItem(formatText);

		returnTypeComboBox.setSelectedIndex(e.isReturnDate() ? 0 : 1);
		startDv.populate(e.getStartDate());
		endDv.populate(e.getEndDate());
	}

	@Override
	protected DateEditor updateSubDirectWriteEditorBean() {
		DateEditor ob = new DateEditor();
		ob.setFormatText(this.getSimpleDateFormat().toPattern());
		ob.setReturnDate(returnTypeComboBox.getSelectedIndex() == 0);
		ob.setStartDate(startDv.update());
		ob.setEndDate(endDv.update());

		return ob;
	}

	private SimpleDateFormat getSimpleDateFormat() {
		String text = (String) currentFormatComboBox.getSelectedItem();
		SimpleDateFormat simpleDateFormat;
		if (text != null && text.length() > 0) {
			try {
				simpleDateFormat = new SimpleDateFormat(text);
				this.currentSamplelabel.setText(simpleDateFormat.format(new Date()));
			} catch (Exception exp) {
				simpleDateFormat = new SimpleDateFormat("");
			}
		} else {
			simpleDateFormat = new SimpleDateFormat("");
		}

		return simpleDateFormat;

	}

}