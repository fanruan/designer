package com.fr.design.actions.cell.style;

import com.fr.base.Style;

/**
 * peter:这个方法只是在编辑Style的时候使用.
 */
public interface StyleActionInterface {
    public Style executeStyle(Style style2Mod, Style selectedStyle);

    /**
     * Update Style.
     *
     * @param style style
     */
    public void updateStyle(Style style);
}