package com.fr.design.mainframe.mobile.ui;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.gui.ibutton.UIColorButton;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UnsignedIntUISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.MobileTabFontConfPane;
import com.fr.design.mainframe.widget.UITitleSplitLine;
import com.fr.design.mainframe.widget.preview.MobileTemplatePreviewPane;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.general.cardtag.mobile.MobileTemplateStyle;
import com.fr.general.cardtag.mobile.TabFontConfig;
import com.fr.general.cardtag.mobile.UniteStyle;
import com.fr.stable.CoreConstants;

import javax.swing.*;
import java.awt.*;

public class UniteStyleDefinePane extends MobileTemplateStyleDefinePane {
    private static final int SPLIT_LINE_WIDTH = 520;
    private static final int SPLIT_LINE_HEIGHT = 20;

    private static final int TOGGLE_BUTTON_WIDTH = 20;
    private static final int LABEL_WIDTH = 75;
    private static final int LINE_COMPONENT_WIDTH = 157;
    private static final int LINE_COMPONENT_HEIGHT = 20;

    private UnsignedIntUISpinner paddingLeftSpinner;
    private UnsignedIntUISpinner paddingRightSpinner;

    private ColorSelectBox initialBackgroundColorBox;
    private ColorSelectBox selectedBackgroundColorBox;

    private LineComboBox borderWidthComboBox;
    private ColorSelectBox borderColorBox;
    private UnsignedIntUISpinner borderRadiusSpinner;

    private MobileTabFontConfPane tabFontConfPane;
    private UIColorButton selectedFontColorButton;

    public UniteStyleDefinePane(WCardTagLayout tagLayout) {
        super(tagLayout);
    }

    @Override
    protected void createBuiltinConfPane(JPanel centerPane) {
        // ignore builtin conf pane
    }

    @Override
    protected void createExtraConfPane(JPanel centerPane) {
        UITitleSplitLine paddingSplit = new UITitleSplitLine(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Tab_Button_Padding"), SPLIT_LINE_WIDTH);
        paddingSplit.setPreferredSize(new Dimension(SPLIT_LINE_WIDTH, SPLIT_LINE_HEIGHT));
        centerPane.add(paddingSplit);
        centerPane.add(this.createPaddingConfPane());

        UITitleSplitLine backgroundSplit = new UITitleSplitLine(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Tab_Button_Background"), SPLIT_LINE_WIDTH);
        backgroundSplit.setPreferredSize(new Dimension(SPLIT_LINE_WIDTH, SPLIT_LINE_HEIGHT));
        centerPane.add(backgroundSplit);
        centerPane.add(this.createBackgroundColorConfPanel());

        UITitleSplitLine borderSplit = new UITitleSplitLine(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Border"), SPLIT_LINE_WIDTH);
        borderSplit.setPreferredSize(new Dimension(SPLIT_LINE_WIDTH, SPLIT_LINE_HEIGHT));
        centerPane.add(borderSplit);
        centerPane.add(this.createBorderConfPanel());

        UITitleSplitLine fontSplit = new UITitleSplitLine(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Tab_Char"), SPLIT_LINE_WIDTH);
        fontSplit.setPreferredSize(new Dimension(SPLIT_LINE_WIDTH, SPLIT_LINE_HEIGHT));
        centerPane.add(fontSplit);
        centerPane.add(this.createFontConfPanel());
    }

    private JPanel createPaddingConfPane() {

        this.paddingLeftSpinner = new UnsignedIntUISpinner(0, Integer.MAX_VALUE, 1, UniteStyle.DEFAULT_PADDING_LEFT);
        this.paddingLeftSpinner.setPreferredSize(new Dimension(62, LINE_COMPONENT_HEIGHT));
        this.paddingRightSpinner = new UnsignedIntUISpinner(0, Integer.MAX_VALUE, 1, UniteStyle.DEFAULT_PADDING_RIGHT);
        this.paddingRightSpinner.setPreferredSize(new Dimension(62, LINE_COMPONENT_HEIGHT));

        UILabel paddingLeftLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Left") + ":", SwingConstants.RIGHT);
        paddingLeftLabel.setPreferredSize(new Dimension(LABEL_WIDTH, LINE_COMPONENT_HEIGHT));
        UILabel paddingRightLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Right") + ":", SwingConstants.RIGHT);
        paddingLeftLabel.setPreferredSize(new Dimension(LABEL_WIDTH, LINE_COMPONENT_HEIGHT));

        JPanel paddingPanel = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{
                paddingLeftLabel, paddingLeftSpinner, paddingRightLabel,paddingRightSpinner
        }}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L1, LayoutConstants.VGAP_MEDIUM);
        paddingPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 20));

        return paddingPanel;
    }

