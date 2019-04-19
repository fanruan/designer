package com.fr.start;

import com.fr.design.fun.OemProcessor;
import com.fr.module.Module;
import com.fr.module.ModuleContext;

/**
 * OEM处理中心
 */
public class OemHandler {
    public static OemProcessor findOem() {
        Module oemModule = ModuleContext.getModule(OemActivator.class);
        if (oemModule != null) {
            return oemModule.getSingleton(OemProcessor.class);
        }
        return null;
    }
}