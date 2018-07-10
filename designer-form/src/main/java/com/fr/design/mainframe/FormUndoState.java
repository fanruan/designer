package com.fr.design.mainframe;

import java.awt.Dimension;

import com.fr.form.main.Form;
import com.fr.form.ui.Widget;

public class FormUndoState extends BaseUndoState<BaseJForm> {
	private Form form;
	private Dimension designerSize;
	private int hValue;
	private int vValue;
	private Widget[] selectWidgets;
	private double widthValue;
	private double heightValue;
	private double slideValue;

	public FormUndoState(BaseJForm t, FormArea formArea) {
		super(t);

		try {
			this.form = (Form) ((Form) t.getTarget()).clone();
		} catch (CloneNotSupportedException ex) {
			throw new RuntimeException(ex);
		}

		this.selectWidgets = formArea.getFormEditor().getSelectionModel().getSelection().getSelectedWidgets();
		this.hValue = formArea.getHorizontalValue();
		this.vValue = formArea.getVerticalValue();
		this.designerSize = formArea.getAreaSize();
		this.widthValue = formArea.getWidthPaneValue();
		this.heightValue = formArea.getHeightPaneValue();
		this.slideValue = formArea.getSlideValue();
	}

	/**
	 * 返回form
	 */
	public Form getForm() {
		return form;
	}

	/**
	 * 返回选中的控件
	 */
	public Widget[] getSelectWidgets() {
		return selectWidgets;
	}

	/**
	 * 返回design区域大小
	 */
	public Dimension getAreaSize() {
		return designerSize;
	}

	/**
	 * 返回横向滚动条值
	 */
	public int getHorizontalValue() {
		return hValue;
	}

	/**
	 * 返回纵向滚动条值
	 */
	public int getVerticalValue() {
		return vValue;
	}
	
	/**
	 * 返回容器实际宽度
	 */
	public double getWidthValue() {
		return this.widthValue;
	}
	
	/**
	 * 返回容器实际高度
	 */
	public double getHeightValue() {
		return this.heightValue;
	}
	
	/**
	 * 返回设定的百分比值
	 */
	public double getSlideValue() {
		return this.slideValue;
	}

	@Override
	public void applyState() {
		this.getApplyTarget().applyUndoState4Form(this);
	}
}