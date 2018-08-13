/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.webattr;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.editor.editor.LongEditor;

import com.fr.stable.StringUtils;
import com.fr.web.attr.ReportWebAttr;

public class CommonPane extends JPanel {
    private UITextField titleTextField;

    private LongEditor cacheValidateTimeEditor;

    public CommonPane() {

        this.initComponents();
    }

    protected void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(6, 2, 4, 2));

        JPanel northPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        this.add(northPane,BorderLayout.NORTH);

        this.titleTextField = new UITextField(24);
        this.cacheValidateTimeEditor = new LongEditor();

        JComponent[][] comps = {
				{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Title") + ":"), this.titleTextField, null},
				{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Cache_Validate_Time") + ":"), this.cacheValidateTimeEditor, new UILabel("milliseconds")}
		};

        this.add(
        		TableLayoutHelper.createCommonTableLayoutPane(
						comps,
						new double[]{TableLayout.PREFERRED, TableLayout.PREFERRED},
						new double[]{TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED},
						4),
        		BorderLayout.CENTER);
    }

    public void populate(ReportWebAttr reportWebAttr) {
        if (reportWebAttr.getTitle() != null && reportWebAttr.getTitle().length() > 0) {
            this.titleTextField.setText(reportWebAttr.getTitle());
        }

        this.cacheValidateTimeEditor.setValue(Long.valueOf(reportWebAttr.getCacheValidateTime()));
    }

    public void update(ReportWebAttr reportWebAttr) {
        if (!StringUtils.isEmpty(this.titleTextField.getText())) {
            reportWebAttr.setTitle(this.titleTextField.getText());
        } else {
        	 reportWebAttr.setTitle(null);
        }
        
        reportWebAttr.setCacheValidateTime(this.cacheValidateTimeEditor.getValue().longValue());
    }
}
