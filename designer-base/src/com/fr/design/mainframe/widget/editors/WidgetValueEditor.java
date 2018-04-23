/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.widget.editors;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.design.Exception.ValidationException;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.editor.BooleanEditor;
import com.fr.design.editor.editor.DateEditor;
import com.fr.design.editor.editor.DoubleEditor;
import com.fr.design.editor.editor.Editor;
import com.fr.design.editor.editor.FormulaEditor;
import com.fr.design.editor.editor.TextEditor;
import com.fr.form.ui.DataControl;
import com.fr.form.ui.WidgetValue;
import com.fr.general.Inter;

public class WidgetValueEditor extends AbstractPropertyEditor {
	
	private DataControl widget;
	private ValueEditorPane wep;

    /**
     * 根据类型创建
     * @param type  类型
     * @param onlyServer 是否是服务器
     * @return 编辑器
     */
	public static Editor createWidgetValueEditorByType(int type, boolean onlyServer) {
		switch (type) {
		case DataControl.TYPE_NUMBER:
			return new DoubleEditor();
		case DataControl.TYPE_FORMULA:
			return new FormulaEditor(Inter.getLocText("Parameter-Formula"));
		case DataControl.TYPE_DATABINDING:
			return onlyServer ? new ServerDataBindingEditor() : new DataBindingEditor();
		case DataControl.TYPE_STRING:
			return new TextEditor();
		case DataControl.TYPE_BOOLEAN:
			return  new BooleanEditor(false);
		case DataControl.TYPE_DATE:
			return new DateEditor(true, Inter.getLocText("Date"));
		case DataControl.TYPE_TABLEDATA:
			return onlyServer ? new ServerDataTableEditor() : new DataTableEditor();
		default:
			return null;
		}
	}

	/**
	 * 用DataControl构建
	 * @param data 数据
     * @param onlyServer 是否是服务器
	 * @return 编辑器
	 */
    public static Editor[] createWidgetValueEditor(DataControl data, boolean onlyServer) {
		int types[] = data.getValueType();
		Editor[] editor = new Editor[types.length ];
		for (int i = 0; i < types.length; i++) {
			editor[i] = createWidgetValueEditorByType(types[i], onlyServer);
			
		}
		return editor;
    }
    
    public WidgetValueEditor(Object o) {
    	this(o, false);
    }
    
	public WidgetValueEditor(Object o, boolean onlyServer) {
		this.widget = (DataControl) o;
		Editor[] editor = createWidgetValueEditor(widget, onlyServer);
		for (final Editor e : editor) {
			e.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					firePropertyChanged();
				}
				
			});
		}
		this.wep = new ValueEditorPane(editor);
		wep.addPropertyChangeListener("value", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				firePropertyChanged();
			}
		});
	}

    /**
     *值验证
     * @throws ValidationException 异常
     */
	public void validateValue() throws ValidationException {

	}

	@Override
	public Component getCustomEditor() {
		return wep;
	}

	@Override
	public Object getValue() {
		return new WidgetValue(wep.update());
	}

	@Override
	public void setValue(Object value) {
		if(value != null) {
			wep.populate(((WidgetValue)value).getValue());
		}
	}
}