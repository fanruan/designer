package com.fr.design.formula;

import com.fr.stable.script.Function;

public class NameAndTypeAndFunctionList extends NameAndFunctionList {
	private Function.Type type;
	
	public NameAndTypeAndFunctionList(String name, Function.Type type) {
		super(name, new Function[0]);
		
		this.type = type;
	}
	
	public boolean test(Function fn) {
		if (fn != null && fn.getType() == this.type) {
			return fnList.add(fn);
		}
		
		return false;
	}
}