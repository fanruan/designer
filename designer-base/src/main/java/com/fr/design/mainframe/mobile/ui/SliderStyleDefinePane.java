package com.fr.design.mainframe.mobile.ui;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.UITitleSplitLine;
import com.fr.design.mainframe.widget.preview.MobileTemplatePreviewPane;
import com.fr.design.style.color.NewColorSelectBox;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.general.FRFont;
import com.fr.general.cardtag.mobile.MobileTemplateStyle;
import com.fr.general.cardtag.mobile.SliderStyle;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class SliderStyleDefinePane extends MobileTemplateStyleDefinePane {
    private NewColorSelectBox initDotColor;
    private NewColorSelectBox selectDotColor;


    public SliderStyleDefinePane(WCardTagLayout tagLayout) {
        super(tagLayout);
    }

    @Override
    public void populateBean(MobileTemplateStyle ob) {
        super.populateBean(ob);
        SliderStyle sliderStyle = (SliderStyle) ob;
        initDotColor.setSelectObject(sliderStyle.getInitDotColor());
        selectDotColor.setSelectObject(sliderStyle.getSelectDotColor());
    }

    @Override
    protected MobileTemplateStyle getDefaultTemplateStyle() {
        return new SliderStyle();
    }

    @Override
    public MobileTemplateStyle updateSubStyle() {
        SliderStyle sliderStyle = new SliderStyle();
        sliderStyle.setInitDotColor(initDotColor.getSelectObject());
        sliderStyle.setSelectDotColor(selectDotColor.getSelectObject());
        return sliderStyle;
    }


    protected void createExtraConfPane(JPanel centerPane) {
        UITitleSplitLine dotIndicator = new UITitleSplitLine(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Slider_Dot_Indicator"), 520);
        dotIndicator.setPreferredSize(new Dimension(520, 20));
        centerPane.add(dotIndicator);
        initDotColor = new NewColorSelectBox(137);
        initDotColor.addSelectChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updatePreviewPane();
            }
        });
        selectDotColor = new NewColorSelectBox(137);
        selectDotColor.addSelectChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updatePreviewPane();
            }
        });
        UILabel initColor = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Init_Fill"));

        UILabel selectColor = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Select_Fill"));
        initColor.setPreferredSize(new Dimension(55, 20));
        selectColor.setPreferredSize(new Dimension(55, 20));
        JPanel initDotColorPane = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{initColor, initDotColor}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L1, LayoutConstants.VGAP_MEDIUM);
        JPanel selectDotColorPane = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{selectColor, selectDotColor}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L1, LayoutConstants.VGAP_MEDIUM);
        initDotColorPane.setPreferredSize(new Dimension(240, 20));
        selectDotColorPane.setPreferredSize(new Dimension(240, 20));
        centerPane.add(initDotColorPane);
        centerPane.add(selectDotColorPane);

    }

    @Override
    protected void initDefaultConfig() {
        this.initialColorBox.setSelectObject(SliderStyle.DEFAULT_INITIAL_COLOR);
        this.fontConfPane.populate(SliderStyle.DEFAULT_TAB_FONT.getFont());
        initDotColor.setSelectObject(SliderStyle.DEFAULT_INITIAL_DOT_COLOR);
        selectDotColor.setSelectObject(SliderStyle.DEFAULT_SELECT_DOT_COLOR);
    }

    @Override
    protected MobileTemplatePreviewPane createPreviewPane() {
        return new SliderStylePreviewPane();
    }


    @Override
    protected String title4PopupWindow() {
        return null;
    }


    public class SliderStylePreviewPane extends MobileTemplatePreviewPane {
        private Color initDotColor;
        private Color selectDotColor;

        public SliderStylePreviewPane() {

        }

        public void repaint() {
            super.repaint();
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2d = (Graphics2D) g.create();
            FRFont frFont = this.getTabFontConfig().getFont();
            g2d.setFont(frFont);
            g2d.setColor(frFont.getForeground());
            g2d.drawString(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Slider_Title"), 10, 18);
            WCardTagLayout cardTagLayout = SliderStyleDefinePane.this.getTagLayout();
            g2d.translate(10, 25);
            for (int i = 0; i < cardTagLayout.getWidgetCount(); i++) {
                if (i == 0) {
                    g2d.setColor(selectDotColor);
                    g2d.fillOval(0, 0, 6, 6);
                    g2d.translate(9, 0);
                    continue;
                }
                g2d.setColor(initDotColor);
                g2d.fillOval(0, 0, 6, 6);
                g2d.translate(9, 0);
            }

        }

        public void populateConfig(MobileTemplateStyle templateStyle) {
            super.populateConfig(templateStyle);
            this.initDotColor = ((SliderStyle) templateStyle).getInitDotColor();
            this.selectDotColor = ((SliderStyle) templateStyle).getSelectDotColor();
        }
    }

}
