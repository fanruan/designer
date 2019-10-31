/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.xpane;

import com.fr.base.BaseUtils;
import com.fr.base.GraphHelper;
import com.fr.base.Utils;
import com.fr.base.background.GradientBackground;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ibutton.UIButtonUI;
import com.fr.design.gui.ibutton.UIColorButton;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.style.BackgroundNoImagePane;
import com.fr.design.gui.style.BackgroundSpecialPane;
import com.fr.design.gui.style.FRFontPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.layout.VerticalFlowLayout;
import com.fr.design.mainframe.JForm;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.form.ui.WidgetTitle;
import com.fr.general.Background;
import com.fr.general.FRFont;
import com.fr.general.act.TitlePacker;
import com.fr.stable.Constants;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * WLayoutBorder Pane.
 */
public class LayoutBorderPane extends BasicPane {

    //圆角 7.1.1暂时不做
    private static final int NO_BORDERS = 0;
    private static final int RIGHTANGLE_BORDERS = 1;
    private static final int ROUNDED_BORDERS = 2;
    private static final int MAX_WIDTH = 220;
    private static final int NO_RADIUS = 0;

    private LayoutBorderStyle borderStyle = new LayoutBorderStyle();

    private LayoutBorderPreviewPane layoutBorderPreviewPane;
    //边框类型
    private UIComboBox borderTypeCombo;
    //渲染风格
    private UIComboBox borderStyleCombo;
    //边框粗细
    private LineComboBox currentLineCombo;
    //边框圆角
    private UISpinner borderCornerSpinner;
    //边框颜色
    private UIColorButton currentLineColorPane;
    //主体背景
    private BackgroundSpecialPane backgroundPane;
    //透明度
    private UINumberDragPane numberDragPane;
    //标题内容
    private TinyFormulaPane formulaPane;
    //标题格式
    private UIComboBox fontNameComboBox;
    private UIComboBox fontSizeComboBox;
    private UIColorButton colorSelectPane;
    private UIToggleButton bold;
    private UIToggleButton italic;
    private UIToggleButton underline;
    private LineComboBox underlineCombo;
    //对齐方式
    private UIButtonGroup hAlignmentPane;
    //标题背景
    private BackgroundNoImagePane titleBackgroundPane;

    private UIScrollPane titlePane;

    private int minNumber = 0;
    private double maxNumber = 100;
    private int iconWidth = 32;

    public LayoutBorderStyle getBorderStyle() {
        return borderStyle;
    }

    public void setBorderStyle(LayoutBorderStyle borderStyle) {
        this.borderStyle = borderStyle;
    }

    public LayoutBorderPreviewPane getLayoutBorderPreviewPane() {
        return layoutBorderPreviewPane;
    }

    public void setLayoutBorderPreviewPane(
            LayoutBorderPreviewPane layoutBorderPreviewPane) {
        this.layoutBorderPreviewPane = layoutBorderPreviewPane;
    }

    public UIComboBox getBorderTypeCombo() {
        return borderTypeCombo;
    }

    public void setBorderTypeCombo(UIComboBox borderTypeCombo) {
        this.borderTypeCombo = borderTypeCombo;
    }

    public UIComboBox getBorderStyleCombo() {
        return borderStyleCombo;
    }

    public void setBorderStyleCombo(UIComboBox borderStyleCombo) {
        this.borderStyleCombo = borderStyleCombo;
    }

    public LineComboBox getCurrentLineCombo() {
        return currentLineCombo;
    }

    public void setCurrentLineCombo(LineComboBox currentLineCombo) {
        this.currentLineCombo = currentLineCombo;
    }


    public UISpinner getBorderCornerSpinner() {
        return borderCornerSpinner;
    }

    public void setBorderCornerSpinner(UISpinner borderCornerSpinner) {
        this.borderCornerSpinner = borderCornerSpinner;
    }


    public UIColorButton getCurrentLineColorPane() {
        return currentLineColorPane;
    }

    public void setCurrentLineColorPane(UIColorButton currentLineColorPane) {
        this.currentLineColorPane = currentLineColorPane;
    }


