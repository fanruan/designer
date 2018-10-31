package com.fr.design.mainframe.menupane;

import com.fr.design.gui.ilable.UILabel;
import com.fr.log.FineLoggerFactory;
import com.fr.main.ReportFitAttr;
import com.fr.main.ReportFitConfig;
import com.fr.report.fun.ReportFitAttrProvider;

import java.awt.Component;

/**
 * Created by 夏翔 on 2016/6/24.
 */
public class TemplateBrowserFitAttrPane extends BrowserFitAttrPane {

    public TemplateBrowserFitAttrPane() {
        initComponents(ReportFitConfig.getInstance().getCptFitAttr());
    }


    @Override
    protected Component[][] initFitComponents() {
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Designer_Fit-Font")), fontFitRadio, null, fontNotFitRadio},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Designer_Fit-Element")), horizonRadio, doubleRadio, notFitRadio}
        };
        return components;
    }

    @Override
    public void populateBean(ReportFitAttrProvider attr) {
        //模板界面，自适应选项去掉了默认，这边的判断为了兼容之前的设置
        if (attr != null && attr.fitStateInPC() == 0) {
            attr.setFitStateInPC(3);
        }
        ReportFitAttrProvider fitAttr = ReportFitConfig.getInstance().getCptFitAttr();
        if (attr == null) {
            //如果为空, 就用全局的
            attr = fitAttr;
            populateGlobalComponents();
        } else if (fitAttr.fitStateInPC() == 0) {
            attr = new ReportFitAttr();
            attr.setFitStateInPC(3);
            initBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Designer_Fit-Local"));
        } else {
            initBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Designer_Fit-Local"));
        }
        this.localFitAttr = attr;
        fontRadioGroup.selectFontFit((attr).isFitFont());
        fitRadionGroup.selectIndexButton(attr.fitStateInPC());
        fitPreviewPane.refreshPreview(getCurrentFitOptions(), fitRadionGroup.isEnabled());
    }

    @Override
    public ReportFitAttrProvider updateBean() {
        ReportFitAttr attr = new ReportFitAttr();
        attr.setFitFont(fontRadioGroup.isFontFit());
        attr.setFitStateInPC(fitRadionGroup.getSelectRadioIndex());

        // 直接用全局的
        if (globalCheck.isSelected()) {
            updateGlobalConfig(attr);
            return null;
        }
        this.localFitAttr = attr;
        return attr;
    }

    private void updateGlobalConfig(ReportFitAttr attr) {
        try {
            ReportFitConfig manager = ReportFitConfig.getInstance();
            manager.setCptFitAttr(attr);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }
    }
}
