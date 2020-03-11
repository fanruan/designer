package com.fr.design.webattr;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.EditingStringListPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.widget.FRWidgetFactory;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.stable.CoreConstants;
import com.fr.stable.StringUtils;
import com.fr.stable.project.ProjectConstants;
import com.fr.web.attr.ReportWebAttr;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class WebCssPane extends BasicPane {
	private UITextField localText;
	UIButton chooseFile;
	private EditingStringListPane centerPane;

	public WebCssPane() {
		this.setLayout(new BorderLayout(0, 20));
		this.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 0));
		
		JPanel outnorth = new JPanel(new BorderLayout(0, 5));
		JPanel northPane = new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));
		localText = new UITextField();
		localText.setPreferredSize(new Dimension(450, 20));
		localText.setEditable(false);
		chooseFile = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Selection"));
		chooseFile.setPreferredSize(new Dimension(75, 23));
		chooseFile.addActionListener(chooseFileListener);
		northPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Disk_File") + ":"), FlowLayout.LEFT);
		northPane.add(localText, FlowLayout.CENTER);
		northPane.add(chooseFile, FlowLayout.RIGHT);
		outnorth.add(northPane,BorderLayout.NORTH);
		UILabel infor = FRWidgetFactory.createLineWrapLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CSS_Warning",
																								ProjectConstants.WEBAPP_NAME, ProjectConstants.WEBAPP_NAME));
		infor.setForeground(new Color(207, 42, 39));
		outnorth.add(infor,BorderLayout.CENTER);
		this.add(outnorth, BorderLayout.NORTH);
		centerPane = new EditingStringListPane() {

			@Override
			protected void selectedChanged(String selected) {
				localText.setText(selected);
				checkEnableState();
			}

			@Override
			protected String getAddOrEditString() {
				return localText.getText();
			}
		};
		this.add(centerPane, BorderLayout.CENTER);
	}

	private ActionListener chooseFileListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			FILEChooserPane fileChooser = FILEChooserPane.getInstance(false, false, true,
					new ChooseFileFilter("css", "css" + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_File")));

			if (fileChooser.showOpenDialog(DesignerContext.getDesignerFrame()) == FILEChooserPane.OK_OPTION) {
				final FILE file = fileChooser.getSelectedFILE();
				if (file == null) {// 选择的文件不能是 null
					return;
				}

				String fileName = file.getName();
				String fileType = fileName.substring(fileName.lastIndexOf(CoreConstants.DOT) + 1);
				if (!"css".equalsIgnoreCase(fileType)) {
					return;
				}
				localText.setText(file.getPath());
				centerPane.setAddEnabled(true);
			}

			fileChooser.removeFILEFilter(new ChooseFileFilter("js"));
		}
	};

	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ReportServerP_Import_Css");
	}

	public void populate(ReportWebAttr reportWebAttr) {
		if (reportWebAttr == null) {
			centerPane.populateBean(new ArrayList<String>());
			return;
		}
		List<String> list = new ArrayList<String>();

		for (int i = 0; i < reportWebAttr.getCSSImportCount(); i++) {
			if (StringUtils.isNotBlank(reportWebAttr.getCSSImport(i))) {
				list.add(reportWebAttr.getCSSImport(i));
			}
		}
		centerPane.populateBean(list);
	}

	public void update(ReportWebAttr reportWebAttr) {
		List<String> valueList = centerPane.updateBean();
		reportWebAttr.clearCSSImportList();
		for (int i = 0; i < valueList.size(); i++) {
			String a = valueList.get(i);
			reportWebAttr.addCSSImport(a);
		}

	}
}
