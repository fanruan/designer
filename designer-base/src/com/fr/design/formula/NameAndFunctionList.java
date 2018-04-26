package com.fr.design.formula;

import java.util.ArrayList;
import java.util.Arrays;

import com.fr.stable.script.Function;

public class NameAndFunctionList implements FunctionGroup {
	protected String name;
	protected java.util.List<Function> fnList = new ArrayList<Function>();
	
	public NameAndFunctionList(String name, Function[] fns) {
		this.name = name;
		fnList.addAll(Arrays.asList(fns));
	}
	
	@Override
	public String getGroupName() {
		return this.name;
	}
	
	@Override
	public NameAndDescription[] getDescriptions() {
		NameAndDescription[] nads = new NameAndDescription[fnList.size()];
		for (int i = 0; i < nads.length; i++) {
			nads[i] = new FunctionNAD(fnList.get(i));
		}
		
		return nads;
	}
}