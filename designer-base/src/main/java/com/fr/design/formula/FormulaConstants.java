package com.fr.design.formula;

import com.fr.stable.StringUtils;

public enum FormulaConstants {

    PAGE_NUMBER("$$page_number", "Page_Number"),
    TOTAL_PAGE_NUMBER("$$totalPage_number", "Total_Page_Number"),
    FINE_USERNAME("$fine_username", "Fine_Username"),
    FINE_ROLE("$fine_role", "Fine_Role"),
    FINE_POSITION("$fine_position", "Fine_Position"),
    NULL("NULL", "Null"),
    NOFILTER("NOFILTER", "No_Filter"),
    REPORT_NAME("reportName", "Report_Name"),
    FORMLET_NAME("formletName", "Formlet_Name"),
    SERVLET_URL("servletURL", "Servlet_URL"),
    SERVER_SCHEMA("serverSchema", "Server_Schema"),
    SERVER_NAME("serverName", "Server_Name"),
    SERVER_PORT("serverPort", "Server_Port"),
    SERVER_URL("serverURL", "Server_URL"),
    CONTEXT_PATH("contextPath", "Context_Path"),
    SESSION_ID("sessionID", "SessionID");


    private String key;
    private String value;

    private FormulaConstants(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static String getValueByKey(String key) {
        for (FormulaConstants formulaConstant : values()) {
            if (formulaConstant.getKey().equals(key)) {
                return formulaConstant.getValue();
            }
        }
        return StringUtils.EMPTY;
    }

}
