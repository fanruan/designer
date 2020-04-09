package com.fr.design.mainframe.mobile.ui;

import com.fr.base.BaseUtils;
import com.fr.base.Utils;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIColorButton;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UnsignedIntUISpinner;
import com.fr.design.gui.style.FRFontPane;
import com.fr.design.gui.style.NumberDragBar;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.mobile.MobileBookMarkStyle;
import com.fr.form.ui.mobile.impl.SidebarMobileBookMarkStyle;
import com.fr.stable.CoreConstants;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * @author Starryi
 * @version 10.0
 * Created by Starryi on 2020/02/28
 */
public class SidebarMobileBookMarkStyleCustomDefinePane extends BasicBeanPane<MobileBookMarkStyle> {
    private static final long serialVersionUID = 1L;

    private static final int COLUMN_WIDTH = 160;

    private UnsignedIntUISpinner buttonWidthSpinner;
    private UnsignedIntUISpinner buttonHeightSpinner;
    private UnsignedIntUISpinner buttonGapSpinner;
    private UnsignedIntUISpinner buttonBorderRadiusSpinner;

    private ColorSelectBox normalBackgroundColorBox;
    private NumberDragBar normalOpacityDragBar;
    private UnsignedIntUISpinner normalOpacitySpinner;
    private LineComboBox normalBorderWidthComBox;
    private ColorSelectBox normalBorderColorBox;
    private UIComboBox normalFontNameComboBox;
    private UIComboBox normalFontSizeComboBox;
    private UIColorButton normalFontColorButton;
    private UIToggleButton normalFontItalicButton;
    private UIToggleButton normalFontBoldButton;

    private ColorSelectBox selectedBackgroundColorBox;
    private NumberDragBar selectedOpacityDragBar;
    private UnsignedIntUISpinner selectedOpacitySpinner;
    private LineComboBox selectedBorderWidthComBox;
    private ColorSelectBox selectedBorderColorBox;
    private UIComboBox selectedFontNameComboBox;
    private UIComboBox selectedFontSizeComboBox;
    private UIColorButton selectedFontColorButton;
    private UIToggleButton selectedFontItalicButton;
    private UIToggleButton selectedFontBoldButton;

    private SidebarMobileBookMarkStyle DEFAULT_STYLE = new SidebarMobileBookMarkStyle();

    public SidebarMobileBookMarkStyleCustomDefinePane() {
        this.initComponent();
    }

