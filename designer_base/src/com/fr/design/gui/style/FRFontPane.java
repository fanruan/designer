package com.fr.design.gui.style;

/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.Style;
import com.fr.base.Utils;
import com.fr.design.constants.UIConstants;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.design.gui.ibutton.UIColorButton;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.ComparatorUtils;
import com.fr.general.DefaultValues;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * Pane to edit Font.
 */
public class FRFontPane extends AbstractBasicStylePane implements GlobalNameObserver {
    @Deprecated //发布的图表插件用到，8.0就先留着吧。9.0就可以删掉了。
    public static Integer[] FONT_SIZES = {new Integer(6), new Integer(8), new Integer(9), new Integer(10), new Integer(11), new Integer(12), new Integer(14), new Integer(16),
            new Integer(18), new Integer(20), new Integer(22), new Integer(24), new Integer(26), new Integer(28), new Integer(36), new Integer(48), new Integer(72)};

    private static final int MAX_FONT_SIZE = 100;
    private static final Dimension BUTTON_SIZE = new Dimension(24, 20);
    private JPanel buttonPane;
    private JPanel isSuperOrSubPane;
    private UIComboBox fontNameComboBox;
    private UIComboBox fontSizeComboBox;
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

    public FRFontPane() {
        this.initComponents();
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("Sytle-FRFont");
    }

