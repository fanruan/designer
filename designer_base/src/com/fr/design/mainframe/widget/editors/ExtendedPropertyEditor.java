package com.fr.design.mainframe.widget.editors;

import java.beans.PropertyEditor;

import com.fr.design.Exception.ValidationException;

/**
 * @author richer
 * @since 6.5.3
 * 属性编辑器
 */
public interface ExtendedPropertyEditor extends PropertyEditor {
	void validateValue() throws ValidationException;

	void setDefaultValue(Object v);
	
	//头疼死了,就先这样做吧,这个属性表浪费太多时间了,为实现许多功能,提需求重做这里;
	boolean refreshInTime();
}