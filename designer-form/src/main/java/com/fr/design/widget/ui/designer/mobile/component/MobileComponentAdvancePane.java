package com.fr.design.widget.ui.designer.mobile.component;

import com.fr.base.iofile.attr.AttrMarkFactory;
import com.fr.base.iofile.attr.FormBodyPaddingAttrMark;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.widget.ui.designer.XmlRelationedBasicPane;
import com.fr.design.widget.ui.designer.component.PaddingBoundPane;
import com.fr.form.ui.RichStyleWidgetProvider;

import java.awt.BorderLayout;

/**
 * 只有内边距设置的高级设置
 */
public class MobileComponentAdvancePane extends XmlRelationedBasicPane {
    private PaddingBoundPane paddingBound;

    public MobileComponentAdvancePane(String xmlTag) {
        super(xmlTag);
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        paddingBound = new PaddingBoundPane(FormBodyPaddingAttrMark.DEFAULT_SIZE, FormBodyPaddingAttrMark.DEFAULT_SIZE, FormBodyPaddingAttrMark.DEFAULT_SIZE, FormBodyPaddingAttrMark.DEFAULT_SIZE);
        UIExpandablePane advanceExpandablePane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Advanced"), 280, 20, paddingBound);
        this.add(advanceExpandablePane, BorderLayout.NORTH);
    }

    @Override
    protected String title4PopupWindow() {
        return "ComponentAdvancePane";
    }

    public void update(RichStyleWidgetProvider marginWidget) {
        FormBodyPaddingAttrMark attrMark = marginWidget.getWidgetAttrMark(getXmlTag());
        attrMark = attrMark == null ? (FormBodyPaddingAttrMark) AttrMarkFactory.createAttrMark(getXmlTag()) : attrMark;
        attrMark.setPaddingMargin(paddingBound.updateBean());
        marginWidget.addWidgetAttrMark(attrMark);
    }

    public void populate(RichStyleWidgetProvider marginWidget) {
        FormBodyPaddingAttrMark attrMark = marginWidget.getWidgetAttrMark(getXmlTag());
        if (attrMark != null) {
            paddingBound.populateBean(attrMark.getPaddingMargin());
        }
    }
}