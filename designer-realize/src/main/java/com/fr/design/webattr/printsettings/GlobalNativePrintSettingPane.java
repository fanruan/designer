package com.fr.design.webattr.printsettings;

import com.fr.base.print.NativePrintAttr;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UIIntNumberField;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.Component;

/**
 * 本地打印设置面板——全局
 * Created by plough on 2018/10/31.
 */
public class GlobalNativePrintSettingPane extends AbstractNativePrintSettingPane {
    private static final int PRINT_PORT_FIELD_COLUMNS = 8;

    // 服务器配置面板特有的组件
    private UICheckBox defaultDownloadUrlCheck;  // 采用默认的软件下载地址
    private UITextField customUrlFieldWin;
    private UITextField customUrlFieldMac;
    private UINumberField printPortField;  // 打印软件端口号


    @Override
    JPanel createHeaderPane(Component... comps) {
        Component[] allComps = new Component[comps.length + 1];
        System.arraycopy(comps, 0, allComps, 0, comps.length);
        allComps[comps.length] = getExtraSettingPane();

        JPanel headerPane = GUICoreUtils.createHeaderLayoutPane(allComps);
        headerPane.setBorder(BorderFactory.createEmptyBorder(2, 12, 5, 0));
        return headerPane;
    }


    @Override
    protected void extraUpdate(NativePrintAttr nativePrintAttr) {
        if (defaultDownloadUrlCheck.isSelected()) {
            nativePrintAttr.setUseDefaultDownloadUrl(true);
        } else {
            nativePrintAttr.setUseDefaultDownloadUrl(false);
            nativePrintAttr.setCustomDownloadUrlMac(customUrlFieldMac.getText());
            nativePrintAttr.setCustomDownloadUrlWin(customUrlFieldWin.getText());
        }
        nativePrintAttr.setPrintPort((int) printPortField.getValue());
    }

    @Override
    protected void extraPopulate(NativePrintAttr nativePrintAttr) {
        defaultDownloadUrlCheck.setSelected(nativePrintAttr.isUseDefaultDownloadUrl());
        customUrlFieldMac.setText(nativePrintAttr.getCustomDownloadUrlMac());
        customUrlFieldWin.setText(nativePrintAttr.getCustomDownloadUrlWin());
        printPortField.setValue(nativePrintAttr.getPrintPort());
    }

    // 服务器配置中，特有的设置面板
    private JPanel getExtraSettingPane() {
        // 软件下载地址
        defaultDownloadUrlCheck = GUICoreUtils.createNoBorderCheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Default"));
        JPanel downloadUrlSettingCheckPane = GUICoreUtils.createCheckboxAndDynamicPane(defaultDownloadUrlCheck, getCustomUrlSettingPane(), true);
        downloadUrlSettingCheckPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        JPanel downloadTipPane = getTopAlignLabelPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Software_Download_Url") + ": ");
        downloadTipPane.setBorder(BorderFactory.createEmptyBorder(0, 0, -6, 0));

        // 打印软件端口号
        UILabel printPortTip = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Native_Print_Port") + ": ");
        JPanel printPortFiledPane = getPrintPortFieldPane();

        // TableLayout
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p};
        double[] columnSize = {p, p};
        Component[][] components = {
            {
                downloadTipPane, downloadUrlSettingCheckPane
            }, {
                printPortTip, printPortFiledPane
            }
        };
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 0, 0);
    }

    private JPanel getPrintPortFieldPane() {
        printPortField = new UIIntNumberField();
        printPortField.setMaxValue(NativePrintAttr.MAX_PRINT_PORT_VALUE);
        printPortField.setColumns(PRINT_PORT_FIELD_COLUMNS);
        JPanel panel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        panel.add(printPortField);
        return panel;
    }

    private JPanel getCustomUrlSettingPane() {
        customUrlFieldWin = new UITextField(20);
        customUrlFieldMac = new UITextField(20);

        // TableLayout
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p};
        double[] columnSize = {60, p};
        Component[][] components = {
                {new UILabel("windows: "), customUrlFieldWin},
                {new UILabel("macOS: "), customUrlFieldMac}
        };
        JPanel urlSettingPane =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 0, 10);
        urlSettingPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        return urlSettingPane;
    }
}
