package com.fr.design.formula;

import java.util.Locale;

import com.fr.base.FRContext;
import com.fr.stable.StringUtils;
import com.fr.stable.script.Function;

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
		} else {
			if (Locale.CHINA.equals(FRContext.getLocale())) {
				return fn.getCN();
			} else {
				return fn.getEN();
			}
		}
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