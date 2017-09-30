/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.cell.style;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.base.core.StyleUtils;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;

import javax.swing.*;

/**
 * Bold.
 */
public class ReportFontItalicAction extends ReportFontBoldAction {

    private final static Icon blackIcon = BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/italic.png");
    private final static Icon whiteIcon = BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/italic_white.png");

    public ReportFontItalicAction(ElementCasePane t) {
		super(t);

		this.setName(Inter.getLocText("FRFont-italic"));
		this.setSmallIcon(blackIcon);
	}


    protected void setSelectedFont (Style style) {
        this.style = StyleUtils.italicReportFont(style);
    }

    protected void setUnselectedFont (Style style) {
        this.style = StyleUtils.unItalicReportFont(style);
    }

    protected Icon getToggleButtonIcon(boolean isSelected) {
        return isSelected ? blackIcon : whiteIcon;
    }

    protected boolean isStyle(FRFont frFont) {
        return frFont.isItalic();
    }

}