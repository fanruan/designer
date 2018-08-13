package com.fr.design.condition;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.style.FRFontPane;
import com.fr.design.style.FRFontPreviewArea;
import com.fr.general.FRFont;

import com.fr.report.cell.cellattr.highlight.FRFontHighlightAction;
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
public class FontPane extends ConditionAttrSingleConditionPane<HighlightAction> {
    private UILabel fontLabel;
    private FRFontPreviewArea frFontPreviewPane;
    private UIComboBox fontScopeComboBox;

    public FontPane(final ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
        fontLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Sytle_FRFont") + ":");
        frFontPreviewPane = new FRFontPreviewArea();
        frFontPreviewPane.setBorder(BorderFactory.createTitledBorder(""));
        frFontPreviewPane.setPreferredSize(new Dimension(80, 20));
        UIButton editFRFontButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Edit"));
        editFRFontButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final FRFontPane frFontPane = new FRFontPane();
                BasicDialog styleDialog = frFontPane.showWindow(
                        SwingUtilities.getWindowAncestor(conditionAttributesPane));
                frFontPane.populate(frFontPreviewPane.getFontObject());
                styleDialog.addDialogActionListener(new DialogActionAdapter() {
                    public void doOk() {
                        frFontPreviewPane.setFontObject(frFontPane.update());
                        // repaint.
                        frFontPreviewPane.repaint();
                    }
                });
                styleDialog.setVisible(true);
            }
        });

        this.fontScopeComboBox = new UIComboBox(new String[] {
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Current_Cell"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Current_Row"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Current_Column") });

        this.add(fontLabel);
        this.add(frFontPreviewPane);
        this.add(editFRFontButton);
        this.add(this.fontScopeComboBox);
        this.frFontPreviewPane.setFontObject(FRFont.getInstance());
    }

    @Override
    public String nameForPopupMenuItem() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Sytle_FRFont");
    }


    public  void populate(HighlightAction ha) {
        this.frFontPreviewPane.setFontObject(((FRFontHighlightAction) ha).getFRFont());
        this.fontScopeComboBox.setSelectedIndex(((FRFontHighlightAction) ha).getScope());
    }

    public HighlightAction update() {
        return new FRFontHighlightAction(this.frFontPreviewPane.getFontObject(), this.fontScopeComboBox.getSelectedIndex());
    }
}
