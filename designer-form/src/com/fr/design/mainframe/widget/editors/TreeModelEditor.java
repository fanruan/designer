package com.fr.design.mainframe.widget.editors;

import com.fr.design.mainframe.widget.accessibles.AccessiblePropertyEditor;
import com.fr.design.mainframe.widget.accessibles.AccessibleTreeModelEditor;

/**
 * 编辑视图树控件和下拉树控件
 * @since 6.5.3
 */
public class TreeModelEditor extends AccessiblePropertyEditor {

    public TreeModelEditor() {
        super(new AccessibleTreeModelEditor());
    }
}