/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.widget.editors;

import com.fr.design.mainframe.widget.accessibles.AccessiblePropertyEditor;
import com.fr.design.mainframe.widget.accessibles.AccessilePaddingMarginEditor;

/**
 * @author richer
 * @since 6.5.3
 */
public class PaddingMarginEditor extends AccessiblePropertyEditor{
    public PaddingMarginEditor() {
        super(new AccessilePaddingMarginEditor());
    }
}