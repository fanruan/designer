package com.fr.design.chart.series.SeriesCondition;

import com.fr.base.background.ColorBackground;
import com.fr.chart.base.AttrBackground;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.style.background.BackgroundPane;
import com.fr.design.style.background.BackgroundPreviewLabel;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
* @author richie
* @date 2015-03-26
* @since 8.0
*/
public class LabelBackgroundPane extends ConditionAttrSingleConditionPane<DataSeriesCondition> {

    private UILabel backgroundLabel;
    private BackgroundPreviewLabel backgroundPreviewPane;

    private AttrBackground attrBackground = new AttrBackground();

    public LabelBackgroundPane(final ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane, true);
        backgroundLabel = new UILabel(Inter.getLocText("Background"));
        this.backgroundPreviewPane = new BackgroundPreviewLabel();
        this.backgroundPreviewPane.setPreferredSize(new Dimension(80, 20));
        UIButton editBackgroundButton = new UIButton(Inter.getLocText("Edit"));
        MouseAdapter mouseListener = new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                final BackgroundPane backgroundPane = new BackgroundPane();
                BasicDialog styleDialog = backgroundPane.showWindow(
                        SwingUtilities.getWindowAncestor(conditionAttributesPane)); // 调整(400,300) 比较合适.
                backgroundPane.populate(backgroundPreviewPane.getBackgroundObject());
                styleDialog.addDialogActionListener(new DialogActionAdapter() {
                    public void doOk() {
                        backgroundPreviewPane.setBackgroundObject(backgroundPane.update());
                        backgroundPreviewPane.repaint();
                    }
                });
                styleDialog.setVisible(true);
            }
        };
        editBackgroundButton.addMouseListener(mouseListener);
        backgroundPreviewPane.addMouseListener(mouseListener);
        this.add(backgroundLabel);
        this.add(backgroundPreviewPane);
        this.add(editBackgroundButton);
        this.backgroundPreviewPane.setBackgroundObject(ColorBackground.getInstance(Color.white));
    }

    @Override
    public String nameForPopupMenuItem() {
        return Inter.getLocText("ChartF-Background_Color");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public void populate(DataSeriesCondition condition) {
        if (condition instanceof AttrBackground) {
            attrBackground = (AttrBackground) condition;
            this.backgroundPreviewPane.setBackgroundObject(attrBackground.getSeriesBackground());
        }
    }

    public DataSeriesCondition update() {
        attrBackground.setSeriesBackground(this.backgroundPreviewPane.getBackgroundObject());
        return attrBackground;
    }
}