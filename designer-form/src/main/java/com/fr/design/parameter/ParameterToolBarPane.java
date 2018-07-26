package com.fr.design.parameter;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.SwingConstants;

import com.fr.base.Parameter;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButton;


/**
 * 参数点击动作条
 * @author: august
 * */
public class ParameterToolBarPane extends BasicBeanPane<Parameter[]> {
	private Parameter[] parameterList;
	private ArrayList<UIButton> parameterSelectedLabellist = new ArrayList<UIButton>();
	private MouseListener paraMouseListner;
	private UIButton addAll;
	private UILabel label;
	private int breakid;

	private static final int GAP_H = 4;
	private static final int GAP_V = 6;
	private static final int GAP_BV = 4;
	private static final int BUTTON_HEIGHT = 20;
	private static final int WIDTH = 225;
	private static final int L_H = 18;
	private static final int LABEL_PADDING_LEFT = 4;

	public ParameterToolBarPane() {
		this.setLayout(new FlowParameterPaneLayout());

		label = new UILabel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(super.getPreferredSize().width, 18);
			}
		};
		label.setText(com.fr.design.i18n.Toolkit.i18nText("FR-Following_parameters_are_not_generated") + ":");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setBorder(BorderFactory.createEmptyBorder(0, LABEL_PADDING_LEFT, 0, 0));
		this.add(label);

		addAll = new UIButton(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Add_all"));
		this.add(addAll);

	}

	public boolean hasSelectedLabelItem() {
		return !parameterSelectedLabellist.isEmpty();
	}

	@Override
	protected String title4PopupWindow() {
		return null;
	}

	public Parameter getTargetParameter(UIButton button) {
		int index = parameterSelectedLabellist.indexOf(button);
		if (index < 0 || index > parameterList.length - 1) {
			return null;
		}
		return parameterList[index];
	}

	@Override
	public void populateBean(Parameter[] parameterArray) {
		parameterSelectedLabellist.clear();
		this.removeAll();

		this.add(label);

		if (parameterArray.length == 0) {
			this.setVisible(false);
			this.repaint();
			return;
		} else {
			this.setVisible(true);
		}

		parameterList = parameterArray;
		for (int i = 0; i < parameterList.length; i++) {
			UIButton parameterSelectedLabel = new UIButton(parameterList[i].getName());
			parameterSelectedLabellist.add(parameterSelectedLabel);
			this.add(parameterSelectedLabel);
		}

		for(UIButton parameterSelectedLabel : parameterSelectedLabellist) {
			parameterSelectedLabel.addMouseListener(paraMouseListner);
		}

		this.add(addAll);
		this.doLayout();
		this.repaint();
	}

	@Override
	public Parameter[] updateBean() {
		return parameterList;
	}

	public void setParaMouseListener(MouseListener l) {
		this.paraMouseListner = l;
	}

	public void addActionListener(ActionListener l) {
		addAll.addActionListener(l);
	}

	// ParameterToolBarPane 的布局
	private class FlowParameterPaneLayout implements LayoutManager {

		public FlowParameterPaneLayout() {
            // do nothing
		}

		public void addLayoutComponent(String name, Component comp) {
            // do nothing
		}

		public void removeLayoutComponent(Component comp) {
            // do nothing
		}

		public Dimension preferredLayoutSize(Container parent) {
			int w = parent.getWidth();

			layoutContainer(parent);

			int h= (parameterSelectedLabellist.isEmpty() ? 0 : breakid * (BUTTON_HEIGHT + GAP_V) + GAP_BV + L_H + GAP_H + addAll.getPreferredSize().height);
			return new Dimension(w, h);
		}

		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(0, 0);
		}

		public void layoutContainer(Container parent) {
			int width = parent.getWidth() == 0 ? WIDTH : parent.getWidth();
			int x = 0;
			int y = L_H + GAP_H;

			label.setBounds(0, 0, width, L_H);

			breakid = 1;
			for (UIButton tab : parameterSelectedLabellist) {
				Dimension dim = tab.getPreferredSize();
				if (x + dim.width > width) {
					breakid++;
					x = 0;
					y += (dim.height + GAP_V);
				}

				tab.setBounds(x, y, dim.width, dim.height);

				x += (dim.width + GAP_H);
			}
			addAll.setBounds(0, y + GAP_V + BUTTON_HEIGHT, width, addAll.getPreferredSize().height);

		}
	}
}