package com.fr.design.form.parameter;

import com.fr.base.BaseUtils;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.properties.WidgetPropertyTable;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.core.WidgetOptionFactory;
import com.fr.design.gui.itable.PropertyGroup;
import com.fr.design.mainframe.FormDesignerModeForSpecial;
import com.fr.form.main.parameter.FormParameterUI;
import com.fr.form.parameter.FormSubmitButton;
import com.fr.general.Inter;

import java.util.ArrayList;

public class FormParaTargetMode extends FormDesignerModeForSpecial<FormParaDesigner> {

    static {
        XCreatorUtils.objectMap.put(FormSubmitButton.class, XFormSubmit.class);
    }

    public FormParaTargetMode(FormParaDesigner designer) {
        super(designer);
    }

    @Override
    public WidgetOption[] getPredefinedWidgetOptions() {
        return new WidgetOption[]{
                WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[]{"Query", "Form-Button"}),
                        BaseUtils.readIcon("/com/fr/web/images/form/resources/preview_16.png"), FormSubmitButton.class)};
    }

    @Override
    public ArrayList<PropertyGroup> createRootDesignerPropertyGroup() {
        return WidgetPropertyTable.getCreatorPropertyGroup(this.getTarget(), this.getTarget().getRootComponent());
    }

    @Override
    public boolean isFormParameterEditor() {
        return true;
    }

    @Override
    public int getMinDesignHeight() {
        return ((FormParameterUI) this.getTarget().getTarget()).getDesignSize().height + 20;
    }

    @Override
    public int getMinDesignWidth() {
        return ((FormParameterUI) this.getTarget().getTarget()).getDesignSize().width + 20;
    }
}