    private JPanel createBackgroundColorConfPanel() {
        this.initialBackgroundColorBox = new ColorSelectBox(LINE_COMPONENT_WIDTH);
        this.initialBackgroundColorBox.setPreferredSize(new Dimension(LINE_COMPONENT_WIDTH, LINE_COMPONENT_HEIGHT));
        this.selectedBackgroundColorBox = new ColorSelectBox(LINE_COMPONENT_WIDTH);
        this.selectedBackgroundColorBox.setPreferredSize(new Dimension(LINE_COMPONENT_WIDTH, LINE_COMPONENT_HEIGHT));

        UILabel initialBackgroundColorLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Init_Fill") + ":", SwingConstants.RIGHT);
        initialBackgroundColorLabel.setPreferredSize(new Dimension(LABEL_WIDTH, LINE_COMPONENT_HEIGHT));
        UILabel selectedBackgroundColorLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Select_Fill") + ":", SwingConstants.RIGHT);
        selectedBackgroundColorLabel.setPreferredSize(new Dimension(LABEL_WIDTH, LINE_COMPONENT_HEIGHT));

        JPanel backgroundColorPanel = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{
                new Component[]{ initialBackgroundColorLabel, initialBackgroundColorBox },
                new Component[]{ selectedBackgroundColorLabel, selectedBackgroundColorBox }
        }, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L1, LayoutConstants.VGAP_MEDIUM);
        backgroundColorPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 20));

        return backgroundColorPanel;
    }

    private JPanel createBorderConfPanel() {
        this.borderWidthComboBox = new LineComboBox(CoreConstants.UNDERLINE_STYLE_ARRAY);
        this.borderWidthComboBox.setPreferredSize(new Dimension(LINE_COMPONENT_WIDTH, LINE_COMPONENT_HEIGHT));
        this.borderColorBox = new ColorSelectBox(LINE_COMPONENT_WIDTH);
        this.borderColorBox.setPreferredSize(new Dimension(LINE_COMPONENT_WIDTH, LINE_COMPONENT_HEIGHT));
        this.borderRadiusSpinner = new UnsignedIntUISpinner(0, Integer.MAX_VALUE, 1, UniteStyle.DEFAULT_BORDER_RADIUS);
        this.borderRadiusSpinner.setPreferredSize(new Dimension(LINE_COMPONENT_WIDTH, LINE_COMPONENT_HEIGHT));

        UILabel borderSizeLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_FRFont_Line_Style") + ":", SwingConstants.RIGHT);
        borderSizeLabel.setPreferredSize(new Dimension(LABEL_WIDTH, LINE_COMPONENT_HEIGHT));
        UILabel borderColorLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Colors") + ":", SwingConstants.RIGHT);
        borderColorLabel.setPreferredSize(new Dimension(LABEL_WIDTH, LINE_COMPONENT_HEIGHT));
        UILabel borderRadiusLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Radius") + ":", SwingConstants.RIGHT);
        borderRadiusLabel.setPreferredSize(new Dimension(LABEL_WIDTH, LINE_COMPONENT_HEIGHT));

        JPanel borderPanel = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{
                new Component[]{ borderSizeLabel, borderWidthComboBox},
                new Component[]{ borderColorLabel, borderColorBox },
                new Component[]{ borderRadiusLabel, borderRadiusSpinner }
        }, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L1, LayoutConstants.VGAP_MEDIUM);
        borderPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 20));

        return borderPanel;
    }

    private JPanel createFontConfPanel() {
        this.tabFontConfPane = new MobileTabFontConfPane();
        this.selectedFontColorButton = new UIColorButton();
        this.selectedFontColorButton.setPreferredSize(new Dimension(TOGGLE_BUTTON_WIDTH, LINE_COMPONENT_HEIGHT));
        JPanel selectedFontColorPanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        selectedFontColorPanel.add(this.selectedFontColorButton);

        UILabel initialFontLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Tab_Init_Char") + ":", SwingConstants.RIGHT);
        initialFontLabel.setPreferredSize(new Dimension(LABEL_WIDTH, LINE_COMPONENT_HEIGHT));
        UILabel selectedFontLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Tab_Select_Char") + ":", SwingConstants.RIGHT);
        selectedFontLabel.setPreferredSize(new Dimension(LABEL_WIDTH, LINE_COMPONENT_HEIGHT));

        JPanel fontPanel = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{
                new Component[]{ initialFontLabel, tabFontConfPane },
                new Component[]{ selectedFontLabel, selectedFontColorPanel }
        }, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L1, LayoutConstants.VGAP_MEDIUM);
        fontPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 20));

        return fontPanel;
    }

    @Override
    protected void initDefaultConfig() {
        this.paddingLeftSpinner.setValue(UniteStyle.DEFAULT_PADDING_LEFT);
        this.paddingRightSpinner.setValue(UniteStyle.DEFAULT_PADDING_RIGHT);

        this.initialBackgroundColorBox.setSelectObject(UniteStyle.DEFAULT_INITIAL_BACKGROUND_COLOR);
        this.selectedBackgroundColorBox.setSelectObject(UniteStyle.DEFAULT_SELECTED_BACKGROUND_COLOR);

        this.borderWidthComboBox.setSelectedLineStyle(UniteStyle.DEFAULT_BORDER_LINE.getLineStyle());
        this.borderColorBox.setSelectObject(UniteStyle.DEFAULT_BORDER_LINE.getColor());
        this.borderRadiusSpinner.setValue(UniteStyle.DEFAULT_BORDER_RADIUS);

        this.tabFontConfPane.populate(UniteStyle.DEFAULT_TAB_FONT.getFont());
        this.selectedFontColorButton.setColor(UniteStyle.DEFAULT_TAB_FONT.getSelectColor());
    }

    @Override
    protected MobileTemplatePreviewPane createPreviewPane() {
        return null;
    }

    @Override
    protected MobileTemplateStyle getDefaultTemplateStyle() {
        return new UniteStyle();
    }

    @Override
    public void populateSubStyle(MobileTemplateStyle ob) {
        UniteStyle style = (UniteStyle) ob;
        this.paddingLeftSpinner.setValue(style.getPaddingLeft());
        this.paddingRightSpinner.setValue(style.getPaddingRight());

        this.initialBackgroundColorBox.setSelectObject(style.getInitialColor());
        this.selectedBackgroundColorBox.setSelectObject(style.getSelectColor());

        this.borderWidthComboBox.setSelectedLineStyle(style.getBorderLineStyle());
        this.borderColorBox.setSelectObject(style.getBorderColor());
        this.borderRadiusSpinner.setValue(style.getBorderRadius());

        this.tabFontConfPane.populate(style.getTabFontConfig().getFont());
        this.selectedFontColorButton.setColor(style.getTabFontConfig().getSelectColor());
    }

    @Override
    public MobileTemplateStyle updateSubStyle() {
        UniteStyle style = new UniteStyle();
        style.setPaddingLeft((int) this.paddingLeftSpinner.getValue());
        style.setPaddingRight((int) this.paddingRightSpinner.getValue());

        style.setInitialColor(this.initialBackgroundColorBox.getSelectObject());
        style.setSelectColor(this.selectedBackgroundColorBox.getSelectObject());

        style.setBorderLineStyle(this.borderWidthComboBox.getSelectedLineStyle());
        style.setBorderColor(this.borderColorBox.getSelectObject());
        style.setBorderRadius((int) this.borderRadiusSpinner.getValue());

        TabFontConfig config = new TabFontConfig();
        config.setFont(this.tabFontConfPane.update());
        config.setSelectColor(this.selectedFontColorButton.getColor());
        style.setTabFontConfig(config);

        return style;
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }
}
