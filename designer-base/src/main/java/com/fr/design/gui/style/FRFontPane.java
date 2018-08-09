package com.fr.design.gui.style;

/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.Style;
import com.fr.base.Utils;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.design.gui.ibutton.UIColorButton;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.DefaultValues;
import com.fr.general.FRFont;

import com.fr.stable.Constants;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

/**
 * Pane to edit Font.
 */
public class FRFontPane extends AbstractBasicStylePane implements GlobalNameObserver {
    private static final int MAX_FONT_SIZE = 100;
    public static Integer[] FONT_SIZES = {new Integer(6), new Integer(8), new Integer(9), new Integer(10), new Integer(11), new Integer(12), new Integer(14), new Integer(16),
            new Integer(18), new Integer(20), new Integer(22), new Integer(24), new Integer(26), new Integer(28), new Integer(36), new Integer(48), new Integer(72)};
    private static final Dimension BUTTON_SIZE = new Dimension(20, 18);
    private static final Dimension UNDER_LINE_SIZE = new Dimension(87, 20);
    private static final Dimension HIDE_SIZE = new Dimension(0, 0);
    private final String[] fontSizeStyles = {com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Plain"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Bold"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Italic"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Bolditalic")};
    private JPanel buttonPane;
    private JPanel isSuperOrSubPane;
    private UIComboBox fontNameComboBox;
    private UIComboBox fontSizeStyleComboBox;
    protected UIComboBox fontSizeComboBox;
    private UIToggleButton bold;
    private UIToggleButton italic;
    private UIToggleButton underline;
    private GlobalNameListener globalNameListener = null;

    // underline
    private LineComboBox underlineCombo;
    private UIColorButton colorSelectPane;
    // effects.
    private UIToggleButton isStrikethroughCheckBox;
    private UIToggleButton isShadowCheckBox;
    private UIToggleButton superPane;
    private UIToggleButton subPane;
    private JPanel linePane;
    private int italic_bold;

    public FRFontPane() {
        this.initComponents();
    }

    public static void main(String[] args) {
        JFrame jf = new JFrame("test");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel content = (JPanel) jf.getContentPane();
        content.setLayout(new BorderLayout());
        content.add(new FRFontPane(), BorderLayout.CENTER);
        GUICoreUtils.centerWindow(jf);
        jf.setSize(290, 400);
        jf.setVisible(true);
    }


    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Sytle-FRFont");
    }

    /**
     * Use font to populate pane.
     */
    public void populateBean(FRFont frFont) {
        fontNameComboBox.setSelectedItem(frFont.getFamily());
        fontSizeStyleComboBox.setSelectedIndex(frFont.getStyle());
        fontSizeComboBox.setSelectedItem(Utils.round5(frFont.getSize2D()));
        bold.setSelected(frFont.isBold());
        italic.setSelected(frFont.isItalic());

        // foreground.
        this.colorSelectPane.setColor(frFont.getForeground());
        this.colorSelectPane.repaint();
        // update frFont.

        CardLayout cly = (CardLayout) linePane.getLayout();
        int line = frFont.getUnderline();
        if (line == Constants.LINE_NONE) {
            underline.setSelected(false);
            cly.show(linePane, "none");
            linePane.setPreferredSize(HIDE_SIZE);
        } else {
            underline.setSelected(true);
            cly.show(linePane, "combobox");
            linePane.setPreferredSize(UNDER_LINE_SIZE);
            this.underlineCombo.setSelectedLineStyle(line);
        }
        // effects
        this.isStrikethroughCheckBox.setSelected(frFont.isStrikethrough());
        this.isShadowCheckBox.setSelected(frFont.isShadow());
        if (frFont.isSuperscript()) {
            this.superPane.setSelected(true);
            this.subPane.setSelected(false);
        } else if (frFont.isSubscript()) {
            this.superPane.setSelected(false);
            this.subPane.setSelected(true);
        } else {
            this.superPane.setSelected(false);
            this.subPane.setSelected(false);
        }
    }

    /**
     * Update pane to get new font.
     */
    public FRFont update(FRFont frFont) {

        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Name"))) {
            frFont = frFont.applyName((String) fontNameComboBox.getSelectedItem());
        }
        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Style"))) {
            frFont = frFont.applyStyle(fontSizeStyleComboBox.getSelectedIndex());
        }
        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_FRFont_Size"))) {
            frFont = frFont.applySize(Float.parseFloat(fontSizeComboBox.getSelectedItem().toString()));
        }
        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Foreground"))) {
            frFont = frFont.applyForeground(this.colorSelectPane.getColor());
        }

        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Underline"))) {

            int line = underline.isSelected() ? this.underlineCombo.getSelectedLineStyle() : Constants.LINE_NONE;
            frFont = frFont.applyUnderline(line);

        }

        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_FRFont_Line_Style"))) {
            frFont = frFont.applyUnderline(this.underlineCombo.getSelectedLineStyle());
        }

        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Strikethrough"))) {
            frFont = frFont.applyStrikethrough(isStrikethroughCheckBox.isSelected());
        }
        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Shadow"))) {
            frFont = frFont.applyShadow(isShadowCheckBox.isSelected());
        }

        frFont = updateOthers(frFont);

        return frFont;
    }


    private FRFont updateOthers(FRFont frFont) {
        frFont = updateSubSuperscript(frFont);
        return frFont;
    }

    private FRFont updateSubSuperscript(FRFont frFont) {
        boolean isSuper = frFont.isSuperscript();
        boolean isSub = frFont.isSubscript();
        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Superscript"))) {
            //如果上标没有选中,点击则选中上标，并且下标一定是不选中状态
            //如果上标选中，点击则取消选中上标，字体回复正常
            if (superPane.isSelected() && !isSuper) {
                frFont = frFont.applySuperscript(true);
                frFont = frFont.applySubscript(false);
                this.subPane.setSelected(false);
            } else if (!superPane.isSelected() && isSuper) {
                frFont = frFont.applySuperscript(false);
            }
        }
        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Subscript"))) {
            if (subPane.isSelected() && !isSub) {
                frFont = frFont.applySubscript(true);
                frFont = frFont.applySuperscript(false);
                this.superPane.setSelected(false);
            } else if (!subPane.isSelected() && isSub) {
                frFont = frFont.applySubscript(false);
            }
        }
        return frFont;
    }

    @Override
    public void populateBean(Style style) {
        this.populateBean(style.getFRFont());
    }

    @Override
    public Style update(Style style) {
        // TODO Auto-generated method stub
        FRFont frFont = style.getFRFont();
        frFont = this.update(frFont);
        return style.deriveFRFont(frFont);
    }

    public static Vector<Integer> getFontSizes() {
        Vector<Integer> FONT_SIZES = new Vector<Integer>();
        for (int i = 1; i < MAX_FONT_SIZE; i++) {
            FONT_SIZES.add(i);
        }
        return FONT_SIZES;
    }

    protected void initComponents() {
        fontSizeStyleComboBox = new UIComboBox(fontSizeStyles);
        fontNameComboBox = new UIComboBox(Utils.getAvailableFontFamilyNames4Report());
        fontNameComboBox.setPreferredSize(new Dimension(144, 20));
        fontSizeComboBox = new UIComboBox(getFontSizes());
        fontSizeComboBox.setEditable(true);
        this.underlineCombo = new LineComboBox(UIConstants.BORDER_LINE_STYLE_ARRAY);
        colorSelectPane = new UIColorButton();
        bold = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/bold.png"));
        italic = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/italic.png"));
        underline = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/underline.png"));
        bold.setPreferredSize(BUTTON_SIZE);
        italic.setPreferredSize(BUTTON_SIZE);
        underline.setPreferredSize(BUTTON_SIZE);
        isStrikethroughCheckBox = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/strikethrough.png"));
        isStrikethroughCheckBox.setPreferredSize(BUTTON_SIZE);
        isShadowCheckBox = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/shadow.png"));
        isShadowCheckBox.setPreferredSize(BUTTON_SIZE);
        superPane = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/sup.png"));
        superPane.setPreferredSize(BUTTON_SIZE);
        subPane = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/sub.png"));
        subPane.setPreferredSize(BUTTON_SIZE);
        Component[] SuperOrSubComponents = new Component[]{
                superPane, subPane
        };
        isSuperOrSubPane = new JPanel(new BorderLayout());
        isSuperOrSubPane.add(GUICoreUtils.createFlowPane(SuperOrSubComponents, FlowLayout.LEFT, LayoutConstants.HGAP_SMALL));
        Component[] components_font = new Component[]{
                colorSelectPane, underline, isStrikethroughCheckBox, isShadowCheckBox
        };
        buttonPane = new JPanel(new BorderLayout());
        buttonPane.add(GUICoreUtils.createFlowPane(components_font, FlowLayout.LEFT, LayoutConstants.HGAP_SMALL));
        linePane = new JPanel(new CardLayout());
        initAllNames();
        setToolTips();
        this.setLayout(new BorderLayout());
        this.add(createPane(), BorderLayout.CENTER);
        DefaultValues defaultValues = FRContext.getDefaultValues();
        populateBean(defaultValues.getFRFont());
    }

    private void initAllNames() {
        fontSizeStyleComboBox.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Style"));
        fontNameComboBox.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Name"));
        fontSizeComboBox.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_FRFont_Size"));
        colorSelectPane.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Foreground"));
        italic.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Italic"));
        bold.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Bold"));
        underline.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Underline"));
        underlineCombo.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_FRFont_Line_Style"));
        isStrikethroughCheckBox.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Strikethrough"));
        isShadowCheckBox.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Shadow"));
        superPane.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Superscript"));
        subPane.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Subscript"));
    }

    private void setToolTips() {
        colorSelectPane.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Foreground"));
        italic.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Italic"));
        bold.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Bold"));
        underline.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Underline"));
        isStrikethroughCheckBox.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Strikethrough"));
        isShadowCheckBox.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Shadow"));
        superPane.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Superscript"));
        subPane.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FR_Font_Subscript"));
    }


    private JPanel createLinePane() {
        linePane.add(new JPanel(), "none");
        JPanel gap = new JPanel(new GridLayout(0, 1));
        gap.add(underlineCombo);
        linePane.add(gap, "combobox");
        underline.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                CardLayout cly = (CardLayout) linePane.getLayout();
                cly.show(linePane, underline.isSelected() ? "combobox" : "none");
                if(underline.isSelected()){
                    linePane.setPreferredSize(UNDER_LINE_SIZE);
                }else{
                    linePane.setPreferredSize(HIDE_SIZE);
                }
            }
        });

        return linePane;
    }

    private JPanel createLeftPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p};
        double[] rowSize = {p, p, p};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{fontSizeStyleComboBox},
                new Component[]{buttonPane},
                new Component[]{createLinePane()}
        };
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM);
    }

    protected JPanel createRightPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = {p, p};
        int[][] rowCount = {{1, 1}, {1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{fontSizeComboBox},
                new Component[]{isSuperOrSubPane}
        };
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM);
    }

    private JPanel createPane() {
        JPanel createPane = new JPanel(new BorderLayout());
        createPane.add(fontNameComboBox, BorderLayout.NORTH);
        JPanel jPanel = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{createLeftPane(), createRightPane()}}, TableLayoutHelper.FILL_LASTCOLUMN, LayoutConstants.VGAP_LARGE, LayoutConstants.VGAP_LARGE);
        jPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        createPane.add(jPanel, BorderLayout.CENTER);
        return createPane;
    }

    /**
     * @param listener 观察者监听事件
     */
    public void registerNameListener(GlobalNameListener listener) {
        globalNameListener = listener;
    }

    /**
     * @return
     */
    public boolean shouldResponseNameListener() {
        return false;
    }

    public void setGlobalName(String name) {
    }

    private class TwoButtonPane extends JPanel {
        public UIToggleButton leftButton;
        public UIToggleButton rightButton;

        public TwoButtonPane(UIToggleButton leftButton, UIToggleButton rightButton) {
            this.leftButton = leftButton;
            this.rightButton = rightButton;
            this.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 0));
            this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            initButton(leftButton);
            initButton(rightButton);
            initListener();
        }

        private void initListener() {
            leftButton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    rightButton.setSelected(false);
                }
            });
            rightButton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    leftButton.setSelected(false);
                }
            });
        }

        private void initButton(UIToggleButton button) {
            button.setRoundBorder(false);
            button.setBorderPainted(false);
            this.add(button);
        }


        protected void paintBorder(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(UIConstants.LINE_COLOR);
            int buttonX = getComponent(0).getX();
            int buttonY = getComponent(0).getY();
            int height = getComponent(0).getHeight();
            int width = getComponent(0).getWidth();
            g.drawLine(buttonX + width, 0, buttonX + width, height);
            width += getComponent(1).getWidth();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.drawRoundRect(buttonX - 1, buttonY - 1, width + 2, getHeight() - 1, UIConstants.ARC, UIConstants.ARC);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
    }
}
