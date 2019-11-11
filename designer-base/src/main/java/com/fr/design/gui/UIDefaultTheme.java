package com.fr.design.gui;


import com.fr.design.constants.UIConstants;
import com.fr.design.utils.ThemeUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 13-12-31
 * Time: 下午2:14
 */
public class UIDefaultTheme extends DefaultMetalTheme {


    /**
     * Secondary Color 1, used for the following:
     * Dark border for flush 3D style.
     */
    private static final ColorUIResource SECONDARY_1 = new ColorUIResource(167, 165, 163);

    /**
     * Secondary Color 2, used for the following:
     * Inactive internal window borders; dimmed button borders.
     * Shadows; highlighting of toolbar buttons upon mouse button down.
     * Dimmed text (for example, inactive menu items or labels).
     */
    private static final ColorUIResource SECONDARY_2 = new ColorUIResource(167, 165, 163);

    /**
     * 增加一些自定义的值到默认表中
     *
     * @param table 默认表
     */
    public void addCustomEntriesToTable(UIDefaults table) {
        super.addCustomEntriesToTable(table);
        putMargin(table);
        putTextConnection(table);
        putToolTip(table);
        putDisable(table);
        putTreeAndList(table);
        putOthers(table);
        table.put("Desktop.background", new ColorUIResource(212, 210, 194));
        table.put("Separator.background", new ColorUIResource(232, 232, 233));
        table.put("Separator.foreground", new ColorUIResource(236, 233, 216));
        table.put("TitledBorder.border", new LineBorder(
                new ColorUIResource(165, 163, 151)));
        table.put("Panel.background", ThemeUtils.BACK_COLOR);
    }

    public FontUIResource getMenuTextFont() {
        return ThemeUtils.PLAIN_FONT;
    }


    private void putMargin(UIDefaults table) {
        table.put("Button.margin", new InsetsUIResource(2, 12, 2, 12));
        table.put("CheckBox.margin", new InsetsUIResource(2, 2, 2, 2));
        table.put("RadioButton.margin", new InsetsUIResource(2, 2, 2, 2));
    }

    protected ColorUIResource getSecondary3() {
        return ThemeUtils.BACK_COLOR;
    }

    private void putOthers(UIDefaults table) {
        table.put("Button.background", new ColorUIResource(231, 232, 245));
        table.put("Table.background", ThemeUtils.TEXT_BG_COLOR);
        table.put("Table.selectionForeground", ThemeUtils.NORMAL_FOREGROUND);
        table.put("Table.selectionBackground", new ColorUIResource(200, 221, 233));
        table.put("TableHeader.background", new ColorUIResource(229, 229, 229));
        table.put("ProgressBar.foreground", ThemeUtils.PROCESS_COLOR);
        table.put("ProgressBar.background", ThemeUtils.TEXT_BG_COLOR);
        table.put("ProgressBar.modern.foreground", new ColorUIResource(0x3497FF));
        table.put("ProgressBar.modern.background", UIConstants.PROPERTY_PANE_BACKGROUND);
        table.put("ProgressBar.selectionForeground", ThemeUtils.NORMAL_FOREGROUND);
        table.put("ProgressBar.selectionBackground", ThemeUtils.NORMAL_FOREGROUND);
        table.put("PopupMenu.background", ThemeUtils.NORMAL_BG);
        table.put("TabbedPane.background", ThemeUtils.NORMAL_BG);
        table.put("TabbedPane.tabAreaInsets", new InsetsUIResource(4, 2, 0, 0));
        table.put("TabbedPane.tabInsets", new InsetsUIResource(1, 6, 4, 6));
        table.put("Table.gridColor", ThemeUtils.TABLE_GRID_COLOR);
        table.put("MenuBar.background", new ColorUIResource(212, 212, 216));
        table.put("Menu.foreground", ThemeUtils.MENU_ITEM_FONT_COLOR);
        table.put("MenuItem.foreground", ThemeUtils.MENU_ITEM_FONT_COLOR);
        table.put("ToolBar.background", new ColorUIResource(239, 237, 229));

        table.put("EditorPane.caretForeground", ThemeUtils.NORMAL_FOREGROUND);
        table.put("PasswordField.caretForeground", ThemeUtils.NORMAL_FOREGROUND);
        table.put("TextArea.caretForeground", ThemeUtils.NORMAL_FOREGROUND);
        table.put("TextField.caretForeground", ThemeUtils.NORMAL_FOREGROUND);
        table.put("FormattedTextField.caretForeground", ThemeUtils.NORMAL_FOREGROUND);
    }

