package com.fr.design.gui.style;

/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.fun.IndentationUnitProcessor;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.UIComponentUtils;
import com.fr.event.EventDispatcher;
import com.fr.general.ComparatorUtils;
import com.fr.general.IOUtils;
import com.fr.plugin.ExtraClassManager;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.manage.PluginFilter;
import com.fr.plugin.observer.PluginEvent;
import com.fr.plugin.observer.PluginEventListener;
import com.fr.plugin.observer.PluginEventType;
import com.fr.report.fun.VerticalTextProcessor;
import com.fr.report.fun.impl.DefaultVerticalTextProcessor;
import com.fr.stable.Constants;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Pane to edit cell alignment.
 */
public class AlignmentPane extends AbstractBasicStylePane implements GlobalNameObserver {
    private static final int ANGEL = 90;
    private static final int GAP = 23;
    private static final int VERGAP = 3;
    private static final Dimension SPINNER_DIMENSION = new Dimension(75, 20);
    private static final String[] TEXT = {com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Wrap_Text"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Single_Line"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_StyleAlignment_Single_Line(Adjust_Font)"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_StyleAlignment_Multi_Line(Adjust_Font)")};

    private static final String[] LAYOUT = {com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Layout_Default"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Layout_Image_Titled"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Layout_Image_Extend"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Layout_Image_Adjust")};

    private JPanel hPaneContainer;
    private JPanel vPaneContainer;
    private JPanel rotationBarCC;
    private JPanel basicPane;
    private JPanel seniorPane;

    private UIComboBox textComboBox;
    private UIComboBox textRotationComboBox;
    private UIComboBox imageLayoutComboBox;

    private UIButtonGroup<Integer> hAlignmentPane;// 左对齐 水平居中 右对齐 水平分散
    private UIButtonGroup<Integer> vAlignmentPane;// 居上 垂直居中 居下

    private UINumberDragPane rotationPane;

    private UISpinner leftIndentSpinner;
    private UISpinner rightIndentSpinner;

    private UISpinner spaceBeforeSpinner;
    private UISpinner spaceAfterSpinner;
    private UISpinner lineSpaceSpinner;
    private GlobalNameListener globalNameListener = null;
    private IndentationUnitProcessor indentationUnitProcessor = null;

    public AlignmentPane() {
        this.initComponents();
    }


    protected void initComponents() {
        textComboBox = new UIComboBox(TEXT);
        imageLayoutComboBox = new UIComboBox(LAYOUT);
        initTextRotationCombox();

        Icon[][] hAlignmentIconArray = {{IOUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_left_normal.png"), IOUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_left_normal_white.png")},
                {IOUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_center_normal.png"), IOUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_center_normal_white.png")},
                {IOUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_right_normal.png"), IOUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_right_normal_white.png")},
                {IOUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_s_normal.png"), IOUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_s_normal_white.png")},
                {IOUtils.readIcon("/com/fr/design/images/m_format/cellstyle/defaultAlignment.png"), IOUtils.readIcon("/com/fr/design/images/m_format/cellstyle/defaultAlignment_white.png")}};
        Integer[] hAlignment = new Integer[]{Constants.LEFT, Constants.CENTER, Constants.RIGHT, Integer.valueOf(Constants.DISTRIBUTED), Constants.NULL};
        hAlignmentPane = new UIButtonGroup<>(hAlignmentIconArray, hAlignment);
        hAlignmentPane.setAllToolTips(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Tooltips_Left"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Tooltips_Center"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Tooltips_Right"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Tooltips_Distributed"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Tooltips_DEFAULT")});
        hPaneContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        vPaneContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        Icon[][] vAlignmentIconArray = {{IOUtils.readIcon("/com/fr/design/images/m_format/cellstyle/v_top_normal.png"), IOUtils.readIcon("/com/fr/design/images/m_format/cellstyle/v_top_normal_white.png")},
                {IOUtils.readIcon("/com/fr/design/images/m_format/cellstyle/v_center_normal.png"), IOUtils.readIcon("/com/fr/design/images/m_format/cellstyle/v_center_normal_white.png")},
                {IOUtils.readIcon("/com/fr/design/images/m_format/cellstyle/v_down_normal.png"), IOUtils.readIcon("/com/fr/design/images/m_format/cellstyle/v_down_normal_white.png")}};
        Integer[] vAlignment = new Integer[]{Constants.TOP, Constants.CENTER, Constants.BOTTOM};
        vAlignmentPane = new UIButtonGroup<>(vAlignmentIconArray, vAlignment);
        vAlignmentPane.setAllToolTips(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Tooltips_Top"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Tooltips_Center"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Tooltips_Bottom")});
        initOtherComponent();
        initAllNames();

        addPluginListeners(PluginEventType.AfterRun);
        addPluginListeners(PluginEventType.AfterStop);
        refreshIndentationUnit();
    }

    private void addPluginListeners(PluginEventType type) {
        EventDispatcher.listen(type, new PluginEventListener() {

            @Override
            public void on(PluginEvent event) {
                refreshIndentationUnit();
            }
        }, new PluginFilter() {

            @Override
            public boolean accept(PluginContext context) {
                return context.contain(IndentationUnitProcessor.MARK_STRING);
            }
        });
    }
    private void refreshIndentationUnit() {
        this.indentationUnitProcessor = ExtraDesignClassManager.getInstance().getSingle(IndentationUnitProcessor.MARK_STRING);
        if (null == this.indentationUnitProcessor) {
            this.indentationUnitProcessor = new DefaultIndentationUnitProcessor();
        }
    }

    private void initOtherComponent() {
        hPaneContainer.add(hAlignmentPane);
        vPaneContainer.add(vAlignmentPane);
        rotationPane = new UINumberDragPane(-ANGEL, ANGEL);

        leftIndentSpinner = new UISpinner(0, Integer.MAX_VALUE, 1, 0);
        rightIndentSpinner = new UISpinner(0, Integer.MAX_VALUE, 1, 0);

        spaceBeforeSpinner = new UISpinner(0, Integer.MAX_VALUE, 1, 0);
        spaceAfterSpinner = new UISpinner(0, Integer.MAX_VALUE, 1, 0);
        lineSpaceSpinner = new UISpinner(0, Integer.MAX_VALUE, 1, 0);

        rotationBarCC = new JPanel(new CardLayout());
        rotationBarCC.add(rotationPane, "show");
        rotationBarCC.add(new JPanel(), "hide");

        this.setLayout(new BorderLayout());
        this.add(createPane(), BorderLayout.CENTER);

        textRotationComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                CardLayout cc = (CardLayout) rotationBarCC.getLayout();
                cc.show(rotationBarCC, textRotationComboBox.getSelectedIndex() == 0 ? "show" : "hide");
            }
        });
    }

    private void initTextRotationCombox() {
        ArrayList<String> selectOption = new ArrayList<>();
        selectOption.add(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Custom_Angle"));
        VerticalTextProcessor processor = ExtraClassManager.getInstance().getSingle(VerticalTextProcessor.XML_TAG, DefaultVerticalTextProcessor.class);
        selectOption.addAll(Arrays.asList(processor.getComboxOption()));

        textRotationComboBox = new UIComboBox(selectOption.toArray(new String[selectOption.size()]));
    }

    private void initAllNames() {
        hAlignmentPane.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Pane_Horizontal"));
        vAlignmentPane.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Pane_Vertical"));
        imageLayoutComboBox.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Image_Layout"));
        textComboBox.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Text_Style"));
        textRotationComboBox.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_StyleAlignment_Text_Rotation"));
        rotationPane.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_StyleAlignment_Text_Rotation"));
        leftIndentSpinner.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Left_Indent"));
        rightIndentSpinner.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Right_Indent"));
        spaceBeforeSpinner.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Spacing_Before"));
        spaceAfterSpinner.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Spacing_After"));
        lineSpaceSpinner.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Line_Spacing"));
    }

    private JPanel createPane() {
        JPanel jp1 = new JPanel(new BorderLayout());
        basicPane = new JPanel();
        seniorPane = new JPanel();
        basicPane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Basic"), 290, 24, basicPane());
        seniorPane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Advanced"), 290, 24, seniorPane());

        jp1.add(basicPane, BorderLayout.NORTH);
        jp1.add(seniorPane, BorderLayout.CENTER);

        return jp1;
    }

    private JPanel basicPane() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        UILabel horizontalLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Pane_Horizontal") + "   ", SwingConstants.LEFT);
        UIComponentUtils.setLineWrap(horizontalLabel);
        Component[][] components = new Component[][]{
                new Component[]{null, null},
                new Component[]{horizontalLabel, hPaneContainer},
                new Component[]{null, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Pane_Vertical") + "   ", SwingConstants.RIGHT), vPaneContainer},
                new Component[]{null, null}
        };
        double[] rowSize = {p, p, p, p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}};
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM);
    }

    private JPanel seniorPane() {
        JPanel senPane = new JPanel(new BorderLayout());
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel((com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Image_Layout")) + "   ", SwingConstants.LEFT), imageLayoutComboBox},
                new Component[]{null, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Text_Style") + "   ", SwingConstants.LEFT), textComboBox},
                new Component[]{null, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_StyleAlignment_Text_Rotation") + "   ", SwingConstants.LEFT), textRotationComboBox},
                new Component[]{null, rotationBarCC},
                new Component[]{null, null},
        };
        double[] rowSize = {p, p, p, p, p, p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}};
        JPanel tempPane = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM);
        senPane.add(tempPane, BorderLayout.NORTH);
        senPane.add(seniorDownPane(), BorderLayout.CENTER);
        return senPane;

    }

    private JPanel seniorDownPane() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        leftIndentSpinner.setPreferredSize(SPINNER_DIMENSION);
        rightIndentSpinner.setPreferredSize(SPINNER_DIMENSION);
        spaceBeforeSpinner.setPreferredSize(SPINNER_DIMENSION);
        spaceAfterSpinner.setPreferredSize(SPINNER_DIMENSION);
        lineSpaceSpinner.setPreferredSize(SPINNER_DIMENSION);

        JPanel indentationPane = new JPanel(new BorderLayout());
        indentationPane.add(new UILabel((com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Style_Indentation")), SwingConstants.LEFT));
        indentationPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, GAP));
        JPanel partSpacingPane = new JPanel(new BorderLayout());
        partSpacingPane.add(new UILabel((com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Style_Part_Spacing")), SwingConstants.LEFT));
        partSpacingPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, GAP));
        JPanel spacingPane = new JPanel(new BorderLayout());
        spacingPane.add(new UILabel((com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Style_Spacing")), SwingConstants.LEFT));
        spacingPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, GAP));

        Component[][] components = new Component[][]{
                new Component[]{null, null, null},
                new Component[]{indentationPane, creatSpinnerPane(leftIndentSpinner), creatSpinnerPane(rightIndentSpinner)},
                new Component[]{null, new UILabel((com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Left")), SwingConstants.CENTER), new UILabel((com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Right")), SwingConstants.CENTER)},
                new Component[]{null, null, null},
                new Component[]{null, null, null},
                new Component[]{partSpacingPane, creatSpinnerPane(spaceBeforeSpinner), creatSpinnerPane(spaceAfterSpinner)},
                new Component[]{null, new UILabel((com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Front")), SwingConstants.CENTER), new UILabel((com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Behind")), SwingConstants.CENTER)},
                new Component[]{null, null, null},
                new Component[]{null, null, null},
                new Component[]{spacingPane, creatSpinnerPane(lineSpaceSpinner), null},
        };
        double[] rowSize = {p, p, p, p, p, p, p, p, p, p};
        double[] columnSize = {p, f, f};
        int[][] rowCount = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_MEDIUM, VERGAP);
    }

    private JPanel creatSpinnerPane(Component comp) {
        JPanel jp = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        jp.add(comp);
        return jp;
    }

    /**
     * 标题
     *
     * @return 标题
     */
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Style_Alignment");
    }

    /**
     * Populate cellstyle border.
     *
     * @param style the new style.
     */
    @Override
    public void populateBean(Style style) {
        hAlignmentPane.setSelectedItem(BaseUtils.getAlignment4Horizontal(style));
        vAlignmentPane.setSelectedItem(style.getVerticalAlignment());

        if (style.getTextStyle() == Style.TEXTSTYLE_SINGLELINE) {
            this.textComboBox.setSelectedIndex(1);
        } else if (style.getTextStyle() == Style.TEXTSTYLE_SINGLELINEADJUSTFONT) {
            this.textComboBox.setSelectedIndex(2);
        } else if (style.getTextStyle() == Style.TEXTSTYLE_MULTILINEADJUSTFONT) {
            this.textComboBox.setSelectedIndex(3);
        } else {
            this.textComboBox.setSelectedIndex(0);
        }
        if (style.getVerticalText() == Style.VERTICALTEXT) {
            textRotationComboBox.setSelectedIndex(style.getTextDirection() == Style.LEFT_TO_RIGHT ? 1 : 2);
        } else {
            textRotationComboBox.setSelectedIndex(0);
            rotationPane.populateBean((double) style.getRotation());
        }

        if (style.getImageLayout() == Constants.IMAGE_TILED) {
            imageLayoutComboBox.setSelectedIndex(1);
        } else if (style.getImageLayout() == Constants.IMAGE_EXTEND) {
            imageLayoutComboBox.setSelectedIndex(2);
        } else if (style.getImageLayout() == Constants.IMAGE_ADJUST) {
            imageLayoutComboBox.setSelectedIndex(3);
        } else {
            imageLayoutComboBox.setSelectedIndex(0);
        }

        int leftPadding = indentationUnitProcessor.paddingUnitProcessor(style.getPaddingLeft());
        int rightPadding = indentationUnitProcessor.paddingUnitProcessor(style.getPaddingRight());

        // alex:indent
        this.leftIndentSpinner.setValue(leftPadding);
        this.rightIndentSpinner.setValue(rightPadding);

        this.spaceBeforeSpinner.setValue(style.getSpacingBefore());
        this.spaceAfterSpinner.setValue(style.getSpacingAfter());
        this.lineSpaceSpinner.setValue(style.getLineSpacing());

    }

    /**
     * Update cellstyle border
     *
     * @param style the new style.
     */
    public Style update(Style style) {
        // peter:需要判断传递进来的值是否为null.
        if (style == null) {
            return null;
        }

        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Pane_Horizontal"))) {
            Integer h = this.hAlignmentPane.getSelectedItem();
            style = style.deriveHorizontalAlignment(h == null ? -1 : h);
        }
        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Pane_Vertical"))) {
            Integer vAlign = this.vAlignmentPane.getSelectedItem();
            if (vAlign != null) {
                style = style.deriveVerticalAlignment(vAlign);
            }
        }

        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Text_Style"))) {
            if (ComparatorUtils.equals(this.textComboBox.getSelectedItem(), TEXT[0])) {
                style = style.deriveTextStyle(Style.TEXTSTYLE_WRAPTEXT);
            } else if (ComparatorUtils.equals(this.textComboBox.getSelectedItem(), TEXT[1])) {
                style = style.deriveTextStyle(Style.TEXTSTYLE_SINGLELINE);
            } else if (ComparatorUtils.equals(this.textComboBox.getSelectedItem(), TEXT[2])) {
                style = style.deriveTextStyle(Style.TEXTSTYLE_SINGLELINEADJUSTFONT);
            } else {
                style = style.deriveTextStyle(Style.TEXTSTYLE_MULTILINEADJUSTFONT);
            }
        }

        style = updateImageLayout(style);
        style = updateTextRotation(style);
        style = updateOther(style);
        return style;
    }


    private Style updateImageLayout(Style style) {
        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Image_Layout"))) {
            if (ComparatorUtils.equals(this.imageLayoutComboBox.getSelectedItem(), LAYOUT[1])) {
                style = style.deriveImageLayout(Constants.IMAGE_TILED);
            } else if (ComparatorUtils.equals(this.imageLayoutComboBox.getSelectedItem(), LAYOUT[2])) {
                style = style.deriveImageLayout(Constants.IMAGE_EXTEND);
            } else if (ComparatorUtils.equals(this.imageLayoutComboBox.getSelectedItem(), LAYOUT[3])) {
                style = style.deriveImageLayout(Constants.IMAGE_ADJUST);
            } else {
                style = style.deriveImageLayout(Constants.IMAGE_CENTER);
            }
        }
        return style;
    }


    private Style updateTextRotation(Style style) {
        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_StyleAlignment_Text_Rotation"))) {
            if (this.textRotationComboBox.getSelectedIndex() != 0) {
                style = style.deriveVerticalText(Style.VERTICALTEXT);
                style = style.deriveRotation(0);
                style = style.deriveTextDirection(this.textRotationComboBox.getSelectedIndex() == 1 ? Style.LEFT_TO_RIGHT : Style.RIGHT_TO_LEFT);
            } else {
                style = style.deriveVerticalText(Style.HORIZONTALTEXT);
                style = style.deriveRotation(rotationPane.updateBean().intValue());
            }
        }
        return style;
    }

    private Style updateOther(Style style) {
        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Left_Indent"))) {
            style = style.derivePaddingLeft(indentationUnitProcessor.paddingUnitGainFromSpinner((int) (this.leftIndentSpinner.getValue())));
        }

        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Right_Indent"))) {
            style = style.derivePaddingRight(indentationUnitProcessor.paddingUnitGainFromSpinner((int) (this.rightIndentSpinner.getValue())));
        }
        //间距
        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Spacing_Before"))) {
            style = style.deriveSpacingBefore((int) (this.spaceBeforeSpinner.getValue()));
        }
        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Spacing_After"))) {
            style = style.deriveSpacingAfter((int) (this.spaceAfterSpinner.getValue()));
        }
        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Line_Spacing"))) {
            style = style.deriveLineSpacing((int) (this.lineSpaceSpinner.getValue()));
        }
        return style;
    }

    /**
     * 注册监听事件
     *
     * @param listener 观察者监听事件
     */
    public void registerNameListener(GlobalNameListener listener) {
        globalNameListener = listener;
    }

    /**
     * 是否响应监听
     *
     * @return 否
     */
    public boolean shouldResponseNameListener() {
        return false;
    }

    /**
     * @param name 全局名
     */
    public void setGlobalName(String name) {
    }
}
