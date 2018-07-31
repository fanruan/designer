package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.file.NewTemplatePane;

import javax.swing.Icon;

/**
 * Created by hzzz on 2017/12/26.
 */
public class ToolBarNewTemplatePane extends NewTemplatePane {

    private static final ToolBarNewTemplatePane instance = new ToolBarNewTemplatePane();

    private ToolBarNewTemplatePane() {
    }

    public static NewTemplatePane getInstance() {
        return instance;
    }

    @Override
    public Icon getNew() {
        return BaseUtils.readIcon("/com/fr/design/images/buttonicon/addicon.png");
    }

    @Override
    public Icon getMouseOverNew() {
        return BaseUtils.readIcon("/com/fr/design/images/buttonicon/add_press.png");
    }

    @Override
    public Icon getMousePressNew() {
        return BaseUtils.readIcon("/com/fr/design/images/buttonicon/add_press.png");
    }
}
