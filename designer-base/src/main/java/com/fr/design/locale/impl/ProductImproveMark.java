package com.fr.design.locale.impl;

import com.fr.general.GeneralContext;
import com.fr.general.locale.LocaleMark;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Hades
 * @date 2019/6/24
 */
public class ProductImproveMark implements LocaleMark<Boolean> {

    private Map<Locale, Boolean> map = new HashMap<>();

    public ProductImproveMark() {
        map.put(Locale.CHINA, true);
        map.put(Locale.TAIWAN, false);
        map.put(Locale.US, false);
        map.put(Locale.KOREA, false);
        map.put(Locale.JAPAN, false);
    }

    @Override
    public Boolean getValue() {
        Boolean result = map.get(GeneralContext.getLocale());
        return result == null ? false : result;
    }
}
