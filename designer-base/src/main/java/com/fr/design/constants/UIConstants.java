/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.constants;

import com.fr.general.IOUtils;
import com.fr.stable.Constants;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.border.Border;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

/**
 * This class defines the constants used in the designer.
 */
public interface UIConstants {

    public static final Icon CPT_ICON = IOUtils.readIcon("/com/fr/base/images/oem/cpt.png");
    public static final Icon BLACK_ICON = IOUtils.readIcon("/com/fr/base/images/cell/blank.gif");

    public static final Image APPFIT_V0 = IOUtils.readImage("/com/fr/design/images/dialog/appfit/V0.png");
    public static final Image APPFIT_V1 = IOUtils.readImage("/com/fr/design/images/dialog/appfit/V1.png");
    public static final Image APPFIT_V2 = IOUtils.readImage("/com/fr/design/images/dialog/appfit/V2.png");
    public static final Image APPFIT_V3 = IOUtils.readImage("/com/fr/design/images/dialog/appfit/V3.png");
    public static final Image APPFIT_V4 = IOUtils.readImage("/com/fr/design/images/dialog/appfit/V4.png");
    public static final Image APPFIT_H0 = IOUtils.readImage("/com/fr/design/images/dialog/appfit/H0.png");
    public static final Image APPFIT_H1 = IOUtils.readImage("/com/fr/design/images/dialog/appfit/H1.png");
    public static final Image APPFIT_H2 = IOUtils.readImage("/com/fr/design/images/dialog/appfit/H2.png");
    public static final Image APPFIT_H3 = IOUtils.readImage("/com/fr/design/images/dialog/appfit/H3.png");
    public static final Image APPFIT_H4 = IOUtils.readImage("/com/fr/design/images/dialog/appfit/H4.png");

    public static final Border CELL_ATTR_ZEROBORDER = BorderFactory.createEmptyBorder(0, 0, 0, 0);
    public static final Border CELL_ATTR_EMPTYBORDER = BorderFactory.createEmptyBorder(0, 10, 0, 0);
    public static final Border CELL_ATTR_PRESENTBORDER = BorderFactory.createEmptyBorder(0, 5, 0, 0);
    public static final Border CELL_ATTR_NORMALBORDER = BorderFactory.createEmptyBorder(0, 10, 0, 15);


    public static final int SIZE = 17;

    public static final int GAP_NORMAL = 10;  // 10px

