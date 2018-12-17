package com.fr.design.mainframe.mobile.ui;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.gui.ibutton.UIColorButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.MobileTabFontConfPane;
import com.fr.design.style.color.NewColorSelectBox;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.general.cardtag.mobile.MobileTemplateStyle;
import com.fr.general.cardtag.mobile.TabFontConfig;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

public abstract class StyleDefinePaneWithSelectConf extends MobileTemplateStyleDefinePane {

    protected NewColorSelectBox selectColorBox;
    protected UIColorButton selectFontColor;

    public StyleDefinePaneWithSelectConf(WCardTagLayout tagLayout) {
        super(tagLayout);
    }

    protected JPanel createBackgroundConfPane() {
        JPanel panel = FRGUIPaneFactory.createVerticalFlowLayout_Pane(true, FlowLayout.LEADING, 0, 10);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 5, 20));
        initialColorBox = new NewColorSelectBox(137);
        initialColorBox.addSelectChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updatePreviewPane();
            }
        });

        selectColorBox = new NewColorSelectBox(137);
        selectColorBox.addSelectChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updatePreviewPane();
            }
        });
        UILabel initFillLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Init_Fill"));
        UILabel selectFillLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Select_Fill"));
        initFillLabel.setPreferredSize(new Dimension(55, 20));
        selectFillLabel.setPreferredSize(new Dimension(55, 20));
        JPanel jPanel2 = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{initFillLabel, initialColorBox}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L1, LayoutConstants.VGAP_MEDIUM);
        JPanel jPanel3 = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{selectFillLabel, selectColorBox}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L1, LayoutConstants.VGAP_MEDIUM);
        jPanel2.setPreferredSize(new Dimension(240, 20));
        jPanel3.setPreferredSize(new Dimension(240, 20));
        panel.add(jPanel2);
        panel.add(jPanel3);
        return panel;
    }

    protected JPanel createFontConfPane() {
        JPanel panel = FRGUIPaneFactory.createVerticalFlowLayout_Pane(true, FlowLayout.LEADING, 0, 10);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 5, 20));
        fontConfPane = new MobileTabFontConfPane();
        fontConfPane.addFontChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updatePreviewPane();
            }
        });
        selectFontColor = new UIColorButton();
        selectFontColor.addColorChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                updatePreviewPane();
            }
        });
        UILabel initCharLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Tab_Init_Char"));
        initCharLabel.setPreferredSize(new Dimension(55, 20));
        JPanel jPanel3 = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{initCharLabel, fontConfPane}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L1, LayoutConstants.VGAP_MEDIUM);
        jPanel3.setPreferredSize(new Dimension(500, 20));

        UILabel selectCharLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Tab_Select_Char"));
        selectCharLabel.setPreferredSize(new Dimension(55, 20));
        selectFontColor.setPreferredSize(new Dimension(20, 20));
        JPanel jPanel4 = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{selectCharLabel, selectFontColor}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L1, LayoutConstants.VGAP_MEDIUM);
        JPanel jPanel5 = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        jPanel5.setPreferredSize(new Dimension(500, 20));
        jPanel5.add(jPanel4);
        panel.add(jPanel3);
        panel.add(jPanel5);
        return panel;
    }

    @Override
    public MobileTemplateStyle updateSubStyle() {
        MobileTemplateStyle mobileTemplateStyle = updateStyleWithSelectConf();
        mobileTemplateStyle.setSelectColor(selectColorBox.getSelectObject());
        TabFontConfig config = new TabFontConfig();
        config.setFont(mobileTemplateStyle.getTabFontConfig().getFont());
        config.setSelectColor(selectFontColor.getColor());
        mobileTemplateStyle.setTabFontConfig(config);
        return mobileTemplateStyle;
    }

    protected abstract MobileTemplateStyle updateStyleWithSelectConf();

    public void populateSubStyle(MobileTemplateStyle ob) {
        selectColorBox.setSelectObject(ob.getSelectColor());
        selectFontColor.setColor(ob.getTabFontConfig().getSelectColor());
    }
}
