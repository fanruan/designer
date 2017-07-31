package com.fr.design.present;

import java.util.ArrayList;
import java.util.List;

import com.fr.base.FRContext;
import com.fr.design.gui.controlpane.UIListControlPane;
import com.fr.general.NameObject;
import com.fr.design.condition.HighLightConditionAttributesPane;
import com.fr.design.gui.controlpane.JListControlPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.highlight.DefaultHighlight;
import com.fr.report.cell.cellattr.highlight.Highlight;
import com.fr.report.cell.cellattr.highlight.HighlightGroup;
import com.fr.stable.Nameable;

public class ConditionAttributesGroupPane extends UIListControlPane {
    private static ConditionAttributesGroupPane singleton;

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
		return new NameableCreator[] { new NameObjectCreator(Inter.getLocText("Condition_Attributes"), DefaultHighlight.class, HighLightConditionAttributesPane.class) };
	}

	@Override
	public void saveSettings() {
	}

	@Override
	public String title4PopupWindow() {
		return Inter.getLocText("Condition_Attributes");
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