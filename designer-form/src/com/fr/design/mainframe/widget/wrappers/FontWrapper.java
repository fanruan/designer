package com.fr.design.mainframe.widget.wrappers;

import java.awt.Font;
import java.util.StringTokenizer;

import com.fr.general.FRFont;
import com.fr.stable.StringUtils;
import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

public class FontWrapper implements Encoder, Decoder {

    public FontWrapper() {
    }

    @Override
	/**
	 * decode
	 */
    public Object decode(String txt) {
        if (StringUtils.isEmpty(txt)) {
            return FRFont.getInstance();
        }

        txt = txt.trim();
        txt = txt.substring(1, txt.length() - 1).trim();

        StringTokenizer tokenizer = new StringTokenizer(txt, ",");

        return FRFont.getInstance(new Font(tokenizer.nextToken().trim(),
            Integer.parseInt(tokenizer.nextToken().trim()),
            Integer.parseInt(tokenizer.nextToken().trim())));
    }

    @Override
	/**
	 * encode
	 */
    public String encode(Object v) {
        if (v == null) {
            return null;
        }

        FRFont f = (FRFont) v;

        return "[" + f.getFamily() + ", " + f.getStyleName() + ", " + f.getSize()
            + "]";
    }

    @Override
	/**
	 *
	 */
    public void validate(String txt) throws ValidationException {
    	WrapperUtils.validateIntegerTxtFomat(txt, 3, newValidateException());
    }

    private ValidationException newValidateException() throws ValidationException {
        return new ValidationException(
            "Font string takes form like: [family, style, size]!");
    }
}