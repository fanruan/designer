package com.fr.design.webattr.printsettings;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.base.print.PrintSettingsAttrMark;
import com.fr.report.core.ReportUtils;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * 模版->打印设置
 * Created by plough on 2018/3/6.
 */
public class ReportPrintSettingPane extends BasicPane {
    private static final String[] CHOOSEITEM = new String[] {
            Inter.getLocText("FR-Designer_I_Want_To_Set_Single"),
            Inter.getLocText("FR-Designer_Using_Server_Report_View_Settings")
    };
    private static final int SINGLE_SET = 0;
    private static final int SERVER_SET = 1;

    private UIComboBox chooseComboBox;
    private PrintSettingPane printSettingPane;

    public ReportPrintSettingPane() {
        initComponents();
    }

    private void initComponents() {
        chooseComboBox = new UIComboBox(CHOOSEITEM);
        chooseComboBox.addItemListener(itemListener);
        UILabel belowSetLabel = new UILabel(Inter.getLocText("FR-Designer_Blow_set") + ":");
        JPanel buttonPane = GUICoreUtils.createFlowPane(new Component[] {
                belowSetLabel, chooseComboBox}, FlowLayout.LEFT, 0, 0);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 0));

        printSettingPane = new PrintSettingPane();
        this.setLayout(new BorderLayout());
        this.add(buttonPane, BorderLayout.NORTH);
        this.add(printSettingPane, BorderLayout.CENTER);
    }

    private ItemListener itemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (chooseComboBox.getSelectedIndex() == 0) {
                    checkEnabled(true);
                } else {
                    populateServerSettings();
                    checkEnabled(false);
                }
            }
        }
    };

    private void checkEnabled(boolean isSelected) {
        GUICoreUtils.setEnabled(printSettingPane, isSelected);
    }

    private void populateServerSettings() {
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
        return Inter.getLocText("FR-Designer_Print_Setting");
    }
}
