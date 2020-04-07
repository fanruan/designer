package com.fr.design.mainframe.mobile.ui;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.dialog.AttrScrollPane;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.MobileTabFontConfPane;
import com.fr.design.mainframe.widget.UITitleSplitLine;
import com.fr.design.mainframe.widget.preview.MobileTemplatePreviewPane;
import com.fr.design.style.color.NewColorSelectBox;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.general.FRFont;
import com.fr.general.cardtag.mobile.MobileTemplateStyle;
import com.fr.general.cardtag.mobile.TabFontConfig;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public abstract class MobileTemplateStyleDefinePane extends BasicBeanPane<MobileTemplateStyle> {
    private static final String[] TAB_STYLES = new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Style_Standard"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Custom")};
    private UIComboBox custom;
    protected NewColorSelectBox initialColorBox;
    protected MobileTabFontConfPane fontConfPane;
    private JPanel centerPane;
    protected MobileTemplatePreviewPane previewPane;
    private WCardTagLayout tagLayout;


    public MobileTemplatePreviewPane getPreviewPane() {
        return previewPane;
    }

    public MobileTemplateStyleDefinePane(WCardTagLayout tagLayout) {
        this.tagLayout = tagLayout;
        init();
    }


    public WCardTagLayout getTagLayout() {
        return tagLayout;
    }

    protected void init() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        previewPane = createPreviewPane();
        if (previewPane != null) {
            previewPane.setPreferredSize(new Dimension(500, 60));
            JPanel northPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
            TitledBorder titledBorder = GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preview"), null);
            titledBorder.setTitleFont(FRFont.getInstance("PingFangSC-Regular", Font.PLAIN, 9, Color.BLUE));
            northPane.setBorder(titledBorder);
            northPane.setPreferredSize(new Dimension(500, 83));
            northPane.add(previewPane, BorderLayout.CENTER);
            this.add(northPane, BorderLayout.NORTH);
        }
        createConfigPane();

    }

    protected void createConfigPane() {
        JPanel configPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        TitledBorder titledBorder = GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Set"), null);
        titledBorder.setTitleFont(FRFont.getInstance("PingFangSC-Regular", Font.PLAIN, 9, Color.BLUE));
        configPane.setBorder(titledBorder);
        centerPane = createCenterPane();
        custom = new UIComboBox(TAB_STYLES);
        custom.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean isCustom = custom.getSelectedIndex() == 1;
                centerPane.setVisible(isCustom);
                updatePreviewPane();
            }
        });
        centerPane.setVisible(false);
        custom.setPreferredSize(new Dimension(157, 20));
        final JPanel scrollPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        BasicScrollPane basicScrollPane = new AttrScrollPane() {
            @Override
            protected JPanel createContentPane() {
                return scrollPanel;
            }
        };
        configPane.add(basicScrollPane, BorderLayout.CENTER);
        this.add(configPane, BorderLayout.CENTER);

        JPanel outPanel = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane();
        outPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));
        UILabel tabStyleLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Tab_Style"));
        tabStyleLabel.setPreferredSize(new Dimension(55, 20));
        JPanel jPanel = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{tabStyleLabel, custom}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L1, LayoutConstants.VGAP_MEDIUM);
        jPanel.setPreferredSize(new Dimension(200, 20));
        outPanel.add(jPanel);
        scrollPanel.add(outPanel, BorderLayout.NORTH);

        UITitleSplitLine backgroundSplit = new UITitleSplitLine(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background"), 520);
        backgroundSplit.setPreferredSize(new Dimension(520, 20));
        centerPane.add(backgroundSplit);

        centerPane.add(createBackgroundConfPane());

        UITitleSplitLine fontSplit = new UITitleSplitLine(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Tab_Char"), 520);
        fontSplit.setPreferredSize(new Dimension(520, 20));
        centerPane.add(fontSplit);

        centerPane.add(createFontConfPane());

        createExtraConfPane(centerPane);

        scrollPanel.add(centerPane, BorderLayout.CENTER);
        initDefaultConfig();
    }

    protected JPanel createCenterPane() {
        JPanel panel = FRGUIPaneFactory.createVerticalFlowLayout_Pane(true, FlowLayout.LEADING, 0, 10);
        return panel;
    }

    protected JPanel createBackgroundConfPane() {
        initialColorBox = new NewColorSelectBox(137);
        initialColorBox.addSelectChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updatePreviewPane();
            }
        });
        UILabel fillLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Fill"));
        fillLabel.setPreferredSize(new Dimension(55, 20));

        JPanel jPanel = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{fillLabel, initialColorBox}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L1, LayoutConstants.VGAP_MEDIUM);
        jPanel.setPreferredSize(new Dimension(240, 20));
        initialColorBox.setPreferredSize(new Dimension(157, 20));
        jPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 5, 20));
        return jPanel;
    }

    protected JPanel createFontConfPane() {
        fontConfPane = new MobileTabFontConfPane();
        fontConfPane.addFontChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updatePreviewPane();
            }
        });
        UILabel initCharLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Tab_Init_Char"));
        initCharLabel.setPreferredSize(new Dimension(55, 20));
        JPanel jPanel3 = GUICoreUtils.createBoxFlowInnerContainerPane(5, 0);
        jPanel3.add(initCharLabel);
        jPanel3.add(fontConfPane);
        jPanel3.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        return jPanel3;
    }


    protected void createExtraConfPane(JPanel centerPane) {

    }

    protected abstract void initDefaultConfig();

    protected abstract MobileTemplatePreviewPane createPreviewPane();

    @Override
    public void populateBean(MobileTemplateStyle ob) {
        centerPane.setVisible(ob.isCustom());
        populateSubStyle(ob);
        custom.setSelectedItem(!ob.isCustom() ? com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Default") :
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Custom"));
        initialColorBox.setSelectObject(ob.getInitialColor());
        fontConfPane.populate(ob.getTabFontConfig().getFont());
        updatePreviewPane();
    }


    @Override
    public MobileTemplateStyle updateBean() {
        //保存之前需要先将cardSwitchBtn的icon设置清空
        for (int i = 0; i < getTagLayout().getWidgetCount(); i++) {
            CardSwitchButton cardSwitchButton = (CardSwitchButton) getTagLayout().getWidget(i);
            cardSwitchButton.setInitIconName(StringUtils.EMPTY);
            cardSwitchButton.setSelectIconName(StringUtils.EMPTY);
        }
        return updateConfig();
    }

    public MobileTemplateStyle updateConfig(){
        if (custom.getSelectedIndex() == 0) {
            return getDefaultTemplateStyle();
        }
        MobileTemplateStyle ob = updateSubStyle();
        ob.setCustom(custom.getSelectedIndex() == 1);
        ob.setInitialColor(initialColorBox.getSelectObject());
        TabFontConfig config = new TabFontConfig();
        config.setSelectColor(ob.getTabFontConfig().getSelectColor());
        config.setFont(fontConfPane.update());
        ob.setTabFontConfig(config);
        return ob;
    }

    protected abstract MobileTemplateStyle getDefaultTemplateStyle();

    public void updatePreviewPane() {
        if (previewPane != null) {
            previewPane.populateConfig(updateConfig());
            previewPane.setBackground(previewPane.getInitialColor());
            previewPane.repaint();
        }
    }

    public abstract void populateSubStyle(MobileTemplateStyle ob);

    public abstract MobileTemplateStyle updateSubStyle();


    @Override
    protected String title4PopupWindow() {
        return null;
    }
}
