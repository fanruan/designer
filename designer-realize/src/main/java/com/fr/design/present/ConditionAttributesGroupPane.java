package com.fr.design.present;

import java.util.ArrayList;
import java.util.List;

import com.fr.base.FRContext;
import com.fr.design.actions.utils.ReportActionUtils;
import com.fr.design.gui.controlpane.UIListControlPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.NameObject;
import com.fr.design.condition.HighLightConditionAttributesPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;

import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.highlight.DefaultHighlight;
import com.fr.report.cell.cellattr.highlight.Highlight;
import com.fr.report.cell.cellattr.highlight.HighlightGroup;
import com.fr.report.core.SheetUtils;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.Nameable;

public class ConditionAttributesGroupPane extends UIListControlPane {
	private static ConditionAttributesGroupPane singleton;
	private TemplateCellElement editCellElement;  // 当前单元格对象
	private Selection editSelection;  // 当前编辑对象
	private ElementCasePane ePane;

	private ConditionAttributesGroupPane() {
		super();
	}

	public static ConditionAttributesGroupPane getInstance() {
		if (singleton == null) {
			singleton = new ConditionAttributesGroupPane();
		}
		return singleton;
	}

	@Override
	public NameableCreator[] createNameableCreators() {
		return new NameableCreator[] { new NameObjectCreator(com.fr.design.i18n.Toolkit.i18nText("Condition_Attributes"), DefaultHighlight.class, HighLightConditionAttributesPane.class) };
	}

	@Override
	public void saveSettings() {
		if (isPopulating) {
			return;
		}
		final TemplateElementCase tplEC = ePane.getEditingElementCase();
		final HighlightGroup highlightGroup = updateHighlightGroup();
		ReportActionUtils.actionIterateWithCellSelection((CellSelection) editSelection, tplEC, new ReportActionUtils.IterAction() {
			public void dealWith(CellElement editCellElement) {
				try {
					((TemplateCellElement)editCellElement).setHighlightGroup((HighlightGroup) highlightGroup.clone());
				} catch (CloneNotSupportedException e) {
					FRContext.getLogger().error("InternalError: " + e.getMessage());
				}
			}
		});
		DesignerContext.getDesignerFrame().getSelectedJTemplate().fireTargetModified();
	}

	@Override
	public String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Condition_Attributes");
	}

	@Override
	public String getAddItemText() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Add_Condition");
	}

	public void populate(ElementCasePane ePane) {
		this.ePane = ePane;
		this.editSelection = ePane.getSelection();
		CellSelection cs = (CellSelection) ePane.getSelection();
		final TemplateElementCase tplEC = ePane.getEditingElementCase();
		editCellElement = tplEC.getTemplateCellElement(cs.getColumn(), cs.getRow());
		if (editCellElement == null) {
			editCellElement = new DefaultTemplateCellElement(cs.getColumn(), cs.getRow());
		}

		SheetUtils.calculateDefaultParent(tplEC);  // 不知道这行代码的作用，怕去掉之后会出问题，先放在这里

		populate(editCellElement.getHighlightGroup());
	}

	/**
	 * Populate
	 */
	public void populate(HighlightGroup highlightGroup) {
		// marks这个必须放在前面，不论是否有高亮分组都可以操作
		if (highlightGroup == null || highlightGroup.size() <= 0) {
			this.populate(new NameObject[0]);
			return;
		}
		List<NameObject> nameObjectList = new ArrayList<NameObject>();

		for (int i = 0; i < highlightGroup.size(); i++) {
			nameObjectList.add(new NameObject(highlightGroup.getHighlight(i).getName(), highlightGroup.getHighlight(i)));
		}

		this.populate(nameObjectList.toArray(new NameObject[nameObjectList.size()]));
	}

	/**
	 * Update.
	 */
	public HighlightGroup updateHighlightGroup() {
		Nameable[] res = this.update();
		Highlight[] res_array = new Highlight[res.length];
		for (int i = 0; i < res.length; i++) {
			// carl:update出来的是一个对象，在块操作时就需要clone
			Highlight highlight = (Highlight)((NameObject)res[i]).getObject();
			highlight.setName(((NameObject)res[i]).getName());
			try {
				highlight = (Highlight)highlight.clone();
			} catch (CloneNotSupportedException e) {
				FRContext.getLogger().error(e.getMessage(), e);
			}
			res_array[i] = highlight;
		}

		return new HighlightGroup(res_array);
	}
}
