package com.fr.design.actions;

import javax.swing.Icon;

import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.mainframe.ElementCasePane;

public abstract class ButtonGroupAction extends ElementCaseAction{
	protected Icon[][] iconArray;
	protected Integer[] valueArray;

	private UIButtonGroup<Integer> group;
	
	protected ButtonGroupAction(ElementCasePane t, Icon[][] iconArray, Integer[] valueArray) {
		super(t);
		this.iconArray = iconArray;
		this.valueArray = valueArray;
	}
	
	protected Integer getSelectedValue() {
		if(getSelectedIndex() < 0 || getSelectedIndex() >= valueArray.length) {
			return valueArray[0];
		}
		return valueArray[getSelectedIndex()];
	}
	
	private int getSelectedIndex() {
		return createToolBarComponent().getSelectedIndex();
	}
	
	protected void setSelectedIndex(int value) {
		int index = -1;
		for(int i = 0; i < valueArray.length; i++) {
			if(value == valueArray[i]) {
				index = i;
				break;
			}
		}
        if(createToolBarComponent().hasClick()) {
            index = getSelectedIndex();
            createToolBarComponent().setClickState(false);
        }
		createToolBarComponent().removeActionListener(this);
		createToolBarComponent().setSelectedIndex(index);
		createToolBarComponent().addActionListener(this);
	}

	@Override
	public UIButtonGroup<Integer> createToolBarComponent() {
		if(group == null) {
			group = new UIButtonGroup<Integer>(iconArray, valueArray);
			group.addActionListener(this);
		}
		return group;
	}
}