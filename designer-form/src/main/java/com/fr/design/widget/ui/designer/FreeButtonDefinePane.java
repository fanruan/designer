package com.fr.design.widget.ui.designer;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.style.FRFontPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.widget.component.ButtonBackgroundPane;
import com.fr.form.ui.FreeButton;
import com.fr.general.FRFont;


import javax.swing.*;
import java.awt.*;

/**
 * Created by ibm on 2017/8/6.
 */
public class FreeButtonDefinePane extends ButtonDefinePane<FreeButton> {
    private ButtonBackgroundPane backgroundCompPane;
    private FRFontPane frFontPane;
    private UILabel fontLabel;

    public FreeButtonDefinePane(XCreator xcreator) {
        super(xcreator);
    }

    public Component[] createBackgroundComp() {
        backgroundCompPane = new ButtonBackgroundPane();
        return new Component[]{backgroundCompPane,null};
    }

    public Component[] createFontPane() {
        JPanel fontLabelPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        fontLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Font"));
        fontLabelPanel.add(fontLabel, BorderLayout.CENTER);
        fontLabel.setVerticalAlignment(SwingConstants.TOP);
        frFontPane = new FRFontPane();
        JPanel fontPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        fontPanel.add(frFontPane, BorderLayout.CENTER);
        return new Component[]{fontLabelPanel, fontPanel};
    }

    public void populateSubButtonPane(FreeButton e) {
        backgroundCompPane.populate(e);
        frFontPane.setVisible(e.isCustomStyle());
        fontLabel.setVisible(e.isCustomStyle());
        if(e.isCustomStyle()){
            FRFont frFont = e.getFont();
            if (frFont != null) {
                frFontPane.populateBean(e.getFont());
            }
        }
    }

    public FreeButton updateSubButtonPane() {
        FreeButton freeButton = (FreeButton) creator.toData();
        backgroundCompPane.update(freeButton);
        frFontPane.setVisible(freeButton.isCustomStyle());
        fontLabel.setVisible(freeButton.isCustomStyle());
        if(freeButton.isCustomStyle()){
            FRFont frFont = freeButton.getFont() == null ? FRFont.getInstance() : freeButton.getFont();
            freeButton.setFont(frFontPane.update(frFont));
        }
        return freeButton;
    }
}
