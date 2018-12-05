package com.fr.design.mainframe.widget.preview;

import com.fr.general.cardtag.mobile.MobileTemplateStyle;
import com.fr.general.cardtag.mobile.TabFontConfig;

import javax.swing.JPanel;
import java.awt.Color;

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

    public MobileTemplatePreviewPane(){

    }

    public void populateConfig(MobileTemplateStyle templateStyle){
        this.setInitialColor(templateStyle.getInitialColor());
        this.setBackground(templateStyle.getInitialColor());
        this.setSelectColor(templateStyle.getSelectColor());
        this.setTabFontConfig(templateStyle.getTabFontConfig());
    }

    public void repaint (){
        super.repaint();
    }

}
