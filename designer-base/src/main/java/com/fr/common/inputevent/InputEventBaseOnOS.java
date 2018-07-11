package com.fr.common.inputevent;

import com.fr.stable.OperatingSystem;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

/**
 * Created by hzzz on 2017/5/26.
 */
public class InputEventBaseOnOS {
    private static final boolean IS_MACOS = OperatingSystem.isMacOS();

    public static boolean isControlDown(MouseEvent e) {
        return IS_MACOS ? e.isMetaDown() : e.isControlDown();
    }

    public static boolean isControlDown(KeyEvent e) {
        return IS_MACOS ? e.isMetaDown() : e.isControlDown();
    }

    public static void addBasicEditInputMap(JComponent jComponent) {
        InputMap inputMap = jComponent.getInputMap();
        while (inputMap.getParent() != null) {
            inputMap = inputMap.getParent();
        }
        if (inputMap.get(KeyStroke.getKeyStroke(KeyEvent.VK_A, DEFAULT_MODIFIER)) == null) {
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, DEFAULT_MODIFIER), DefaultEditorKit.selectAllAction);
        }
        if (inputMap.get(KeyStroke.getKeyStroke(KeyEvent.VK_C, DEFAULT_MODIFIER)) == null) {
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, DEFAULT_MODIFIER), DefaultEditorKit.copyAction);
        }
        if (inputMap.get(KeyStroke.getKeyStroke(KeyEvent.VK_V, DEFAULT_MODIFIER)) == null) {
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, DEFAULT_MODIFIER), DefaultEditorKit.pasteAction);
        }
        if (inputMap.get(KeyStroke.getKeyStroke(KeyEvent.VK_X, DEFAULT_MODIFIER)) == null) {
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, DEFAULT_MODIFIER), DefaultEditorKit.cutAction);
        }
    }
}
