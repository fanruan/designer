/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.widget.wrappers;

import com.fr.general.Inter;
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
            return Inter.getLocText(new String[]{"HF-Undefined", "Hyperlink"});
        }
        NameJavaScriptGroup group = (NameJavaScriptGroup)v;
        return Inter.getLocText(new String[]{"Total", "Has"}) + group.size() + Inter.getLocText(new String[]{"SpecifiedG-Groups", "Hyperlink"});
    }

    @Override
    public void validate(String txt) throws ValidationException {
        
    }
}