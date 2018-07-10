package com.fr.design.fun;

import com.fr.design.style.background.BackgroundDetailPane;
import com.fr.general.Background;
import com.fr.stable.fun.mark.Mutable;

/**
 * Created by richie on 16/5/18.
 */
public interface BackgroundUIProvider extends Mutable {

    String MARK_STRING = "BackgroundUIProvider";

    int CURRENT_LEVEL = 1;

    /**
     * 对应的背景具体类型
     * @return 背景
     */
    Class<? extends Background> targetClass();

    /**
     * 背景设置界面
     * @return 界面
     */
    Class<? extends BackgroundDetailPane> targetUIClass();

    /**
     * 标题
     * @return 在设计界面上这个选项的显示标题
     */
    String targetTitle();
}
