package com.fr.design.widget.btn;

import com.fr.form.ui.Button;
import com.fr.form.ui.FreeButton;
import com.fr.form.ui.Widget;

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
            com.fr.design.i18n.Toolkit.i18nText("Common"),
            com.fr.design.i18n.Toolkit.i18nText("Custom"),
            com.fr.design.i18n.Toolkit.i18nText("Utils-Insert_Row"),
            com.fr.design.i18n.Toolkit.i18nText("Utils-Delete_Row"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Button_Type_Parameter_Submit"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Widget_TreeNode")
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
