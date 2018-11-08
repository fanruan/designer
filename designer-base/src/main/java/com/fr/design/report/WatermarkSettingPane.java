package com.fr.design.report;

import com.fr.base.iofile.attr.WatermarkAttr;
import com.fr.design.dialog.AbstractTemplateServerSettingPane;
import com.fr.report.core.ReportUtils;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Created by plough on 2018/11/7.
 */
public class WatermarkSettingPane extends AbstractTemplateServerSettingPane {
    private WatermarkPane watermarkPane;

    public WatermarkSettingPane() {
        super();
        initComponents();
    }

    private void initComponents() {
        buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 8, 0, 0));
    }

    @Override
    protected JPanel getContentPane() {
        if (watermarkPane == null) {
            watermarkPane = new WatermarkPane();
        }
        return watermarkPane;
    }

    @Override
    protected void populateServerSettings() {
        WatermarkAttr watermarkAttr = ReportUtils.getWatermarkAttrFromServerConfig();
        watermarkPane.populate(watermarkAttr);
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_WaterMark");
    }

    public void populate(WatermarkAttr watermark) {
        if (!watermark.isValid()) {
            chooseComboBox.setSelectedIndex(SERVER_SET);
            populateServerSettings();
            return;
        }
        chooseComboBox.setSelectedIndex(SINGLE_SET);
        watermarkPane.populate(watermark);
    }


    public WatermarkAttr update() {
        WatermarkAttr watermark = watermarkPane.update();
        if (isUsingServerSettings()) {
            watermark.setValid(false);
        }
        return watermark;
    }
}
