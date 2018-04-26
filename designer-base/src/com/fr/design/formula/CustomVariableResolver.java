package com.fr.design.formula;

public class CustomVariableResolver extends VariableResolverAdapter {
	private String[] columnNames;
	private boolean isBindCell;
	
	public CustomVariableResolver(String[] columnNames, boolean isBindCell) {
		this.columnNames = columnNames;
		this.isBindCell = isBindCell;
	}

	@Override
	public String[] resolveColumnNames() {
		return this.columnNames;
	}

	@Override
	public boolean isBindCell() {
		return this.isBindCell;
	}

}