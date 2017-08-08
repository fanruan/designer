package com.fr.design.widget.ui.designer;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.style.FRFontPane;
import com.fr.design.widget.component.ButtonBackgroundPane;
import com.fr.form.ui.FreeButton;
import com.fr.general.FRFont;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ibm on 2017/8/6.
 */
public class FreeButtonDefinePane extends ButtonDefinePane<FreeButton> {
    private ButtonBackgroundPane backgroundCompPane;
    private FRFontPane frFontPane;

    public FreeButtonDefinePane(XCreator xcreator) {
        super(xcreator);
    }

    public Component[] createBackgroundComp() {
        backgroundCompPane = new ButtonBackgroundPane();
        return new Component[]{new UILabel(Inter.getLocText("FR-Designer_Background") + ":"), backgroundCompPane};
    }

    public Component[] createFontPane() {
        UILabel fontLabel = new UILabel(Inter.getLocText("FR-Designer_Font"));
        fontLabel.setVerticalAlignment(SwingConstants.TOP);
        frFontPane = new FRFontPane();
        return new Component[]{fontLabel, frFontPane};
    }

    public void populateSubButtonPane(FreeButton e) {
        backgroundCompPane.populate(e);
        FRFont frFont = e.getFont();
        if (frFont != null) {
            frFontPane.populateBean(e.getFont());
        }
    }

    public FreeButton updateSubButtonPane() {
        FreeButton freeButton = (FreeButton) creator.toData();
        backgroundCompPane.update(freeButton);
        frFontPane.update(freeButton.getFont());
        return freeButton;
    }
}
