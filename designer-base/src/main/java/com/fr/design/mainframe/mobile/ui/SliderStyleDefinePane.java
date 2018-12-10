package com.fr.design.mainframe.mobile.ui;

import com.fr.base.GraphHelper;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.UITitleSplitLine;
import com.fr.design.mainframe.widget.preview.MobileTemplatePreviewPane;
import com.fr.design.style.color.NewColorSelectBox;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.general.FRFont;
import com.fr.general.cardtag.mobile.MobileTemplateStyle;
import com.fr.general.cardtag.mobile.SliderStyle;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class SliderStyleDefinePane extends MobileTemplateStyleDefinePane {
    private NewColorSelectBox initDotColor;
    private NewColorSelectBox selectDotColor;


    public SliderStyleDefinePane(WCardTagLayout tagLayout) {
        super(tagLayout);
    }

    @Override
    public void populateSubStyle(MobileTemplateStyle ob) {
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
        JPanel panel = FRGUIPaneFactory.createVerticalFlowLayout_Pane(true, FlowLayout.LEADING, 0, 10);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 5, 20));
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
        panel.add(initDotColorPane);
        panel.add(selectDotColorPane);
        centerPane.add(panel);
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
        private static final int CIRCLE_SIZE = 6;
        private static final int GAP = 4;
        private static final int OFFSET_X = 10;
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
            Dimension dimension = this.getSize();
            int panelHeight = dimension.height;
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            FRFont frFont = this.getTabFontConfig().getFont();
            g2d.setFont(frFont);
            g2d.setColor(frFont.getForeground());
            FontMetrics fm = GraphHelper.getFontMetrics(frFont);
            int fontHeight = fm.getHeight();
            int ascent = fm.getAscent();
            g2d.drawString(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Slider_Title"), OFFSET_X, (panelHeight - fontHeight - GAP - CIRCLE_SIZE) / 2 + ascent);
            WCardTagLayout cardTagLayout = SliderStyleDefinePane.this.getTagLayout();
            g2d.translate(OFFSET_X, (panelHeight + fontHeight + GAP - CIRCLE_SIZE) / 2);
            for (int i = 0; i < cardTagLayout.getWidgetCount(); i++) {
                if (i == 0) {
                    g2d.setColor(selectDotColor);
                    g2d.fillOval(0, 0, CIRCLE_SIZE, CIRCLE_SIZE);
                    g2d.translate(CIRCLE_SIZE + GAP, 0);
                    continue;
                }
                g2d.setColor(initDotColor);
                g2d.fillOval(0, 0, CIRCLE_SIZE, CIRCLE_SIZE);
                g2d.translate(CIRCLE_SIZE + GAP, 0);
            }

        }

        public void populateConfig(MobileTemplateStyle templateStyle) {
            super.populateConfig(templateStyle);
            this.initDotColor = ((SliderStyle) templateStyle).getInitDotColor();
            this.selectDotColor = ((SliderStyle) templateStyle).getSelectDotColor();
        }
    }

}
