package com.fr.design.mainframe;

public abstract class BaseUndoState<T> {
    //这三个是针对正常报表编辑状态下的undostate的属性，
    public static final int NORMAL_STATE = 0;
    //全线编辑的state
    public static final int AUTHORITY_STATE = 1;
    //权限编辑状态前的一个state
    public static final int STATE_BEFORE_AUTHORITY = 2;
    //报表块编辑的state
    public static final int STATE_FORM_REPORT = 3;
    //报表块编辑状态前的一个state
    public static final int STATE_BEFORE_FORM_REPORT = 4;
    private T applyTarget;
    private int isAuthorityType = NORMAL_STATE;
    private int isFormReportType = NORMAL_STATE;

    public BaseUndoState(T t) {
        this.applyTarget = t;
    }

    public T getApplyTarget() {
        return this.applyTarget;
    }

    public void setAuthorityType(int isAuthoritytype) {
        this.isAuthorityType = isAuthoritytype;
    }

    public int getAuthorityType() {
        return isAuthorityType;
    }

    public void setFormReportType(int isFormReportType) {
        this.isFormReportType = isFormReportType;
    }

    public int getFormReportType() {
        return isFormReportType;
    }
    /**
     * 应用状态
     */
    public abstract void applyState();
}