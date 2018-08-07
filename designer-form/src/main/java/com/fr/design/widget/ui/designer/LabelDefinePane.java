package com.fr.design.widget.ui.designer;

import com.fr.base.BaseUtils;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.style.FRFontPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.ui.designer.component.FormWidgetValuePane;
import com.fr.form.ui.Label;

import com.fr.stable.Constants;

import javax.swing.*;
import java.awt.*;


/**
 * Created by ibm on 2017/8/3.
 */
public class LabelDefinePane extends AbstractDataModify<Label> {
    private FormWidgetValuePane formWidgetValuePane;
    private UICheckBox isPageSetupVertically;
    private UICheckBox isStyleAlignmentWrapText;
    private UIButtonGroup hAlignmentPane;
    private FRFontPane frFontPane;

    public LabelDefinePane(XCreator xCreator) {
        super(xCreator);
        initComponent();
    }

    public void initComponent() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel advancePane = createAdvancePane();
        UIExpandablePane advanceExpandablePane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Advanced"), 280, 20, advancePane);
        this.add(advanceExpandablePane, BorderLayout.CENTER);
    }

    public JPanel createAdvancePane() {
        formWidgetValuePane = new FormWidgetValuePane(creator.toData(), false);
        isPageSetupVertically = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_PageSetup-Vertically"));
        isPageSetupVertically.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        isStyleAlignmentWrapText = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_StyleAlignment-Wrap_Text"));
        isStyleAlignmentWrapText.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        Icon[] hAlignmentIconArray = {BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_left_normal.png"),
                BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_center_normal.png"),
                BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_right_normal.png"),};
        Integer[] hAlignment = new Integer[]{Constants.LEFT, Constants.CENTER, Constants.RIGHT};
        hAlignmentPane = new UIButtonGroup<Integer>(hAlignmentIconArray, hAlignment);
        hAlignmentPane.setAllToolTips(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_FormStyleAlignment_Left")
                , com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_StyleAlignment_Center"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_StyleAlignment_Right")});
        frFontPane = new FRFontPane();
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p, p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 3}, {1, 1}, {1, 1}, {1, 1}, {1, 1}};
        UILabel widgetValueLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Estate_Widget_Value"));
        widgetValueLabel.setVerticalAlignment(SwingConstants.TOP);
        UILabel fontLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Font-Size"));
        fontLabel.setVerticalAlignment(SwingConstants.TOP);
        Component[][] components = new Component[][]{
                new Component[]{widgetValueLabel, formWidgetValuePane},
                new Component[]{isStyleAlignmentWrapText, null},
                new Component[]{isPageSetupVertically, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Widget_Display_Position")), hAlignmentPane},
                new Component[]{fontLabel, frFontPane},
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
        JPanel boundsPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        boundsPane.add(panel);
        return boundsPane;
    }

    @Override
    public String title4PopupWindow() {
        return "label";
    }

    @Override
    public void populateBean(Label ob) {
        formWidgetValuePane.populate(ob);
        isStyleAlignmentWrapText.setSelected(ob.isWrap());
        isPageSetupVertically.setSelected(ob.isVerticalCenter());
        hAlignmentPane.setSelectedItem(ob.getTextalign());
        frFontPane.populateBean(ob.getFont());
    }


    @Override
    public Label updateBean() {
        Label layout = (Label) creator.toData();
        formWidgetValuePane.update(layout);
        layout.setWrap(isStyleAlignmentWrapText.isSelected());
        layout.setVerticalCenter(isPageSetupVertically.isSelected());
        layout.setTextalign((int) hAlignmentPane.getSelectedItem());
        layout.setFont(frFontPane.update(layout.getFont()));
        return layout;
    }
}
