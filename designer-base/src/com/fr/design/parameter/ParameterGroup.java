package com.fr.design.parameter;

import com.fr.base.Parameter;

public class ParameterGroup {
	private String groupName;
	private Parameter[] parameters;

	public ParameterGroup(String groupName, Parameter[] parameters) {
		this.groupName = groupName;
		this.parameters = parameters;
	}

	public String getGroupName() {
		return groupName;
	}

	public Parameter[] getParameter() {
		return parameters;
	}
}