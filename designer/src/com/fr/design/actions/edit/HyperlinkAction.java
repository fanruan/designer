/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.edit;

import java.awt.Color;

import javax.swing.SwingUtilities;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.Style;
import com.fr.design.actions.ElementCaseAction;
import com.fr.design.actions.utils.ReportActionUtils;
import com.fr.design.actions.utils.ReportActionUtils.IterAction;
import com.fr.design.gui.frpane.HyperlinkGroupPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.general.FRFont;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.js.NameJavaScriptGroup;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.Constants;

/**
 * HyperlinkAction.
 */
public class HyperlinkAction extends ElementCaseAction {
	private boolean b;

	public HyperlinkAction(ElementCasePane t) {
    	super(t);
        this.setMenuKeySet(KeySetUtils.HYPER_LINK);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/hyperLink.png"));
    }

    /**
     * 计算Action, 
     * @return 返回是否需要记录undo
     */
	public boolean executeActionReturnUndoRecordNeeded() {
    	b = true;
        ElementCasePane reportPane = this.getEditingComponent();
        if (reportPane == null) {
            return false;
        }
        
        final TemplateElementCase report = reportPane.getEditingElementCase();
        NameJavaScriptGroup nameHyperlinks = getNameJSGroup(reportPane, report);
        final HyperlinkGroupPane pane = DesignerContext.getDesignerFrame().getSelectedJTemplate().getHyperLinkPane();
        pane.populate(nameHyperlinks);
        
        final Selection sel = reportPane.getSelection();
        BasicDialog dialog = pane.showWindow(SwingUtilities.getWindowAncestor(reportPane));
        dialog.addDialogActionListener(new DialogActionAdapter() {
			public void doOk() {
                super.doOk();
                final NameJavaScriptGroup updateNameHyperlinks = pane.updateJSGroup();
                if (sel instanceof FloatSelection) {
                    FloatElement selectedFloatElement = report.getFloatElement(((FloatSelection)sel).getSelectedFloatName());
                    selectedFloatElement.setNameHyperlinkGroup(updateNameHyperlinks);
                } else {
                	ReportActionUtils.actionIterateWithCellSelection((CellSelection)sel, report, new IterAction() {
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
                                FRContext.getLogger().error("InternalError: " + e.getMessage());
                            }
                        }
                    });
                }
            }
        	public void doCancel() {
        		b = false;
        	}
        });
        dialog.setVisible(true);

        return b;
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