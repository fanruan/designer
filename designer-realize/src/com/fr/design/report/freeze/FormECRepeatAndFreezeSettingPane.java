package com.fr.design.report.freeze;

import javax.swing.JPanel;

import com.fr.general.Inter;

/**
 * 表单的重复标题行以及冻结设置的Pane
 * 
 * 目前只支持设置冻结行, 不支持横向的冻结列与填报
 */
public class FormECRepeatAndFreezeSettingPane extends RepeatAndFreezeSettingPane {
	
	/**
	 * 获取分页冻结的标题(表单中不需要写分页二字)
	 * 
	 * @return 分页冻结的标题
	 * 
	 *
	 * @date 2014-11-14-下午1:32:08
	 * 
	 */
	protected String getPageFrozenTitle(){
		return Inter.getLocText("FR-Engine_Frozen") + ":";
	}
	
	protected void initWriteListener(){
		
	}
	
	protected void addWriteFrozen(JPanel freezePanel) {
		
	}
	
}