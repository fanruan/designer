package com.fr.design.fun;

import com.fr.design.mainframe.backgroundpane.BackgroundQuickPane;
import com.fr.stable.fun.Level;
import com.fr.stable.fun.Provider;

/**
 * Created by richie on 16/5/18.
 * 背景设置界面接口,用于扩展设置更多类型的背景
 */
public interface BackgroundQuickUIProvider extends Level, Provider {

    String MARK_STRING = "BackgroundQuickUIProvider";

    int CURRENT_LEVEL = 1;

    /**
     * 背景设置界面
     * @return 设置界面
     */
    BackgroundQuickPane appearanceForBackground();
}
