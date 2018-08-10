package com.fr.design.condition;

import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;

import com.fr.report.cell.cellattr.highlight.HighlightAction;
import com.fr.report.cell.cellattr.highlight.PageHighlightAction;

/**
* @author richie
* @date 2015-03-26
* @since 8.0
*/
public class PagePane extends ConditionAttrSingleConditionPane<HighlightAction> {
    private UILabel pageLabel;
    private UIComboBox pageComboBox;

    public PagePane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
        pageLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Pagination") + ":");
        this.add(pageLabel);
        this.pageComboBox = new UIComboBox(new String[] {
                com.fr.design.i18n.Toolkit.i18nText("Utils-No_Pagination"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Cell_Write_Page_After_Row"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Cell_Write_Page_Before_Row"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Cell_Write_Page_After_Column"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Cell_Write_Page_Before_Column") });
        this.add(this.pageComboBox);
        this.pageComboBox.setSelectedIndex(0);
    }

    @Override
    public String nameForPopupMenuItem() {
        return com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Pagination");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public  void populate(HighlightAction ha) {
        this.pageComboBox.setSelectedIndex(((PageHighlightAction) ha).getPage());
    }

    public HighlightAction update() {
        return new PageHighlightAction(this.pageComboBox.getSelectedIndex());
    }
}