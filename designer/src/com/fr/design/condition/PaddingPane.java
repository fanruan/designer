package com.fr.design.condition;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.fun.IndentationUnitProcessor;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.style.DefaultIndentationUnitProcessor;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.highlight.HighlightAction;
import com.fr.report.cell.cellattr.highlight.PaddingHighlightAction;

import javax.swing.*;

/**
* @author richie
* @date 2015-03-26
* @since 8.0
*/
public class PaddingPane extends ConditionAttrSingleConditionPane<HighlightAction> {
    private UILabel paddingLeft;
    private UIBasicSpinner paddingLeftSpinner;
    private UILabel paddingRight;
    private UIBasicSpinner paddingRightSpinner;
    private UIComboBox paddingScopeComboBox;
    private IndentationUnitProcessor indentationUnitProcessor = null;

    public PaddingPane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
        paddingLeft = new UILabel(Inter.getLocText("Style-Left_Indent") + ":");
        paddingLeftSpinner = new UIBasicSpinner(new SpinnerNumberModel(2, 0, Integer.MAX_VALUE, 1));
        GUICoreUtils.setColumnForSpinner(paddingLeftSpinner, 5);
        paddingRight = new UILabel(Inter.getLocText("Style-Right_Indent") + ":");
        paddingRightSpinner = new UIBasicSpinner(new SpinnerNumberModel(2, 0, Integer.MAX_VALUE, 1));
        GUICoreUtils.setColumnForSpinner(paddingRightSpinner, 5);
        this.add(paddingLeft);
        this.add(paddingLeftSpinner);
        this.add(paddingRight);
        this.add(paddingRightSpinner);
        this.paddingScopeComboBox = new UIComboBox(new String[] {
                Inter.getLocText("Utils-Current_Cell"),
                Inter.getLocText("Utils-Current_Row"),
                Inter.getLocText("Utils-Current_Column") });
        this.add(this.paddingScopeComboBox);
        this.paddingLeftSpinner.setValue(new Integer(0));
        this.paddingRightSpinner.setValue(new Integer(0));

        this.indentationUnitProcessor = ExtraDesignClassManager.getInstance().getIndentationUnitEditor();
        if (null == this.indentationUnitProcessor) {
            this.indentationUnitProcessor = new DefaultIndentationUnitProcessor();
        }
    }

    /**
     * 弹出窗口菜单命名
     * @return 菜单名
     */
    @Override
    public String nameForPopupMenuItem() {
        return Inter.getLocText("FR-Designer_Sytle-Indentation");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public  void populate(HighlightAction ha) {
        int leftPadding = indentationUnitProcessor.paddingUnitProcessor(((PaddingHighlightAction) ha).getPaddingLeft());
        int rightPadding = indentationUnitProcessor.paddingUnitProcessor(((PaddingHighlightAction) ha).getPaddingRight());
        this.paddingLeftSpinner.setValue(new Integer(leftPadding));
        this.paddingRightSpinner.setValue(new Integer(rightPadding));
        this.paddingScopeComboBox.setSelectedIndex(((PaddingHighlightAction) ha).getScope());
    }

    public HighlightAction update() {
        int leftPadding = indentationUnitProcessor.paddingUnitGainFromSpinner((Integer)this.paddingLeftSpinner.getValue());
        int rightPadding = indentationUnitProcessor.paddingUnitGainFromSpinner((Integer)this.paddingRightSpinner.getValue());
        return new PaddingHighlightAction(Integer.valueOf(leftPadding), Integer.valueOf(rightPadding),
                 this.paddingScopeComboBox.getSelectedIndex());
    }

}