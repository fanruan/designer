package com.fr.design.gui.chart;

import java.awt.Dialog;
import java.awt.Frame;

import com.fr.base.chart.BaseChartCollection;
import com.fr.design.dialog.BasicDialog;

/**
 *  * 抽象的, 用于多工程间协作, 封装一层 图表新建的对话框, 配合属性表确定: 先单独只要一种图表类型的对话框.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-7-10 上午10:07:56
 */
public abstract class MiddleChartDialog extends BasicDialog{

	public MiddleChartDialog(Dialog parent) {
		super(parent);
	}
	
    public MiddleChartDialog(Frame owner) {
        super(owner);
    }
    
    public abstract BaseChartCollection getChartCollection();
    
    public abstract void populate(BaseChartCollection cc);

}