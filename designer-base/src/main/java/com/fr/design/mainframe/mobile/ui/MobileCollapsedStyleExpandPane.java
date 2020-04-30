package com.fr.design.mainframe.mobile.ui;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UIIntNumberField;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.mobile.MobileCollapsedStyle;
import com.fr.form.ui.mobile.MobileFormCollapsedStyle;

import javax.swing.*;
import java.awt.*;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/2/13
 */
public class MobileCollapsedStyleExpandPane extends MobileCollapsedStylePane {

    private UISpinner rowSpinner;

    public MobileCollapsedStyleExpandPane() {
    }

    @Override
    protected void createConfigPanes(JPanel settingPane) {
        JPanel lineNumberConfigPane = this.createLineNumberConfigPane();
        settingPane.add(lineNumberConfigPane);
        super.createConfigPanes(settingPane);
    }

    protected JPanel createLineNumberConfigPane() {
        JPanel configPane = super.createTitleConfigPane(Toolkit.i18nText("Fine-Design_Mobile_Collapse_Line_Number"));

        UILabel collapseLocationLabel = createLabel(Toolkit.i18nText("Fine-Design_Mobile_Collapse_Show_Button"));
        this.rowSpinner = new UISpinner(1, Integer.MAX_VALUE, 1, 1) {
            @Override
            protected UINumberField initNumberField(){
                return new UIIntNumberField();
            }
        };
        this.rowSpinner.setPreferredSize(new Dimension(62, COMPONENT_HEIGHT));
        JPanel defaultCollapsedStatePanel = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        defaultCollapsedStatePanel.add(createLabel(Toolkit.i18nText("Fine-Design_Mobile_Collapse_Start_From") + " "));
        defaultCollapsedStatePanel.add(rowSpinner);
        defaultCollapsedStatePanel.add(createLabel(" " + Toolkit.i18nText("Fine-Design_Mobile_Collapse_Row_To_Fold")));

        double[] rowSize = {COMPONENT_HEIGHT};
        double[] columnSize = {LABEL_WIDTH, COMPONENT_WIDTH};
        double[] verticalGaps = {0};
        JPanel navButtonSettingsPanel = TableLayoutHelper.createDiffVGapTableLayoutPane(new JComponent[][]{
                {collapseLocationLabel, defaultCollapsedStatePanel},
        }, rowSize, columnSize, 5, verticalGaps);
        navButtonSettingsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        configPane.add(navButtonSettingsPanel);

        return configPane;
    }

    @Override
    public void populateBean(MobileCollapsedStyle ob) {
        super.populateBean(ob);
        rowSpinner.setValue(((MobileFormCollapsedStyle) ob).getLineAttr().getNumber());
    }

    @Override
    public MobileCollapsedStyle updateBean() {
        MobileCollapsedStyle style = super.updateBean();
        ((MobileFormCollapsedStyle) style).getLineAttr().setNumber((int) rowSpinner.getValue());
        return style;
    }

    @Override
    protected MobileCollapsedStyle updateDiffBean() {
        return new MobileFormCollapsedStyle();
    }

}
