package com.fr.design.fun;

import com.fr.common.annotations.Open;
import com.fr.stable.fun.mark.Mutable;

import java.util.List;

/**
 * Created by kerry on 2019-11-11
 */
@Open
public interface MultiStyleUIConfigProvider extends Mutable {
    String XML_TAG = "MultiStyleUIConfigProvider";

    int CURRENT_LEVEL = 1;

    /**
     * 获取配置项list
     *
     * @return 配置项list
     */
    List<CustomStyleUIConfigProvider> getConfigList();
}