    public BackgroundSpecialPane getBackgroundPane() {
        return backgroundPane;
    }

    public void setBackgroundPane(BackgroundSpecialPane backgroundPane) {
        this.backgroundPane = backgroundPane;
    }

    public UINumberDragPane getNumberDragPane() {
        return numberDragPane;
    }

    public void setNumberDragPane(UINumberDragPane numberDragPane) {
        this.numberDragPane = numberDragPane;
    }

    public TinyFormulaPane getFormulaPane() {
        return formulaPane;
    }

    public void setFormulaPane(TinyFormulaPane formulaPane) {
        this.formulaPane = formulaPane;
    }

    public UIComboBox getFontNameComboBox() {
        return fontNameComboBox;
    }

    public void setFontNameComboBox(UIComboBox fontNameComboBox) {
        this.fontNameComboBox = fontNameComboBox;
    }

    public UIComboBox getFontSizeComboBox() {
        return fontSizeComboBox;
    }

    public void setFontSizeComboBox(UIComboBox fontSizeComboBox) {
        this.fontSizeComboBox = fontSizeComboBox;
    }

    public UIColorButton getColorSelectPane() {
        return colorSelectPane;
    }

    public void setColorSelectPane(UIColorButton colorSelectPane) {
        this.colorSelectPane = colorSelectPane;
    }

    public UIToggleButton getBold() {
        return bold;
    }

    public void setBold(UIToggleButton bold) {
        this.bold = bold;
    }

    public UIToggleButton getItalic() {
        return italic;
    }

    public void setItalic(UIToggleButton italic) {
        this.italic = italic;
    }

    public UIToggleButton getUnderline() {
        return underline;
    }

    public void setUnderline(UIToggleButton underline) {
        this.underline = underline;
    }

    public LineComboBox getUnderlineCombo() {
        return underlineCombo;
    }

    public void setUnderlineCombo(LineComboBox underlineCombo) {
        this.underlineCombo = underlineCombo;
    }

    public UIButtonGroup gethAlignmentPane() {
        return hAlignmentPane;
    }

    public void sethAlignmentPane(UIButtonGroup hAlignmentPane) {
        this.hAlignmentPane = hAlignmentPane;
    }

    public BackgroundNoImagePane getTitleBackgroundPane() {
        return titleBackgroundPane;
    }

    public void setTitleBackgroundPane(BackgroundNoImagePane titleBackgroundPane) {
        this.titleBackgroundPane = titleBackgroundPane;
    }

    public UIScrollPane getTitlePane() {
        return titlePane;
    }

    public void setTitlePane(UIScrollPane titlePane) {
        this.titlePane = titlePane;
    }

    public int getMinNumber() {
        return minNumber;
    }

    public void setMinNumber(int minNumber) {
        this.minNumber = minNumber;
    }

    public double getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(double maxNumber) {
        this.maxNumber = maxNumber;
    }

    public int getIconWidth() {
        return iconWidth;
    }

    public void setIconWidth(int iconWidth) {
        this.iconWidth = iconWidth;
    }

    public final static int[] BORDER_LINE_STYLE_ARRAY = new int[]{
            Constants.LINE_NONE,
            Constants.LINE_THIN, //1px
            Constants.LINE_MEDIUM, //2px
            Constants.LINE_THICK, //3px
    };

