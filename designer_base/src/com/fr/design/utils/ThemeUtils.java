package com.fr.design.utils;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-13
 * Time: 下午3:04
 */
public class ThemeUtils {
    public static final ColorUIResource NORMAL_FOREGROUND = new ColorUIResource(0, 0, 0);
    public static final ColorUIResource TEXT_SELECTED_BG_COLOR = new ColorUIResource(43, 107, 197);
    public static final ColorUIResource TEXT_SELECTED_TEXT_COLOR = new ColorUIResource(255, 255, 255);
    public static final ColorUIResource NORMAL_BG = new ColorUIResource(212, 212, 216);
    public static final ColorUIResource DISABLE_TEXT = new ColorUIResource(143, 142, 139);
    public static final ColorUIResource TEXT_DISABLED_BG_COLOR = new ColorUIResource(244, 243, 233);
    public static final ColorUIResource TEXT_BG_COLOR = new ColorUIResource(255, 255, 255);
    public static final ColorUIResource TEXT_BORDER_DISABLED_COLOR = new ColorUIResource(201, 198, 184);
    public static final ColorUIResource TEXT_BORDER_COLOR = new ColorUIResource(128, 152, 186);
    public static final Insets TEXT_INSETS = new Insets(2, 3, 2, 3);

    public static final ColorUIResource BUTTON_DISABLE_COLOR = new ColorUIResource(245, 244, 235);
    public static final ColorUIResource BUTTON_PRESS_COLOR = new ColorUIResource(217, 218, 230);
    public static final ColorUIResource BUTTON_NORMAL_COLOR = new ColorUIResource(231, 232, 245);
    public static final ColorUIResource BUTTON_ROLLOVER_COLOR = new ColorUIResource(248, 179, 48);
    public static final ColorUIResource BUTTON_ROLLOVER_BG_COLOR = new ColorUIResource(239, 240, 248);
    public static final ColorUIResource BUTTON_BORDER_COLOR = new ColorUIResource(148, 148, 148);
    public static final ColorUIResource BUTTON_BORDER_DISABLE_COLOR = new ColorUIResource(201, 198, 184);
    public static final ColorUIResource BUTTON_DEFAULT_COLOR = new ColorUIResource(160, 182, 235);
    public static final ColorUIResource BUTTON_CHECK_COLOR = new ColorUIResource(34, 161, 34);
    public static final ColorUIResource BUTTON_CHECK_DISABLE_COLOR = new ColorUIResource(208, 205, 190);

    public static final ColorUIResource RADIO_BORDER_NORMAL_COLOR = new ColorUIResource(0xababab);
    public static final ColorUIResource RADIO_CHECK_ROLLOVER_COLOR = new ColorUIResource(0x3384f0);

    public static final boolean BUTTON_FOCUS_BORDER = false;
    public static final boolean BUTTON_ROLLOVER = true;
    public static final boolean BUTTON_ENTER = false;
    public static final boolean BUTTON_FOCUS = true;
    public static final boolean TOOL_FOCUS = false;
    public static final boolean SHIFT_BUTTON_TEXT = true;

    public static final int BUTTON_SPREAD_LIGHT = 20;
    public static final int BUTTON_SPREAD_DARK = 3;
    public static final int BUTTON_SPREAD_LIGHT_DISABLE = 20;
    public static final int BUTTON_SPREAD_DARK_DISABLE = 1;

