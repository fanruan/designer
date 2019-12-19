package com.fr.design.gui.style;

/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */

import com.fr.base.BaseUtils;
import com.fr.base.CellBorderStyle;
import com.fr.base.Style;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.backgroundpane.BackgroundQuickPane;
import com.fr.design.mainframe.backgroundpane.ColorBackgroundQuickPane;
import com.fr.design.style.color.NewColorSelectBox;

import com.fr.general.Background;
import com.fr.stable.Constants;
import com.fr.stable.CoreConstants;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zhou
 * @since 2012-5-28下午6:22:04
 */
public class BorderPane extends AbstractBasicStylePane implements GlobalNameObserver {

    private static final String[] BORDERARRAY = {"currentLineCombo", "currentLineColorPane", "outerToggleButton", "topToggleButton",
            "leftToggleButton", "bottomToggleButton", "rightToggleButton", "innerToggleButton", "horizontalToggleButton", "verticalToggleButton"};
    private static final Set<String> BORDER_SET = new HashSet<>(Arrays.asList(BORDERARRAY));
    private boolean insideMode = false;

    private UIToggleButton topToggleButton;
    private UIToggleButton horizontalToggleButton;
    private UIToggleButton bottomToggleButton;
    private UIToggleButton leftToggleButton;
    private UIToggleButton verticalToggleButton;
    private UIToggleButton rightToggleButton;

    private UIToggleButton innerToggleButton;
    private UIToggleButton outerToggleButton;

    private LineComboBox currentLineCombo;
    private NewColorSelectBox currentLineColorPane;
    private JPanel panel;
    private JPanel borderPanel;
    private JPanel backgroundPanel;
    private BackgroundPane backgroundPane;
    private GlobalNameListener globalNameListener = null;

    public BorderPane() {
        this.initComponents();
    }

