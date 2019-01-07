package com.fr.design.style.background.gradient;

import com.fr.base.background.GradientBackground;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.background.BackgroundDetailPane;
import com.fr.general.Background;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 渐变色的面板，不是很pp，面板应用显得繁琐，有写可以写成控件类型，比如色彩选择的。。，可以做得花哨点
 * @author ben
 */
public class GradientBackgroundPane extends BackgroundDetailPane {
	private static final long serialVersionUID = -6854603990673031897L;

	private UIRadioButton left2right, top2bottom;
	private GradientBar gradientBar;
	private ChangeListener changeListener = null;

	public GradientBackgroundPane() {

		// bug 5452 简化GradientPane
		JPanel jpanel = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Background_Choose_Gradient_Color"));
		jpanel.setPreferredSize(new Dimension(450, 320));
		jpanel.setLayout(new BorderLayout());

		// neil:增加渐变色拖动条
		JPanel gradientPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
		JPanel blankJp = new JPanel();
		gradientBar = new GradientBar(4, 254);
		blankJp.add(gradientBar);
		UILabel jl = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Drag_To_Select_Gradient"));
		jl.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
		jl.setLineWrap();
		jl.setHorizontalAlignment(SwingConstants.CENTER);
		gradientPanel.add(jl, BorderLayout.NORTH);
		gradientPanel.add(blankJp, BorderLayout.SOUTH);
		jpanel.add(gradientPanel, BorderLayout.NORTH);

		JPanel centerPane = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
		JPanel innercenterPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		centerPane.add(new UILabel("           "));
		centerPane.add(innercenterPane);
		innercenterPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Gradient_Direction") + ":"));

		left2right = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Page_Setup_Horizontal"));
		innercenterPane.add(left2right);
		left2right.setSelected(true);
		left2right.addActionListener(reviewListener);

		top2bottom = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Page_Setup_Vertical"));
		innercenterPane.add(top2bottom);
		top2bottom.addActionListener(reviewListener);

		ButtonGroup toggle = new ButtonGroup();
		toggle.add(left2right);
		toggle.add(top2bottom);
		jpanel.add(centerPane, BorderLayout.CENTER);

		this.add(jpanel);
	}


	public void populate(Background background) {
		if (!(background instanceof GradientBackground)) {
			return;
		}
		GradientBackground bg = (GradientBackground) background;
		this.gradientBar.getSelectColorPointBtnP1().setColorInner(bg.getStartColor());
		this.gradientBar.getSelectColorPointBtnP2().setColorInner(bg.getEndColor());
		if (bg.getDirection() == GradientBackground.LEFT2RIGHT) {
			left2right.setSelected(true);
		} else {
			top2bottom.setSelected(true);
		}
		if (bg.isUseCell()) {
			return;
		}
		double startValue = (double) bg.getBeginPlace();
		double endValue = (double) bg.getFinishPlace();
		gradientBar.setStartValue(startValue);
		gradientBar.setEndValue(endValue);
		this.gradientBar.repaint();
	}

	public GradientBackground update() {
		GradientBackground gb = new GradientBackground(
				gradientBar.getSelectColorPointBtnP1().getColorInner(),
				gradientBar.getSelectColorPointBtnP2().getColorInner());
		if (left2right.isSelected()) {
			gb.setDirection(GradientBackground.LEFT2RIGHT);
		} else {
			gb.setDirection(GradientBackground.TOP2BOTTOM);
		}
		if (gradientBar.isOriginalPlace()) {
			gb.setUseCell(true);
		} else {
			gb.setUseCell(false);
			gb.setBeginPlace((float) gradientBar.getStartValue());
			gb.setFinishPlace((float) gradientBar.getEndValue());
		}
		return gb;
	}


	ActionListener reviewListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			fireChagneListener();
		}
	};

	public void addChangeListener(ChangeListener changeListener) {
		this.changeListener = changeListener;
		gradientBar.addChangeListener(changeListener);
	}

	public void fireChagneListener() {
		if (this.changeListener != null) {
			ChangeEvent evt = new ChangeEvent(this);
			this.changeListener.stateChanged(evt);
		}
	}
}
