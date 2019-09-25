package com.fr.start.module;

import com.fr.design.i18n.Toolkit;
import com.fr.event.EventDispatcher;
import com.fr.module.Activator;
import com.fr.module.ModuleEvent;
import com.fr.start.DesignerInitial;

/**
 * Created by juhaoyu on 2019-06-14.
 */
public class DesignerShowActivator extends Activator {

    @Override
    public void start() {
        EventDispatcher.asyncFire(ModuleEvent.MajorModuleStarting, Toolkit.i18nText("Fine-Design_Module_Name_Designer"));
        DesignerInitial.prepare();
    }

    @Override
    public void stop() {
        // void
    }
}
