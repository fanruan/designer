package com.fr.design.mainframe.widget.wrappers;

import com.fr.stable.StringUtils;
import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

/**
 *  包装下数据字典
 *  @version 6.5.3
 */
public class DictionaryWrapper implements Encoder, Decoder {

    public DictionaryWrapper() {
    }

    @Override
    public Object decode(String txt) {
        return txt;
    }

    @Override
    public String encode(Object v) {
        if (v == null) {
            return StringUtils.EMPTY;
        }

        return v.toString();
    }

    @Override
    public void validate(String txt) throws ValidationException {
        
    }
}