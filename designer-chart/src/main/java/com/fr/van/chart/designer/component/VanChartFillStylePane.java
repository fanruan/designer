package com.fr.van.chart.designer.component;

import com.fr.base.ChartColorMatching;
import com.fr.base.ChartPreStyleConfig;
import com.fr.base.Utils;
import com.fr.chart.base.AttrFillStyle;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.base.ChartUtils;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chartx.component.combobox.ColorSchemeComboBox;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.mainframe.chart.gui.style.ChartColorAdjustPane;
import com.fr.design.style.background.gradient.FixedGradientBar;
import com.fr.stable.StringUtils;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by mengao on 2017/8/17.
 */
public class VanChartFillStylePane extends BasicBeanPane<AttrFillStyle> {


    protected ColorSchemeComboBox styleSelectBox;
    protected JPanel customPane;
    protected JPanel changeColorSetPane;
    protected FixedGradientBar colorGradient;

    protected CardLayout cardLayout;

    protected ChartColorAdjustPane colorAdjustPane;

    private Color[] gradientColors;
    private Color[] accColors;

    private boolean gradientSelect = false;

    public VanChartFillStylePane() {
        this.setLayout(new BorderLayout());

        styleSelectBox = new ColorSchemeComboBox();
        customPane = new JPanel(FRGUIPaneFactory.createBorderLayout());

        changeColorSetPane = new JPanel(cardLayout = new CardLayout());
        changeColorSetPane.add(colorGradient = new FixedGradientBar(4, 130), "gradient");
        gradientColors = new Color[]{Color.WHITE, FixedGradientBar.NEW_CHARACTER};
        changeColorSetPane.add(colorAdjustPane = new ChartColorAdjustPane(), "acc");
        accColors = ChartColorAdjustPane.defaultColors;
        cardLayout.show(changeColorSetPane, "acc");
        customPane.add(changeColorSetPane, BorderLayout.CENTER);
        initListener();
        initLayout();
    }

    private void initListener() {
        colorAdjustPane.registerChangeListener(new UIObserverListener() {
            @Override
            public void doChange() {
                accColors = colorAdjustPane.getColors();
                if (styleSelectBox.getSelectedIndex() != styleSelectBox.getItemCount() - 2) {
                    styleSelectBox.setSelectedIndex(styleSelectBox.getItemCount() - 2);
                }
                VanChartFillStylePane.this.revalidate();
            }
        });
        colorGradient.registerChangeListener(new UIObserverListener() {
            @Override
            public void doChange() {
                gradientColors[0] = colorGradient.getSelectColorPointBtnP1().getColorInner();
                gradientColors[1] = colorGradient.getSelectColorPointBtnP2().getColorInner();
                if (styleSelectBox.getSelectedIndex() != styleSelectBox.getItemCount() - 1) {
                    styleSelectBox.setSelectedIndex(styleSelectBox.getItemCount() - 1);
                }
            }
        });
        styleSelectBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ColorSchemeComboBox.ColorInfo selectColorInfo = styleSelectBox.getSelectColorInfo();
                if (styleSelectBox.getSelectedIndex() == styleSelectBox.getItemCount() - 2) {
                    colorAdjustPane.updateColor(accColors);
                    cardLayout.show(changeColorSetPane, "acc");
                    gradientSelect = false;
                } else if (styleSelectBox.getSelectedIndex() == styleSelectBox.getItemCount() - 1) {
                    colorGradient.updateColor(gradientColors[0], gradientColors[1]);
                    changeColorSetPane.add(colorGradient, "gradient");
                    cardLayout.show(changeColorSetPane, "gradient");
                    gradientSelect = true;
                } else if (selectColorInfo.isGradient()) {
                    colorGradient.updateColor(selectColorInfo.getColors().get(0), selectColorInfo.getColors().get(1));
                    changeColorSetPane.add(colorGradient, "gradient");
                    cardLayout.show(changeColorSetPane, "gradient");
                    gradientSelect = true;
                } else {
                    colorAdjustPane.updateColor(selectColorInfo.getColors().toArray(new Color[]{}));
                    cardLayout.show(changeColorSetPane, "acc");
                    gradientSelect = false;
                }
                VanChartFillStylePane.this.revalidate();
            }
        });
    }

    protected void initLayout() {
        this.setLayout(new BorderLayout());
        this.add(getContentPane(), BorderLayout.CENTER);
    }

    @Override
    public Dimension getPreferredSize() {
        if (gradientSelect) {
            return new Dimension(225, 80);
        }
        return super.getPreferredSize();
    }

    protected JPanel getContentPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        double[] rowSize = {p, p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Match_Color_Scheme")), styleSelectBox},
                new Component[]{null, customPane},

        };
        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components, rowSize, columnSize);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        return panel;
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Color");
    }

    @Override
    public void populateBean(AttrFillStyle condition) {
        String fillStyleName = condition == null ? "" : condition.getFillStyleName();
        if (StringUtils.isBlank(fillStyleName)) {//兼容处理
            if (condition == null || condition.getColorStyle() == ChartConstants.COLOR_DEFAULT) {
                styleSelectBox.setSelectedIndex(0);//默认
            } else {
                int colorStyle = condition.getColorStyle();
                if (colorStyle == ChartConstants.COLOR_GRADIENT) {
                    gradientColors[0] = condition.getColorIndex(0);
                    gradientColors[1] = condition.getColorIndex(1);
                    styleSelectBox.setSelectedIndex(styleSelectBox.getItemCount() - 1);
                } else {
                    int colorSize = condition.getColorSize();
                    accColors = new Color[colorSize];
                    for (int i = 0; i < colorSize; i++) {
                        accColors[i] = condition.getColorIndex(i);
                    }
                    styleSelectBox.setSelectedIndex(styleSelectBox.getItemCount() - 2);
                }
            }
        } else {
            styleSelectBox.setSelectedItem(fillStyleName);
        }
    }

    @Override
    public AttrFillStyle updateBean() {
        AttrFillStyle condition = new AttrFillStyle();
        condition.clearColors();

        //自定义组合色
        if (styleSelectBox.getSelectedIndex() == styleSelectBox.getItemCount() - 2) {
            condition.setColorStyle(ChartConstants.COLOR_ACC);
            for (int i = 0, length = accColors.length; i < length; i++) {
                condition.addFillColor(accColors[i]);
            }
            //自定义渐变色
        } else if (styleSelectBox.getSelectedIndex() == styleSelectBox.getItemCount() - 1) {
            condition.setColorStyle(ChartConstants.COLOR_GRADIENT);
            Color start = gradientColors[0];
            Color end = gradientColors[1];
            condition.addFillColor(start);
            condition.addFillColor(end);
        } else if (styleSelectBox.getSelectedIndex() == 0) {
            condition.setColorStyle(ChartConstants.COLOR_DEFAULT);
        } else {
            ChartPreStyleConfig manager = ChartPreStyleConfig.getInstance();
            Object preStyle = manager.getPreStyle(styleSelectBox.getSelectedItem());
            if (preStyle instanceof ChartColorMatching) {
                AttrFillStyle def = ChartUtils.chartColorMatching2AttrFillStyle((ChartColorMatching) preStyle);
                def.setFillStyleName(Utils.objectToString(styleSelectBox.getSelectedItem()));
                return def;
            } else {
                condition.setColorStyle(ChartConstants.COLOR_DEFAULT);
            }
            condition.setCustomFillStyle(true);
        }

        return condition;
    }
}
