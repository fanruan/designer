package com.fr.design.widget.ui.designer;

import com.fr.data.Dictionary;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.widget.accessibles.AccessibleDictionaryEditor;
import com.fr.form.ui.DictContainedCustomWriteAbleEditor;


import java.awt.*;

/**
 * Created by ibm on 2017/8/6.
 */
public abstract class DictEditorDefinePane<T extends DictContainedCustomWriteAbleEditor> extends CustomWritableRepeatEditorPane<T> {
    private AccessibleDictionaryEditor dictionaryEditor;


    public DictEditorDefinePane(XCreator xCreator) {
        super(xCreator);
    }


    protected Component[] createDictPane(){
        dictionaryEditor = new AccessibleDictionaryEditor();
        return new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_DS-Dictionary")), dictionaryEditor};
    }

    @Override
    protected void populateSubCustomWritableRepeatEditorBean(T e) {
        populateSubDictionaryEditorBean(e);
        dictionaryEditor.setValue(e.getDictionary());
    }


    @Override
    protected T updateSubCustomWritableRepeatEditorBean() {
        T e = updateSubDictionaryEditorBean();
        e.setDictionary((Dictionary) dictionaryEditor.getValue());
        return e;
    }

    protected abstract void populateSubDictionaryEditorBean(T e);

    protected abstract T updateSubDictionaryEditorBean();

}