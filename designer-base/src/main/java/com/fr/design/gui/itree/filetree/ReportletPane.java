package com.fr.design.gui.itree.filetree;

import com.fr.base.FRContext;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.file.filetree.IOFileNodeFilter;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReportletPane extends BasicPane {
    private TemplateFileTree templateReportletTree;
    private JScrollPane t_panel;
    private ClassFileTree classReportletTree;
    private JScrollPane c_panel;

    private UIButton switchButton;
    private CardLayout card;
    private JPanel cardPane;

    public ReportletPane() {
        this.setLayout(FRGUIPaneFactory.createM_BorderLayout());
        // Description
        UITextArea textPane = new UITextArea();
        this.add(textPane, BorderLayout.NORTH);
        textPane.setEditable(false);
        textPane.setLineWrap(true);

        textPane.setFont(FRContext.getDefaultValues().getFRFont().deriveFont(Font.BOLD, 12));
        textPane.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Schedule_The_Selected_File_Must_Be_End_With_Filter"));

        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(centerPane, BorderLayout.CENTER);

        switchButton = new UIButton("switch");
        centerPane.add(GUICoreUtils.createBorderPane(switchButton, BorderLayout.WEST), BorderLayout.NORTH);
        switchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                switchCardPane(t_panel.isVisible());
            }
        });

        cardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        centerPane.add(cardPane, BorderLayout.CENTER);
        cardPane.setLayout(card = new CardLayout());
        templateReportletTree = new TemplateFileTree();
        IOFileNodeFilter filter = new IOFileNodeFilter(new String[]{".cpt", ".class",".frm",".form"});
        templateReportletTree.setFileNodeFilter(filter);
        cardPane.add(t_panel = new JScrollPane(templateReportletTree), "TEMPLATE");
        classReportletTree = new ClassFileTree();
        cardPane.add(c_panel = new JScrollPane(classReportletTree), "CLASS");
    
        this.refreshEnv();
    }

    /*
     * 切换CardPane
     */
    private void switchCardPane(boolean switch2Class) {
        if (switch2Class) {
            card.show(cardPane, "CLASS");
            switchButton.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Switch_To_Template_Reportlet"));
        } else {
            card.show(cardPane, "TEMPLATE");
            switchButton.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Switch_To_Class_Reportlet"));
        }
    }

    /**
     * 检查是否符合规范
     *
     * @throws Exception  抛错
     */
    public void checkValid() throws Exception {
        String path = this.getSelectedReportletPath();
        if (path == null) {
            throw new Exception(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Function_The_Selected_File_Cannot_Be_Null"));
        }
    }


    /**
     * 刷新Env
     */
    public void refreshEnv() {
    
        this.templateReportletTree.refreshEnv();
        this.classReportletTree.refreshEnv();
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Reportlet");
    }

    /*
     * 返回选中的Reportlet的路径
     */
    public String getSelectedReportletPath() {
        if (t_panel.isVisible()) {
            return templateReportletTree.getSelectedTemplatePath();
        } else if (c_panel.isVisible()) {
            return classReportletTree.getSelectedClassPath();
        }

        return null;
    }

    /*
     * 选中某Reportlet
     */
    public void setSelectedReportletPath(String reportletPath) {
        if (reportletPath == null) {
            return;
        }

        if (reportletPath.endsWith(".class")) {
            switchCardPane(true);
            classReportletTree.setSelectedClassPath(reportletPath);
        } else {
            switchCardPane(false);
            templateReportletTree.setSelectedTemplatePath(reportletPath);
        }
    }
}