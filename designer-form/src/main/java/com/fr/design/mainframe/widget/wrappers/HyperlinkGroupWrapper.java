/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.widget.wrappers;


import com.fr.js.NameJavaScriptGroup;
import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

/**
 * @author richer
 * @since 6.5.3
 */
public class HyperlinkGroupWrapper implements Encoder, Decoder {

    public HyperlinkGroupWrapper() {
    }

    @Override
    public Object decode(String txt) {
        return null;
    }

    @Override
    public String encode(Object v) {
        if (v == null) {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Hyperlink_Undefined");
        }
        NameJavaScriptGroup group = (NameJavaScriptGroup)v;
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Hyperlink_Group_Count", group.size());
    }

    @Override
    public void validate(String txt) throws ValidationException {
        
    }
}
