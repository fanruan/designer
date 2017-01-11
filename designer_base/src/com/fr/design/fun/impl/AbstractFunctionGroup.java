package com.fr.design.fun.impl;

import com.fr.design.formula.FunctionDefNAD;
import com.fr.design.formula.NameAndDescription;
import com.fr.design.fun.FunctionGroupDefineProvider;
import com.fr.stable.fun.mark.API;
import com.fr.stable.script.FunctionDef;


@API(level = FunctionGroupDefineProvider.CURRENT_LEVEL)
public abstract class AbstractFunctionGroup implements FunctionGroupDefineProvider {
	
	@Override
	public int currentAPILevel() {
		return CURRENT_LEVEL;
	}
	
	@Override
	public String mark4Provider() {
		return getClass().getName();
	}
	
	@Override
	public NameAndDescription[] getDescriptions() {
		FunctionDef[] funcs = getFunctionDefs();
		int count = funcs.length;
        FunctionDefNAD[] nads = new FunctionDefNAD[count];
        for (int i = 0; i < count; i ++) {
            nads[i] = new FunctionDefNAD(funcs[i]);
        }
        return nads;
	}
	
}
