package com.fr.design.mainframe.form;

import com.fr.design.event.TargetModifiedListener;
import com.fr.form.FormElementCaseProvider;

public interface FormECCompositeProvider {
	
	public static final String XML_TAG = "FormReportComponentComposite";

    public void setSelectedWidget(FormElementCaseProvider fc);
    /**
     *  添加目标改变的监听
     * @param targetModifiedListener     目标改变事件
     */
	public void addTargetModifiedListener(TargetModifiedListener targetModifiedListener);

}