    public static final ColorUIResource BACK_COLOR = new ColorUIResource(245, 245, 247);
    public static final ColorUIResource TOOL_SEP_DARK_COLOR = new ColorUIResource(220, 220, 220);
    public static final ColorUIResource SCROLL_TRACK_COLOR = new ColorUIResource(249, 249, 247);
    public static final ColorUIResource SCROLL_TRACK_DISABLED_COLOR = new ColorUIResource(249, 249, 247);
    public static final ColorUIResource SCROLL_TRACK_BORDER_COLOR = new ColorUIResource(234, 231, 218);
    public static final ColorUIResource SCROLL_TRACK_BORDER_DISABLED_COLOR = new ColorUIResource(234, 231, 218);
    public static final ColorUIResource SCROLL_THUMB_PRESSED_COLOR = new ColorUIResource(96, 189, 246);
    public static final ColorUIResource SCROLL_THUMB_DISABLED_COLOR = new ColorUIResource(238, 238, 231);
    public static final ColorUIResource SCROLL_GRIPLIGHT_COLOR = new ColorUIResource(238, 243, 254);
    public static final ColorUIResource SCROLL_GRIP_DARK_COLOR = new ColorUIResource(171, 185, 219);
    public static final ColorUIResource SCROLL_THUMB_ROLLOVER_COLOR = new ColorUIResource(165, 217, 249);
    public static final ColorUIResource SCROLL_THUMB_COLOR = new ColorUIResource(210, 210, 210);
    public static final ColorUIResource SCROLL_BUTTON_DISABLED_COLOR = new ColorUIResource(238, 237, 231);
    public static final ColorUIResource SCROLL_BUTTON_PRESSED_COLOR = new ColorUIResource(96, 189, 246);
    public static final ColorUIResource SCROLL_BUTTON_ROLLOVER_COLOR = new ColorUIResource(165, 217, 249);
    public static final ColorUIResource SCROLL_BUTTON_COLOR = new ColorUIResource(210, 210, 210);
    public static final ColorUIResource SCROLL_ARROW_DISABLED_COLOR = new ColorUIResource(193, 193, 193);
    public static final ColorUIResource SCROLL_ARROW_COLOR = new ColorUIResource(112, 112, 112);


    public static final ColorUIResource ROLLOVER_PRESSED_COLOR_8 = new ColorUIResource(0xd2d2d2);
    public static final ColorUIResource NORMAL__COLOR_8 = new ColorUIResource(0xe2e2e2);
    public static final ColorUIResource PRESSED_LIGHT_COLOR = new ColorUIResource(205, 234, 252);
    public static final ColorUIResource PRESSED_DARK_COLOR = new ColorUIResource(96, 189, 246);
    public static final ColorUIResource ROLLOVER_LIGHT_COLOR = new ColorUIResource(205, 234, 252);
    public static final ColorUIResource ROLLOVER_DARK_COLOR = new ColorUIResource(165, 217, 249);
    public static final ColorUIResource NORMAL_LIGHT_COLOR = new ColorUIResource(236, 236, 236);
    public static final ColorUIResource NORMAL_DARK_COLOR = new ColorUIResource(210, 210, 210);
    public static final ColorUIResource WHITE_BORDER_COLOR = new ColorUIResource(212, 212, 216);
    public static final ColorUIResource SCROLL_BORDER_COLOR = new ColorUIResource(198, 198, 198);
    public static final ColorUIResource BORDER_COLOR = new ColorUIResource(148, 148, 148);
    public static final boolean SCROLL_ROLLOVER = true;


    public static final ColorUIResource COMBOBUTT_DISABLED_COLOR = new ColorUIResource(238, 237, 231);
    public static final ColorUIResource COMBOBUTT_PRESSED_COLOR = new ColorUIResource(96, 189, 246);
    public static final ColorUIResource COMBOBUTT_ROLLOVER_COLOR = new ColorUIResource(165, 217, 249);
    public static final ColorUIResource COMBOBUTT_COLOR = new ColorUIResource(210, 210, 210);
    public static final ColorUIResource COMBO_ARROW_COLOR = new ColorUIResource(77, 100, 132);
    public static final ColorUIResource COMBO_ARROW_DISABLED_COLOR = new ColorUIResource(203, 200, 186);
    public static final ColorUIResource COMBO_BORDER_COLOR = new ColorUIResource(198, 198, 198);
    public static final ColorUIResource COMBO_BORDER_DISABLED_COLOR = new ColorUIResource(201, 198, 184);
    public static final ColorUIResource MAIN_COLOR = new ColorUIResource(0, 106, 255);
    public static final Insets COMBO_INSETS = new Insets(2, 2, 2, 2);
    public static final int COMBOBUTTTON_WIDTH = 18;
    public static final boolean COMBO_FOCUS = false;
    public static final boolean SPINNER_ROLLOVER = false;

