/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.widget.editors;

import com.fr.design.mainframe.widget.accessibles.AccessibleFormulaEditor;
import com.fr.design.mainframe.widget.accessibles.AccessiblePropertyEditor;

/**
 * @author richer
 * @since 6.5.3
 * 公式编辑器
 */
public class FormulaEditor extends AccessiblePropertyEditor {
    public FormulaEditor() {
        super(new AccessibleFormulaEditor());
    }
}