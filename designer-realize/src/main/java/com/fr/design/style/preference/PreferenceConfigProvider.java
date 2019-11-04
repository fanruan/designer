package com.fr.design.style.preference;

import com.fr.stable.fun.mark.Mutable;

import java.util.List;

/**
 * Created by kerry on 2019-11-04
 */
public interface PreferenceConfigProvider extends Mutable {

    String XML_TAG = "PreferenceConfigProvider";

    int CURRENT_LEVEL = 1;

    /**
     * 获取tab配置项
     * @return tab配置项
     */
    List<PreferenceTabConfig> getConfigList();

}
