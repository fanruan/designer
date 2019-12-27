package com.fr.design.mainframe;

import com.fr.base.Style;
import com.fr.design.actions.utils.ReportActionUtils;
import com.fr.design.designer.TargetComponent;
import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.gui.frpane.HyperlinkGroupPane;
import com.fr.design.gui.frpane.HyperlinkGroupPaneActionProvider;
import com.fr.general.FRFont;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.js.NameJavaScriptGroup;
import com.fr.log.FineLoggerFactory;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.Constants;

import java.awt.Color;

/**
 * Created by plough on 2017/7/26.
 */
public class HyperlinkGroupPaneActionImpl implements HyperlinkGroupPaneActionProvider {
    private static HyperlinkGroupPaneActionProvider instance;
    private static Selection selection ;
    private HyperlinkGroupPaneActionImpl() {
    }

    public static HyperlinkGroupPaneActionProvider getInstance() {
        if (instance == null) {
            instance = new HyperlinkGroupPaneActionImpl();
        }
        return instance;
    }

    @Override
    public void populate(HyperlinkGroupPane hyperlinkGroupPane, TargetComponent elementCasePane) {
        ElementCasePane reportPane = (ElementCasePane)elementCasePane;
        final TemplateElementCase report = reportPane.getEditingElementCase();
        NameJavaScriptGroup nameHyperlinks = getNameJSGroup(reportPane, report);
        selection = reportPane.getSelection();
        hyperlinkGroupPane.populate(nameHyperlinks);

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
    public void saveSettings(HyperlinkGroupPane hyperlinkGroupPane) {
        // plough: 需要判断设计器是否初始化完成，不能用 HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()
        JTemplate jt = DesignerContext.getDesignerFrame().getSelectedJTemplate();
        if (jt == null) {
            return;
        }
        ElementCasePane reportPane = (ElementCasePane)jt.getCurrentElementCasePane();
        final TemplateElementCase report = reportPane.getEditingElementCase();
        final NameJavaScriptGroup updateNameHyperlinks = hyperlinkGroupPane.updateJSGroup();
        if (selection instanceof FloatSelection) {
            FloatElement selectedFloatElement = report.getFloatElement(((FloatSelection)selection).getSelectedFloatName());
            selectedFloatElement.setNameHyperlinkGroup(updateNameHyperlinks);
        } else {
            ReportActionUtils.actionIterateWithCellSelection((CellSelection)selection, report, new ReportActionUtils.IterAction() {
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
                    try {
                        editCellElement.setNameHyperlinkGroup((NameJavaScriptGroup) updateNameHyperlinks.clone());
                    } catch (CloneNotSupportedException e) {
                        // this shouldn't happen, since NameJavaScriptGroup are FCloneable
                        FineLoggerFactory.getLogger().error("InternalError: " + e.getMessage());
                    }
                }
            });
        }
        HistoryTemplateListCache.getInstance().getCurrentEditingTemplate().fireTargetModified();
    }
}
