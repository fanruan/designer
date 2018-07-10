package com.fr.design.gui.icombobox;

import com.fr.base.SeparationConstants;
import com.fr.general.Inter;

public class DictionaryConstants {
	// richer:正则表达式
	public final static String[] regexps = new String[] { 
		"", 
		"^.+$",
		"^\\w{0,13}$", 
		"^\\d+(\\.\\d+)?$",
		"^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$",
		"^\\d{3,4}-\\d{7,8}$", 
		"^\\d{11}$",
		"(^\\d{15}$)|(^\\d{17}([0-9]|[Xx])$)",
		"^\\d{6}$" };
	
	public final static String[] regexpsDisplays = new String[] {
		Inter.getLocText("None"), 
		Inter.getLocText("Required"),
		Inter.getLocText("Length"), 
		Inter.getLocText("Float"),
		Inter.getLocText("Email"), 
		Inter.getLocText("Phone"),
		Inter.getLocText("MobilePhone"), 
		Inter.getLocText("IDCard"),
		Inter.getLocText("PostCode"),
		Inter.getLocText("Custom")};
	
	public final static int REG_NONE = 0;
	public final static int REG_REQUIRED = 1;
	public final static int REG_LENGTH = 2;
	public final static int REG_FLOAT = 3;
	public final static int REG_EMAIL = 4;
	public final static int REG_PHONE = 5;
	public final static int REG_MOBILEPHONE = 6;
	public final static int REG_IDCARD = 7;
	public final static int REG_POSTCODE = 8;
	public final static int REG_CUSTOM = 9;
	
	// richer:分隔符
	public static final String[] delimiters = new String[]{
		SeparationConstants.COMMA, SeparationConstants.SEMICOLON, SeparationConstants.COLON
		};
	
	public static final String[] delimiterDisplays = new String[]{
		Inter.getLocText("Form-Comma"),
		Inter.getLocText("Form-Semicolon"),
		Inter.getLocText("Form-Colon")
		};
	
	public static final String[] symbols = new String[]{
		"", SeparationConstants.SINGLE_QUOTE, SeparationConstants.DOUBLE_QUOTES
	};
	
	public static final String[] symbolDisplays = new String[]{
		Inter.getLocText("None"),
		Inter.getLocText("Form-Single_quote"),
		Inter.getLocText("Form-Double_quotes")
	};
	// richer:文件类型选择器
	public static final String[] fileTypes = new String[]{
		"",
		"txt",
		"pdf",
		"jpg,png,gif",
		"doc,xls,ppt"
	};
	
	public static final String[] acceptTypes = new String[]{
		"",
		"txt",
		"pdf",
		"jpg|png|gif",
		"doc|xls|ppt"
	};
	
	public static final String[] fileTypeDisplays = new String[]{
		Inter.getLocText("Form-All_Files"),
		"txt",
		"pdf",
		"jpg,png,gif",
		"doc,xls,ppt"		
	};
}