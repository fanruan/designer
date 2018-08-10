/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.headerfooter;

import com.fr.base.BaseUtils;
import com.fr.base.PaperSize;
import com.fr.base.ScreenResolution;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.page.ReportSettingsProvider;
import com.fr.report.core.ReportHF;
import com.fr.report.stable.ReportConstants;
import com.fr.stable.unit.FU;
import com.fr.stable.unit.INCH;
import com.fr.stable.unit.UNIT;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.Hashtable;

/**
 * Edit header or footer(the object Header and Footer).
 */
public abstract class HeaderFooterPane extends BasicPane {
	private Hashtable reportHFHash = new Hashtable();

	private JList reportHFYypeList;
	private int lastSelectedHFType = -1;
	private UICheckBox defineCheckBox;
	private HeaderFooterEditPane headerFooterEditPane;
	private boolean isHeader;

	/**
	 * init components.
	 */
	public HeaderFooterPane() {
		this.setLayout(new BorderLayout(0, 4));
		// Left pane
		JPanel leftPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		this.add(leftPane, BorderLayout.WEST);

		DefaultListModel defaultListModel = new DefaultListModel();
		reportHFYypeList = new JList(defaultListModel);
		reportHFYypeList.setCellRenderer(reportPageTypeRenderer);
		reportHFYypeList
				.addListSelectionListener(reportHFTypeSelectionListener);
		JScrollPane hfTypeListScrollPane = new JScrollPane(reportHFYypeList);
		leftPane.add(hfTypeListScrollPane, BorderLayout.CENTER);

		defaultListModel.addElement(new Integer(
				ReportConstants.REPORTPAGE_DEFAULT));
		defaultListModel.addElement(new Integer(
				ReportConstants.REPORTPAGE_FIRST));
		defaultListModel
				.addElement(new Integer(ReportConstants.REPORTPAGE_LAST));
		defaultListModel
				.addElement(new Integer(ReportConstants.REPORTPAGE_ODD));
		defaultListModel
				.addElement(new Integer(ReportConstants.REPORTPAGE_EVEN));

		// CenterPane.
		JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		this.add(centerPane, BorderLayout.CENTER);

		JPanel definePane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		centerPane.add(definePane, BorderLayout.NORTH);

		defineCheckBox = new UICheckBox(Toolkit.i18nText("Fine-Design_Report_HF_Whether_To_Define_The_Selected_Type"), true);
		definePane.add(defineCheckBox);
		defineCheckBox.addChangeListener(defineChangeListener);

		// HeaderFooterEditPane.
		headerFooterEditPane = new HeaderFooterEditPane();
		centerPane.add(headerFooterEditPane, BorderLayout.CENTER);
		headerFooterEditPane.setBorder(BorderFactory
				.createLineBorder(GUICoreUtils.getTitleLineBorderColor()));
	}

	/**
	 * populate reportSettings
	 *
	 * @param reportSettings
	 */
	public void populate(ReportSettingsProvider reportSettings, boolean isHeader) {
		headerFooterEditPane.populateReportSettings(reportSettings, isHeader);
	}

	/**
	 * update reportSettings
	 */
	public UNIT updateReportSettings() {
		return headerFooterEditPane.updateReportSettings();
	}

	public void populate(Hashtable reportHFHash) {
		this.reportHFHash = reportHFHash;

		this.reportHFYypeList.setSelectedIndex(0);
	}

	public Hashtable update() {
		Object selectObj = reportHFYypeList.getSelectedValue();
		if (selectObj != null && selectObj instanceof Integer) {
			if (defineCheckBox.isSelected()) {
				reportHFHash.put(lastSelectedHFType,
						headerFooterEditPane.update());
			} else {
				reportHFHash.remove(new Integer(lastSelectedHFType));
			}
		}

		return reportHFHash;
	}

	ChangeListener defineChangeListener = new ChangeListener() {
		public void stateChanged(ChangeEvent evt) {
			Object selectObj = reportHFYypeList.getSelectedValue();
			if (selectObj == null || !(selectObj instanceof Integer)) {
				return;
			}

			int currentReportHFType = (Integer) selectObj;
			if (defineCheckBox.isSelected()) {
				GUICoreUtils.setEnabled(headerFooterEditPane, true);
				reportHFHash.put(currentReportHFType,
						headerFooterEditPane.update());
			} else {
				GUICoreUtils.setEnabled(headerFooterEditPane, false);
				reportHFHash.remove(new Integer(currentReportHFType));
			}

			reportHFYypeList.repaint();
		}
	};

	ListSelectionListener reportHFTypeSelectionListener = new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent evt) {
			Object selectObj = reportHFYypeList.getSelectedValue();
			if (selectObj == null || !(selectObj instanceof Integer)) {
				return;
			}

			int currentReportHFType = (Integer) selectObj;
			if (currentReportHFType != lastSelectedHFType) {
				// 需要保存上次选择的页面.
				if (lastSelectedHFType != -1) {// last SelectedHFType
					if (defineCheckBox.isSelected()) {
						reportHFHash.put(lastSelectedHFType,
								headerFooterEditPane.update());
					} else {
						reportHFHash.remove(new Integer(lastSelectedHFType));
					}
				}

				lastSelectedHFType = currentReportHFType;

				// Populate当前的.
				Object reportHFObject = reportHFHash.get(new Integer(
						currentReportHFType));
				if (reportHFObject == null) {
					defineCheckBox.setSelected(false);
				} else {
					defineCheckBox.setSelected(true);
				}
				headerFooterEditPane.populate((ReportHF) reportHFObject,
						FU.getInstance(PaperSize.PAPERSIZE_A4.getWidth().toFU() - new INCH(0.75f).toFU() - new INCH(0.75f).toFU()).toPixD(ScreenResolution.getScreenResolution()),
						0.53 * ScreenResolution.getScreenResolution());

//				headerFooterEditPane.populate(null, eventMask, eventMask);
				// 默认页面
				if (currentReportHFType == ReportConstants.REPORTPAGE_DEFAULT) {
					defineCheckBox.setSelected(true);
					defineCheckBox.setEnabled(false);
				} else {
					defineCheckBox.setEnabled(true);
				}
			}
		}
	};


	UIComboBoxRenderer reportPageTypeRenderer = new UIComboBoxRenderer() {
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
													  int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);

			if (value != null && value instanceof Integer) {
				int reportHFType = (Integer) value;

				this
						.setIcon(BaseUtils
								.readIcon("/com/fr/base/images/oem/logo.png"));
				if (reportHFType == ReportConstants.REPORTPAGE_DEFAULT) {
					this.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_HF_Default_Page"));
				} else if (reportHFType == ReportConstants.REPORTPAGE_FIRST) {
					this.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_HF_First_Page"));
				} else if (reportHFType == ReportConstants.REPORTPAGE_LAST) {
					this.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Last_Page"));
				} else if (reportHFType == ReportConstants.REPORTPAGE_ODD) {
					this.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_HF_Odd_Page"));
				} else if (reportHFType == ReportConstants.REPORTPAGE_EVEN) {
					this.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_HF_Even_Page"));
				}

				if (reportHFHash != null) {
					Object obj = reportHFHash.get(new Integer(reportHFType));
					if (obj == null) {
						this.setEnabled(false);
					}
				}
			}

			return this;
		}
	};


}