    protected void initComponents() {
        initButtonsWithIcon();
        this.setLayout(new BorderLayout(0, 0));
        JPanel externalPane = new JPanel(new GridLayout(0, 4));
        externalPane.add(topToggleButton);
        externalPane.add(leftToggleButton);
        externalPane.add(bottomToggleButton);
        externalPane.add(rightToggleButton);
        JPanel insidePane = new JPanel(new GridLayout(0, 2));
        insidePane.add(horizontalToggleButton);
        insidePane.add(verticalToggleButton);
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Style") + "    ", SwingConstants.LEFT), currentLineCombo},
                new Component[]{null, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Color") + "    ", SwingConstants.LEFT), currentLineColorPane},
                new Component[]{null, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Out_Border") + "    ", SwingConstants.LEFT), outerToggleButton = new UIToggleButton(new Icon[]{BaseUtils.readIcon("com/fr/design/images/m_format/out.png"), BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/out_white.png")}, false)},
                new Component[]{null, externalPane},
                new Component[]{null, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_In_Border") + "    ", SwingConstants.LEFT), innerToggleButton = new UIToggleButton(new Icon[]{BaseUtils.readIcon("com/fr/design/images/m_format/in.png"), BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/in_white.png")}, false)},
                new Component[]{null, insidePane},
                new Component[]{null, null}
        };
        double[] rowSize = {p, p, p, p, p, p, p, p, p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}};
        panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_SMALL, LayoutConstants.VGAP_MEDIUM);
        borderPanel = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Border"), 280, 24, panel);
        this.add(borderPanel, BorderLayout.NORTH);

        backgroundPane = new BackgroundPane();
        backgroundPanel = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background"), 280, 24, backgroundPane);
        this.add(backgroundPanel, BorderLayout.CENTER);
        initAllNames();
        outerToggleButton.addChangeListener(outerToggleButtonChangeListener);
        innerToggleButton.addChangeListener(innerToggleButtonChangeListener);
    }

    ChangeListener outerToggleButtonChangeListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            boolean value = outerToggleButton.isSelected();
            topToggleButton.setSelected(value);
            bottomToggleButton.setSelected(value);
            leftToggleButton.setSelected(value);
            rightToggleButton.setSelected(value);
        }
    };

    ChangeListener innerToggleButtonChangeListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            boolean value = innerToggleButton.isSelected();
            horizontalToggleButton.setSelected(value);
            verticalToggleButton.setSelected(value);
        }
    };

    private void initButtonsWithIcon() {
        topToggleButton = new UIToggleButton(new Icon[]{BaseUtils.readIcon("/com/fr/base/images/dialog/border/top.png"), BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/top_white.png")}, false);
        leftToggleButton = new UIToggleButton(new Icon[]{BaseUtils.readIcon("/com/fr/base/images/dialog/border/left.png"), BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/left_white.png")}, false);
        bottomToggleButton = new UIToggleButton(new Icon[]{BaseUtils.readIcon("/com/fr/base/images/dialog/border/bottom.png"), BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/bottom_white.png")}, false);
        rightToggleButton = new UIToggleButton(new Icon[]{BaseUtils.readIcon("/com/fr/base/images/dialog/border/right.png"), BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/right_white.png")}, false);
        horizontalToggleButton = new UIToggleButton(new Icon[]{BaseUtils.readIcon("/com/fr/base/images/dialog/border/horizontal.png"), BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/horizontal_white.png")}, false);
        verticalToggleButton = new UIToggleButton(new Icon[]{BaseUtils.readIcon("/com/fr/base/images/dialog/border/vertical.png"), BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/vertical_white.png")}, false);
        this.currentLineCombo = new LineComboBox(CoreConstants.UNDERLINE_STYLE_ARRAY);
        this.currentLineColorPane = new NewColorSelectBox(100);
    }

    private void initAllNames() {
        currentLineCombo.setGlobalName("currentLineCombo");
        currentLineColorPane.setGlobalName("currentLineColorPane");
        outerToggleButton.setGlobalName("outerToggleButton");
        topToggleButton.setGlobalName("topToggleButton");
        leftToggleButton.setGlobalName("leftToggleButton");
        bottomToggleButton.setGlobalName("bottomToggleButton");
        rightToggleButton.setGlobalName("rightToggleButton");
        innerToggleButton.setGlobalName("innerToggleButton");
        horizontalToggleButton.setGlobalName("horizontalToggleButton");
        verticalToggleButton.setGlobalName("verticalToggleButton");
    }

    @Override
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Cell");
    }

    @Override
    public void populateBean(Style style) {
        if (style == null) {
            style = Style.DEFAULT_STYLE;
        }

        CellBorderStyle cellBorderStyle = new CellBorderStyle();
        cellBorderStyle.setTopStyle(style.getBorderTop());
        cellBorderStyle.setTopColor(style.getBorderTopColor());
        cellBorderStyle.setLeftStyle(style.getBorderLeft());
        cellBorderStyle.setLeftColor(style.getBorderLeftColor());
        cellBorderStyle.setBottomStyle(style.getBorderBottom());
        cellBorderStyle.setBottomColor(style.getBorderBottomColor());
        cellBorderStyle.setRightStyle(style.getBorderRight());
        cellBorderStyle.setRightColor(style.getBorderRightColor());
        this.backgroundPane.populateBean(style.getBackground());
        this.populateBean(cellBorderStyle, false, style.getBorderTop(), style.getBorderTopColor());

    }

    public void populateBean(CellBorderStyle cellBorderStyle, boolean insideMode, int currentStyle, Color currentColor) {
        this.insideMode = insideMode;

        this.currentLineCombo.setSelectedLineStyle(cellBorderStyle.getTopStyle() == Constants.LINE_NONE ? Constants.LINE_THIN : cellBorderStyle.getTopStyle());
        this.currentLineColorPane.setSelectObject(cellBorderStyle.getTopColor());

        this.topToggleButton.setSelected(cellBorderStyle.getTopStyle() != Constants.LINE_NONE);
        this.bottomToggleButton.setSelected(cellBorderStyle.getBottomStyle() != Constants.LINE_NONE);
        this.leftToggleButton.setSelected(cellBorderStyle.getLeftStyle() != Constants.LINE_NONE);
        this.rightToggleButton.setSelected(cellBorderStyle.getRightStyle() != Constants.LINE_NONE);

        this.horizontalToggleButton.setSelected(cellBorderStyle.getHorizontalStyle() != Constants.LINE_NONE);
        this.verticalToggleButton.setSelected(cellBorderStyle.getVerticalStyle() != Constants.LINE_NONE);

        this.innerToggleButton.setSelected(cellBorderStyle.getInnerBorder() != Constants.LINE_NONE);
        this.outerToggleButton.setSelected(cellBorderStyle.getOuterBorderStyle() != Constants.LINE_NONE);

        this.innerToggleButton.setEnabled(this.insideMode);
        this.horizontalToggleButton.setEnabled(this.insideMode);
        this.verticalToggleButton.setEnabled(this.insideMode);
    }

    @Override
    public Style update(Style style) {

        if (style == null) {
            style = Style.DEFAULT_STYLE;
        }

        if (backgroundPane.currentPane.isBackgroundChange()) {
            style = style.deriveBackground(backgroundPane.update());
        }
        if (BORDER_SET.contains(globalNameListener.getGlobalName())) {
            CellBorderStyle cellBorderStyle = this.update();
            style = style.deriveBorder(cellBorderStyle.getTopStyle(), cellBorderStyle.getTopColor(), cellBorderStyle.getBottomStyle(), cellBorderStyle.getBottomColor(),
                                       cellBorderStyle.getLeftStyle(), cellBorderStyle.getLeftColor(), cellBorderStyle.getRightStyle(), cellBorderStyle.getRightColor());
        }

        return style;
    }

    public CellBorderStyle update() {
        int lineStyle = currentLineCombo.getSelectedLineStyle();
        Color lineColor = currentLineColorPane.getSelectObject();
        CellBorderStyle cellBorderStyle = new CellBorderStyle();
        cellBorderStyle.setTopColor(lineColor);
        cellBorderStyle.setTopStyle(topToggleButton.isSelected() ? lineStyle : Constants.LINE_NONE);
        cellBorderStyle.setBottomColor(lineColor);
        cellBorderStyle.setBottomStyle(bottomToggleButton.isSelected() ? lineStyle : Constants.LINE_NONE);
        cellBorderStyle.setLeftColor(lineColor);
        cellBorderStyle.setLeftStyle(leftToggleButton.isSelected() ? lineStyle : Constants.LINE_NONE);
        cellBorderStyle.setRightColor(lineColor);
        cellBorderStyle.setRightStyle(rightToggleButton.isSelected() ? lineStyle : Constants.LINE_NONE);
        cellBorderStyle.setVerticalColor(lineColor);
        cellBorderStyle.setVerticalStyle(verticalToggleButton.isSelected() ? lineStyle : Constants.LINE_NONE);
        cellBorderStyle.setHorizontalColor(lineColor);
        cellBorderStyle.setHorizontalStyle(horizontalToggleButton.isSelected() ? lineStyle : Constants.LINE_NONE);
        return cellBorderStyle;
    }

    @Override
    public void registerNameListener(GlobalNameListener listener) {
        globalNameListener = listener;
    }

    @Override
    public boolean shouldResponseNameListener() {
        return false;
    }

    @Override
    public void setGlobalName(String name) {

    }
}
