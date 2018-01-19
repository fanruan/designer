package com.fr.design.fun;

import com.fr.stable.fun.mark.Aftermath;
import com.fr.stable.fun.mark.Mutable;

/**
 * 左上角目录树上边工具条的插件接口
 * Created by hzzz on 2017/11/30.
 */
public interface TemplateTreeShortCutProvider extends Mutable, Aftermath {
    String XML_TAG = "TemplateTreeShortCut";

    int CURRENT_LEVEL = 1;
}
