package com.fr.design.mainframe.widget.editors;

import com.fr.design.mainframe.widget.accessibles.AccessibleDictionaryEditor;
import com.fr.design.mainframe.widget.accessibles.AccessiblePropertyEditor;

/**
 * 所有具有数据字典属性的控件都使用此编辑器
 * @version 6.5.3
 */
public class DictionaryEditor extends AccessiblePropertyEditor {

    public DictionaryEditor() {
        super(new AccessibleDictionaryEditor());
    }
}