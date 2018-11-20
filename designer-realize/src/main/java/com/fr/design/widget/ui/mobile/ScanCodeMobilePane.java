package com.fr.design.widget.ui.mobile;

import com.fr.base.mobile.MobileScanCodeAttr;
import com.fr.base.mobile.ScanCodeState;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.widget.mobile.WidgetMobilePane;
import com.fr.form.ui.TextEditor;
import com.fr.form.ui.Widget;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class ScanCodeMobilePane extends WidgetMobilePane {

    private UICheckBox appScanCodeCheck;

    protected void init() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.add(getMobileSettingPane(), BorderLayout.NORTH);
    }

    private UIExpandablePane getMobileSettingPane() {
        JPanel panel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        appScanCodeCheck = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Support_Scan_Code"), true);
        appScanCodeCheck.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel.add(appScanCodeCheck);
        final JPanel panelWrapper = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panelWrapper.add(panel, BorderLayout.NORTH);
        return new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Advanced"), 280, 20, panelWrapper);
    }

    @Override
    public void populate(Widget widget) {
        MobileScanCodeAttr mobileScanCodeAttr = ((TextEditor) widget).getMobileScanCodeAttr();
        ScanCodeState scanCodeState = mobileScanCodeAttr.getScanCodeState();
        appScanCodeCheck.setSelected(scanCodeState2boolean(scanCodeState));
    }

    @Override
    public void update(Widget widget) {
        MobileScanCodeAttr mobileScanCodeAttr = ((TextEditor) widget).getMobileScanCodeAttr();
        mobileScanCodeAttr.setScanCodeState(boolean2ScanCodeState(appScanCodeCheck.isSelected()));
    }

    private ScanCodeState boolean2ScanCodeState(boolean scanCodeCheck) {
        if (scanCodeCheck) {
            return ScanCodeState.SUPPORT_SCAN_CODE;
        } else {
            return ScanCodeState.NOT_SUPPORT_SCAN_CODE;
        }
    }

    private boolean scanCodeState2boolean(ScanCodeState scanCodeState) {
        return scanCodeState == ScanCodeState.SUPPORT_SCAN_CODE;
    }
}
