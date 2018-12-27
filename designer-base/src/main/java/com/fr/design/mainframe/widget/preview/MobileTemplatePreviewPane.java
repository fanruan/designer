package com.fr.design.mainframe.widget.preview;

import com.fr.general.cardtag.mobile.MobileTemplateStyle;
import com.fr.general.cardtag.mobile.TabFontConfig;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.FontMetrics;

public abstract class MobileTemplatePreviewPane extends JPanel {
    private Color initialColor;
    private Color selectColor;
    private TabFontConfig tabFontConfig = new TabFontConfig();

    public Color getInitialColor() {
        return initialColor;
    }

    public void setInitialColor(Color initialColor) {
        this.initialColor = initialColor;
    }

    public Color getSelectColor() {
        return selectColor;
    }

    public void setSelectColor(Color selectColor) {
        this.selectColor = selectColor;
    }

    public TabFontConfig getTabFontConfig() {
        return tabFontConfig;
    }

    public void setTabFontConfig(TabFontConfig tabFontConfig) {
        this.tabFontConfig = tabFontConfig;
    }

    public MobileTemplatePreviewPane() {

    }

    public void populateConfig(MobileTemplateStyle templateStyle) {
        this.setInitialColor(templateStyle.getInitialColor());
        this.setBackground(templateStyle.getInitialColor());
        this.setSelectColor(templateStyle.getSelectColor());
        this.setTabFontConfig(templateStyle.getTabFontConfig());
    }

    protected String calculateDisplayName(String widgetName, FontMetrics fm, int eachWidth) {
        StringBuffer buffer = new StringBuffer();
        String result;
        for (int i = 0; i < widgetName.length(); i++) {
            result = buffer.toString();
            buffer.append(widgetName.charAt(i));
            if (fm.stringWidth(buffer.toString()) > eachWidth) {
                return result;
            }
        }
        return buffer.toString();

    }

    public void repaint() {
        super.repaint();
    }

}
