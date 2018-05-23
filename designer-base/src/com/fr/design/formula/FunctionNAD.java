package com.fr.design.formula;

import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import com.fr.stable.script.Function;

public class FunctionNAD extends AbstractNameAndDescription {

	private static final String LOCALE_PREFIX = "Fine-Core_Formula_";

	private Function fn;
	
	FunctionNAD(Function fn) {
		this.fn = fn;
	}
	
	public String getName() {
		return fn == null ? StringUtils.EMPTY : fn.getClass().getSimpleName();
	}
	
	public String getDesc() {
		if (fn == null) {
			return StringUtils.EMPTY;
		}
		// 统一用Fine-Core_Formula_+公式小写名作为国际化的key
		String localeKey = LOCALE_PREFIX + fn.getClass().getSimpleName().toLowerCase();
		return Inter.getLocText(localeKey);
	}

    @Override
    public String searchResult(String keyWord, boolean findDescription) {
        String functionName = getName();
        String des = getDesc();
        if (findDescription && des.contains(keyWord)) {
            return functionName;
        } else {
            return super.searchResult(keyWord, findDescription);
        }
    }
}