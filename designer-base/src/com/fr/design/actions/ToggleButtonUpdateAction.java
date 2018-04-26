package com.fr.design.actions;

import com.fr.design.gui.ibutton.UIToggleButton;

/**
 * 具有ToggleButton属性的action,继承这个类的action就会有
 * 
 * @author zhou
 * 
 */

public interface ToggleButtonUpdateAction {

	public abstract UIToggleButton createToolBarComponent();
}