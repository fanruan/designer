package com.fr.design.condition;

import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.highlight.HighlightAction;
import com.fr.report.cell.cellattr.highlight.ValueHighlightAction;

/**
* @author richie
* @date 2015-03-26
* @since 8.0
*/
public class NewRealValuePane extends ConditionAttrSingleConditionPane<HighlightAction> {
    private ValueEditorPane valueEditor;

    public NewRealValuePane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
        this.add(new UILabel(Inter.getLocText("FR-Designer_New_Value") + ":"));
        valueEditor = ValueEditorPaneFactory.createBasicValueEditorPane();
        this.add(valueEditor);
    }

    @Override
    public String nameForPopupMenuItem() {
        return Inter.getLocText("FR-Designer_New_Value");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public void populate(HighlightAction ha) {
        this.valueEditor.populate(((ValueHighlightAction)ha).getValue());
    }

    public HighlightAction update() {
        return new ValueHighlightAction(this.valueEditor.update());
    }
}