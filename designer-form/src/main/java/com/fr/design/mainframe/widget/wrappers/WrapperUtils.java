package com.fr.design.mainframe.widget.wrappers;

import java.util.StringTokenizer;

import com.fr.stable.StringUtils;
import com.fr.design.Exception.ValidationException;

public abstract class WrapperUtils {

    private WrapperUtils() {
    }

    public static void validateIntegerTxtFomat(String txt, int times, ValidationException e) throws ValidationException {
    	if (StringUtils.isEmpty(txt)) {
            return;
        }
    	
    	txt = txt.trim();

        if (txt.length() < times * 2 + 1) {
            throw e;
        }
    	
        char c = txt.charAt(0);

        if (c != '[') {
            throw e;
        }

        c = txt.charAt(txt.length() - 1);

        if (c != ']') {
            throw e;
        }

        txt = txt.substring(1, txt.length() - 1).trim();

        StringTokenizer tokenizer = new StringTokenizer(txt, ",");

        validateTokenizerParseInt(tokenizer, times, e);
    }

    public static void validateTokenizerParseInt(StringTokenizer tokenizer, int times, ValidationException e) throws ValidationException {
        if (tokenizer.hasMoreTokens()) {
            try {
                Integer.parseInt(tokenizer.nextToken().trim());
            } catch (NumberFormatException nfe) {
                throw e;
            }

            validateTokenizerParseInt(tokenizer, times - 1, e);
        }
    }
}