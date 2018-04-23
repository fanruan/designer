package com.fr.design.mainframe;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.fr.form.FormElementCaseContainerProvider;
import com.fr.form.FormElementCaseProvider;

public class TabChangeAction extends AbstractAction {
	private final int index;
	private BaseJForm jform;
	private FormElementCaseContainerProvider ecContainer;

	public TabChangeAction(int index, BaseJForm jform) {
		this(index, jform, null);
	}
	
	public TabChangeAction(int index, BaseJForm jform, FormElementCaseContainerProvider ecContainer) {
		this.index = index;
		this.jform = jform;
		this.ecContainer = ecContainer;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		jform.tabChanged(index, ecContainer);
	}

}