    public static final ColorUIResource TOOLBUTT_COLOR = new ColorUIResource(new Color(255, 255, 255, 0));
    public static final ColorUIResource TOOLBUTT_SELECTED_COLOR = new ColorUIResource(243, 242, 239);
    public static final ColorUIResource TOOLBUTT_ROLLOVER_COLOR = new ColorUIResource(251, 251, 248);
    public static final ColorUIResource TOOLBUTT_PRESSED_COLOR = new ColorUIResource(225, 224, 218);
    public static final ColorUIResource TOOL_BORDER_COLOR = new ColorUIResource(new Color(239, 237, 229, 0));
    public static final ColorUIResource TOOL_BORDER_PRESSED_COLOR = new ColorUIResource(122, 144, 174);
    public static final ColorUIResource TOOL_BORDER_ROLLOVER_COLOR = new ColorUIResource(122, 144, 174);
    public static final ColorUIResource TOOL_BORDER_SELECTED_COLOR = new ColorUIResource(122, 144, 174);
    public static final ColorUIResource TOOL_BAR_LIGHT_COLOR = new ColorUIResource(255, 255, 255);
    public static final ColorUIResource TOOL_BAR_COLOR = new ColorUIResource(239, 237, 229);
    public static final ColorUIResource TOOL_BAR_DARK_COLOR = new ColorUIResource(168, 169, 169);
    public static final ColorUIResource TOOL_GRIP_LIGHT_COLOR = new ColorUIResource(255, 255, 255);
    public static final ColorUIResource TOOL_GRIP_DARK_COLOR = new ColorUIResource(220, 220, 220);


    public static final ColorUIResource DEFAULT_LIGHT_COLOR = new ColorUIResource(198, 220, 233);
    public static final ColorUIResource DEFAULT_DARK_COLOR = new ColorUIResource(161, 194, 213);
    public static final Dimension CHECK_SIZE = new Dimension(13, 13);
    public static final int TOOL_MARGIN_TOP = 5;
    public static final int TOOL_MARGIN_LEFT = 5;
    public static final int TOOL_MARGIN_BOTTOM = 5;
    public static final int TOOL_MARGIN_RIGHT = 5;

    public static final ColorUIResource TABLE_GRID_COLOR = new ColorUIResource(210, 210, 210);
    public static final ColorUIResource PROCESS_SELECTED_FORECOLOR = new ColorUIResource(0, 0, 0);
    public static final ColorUIResource PROCESS_SELECTED_BACKCOLOR = new ColorUIResource(0, 0, 0);
    public static final ColorUIResource PROCESS_BORDER_COLOR = new ColorUIResource(104, 104, 104);
    public static final ColorUIResource PROCESS_DARK_COLOR = new ColorUIResource(190, 190, 190);
    public static final ColorUIResource PROCESS_LIGHT_COLOR = new ColorUIResource(238, 238, 238);
    public static final ColorUIResource PROCESS_TRACK_COLOR = new ColorUIResource(255, 255, 255);
    public static final ColorUIResource PROCESS_COLOR = new ColorUIResource(44, 212, 43);

