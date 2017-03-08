package com.fr.design.mainframe.widget.editors;

import com.fr.design.mainframe.widget.accessibles.AccessibleECToolBarEditor;
import com.fr.design.mainframe.widget.accessibles.AccessiblePropertyEditor;

/**
 * Created by harry on 2017-2-23.
 */
public class ECToolBarEditor extends AccessiblePropertyEditor {
    public ECToolBarEditor() {
        super(new AccessibleECToolBarEditor());
    }
}
