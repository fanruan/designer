package com.fr.design.mainframe.widget.editors;

import com.fr.design.mainframe.widget.accessibles.AccessiblePropertyEditor;
import com.fr.design.mainframe.widget.accessibles.AccessibleWLayoutBorderStyleEditor;
/**
 * 表单容器边框
 * @since 6.5.6
 */
public class WLayoutBorderStyleEditor extends AccessiblePropertyEditor{
	public WLayoutBorderStyleEditor() {
        super(new AccessibleWLayoutBorderStyleEditor());
    }
}