/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.form.util;

import java.awt.BasicStroke;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * @author richer
 * @since 6.5.3
 */
public class XCreatorConstants {

    private XCreatorConstants() {
    }
    public static final String WIDGETNAME = "widgetName";
    // 描述属性的分类
    public static final String PROPERTY_CATEGORY = "category";
    public static final String DEFAULT_GROUP_NAME = "Form-Basic_Properties";
    public static final Color FORM_BG = new Color(252, 252, 254);
    // 拖拽标识块的大小
    public static final int RESIZE_BOX_SIZ = 5;
    // 拖拽的小方块的内部颜色
    public static final Color RESIZE_BOX_INNER_COLOR = Color.white;
    // 拖拽的小方块的边框颜色
    public static final Color RESIZE_BOX_BORDER_COLOR = new Color(143, 171, 196);
    // 当前选取的组件的边框线着色
	public static final Color SELECTION_COLOR = new Color(179, 209, 236);
    // 设计器区域外边框的颜色和粗细
    public static final Border AREA_BORDER = BorderFactory.createLineBorder(new Color(224, 224, 255), 0);
    // 布局拖拽时的颜色
    public static final Color LAYOUT_HOTSPOT_COLOR = new Color(64, 240, 0);
    public static final Color LAYOUT_FORBIDDEN_COLOR = new Color(254, 0, 0);
    //自适应布局拖拽颜色
    public static final Color FIT_LAYOUT_HOTSPOT_COLOR = new Color(154, 195, 233);
    // 自适应布局的交叉点渲染颜色
    public static final Color FIT_LAYOUT_POINT_COLOR = new Color(106, 168, 222);
    // 格子布局的分割线
    public static final Color LAYOUT_SEP_COLOR = new Color(210, 210, 210);
    
    // 伸缩表单操作条的颜色
    public static final Color OP_COLOR = new Color(157,228,245);
    
    // 不同粗细的线
    public static final BasicStroke STROKE = new BasicStroke(2);
}