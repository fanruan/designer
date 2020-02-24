package com.fr.design.mainframe.mobile.ui;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.gui.ibutton.ModeButtonGroup;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.layout.VerticalFlowLayout;
import com.fr.design.mainframe.widget.UITitleSplitLine;
import com.fr.design.style.color.NewColorSelectBox;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.mobile.CollapseState;
import com.fr.form.ui.mobile.MobileChartCollapsedStyle;
import com.fr.form.ui.mobile.MobileCollapsedStyle;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/2/13
 */
public class MobileCollapsedStylePane extends BasicBeanPane<MobileCollapsedStyle> {

    private UICheckBox showButtonCheck;
    private NewColorSelectBox buttonColorBox;
    private UITextField foldedTextFiled;
    private UITextField unfoldedTextFiled;
    private ModeButtonGroup<CollapseState> buttonGroup;


    public MobileCollapsedStylePane() {
        TitledBorder titledBorder = GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Set"), null);
        VerticalFlowLayout layout = new VerticalFlowLayout(FlowLayout.LEADING, 0, 10);
        layout.setAlignLeft(true);
        this.setBorder(titledBorder);
        this.setLayout(layout);
        this.add(createLinePane());
        this.add(createSettingPane());
    }

    private JPanel createSettingPane() {
        JPanel settingPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        UITitleSplitLine splitLine = new UITitleSplitLine(Toolkit.i18nText("Fine-Design_Mobile_Collapse_Button"), 520);
        splitLine.setPreferredSize(new Dimension(520, 20));
        UILabel showButtonLabel = new UILabel(Toolkit.i18nText("Fine-Design_Mobile_Collapse_Show_Button"));
        showButtonCheck = new UICheckBox(Toolkit.i18nText("Fine-Design_Mobile_Collapse_Show_Button_On_Right"));
        UILabel buttonColorLabel = new UILabel(Toolkit.i18nText("Fine-Design_Mobile_Collapse_Button_Color"));
        buttonColorBox = new NewColorSelectBox(137);
        UILabel foldedLabel = new UILabel(Toolkit.i18nText("Fine-Design_Mobile_Collapse_Folded_Hint"));
        foldedTextFiled = new UITextField();
        UILabel unfoldedLabel = new UILabel(Toolkit.i18nText("Fine-Design_Mobile_Collapse_UnFolded_Hint"));
        unfoldedTextFiled = new UITextField();
        UILabel defaultStateLabel = new UILabel(Toolkit.i18nText("Fine-Design_Mobile_Collapse_Default_State"));
        buttonGroup = new ModeButtonGroup<>();
        UIRadioButton foldedButton = new UIRadioButton(Toolkit.i18nText("Fine-Design_Mobile_Collapse_Fold"));
        foldedButton.setSelected(true);
        UIRadioButton unfoldedButton = new UIRadioButton(Toolkit.i18nText("Fine-Design_Mobile_Collapse_Unfold"));
        buttonGroup.put(CollapseState.FOLDED, foldedButton);
        buttonGroup.put(CollapseState.UNFOLDED, unfoldedButton);
        JPanel flowLeftPane = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
        flowLeftPane.add(foldedButton);
        flowLeftPane.add(unfoldedButton);
        Component[][] northComponents = new Component[][] {
                new Component[] {showButtonLabel, showButtonCheck}
        };
        Component[][] southComponents = new Component[][] {
                new Component[] {defaultStateLabel, flowLeftPane}
        };
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p, p};
        double[] colSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}};
        Component[][] centerComponents = new Component[][] {
                new Component[] {buttonColorLabel, buttonColorBox},
                new Component[] {foldedLabel, foldedTextFiled},
                new Component[] {unfoldedLabel, unfoldedTextFiled},
        };
        JPanel northPane = TableLayoutHelper.createGapTableLayoutPane(northComponents, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
        JPanel southPane = TableLayoutHelper.createGapTableLayoutPane(southComponents, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
        final JPanel centerPane = TableLayoutHelper.createGapTableLayoutPane(centerComponents, rowSize, colSize, rowCount, LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM);
        JPanel panel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panel.add(northPane, BorderLayout.NORTH);
        panel.add(centerPane, BorderLayout.CENTER);
        panel.add(southPane, BorderLayout.SOUTH);
        settingPane.add(splitLine, BorderLayout.NORTH);
        settingPane.add(panel, BorderLayout.CENTER);
        showButtonCheck.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                centerPane.setVisible(showButtonCheck.isSelected());
            }
        });
        return settingPane;
    }

    protected JPanel createLinePane() {
        return FRGUIPaneFactory.createBorderLayout_S_Pane();
    }

    @Override
    public void populateBean(MobileCollapsedStyle ob) {
        showButtonCheck.setSelected(ob.getCollapseButton().isShowButton());
        buttonColorBox.setSelectObject(ob.getCollapseButton().getButtonColor());
        foldedTextFiled.setText(ob.getCollapseButton().getFoldedHint());
        unfoldedTextFiled.setText(ob.getCollapseButton().getUnfoldedHint());
        buttonGroup.setSelectButton(ob.getCollapseButton().getDefaultState());
    }

    @Override
    public MobileCollapsedStyle updateBean() {
        MobileCollapsedStyle style = updateDiffBean();
        style.getCollapseButton().setShowButton(showButtonCheck.isSelected());
        style.getCollapseButton().setButtonColor(buttonColorBox.getSelectObject());
        style.getCollapseButton().setFoldedHint(foldedTextFiled.getText());
        style.getCollapseButton().setUnfoldedHint(unfoldedTextFiled.getText());
        style.getCollapseButton().setDefaultState(buttonGroup.getCurrentSelected());
        return style;
    }

    protected MobileCollapsedStyle updateDiffBean() {
        return new MobileChartCollapsedStyle();
    }

    @Override
    protected String title4PopupWindow() {
        return Toolkit.i18nText("Fine-Design_Mobile_Collapse_Expand");
    }
}
