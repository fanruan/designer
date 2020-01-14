package com.fr.design.mainframe.backgroundpane;

import com.fr.base.background.GradientBackground;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.background.gradient.GradientBar;
import com.fr.general.Background;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * @author zhou
 * @since 2012-5-30上午10:36:21
 */
public class GradientBackgroundQuickPane extends BackgroundQuickPane {
	private static final long serialVersionUID = -6854603990673031897L;

    private static final int DEFAULT_GRADIENT_WIDTH = 150	;

    private int gradientBarWidth = DEFAULT_GRADIENT_WIDTH;

	private GradientBar gradientBar;
	private UIButtonGroup<Integer> directionPane;

	public GradientBackgroundQuickPane() {
        constructPane();
	}

    public GradientBackgroundQuickPane(int gradientBarWidth) {
        this.gradientBarWidth = gradientBarWidth;
        constructPane();
    }

    private void constructPane(){
        String[] textArray = {com.fr.design.i18n.Toolkit.i18nText("FIne-Design_Report_Utils_Left_To_Right"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Top_To_Bottom")};
        Integer[] valueArray = {GradientBackground.LEFT2RIGHT, GradientBackground.TOP2BOTTOM};
        directionPane = new UIButtonGroup<Integer>(textArray, valueArray);
        directionPane.setSelectedIndex(0);
        gradientBar = new GradientBar(4, this.gradientBarWidth);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p,};

        Component[][] components = new Component[][]{
                new Component[]{gradientBar, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Gradient_Direction")), directionPane}
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
		if (bg.isUseCell()) {
			return;
		}
		double startValue = (double) bg.getBeginPlace();
		double endValue = (double) bg.getFinishPlace();
		gradientBar.setStartValue(startValue);
		gradientBar.setEndValue(endValue);
        if(this.gradientBar.getSelectColorPointBtnP1() != null && this.gradientBar.getSelectColorPointBtnP2() != null){
            this.gradientBar.getSelectColorPointBtnP1().setX(startValue);
            this.gradientBar.getSelectColorPointBtnP2().setX(endValue);
        }
		this.gradientBar.repaint();
	}

	public GradientBackground updateBean() {
		GradientBackground gb = new GradientBackground(gradientBar.getSelectColorPointBtnP1().getColorInner(), gradientBar.getSelectColorPointBtnP2().getColorInner());
		gb.setDirection(directionPane.getSelectedItem());
		if (gradientBar.isOriginalPlace()) {
			gb.setUseCell(true);
		} else {
			gb.setUseCell(false);
			gb.setBeginPlace((float) gradientBar.getStartValue());
			gb.setFinishPlace((float) gradientBar.getEndValue());
		}
		return gb;
	}

	/**
	 * 给组件登记一个观察者监听事件
	 *
	 * @param listener 观察者监听事件
	 */
	@Override
	public void registerChangeListener(final UIObserverListener listener) {
	   gradientBar.addChangeListener(new ChangeListenerImpl(listener));
	   directionPane.addChangeListener(new ChangeListenerImpl(listener));
	}

	@Override
	public boolean accept(Background background) {
		return background instanceof GradientBackground;
	}

	@Override
	public String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Gradient_Color");
	}

	@Override
	public void reset() {
		this.gradientBar.getSelectColorPointBtnP1().setColorInner(Color.WHITE);
		this.gradientBar.getSelectColorPointBtnP2().setColorInner(Color.BLACK);
		directionPane.setSelectedItem(0);
		int startValue = 4;
        int endValue = this.gradientBarWidth;
		gradientBar.setStartValue(startValue);
		gradientBar.setEndValue(endValue);
		if(this.gradientBar.getSelectColorPointBtnP1() != null && this.gradientBar.getSelectColorPointBtnP2() != null){
			this.gradientBar.getSelectColorPointBtnP1().setX(startValue);
			this.gradientBar.getSelectColorPointBtnP2().setX(endValue);
		}
	}

}
