package com.fr.design.webattr.printsettings;

import com.fr.base.print.NativePrintAttr;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
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
    // 服务器配置面板特有的组件
    private UICheckBox defaultDownloadUrlCheck;  // 采用默认的软件下载地址
    private UITextField customUrlFieldWin;
    private UITextField customUrlFieldMac;


    @Override
    JPanel createHeaderPane(Component... comps) {

        Component[] newComps = {
                getDownloadUrlSettingPane()
        };
        Component[] allComps = new Component[comps.length + newComps.length];
        System.arraycopy(comps, 0, allComps, 0, comps.length);
        System.arraycopy(newComps, 0, allComps, comps.length, newComps.length);

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
    }

    @Override
    protected void extraPopulate(NativePrintAttr nativePrintAttr) {
        defaultDownloadUrlCheck.setSelected(nativePrintAttr.isUseDefaultDownloadUrl());
        customUrlFieldMac.setText(nativePrintAttr.getCustomDownloadUrlMac());
        customUrlFieldWin.setText(nativePrintAttr.getCustomDownloadUrlWin());
    }

    private JPanel getDownloadUrlSettingPane() {
        defaultDownloadUrlCheck = GUICoreUtils.createNoBorderCheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Default"));
        JPanel downloadUrlSettingCheckPane = GUICoreUtils.createCheckboxAndDynamicPane(defaultDownloadUrlCheck, getCustomUrlSettingPane(), true);
        downloadUrlSettingCheckPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        // TableLayout
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p};
        double[] columnSize = {p, p};
        Component[][] components = {
                {getTopAlignLabelPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Software_Download_Url") + ": "), downloadUrlSettingCheckPane}
        };
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 0, 15);
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
        urlSettingPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        return urlSettingPane;
    }
}
