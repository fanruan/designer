package com.fr.design.mainframe.widget.renderer;

import com.fr.design.mainframe.widget.wrappers.DictionaryWrapper;

/**
 * 数据字典渲染器,所有包含数据字典属性的编辑器通用此渲染器
 * @version 6.5.3
 */
public class DictionaryRenderer extends EncoderCellRenderer {

    public DictionaryRenderer() {
        super(new DictionaryWrapper());
    }
}