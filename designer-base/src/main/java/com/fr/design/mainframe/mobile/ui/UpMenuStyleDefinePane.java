package com.fr.design.mainframe.mobile.ui;

import com.fr.base.GraphHelper;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.UITitleSplitLine;
import com.fr.design.mainframe.widget.preview.MobileTemplatePreviewPane;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.general.FRFont;
import com.fr.general.cardtag.mobile.LineDescription;
import com.fr.general.cardtag.mobile.MobileTemplateStyle;
import com.fr.general.cardtag.mobile.UpMenuStyle;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class UpMenuStyleDefinePane extends StyleDefinePaneWithSelectConf {
    private UIRadioButton gapFix;
    private UIRadioButton titleWidthFix;
    private LinePane bottomBorderPane;
    private LinePane underLinePane;

    public UpMenuStyleDefinePane(WCardTagLayout tagLayout) {
        super(tagLayout);
    }

    protected JPanel createCenterPane() {
        JPanel panel = super.createCenterPane();
        UILabel displayGap = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Display_Gap"));
        displayGap.setPreferredSize(new Dimension(55, 20));
        gapFix = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Gap_Fix"));
        titleWidthFix = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Title_Width_Fix"));
        ButtonGroup buttonGroup = new ButtonGroup();
        titleWidthFix.setSelected(true);
        buttonGroup.add(gapFix);
        gapFix.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        titleWidthFix.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonGroup.add(titleWidthFix);
        gapFix.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updatePreviewPane();
            }
        });
        titleWidthFix.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updatePreviewPane();
            }
        });


        JPanel flowLeft = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
        flowLeft.add(gapFix);
        flowLeft.add(titleWidthFix);
        JPanel centerPane = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{displayGap, flowLeft}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L1, LayoutConstants.VGAP_MEDIUM);
        centerPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 15, 20));
        centerPane.setPreferredSize(new Dimension(500, 20));
        JPanel outerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        outerPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        outerPane.add(centerPane, BorderLayout.CENTER);
        panel.add(outerPane);
        return panel;
    }

    protected void createExtraConfPane(JPanel centerPane) {
        bottomBorderPane = new LinePane();
        underLinePane = new LinePane();
        bottomBorderPane.addLineChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updatePreviewPane();
            }
        });
        underLinePane.addLineChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updatePreviewPane();
            }
        });
        UITitleSplitLine titleSplitLine = new UITitleSplitLine(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Bottom_Border"), 520);
        titleSplitLine.setPreferredSize(new Dimension(520, 20));
        centerPane.add(titleSplitLine);
        centerPane.add(bottomBorderPane);
        UITitleSplitLine titleUnderLine = new UITitleSplitLine(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Under_Line"), 520);
        titleUnderLine.setPreferredSize(new Dimension(520, 20));
        centerPane.add(titleUnderLine);
        centerPane.add(underLinePane);
    }

    @Override
    protected void initDefaultConfig() {
        this.initialColorBox.setSelectObject(UpMenuStyle.DEFAULT_INITIAL_COLOR);
        this.fontConfPane.populate(UpMenuStyle.DEFAULT_TAB_FONT.getFont());
        this.selectColorBox.setSelectObject(UpMenuStyle.DEFAULT_SELECT_COLOR);
        this.selectFontColor.setColor(UpMenuStyle.DEFAULT_SELECT_FONT_COLOR);
        this.bottomBorderPane.populate(UpMenuStyle.DEFAULT_BOTTOM_BORDER);
        this.underLinePane.populate(UpMenuStyle.DEFAULT_UNDER_LINE);
    }

    @Override
    protected MobileTemplatePreviewPane createPreviewPane() {
        return new UpMenuStylePreviewPane();
    }

    @Override
    public void populateSubStyle(MobileTemplateStyle ob) {
        super.populateSubStyle(ob);
        UpMenuStyle style = (UpMenuStyle) ob;
        gapFix.setSelected(style.isGapFix());
        titleWidthFix.setSelected(style.isTitleWidthFix());
        bottomBorderPane.populate(style.getBottomBorder());
        underLinePane.populate(style.getUnderline());
    }

    @Override
    protected MobileTemplateStyle getDefaultTemplateStyle() {
        return new UpMenuStyle();
    }


    @Override
    public MobileTemplateStyle updateStyleWithSelectConf() {
        UpMenuStyle style = new UpMenuStyle();
        style.setGapFix(gapFix.isSelected());
        style.setTitleWidthFix(titleWidthFix.isSelected());
        style.setBottomBorder(bottomBorderPane.update());
        style.setUnderline(underLinePane.update());
        return style;
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }


    public class UpMenuStylePreviewPane extends MobileTemplatePreviewPane {
        private LineDescription bottomBorder;
        private LineDescription underLine;
        private boolean isGapFix;

        public UpMenuStylePreviewPane() {
            this.setBackground(Color.WHITE);
        }

        public void repaint() {
            super.repaint();
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Color selectFontColor = this.getTabFontConfig().getSelectColor();
            Dimension dimension = this.getSize();
            int panelWidth = dimension.width;
            int panelHeight = dimension.height;
            Graphics2D g2d = (Graphics2D) g.create();
            FRFont frFont = UpMenuStyleDefinePane.this.fontConfPane.update();
            FontMetrics fm = GraphHelper.getFontMetrics(frFont);
            WCardTagLayout cardTagLayout = UpMenuStyleDefinePane.this.getTagLayout();
            int eachWidth = panelWidth / cardTagLayout.getWidgetCount();
            g2d.setFont(frFont);
            int fontHeight = fm.getHeight();
            int ascentHeight = fm.getAscent();
            if (bottomBorder.getLineStyle() != 0) {
                g2d.setColor(bottomBorder.getColor());
                g2d.setStroke(GraphHelper.getStroke(bottomBorder.getLineStyle()));
                g2d.drawLine(0, panelHeight - 1, panelWidth, panelHeight - 1);
            }
            for (int i = 0; i < cardTagLayout.getWidgetCount(); i++) {
                g2d.setColor(i == 0 ? selectFontColor : frFont.getForeground());
                CardSwitchButton cardSwitchButton = cardTagLayout.getSwitchButton(i);
                String widgetName = cardSwitchButton.getText();
                int width = fm.stringWidth(widgetName);
                if(i == 0){
                    Color oldColor = g2d.getColor();
                    g2d.setColor(this.getSelectColor());
                    g2d.fillRect(0, 0 ,eachWidth, panelHeight - 2);
                    g2d.setColor(oldColor);
                }
                g2d.drawString(widgetName, (eachWidth - width) / 2, (panelHeight - fontHeight) / 2 + ascentHeight);
                Stroke oldStroke = g2d.getStroke();
                if (i == 0) {
                    g2d.setColor(this.underLine.getColor());
                    g2d.setStroke(GraphHelper.getStroke(underLine.getLineStyle()));
                    int underLineX = this.isGapFix ? (eachWidth - width) / 2 : 0;
                    int underLineWidth = this.isGapFix ? width : eachWidth;
                    g2d.drawLine(underLineX, panelHeight - 2, underLineX + underLineWidth, panelHeight - 2);
                }
                g2d.setStroke(oldStroke);
                g2d.translate(eachWidth, 0);
            }
        }

        public void populateConfig(MobileTemplateStyle templateStyle) {
            super.populateConfig(templateStyle);
            this.bottomBorder = ((UpMenuStyle) templateStyle).getBottomBorder();
            this.underLine = ((UpMenuStyle) templateStyle).getUnderline();
            this.isGapFix = ((UpMenuStyle) templateStyle).isGapFix();
        }
    }

}
