package com.fr.van.chart.designer.component.background;

import com.fr.base.background.GradientBackground;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.backgroundpane.GradientBackgroundQuickPane;
import com.fr.design.style.background.gradient.FixedGradientBar;
import com.fr.general.Background;


import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * 渐变色设置界面，bar无法拖拽
 */
public class VanChartGradientPane extends GradientBackgroundQuickPane {
    protected static final int CHART_GRADIENT_WIDTH = 150;
    private static final long serialVersionUID = 256594362341221087L;

    private FixedGradientBar gradientBar;
    private UIButtonGroup<Integer> directionPane;

    public VanChartGradientPane() {
        constructPane();
    }

    protected void constructPane(){
        String[] textArray = {com.fr.design.i18n.Toolkit.i18nText("Utils-Left_to_Right"), com.fr.design.i18n.Toolkit.i18nText("Utils-Top_to_Bottom")};
        Integer[] valueArray = {GradientBackground.LEFT2RIGHT, GradientBackground.TOP2BOTTOM};
        directionPane = new UIButtonGroup<Integer>(textArray, valueArray);
        directionPane.setSelectedIndex(0);
        gradientBar = new FixedGradientBar(4, CHART_GRADIENT_WIDTH);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p, p};

        Component[][] components = new Component[][]{
                new Component[]{gradientBar, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Gradient_Direction")),directionPane},
        };
        JPanel Gradient = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(Gradient, BorderLayout.CENTER);
    }

    public void populateBean(Background background) {
        GradientBackground bg = (GradientBackground) background;
        this.gradientBar.getSelectColorPointBtnP1().setColorInner(bg.getStartColor());
        this.gradientBar.getSelectColorPointBtnP2().setColorInner(bg.getEndColor());
        directionPane.setSelectedItem(bg.getDirection());
        this.gradientBar.repaint();
    }

    public GradientBackground updateBean() {
        GradientBackground gb = new GradientBackground(gradientBar.getSelectColorPointBtnP1().getColorInner(), gradientBar.getSelectColorPointBtnP2().getColorInner());
        gb.setDirection(directionPane.getSelectedItem());

        return gb;
    }

    /**
     * 给组件登记一个观察者监听事件
     *
     * @param listener 观察者监听事件
     */
    public void registerChangeListener(final UIObserverListener listener) {
        gradientBar.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                listener.doChange();
            }
        });
        directionPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                listener.doChange();
            }
        });
    }

    /**
     * 是否接受
     * @param background     背景
     * @return    是则返回true
     */
    public boolean accept(Background background) {
        return background instanceof GradientBackground;
    }

    /**
     * 名称
     * @return     名称
     */
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Gradient_Color");
    }

}