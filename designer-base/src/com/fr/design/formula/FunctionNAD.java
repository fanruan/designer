package com.fr.design.formula;

import com.fr.base.FRContext;
import com.fr.script.CalculatorEmbeddedFunction;
import com.fr.stable.StringUtils;
import com.fr.stable.script.Function;

import java.util.Locale;

public class FunctionNAD extends AbstractNameAndDescription {


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
		Locale locale = FRContext.getLocale();
		String describtion = fn.getDescribtion(locale);
		if (describtion.startsWith(CalculatorEmbeddedFunction.LOCALE_PREFIX)) {
			// 老的自定义函数兼容, 没有重写getDescribtion
			return Locale.CHINA.equals(locale) ? fn.getCN() : fn.getEN();
		}

		return describtion;
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