    /**
     * Cell default cursor.
     */
    public static final Cursor CELL_DEFAULT_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
            IOUtils.readImage("/com/fr/base/images/cell/cursor/cell_default.png"),
            new Point(16, 16), "CellDefaultCursor");
    public static final Cursor DRAW_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
            IOUtils.readImage("/com/fr/base/images/cell/cursor/cursor_draw.png"),
            new Point(16, 16), "DrawCursor");


    public static final Cursor FORMAT_BRUSH_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
            IOUtils.readImage("/com/fr/base/images/cell/cursor/brush_cursor0.png"),
            new Point(16, 16), "formatBrushCursor");

    /**
     * Border style array.
     */
    public final static int[] BORDER_LINE_STYLE_ARRAY = new int[]{
            Constants.LINE_THIN, //Thin border.
            Constants.LINE_MEDIUM, //Medium border
            Constants.LINE_DASH, //dash border
            Constants.LINE_HAIR, //hair-line border
            Constants.LINE_HAIR2, //hair-line border
            Constants.LINE_THICK, //Thick border
            Constants.LINE_DOUBLE, //double-line border
            Constants.LINE_DOT, //dot border
            Constants.LINE_MEDIUM_DASH, //Medium dashed border
            Constants.LINE_DASH_DOT, //dash-dot border
            Constants.LINE_MEDIUM_DASH_DOT, //medium dash-dot border
            Constants.LINE_DASH_DOT_DOT, //dash-dot-dot border
            Constants.LINE_MEDIUM_DASH_DOT_DOT, //medium dash-dot-dot border
            Constants.LINE_SLANTED_DASH_DOT, //slanted dash-dot border
    };
    public static final Color TOOLBAR_BORDER_COLOR = new Color(0xD9DADD);
    public static final Color COMBOBOX_BTN_NORMAL = new Color(0xD9DADD);
    public static final Color COMBOBOX_BTN_ROLLOVER = new Color(0xC8C9CD);
    public static final Color COMBOBOX_BTN_PRESS = new Color(0xD8F2FD);
    public static final Color UIPOPUPMENU_LINE_COLOR = new Color(0xC8C9CD);
    public static final Color UIPOPUPMENU_BACKGROUND = new Color(0xEDEDEE);
    public static final Color LINE_COLOR = new Color(153, 153, 153);
    public static final Color FONT_COLOR = new Color(51, 51, 51);
    public static final Color LIGHT_BLUE = new Color(182, 217, 253);
    public static final Color SKY_BLUE = new Color(164, 192, 220);
    public static final Color OCEAN_BLUE = new Color(141, 179, 217);
    public static final Color DARK_BLUE = new Color(0, 88, 144);
    public static final Color NORMAL_BACKGROUND = new Color(0xe0e0e3);
    public static final Color TREE_BACKGROUND = new Color(245, 245, 247);
    public static final Color TOOL_PANE_BACKGROUND = new Color(232, 232, 223);
    public static final Color SELECT_TAB = new Color(245, 245, 247);
    public static final Color TOOLBARUI_BACKGROUND = new Color(255, 255, 255);
    public static final Color SHADOW_GREY = new Color(217, 218, 221);
    public static final Color SHADOW_CENTER = new Color(200, 200, 200);
    public static final Color SHADOW_PURPLE = new Color(255, 0, 255);
    public static final Color FLESH_BLUE = new Color(65, 155, 249);
    public static final Color HOVER_BLUE = new Color(0xd2d2d2);
    public static final Color DOTTED_LINE_COLOR = new Color(35, 108, 184);
    public static final Color AUTHORITY_COLOR = new Color(88, 125, 153);
    public static final Color AUTHORITY_BLUE = new Color(0xe2e2e2);
    public static final Color AUTHORITY_DARK_BLUE = new Color(136, 164, 186);
    public static final Color AUTHORITY_PRESS_BLUE = new Color(131, 159, 181);
    public static final Color AUTHORITY_LINE_COLOR = new Color(0, 124, 229);
    public static final Color AUTHORITY_SHEET_DARK = new Color(86, 120, 143);
    public static final Color AUTHORITY_SHEET_LIGHT = new Color(156, 204, 238);
    public static final Color AUTHORITY_SHEET_UNSELECTED = new Color(146, 192, 225);
    public static final Color ATTRIBUTE_PRESS = new Color(0x419BF9);
    public static final Color NORMAL_BLUE = new Color(0x419BF9);
    public static final Color DISABLED_ICON_COLOR = new Color(170, 170, 171);
    public static final Color ATTRIBUTE_NORMAL = Color.WHITE;
    public static final Color ATTRIBUTE_HOVER = new Color(0xF5F5F7);
    public static final Color UI_TOOLBAR_COLOR = new Color(0xF5F5F7);
    public static final Color DIALOG_TITLEBAR_BACKGROUND = new Color(0xFCFCFD);
    public static final Color CHECKBOX_HOVER_SELECTED = new Color(0x3394f0);
    public static final Color TEXT_FILED_BORDER_SELECTED = new Color(0x3384f0);
    public static final Color SHEET_NORMAL = new Color(0xc8c8ca);
    public static final Color SELECTED_BACKGROUND = new Color(0xdeedfe);
    public static final Color SELECTED_BORDER_LINE_COLOR = new Color(0x3384f0);
    public static final Color DEFAULT_BG_RULER = new Color(0xffffff);
    public static final Color RULER_LINE_COLOR = new Color(0xD9DADD);
    public static final Color RULER_SCALE_COLOR = new Color(0x4e504f);
    public static final Color PROPERTY_PANE_BACKGROUND = new Color(0xe8e8e9);
    public static final Color SPLIT_LINE = new Color(201, 198, 184);
    public static final Color TITLED_BORDER_COLOR = new Color(0xe8e8e9);
    public static final Color GRID_ROW_DETAILS_BACKGROUND = new Color(0xe8e8e9);
    public static final Color GRID_COLUMN_DETAILS_BACKGROUND = GRID_ROW_DETAILS_BACKGROUND;
    public static final Color TEMPLATE_TAB_PANE_BACKGROUND = NORMAL_BACKGROUND;
    public static final Color LOG_MESSAGE_BAR_BACKGROUND = TEMPLATE_TAB_PANE_BACKGROUND;
    public static final Color UI_MENU_BACKGOURND = LOG_MESSAGE_BAR_BACKGROUND;
    public static final Color POP_DIALOG_BORDER = new Color(218, 218, 221);
    public static final Color PROPERTY_DIALOG_BORDER = new Color(0xc9c9cd);
    public static final Color TAB_BUTTON_HOVER = new Color(248, 248, 248);
    public static final Color TAB_BUTTON_HOVER_SELECTED = new Color(239, 239, 241);
    public static final Color TAB_BUTTON_PRESS = new Color(228, 227, 232);
    public static final Color TAB_BUTTON_PRESS_SELECTED = new Color(236, 236, 238);
    public static final Color POPUP_TITLE_BACKGROUND = new Color(0xd8f2fd);
    public static final Color LIST_ITEM_SPLIT_LINE = new Color(0xf0f0f3);


    public static final BufferedImage DRAG_BAR = IOUtils.readImage("com/fr/design/images/control/bar.png");
    public static final BufferedImage DRAG_BAR_LIGHT = IOUtils.readImage("com/fr/design/images/control/bar-light.png");
    public static final BufferedImage ARROW_NORTH = IOUtils.readImage("com/fr/design/images/control/up_arrow.png");
    public static final BufferedImage ARROW_SOUTH = IOUtils.readImage("com/fr/design/images/control/down_arrow.png");
    public static final BufferedImage ARROW_EAST = IOUtils.readImage("com/fr/design/images/control/east_arrow.png");
    public static final BufferedImage ARROW_WEST = IOUtils.readImage("com/fr/design/images/control/west_arrow.png");

    public static final BufferedImage DRAG_BAR_RIGHT = IOUtils.readImage("com/fr/design/images/control/barm.png");
    public static final BufferedImage DRAG_BAR_LEFT = IOUtils.readImage("com/fr/design/images/control/barl.png");
    public static final BufferedImage DRAG_UP_NORMAL = IOUtils.readImage("com/fr/design/images/control/upnor.png");
    public static final BufferedImage DRAG_UP_PRESS = IOUtils.readImage("com/fr/design/images/control/uppre.png");
    public static final BufferedImage DRAG_DOWN_NORMAL = IOUtils.readImage("com/fr/design/images/control/downnor.png");
    public static final BufferedImage DRAG_DOWN_PRESS = IOUtils.readImage("com/fr/design/images/control/downpre.png");
    public static final BufferedImage DRAG_RIGHT_NORMAL = IOUtils.readImage("com/fr/design/images/control/rightnor.png");
    public static final BufferedImage DRAG_RIGHT_PRESS = IOUtils.readImage("com/fr/design/images/control/rightpre.png");
    public static final BufferedImage DRAG_LEFT_NORMAL = IOUtils.readImage("com/fr/design/images/control/leftnor.png");
    public static final BufferedImage DRAG_LEFT_PRESS = IOUtils.readImage("com/fr/design/images/control/leftpre.png");
    public static final BufferedImage DRAG_DOT = IOUtils.readImage("com/fr/design/images/control/dot.png");
    public static final BufferedImage DRAG_LINE = IOUtils.readImage("com/fr/design/images/control/dot-line.png");
    public static final BufferedImage ACCESSIBLE_EDITOR_DOT = IOUtils.readImage("com/fr/design/images/control/dot.png");
    public static final BufferedImage DRAG_DOT_VERTICAL = IOUtils.readImage("com/fr/design/images/control/dotv.png");
    public static final BufferedImage POP_BUTTON_DOWN = IOUtils.readImage("com/fr/design/images/buttonicon/popdownarrow.png");
    public static final BufferedImage POP_BUTTON_UP = IOUtils.readImage("com/fr/design/images/buttonicon/popuparrow.png");
    public static final BufferedImage DRAG_DOWN_SELECTED_SMALL = IOUtils.readImage("com/fr/design/images/buttonicon/downSelected.png");
    public static final BufferedImage DRAG_LEFT_NORMAL_SMALL = IOUtils.readImage("com/fr/design/images/buttonicon/leftNormal.png");
    public static final BufferedImage WATERMARK_BACKGROUND = IOUtils.readImage("/com/fr/design/images/dialog/watermark/" + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_WaterMark_Background_Icon_File_Name"));

    public static final int MODEL_NORMAL = 0;
    public static final int MODEL_PRESS = 1;
    public static final Icon ARROW_DOWN_ICON = IOUtils.readIcon("com/fr/design/images/buttonicon/arrowdown.png");
    public static final Icon ARROW_UP_ICON = IOUtils.readIcon("com/fr/design/images/buttonicon/arrowup.png");
    public static final Icon YES_ICON = IOUtils.readIcon("com/fr/design/images/buttonicon/yes.png");
    public static final Icon CHOOSEN_ICON = IOUtils.readIcon("com/fr/design/images/buttonicon/select_item.png");
    public static final Icon PRE_WIDGET_ICON = IOUtils.readIcon("com/fr/design/images/buttonicon/prewidget.png");
    public static final Icon EDIT_NORMAL_ICON = IOUtils.readIcon("com/fr/design/images/buttonicon/editn.png");
    public static final Icon EDIT_PRESSED_ICON = IOUtils.readIcon("com/fr/design/images/buttonicon/editp.png");
    public static final Icon HIDE_NORMAL_ICON = IOUtils.readIcon("com/fr/design/images/buttonicon/hiden.png");
    public static final Icon HIDE_PRESSED_ICON = IOUtils.readIcon("com/fr/design/images/buttonicon/hidep.png");
    public static final Icon VIEW_NORMAL_ICON = IOUtils.readIcon("com/fr/design/images/buttonicon/viewn.png");
    public static final Icon VIEW_PRESSED_ICON = IOUtils.readIcon("com/fr/design/images/buttonicon/viewp.png");
    public static final Icon RUN_BIG_ICON = IOUtils.readIcon("com/fr/design/images/buttonicon/run24.png");
    public static final Icon RUN_SMALL_ICON = IOUtils.readIcon("com/fr/design/images/buttonicon/runs.png");
    public static final Icon PAGE_BIG_ICON = IOUtils.readIcon("com/fr/design/images/buttonicon/pageb24.png");
    public static final Icon WRITE_BIG_ICON = IOUtils.readIcon("com/fr/design/images/buttonicon/writeb24.png");
    public static final Icon ANA_BIG_ICON = IOUtils.readIcon("com/fr/design/images/buttonicon/anab24.png");
    public static final Icon PAGE_SMALL_ICON = IOUtils.readIcon("com/fr/design/images/buttonicon/pages.png");
    public static final Icon WRITE_SMALL_ICON = IOUtils.readIcon("com/fr/design/images/buttonicon/writes.png");
    public static final Icon ANA_SMALL_ICON = IOUtils.readIcon("com/fr/design/images/buttonicon/anas.png");
    public static final Icon REFRESH_ICON = IOUtils.readIcon("com/fr/design/images/buttonicon/refresh.png");
    public static final Icon FONT_ICON = IOUtils.readIcon("/com/fr/design/images/gui/color/foreground.png");
    public static final Icon AUTO_FONT_ICON = IOUtils.readIcon("/com/fr/design/images/gui/color/autoForeground.png");
    public static final Icon HISTORY_ICON = IOUtils.readIcon("com/fr/design/images/buttonicon/history.png");
    public static final Icon DELETE_ICON = IOUtils.readIcon("com/fr/design/images/m_file/close.png");
    public static final Icon EDIT_ICON = IOUtils.readIcon("com/fr/design/images/m_file/edit.png");
    public static final Icon SEARCH_ICON = IOUtils.readIcon("/com/fr/design/images/data/search.png");
    public static final Icon BLACK_SEARCH_ICON = IOUtils.readIcon("/com/fr/design/images/data/black_search.png");
    public static final Icon CLEAR_ICON = IOUtils.readIcon("/com/fr/design/images/data/source/delete.png");
    public static final Icon LIST_EDIT_ICON = IOUtils.readIcon("/com/fr/design/images/control/edit.png");
    public static final Icon LIST_EDIT_WHITE_ICON = IOUtils.readIcon("/com/fr/design/images/control/edit_white.png");
    public static final Color PRESSED_DARK_GRAY = new Color(127, 127, 127);
    public static final Color GRDIENT_DARK_GRAY = new Color(45, 45, 45);
    public static final Color BARNOMAL = new Color(232, 232, 233);
    public static final Color COMPONENT_BACKGROUND_COLOR = new Color(237, 237, 238);
    public static final int ARC = 0;
    public static final int BUTTON_GROUP_ARC = 0;
    public static final int LARGEARC = 6;
    public static final Stroke BS = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 2f, new float[]{3, 1}, 0);
    public static final Icon PREVIEW_DOWN = IOUtils.readIcon("com/fr/design/images/buttonicon/prevew_down_icon.png");
    public static final Icon CLOSE_OF_AUTHORITY = IOUtils.readIcon("/com/fr/design/images/m_report/close.png");
    public static final Icon CLOSE_OVER_AUTHORITY = IOUtils.readIcon("/com/fr/design/images/m_report/close_over.png");
    public static final Icon CLOSE_PRESS_AUTHORITY = IOUtils.readIcon("/com/fr/design/images/m_report/close_press.png");
    public static final int CLOSE_AUTHORITY_HEIGHT_AND_WIDTH = 24;


    /**
     * 正在加载的界面
     */
    public static final Object PENDING = new Object() {

        @Override
        public String toString() {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Loading") + "...";
        }
    };
    /**
     * 数据库连接失败的界面
     */
    public static final Object CONNECTION_FAILED = new Object() {

        public String toString() {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Database_Connection_Failed") + "!";
        }
    };

    /**
     * 自动补全的默认快捷键，一般来说是 alt + /.
     */
    public static final String DEFAULT_AUTO_COMPLETE = "alt + SLASH";
}