    public static final FontUIResource PLAIN_FONT = new FontUIResource("Tahoma", Font.PLAIN, 11);
    public static final ColorUIResource MENU_ITEM_FONT_COLOR = new ColorUIResource(0, 0, 0);
    public static final ColorUIResource MENU_DISABLED_FG_COLOR = new ColorUIResource(143, 142, 139);
    public static final ColorUIResource MENU_ROLLOVER_FG_COLOR = new ColorUIResource(0, 0, 0);
    public static final ColorUIResource MENU_ROLLOVER_BG_COLOR = new ColorUIResource(189, 208, 234);
    public static final ColorUIResource MENU_ITEM_ROLLOVER_COLOR = new ColorUIResource(189, 208, 234);
    public static final ColorUIResource MENU_FONT_COLOR = new ColorUIResource(0, 0, 0);
    public static final ColorUIResource MENU_SELECTED_TEXT_COLOR = new ColorUIResource(0, 0, 0);
    public static final ColorUIResource MENU_ICON_SHADOW_COLOR = new ColorUIResource(236, 233, 216);
    public static final ColorUIResource MENU_ICON_DISABLED_COLOR = new ColorUIResource(165, 163, 151);
    public static final ColorUIResource MENU_BAR_COLOR = new ColorUIResource(212, 212, 216);
    public static final ColorUIResource MENU_POPUP_COLOR = new ColorUIResource(255, 255, 255);
    public static final ColorUIResource MENU_BORDER_COLOR = new ColorUIResource(173, 170, 153);
    public static final boolean MENU_ROLLOVER = true;


    public static final ColorUIResource SCROLL_PANE_BORDER_COLOR = new ColorUIResource(0xD9DADD);
    public static final ColorUIResource TABLE_BORDER_LIGHT_COLOR = new ColorUIResource(210, 210, 210);
    public static final ColorUIResource TABLE_BORDER_DARK_COLOR = new ColorUIResource(210, 210, 210);


    public static final boolean FRAME_IS_TRANSPARENT = true;
    public static final int FRAME_PALETTE_TITLE_HEIGHT = 21;
    public static final int FRAME_INTERNAL_TITLE_HEIGHT = 25;
    public static final ColorUIResource FRAME_BORDER_COLOR = new ColorUIResource(0, 60, 161);
    public static final ColorUIResource FRAME_BORDER_DISABLED_COLOR = new ColorUIResource(74, 125, 212);
    public static final ColorUIResource FRAME_CAPTION_COLOR = new ColorUIResource(38, 111, 255);
    public static final ColorUIResource FRAME_CAPTION_DISABLED_COLOR = new ColorUIResource(122, 159, 223);
    public static final int FRAME_SPREAD_DARK_DISABLED = 2;
    public static final int FRAME_SPREAD_LIGHT_DISABLED = 2;
    public static final int FRAME_SPREAD_DARK = 6;
    public static final int FRAME_SPREAD_LIGHT = 2;
    public static final ColorUIResource FRAME_LIGHT_COLOR = new ColorUIResource(0, 68, 184);
    public static final ColorUIResource FRAME_DARK_COLOR = new ColorUIResource(236, 233, 216);
    public static final ColorUIResource FRAME_DARK_DISABLED_COLOR = new ColorUIResource(236, 233, 216);
    public static final ColorUIResource FRAME_LIGHT_DISABLED_COLOR = new ColorUIResource(99, 144, 233);
    public static final int FRAME_BORDER_WIDTH = 3;
    public static final int FRAME_TITLE_HEIGHT = 29;


    public static final ColorUIResource SPINNER_BUTT_DISABLED_COLOR = new ColorUIResource(242, 240, 228);
    public static final ColorUIResource SPINNER_BUTT_COLOR = new ColorUIResource(198, 213, 250);
    public static final ColorUIResource SPINNER_BUTT_ROLLOVER_COLOR = new ColorUIResource(232, 238, 254);
    public static final ColorUIResource SPINNER_BUTT_PRESSED_COLOR = new ColorUIResource(175, 190, 224);
    public static final ColorUIResource SPINNER_ARROW_DISABLED_COLOR = new ColorUIResource(212, 210, 194);
    public static final ColorUIResource SPINNER_ARROW_COLOR = new ColorUIResource(77, 100, 132);
    public static final ColorUIResource SPINNER_BORDER_DISABLED_COLOR = new ColorUIResource(215, 212, 197);

    public static final ColorUIResource TABLE_HEADER_DARK_COLOR = new ColorUIResource(189, 186, 173);
    public static final ColorUIResource TABLE_HEADER_LIGHT_COLOR = new ColorUIResource(255, 255, 255);
    public static final ColorUIResource TABLE_HEADER_ROLLOVER_COLOR = new ColorUIResource(248, 179, 48);


}