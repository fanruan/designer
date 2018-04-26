package com.fr.design.gui.frpane;

import com.fr.design.designer.TargetComponent;

/**
 * Created by plough on 2017/7/26.
 */
public interface HyperlinkGroupPaneActionProvider {
    void populate(HyperlinkGroupPane hyperlinkGroupPane, TargetComponent elementCasePane);
    void saveSettings(HyperlinkGroupPane hyperlinkGroupPane);
}
