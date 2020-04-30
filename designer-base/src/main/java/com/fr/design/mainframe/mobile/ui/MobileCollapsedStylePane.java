package com.fr.design.mainframe.mobile.ui;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.ModeButtonGroup;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.UITitleSplitLine;
import com.fr.design.style.color.NewColorSelectBox;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.mobile.CollapseState;
import com.fr.form.ui.mobile.MobileChartCollapsedStyle;
import com.fr.form.ui.mobile.MobileCollapsedStyle;

import javax.swing.*;
import java.awt.*;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/2/13
 */
public class MobileCollapsedStylePane extends BasicBeanPane<MobileCollapsedStyle> {

    public final static Color THEME_COLOR = Color.decode("#2F8EF1");
    public final static Font TEXT_FONT = new Font("Default", Font.PLAIN, 12);
    public final static Color TEXT_FONT_COLOR = Color.decode("#333334");
    public final static int LABEL_WIDTH = 96;
    public final static int COMPONENT_HEIGHT = 20;
    public final static int COMPONENT_WIDTH = 160;

    private UICheckBox showButtonCheck;
    private NewColorSelectBox buttonColorBox;
    private UITextField foldedTextFiled;
    private UITextField unfoldedTextFiled;
    private ModeButtonGroup<CollapseState> buttonGroup;


    public MobileCollapsedStylePane() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder( 10, 5, 0, 5));

        JPanel settingPane = this.createSettingPane();
        this.createConfigPanes(settingPane);

        this.add(settingPane);
    }

    private JPanel createSettingPane() {
        JPanel settingPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        settingPane.setBorder(GUICoreUtils.createTitledBorder(Toolkit.i18nText("Fine-Design_Report_Set"), THEME_COLOR));
        return settingPane;
    }

    protected UILabel createLabel(String text) {
        UILabel label = new UILabel(text, SwingConstants.RIGHT);
        label.setForeground(TEXT_FONT_COLOR);
        label.setFont(TEXT_FONT);
        return label;
    }

    protected void createConfigPanes(JPanel settingPane) {
        JPanel collapsedButtonConfigPane = this.createCollapsedButtonConfigPane();
        settingPane.add(collapsedButtonConfigPane);
    }

    protected JPanel createTitleConfigPane(String title) {
        JPanel configPane = new JPanel();
        configPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        configPane.setLayout(new BoxLayout(configPane, BoxLayout.Y_AXIS));
        Dimension titleLineDim = new Dimension(610, 20);
        UITitleSplitLine splitLine = new UITitleSplitLine(title, titleLineDim.width);
        splitLine.setPreferredSize(titleLineDim);
        configPane.add(splitLine);
        return configPane;
    }

    private JPanel createCollapsedButtonConfigPane() {
        JPanel configPane = this.createTitleConfigPane(Toolkit.i18nText("Fine-Design_Mobile_Collapse_Button"));

        UILabel showButtonLabel = this.createLabel(Toolkit.i18nText("Fine-Design_Mobile_Collapse_Show_Button"));
        showButtonCheck = new UICheckBox(Toolkit.i18nText(Toolkit.i18nText("Fine-Design_Mobile_Collapse_Show_Button_On_Right")));
        showButtonCheck.setForeground(TEXT_FONT_COLOR);
        showButtonCheck.setFont(TEXT_FONT);

        UILabel buttonColorLabel = this.createLabel(Toolkit.i18nText("Fine-Design_Mobile_Collapse_Button_Color"));
        buttonColorBox = new NewColorSelectBox(COMPONENT_WIDTH);

        UILabel foldedLabel = this.createLabel(Toolkit.i18nText("Fine-Design_Mobile_Collapse_Folded_Hint"));
        foldedTextFiled = new UITextField();

        UILabel unfoldedLabel = this.createLabel(Toolkit.i18nText("Fine-Design_Mobile_Collapse_UnFolded_Hint"));
        unfoldedTextFiled = new UITextField();

        UILabel defaultCollapsedStateLabel = this.createLabel(Toolkit.i18nText("Fine-Design_Mobile_Collapse_Default_State"));
        JPanel defaultCollapsedStatePanel = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        UIRadioButton foldedButton = new UIRadioButton(Toolkit.i18nText("Fine-Design_Mobile_Collapse_Fold"));
        foldedButton.setSelected(true);
        foldedButton.setPreferredSize(new Dimension(COMPONENT_WIDTH / 2, COMPONENT_HEIGHT));
        foldedButton.setForeground(TEXT_FONT_COLOR);
        foldedButton.setFont(TEXT_FONT);
        UIRadioButton unfoldedButton = new UIRadioButton(Toolkit.i18nText("Fine-Design_Mobile_Collapse_Unfold"));
        unfoldedButton.setPreferredSize(new Dimension(COMPONENT_WIDTH / 2, COMPONENT_HEIGHT));
        unfoldedButton.setForeground(TEXT_FONT_COLOR);
        unfoldedButton.setFont(TEXT_FONT);
        defaultCollapsedStatePanel.add(foldedButton);
        defaultCollapsedStatePanel.add(unfoldedButton);

        buttonGroup = new ModeButtonGroup<>();
        buttonGroup.put(CollapseState.FOLDED, foldedButton);
        buttonGroup.put(CollapseState.UNFOLDED, unfoldedButton);

        double[] rowSize = {COMPONENT_HEIGHT, COMPONENT_HEIGHT, COMPONENT_HEIGHT, COMPONENT_HEIGHT, COMPONENT_HEIGHT};
        double[] columnSize = {LABEL_WIDTH, COMPONENT_WIDTH};
        double[] verticalGaps = {10, 10, 10, 10, 10};
        JPanel contentPane = TableLayoutHelper.createDiffVGapTableLayoutPane(new JComponent[][]{
                {showButtonLabel, showButtonCheck},
                {buttonColorLabel, buttonColorBox},
                {foldedLabel, foldedTextFiled},
                {unfoldedLabel, unfoldedTextFiled},
                {defaultCollapsedStateLabel, defaultCollapsedStatePanel}
        }, rowSize, columnSize, 5, verticalGaps);
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        configPane.add(contentPane);

        return configPane;
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
