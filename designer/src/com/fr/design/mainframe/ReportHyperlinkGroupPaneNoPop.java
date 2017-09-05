package com.fr.design.mainframe;

import com.fr.design.gui.frpane.HyperlinkGroupPaneActionProvider;

/**
 * Created by plough on 2017/9/4.
 */
public class ReportHyperlinkGroupPaneNoPop extends ReportHyperlinkGroupPane{
    private static ReportHyperlinkGroupPaneNoPop singleton;

    private ReportHyperlinkGroupPaneNoPop(HyperlinkGroupPaneActionProvider hyperlinkGroupPaneActionProvider) {
        super(hyperlinkGroupPaneActionProvider);
    }

    public static ReportHyperlinkGroupPaneNoPop getInstance(HyperlinkGroupPaneActionProvider hyperlinkGroupPaneActionProvider) {
        if (singleton == null) {
            singleton = new ReportHyperlinkGroupPaneNoPop(hyperlinkGroupPaneActionProvider);
        }
        singleton.refreshPane();
        return singleton;
    }

    @Override
    protected boolean isNewStyle() {
        return false;
    }
}
