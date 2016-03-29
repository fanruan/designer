package com.fr.design.mainframe;

import com.fr.base.io.IOFile;

/**
 * Created by Administrator on 2016/3/17/0017.
 */
public abstract class AbstractAppProvider<T extends IOFile> implements App{

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }
}
