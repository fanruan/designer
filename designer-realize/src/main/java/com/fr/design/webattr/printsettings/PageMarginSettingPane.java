package com.fr.design.webattr.printsettings;

import com.fr.base.Margin;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.report.UnitFieldPane;

import com.fr.stable.Constants;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by plough on 2018/3/5.
 */
public class PageMarginSettingPane extends JPanel {
    private UnitFieldPane marginTopUnitFieldPane;
    private UnitFieldPane marginBottomUnitFieldPane;
    private UnitFieldPane marginLeftUnitFieldPane;
    private UnitFieldPane marginRightUnitFieldPane;

    public PageMarginSettingPane() {
        initComponents();
    }
    private void initComponents() {
        // 页边距设置面板
        JPanel marginPane = FRGUIPaneFactory.createX_AXISBoxInnerContainer_M_Pane();
        // left
        JPanel marginLeftPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
        marginPane.add(marginLeftPane);

        JPanel marginLeftTextPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        marginLeftPane.add(marginLeftTextPane);
        marginLeftTextPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Top_Duplicate") + ":"));
        marginTopUnitFieldPane = new UnitFieldPane(Constants.UNIT_MM);
        marginLeftTextPane.add(marginTopUnitFieldPane);
        JPanel marginLeftUnitPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        marginLeftPane.add(marginLeftUnitPane);
        marginLeftUnitPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Bottom") + ":"));
        marginBottomUnitFieldPane = new UnitFieldPane(Constants.UNIT_MM);
        marginLeftUnitPane.add(marginBottomUnitFieldPane);

        // right
        JPanel marginRightPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
        marginPane.add(marginRightPane);

        // peter:这个一个垂直的上下的字符panel.
        JPanel marginRightTextPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        marginRightPane.add(marginRightTextPane);
        marginRightTextPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Left") + ":"));
        marginLeftUnitFieldPane = new UnitFieldPane(Constants.UNIT_MM);
        marginRightTextPane.add(marginLeftUnitFieldPane);

        JPanel marginRightUnitPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        marginRightPane.add(marginRightUnitPane);
        marginRightUnitPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Right") + ":"));
        marginRightUnitFieldPane = new UnitFieldPane(Constants.UNIT_MM);
        marginRightUnitPane.add(marginRightUnitFieldPane);

        this.setLayout(new BorderLayout());
        this.add(marginPane, BorderLayout.CENTER);
    }

    public void populate(Margin margin) {
        marginTopUnitFieldPane.setUnitValue(margin.getTop());
        marginLeftUnitFieldPane.setUnitValue(margin.getLeft());
        marginBottomUnitFieldPane.setUnitValue(margin.getBottom());
        marginRightUnitFieldPane.setUnitValue(margin.getRight());
    }

    public Margin updateBean() {
        Margin margin = new Margin();
        margin.setTop(marginTopUnitFieldPane.getUnitValue());
        margin.setLeft(marginLeftUnitFieldPane.getUnitValue());
        margin.setBottom(marginBottomUnitFieldPane.getUnitValue());
        margin.setRight(marginRightUnitFieldPane.getUnitValue());
        return margin;
    }
}
