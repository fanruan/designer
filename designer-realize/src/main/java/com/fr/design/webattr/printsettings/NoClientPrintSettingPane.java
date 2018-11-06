package com.fr.design.webattr.printsettings;

import com.fr.base.print.NoClientPrintAttr;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * 零客户端打印设置面板
 * Created by plough on 2018/3/5.
 */
public class NoClientPrintSettingPane extends JPanel {
    private UICheckBox setMarginWhenPrintCheck;
    private UICheckBox ieQuietPrintCheck;
    private UICheckBox needlePrinterOptimizeCheck;
    private UICheckBox inheritPageMarginSettingCheck;  // 继承页面边距设置
    private PageMarginSettingPane pageMarginSettingPane;
    private JPanel centerPane;

    NoClientPrintSettingPane() {
        initComponents();
        initListeners();
    }

    private void initComponents() {
        JPanel printPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        printPane.add(createHeaderPane(), BorderLayout.NORTH);

        initCenterPane();
        printPane.add(centerPane, BorderLayout.CENTER);

        this.setLayout(new BorderLayout());
        this.add(printPane, BorderLayout.CENTER);
    }

    private void initCenterPane() {
        centerPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Default_Settings"));

        inheritPageMarginSettingCheck = GUICoreUtils.createNoBorderCheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Inherit_Page_Margin_Setting"));
        pageMarginSettingPane = new PageMarginSettingPane();
        pageMarginSettingPane.setBorder(BorderFactory.createEmptyBorder(10, -10, 0, 0));
        JPanel pageMarginCheckPane = GUICoreUtils.createCheckboxAndDynamicPane(inheritPageMarginSettingCheck, pageMarginSettingPane, true);

        // TableLayout
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p};
        double[] columnSize = {60, p};
        Component[][] components = {
                {getTopAlignLabelPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Margin") + ":"), pageMarginCheckPane}
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 0, 15);

        centerPane.add(panel);
    }

    private JPanel createHeaderPane() {
        setMarginWhenPrintCheck = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Set_Margin_When_Printing"));
        setMarginWhenPrintCheck.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        UILabel marginTip = GUICoreUtils.createTipLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Tip_Use_Default_Print_Margin"));
        JPanel setMarginWhenPrintPane =  GUICoreUtils.createFlowPane(new Component[] {
                setMarginWhenPrintCheck, marginTip}, FlowLayout.LEFT);

        ieQuietPrintCheck = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_No_Print_Settings_In_IE"));
        ieQuietPrintCheck.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        needlePrinterOptimizeCheck = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Needle_Printer_Optimize"));
        needlePrinterOptimizeCheck.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        UILabel needleTip = GUICoreUtils.createTipLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Needle_Printer_Optimize_Tip"));
        JPanel needlePrinterOptimizePane =  GUICoreUtils.createFlowPane(new Component[] {
                needlePrinterOptimizeCheck, needleTip}, FlowLayout.LEFT);

        JPanel headerPane = GUICoreUtils.createHeaderLayoutPane(setMarginWhenPrintPane, ieQuietPrintCheck, needlePrinterOptimizePane);
        headerPane.setBorder(BorderFactory.createEmptyBorder(2, 12, 12, 0));
        return headerPane;
    }



    private void initListeners() {
        setMarginWhenPrintCheck.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                checkEnabled();
            }
        });
    }

    // 返回包含一个标签的 panel，标签始终位于 panel 顶部
    private JPanel getTopAlignLabelPane(String labelText) {
        JPanel labelPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        labelPane.add(new UILabel(labelText), BorderLayout.NORTH);
        return labelPane;
    }

    public void populate(NoClientPrintAttr noClientPrintAttr) {
        setMarginWhenPrintCheck.setSelected(noClientPrintAttr.isSetMarginOnPrint());
        ieQuietPrintCheck.setSelected(noClientPrintAttr.isIeQuietPrint());
        needlePrinterOptimizeCheck.setSelected(noClientPrintAttr.isNeedlePrinterOptimize());
        inheritPageMarginSettingCheck.setSelected(noClientPrintAttr.isInheritPageMarginSetting());
        pageMarginSettingPane.populate(noClientPrintAttr.getMargin());
    }

    public void update(NoClientPrintAttr noClientPrintAttr) {
        noClientPrintAttr.setSetMarginOnPrint(setMarginWhenPrintCheck.isSelected());
        noClientPrintAttr.setIeQuietPrint(ieQuietPrintCheck.isSelected());
        noClientPrintAttr.setNeedlePrinterOptimize(needlePrinterOptimizeCheck.isSelected());
        noClientPrintAttr.setInheritPageMarginSetting(inheritPageMarginSettingCheck.isSelected());
        noClientPrintAttr.setMargin(pageMarginSettingPane.updateBean());
    }

    // 刷新面板可用状态
    public void checkEnabled() {
        GUICoreUtils.setEnabled(centerPane, !setMarginWhenPrintCheck.isSelected());
    }
}
