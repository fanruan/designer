package com.fr.design.condition;

import com.fr.base.background.ColorBackground;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.style.background.BackgroundPane;
import com.fr.design.style.background.BackgroundPreviewLabel;

import com.fr.report.cell.cellattr.highlight.BackgroundHighlightAction;
import com.fr.report.cell.cellattr.highlight.HighlightAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
* @author richie
* @date 2015-03-26
* @since 8.0
*/
public class BackPane extends ConditionAttrSingleConditionPane<HighlightAction> {
    private UILabel backgroundLabel;
    private BackgroundPreviewLabel backgroundPreviewPane;
    private UIComboBox backScopeComboBox;

    public BackPane(final ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
        backgroundLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Base_Background") + ":");
        this.backgroundPreviewPane = new BackgroundPreviewLabel();
        this.backgroundPreviewPane.setPreferredSize(new Dimension(80, 20));
        UIButton editBackgroundButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Edit"));
        editBackgroundButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final BackgroundPane backgroundPane = new BackgroundPane();
                backgroundPane.populate(backgroundPreviewPane.getBackgroundObject());
                backgroundPane.showWindow(
                        SwingUtilities.getWindowAncestor(conditionAttributesPane), new DialogActionAdapter() {
                            public void doOk() {
                                backgroundPreviewPane.setBackgroundObject(backgroundPane.update());
                                backgroundPreviewPane.repaint();
                            }
                        }).setVisible(true);
            }
        });

        this.backScopeComboBox = new UIComboBox(new String[] {
                com.fr.design.i18n.Toolkit.i18nText("Utils-Current_Cell"),
                com.fr.design.i18n.Toolkit.i18nText("Utils-Current_Row"),
                com.fr.design.i18n.Toolkit.i18nText("Utils-Current_Column") });

        this.add(backgroundLabel);
        this.add(backgroundPreviewPane);
        this.add(editBackgroundButton);
        this.add(this.backScopeComboBox);
        this.backgroundPreviewPane.setBackgroundObject(ColorBackground.getInstance(Color.WHITE));
    }

    @Override
    public String nameForPopupMenuItem() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Base_Background");
    }


    public  void populate(HighlightAction ha) {
        this.backgroundPreviewPane.setBackgroundObject(((BackgroundHighlightAction) ha).getBackground());
        this.backScopeComboBox.setSelectedIndex(((BackgroundHighlightAction) ha).getScope());
    }

    public HighlightAction update() {
        return new BackgroundHighlightAction(this.backgroundPreviewPane.getBackgroundObject(), this.backScopeComboBox.getSelectedIndex());
    }
}
