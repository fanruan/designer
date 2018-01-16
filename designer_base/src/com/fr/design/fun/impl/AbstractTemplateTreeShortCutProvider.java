package com.fr.design.fun.impl;

import com.fr.design.actions.UpdateAction;
import com.fr.design.fun.TemplateTreeShortCutProvider;
import com.fr.stable.fun.mark.API;

/**
 * 左上角目录树上边工具条的插件接口
 * Created by hzzz on 2017/11/30.
 */
@API(level = TemplateTreeShortCutProvider.CURRENT_LEVEL)
public abstract class AbstractTemplateTreeShortCutProvider extends UpdateAction implements TemplateTreeShortCutProvider {

    public void notifyFromAuhtorityChange(boolean isAuhtority) {
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }

    @Override
    public void process() {
    }

    @Override
    public void undo() {
    }
}
