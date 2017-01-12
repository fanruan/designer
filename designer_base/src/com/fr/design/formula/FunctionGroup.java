package com.fr.design.formula;


public interface FunctionGroup {
	
	int CURRENT_LEVEL = 1;
	String MARK_STRING = "FunctionGroup";
	
	String getGroupName();
	NameAndDescription[] getDescriptions();
}