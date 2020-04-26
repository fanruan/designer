package com.fr.common.report;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/3/27
 */
public enum ReportState {

    STOP("stop"), ACTIVE("active");

    private String value;

    ReportState(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}