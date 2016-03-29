/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.beans.actions;

import com.fr.design.actions.TemplateComponentAction;
import com.fr.design.mainframe.FormDesigner;

/**
 * @author richer
 * @since 6.5.3
 */
public abstract class FormUndoableAction extends TemplateComponentAction<FormDesigner> {
	protected FormUndoableAction(FormDesigner t) {
		super(t);
	}
}