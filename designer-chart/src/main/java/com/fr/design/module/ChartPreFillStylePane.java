package com.fr.design.module;

import com.fr.base.ChartColorMatching;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.style.ChartColorAdjustPane;
import com.fr.design.style.background.gradient.FixedGradientBar;

import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * 预定义的图表配色界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-8-21 下午03:16:27
 */
public class ChartPreFillStylePane extends BasicBeanPane<ChartColorMatching> {

    private JPanel changeColorSetPane;
    private CardLayout cardLayout;

    private UIButton accButton;
    private UIButton gradientButton;

    private ChartColorAdjustPane colorAdjustPane;
    private FixedGradientBar colorGradient;

    public ChartPreFillStylePane() {

        initComponents();

        initListener();
    }

    private void initComponents() {

        JPanel customPane = new JPanel(FRGUIPaneFactory.createBorderLayout());

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPane.add(accButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Custom_Color")));
        buttonPane.add(gradientButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Gradient_Color")));
        customPane.add(buttonPane, BorderLayout.NORTH);

        changeColorSetPane = new JPanel(cardLayout = new CardLayout());
        changeColorSetPane.add(colorGradient = new FixedGradientBar(4, 130), "gradient");
        changeColorSetPane.add(colorAdjustPane = new ChartColorAdjustPane(), "acc");
        cardLayout.show(changeColorSetPane, "acc");
        customPane.add(changeColorSetPane, BorderLayout.CENTER);

        accButton.setSelected(true);

        customPane.setPreferredSize(new Dimension(200, 200));
        colorGradient.setPreferredSize(new Dimension(120, 30));
        colorGradient.getSelectColorPointBtnP1().setColorInner(Color.WHITE);
        colorGradient.getSelectColorPointBtnP2().setColorInner(FixedGradientBar.NEW_CHARACTER);

        double p = TableLayout.PREFERRED;
        double[] columnSize = {p, p};
        double[] rowSize = {p, p, p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(" " + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Color_Match")), null},
                new Component[]{null, customPane},
        };

        this.setLayout(new BorderLayout());
        this.add(TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize), BorderLayout.WEST);
    }

    private void initListener() {

        accButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accButton.setSelected(true);
                gradientButton.setSelected(false);
                cardLayout.show(changeColorSetPane, "acc");
            }
        });

        gradientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gradientButton.setSelected(true);
                accButton.setSelected(false);
                cardLayout.show(changeColorSetPane, "gradient");
            }
        });
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ServerM_Predefined_Styles");
    }

    public void populateBean(ChartColorMatching condition) {
        if (condition == null) {
            return;
        }

        boolean isGradient = condition.getGradient();
        List<Color> colorList = condition.getColorList();
        if (isGradient) {
            gradientButton.setSelected(true);
            accButton.setSelected(false);
            cardLayout.show(changeColorSetPane, "gradient");

            if (colorList.size() == 2) {
                colorGradient.getSelectColorPointBtnP1().setColorInner(colorList.get(0));
                colorGradient.getSelectColorPointBtnP2().setColorInner(colorList.get(1));
                colorGradient.repaint();
            }
        } else {
            accButton.setSelected(true);
            gradientButton.setSelected(false);
            cardLayout.show(changeColorSetPane, "acc");

            if (colorList.size() > 0) {
                colorAdjustPane.updateColor(colorList.toArray(new Color[colorList.size()]));
            }
        }
    }

    @Override
    public ChartColorMatching updateBean() {
        ChartColorMatching chartColorMatching = new ChartColorMatching();

        List<Color> colorList = new ArrayList<Color>();

        if(gradientButton.isSelected()) {
            chartColorMatching.setGradient(true);

            Color start = colorGradient.getSelectColorPointBtnP1().getColorInner();
            Color end = colorGradient.getSelectColorPointBtnP2().getColorInner();
            colorList.add(start);
            colorList.add(end);
        } else {
            chartColorMatching.setGradient(false);

            Color[] colors = colorAdjustPane.getColors();
            for(Color color : colors) {
                colorList.add(color);
            }
        }

        chartColorMatching.setColorList(colorList);

        return chartColorMatching;
    }
}
