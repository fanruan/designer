package com.fr.design.webattr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;

import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.fr.base.FRContext;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.frpane.EditingStringListPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.dialog.BasicPane;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;
import com.fr.stable.StringUtils;
import com.fr.web.attr.ReportWebAttr;

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

		localFileRadioButton = new UIRadioButton(Inter.getLocText("Disk_File") + ":", true);
		urlFileRadioButton = new UIRadioButton(Inter.getLocText("Url_location")+ ":", false);
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
		chooseFile = new UIButton(Inter.getLocText("Selection"));
		chooseFile.addActionListener(chooseFileListener);

		testConnection = new UIButton(Inter.getLocText("Test_URL"));
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
		infor1 = new UILabel(Inter.getLocText("JS_WARNING1"));
		infor1.setForeground(new Color(207, 42, 39));
		firstnorth.add(infor1,BorderLayout.CENTER);

		JPanel secondnorth = new JPanel(new BorderLayout(0, 5));
		JPanel centerPane = new JPanel(new FlowLayout(FlowLayout.LEFT,7,0));
		centerPane.add(urlFileRadioButton);
		centerPane.add(urlText);
		centerPane.add(testConnection);
		secondnorth.add(centerPane,BorderLayout.NORTH);
		infor2 = new UILabel(Inter.getLocText("JS_WARNING2"));
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
						JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(WebJsPane.this), Inter.getLocText("Add_JS_warning"));
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
		return Inter.getLocText("ReportServerP-Import_JavaScript");
	}

	private ActionListener chooseFileListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			FILEChooserPane fileChooser = FILEChooserPane.getInstance(false, false, true,
					new ChooseFileFilter("js", "javascript" + Inter.getLocText("File")));

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
                localText.setText(file.getPath().substring(1));
                editingPane.setAddEnabled(true);
			}

			fileChooser.removeFILEFilter(new ChooseFileFilter("js"));
		}
	};

	private ActionListener testConnectionListener = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			String uri = urlText.getText();
			if (!uri.matches("^[a-zA-z]+://.+js")) {
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(WebJsPane.this), Inter.getLocText("Add_JS_warning"));
				return;
			}
			InputStream in = null;
			try {
				URL url = new URL(urlText.getText());
				URLConnection connection = url.openConnection();
				in = connection.getInputStream();
			} catch (Throwable e) {
				FRContext.getLogger().error(e.getMessage(), e);
			}
			if (in == null) {
				JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("Datasource-Connection_failed"));
			} else {
				JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("Datasource-Connection_successfully"));
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
				infor1.setText(Inter.getLocText("JS_WARNING1"));
				infor2.setText(" ");
			} else if (urlFileRadioButton.isSelected()) {
				urlRadioSelectAction();
				localFileRadioButton.setForeground(new Color(143, 142, 139));
				urlFileRadioButton.setForeground(Color.black);
				infor2.setText(Inter.getLocText("JS_WARNING2"));
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