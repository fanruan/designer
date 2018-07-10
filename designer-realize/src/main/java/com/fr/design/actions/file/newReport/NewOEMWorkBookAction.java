package com.fr.design.actions.file.newReport;

import com.fr.base.BaseUtils;

import javax.swing.*;

/**
 * @author richie
 * @date 2015-06-16
 * @since 8.0
 */
public class NewOEMWorkBookAction extends NewWorkBookAction {

    @Override
    protected Icon icon() {
        return BaseUtils.readIcon("/com/fr/plugin/oem/images/newoemcpt.png");
    }
}