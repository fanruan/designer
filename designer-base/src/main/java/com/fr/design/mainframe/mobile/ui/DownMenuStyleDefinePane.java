package com.fr.design.mainframe.mobile.ui;

import com.fr.base.GraphHelper;
import com.fr.base.Icon;
import com.fr.base.IconManager;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.UITitleSplitLine;
import com.fr.design.mainframe.widget.preview.MobileTemplatePreviewPane;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.general.FRFont;
import com.fr.general.cardtag.mobile.DownMenuStyle;
import com.fr.general.cardtag.mobile.LineDescription;
import com.fr.general.cardtag.mobile.MobileTemplateStyle;
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
import java.awt.Stroke;
import java.util.ArrayList;

public class DownMenuStyleDefinePane extends StyleDefinePaneWithSelectConf {
    private LinePane splitLinePane;
    private TabIconConfigPane initIconConfigPane;
    private TabIconConfigPane selectIconConfigPane;

    public DownMenuStyleDefinePane(WCardTagLayout tagLayout) {
        super(tagLayout);
    }

    protected void createExtraConfPane(JPanel centerPane) {
        JPanel panel = FRGUIPaneFactory.createVerticalFlowLayout_Pane(true, FlowLayout.LEADING, 0, 0);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 5, 20));
        UITitleSplitLine iconSplitLine = new UITitleSplitLine(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Icon"), 520);
        iconSplitLine.setPreferredSize(new Dimension(520, 20));
        centerPane.add(iconSplitLine);

        initIconConfigPane = new TabIconConfigPane(getTagLayout().getWidgetCount());
        selectIconConfigPane = new TabIconConfigPane(getTagLayout().getWidgetCount());

        UILabel initIconLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Initial_Icon"));
        UILabel selectIconLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Select_Icon"));
        initIconLabel.setPreferredSize(new Dimension(55, 20));
        selectIconLabel.setPreferredSize(new Dimension(55, 20));
        JPanel initIconContainPane = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{initIconLabel, initIconConfigPane}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L1, LayoutConstants.VGAP_MEDIUM);
        JPanel selectIconContainePane = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{selectIconLabel, selectIconConfigPane}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L1, LayoutConstants.VGAP_MEDIUM);
        initIconContainPane.setPreferredSize(new Dimension(240, 50));
        selectIconContainePane.setPreferredSize(new Dimension(240, 50));
        panel.add(initIconContainPane);
        panel.add(selectIconContainePane);
        UITitleSplitLine splitLine = new UITitleSplitLine(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Spit_Line"), 520);
        splitLine.setPreferredSize(new Dimension(520, 20));
        splitLinePane = new LinePane();
        splitLinePane.addLineChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updatePreviewPane();
            }
        });
        centerPane.add(panel);
        centerPane.add(splitLine);
        centerPane.add(splitLinePane);
    }

    @Override
    protected void initDefaultConfig() {
        this.initialColorBox.setSelectObject(DownMenuStyle.DEFAULT_INITIAL_COLOR);
        this.fontConfPane.populate(DownMenuStyle.DEFAULT_TAB_FONT.getFont());
        this.selectColorBox.setSelectObject(DownMenuStyle.DEFAULT_SELECT_COLOR);
        this.selectFontColor.setColor(DownMenuStyle.DEFAULT_SELECT_FONT_COLOR);
        this.splitLinePane.populate(DownMenuStyle.DEFAULT_SPLIT_LINE);
    }

    @Override
    protected MobileTemplatePreviewPane createPreviewPane() {
        return new DownMenuStylePreviewPane();
    }


    @Override
    public void populateSubStyle(MobileTemplateStyle ob) {
        super.populateSubStyle(ob);
        DownMenuStyle downMenuStyle = (DownMenuStyle) ob;
        splitLinePane.populate(downMenuStyle.getSplitLine());
        ArrayList<String> initialIconNames = new ArrayList<String>();
        ArrayList<String> selectIconNames = new ArrayList<String>();
        for (int i = 0; i < getTagLayout().getWidgetCount(); i++) {
            CardSwitchButton cardSwitchButton = (CardSwitchButton) getTagLayout().getWidget(i);
            initialIconNames.add(cardSwitchButton.getInitIconName());
            selectIconNames.add(cardSwitchButton.getSelectIconName());
        }
        initIconConfigPane.populate(initialIconNames);
        selectIconConfigPane.populate(selectIconNames);
    }

    @Override
    protected MobileTemplateStyle getDefaultTemplateStyle() {
        return new DownMenuStyle();
    }

    @Override
    public MobileTemplateStyle updateStyleWithSelectConf() {
        DownMenuStyle downMenuStyle = new DownMenuStyle();
        downMenuStyle.setSplitLine(splitLinePane.update());
        ArrayList<String> initialIconNames = initIconConfigPane.update();
        ArrayList<String> selectIconNames = selectIconConfigPane.update();
        for (int i = 0; i < getTagLayout().getWidgetCount(); i++) {
            CardSwitchButton cardSwitchButton = (CardSwitchButton) getTagLayout().getWidget(i);
            cardSwitchButton.setInitIconName(initialIconNames.get(i));
            cardSwitchButton.setSelectIconName(selectIconNames.get(i));
        }
        return downMenuStyle;
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }

    public class DownMenuStylePreviewPane extends MobileTemplatePreviewPane {
        private static final int ICON_OFFSET = 16;
        private static final int GAP = 6;
        private static final String PAINT_ICON = "fund_white";
        private static final String ICON_PATH = "/com/fr/web/images/fund_white.png";
        private LineDescription splitLine;

        public DownMenuStylePreviewPane() {
            this.setBackground(Color.decode("#3888EE"));
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
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            FRFont frFont = this.getTabFontConfig().getFont();
            FontMetrics fm = GraphHelper.getFontMetrics(frFont);
            WCardTagLayout cardTagLayout = DownMenuStyleDefinePane.this.getTagLayout();
            int eachWidth = panelWidth / cardTagLayout.getWidgetCount();
            g2d.setFont(frFont);
            int fontHeight = fm.getHeight();
            int ascent = fm.getAscent();
            for (int i = 0; i < cardTagLayout.getWidgetCount(); i++) {
                g2d.setColor(i == 0 ? selectFontColor : frFont.getForeground());
                CardSwitchButton cardSwitchButton = cardTagLayout.getSwitchButton(i);
                String widgetName = cardSwitchButton.getText();
                int width = fm.stringWidth(widgetName);
                if(i == 0){
                    Color oldColor = g2d.getColor();
                    g2d.setColor(this.getSelectColor());
                    g2d.fillRect(0, 0 ,eachWidth, panelHeight);
                    g2d.setColor(oldColor);
                }
                Icon icon = new Icon(PAINT_ICON, ICON_PATH);
                g2d.drawImage(IconManager.getIconManager().getDefaultIconImage(icon), (eachWidth - ICON_OFFSET) / 2, (panelHeight - ICON_OFFSET - GAP - fontHeight) / 2, null);
                g2d.drawString(widgetName, (eachWidth - width) / 2, (panelHeight + ICON_OFFSET + GAP - fontHeight) / 2  + ascent);
                Stroke oldStroke = g2d.getStroke();
                if (splitLine.getLineStyle() != 0) {
                    g2d.setColor(splitLine.getColor());
                    g2d.setStroke(GraphHelper.getStroke(splitLine.getLineStyle()));
                    g2d.drawLine(eachWidth, 0, eachWidth, panelHeight);
                }
                g2d.setStroke(oldStroke);
                g2d.translate(eachWidth, 0);

            }

        }

        public void populateConfig(MobileTemplateStyle templateStyle) {
            super.populateConfig(templateStyle);
            this.splitLine = ((DownMenuStyle) templateStyle).getSplitLine();
        }
    }
}