    private void initComponent() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        this.add(this.createNavButtonStylePanel());
        this.add(this.createNormalStateStylePanel());
        this.add(this.createSelectedStateStylePanel());
    }

    private JPanel createNavButtonStylePanel() {

        buttonWidthSpinner = new UnsignedIntUISpinner(20, 150, 1, DEFAULT_STYLE.getWidth());
        buttonHeightSpinner = new UnsignedIntUISpinner(20, 100, 1, DEFAULT_STYLE.getHeight());
        buttonGapSpinner = new UnsignedIntUISpinner(0, Integer.MAX_VALUE, 1, DEFAULT_STYLE.getGap());
        buttonBorderRadiusSpinner = new UnsignedIntUISpinner(0, Integer.MAX_VALUE, 1, DEFAULT_STYLE.getBorderRadius());

        UILabel sizeLabel = new UILabel(Toolkit.i18nText("Fine-Design_Mobile_BookMark_Style_Sidebar_Button_Size") + ":",
                SwingConstants.RIGHT);
        JPanel sizePane = FRGUIPaneFactory.createNColumnGridInnerContainer_Pane(2, 5, 0);
        sizePane.add(buttonWidthSpinner);
        sizePane.add(buttonHeightSpinner);

        JPanel sizeTipsPane = FRGUIPaneFactory.createNColumnGridInnerContainer_Pane(2, 5, 0);
        sizeTipsPane.add(new UILabel(Toolkit.i18nText("Fine-Design_Mobile_BookMark_Style_Sidebar_Button_Width"),
                SwingConstants.CENTER));
        sizeTipsPane.add(new UILabel(Toolkit.i18nText("Fine-Design_Mobile_BookMark_Style_Sidebar_Button_Height"),
                SwingConstants.CENTER));

        UILabel gapLabel = new UILabel(Toolkit.i18nText("Fine-Design_Mobile_BookMark_Style_Sidebar_Button_Gap") + ":",
                SwingConstants.RIGHT);
        UILabel borderRadiusLabel = new UILabel(Toolkit.i18nText("Fine-Design_Mobile_BookMark_Style_Sidebar_Button_Border_Radius") + ":",
                SwingConstants.RIGHT);

        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p};
        double[] columnSize = {p, COLUMN_WIDTH};
        double[] verticalGaps = {0, 10, 10};

        JPanel navButtonSettingsPanel = TableLayoutHelper.createDiffVGapTableLayoutPane(new JComponent[][]{
                {sizeLabel, sizePane},
                {null, sizeTipsPane},
                {gapLabel, buttonGapSpinner},
                {borderRadiusLabel, buttonBorderRadiusSpinner},
        }, rowSize, columnSize, 5, verticalGaps);

        navButtonSettingsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        JPanel containerPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        containerPane.setBorder(GUICoreUtils.createTitledBorder(Toolkit.i18nText("Fine-Design_Mobile_BookMark_Style_Sidebar_Button_Style"),
                Color.decode("#2F8EF1")));
        containerPane.add(navButtonSettingsPanel);

        return containerPane;
    }

    private JPanel createNormalStateStylePanel() {
        double p = TableLayout.PREFERRED;

        normalBackgroundColorBox  = new ColorSelectBox(COLUMN_WIDTH);
        normalBackgroundColorBox.setSelectObject(DEFAULT_STYLE.getBackgroundColor());
        normalOpacityDragBar = new NumberDragBar(0, 100);
        normalOpacityDragBar.setValue(DEFAULT_STYLE.getOpacity());
        normalOpacityDragBar.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                normalOpacitySpinner.setValue(normalOpacityDragBar.getValue());
            }
        });
        normalOpacitySpinner = new UnsignedIntUISpinner(0, 100, 1, DEFAULT_STYLE.getOpacity());
        normalOpacitySpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                normalOpacityDragBar.setValue((int) normalOpacitySpinner.getValue());
            }
        });
        normalBorderWidthComBox = new LineComboBox(CoreConstants.UNDERLINE_STYLE_ARRAY);
        normalBorderWidthComBox.setSelectedLineStyle(DEFAULT_STYLE.getBorderLineStyle());
        normalBorderColorBox = new ColorSelectBox(COLUMN_WIDTH);
        normalBorderColorBox.setSelectObject(DEFAULT_STYLE.getBorderColor());
        normalFontNameComboBox = new UIComboBox(Utils.getAvailableFontFamilyNames4Report());
        normalFontNameComboBox.setSelectedItem(DEFAULT_STYLE.getSelectedFontFamily());
        normalFontSizeComboBox = new UIComboBox(FRFontPane.FONT_SIZES);
        normalFontSizeComboBox.setSelectedItem(DEFAULT_STYLE.getFontSize());
        normalFontColorButton = new UIColorButton();
        normalFontColorButton.setColor(DEFAULT_STYLE.getFontColor());
        normalFontItalicButton = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/italic.png"));
        normalFontItalicButton.setSelected(DEFAULT_STYLE.isFontItalic());
        normalFontBoldButton = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/bold.png"));
        normalFontBoldButton.setSelected(DEFAULT_STYLE.isFontBold());

        JPanel opacityPane = new JPanel(new BorderLayout(0, 0));
        JPanel jp = FRGUIPaneFactory.createNColumnGridInnerContainer_Pane(2, 5, 0);
        jp.add(normalOpacityDragBar);
        jp.add(normalOpacitySpinner);
        opacityPane.add(jp, BorderLayout.CENTER);

        JPanel fontExtraPane = TableLayoutHelper.createGapTableLayoutPane(
                new JComponent[][]{{normalFontSizeComboBox, normalFontColorButton, normalFontItalicButton, normalFontBoldButton}},
                new double[]{p},
                new double[]{p, p, p, p},
                0, 5
        );

        double[] rowSize = {p, p, p, p, p};
        double[] columnSize = {p, COLUMN_WIDTH, p};

        JPanel normalStateStyleSettingsPanel = TableLayoutHelper.createGapTableLayoutPane(new JComponent[][]{
                {
                        new UILabel(Toolkit.i18nText("Fine-Design_Mobile_BookMark_Style_Sidebar_Background_Color") + ":", SwingConstants.RIGHT),
                        normalBackgroundColorBox
                },
                {
                        new UILabel(Toolkit.i18nText("Fine-Design_Mobile_BookMark_Style_Sidebar_Opacity") + ":", SwingConstants.RIGHT),
                        opacityPane,
                        new UILabel("%")
                },
                {
                        new UILabel(Toolkit.i18nText("Fine-Design_Mobile_BookMark_Style_Sidebar_Border_Width") + ":", SwingConstants.RIGHT),
                        normalBorderWidthComBox
                },
                {
                        new UILabel(Toolkit.i18nText("Fine-Design_Mobile_BookMark_Style_Sidebar_Border_Color") + ":", SwingConstants.RIGHT),
                        normalBorderColorBox
                },
                {
                        new UILabel(Toolkit.i18nText("Fine-Design_Mobile_BookMark_Style_Sidebar_Font") + ":", SwingConstants.RIGHT),
                        normalFontNameComboBox,
                        fontExtraPane
                }
        }, rowSize, columnSize, 5, 10);
        normalStateStyleSettingsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        JPanel containerPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        containerPane.setBorder(
                GUICoreUtils.createTitledBorder(Toolkit.i18nText("Fine-Design_Mobile_BookMark_Style_Sidebar_Normal_Style"),
                                                Color.decode("#2F8EF1"))
        );
        containerPane.add(normalStateStyleSettingsPanel);

        return containerPane;
    }

    private JPanel createSelectedStateStylePanel() {
        double p = TableLayout.PREFERRED;

        selectedBackgroundColorBox = new ColorSelectBox(COLUMN_WIDTH);
        selectedBackgroundColorBox.setSelectObject(DEFAULT_STYLE.getSelectedBackgroundColor());
        selectedOpacityDragBar = new NumberDragBar(0, 100);
        selectedOpacityDragBar.setValue(DEFAULT_STYLE.getSelectedOpacity());
        selectedOpacityDragBar.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                selectedOpacitySpinner.setValue(selectedOpacityDragBar.getValue());
            }
        });
        selectedOpacitySpinner = new UnsignedIntUISpinner(0, 100, 1, DEFAULT_STYLE.getSelectedOpacity());
        selectedOpacitySpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                selectedOpacityDragBar.setValue((int) selectedOpacitySpinner.getValue());
            }
        });
        selectedBorderWidthComBox = new LineComboBox(CoreConstants.UNDERLINE_STYLE_ARRAY);
        selectedBorderWidthComBox.setSelectedLineStyle(DEFAULT_STYLE.getSelectedBorderLineStyle());
        selectedBorderColorBox = new ColorSelectBox(COLUMN_WIDTH);
        selectedBorderColorBox.setSelectObject(DEFAULT_STYLE.getSelectedBorderColor());
        selectedFontNameComboBox = new UIComboBox(Utils.getAvailableFontFamilyNames4Report());
        selectedFontNameComboBox.setSelectedItem(DEFAULT_STYLE.getSelectedFontFamily());
        selectedFontSizeComboBox = new UIComboBox(FRFontPane.FONT_SIZES);
        selectedFontSizeComboBox.setSelectedItem(DEFAULT_STYLE.getSelectedFontSize());
        selectedFontColorButton = new UIColorButton();
        selectedFontColorButton.setColor(DEFAULT_STYLE.getSelectedFontColor());
        selectedFontItalicButton = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/italic.png"));
        selectedFontItalicButton.setSelected(DEFAULT_STYLE.isSelectedFontItalic());
        selectedFontBoldButton = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/bold.png"));
        selectedFontBoldButton.setSelected(DEFAULT_STYLE.isSelectedFontBold());

        JPanel opacityPane = new JPanel(new BorderLayout(0, 0));
        JPanel jp = FRGUIPaneFactory.createNColumnGridInnerContainer_Pane(2, 5, 0);
        jp.add(selectedOpacityDragBar);
        jp.add(selectedOpacitySpinner);
        opacityPane.add(jp, BorderLayout.CENTER);

        JPanel fontExtraPane = TableLayoutHelper.createGapTableLayoutPane(
                new JComponent[][]{{selectedFontSizeComboBox, selectedFontColorButton, selectedFontItalicButton, selectedFontBoldButton}},
                new double[]{p},
                new double[]{p, p, p, p},
                0, 5
        );

        double[] rowSize = {p, p, p, p, p};
        double[] columnSize = {p, COLUMN_WIDTH, p};

        JPanel selectedStateStyleSettingsPanel = TableLayoutHelper.createGapTableLayoutPane(new JComponent[][]{
                {
                        new UILabel(Toolkit.i18nText("Fine-Design_Mobile_BookMark_Style_Sidebar_Background_Color") + ":", SwingConstants.RIGHT),
                        selectedBackgroundColorBox
                },
                {
                        new UILabel(Toolkit.i18nText("Fine-Design_Mobile_BookMark_Style_Sidebar_Opacity") + ":", SwingConstants.RIGHT),
                        opacityPane,
                        new UILabel("%")
                },
                {
                        new UILabel(Toolkit.i18nText("Fine-Design_Mobile_BookMark_Style_Sidebar_Border_Width") + ":", SwingConstants.RIGHT),
                        selectedBorderWidthComBox
                },
                {
                        new UILabel(Toolkit.i18nText("Fine-Design_Mobile_BookMark_Style_Sidebar_Border_Color") + ":", SwingConstants.RIGHT),
                        selectedBorderColorBox
                },
                {
                        new UILabel(Toolkit.i18nText("Fine-Design_Mobile_BookMark_Style_Sidebar_Font") + ":", SwingConstants.RIGHT),
                        selectedFontNameComboBox,
                        fontExtraPane
                }
        }, rowSize, columnSize, 5, 10);
        selectedStateStyleSettingsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        JPanel containerPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        containerPane.setBorder(
                GUICoreUtils.createTitledBorder(Toolkit.i18nText("Fine-Design_Mobile_BookMark_Style_Sidebar_Selected_Style"),
                        Color.decode("#2F8EF1"))
        );
        containerPane.add(selectedStateStyleSettingsPanel);

        return containerPane;
    }

    @Override
    public void populateBean(MobileBookMarkStyle ob) {
        SidebarMobileBookMarkStyle style = null;
        if (ob instanceof SidebarMobileBookMarkStyle) {
            style = (SidebarMobileBookMarkStyle) ob;
        }
        if (style == null) {
            return;
        }

        buttonWidthSpinner.setValue(style.getWidth());
        buttonHeightSpinner.setValue(style.getHeight());
        buttonGapSpinner.setValue(style.getGap());
        buttonBorderRadiusSpinner.setValue(style.getBorderRadius());

        normalBackgroundColorBox.setSelectObject(style.getBackgroundColor());
        normalOpacityDragBar.setValue(style.getOpacity());
        normalOpacitySpinner.setValue(style.getOpacity());
        normalBorderWidthComBox.setSelectedLineStyle(style.getBorderLineStyle());
        normalBorderColorBox.setSelectObject(style.getBorderColor());
        normalFontNameComboBox.setSelectedItem(style.getFontFamily());
        normalFontSizeComboBox.setSelectedItem(style.getFontSize());
        normalFontColorButton.setColor(style.getFontColor());
        normalFontItalicButton.setSelected(style.isFontItalic());
        normalFontBoldButton.setSelected(style.isFontBold());

        selectedBackgroundColorBox.setSelectObject(style.getSelectedBackgroundColor());
        selectedOpacityDragBar.setValue(style.getSelectedOpacity());
        selectedOpacitySpinner.setValue(style.getSelectedOpacity());
        selectedBorderWidthComBox.setSelectedLineStyle(style.getSelectedBorderLineStyle());
        selectedBorderColorBox.setSelectObject(style.getSelectedBorderColor());
        selectedFontNameComboBox.setSelectedItem(style.getSelectedFontFamily());
        selectedFontSizeComboBox.setSelectedItem(style.getSelectedFontSize());
        selectedFontColorButton.setColor(style.getSelectedFontColor());
        selectedFontItalicButton.setSelected(style.isSelectedFontItalic());
        selectedFontBoldButton.setSelected(style.isSelectedFontBold());
    }

    @Override
    public MobileBookMarkStyle updateBean() {
        SidebarMobileBookMarkStyle style = new SidebarMobileBookMarkStyle();

        style.setWidth((int) buttonWidthSpinner.getValue());
        style.setHeight((int) buttonHeightSpinner.getValue());
        style.setGap((int) buttonGapSpinner.getValue());
        style.setBorderRadius((int) buttonBorderRadiusSpinner.getValue());

        if (normalBackgroundColorBox.getSelectObject() != null) {
            style.setBackgroundColor(normalBackgroundColorBox.getSelectObject());
        }
        style.setOpacity((int) normalOpacitySpinner.getValue());
        style.setBorderLineStyle(normalBorderWidthComBox.getSelectedLineStyle());
        style.setBorderColor(normalBorderColorBox.getSelectObject());
        if (normalFontNameComboBox.getSelectedItem() != null) {
            style.setFontFamily((String) normalFontNameComboBox.getSelectedItem());
        }
        if (normalFontSizeComboBox.getSelectedItem() != null) {
            style.setFontSize((Integer) normalFontSizeComboBox.getSelectedItem());
        }
        style.setFontColor(normalFontColorButton.getColor());
        style.setFontItalic(normalFontItalicButton.isSelected());
        style.setFontBold(normalFontBoldButton.isSelected());

        if (selectedBackgroundColorBox.getSelectObject() != null) {
            style.setSelectedBackgroundColor(selectedBackgroundColorBox.getSelectObject());
        }
        style.setSelectedOpacity((int) selectedOpacitySpinner.getValue());
        style.setSelectedBorderLineStyle(selectedBorderWidthComBox.getSelectedLineStyle());
        style.setSelectedBorderColor(selectedBorderColorBox.getSelectObject());
        if (selectedFontNameComboBox.getSelectedItem() != null) {
            style.setSelectedFontFamily((String) selectedFontNameComboBox.getSelectedItem());
        }
        if (selectedFontSizeComboBox.getSelectedItem() != null) {
            style.setSelectedFontSize((Integer) selectedFontSizeComboBox.getSelectedItem());
        }
        style.setSelectedFontColor(selectedFontColorButton.getColor());
        style.setSelectedFontItalic(selectedFontItalicButton.isSelected());
        style.setSelectedFontBold(selectedFontBoldButton.isSelected());

        return style;
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }
}
