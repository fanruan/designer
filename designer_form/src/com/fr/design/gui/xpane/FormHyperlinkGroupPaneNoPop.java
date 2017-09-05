package com.fr.design.gui.xpane;

import com.fr.design.gui.frpane.HyperlinkGroupPaneActionProvider;

import java.awt.*;

/**
 * Created by plough on 2017/9/5.
 */
public class FormHyperlinkGroupPaneNoPop extends FormHyperlinkGroupPane{
    private static FormHyperlinkGroupPaneNoPop singleton;

    private FormHyperlinkGroupPaneNoPop(HyperlinkGroupPaneActionProvider hyperlinkGroupPaneActionProvider) {
        super(hyperlinkGroupPaneActionProvider);
    }

    public static FormHyperlinkGroupPaneNoPop getInstance(HyperlinkGroupPaneActionProvider hyperlinkGroupPaneActionProvider) {
        if (singleton == null) {
            singleton = new FormHyperlinkGroupPaneNoPop(hyperlinkGroupPaneActionProvider);
        }
        return singleton;
    }

    @Override
    protected boolean isNewStyle() {
        return false;
    }

    @Override
    protected void popupEditDialog(Point mousePos) {
        // no pop, do nothing
    }
}
