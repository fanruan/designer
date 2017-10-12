package com.fr.design.gui.controlpane;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.js.JavaScript;
import com.fr.stable.Nameable;

import javax.swing.*;

public interface NameableCreator {
	public String menuName();
	
	public Icon menuIcon();
	
	public String createTooltip();
	
	public Nameable createNameable(UnrepeatedNameHelper helper);
	
	public Class<? extends BasicBeanPane> getUpdatePane();
	
	public Object acceptObject2Populate(Object ob);
	
	public void saveUpdatedBean(ListModelElement wrapper, Object bean);

	public Class <? extends JavaScript> getHyperlink();


	public boolean isNeedParameterWhenPopulateJControlPane();
}