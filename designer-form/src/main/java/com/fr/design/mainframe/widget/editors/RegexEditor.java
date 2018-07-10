package com.fr.design.mainframe.widget.editors;

import com.fr.design.mainframe.widget.accessibles.AccessiblePropertyEditor;
import com.fr.design.mainframe.widget.accessibles.AccessibleRegexEditor;

public class RegexEditor extends AccessiblePropertyEditor {

	public RegexEditor() {
		super(new AccessibleRegexEditor());
	}

	public static class RegexEditor4TextArea extends AccessiblePropertyEditor {
		public RegexEditor4TextArea() {
			super(new AccessibleRegexEditor.AccessibleRegexEditor4TextArea());
		}
	}
}