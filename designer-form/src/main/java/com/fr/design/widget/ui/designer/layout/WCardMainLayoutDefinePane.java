package com.fr.design.widget.ui.designer.layout;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.accessibles.AccessibleCardTagWLayoutBorderStyleEditor;
import com.fr.design.widget.ui.designer.AbstractDataModify;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.form.ui.container.WCardLayout;
import com.fr.form.ui.container.cardlayout.WCardMainBorderLayout;


import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ibm on 2017/8/2.
 */
public class WCardMainLayoutDefinePane extends AbstractDataModify<WCardMainBorderLayout> {
    private AccessibleCardTagWLayoutBorderStyleEditor accessibleCardTagWLayoutBorderStyleEditor;
    private UICheckBox setCarousel;
    private UISpinner carouselInterval;
    private JPanel IntervalPane;

    public WCardMainLayoutDefinePane(XCreator xCreator) {
        super(xCreator);
        initComponent();
    }

    public void initComponent() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        carouselInterval = new UISpinner(0, Integer.MAX_VALUE, 1, 0);
        accessibleCardTagWLayoutBorderStyleEditor = new AccessibleCardTagWLayoutBorderStyleEditor();
        JPanel accessibleCardlayout = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel stylePane =  TableLayoutHelper.createGapTableLayoutPane(new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Style")), accessibleCardTagWLayoutBorderStyleEditor}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_W3, IntervalConstants.INTERVAL_L1);
        stylePane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        accessibleCardlayout.add(stylePane, BorderLayout.CENTER);
        UIExpandablePane advanceExpandablePane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Advanced"), 280, 20, accessibleCardlayout);
        final JPanel jPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setCarousel = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_setCarousel"));
        IntervalPane = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{
                new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_carouselInterval")), carouselInterval}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
        IntervalPane.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L6, IntervalConstants.INTERVAL_L5, IntervalConstants.INTERVAL_L6, IntervalConstants.INTERVAL_L6));
        jPanel.add(setCarousel, BorderLayout.NORTH);
        jPanel.add(IntervalPane, BorderLayout.CENTER);
        setCarousel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IntervalPane.setVisible(setCarousel.isSelected());
            }
        });
        UIExpandablePane setCarouselPane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Tab_carousel"), 280, 20, jPanel);
        this.add(advanceExpandablePane, BorderLayout.NORTH);
        this.add(setCarouselPane, BorderLayout.CENTER);
    }

    @Override
    public String title4PopupWindow() {
        return "tabFitLayout";
    }

    @Override
    public void populateBean(WCardMainBorderLayout ob) {
        WCardLayout cardLayout = ob.getCardPart();
        accessibleCardTagWLayoutBorderStyleEditor.setValue(cardLayout.getBorderStyle());
        setCarousel.setSelected(cardLayout.isCarousel());
        IntervalPane.setVisible(cardLayout.isCarousel());
        carouselInterval.setValue(cardLayout.getCarouselInterval());
    }


    @Override
    public WCardMainBorderLayout updateBean() {
        WCardMainBorderLayout layout = (WCardMainBorderLayout) creator.toData();
        WCardLayout wCardLayout = layout.getCardPart();
        wCardLayout.setBorderStyle((LayoutBorderStyle) accessibleCardTagWLayoutBorderStyleEditor.getValue());
        wCardLayout.setCarousel(setCarousel.isSelected());
        wCardLayout.setCarouselInterval(carouselInterval.getValue());
        return layout;
    }
}
