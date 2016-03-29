package com.fr.design.formula;

import com.fr.stable.StringUtils;

public abstract class AbstractNameAndDescription implements NameAndDescription {

    @Override
    public String searchResult(String keyWord, boolean findDescription) {
        String functionName = getName();
        if (StringUtils.isBlank(functionName)) {
            return null;
        }
        int sign = 1;
        int length = keyWord.length();
        String temp = functionName.toUpperCase();
        for (int j = 0; j < length; j++) {
            String check = keyWord.substring(j, j + 1);
            int index = temp.indexOf(check.toUpperCase());
            if (index == -1) {
                sign = 0;
                break;
            } else {
                temp = temp.substring(index + 1);
            }
        }
        if (sign == 1) {
            return functionName;
        }
        return null;
    }
}