    private void putTextConnection(UIDefaults table) {
        table.put("List.foreground", ThemeUtils.NORMAL_FOREGROUND);
        table.put("List.background", ThemeUtils.TEXT_BG_COLOR);
        table.put("ComboBox.foreground", ThemeUtils.NORMAL_FOREGROUND);
        table.put("ComboBox.background", ThemeUtils.TEXT_BG_COLOR);
        table.put("ComboBox.disabledBackground", ThemeUtils.TEXT_DISABLED_BG_COLOR);
        table.put("EditorPane.foreground", ThemeUtils.NORMAL_FOREGROUND);
        table.put("PasswordField.background", ThemeUtils.TEXT_BG_COLOR);
        table.put("PasswordField.foreground", ThemeUtils.NORMAL_FOREGROUND);
        table.put("PasswordField.inactiveBackground", ThemeUtils.TEXT_DISABLED_BG_COLOR);
        table.put("TextArea.background", ThemeUtils.TEXT_BG_COLOR);
        table.put("TextArea.foreground", ThemeUtils.NORMAL_FOREGROUND);
        table.put("TextArea.inactiveBackground", ThemeUtils.TEXT_DISABLED_BG_COLOR);
        table.put("TextField.background", ThemeUtils.TEXT_BG_COLOR);
        table.put("TextField.foreground", ThemeUtils.NORMAL_FOREGROUND);
        table.put("TextField.inactiveBackground", ThemeUtils.TEXT_DISABLED_BG_COLOR);
        table.put("FormattedTextField.background", ThemeUtils.TEXT_BG_COLOR);
        table.put("FormattedTextField.foreground", ThemeUtils.NORMAL_FOREGROUND);
        table.put("FormattedTextField.inactiveBackground", ThemeUtils.TEXT_DISABLED_BG_COLOR);
        table.put("TextPane.background", ThemeUtils.TEXT_BG_COLOR);
        table.put("EditorPane.background", ThemeUtils.TEXT_BG_COLOR);
        table.put("OptionPane.messageForeground", ThemeUtils.NORMAL_FOREGROUND);
        table.put("ComboBox.selectionBackground", new ColorUIResource(43, 107, 197));
        table.put("ComboBox.selectionForeground", ThemeUtils.TEXT_BG_COLOR);
        table.put("ComboBox.focusBackground", new ColorUIResource(43, 107, 197));
        table.put("PasswordField.selectionBackground", ThemeUtils.TEXT_SELECTED_BG_COLOR);
        table.put("PasswordField.selectionForeground", ThemeUtils.TEXT_SELECTED_TEXT_COLOR);
        table.put("TextField.selectionBackground", ThemeUtils.TEXT_SELECTED_BG_COLOR);
        table.put("TextField.selectionForeground", ThemeUtils.TEXT_SELECTED_TEXT_COLOR);
        table.put("FormattedTextField.selectionBackground", ThemeUtils.TEXT_SELECTED_BG_COLOR);
        table.put("FormattedTextField.selectionForeground", ThemeUtils.TEXT_SELECTED_TEXT_COLOR);
        table.put("TextArea.selectionBackground", ThemeUtils.TEXT_SELECTED_BG_COLOR);
        table.put("TextArea.selectionForeground", ThemeUtils.TEXT_SELECTED_TEXT_COLOR);
        table.put("TextPane.selectionBackground", ThemeUtils.TEXT_SELECTED_BG_COLOR);
        table.put("TextPane.selectionForeground", ThemeUtils.TEXT_SELECTED_TEXT_COLOR);
        table.put("MenuItem.acceleratorForeground", ThemeUtils.NORMAL_FOREGROUND);
    }

    private void putToolTip(UIDefaults table) {
        table.put("ToolTip.background", new ColorUIResource(255, 255, 255));
        table.put("ToolTip.backgroundInactive", new ColorUIResource(236, 233, 216));
        table.put("ToolTip.foreground", new ColorUIResource(0, 0, 0));
        table.put("ToolTip.foregroundInactive", new ColorUIResource(143, 141, 139));
    }

    private void putDisable(UIDefaults table) {

        table.put("Button.disabledText", ThemeUtils.DISABLE_TEXT);
        table.put("CheckBox.disabledText", ThemeUtils.DISABLE_TEXT);
        table.put("RadioButton.disabledText", ThemeUtils.DISABLE_TEXT);
        table.put("ToggleButton.disabledText", ThemeUtils.DISABLE_TEXT);
        table.put("ToggleButton.disabledSelectedText", ThemeUtils.DISABLE_TEXT);
        table.put("TextArea.inactiveForeground", ThemeUtils.DISABLE_TEXT);
        table.put("TextField.inactiveForeground", ThemeUtils.DISABLE_TEXT);
        table.put("FormattedTextField.inactiveForeground", ThemeUtils.DISABLE_TEXT);
        table.put("TextPane.inactiveForeground", ThemeUtils.DISABLE_TEXT);
        table.put("PasswordField.inactiveForeground", ThemeUtils.DISABLE_TEXT);
        table.put("ComboBox.disabledForeground", ThemeUtils.DISABLE_TEXT);
        table.put("Label.disabledForeground", ThemeUtils.DISABLE_TEXT);
        table.put("textInactiveText", ThemeUtils.DISABLE_TEXT);
    }

    private void putTreeAndList(UIDefaults table) {
        table.put("List.selectionForeground", new ColorUIResource(255, 255, 255));
        table.put("List.selectionBackground", ThemeUtils.TEXT_SELECTED_BG_COLOR);
        table.put("Tree.background", ThemeUtils.TEXT_BG_COLOR);
        table.put("Tree.textBackground", ThemeUtils.TEXT_BG_COLOR);
        table.put("Tree.textForeground", ThemeUtils.NORMAL_FOREGROUND);
        table.put("Tree.selectionBackground", ThemeUtils.TEXT_SELECTED_BG_COLOR);
        table.put("Tree.selectionForeground", ThemeUtils.TEXT_SELECTED_TEXT_COLOR);
        table.put("Tree.hash", new ColorUIResource(240, 240,243));
        table.put("Tree.line", new ColorUIResource(240, 240,243));
    }


    /**
     * Gets the first secondary color.
     *
     * @return The first secondary color. See field declaration for more details.
     */
    protected ColorUIResource getSecondary1() {
        return SECONDARY_1;
    }

    /**
     * Gets the second secondary color.
     *
     * @return The second secondary color. See field declaration for more details.
     */
    protected ColorUIResource getSecondary2() {
        return SECONDARY_2;
    }

    public ColorUIResource getMenuSelectedBackground() {
        return new ColorUIResource(200, 200, 255);
    }
}