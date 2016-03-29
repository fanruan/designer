package com.fr.design.condition;

import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.highlight.ForegroundHighlightAction;
import com.fr.report.cell.cellattr.highlight.HighlightAction;

import java.awt.*;

/**
* @author richie
* @date 2015-03-26
* @since 8.0
*/
public class ForeGroundPane extends ConditionAttrSingleConditionPane<HighlightAction> {
    private UILabel foregroundLabel;
    private ColorSelectBox foregroundColorPane;
    private UIComboBox foreScopeComboBox;


    public ForeGroundPane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
        foregroundLabel = new UILabel(Inter.getLocText("FR-Designer_Foreground") + ":");
        this.foregroundColorPane = new ColorSelectBox(80);
        this.add(foregroundLabel);
        this.add(this.foregroundColorPane);
        this.foreScopeComboBox = new UIComboBox(new String[] {
                Inter.getLocText("Utils-Current_Cell"),
                Inter.getLocText("Utils-Current_Row"),
                Inter.getLocText("Utils-Current_Column") });
        this.add(this.foreScopeComboBox);
        this.foregroundColorPane.setSelectObject(Color.black);
    }

    @Override
    public String nameForPopupMenuItem() {
        return Inter.getLocText("FR-Designer_Foreground");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public  void populate(HighlightAction ha) {
        this.foregroundColorPane.setSelectObject(((ForegroundHighlightAction)ha).getForegroundColor());
        this.foreScopeComboBox.setSelectedIndex(((ForegroundHighlightAction)ha).getScope());
    }

    public HighlightAction update() {
        return new ForegroundHighlightAction(this.foregroundColorPane.getSelectObject(), this.foreScopeComboBox.getSelectedIndex());
    }
}