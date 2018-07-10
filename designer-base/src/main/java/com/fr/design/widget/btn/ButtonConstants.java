package com.fr.design.widget.btn;

import com.fr.form.ui.Button;
import com.fr.form.ui.FreeButton;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import com.fr.stable.bridge.BridgeMark;
import com.fr.stable.bridge.StableFactory;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-15
 * Time   : 下午6:23
 */
public class ButtonConstants {

    public static final String[] HOTKEYS = {
            "esc", "tab", "space", "enter", "backspace", "scroll", "capslock", "numlock", "pause", "insert",
            "home", "del", "end", "pageup", "pagedown", "left", "up", "right", "down",
            "f1", "f2", "f12",
            "1", "9", "0",
            "a", "z",
            "Ctrl+a", "Ctrl+z",
            "Shift+esc", "shift+return",
            "Alt+tab", "Alt+f1"
    };

    public static final String[] TYPES4BUTTON = {
            Inter.getLocText("Common"),
            Inter.getLocText("Custom"),
            Inter.getLocText("Utils-Insert_Row"),
            Inter.getLocText("Utils-Delete_Row"),
            Inter.getLocText(new String[]{"Parameter", "Custom_Button_Type_Submit"}),
            Inter.getLocText("Widget-TreeNode")
    };

    public static final Class[] CLASSES4BUTTON = {
            Button.class,
            FreeButton.class,
            StableFactory.getMarkedClass(BridgeMark.APPEND_ROW_BUTTON, Widget.class),
            StableFactory.getMarkedClass(BridgeMark.DELETE_ROW_BUTTON, Widget.class),
            StableFactory.getMarkedClass(BridgeMark.SUBMIT_BUTTON, Widget.class),
            StableFactory.getMarkedClass(BridgeMark.TREE_NODE_TOGGLE_BUTTON, Widget.class)
    };
}