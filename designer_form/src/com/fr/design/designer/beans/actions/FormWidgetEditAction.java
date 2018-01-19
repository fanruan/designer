package com.fr.design.designer.beans.actions;

import com.fr.design.actions.TemplateComponentAction;
import com.fr.design.designer.beans.actions.behavior.UpdateBehavior;
import com.fr.design.mainframe.FormDesigner;
import com.fr.general.Inter;
import com.fr.plugin.ExtraClassManager;
import com.fr.stable.ReportFunctionProcessor;
import com.fr.stable.fun.FunctionProcessor;

import javax.swing.*;
import java.awt.event.ActionEvent;

public abstract class FormWidgetEditAction extends TemplateComponentAction<FormDesigner> {

	private UpdateBehavior updateBehavior;

	protected FormWidgetEditAction(FormDesigner t) {
		super(t);
	}

	@Override
	public void update() {
		if (updateBehavior != null) {
			updateBehavior.doUpdate(this);
		} else {
			this.setEnabled(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		// 记录功能点
		FunctionProcessor processor = ExtraClassManager.getInstance().getFunctionProcessor();
		if (processor != null) {
			FunctionProcessor functionProcessor;
			if (evt.getSource() instanceof JButton) {
				functionProcessor = ReportFunctionProcessor.FORM_WIDGET_EDIT_TOOLBAR;
			} else {
				functionProcessor = ReportFunctionProcessor.FORM_WIDGET_EDIT_POPUPMENU;
			}
			processor.recordFunction(functionProcessor);
		}
	}

	public void setUpdateBehavior(UpdateBehavior updateBehavior) {
		this.updateBehavior = updateBehavior;
	}

}