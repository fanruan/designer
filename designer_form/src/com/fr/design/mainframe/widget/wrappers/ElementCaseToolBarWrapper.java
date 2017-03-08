package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;
import com.fr.form.web.FormToolBarManager;
import com.fr.general.Inter;

/**
 * Created by harry on 2017-3-1.
 */
public class ElementCaseToolBarWrapper implements Encoder, Decoder {
    @Override
    public Object decode(String txt) {
        return null;
    }

    @Override
    public void validate(String txt) throws ValidationException {

    }

    @Override
    public String encode(Object v) {
        if (v == null) {
            return null;
        }
        FormToolBarManager[] toolBarManager = (FormToolBarManager[])v;
        if (toolBarManager.length != 0) {
            return Inter.getLocText("FR-Designer_Open");
        }
        return Inter.getLocText("FR-Designer_Close");
    }
}
