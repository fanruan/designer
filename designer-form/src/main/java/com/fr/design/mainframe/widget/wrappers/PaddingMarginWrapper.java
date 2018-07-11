/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.widget.wrappers;

import java.util.StringTokenizer;

import com.fr.stable.StringUtils;
import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;
import com.fr.form.ui.PaddingMargin;

/**
 * @author richer
 * @since 6.5.3
 */
public class PaddingMarginWrapper implements Encoder, Decoder {

    @Override
    public String encode(Object v) {
        if (v == null) {
            return null;
        }
        PaddingMargin pm = (PaddingMargin) v;
        return pm.toString();
    }

    @Override
    public Object decode(String txt) {
        if (StringUtils.isEmpty(txt)) {
            return null;
        }

        txt = txt.trim();
        txt = txt.substring(1, txt.length() - 1).trim();
        StringTokenizer tokenizer = new StringTokenizer(txt, ",");

        return new PaddingMargin(Integer.parseInt(tokenizer.nextToken().trim()), Integer.parseInt(tokenizer.nextToken().trim()),
            Integer.parseInt(tokenizer.nextToken().trim()), Integer.parseInt(tokenizer.nextToken().trim()));
    }

    @Override
    public void validate(String txt) throws ValidationException {
        if (StringUtils.isEmpty(txt)) {
            return;
        }
        txt = txt.trim();

        if (txt.length() < 9) {
             throw new ValidationException("String takes form like: [top, left, bottom, right]!");
        }
    }
}