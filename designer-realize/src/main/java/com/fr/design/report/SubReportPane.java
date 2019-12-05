package com.fr.design.report;

import com.fr.base.core.KV;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.file.FILEFactory;
import com.fr.main.TemplateWorkBook;
import com.fr.main.impl.LinkWorkBookTemplate;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.cellattr.core.SubReport;
import com.fr.report.elementcase.ElementCase;
import com.fr.stable.StringUtils;
import com.fr.stable.project.ProjectConstants;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SubReportPane extends BasicPane {
	private UITextField pathTextField;
	private FILE chooseFILE = null;
	private ReportletParameterViewPane kvPane = null;
	private UICheckBox extend;
	
	public SubReportPane() {
		this.initComponents();
	}

    protected void initComponents() {
    	this.setLayout(FRGUIPaneFactory.createM_BorderLayout());
        JPanel northPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
//        northPane.setLayout(FRGUIPaneFactory.createM_BorderLayout());
		northPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Sub_Report_Path"),null));
        northPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Location") + ":"), BorderLayout.WEST);
        northPane.add(pathTextField = new UITextField(), BorderLayout.CENTER);
		pathTextField.setEditable(false);
		UIButton browseButton = new UIButton("...");
		northPane.add(browseButton, BorderLayout.EAST);
		browseButton.setPreferredSize(new java.awt.Dimension(20, 20));
		browseButton.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Click_this_button"));
		
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FILEChooserPane fileChooser = FILEChooserPane.getInstance(true, false);
				int chooseResult = fileChooser.showOpenDialog(SubReportPane.this);
				if(chooseResult == FILEChooserPane.OK_OPTION ||chooseResult == FILEChooserPane.JOPTIONPANE_OK_OPTION ) {
					
					chooseFILE = fileChooser.getSelectedFILE();
					if (chooseFILE != null && chooseFILE.exists()) {
						pathTextField.setText(chooseFILE.prefix() + chooseFILE.getPath());
					} else {
						FineJOptionPane.showConfirmDialog(SubReportPane.this, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Sub_Report_Message1"),
								com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Sub_Report_ToolTips"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			    		chooseFILE = null;
						pathTextField.setText("");
					}
		        }
			}
		});
        this.add(northPane, BorderLayout.NORTH);
        
        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(centerPane, BorderLayout.CENTER);
		centerPane.setLayout(FRGUIPaneFactory.createM_BorderLayout());
		centerPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));
        kvPane = new ReportletParameterViewPane();
		centerPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Set_Delivery_Parameter"),null));
		JPanel kcPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        kcPane.add(kvPane);
        extend = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Hyperlink_Extends_Report_Parameters"));
        kcPane.add(extend, BorderLayout.SOUTH);
        centerPane.add(kcPane, BorderLayout.CENTER);
		UITextArea description = new UITextArea(2, 1);
		centerPane.add(description, BorderLayout.SOUTH);
		description.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Sub_Report_Description"));
		description.setEditable(false);
		description.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Attention"), null));
    }
    
    @Override
    protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Insert_Sub_Report");
	}

    public void populate(ElementCase report, CellElement cellElment) {
    	Object cellValue = cellElment.getValue();
    	if (cellValue != null && cellValue instanceof SubReport) {
    		TemplateWorkBook rt = ((SubReport)cellValue).getPackee();
    		if (rt != null && rt instanceof LinkWorkBookTemplate) {
    			String path = ((LinkWorkBookTemplate)rt).getTemplatePath();
    			if (StringUtils.isNotBlank(path)) {
    				pathTextField.setText(FILEFactory.ENV_PREFIX
    						+ ProjectConstants.REPORTLETS_NAME + File.separator + path);
    			}
    		}
    		KV[] kv = ((SubReport)cellValue).getParameterKVS();
    		kvPane.populate(kv);
    		extend.setSelected(((SubReport)cellValue).isExtendOwnerParameters());
    	}
    }

    public SubReport update() {
    	// check 的时候已经判别过了
//    	if (StringUtils.isNotBlank(pathTextField.getText())) {
    		LinkWorkBookTemplate linkTemplate = new LinkWorkBookTemplate();
    		int length = "env://".length() + ProjectConstants.REPORTLETS_NAME.length() + 1;
    		String path = pathTextField.getText().substring(length);
//    		if (path.startsWith(ProjectConstants.REPORTLETS_NAME)) {
//		    	path = path.substring(ProjectConstants.REPORTLETS_NAME.length() + 1, path.length());
//    		}
    		linkTemplate.setTemplatePath(path);
    		SubReport subReport = new SubReport(linkTemplate);
    		subReport.setParameterKVS(kvPane.updateKV());
    		subReport.setExtendOwnerParameters(extend.isSelected());
    		return subReport;
//    	} else {
//    		return null;
//    	}
    }
    
    protected boolean checkFILE() {
    	// TODO ALEX_SEP 子报表都要删了,这个还???
//    	if (StringUtils.isBlank(pathTextField.getText())) {
//    		JOptionPane.showConfirmDialog(this, com.fr.design.i18n.Toolkit.i18nText("Sub_Report_Message1"),
//					com.fr.design.i18n.Toolkit.i18nText("Sub_Report_ToolTips"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
//    		return false;
//    	// 确保父报表存在，主要是防止父报表不再当前运行环境中
//    	}else if (!ReportDeziUtils.getEditingReportInternalFrame().getEditingFILE().exists()) {
//    		JOptionPane.showConfirmDialog(this, com.fr.design.i18n.Toolkit.i18nText("Sub_Report_Message2"),
//    				com.fr.design.i18n.Toolkit.i18nText("Sub_Report_ToolTips"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
//    		return false;
//    	// carl:父子报表不能相同
//    	} else if(ReportDeziUtils.getEditingReportInternalFrame().getEditingFILE().getPath().equals(chooseFILE.getPath())) {
//    		JOptionPane.showConfirmDialog(this, com.fr.design.i18n.Toolkit.i18nText("Sub_Report_Message3"),
//    				com.fr.design.i18n.Toolkit.i18nText("Sub_Report_ToolTips"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
//    		return false;
//    	} else {
    		return true;
//    	}
    }

}
