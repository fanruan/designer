package com.fr.design.mainframe.app;

import com.fr.design.mainframe.App;
import com.fr.design.mainframe.JTemplateFactory;
import com.fr.module.Activator;
import com.fr.module.extension.Prepare;

import java.util.List;

/**
 * Created by juhaoyu on 2018/6/27.
 */
public class DesignerAppActivator extends Activator implements Prepare {

    @Override
    public void start() {

        List<App> appList = findMutable(App.KEY);
        for (App app : appList) {
            JTemplateFactory.register(app);
        }
    }

    @Override
    public void stop() {

        List<App> appList = findMutable(App.KEY);
        for (App app : appList) {
            JTemplateFactory.remove(app);
        }
    }

    @Override
    public void prepare() {

        addMutable(App.KEY, new CptApp(), new FormApp(), new XlsApp(), new XlsxApp());

    }
}