    /**
     * Use font to populate pane.
     */
    public void populateBean(FRFont frFont) {
        fontNameComboBox.setSelectedItem(frFont.getFamily());
        fontSizeComboBox.setSelectedItem(frFont.getSize());
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
        } else {
            underline.setSelected(true);
            cly.show(linePane, "combobox");
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

        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), Inter.getLocText("FRFont-Family"))) {
            frFont = frFont.applyName((String) fontNameComboBox.getSelectedItem());
        }
        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), Inter.getLocText("FRFont-Size"))) {
            frFont = frFont.applySize((Integer) fontSizeComboBox.getSelectedItem());
        }
        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), Inter.getLocText("FRFont-Foreground"))) {
            frFont = frFont.applyForeground(this.colorSelectPane.getColor());
        }

        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), Inter.getLocText("FRFont-Underline"))) {

            int line = underline.isSelected() ? this.underlineCombo.getSelectedLineStyle() : Constants.LINE_NONE;
            frFont = frFont.applyUnderline(line);

        }

        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), Inter.getLocText("Line-Style"))) {
            frFont = frFont.applyUnderline(this.underlineCombo.getSelectedLineStyle());
        }

        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), Inter.getLocText("FRFont-Strikethrough"))) {
            frFont = frFont.applyStrikethrough(isStrikethroughCheckBox.isSelected());
        }
        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), Inter.getLocText("FRFont-Shadow"))) {
            frFont = frFont.applyShadow(isShadowCheckBox.isSelected());
        }

        frFont = updateOthers(frFont);

        return frFont;
    }


    private FRFont updateOthers(FRFont frFont) {
        frFont = updateItalicBold(frFont);
        frFont = updateSubSuperscript(frFont);
        return frFont;
    }

    private FRFont updateSubSuperscript(FRFont frFont) {
        boolean isSuper = frFont.isSuperscript();
        boolean isSub = frFont.isSubscript();
        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), Inter.getLocText("FRFont-Superscript"))) {
            //如果上标没有选中,点击则选中上标，并且下标一定是不选中状态
            //如果上标选中，点击则取消选中上标，字体回复正常
            if (superPane.isSelected() && !isSuper) {
                frFont = frFont.applySuperscript(true);
                frFont = frFont.applySubscript(false);
            } else if (!superPane.isSelected() && isSuper) {
                frFont = frFont.applySuperscript(false);
            }
        }
        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), Inter.getLocText("FRFont-Subscript"))) {
            if (subPane.isSelected() && !isSub) {
                frFont = frFont.applySubscript(true);
                frFont = frFont.applySuperscript(false);
            } else if (!subPane.isSelected() && isSub) {
                frFont = frFont.applySubscript(false);
            }
        }
        return frFont;
    }

    private FRFont updateItalicBold(FRFont frFont) {
        int italic_bold = frFont.getStyle();
        boolean isItalic = italic_bold == Font.ITALIC || italic_bold == (Font.BOLD + Font.ITALIC);
        boolean isBold = italic_bold == Font.BOLD || italic_bold == (Font.BOLD + Font.ITALIC);
        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), Inter.getLocText("FRFont-italic"))) {
            if (italic.isSelected() && !isItalic) {
                italic_bold += Font.ITALIC;
            } else if (!italic.isSelected() && isItalic) {
                italic_bold -= Font.ITALIC;
            }
            frFont = frFont.applyStyle(italic_bold);
        }
        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), Inter.getLocText("FRFont-bold"))) {
            if (bold.isSelected() && !isBold) {
                italic_bold += Font.BOLD;
            } else if (!bold.isSelected() && isBold) {
                italic_bold -= Font.BOLD;
            }
            frFont = frFont.applyStyle(italic_bold);
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
        fontNameComboBox = new UIComboBox(Utils.getAvailableFontFamilyNames4Report());
        fontNameComboBox.setPreferredSize(new Dimension(144, 20));
        fontSizeComboBox = new UIComboBox(getFontSizes());
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
        superPane.setPreferredSize(new Dimension(22, 18));
        subPane = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/sub.png"));
        subPane.setPreferredSize(new Dimension(22, 18));
        isSuperOrSubPane = new TwoButtonPane(superPane, subPane);
        Component[] components_font = new Component[]{
                colorSelectPane, italic, bold, underline, isStrikethroughCheckBox, isShadowCheckBox
        };
        buttonPane = new JPanel(new BorderLayout());
        buttonPane.add(GUICoreUtils.createFlowPane(components_font, FlowLayout.LEFT, LayoutConstants.HGAP_SMALL));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        linePane = new JPanel(new CardLayout());
        initAllNames();
        setToolTips();
        this.setLayout(new BorderLayout());
        this.add(createPane(), BorderLayout.CENTER);
        DefaultValues defaultValues = FRContext.getDefaultValues();
        populateBean(defaultValues.getFRFont());
    }

    private void initAllNames() {
        fontNameComboBox.setGlobalName(Inter.getLocText("FRFont-Family"));
        fontSizeComboBox.setGlobalName(Inter.getLocText("FRFont-Size"));
        colorSelectPane.setGlobalName(Inter.getLocText("FRFont-Foreground"));
        italic.setGlobalName(Inter.getLocText("FRFont-italic"));
        bold.setGlobalName(Inter.getLocText("FRFont-bold"));
        underline.setGlobalName(Inter.getLocText("FRFont-Underline"));
        underlineCombo.setGlobalName(Inter.getLocText("Line-Style"));
        isStrikethroughCheckBox.setGlobalName(Inter.getLocText("FRFont-Strikethrough"));
        isShadowCheckBox.setGlobalName(Inter.getLocText("FRFont-Shadow"));
        superPane.setGlobalName(Inter.getLocText("FRFont-Superscript"));
        subPane.setGlobalName(Inter.getLocText("FRFont-Subscript"));
    }

    private void setToolTips() {
        colorSelectPane.setToolTipText(Inter.getLocText("FRFont-Foreground"));
        italic.setToolTipText(Inter.getLocText("FRFont-italic"));
        bold.setToolTipText(Inter.getLocText("FRFont-bold"));
        underline.setToolTipText(Inter.getLocText("FRFont-Underline"));
        isStrikethroughCheckBox.setToolTipText(Inter.getLocText("FRFont-Strikethrough"));
        isShadowCheckBox.setToolTipText(Inter.getLocText("FRFont-Shadow"));
        superPane.setToolTipText(Inter.getLocText("FRFont-Superscript"));
        subPane.setToolTipText(Inter.getLocText("FRFont-Subscript"));
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
            }
        });
        return linePane;
    }

    private JPanel createLeftPane() {
        double p = TableLayout.PREFERRED;
        double[] columnSize = {p};
        double[] rowSize = {p, p, p};
        Component[][] components = new Component[][]{
                new Component[]{fontNameComboBox},
                new Component[]{buttonPane},
                new Component[]{createLinePane()}
        };
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    private JPanel createRightPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = {p, p};
        Component[][] components = new Component[][]{
                new Component[]{fontSizeComboBox},
                new Component[]{isSuperOrSubPane}
        };
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    private JPanel createPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p};
        Component[][] components = new Component[][]{
                new Component[]{createLeftPane(), createRightPane()},
        };
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
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
            this.setLayout(new FlowLayout(FlowLayout.RIGHT, 1, 0));
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