    public final static String[] BORDER_TYPE = new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Style_Standard"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Style_Custom")};
    public final static String[] BORDER_STYLE= new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Style_Common"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Style_Shadow")};

    private final static Dimension BUTTON_SIZE = new Dimension(24, 20);

    public LayoutBorderPane() {
        this.initComponents();
    }

    protected void initComponents() {
        this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        JPanel defaultPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(defaultPane, BorderLayout.CENTER);

        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        defaultPane.add(centerPane, BorderLayout.CENTER);
        centerPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Style_Preview"), null));

        JPanel borderPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        centerPane.add(borderPane, BorderLayout.CENTER);
        borderPane.setBorder(BorderFactory.createEmptyBorder(10, 4, 10, 4));

        layoutBorderPreviewPane = new LayoutBorderPreviewPane(borderStyle);

        borderPane.add(layoutBorderPreviewPane, BorderLayout.CENTER);

        JPanel rightPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        defaultPane.add(rightPane, BorderLayout.EAST);
        rightPane.add(initRightBottomPane(), BorderLayout.CENTER);
        JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if (!jTemplate.isJWorkBook() && ((JForm)jTemplate).isSelectRootPane()){
            //界面上表单主体只有背景和透明度可以设置
            rightPane.add(initBodyRightTopPane(), BorderLayout.NORTH);
        } else {
            rightPane.add(initRightTopPane(), BorderLayout.NORTH);
        }


    }

    protected UIScrollPane initRightTopPane(){
        switchBorderType();
        this.borderStyleCombo = new UIComboBox(BORDER_STYLE);
        this.currentLineCombo = new LineComboBox(BORDER_LINE_STYLE_ARRAY);
        this.currentLineColorPane = new UIColorButton(null);
        this.borderCornerSpinner = new UISpinner(0,1000,1,0);
        currentLineColorPane.setUI(getButtonUI(currentLineColorPane));
        currentLineColorPane.set4ToolbarButton();
        currentLineColorPane.setPreferredSize(new Dimension(20,20));
        JPanel buttonPane = new JPanel(new BorderLayout());
        buttonPane.add(currentLineColorPane,BorderLayout.WEST);
        backgroundPane = new BackgroundSpecialPane();
        JPanel transparencyPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.numberDragPane = new UINumberDragPane(0,100);
        transparencyPane.add(numberDragPane, BorderLayout.CENTER);
        transparencyPane.add(new UILabel(" %"), BorderLayout.EAST);

        double p = TableLayout.PREFERRED;
        double[] rowSize = {p,p,p,p,p,p,p,p};
        double[] columnSize = { p, MAX_WIDTH};
        JPanel rightTopContentPane = TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][]{
                getBorderTypeComp(),
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Style_Render_Style")), borderStyleCombo},
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Style_Border_Line")), currentLineCombo},
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Style_Border_Color")), buttonPane},
                getBorderCornerSpinnerComp(),
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget-Style_Body_Background")), backgroundPane},
                {new UILabel(""),new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget-Style_Alpha"))},
                {new UILabel(""),transparencyPane},
        }, rowSize, columnSize, 10);
        rightTopContentPane.setBorder(BorderFactory.createEmptyBorder(15, 12, 10, 12));
        UIScrollPane rightTopPane = new UIScrollPane(rightTopContentPane);
        rightTopPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Style_Frame"),null));
        rightTopPane.setPreferredSize(rightTopPane.getPreferredSize());
        return  rightTopPane;
    }

    protected JComponent[] getBorderTypeComp(){
        return new JComponent[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Style_Frame_Style")), borderTypeCombo};
    }

    protected JComponent[] getBorderCornerSpinnerComp(){
        return new JComponent[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Radius")+":"),borderCornerSpinner};
    }

    protected void switchBorderType(){
        this.borderTypeCombo = new UIComboBox(BORDER_TYPE);
        this.borderTypeCombo.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if(borderTypeCombo.getSelectedIndex() == 0){
                    titlePane.setVisible(false);
                }  else {
                    titlePane.setVisible(true);
                    currentLineCombo.setSelectedItem(Constants.LINE_THIN);
                }
            }
        });
    }



    protected JPanel initBodyRightTopPane(){
        this.borderTypeCombo = new UIComboBox(BORDER_TYPE);
        this.borderStyleCombo = new UIComboBox(BORDER_STYLE);
        this.borderCornerSpinner = new UISpinner(0,1000,1,0);
        this.currentLineCombo = new LineComboBox(BORDER_LINE_STYLE_ARRAY);
        this.currentLineColorPane = new UIColorButton(null);

        backgroundPane = new BackgroundSpecialPane();
        JPanel transparencyPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.numberDragPane = new UINumberDragPane(0,100);
        transparencyPane.add(numberDragPane, BorderLayout.CENTER);
        transparencyPane.add(new UILabel(" %"), BorderLayout.EAST);

        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p,p,p};
        double[] columnSize = { p, f};
        JPanel rightTopContentPane = TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][]{
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget-Style_Body_Background")), backgroundPane},
                {new UILabel(""),new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget-Style_Alpha"))},
                {new UILabel(""),transparencyPane},
        }, rowSize, columnSize, 10);
        rightTopContentPane.setBorder(BorderFactory.createEmptyBorder(15, 12, 10, 6));
        JPanel bodyRightTopPane =FRGUIPaneFactory.createBorderLayout_S_Pane();
        bodyRightTopPane.add(rightTopContentPane,BorderLayout.CENTER);
        bodyRightTopPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Style_Frame"),null));
        return  bodyRightTopPane;
    }

    protected UIScrollPane initRightBottomPane(){
        formulaPane = new TinyFormulaPane();
        fontSizeComboBox = new UIComboBox(FRFontPane.FONT_SIZES);
        fontNameComboBox = new UIComboBox(Utils.getAvailableFontFamilyNames4Report());
        fontNameComboBox.setPreferredSize(new Dimension(160, 30));
        JPanel fontSizeTypePane = new JPanel(new BorderLayout(10,0));
        fontSizeTypePane.add(fontSizeComboBox, BorderLayout.CENTER);
        fontSizeTypePane.add(fontNameComboBox, BorderLayout.EAST);

        Icon[] hAlignmentIconArray = {BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_left_normal.png"),
                BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_center_normal.png"),
                BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_right_normal.png"),};
        Integer[] hAlignment = new Integer[]{Constants.LEFT, Constants.CENTER, Constants.RIGHT};
        hAlignmentPane = new UIButtonGroup<Integer>(hAlignmentIconArray, hAlignment);
        hAlignmentPane.setAllToolTips(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_StyleAlignment_Left")
                , com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_StyleAlignment_Center"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_StyleAlignment_Right")});
        JPanel hPaneContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        hPaneContainer.add(hAlignmentPane);

        titleBackgroundPane = new BackgroundNoImagePane();

        double p = TableLayout.PREFERRED;
        double[] rowSize = {p,p,p,p,p,p};
        double[] columnSize = { p, MAX_WIDTH};

        JPanel rightBottomContentPane = TableLayoutHelper.createCommonTableLayoutPane( new JComponent[][]{
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Style_Title_Content")), formulaPane},
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Style_Title_Format")), fontSizeTypePane},
                {new UILabel(""), initFontButtonPane()},
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Alignment-Style")), hPaneContainer},
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Style_Title_Background")), titleBackgroundPane},
        }, rowSize, columnSize, 10);
        rightBottomContentPane.setBorder(BorderFactory.createEmptyBorder(15, 12, 10, 12));
        titlePane =new UIScrollPane(rightBottomContentPane);
        titlePane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Style_Title"),null));
        titlePane.setVisible(false);
        return titlePane;
    }



    protected JPanel initFontButtonPane(){
        colorSelectPane = new UIColorButton();
        bold = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/bold.png"));
        italic = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/italic.png"));
        underline = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/underline.png"));
        bold.setPreferredSize(BUTTON_SIZE);
        italic.setPreferredSize(BUTTON_SIZE);
        underline.setPreferredSize(BUTTON_SIZE);
        underline.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                underlineCombo.setVisible(underline.isSelected());
            }
        });
        underlineCombo = new LineComboBox(UIConstants.BORDER_LINE_STYLE_ARRAY);
        Component[] components_font = new Component[]{
                colorSelectPane, italic, bold, underline
        };
        JPanel buttonPane = new JPanel(new BorderLayout());
        buttonPane.add(GUICoreUtils.createFlowPane(components_font, FlowLayout.LEFT, LayoutConstants.HGAP_SMALL));
        JPanel combinePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        combinePane.add(buttonPane, BorderLayout.WEST);
        combinePane.add(underlineCombo, BorderLayout.CENTER);
        initAllNames();
        setToolTips();
        return combinePane;

    }

    protected void initAllNames() {
        fontNameComboBox.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_FRFont_Family"));
        fontSizeComboBox.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_FRFont_Size"));
        colorSelectPane.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_FRFont_Foreground"));
        italic.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_FRFont_Italic"));
        bold.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_FRFont_Bold"));
        underline.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_FRFont_Underline"));
        underlineCombo.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_FRFont_Line_Style"));
    }

    protected void setToolTips() {
        colorSelectPane.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_FRFont_Foreground"));
        italic.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_FRFont_Italic"));
        bold.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_FRFont_Bold"));
        underline.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_FRFont_Underline"));
    }


    public LayoutBorderStyle update() {
        LayoutBorderStyle style = new LayoutBorderStyle();
        style.setType(borderTypeCombo.getSelectedIndex());
        style.setBorderStyle(borderStyleCombo.getSelectedIndex());
        style.setBorderRadius((int)borderCornerSpinner.getValue());
        style.setBorder(currentLineCombo.getSelectedLineStyle());
        style.setColor(currentLineColorPane.getColor());
        style.setBackground(backgroundPane.update());
        style.setAlpha((float)(numberDragPane.updateBean()/maxNumber));
        TitlePacker title = style.getTitle() == null ? new WidgetTitle() : style.getTitle();
        title.setTextObject(formulaPane.updateBean());
        FRFont frFont = title.getFrFont();
        frFont = frFont.applySize((Integer)fontSizeComboBox.getSelectedItem());
        frFont = frFont.applyName(fontNameComboBox.getSelectedItem().toString());
        frFont = frFont.applyForeground(colorSelectPane.getColor());
        frFont = updateItalicBold(frFont);
        int line = underline.isSelected() ? this.underlineCombo.getSelectedLineStyle() : Constants.LINE_NONE;
        frFont = frFont.applyUnderline(line);
        title.setFrFont(frFont);
        title.setPosition((Integer)hAlignmentPane.getSelectedItem());
        title.setBackground(titleBackgroundPane.update());
        style.setTitle(title);
        return style;
    }

    protected FRFont updateItalicBold(FRFont frFont) {
        int italic_bold = frFont.getStyle();
        boolean isItalic = italic_bold == Font.ITALIC || italic_bold == (Font.BOLD + Font.ITALIC);
        boolean isBold = italic_bold == Font.BOLD || italic_bold == (Font.BOLD + Font.ITALIC);
        if (italic.isSelected() && !isItalic) {
            italic_bold += Font.ITALIC;
        } else if (!italic.isSelected() && isItalic) {
            italic_bold -= Font.ITALIC;
        }
        frFont = frFont.applyStyle(italic_bold);
        if (bold.isSelected() && !isBold) {
            italic_bold += Font.BOLD;
        } else if (!bold.isSelected() && isBold) {
            italic_bold -= Font.BOLD;
        }
        frFont = frFont.applyStyle(italic_bold);
        return frFont;
    }

    public void populate(LayoutBorderStyle style) {
        if(this.borderStyle == null) {
            borderStyle = new LayoutBorderStyle();
        }
        this.borderStyle.setStyle(style);

        populateBorder();

        populateTitle();

    }

    protected void populateBorderType(){
        this.borderTypeCombo.setSelectedIndex(borderStyle.getType());
        this.borderTypeCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                paintPreviewPane();
            }
        });
    }

    protected void populateBorder(){
        populateBorderType();
        this.borderStyleCombo.setSelectedIndex(borderStyle.getBorderStyle());
        this.borderStyleCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                paintPreviewPane();
            }
        });
        this.borderCornerSpinner.setValue(borderStyle.getBorderRadius());
        this.borderCornerSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                paintPreviewPane();
            }
        });
        this.currentLineCombo.setSelectedLineStyle(borderStyle.getBorder());
        this.currentLineCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                paintPreviewPane();
            }
        });
        this.currentLineColorPane.setColor(borderStyle.getColor());
        currentLineColorPane.addColorChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                paintPreviewPane();
            }
        });
        this.backgroundPane.populateBean(borderStyle.getBackground());
        this.backgroundPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                paintPreviewPane();
            }
        });
        numberDragPane.populateBean(borderStyle.getAlpha() * maxNumber);
    }

    protected void populateTitle(){
        TitlePacker widgetTitle = borderStyle == null ? new WidgetTitle() : borderStyle.getTitle();
        widgetTitle = widgetTitle == null ? new WidgetTitle() : widgetTitle;

        populateFormula(widgetTitle);
        populateFont(widgetTitle);

        underline.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                paintPreviewPane();
            }
        });
        this.underlineCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                paintPreviewPane();
            }
        });

        hAlignmentPane.setSelectedItem(widgetTitle.getPosition());
        hAlignmentPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                paintPreviewPane();
            }
        });



        titleBackgroundPane.populateBean(widgetTitle.getBackground());
        this.titleBackgroundPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                paintPreviewPane();
            }
        });
        paintPreviewPane();
    }

    protected void populateFont(TitlePacker widgetTitle){
        FRFont frFont = widgetTitle.getFrFont();
        this.fontSizeComboBox.setSelectedItem(frFont.getSize());
        this.fontSizeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                paintPreviewPane();
            }
        });
        this.fontNameComboBox.setSelectedItem(frFont.getFamily());
        this.fontNameComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                paintPreviewPane();
            }
        });
        this.colorSelectPane.setColor(frFont.getForeground());
        colorSelectPane.addColorChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                paintPreviewPane();
            }
        });
        this.colorSelectPane.repaint();
        bold.setSelected(frFont.isBold());
        bold.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                paintPreviewPane();
            }
        });
        italic.setSelected(frFont.isItalic());
        italic.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                paintPreviewPane();
            }
        });

        int line = frFont.getUnderline();
        if (line == Constants.LINE_NONE) {
            underline.setSelected(false);
            underlineCombo.setVisible(false);
        } else {
            underline.setSelected(true);
            underlineCombo.setVisible(true);
            this.underlineCombo.setSelectedLineStyle(line);
        }
    }

    protected void paintPreviewPane(){
        layoutBorderPreviewPane.repaint(update());
    }

    protected UIButtonUI getButtonUI(final UIColorButton uiColorButton) {
        return new UIButtonUI() {

            public void paint(Graphics g, JComponent c) {
                UIButton b = (UIButton) c;
                g.setColor(Color.black);
                GraphHelper.draw(g, new RoundRectangle2D.Double(1, 1, b.getWidth() - 2, b.getHeight() - 2, 0, 0), 1);

                if (b.getModel().isEnabled()) {
                    g.setColor(uiColorButton.getColor());
                } else {
                    g.setColor(new Color(Utils.filterRGB(uiColorButton.getColor().getRGB(), 50)));
                }
                g.fillRect(2, 2, b.getWidth() - 3, b.getHeight() - 3);
            }
        };
    }


    private void populateFormula(TitlePacker widgetTitle) {
        this.formulaPane.populateBean(widgetTitle.getTextObject().toString());
        this.formulaPane.getUITextField().getDocument()
                .addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        paintPreviewPane();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        paintPreviewPane();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        paintPreviewPane();
                    }
                });
    }


    protected JPanel createVerButtonPane(JToggleButton noBorder, String text) {
        JPanel verPane = new JPanel();
        verPane.setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER, 2, 2));
        verPane.add(noBorder);
        verPane.add(new UILabel(text));

        return verPane;
    }

    protected class VerButtonPane extends JPanel {

        JToggleButton noBorder;
        BorderButton normalBorder;
        BorderButton RoundedBorder;
        ButtonGroup group;

        private VerButtonPane () {
            setLayout(new FlowLayout(FlowLayout.CENTER));
            setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
            group = new ButtonGroup();
            noBorder = new BorderButton(NO_BORDERS);
            normalBorder = new BorderButton(RIGHTANGLE_BORDERS);
            RoundedBorder = new BorderButton(ROUNDED_BORDERS);
            group.add(noBorder);
            group.add(normalBorder);
            group.add(RoundedBorder);

//    		add(createVerButtonPane(noBorder, com.fr.design.i18n.Toolkit.i18nText("None")));
//    		add(createVerButtonPane(normalBorder, com.fr.design.i18n.Toolkit.i18nText("Border-Style-Normal")));
//    		add(createVerButtonPane(RoundedBorder, com.fr.design.i18n.Toolkit.i18nText("Border-Style-Radius")));
        }

        public void populate(LayoutBorderStyle style) {
            if(style.getBorder() == Constants.LINE_NONE) {
                group.setSelected(noBorder.getModel(), true);
            } else if(style.getBorderRadius() != NO_RADIUS) {
                group.setSelected(RoundedBorder.getModel(), true);
            }  else {
                group.setSelected(normalBorder.getModel(), true);
            }
        }
    }

    private class BorderButton extends JToggleButton {
        private BorderButton(int border) {
            super();
            this.setIcon(new BorderButtonIcon(border));
            addBorderActionListener(border);
            setPreferredSize(new Dimension(32, 32));
            this.setBorder(new UIRoundedBorder(new Color(220, 220, 220), 1, 5));
            this.setRolloverEnabled(true);
        }

        @Override
        public Border getBorder() {
            ButtonModel model = getModel();
            if (this.isSelected()) {
                return null;
            }
            if (isRolloverEnabled() && model.isRollover()) {
                return new UIRoundedBorder(new Color(148, 148, 148), 1, 5);
            }
            return super.getBorder();
        }

        @Override
        public void updateUI() {
            setUI(new BorderToggleButtonUI());
        }

        private void addBorderActionListener(final int border) {
            addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    if (border == 0) {
                        borderStyle.reset();
                    } else {
                        borderStyle.setColor(currentLineColorPane.getColor());
                        borderStyle.setBorder(currentLineCombo.getSelectedLineStyle());
                        borderStyle.setBorderRadius((int)borderCornerSpinner.getValue());
                    }

                    layoutBorderPreviewPane.repaint();
                }
            });

        }
    }

    private class BorderToggleButtonUI extends BasicToggleButtonUI {

        @Override
        public void paint(Graphics g, JComponent c) {
            paintBackground(g, (AbstractButton) c);
            super.paint(g, c);
        }

        private void paintBackground(Graphics g, AbstractButton b) {
            if (b.isContentAreaFilled()) {
                Dimension size = b.getSize();
                Background background = new GradientBackground(new Color(247, 247, 247), new Color(228, 228, 228),
                        GradientBackground.TOP2BOTTOM);
                background.paint(g, new RoundRectangle2D.Double(2, 2, size.width - 4, size.height - 4, 5, 5));
            }
        }

        private void paintBorder(Graphics g, Color lineColor, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(lineColor);
            g2d.drawRoundRect(0, 0, width - 1, height - 1, 5, 5);
            g2d.setColor(Color.WHITE);
            g2d.drawRoundRect(1, 1, width - 3, height - 3, 5, 5);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }

        @Override
        protected void paintButtonPressed(Graphics g, AbstractButton b) {
            Dimension size = b.getSize();
            paintBorder(g, new Color(78,143,203), size.height, size.width);
        }
    }

    // Icon to display in shortcut setting buttons
    private class BorderButtonIcon implements Icon {
        private int display;

        private BorderButtonIcon(int display) {
            this.display = display;
        }

        public int getIconWidth() {
            return iconWidth;
        }

        public int getIconHeight() {
            return iconWidth;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            if (display == LayoutBorderPane.RIGHTANGLE_BORDERS) {
                g.setColor(Color.black);
                g.drawRect(3, 3, this.getIconWidth() - 7, this.getIconHeight() - 7);
            } else if (display == LayoutBorderPane.ROUNDED_BORDERS) {
                g.setColor(Color.black);
                g.drawRoundRect(3, 3, this.getIconWidth() - 7, this.getIconHeight() - 7, 6, 6);
            }
        }
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Style");
    }
}
