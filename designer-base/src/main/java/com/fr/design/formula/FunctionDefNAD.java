package com.fr.design.formula;

import com.fr.stable.script.FunctionDef;
import com.fr.stable.StringUtils;

public class FunctionDefNAD extends AbstractNameAndDescription {
	private FunctionDef def;
	
	public FunctionDefNAD(FunctionDef def) {
		this.def = def;
	}
	
	@Override
	public String getName() {
		return def == null ? StringUtils.EMPTY : def.getName();
	}
	
	@Override
	public String getDesc() {
		return def == null ? StringUtils.EMPTY : def.getDescription();
	}
}