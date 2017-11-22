package com.fr.design.mainframe;

import com.fr.base.Style;
import com.fr.design.actions.utils.ReportActionUtils;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.frpane.HyperlinkGroupPane;
import com.fr.design.gui.frpane.HyperlinkGroupPaneActionProvider;
import com.fr.general.FRFont;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.js.NameJavaScriptGroup;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.Constants;

import java.awt.*;

/**
 * Created by plough on 2017/7/21.
 */
public class ReportHyperlinkGroupPane extends HyperlinkGroupPane {
    private static ReportHyperlinkGroupPane singleton;

    // 仅供继承使用，外部通过 getInstance 获取实例
    protected ReportHyperlinkGroupPane(HyperlinkGroupPaneActionProvider hyperlinkGroupPaneActionProvider) {
        super(hyperlinkGroupPaneActionProvider);
    }

    public synchronized static ReportHyperlinkGroupPane getInstance(HyperlinkGroupPaneActionProvider hyperlinkGroupPaneActionProvider) {
        if (singleton == null) {
            singleton = new ReportHyperlinkGroupPane(hyperlinkGroupPaneActionProvider);
        }
        singleton.refreshPane();
        return singleton;
    }

    protected void refreshPane() {
        ElementCasePane reportPane = ((JWorkBook)HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()).getEditingElementCasePane();
        if (reportPane == null) {
            return;
        }
        populate(reportPane);
    }
}
