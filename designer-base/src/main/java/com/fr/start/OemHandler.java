package com.fr.start;

import com.fr.design.fun.OemProcessor;
import com.fr.stable.bridge.StableFactory;

/**
 * OEM处理中心
 */
public class OemHandler {

    public static OemProcessor findOem() {
        return StableFactory.getMarkedInstanceObjectFromClass(OemProcessor.MARK_STRING, OemProcessor.class);
    }
}