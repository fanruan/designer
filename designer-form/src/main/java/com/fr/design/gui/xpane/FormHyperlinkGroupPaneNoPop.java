package com.fr.design.gui.xpane;

import com.fr.design.gui.frpane.HyperlinkGroupPaneActionProvider;

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

    /**
     * 是否需要将条件属性中的超级链接自动保存到超级链接属性中
     *
     * @return 是否需要自动保存
     */
    @Override
    public boolean needAutoSave() {
        return false;
    }
}
