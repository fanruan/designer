package com.fr.design.widget.ui.designer.mobile;

import com.fr.base.mobile.MobileScanCodeAttr;
import com.fr.base.mobile.TextInputMode;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.ibutton.ModeButtonGroup;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.FormDesigner;
import com.fr.form.ui.TextEditor;

import javax.swing.*;
import java.awt.*;

public class ScanCodeMobileDefinePane extends MobileWidgetDefinePane {

    private XCreator xCreator;
    private ModeButtonGroup<TextInputMode> buttonGroup;

    public ScanCodeMobileDefinePane(XCreator xCreator) {
        this.xCreator = xCreator;
    }

    @Override
    public void initPropertyGroups(Object source) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel mobileSettingsPane = getMobileSettingsPane();
        this.add(mobileSettingsPane, BorderLayout.NORTH);
        this.repaint();
    }

    private UIExpandablePane getMobileSettingsPane() {
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
        buttonGroup.add(scanCodeAndManualInput);
        buttonGroup.add(onlyManualInput);
        buttonGroup.add(onlyScanCodeInput);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel.add(scanCodeAndManualInput);
        panel.add(onlyManualInput);
        panel.add(onlyScanCodeInput);
        final JPanel panelWrapper = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panelWrapper.add(panel, BorderLayout.NORTH);
        return new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Advanced"), 280, 20,
                                    panelWrapper);
    }

    private void bindListeners2Widgets() {
        reInitAllListeners();
        AttributeChangeListener changeListener = new AttributeChangeListener() {
            @Override
            public void attributeChange() {
                update();
            }
        };
        this.addAttributeChangeListener(changeListener);
    }

    private void reInitAllListeners() {
        initListener(this);
    }

    @Override
    public void populate(FormDesigner designer) {
        MobileScanCodeAttr mobileScanCodeAttr = ((TextEditor) xCreator.toData()).getMobileScanCodeAttr();
        buttonGroup.setSelectButton(mobileScanCodeAttr.getTextInputMode());
        this.bindListeners2Widgets();
    }

    @Override
    public void update() {
        MobileScanCodeAttr mobileScanCodeAttr = ((TextEditor) xCreator.toData()).getMobileScanCodeAttr();
        mobileScanCodeAttr.setTextInputMode(buttonGroup.getCurrentSelected());
        DesignerContext.getDesignerFrame().getSelectedJTemplate().fireTargetModified();
    }

}
