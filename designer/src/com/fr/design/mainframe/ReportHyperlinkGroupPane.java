package com.fr.design.mainframe;

import com.fr.design.gui.frpane.HyperlinkGroupPane;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.js.NameJavaScriptGroup;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.TemplateElementCase;

/**
 * Created by plough on 2017/7/21.
 */
public class ReportHyperlinkGroupPane extends HyperlinkGroupPane {
    private static ReportHyperlinkGroupPane singleton;

    private ReportHyperlinkGroupPane() {
        super();
    }

    public synchronized static ReportHyperlinkGroupPane getInstance() {
        if (singleton == null) {
            singleton = new ReportHyperlinkGroupPane();
        }
        return singleton;
    }

    public void populate(ElementCasePane reportPane) {
//        TemplateElementCase elementCase = ePane.getEditingElementCase();
//        if (elementCase == null) {
//            return;
//        }
//        ePane.getSelection().populatePropertyPane(ePane);
        final TemplateElementCase report = reportPane.getEditingElementCase();
        NameJavaScriptGroup nameHyperlinks = getNameJSGroup(reportPane, report);
        populate(nameHyperlinks);
    }

    private NameJavaScriptGroup getNameJSGroup(ElementCasePane reportPane, final TemplateElementCase report) {
        NameJavaScriptGroup nameHyperlinks = null;
        final Selection sel = reportPane.getSelection();
        if (sel instanceof FloatSelection) {
            FloatElement selectedFloatElement = report.getFloatElement(((FloatSelection)sel).getSelectedFloatName());
            nameHyperlinks = selectedFloatElement.getNameHyperlinkGroup();
        } else {
            CellElement editCellElement = report.getCellElement(((CellSelection)sel).getColumn(), ((CellSelection)sel).getRow());
            if (editCellElement != null) {
                nameHyperlinks = editCellElement.getNameHyperlinkGroup();
            }
        }

        return nameHyperlinks;
    }
}
