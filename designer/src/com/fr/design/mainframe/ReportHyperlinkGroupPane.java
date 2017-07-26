package com.fr.design.mainframe;

import com.fr.base.Style;
import com.fr.design.actions.utils.ReportActionUtils;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.frpane.HyperlinkGroupPane;
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

    private ReportHyperlinkGroupPane() {
        super();
    }

    public synchronized static ReportHyperlinkGroupPane getInstance() {
        if (singleton == null) {
            singleton = new ReportHyperlinkGroupPane();
        }
        singleton.refreshPane();
        return singleton;
    }

    private void refreshPane() {
        ElementCasePane reportPane = ((JWorkBook)HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()).getEditingElementCasePane();
        if (reportPane == null) {
            return;
        }
//
        final TemplateElementCase report = reportPane.getEditingElementCase();
        NameJavaScriptGroup nameHyperlinks = getNameJSGroup(reportPane, report);
        populate(nameHyperlinks);
    }

    public void populate(ElementCasePane reportPane) {
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

    @Override
    public void saveSettings() {
        ElementCasePane reportPane = ((JWorkBook)HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()).getEditingElementCasePane();
        if (reportPane == null) {
            return;
        }
        final TemplateElementCase report = reportPane.getEditingElementCase();
        final Selection sel = reportPane.getSelection();
        final NameJavaScriptGroup updateNameHyperlinks = updateJSGroup();
        if (sel instanceof FloatSelection) {
            FloatElement selectedFloatElement = report.getFloatElement(((FloatSelection)sel).getSelectedFloatName());
            selectedFloatElement.setNameHyperlinkGroup(updateNameHyperlinks);
        } else {
            ReportActionUtils.actionIterateWithCellSelection((CellSelection)sel, report, new ReportActionUtils.IterAction() {
                public void dealWith(CellElement editCellElement) {
                    Style elementStyle = editCellElement.getStyle();
                    FRFont frFont = elementStyle.getFRFont();
                    if (updateNameHyperlinks.size() > 0) {
                        frFont = frFont.applyForeground(Color.blue);
                        frFont = frFont.applyUnderline(Constants.LINE_THIN);
                    } else {
                        frFont = frFont.applyForeground(Color.black);
                        frFont = frFont.applyUnderline(Constants.LINE_NONE);
                    }
                    editCellElement.setStyle(elementStyle.deriveFRFont(frFont));
                    editCellElement.setNameHyperlinkGroup(updateNameHyperlinks);
                }
            });
        }
    }
}
