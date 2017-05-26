package com.fr.common.inputevent;

import com.fr.stable.OperatingSystem;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Created by hzzz on 2017/5/26.
 */
public class InputEventBaseOnOS {
    private static final boolean isMacOS = OperatingSystem.isMacOS();

    public static boolean isControlDown(MouseEvent e) {
        return isMacOS ? e.isMetaDown() : e.isControlDown();
    }

    public static boolean isControlDown(KeyEvent e) {
        return isMacOS ? e.isMetaDown() : e.isControlDown();
    }
}
