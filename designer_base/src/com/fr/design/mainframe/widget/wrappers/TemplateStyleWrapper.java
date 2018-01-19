package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;
import com.fr.stable.StringUtils;

/**
 * Created by kerry on 2017/11/23.
 */
public class TemplateStyleWrapper implements Encoder, Decoder {
    @Override
    public String encode(Object v) {
        if (v == null) {
            return StringUtils.EMPTY;
        }
        return v.toString();
    }

    @Override
    public Object decode(String txt) {
        return null;
    }

    @Override
    public void validate(String txt) throws ValidationException {

    }
}
