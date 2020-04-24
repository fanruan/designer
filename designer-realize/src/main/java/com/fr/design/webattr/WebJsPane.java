package com.fr.design.webattr;

import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.frpane.EditingStringListPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.widget.FRWidgetFactory;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.CoreConstants;
import com.fr.stable.StringUtils;
import com.fr.stable.project.ProjectConstants;
import com.fr.web.attr.ReportWebAttr;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class WebJsPane extends BasicPane {
	private UITextField localText;
	private UITextField urlText;
	private UIRadioButton localFileRadioButton;
	private UIRadioButton urlFileRadioButton;
	private EditingStringListPane editingPane;
	UIButton chooseFile;
	UIButton testConnection;
	UILabel infor1;
	UILabel infor2;

	public WebJsPane() {
		this.setLayout(new BorderLayout(0, 20));
		this.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 0));

		localFileRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Disk_File") + ":", true);
		urlFileRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Url_Location")+ ":", false);
		ButtonGroup bg = new ButtonGroup();
		bg.add(localFileRadioButton);
		bg.add(urlFileRadioButton);
		localFileRadioButton.addActionListener(radioActionListener);
		urlFileRadioButton.addActionListener(radioActionListener);
		urlFileRadioButton.setForeground(new Color(143, 142, 139));
		localFileRadioButton.setForeground(Color.black);
		localText = new UITextField();
		localText.setEditable(false);
		urlText = new UITextField();
		localText.setPreferredSize(new Dimension(450, 20));
		urlText.setPreferredSize(new Dimension(450, 20));
		urlText.addKeyListener(urlTextListener);
        urlText.setEnabled(false);
		chooseFile = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Selection"));
		chooseFile.addActionListener(chooseFileListener);

		testConnection = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Test_URL"));
		testConnection.setEnabled(false);
		testConnection.addActionListener(testConnectionListener);// 测试连接按钮
		
		//ember:中英文两个按钮大小比较的结果不确定，做下判断
		if(testConnection.getPreferredSize().width > chooseFile.getPreferredSize().width) {
			chooseFile.setPreferredSize(testConnection.getPreferredSize());
		} else {
			testConnection.setPreferredSize(chooseFile.getPreferredSize());

		}
		
		createNorthPane();

		createEditingPane();
	}

	private void createNorthPane() {
		JPanel outnorth = new JPanel(new BorderLayout(0, 5));
		JPanel firstnorth = new JPanel(new BorderLayout(0, 5));
		JPanel northPane = new JPanel(new FlowLayout(FlowLayout.LEFT,7,0));
		northPane.add(localFileRadioButton);
		northPane.add(localText);
		northPane.add(chooseFile);
		firstnorth.add(northPane,BorderLayout.NORTH);
		infor1 = FRWidgetFactory.createLineWrapLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_JS_WARNING1",
																						 ProjectConstants.WEBAPP_NAME, ProjectConstants.WEBAPP_NAME));
		infor1.setForeground(new Color(207, 42, 39));
		firstnorth.add(infor1,BorderLayout.CENTER);

		JPanel secondnorth = new JPanel(new BorderLayout(0, 5));
		JPanel centerPane = new JPanel(new FlowLayout(FlowLayout.LEFT,7,0));
		centerPane.add(urlFileRadioButton);
		centerPane.add(urlText);
		centerPane.add(testConnection);
		secondnorth.add(centerPane,BorderLayout.NORTH);
		infor2 = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_JS_WARNING2", ProjectConstants.WEBAPP_NAME));
		infor2.setForeground(new Color(207, 42, 39));
		secondnorth.add(infor2,BorderLayout.CENTER);

		outnorth.add(firstnorth,BorderLayout.NORTH);
		outnorth.add(secondnorth,BorderLayout.CENTER);
		this.add(outnorth, BorderLayout.NORTH);
	}

	private void createEditingPane() {
		editingPane = new EditingStringListPane() {
			@Override
			protected String getAddOrEditString() {
				if (localFileRadioButton.isSelected()) {
					return localText.getText();
				} else {
					String url = urlText.getText();
					if (url.matches("^[a-zA-z]+://.+js")) {
						return url;
					} else {
						FineJOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(WebJsPane.this), com.fr.design.i18n.Toolkit.i18nText("Add_JS_warning"));
						return "";
					}
				}
			}

			@Override
			protected void selectedChanged(String selected) {
				if (selected == null) {
					localFileRadioButton.doClick();
					localText.setText("");
					return;
				}
				if (selected.matches("^[a-zA-z]+://.+js")) {
					urlFileRadioButton.doClick();
					urlText.setText(selected);
				} else {
					localFileRadioButton.doClick();
					localText.setText(selected);
				}
				checkEnableState();
			}
		};
		this.add(editingPane, BorderLayout.CENTER);
	}

	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ReportServerP_Import_JavaScript");
	}

	private ActionListener chooseFileListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			FILEChooserPane fileChooser = FILEChooserPane.getInstance(false, false, true,
					new ChooseFileFilter("js", "javascript" + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_File")));

			if (fileChooser.showOpenDialog(DesignerContext.getDesignerFrame()) == FILEChooserPane.OK_OPTION) {
				final FILE file = fileChooser.getSelectedFILE();
				if (file == null) {// 选择的文件不能是 null
					return;
				}

				String fileName = file.getName();
				String fileType = fileName.substring(fileName.lastIndexOf(CoreConstants.DOT) + 1);
				if (!"js".equalsIgnoreCase(fileType)) {
					return;
				}
				localText.setText(file.getPath());
                editingPane.setAddEnabled(true);
			}

			fileChooser.removeFILEFilter(new ChooseFileFilter("js"));
		}
	};

	private ActionListener testConnectionListener = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			String uri = urlText.getText();
			if (!uri.matches("^[a-zA-z]+://.+js")) {
				FineJOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(WebJsPane.this), com.fr.design.i18n.Toolkit.i18nText("Add_JS_warning"));
				return;
			}
			InputStream in = null;
			try {
				URL url = new URL(urlText.getText());
				URLConnection connection = url.openConnection();
				in = connection.getInputStream();
			} catch (Throwable e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
			}
			if (in == null) {
				FineJOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(),
												  com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Database_Connection_Failed"),
												  com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Failed"),
												  JOptionPane.ERROR_MESSAGE);
			} else {
				FineJOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Datasource_Connection_Successfully"));
				try {
					in.close();
				} catch (IOException e) {
					in = null;
				}
			}
		}
	};

	private ActionListener radioActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (localFileRadioButton.isSelected()) {
				localRadioSelectAction();
				urlFileRadioButton.setForeground(new Color(143, 142, 139));
				localFileRadioButton.setForeground(Color.black);
				infor1.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_JS_WARNING1", ProjectConstants.WEBAPP_NAME, ProjectConstants.WEBAPP_NAME));
				infor2.setText(" ");
			} else if (urlFileRadioButton.isSelected()) {
				urlRadioSelectAction();
				localFileRadioButton.setForeground(new Color(143, 142, 139));
				urlFileRadioButton.setForeground(Color.black);
				infor2.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_JS_WARNING2", ProjectConstants.WEBAPP_NAME));
				infor1.setText(" ");
			}
			if (StringUtils.isEmpty(urlText.getText()) && StringUtils.isEmpty(localText.getText())) {
				editingPane.setAddEnabled(false);
			}
		}
	};

	private KeyListener urlTextListener = new KeyAdapter() {

		@Override
		public void keyReleased(KeyEvent e) {
			String url = urlText.getText();
			if (url != null && url.matches("^[a-zA-z]+://.+js")) {
				editingPane.setAddEnabled(true);
			}
		}

	};

	private void localRadioSelectAction() {
		localFileRadioButton.setSelected(true);
		chooseFile.setEnabled(true);
        localText.setEnabled(true);
		urlText.setText("");
		urlText.setEnabled(false);
		testConnection.setEnabled(false);
	}

	private void urlRadioSelectAction() {
		urlFileRadioButton.setSelected(true);
		testConnection.setEnabled(true);
        urlText.setEnabled(true);
		localText.setText("");
        localText.setEnabled(false);
		chooseFile.setEnabled(false);
	}

	public void populate(ReportWebAttr reportWebAttr) {
		if (reportWebAttr == null) {
			editingPane.populateBean(new ArrayList<String>());
			return;
		}
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < reportWebAttr.getJSImportCount(); i++) {
			if (StringUtils.isNotBlank(reportWebAttr.getJSImport(i))) {
				list.add(reportWebAttr.getJSImport(i));
			}
		}
		editingPane.populateBean(list);
	}

	public void update(ReportWebAttr reportWebAttr) {
		List<String> valueList = editingPane.updateBean();
		reportWebAttr.clearJSImportList();
		for (int i = 0; i < valueList.size(); i++) {
			String a = valueList.get(i);
			reportWebAttr.addJSImport(a);
		}
	}

}
