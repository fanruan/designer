package com.fr.design.mainframe;

import com.fr.base.io.IOFile;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

/**
 * Created by Administrator on 2016/3/17/0017.
 */
@API(level = App.CURRENT_LEVEL)
public abstract class AbstractAppProvider<T extends IOFile> extends AbstractProvider implements App {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }

    @Override
    public void process() {
        JTemplateFactory.register(this);
    }

    @Override
    public void undo() {

        JTemplateFactory.remove(this);
    }
}
