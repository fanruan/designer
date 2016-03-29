/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.widget.wrappers;

import com.fr.base.Formula;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

/**
 * @author richer
 * @since 6.5.3
 */
public class FormulaWrapper implements Encoder, Decoder{
    public FormulaWrapper() {

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
        return new Formula(txt);
    }

    @Override
    public void validate(String txt) throws ValidationException {
       if (StringUtils.isBlank(txt)){
           return;
       }
       if (txt.length() > 0 && txt.charAt(0) == '=') {
           return;
       }
       throw new ValidationException(Inter.getLocText("Formula_Tips"));
    }
}