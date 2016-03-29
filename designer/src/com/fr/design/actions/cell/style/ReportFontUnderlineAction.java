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
import com.fr.stable.Constants;

/**
 * Bold.
 */
public class ReportFontUnderlineAction extends ReportFontBoldAction {
	public ReportFontUnderlineAction(ElementCasePane t) {
		super(t);

		this.setName(Inter.getLocText("FRFont-Underline"));
		this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/underline.png"));
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