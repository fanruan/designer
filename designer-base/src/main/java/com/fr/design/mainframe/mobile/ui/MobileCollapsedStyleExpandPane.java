package com.fr.design.mainframe.mobile.ui;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UIIntNumberField;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.widget.UITitleSplitLine;
import com.fr.form.ui.mobile.MobileCollapsedStyle;
import com.fr.form.ui.mobile.MobileFormCollapsedStyle;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/2/13
 */
public class MobileCollapsedStyleExpandPane extends MobileCollapsedStylePane {

    private static final Dimension DEFAULT_SPINNER_SIZE = new Dimension(60, 24);

    private UISpinner rowSpinner;

    public MobileCollapsedStyleExpandPane() {
    }


    @Override
    protected JPanel createLinePane() {
        UITitleSplitLine splitLine = new UITitleSplitLine(Toolkit.i18nText("Fine-Design_Mobile_Collapse_Line_Number"), 520);
        splitLine.setPreferredSize(new Dimension(520, 20));
        this.rowSpinner = new UISpinner(1, Integer.MAX_VALUE, 1, 1) {
            @Override
            protected UINumberField initNumberField(){
                return new UIIntNumberField();
            }
        };
        rowSpinner.setPreferredSize(DEFAULT_SPINNER_SIZE);
        JPanel panel = new JPanel();
        panel.setLayout(FRGUIPaneFactory.createBoxFlowLayout());
        panel.add(new UILabel(Toolkit.i18nText("Fine-Design_Mobile_Collapse_Start_From")));
        panel.add(rowSpinner);
        panel.add(new UILabel(Toolkit.i18nText("Fine-Design_Mobile_Collapse_Row_To_Fold")));
        JPanel linePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        linePane.add(splitLine, BorderLayout.NORTH);
        linePane.add(panel, BorderLayout.CENTER);
        return linePane;
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
