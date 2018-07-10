package com.fr.design.gui.core;

import com.fr.general.Inter;

public class WidgetConstants {
	
	//没有控件
	public static final int NONE = -1;
	
	// TextEditor
	public static final int TEXT = 0;	
	
	// ComboBox
	public static final int COMBOBOX = 1;
	// NumberEditor
	public static final int NUMBER = 2;
	
	// DateEditor
	public static final int DATE = 3;
	
	public static final int COMBOCHECKBOX = 5;
	
	// TreeEditor
	public static final int TREE = 6;

	// TreeComboBoxEditor
	public static final int TREECOMBOBOX = 7;

	// ListEditor
	public static final int LIST = 8;
	
	public static final int RADIO = 9;
	
	public static final int RADIOGROUP = 10;
	// CheckBox
	public static final int CHECKBOX = 11;
	// CheckBoxGroup
	public static final int CHECKBOXGROUP = 12;
	
	// FileEditor
//	public static final int FILE = 13;
//	public static final String NAME_FILE = Inter.getLocText("File");
	
	public static final int BUTTON = 14;
	
	// TextArea & Password
	public static final int TEXTAREA = 15;
	
	public static final int PASSWORD = 16;
	
	public static final int TABLETREE = 17;
	
	public static final int IFRAME = 18;
	
	public static final int MULTI_FILE = 19;
	
	// 默认控件
	public static final String DEFAULT_WIDGETCONFIG = Inter.getLocText("Widget-Form_Widget_Config");
	
	// 报表控件，与默认控件不同，比如label，table,SEARCH等，这里是不需要的
	public static final String REPORT_WIDGETCONFIG = Inter.getLocText("Widget-Default_Widget_Config");
	
	public static final String FORM_CHARTWIDGET = Inter.getLocText("Widget-Chart_Widget_Config");
	
	// 表单容器控件
	public static final String FORM_WIDGETCONTAINER = Inter.getLocText("Widget-Form_Widget_Container");
	
	public  static final String POLY_REPORT_WIDGET = Inter.getLocText("Poly-Report_Component");
	// 服务器预定义控件
	public static final String USER_DEFINED_WIDGETCONFIG = Inter.getLocText("Widget-User_Defined_Widget_Config");
	
	// 服务器组合控件
	public static final String COMB_WIDGETCONFIG = Inter.getLocText("Widget-Comb_Widget_Config");
	
	// 服务器自定义控件
	public static final String CUSTOM_WIDGETCONFIG = Inter.getLocText("Widget-Custom_Widget_Config");
	
}