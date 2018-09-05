package com.fr.design.mainframe;

import com.fr.design.EnvChangeEntrance;

/**
 * @see EnvChangeEntrance
 * @deprecated use {@link EnvChangeEntrance}
 */
@Deprecated
public class TemplatePane {

    public static EnvChangeEntrance getInstance() {
        return EnvChangeEntrance.getInstance();
    }

    private TemplatePane() {
    }
}

