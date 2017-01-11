package com.fr.design.fun;

import com.fr.design.formula.FunctionGroup;
import com.fr.stable.fun.mark.Mutable;
import com.fr.stable.script.FunctionDef;

public interface FunctionGroupDefineProvider extends Mutable,FunctionGroup{
	int CURRENT_LEVEL = 1;
	String MARK_STRING = "FunctionGroupDefineProvider";
	
	public FunctionDef[] getFunctionDefs();
}
