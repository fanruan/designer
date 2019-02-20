/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.editor.editor;

import com.fr.design.gui.date.UIDatePicker;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.GeneralContext;

import javax.swing.JComponent;
import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * CellEditor used to edit Date object.
 *
 * @editor zhou
 * @since 2012-3-29下午6:03:03
 */
public class DateEditor extends Editor<Date> {

	private UIDatePicker uiDatePicker;

	/**
	 * Constructor.
	 */
	public DateEditor() {
		this(null);
	}

	public DateEditor(boolean es) {
		this(null, es);
	}

	public DateEditor(boolean es, String name) {
		this(null, es, name);
	}

	/**
	 * Constructor.
	 */
	public DateEditor(Date value) {
		this(value, false);
	}

	/**
	 * Constructor.
	 */
	public DateEditor(Date value, boolean format) {
		this(value, format, "");
	}

	public DateEditor(Date value, boolean format, String name) {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		uiDatePicker = new UIDatePicker();
		if (format) {
			int dateStyle = (GeneralContext.getLocale() == Locale.ENGLISH
					|| GeneralContext.getLocale() == Locale.US
					|| GeneralContext.getLocale() == Locale.UK) ? uiDatePicker.STYLE_EN_DATE : uiDatePicker.STYLE_CN_DATE;
			uiDatePicker.setStyle(dateStyle);
			uiDatePicker.setEditable(false);
		}
		uiDatePicker.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                fireStateChanged();
            }
        });
		this.uiDatePicker.setFocusTraversalKeysEnabled(false);
		this.add(uiDatePicker, BorderLayout.CENTER);

		this.setValue(value);
		this.setName(name);
	}
	
	//uidatepicker的setstyle方法不起作用，先粗暴地加个构造方法
	public DateEditor(Date value, boolean format, String name, int dateFormat) {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		uiDatePicker = new UIDatePicker(dateFormat);
		if (format) {
			uiDatePicker.setEditable(false);
		}
		uiDatePicker.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                fireStateChanged();
            }
        });
		this.uiDatePicker.setFocusTraversalKeysEnabled(false);
		this.add(uiDatePicker, BorderLayout.CENTER);

		this.setValue(value);
		this.setName(name);
	}
	
	

	public JComponent getEditComp() {
		return uiDatePicker;
	}

	/**
	 * Return the value of the CellEditor.
	 */
	@Override
	public Date getValue() {
		try {
			return this.uiDatePicker.getSelectedDate();
		} catch (ParseException parseException) {
            FineLoggerFactory.getLogger().error(parseException.getMessage(), parseException);
			return new Date();
		}
	}

	/**
	 * Set the value to the CellEditor.
	 */
	@Override
	public void setValue(Date value) {
		// populate data to UI
		if (value == null) {
//			value = new Date();
			return;
		}

		try {
			this.uiDatePicker.setSelectedDate(value);
		} catch (ParseException parseException) {
            FineLoggerFactory.getLogger().error(parseException.getMessage(), parseException);
		}
	}

	/**
	 * Sets whether or not this component is enabled.
	 */
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		this.uiDatePicker.setEnabled(enabled);
	}

	/**
	 * ValueEditorPane(无 日期 公式)切换成公式时执行此方法
	 */
	public void selected() {
		this.uiDatePicker.setSelectedItem(new Date());
	}

	/**
	 * Request focus
	 */
	@Override
	public void requestFocus() {
		this.uiDatePicker.requestFocus();
	}

	public String getIconName() {
		return "type_date";
	}
	
	public void setUIDatePickerStyle(int style){
		this.uiDatePicker.setStyle(style);
	}
	
	public SimpleDateFormat getUIDatePickerFormat(){
		return this.uiDatePicker.getDateFormat();
	}

	@Override
	/**
	 * accept
	 */
	public boolean accept(Object object) {
		return object instanceof Date;
	}
}