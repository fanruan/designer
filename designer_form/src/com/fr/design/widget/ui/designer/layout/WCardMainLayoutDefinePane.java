package com.fr.design.widget.ui.designer.layout;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.design.designer.creator.cardlayout.XWCardTagLayout;
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
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.form.ui.container.cardlayout.WCardTitleLayout;
import com.fr.general.Inter;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

/**
 * Created by ibm on 2017/8/2.
 */
public class WCardMainLayoutDefinePane  extends AbstractDataModify<WCardMainBorderLayout> {
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
                new Component[]{new UILabel(Inter.getLocText("FR-Designer-Widget_Style")), accessibleCardTagWLayoutBorderStyleEditor}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_W3, IntervalConstants.INTERVAL_L1);
        stylePane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        accessibleCardlayout.add(stylePane, BorderLayout.CENTER);
        UIExpandablePane advanceExpandablePane = new UIExpandablePane(Inter.getLocText("FR-Designer_Advanced"), 280, 20, accessibleCardlayout);
        final JPanel jPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setCarousel = new UICheckBox(Inter.getLocText("FR-Designer_setCarousel"));
        IntervalPane = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{
                new UILabel(Inter.getLocText("FR-Designer_carouselInterval")), carouselInterval}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
        IntervalPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel.add(setCarousel, BorderLayout.NORTH);
        jPanel.add(IntervalPane, BorderLayout.CENTER);
        setCarousel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IntervalPane.setVisible(setCarousel.isSelected());
            }
        });
        UIExpandablePane setCarouselPane = new UIExpandablePane(Inter.getLocText("FR-Designer_Tab_carousel"), 280, 20, jPanel);
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
