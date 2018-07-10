/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.widget.editors;

import com.fr.design.mainframe.widget.accessibles.AccessibleBackgroundEditor;
import com.fr.design.mainframe.widget.accessibles.AccessiblePropertyEditor;

/**
 * @author richer
 * @since 6.5.3
 */
public class BackgroundEditor extends AccessiblePropertyEditor{
    public BackgroundEditor() {
        super(new AccessibleBackgroundEditor());
    }
}