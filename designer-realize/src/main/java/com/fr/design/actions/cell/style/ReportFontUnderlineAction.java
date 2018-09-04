/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.cell.style;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.base.core.StyleUtils;
import com.fr.general.FRFont;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.stable.Constants;

import javax.swing.*;

/**
 * Bold.
 */
public class ReportFontUnderlineAction extends ReportFontBoldAction {

    private final static Icon[] ICONS = {BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/underline.png"), BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/underline_white.png")};

    public ReportFontUnderlineAction(ElementCasePane t) {
		super(t);

		this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FRFont_Underline"));
		this.setSmallIcon(ICONS, true);
	}

    protected void setSelectedFont (Style style) {
        this.style = StyleUtils.setReportFontUnderline(style, true);
    }

    protected void setUnselectedFont (Style style) {
        this.style = StyleUtils.setReportFontUnderline(style, false);
    }


    protected boolean isStyle(FRFont frFont) {
        return frFont.getUnderline() != Constants.LINE_NONE;
    }

}