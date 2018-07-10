package com.fr.design.gui.frpane;

import com.fr.design.designer.TargetComponent;

/**
 * @author plough
 * Created by plough on 2017/7/26.
 */
public interface HyperlinkGroupPaneActionProvider {

    /**
     * 刷新面板展示
     *
     * @param hyperlinkGroupPane 超链面板
     * @param elementCasePane    模板
     */
    void populate(HyperlinkGroupPane hyperlinkGroupPane, TargetComponent elementCasePane);

    /**
     * 保存到文件
     *
     * @param hyperlinkGroupPane 超联面板
     */
    void saveSettings(HyperlinkGroupPane hyperlinkGroupPane);
}
