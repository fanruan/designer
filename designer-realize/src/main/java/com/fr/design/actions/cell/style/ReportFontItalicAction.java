/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.cell.style;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.base.core.StyleUtils;
import com.fr.general.FRFont;

import com.fr.design.mainframe.ElementCasePane;

import javax.swing.*;

/**
 * Bold.
 */
public class ReportFontItalicAction extends ReportFontBoldAction {

    private final static Icon[] ICONS = {BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/italic.png"), BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/italic_white.png")};

    public ReportFontItalicAction(ElementCasePane t) {
		super(t);

		this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_FRFont_Italic"));
		this.setSmallIcon(ICONS, true);
	}


    protected void setSelectedFont (Style style) {
        this.style = StyleUtils.italicReportFont(style);
    }

    protected void setUnselectedFont (Style style) {
        this.style = StyleUtils.unItalicReportFont(style);
    }

    protected boolean isStyle(FRFont frFont) {
        return frFont.isItalic();
    }

}
