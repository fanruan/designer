package com.fr.design.fun;

import com.fr.stable.fun.mark.Immutable;

/**
 * Created by Administrator on 2016/8/26.
 */
public interface PluginInstallOptionProcessor extends Immutable {

    String MARK_STRING = "PluginInstallOptionProcessor";

    int CURRENT_LEVEL = 1;

    /**
     * 安裝插件時的其他操作
     */
    void pluginInstallOption() throws Exception;
}

