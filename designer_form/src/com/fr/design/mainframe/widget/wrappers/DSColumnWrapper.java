/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.widget.wrappers;

import com.fr.stable.StringUtils;
import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

/**
 * @author richer
 * @since 6.5.3
 */
public class DSColumnWrapper implements Encoder, Decoder {
    public DSColumnWrapper() {
        
    }
    
    @Override
    public String encode(Object v) {
        if (v == null) {
            return StringUtils.EMPTY;
        }
        return v.toString();
    }

    @Override
    public Object decode(String txt) {
        return txt;
    }

    @Override
    public void validate(String txt) throws ValidationException {
       
    }

}