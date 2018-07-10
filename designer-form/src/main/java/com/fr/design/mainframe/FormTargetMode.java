package com.fr.design.mainframe;

import java.util.ArrayList;

import com.fr.base.BaseUtils;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.core.WidgetOptionFactory;
import com.fr.design.gui.itable.PropertyGroup;
import com.fr.design.designer.properties.WidgetPropertyTable;
import com.fr.form.parameter.FormSubmitButton;
import com.fr.general.Inter;

public class FormTargetMode extends FormDesignerModeForSpecial<FormDesigner> {

	public FormTargetMode(FormDesigner t) {
		super(t);
	}

	/**
	 * 获取预定义的查询按钮
	 */
	public WidgetOption[] getPredefinedWidgetOptions() {
        return new WidgetOption[]{
                WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[]{"Query", "Form-Button"}),
                        BaseUtils.readIcon("/com/fr/web/images/form/resources/preview_16.png"), FormSubmitButton.class)};
    }

	public ArrayList<PropertyGroup> createRootDesignerPropertyGroup() {
		return WidgetPropertyTable.getCreatorPropertyGroup(this.getTarget(), this.getTarget().getRootComponent());
	}

	@Override
	public boolean isFormParameterEditor() {
		return false;
	}

	@Override
	public int getMinDesignHeight() {
		return getTarget().getTarget().getContainer().getMinDesignSize().height;
	}
	
	@Override
	public int getMinDesignWidth() {
		return getTarget().getTarget().getContainer().getMinDesignSize().width;
	}
}