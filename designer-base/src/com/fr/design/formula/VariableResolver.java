package com.fr.design.formula;


public interface VariableResolver {

    String[] resolveColumnNames();

    boolean isBindCell();

    String[] resolveCurReportVariables();

    String[] resolveTableDataParameterVariables();

    String[] resolveReportParameterVariables();

    String[] resolveGlobalParameterVariables();

    VariableResolver DEFAULT = new VariableResolverAdapter() {

        @Override
        public String[] resolveColumnNames() {
            return new String[0];
        }

        @Override
        public boolean isBindCell() {
            return false;
        }
    };
}