package com.fr.design.mainframe.widget.editors;

import com.fr.design.mainframe.widget.accessibles.AccessibleDateEditor;
import com.fr.design.mainframe.widget.accessibles.AccessiblePropertyEditor;

public class DateFormatEditor extends AccessiblePropertyEditor {
	public DateFormatEditor() {
		super(new AccessibleDateEditor());
	}
}