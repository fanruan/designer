package com.fr.design.widget.ui.designer.mobile;

import com.fr.base.mobile.MobileScanCodeAttr;
import com.fr.base.mobile.ScanCodeState;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.FormDesigner;
import com.fr.form.ui.TextEditor;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class ScanCodeMobileDefinePane extends MobileWidgetDefinePane {

    private XCreator xCreator;
    private UICheckBox appScanCodeCheck;

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
        JPanel panel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        appScanCodeCheck = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Support_Scan_Code"), true);
        appScanCodeCheck.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel.add(appScanCodeCheck);
        final JPanel panelWrapper = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panelWrapper.add(panel, BorderLayout.NORTH);
        return new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Advanced"), 280, 20, panelWrapper);
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
        ScanCodeState scanCodeState = mobileScanCodeAttr.getScanCodeState();
        appScanCodeCheck.setSelected(scanCodeState.getState());
        this.bindListeners2Widgets();
    }

    @Override
    public void update() {
        MobileScanCodeAttr mobileScanCodeAttr = ((TextEditor) xCreator.toData()).getMobileScanCodeAttr();
        mobileScanCodeAttr.setScanCodeState(ScanCodeState.parse(appScanCodeCheck.isSelected()));
        DesignerContext.getDesignerFrame().getSelectedJTemplate().fireTargetModified();
    }

}
