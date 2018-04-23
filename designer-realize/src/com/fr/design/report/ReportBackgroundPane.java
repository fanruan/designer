package com.fr.design.report;

import java.awt.BorderLayout;

import com.fr.page.ReportSettingsProvider;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;
import com.fr.design.style.background.BackgroundPane;

public class ReportBackgroundPane extends BasicPane {
    private UICheckBox isPrintBackgroundCheckBox;
    private BackgroundPane backgroundPane;

    public ReportBackgroundPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        backgroundPane = new BackgroundPane();
        this.add(backgroundPane, BorderLayout.CENTER);

        isPrintBackgroundCheckBox = new UICheckBox(
                Inter.getLocText("ReportGUI-Print_Background"));
        this.add(isPrintBackgroundCheckBox, BorderLayout.SOUTH);
    }
    
    @Override
    protected String title4PopupWindow() {
    	return Inter.getLocText(new String[]{"paper", "Background"});
    }

    /**
     * Populate
     */
    public void populate(ReportSettingsProvider reportSettings) {
        this.backgroundPane.populate(reportSettings.getBackground());
        this.isPrintBackgroundCheckBox.setSelected(reportSettings.isPrintBackground());
    }

    /**
     * update
     */
    public void update(ReportSettingsProvider reportSettings) {
        reportSettings.setBackground(this.backgroundPane.update());
        reportSettings.setPrintBackground(this.isPrintBackgroundCheckBox.isSelected());
    }
}