package com.fr.design.webattr.printsettings;

import com.fr.base.print.PrintSettingsAttrMark;
import com.fr.design.dialog.AbstractTemplateServerSettingPane;
import com.fr.report.core.ReportUtils;

import javax.swing.JPanel;

/**
 * 模版->打印设置
 * Created by plough on 2018/3/6.
 */
public class ReportPrintSettingPane extends AbstractTemplateServerSettingPane {

    private PrintSettingPane printSettingPane;

    public ReportPrintSettingPane() {
    }

    @Override
    protected JPanel getContentPane() {
        if (printSettingPane == null) {
            printSettingPane = new PrintSettingPane();
        }
        return printSettingPane;
    }

    @Override
    protected void checkContentPaneEnabled() {
        printSettingPane.checkEnabled();
    }

    protected void populateServerSettings() {
        PrintSettingsAttrMark printSettings = ReportUtils.getPrintSettingsFromServerConfig();
        printSettingPane.populate(printSettings);
    }

    public void populate(PrintSettingsAttrMark printSettings) {
        if (!printSettings.isValid()) {  // 采用服务器配置
            chooseComboBox.setSelectedIndex(SERVER_SET);
            populateServerSettings();
            return;
        }
        chooseComboBox.setSelectedIndex(SINGLE_SET);
        printSettingPane.populate(printSettings);
    }

    public PrintSettingsAttrMark updateBean() {
        PrintSettingsAttrMark printSettings = printSettingPane.updateBean();
        if (chooseComboBox.getSelectedIndex() == SERVER_SET) {
            printSettings.setValid(false);
        }
        return printSettings;
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Print_Setting");
    }
}
