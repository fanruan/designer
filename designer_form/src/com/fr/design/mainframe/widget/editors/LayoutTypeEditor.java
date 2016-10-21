package com.fr.design.mainframe.widget.editors;

import com.fr.design.designer.properties.EnumerationEditor;
import com.fr.design.designer.properties.items.FRLayoutTypeItems;

/**
 * Created by zhouping on 2016/9/18.
 */
public class LayoutTypeEditor extends EnumerationEditor {

    public LayoutTypeEditor() {
        super(new FRLayoutTypeItems());
    }

}