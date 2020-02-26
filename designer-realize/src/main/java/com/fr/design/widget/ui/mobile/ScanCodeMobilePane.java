package com.fr.design.widget.ui.mobile;

import com.fr.base.mobile.MobileScanCodeAttr;
import com.fr.base.mobile.TextInputMode;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ibutton.ModeButtonGroup;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.widget.mobile.WidgetMobilePane;
import com.fr.form.ui.TextEditor;
import com.fr.form.ui.Widget;

import javax.swing.*;
import java.awt.*;

public class ScanCodeMobilePane extends WidgetMobilePane {

    private ModeButtonGroup<TextInputMode> buttonGroup;

    @Override
    protected void init() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.add(getMobileSettingPane(), BorderLayout.NORTH);
    }

    private UIExpandablePane getMobileSettingPane() {
        JPanel panel = FRGUIPaneFactory.createVerticalFlowLayout_Pane(true, FlowLayout.LEADING, 0, 10);
        buttonGroup = new ModeButtonGroup<>();
        UIRadioButton scanCodeAndManualInput = new UIRadioButton(
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design__Mobile_Support_Scan_Code_And_Manual_Input"), true);
        UIRadioButton onlyManualInput = new UIRadioButton(
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Only_Support_Manual_Input"), false);
        UIRadioButton onlyScanCodeInput = new UIRadioButton(
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Only_Support_Scan_Code_Input"), false);
        scanCodeAndManualInput.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        onlyManualInput.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        onlyScanCodeInput.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonGroup.put(TextInputMode.SUPPORT_SCAN_CODE_And_MANUAL, scanCodeAndManualInput);
        buttonGroup.put(TextInputMode.ONLY_SUPPORT_MANUAL, onlyManualInput);
        buttonGroup.put(TextInputMode.ONLY_SUPPORT_SCAN_CODE, onlyScanCodeInput);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel.add(scanCodeAndManualInput);
        panel.add(onlyManualInput);
        panel.add(onlyScanCodeInput);
        final JPanel panelWrapper = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panelWrapper.add(panel, BorderLayout.NORTH);
        return new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Advanced"), 280, 20,
                                    panelWrapper);
    }

    @Override
    public void populate(Widget widget) {
        MobileScanCodeAttr mobileScanCodeAttr = ((TextEditor) widget).getMobileScanCodeAttr();
        buttonGroup.setSelectButton(mobileScanCodeAttr.getTextInputMode());
    }

    @Override
    public void update(Widget widget) {
        MobileScanCodeAttr mobileScanCodeAttr = ((TextEditor) widget).getMobileScanCodeAttr();
        mobileScanCodeAttr.setTextInputMode(buttonGroup.getCurrentSelected());
    }


}
