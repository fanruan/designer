package com.fr.design.designer.creator;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ContainerListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class FormCardPane extends JComponent implements SwingConstants {

	private boolean showTab;
	private int tabPlace;
	private JPanel tabPane;
	private JPanel cardPane;
	private CardLayout layout;
	private java.util.List<Component> tabComponent = new java.util.ArrayList<Component>();
	private int showIndex;

	public FormCardPane(boolean showTab, int tabPlace) {
		this.showTab = showTab;
		this.tabPlace = tabPlace;
		initTabComponent();
		initCardComponent();
		setLayout(new FormCardLayout());
		this.add(tabPane);
		this.add(cardPane);
	}

	public FormCardPane(ContainerListener containerListener) {
		this(true, TOP);
		cardPane.addContainerListener(containerListener);
	}

	private void initTabComponent() {
		tabPane = new JPanel();
		tabPane.setOpaque(true);
		tabPane.setBorder(BorderFactory.createLineBorder(Color.pink));
		tabPane.setBackground(Color.WHITE);
	}

	private void initCardComponent() {
		cardPane = new JPanel();
		cardPane.setBorder(BorderFactory.createLineBorder(Color.orange));
		layout = new CardLayout();
		cardPane.setLayout(layout);
	}

	public void setSelectedIndex(int showIndex) {
		this.setSelectedComponent(this.getComponentAt(showIndex));
	}

	public int getTabCount() {
		return cardPane.getComponentCount();
	}

	@Override
	public void add(Component comp, Object constraints) {
		this.add(comp, constraints, -1);
	}

	@Override
	public void removeAll() {
		cardPane.removeAll();
		tabComponent.clear();
	}

	private void reSetTabComponent() {
		if (tabPlace == TOP || tabPlace == BOTTOM) {
			int totalWidth = 0;
			for (Component comp : tabComponent) {
				totalWidth += comp.getWidth();
			}
			showTabComponent(totalWidth > tabPane.getWidth());

		} else if (tabPlace == LEFT || tabPlace == RIGHT) {
			int totalHeight = 0;
			for (Component comp : tabComponent) {
				totalHeight += comp.getHeight();
			}
			showTabComponent(totalHeight > tabPane.getHeight());
		}
	}

	private void showTabComponent(boolean showButton) {
		tabPane.removeAll();
		if (this.showIndex < 0 || this.showIndex >= tabComponent.size()) {
			this.showIndex = 0;
		}
		if (tabPlace == TOP || tabPlace == BOTTOM) {
			tabPane.setLayout(new FlowLayout(FlowLayout.LEFT));

		} else if (tabPlace == LEFT || tabPlace == RIGHT) {
			tabPane.setLayout(new BoxLayout(tabPane, BoxLayout.Y_AXIS));
		} else {
			return;
		}
		
		for (Component comp : tabComponent) {
			tabPane.add(comp);
		}
		tabPane.repaint();
	}

	@Override
	public void add(Component comp, Object constraints, int index) {
		if (!(comp instanceof XCreator)) {
			return;
		}
		JComponent tabComp = new JPanel();
		if (constraints instanceof String) {
			tabComp = new UILabel((String) constraints);

		} else if (constraints instanceof Icon) {
			tabComp = new UILabel((Icon) constraints);
		} else {
			return;
		}
		tabComp.setOpaque(true);
		tabComp.setBorder(BorderFactory.createLineBorder(Color.red));
		cardPane.add(comp, ((((XCreator) comp).toData())).getWidgetName(), index);
		if (index == -1) {
			tabComponent.add(tabComp);
		} else {
			tabComponent.add(index, tabComp);
		}

		reSetTabComponent();
	}

	public void setTabPlacement(int tabPlace) {
		this.tabPlace = tabPlace;
	}

	public Component getComponentAt(int i) {
		return cardPane.getComponent(i);
	}

	public void setSelectedComponent(Component child) {
		if (child instanceof XCreator) {
			int order = cardPane.getComponentZOrder(child);
			if (order == -1 || order == showIndex) {
				return;
			}
			tabComponent.get(showIndex).setBackground(null);
			this.showIndex = order;
			tabComponent.get(showIndex).setBackground(Color.orange);
			layout.show(cardPane, ((((XCreator) child).toData())).getWidgetName());
		}
	}

	class FormCardLayout implements LayoutManager {

		@Override
		public void addLayoutComponent(String name, Component comp) {
			// TODO Auto-generated method stub

		}

		@Override
		public void layoutContainer(Container parent) {
			Insets inset = parent.getInsets();
			int parentWidth = parent.getWidth() - inset.left - inset.right;
			int parentHeight = parent.getHeight() - inset.top - inset.bottom;
			if (showTab) {
				if (tabPlace == TOP || tabPlace == BOTTOM) {
					int height = getTabPaneHeight();
					tabPane.setSize(parentWidth, height);
					cardPane.setSize(parentWidth, parentHeight - height);
					if (tabPlace == TOP) {
						tabPane.setLocation(inset.left, inset.top);
						cardPane.setLocation(inset.left, inset.top + height);
					} else {
						cardPane.setLocation(inset.left, inset.top);
						tabPane.setLocation(inset.left, inset.top + parentHeight - height);
					}
				} else if (tabPlace == LEFT || tabPlace == RIGHT) {
					int width = getTabPaneWidth();
					tabPane.setSize(width, parentHeight);
					cardPane.setSize(parentWidth - width, parentHeight);
					if (tabPlace == LEFT) {
						tabPane.setLocation(inset.left, inset.top);
						cardPane.setLocation(inset.left + width, inset.top);
					} else {
						cardPane.setLocation(inset.left, inset.top);
						tabPane.setLocation(inset.left + parentWidth - width, inset.top);
					}
				}

			} else {
				tabPane.setVisible(false);
				cardPane.setSize(parent.getWidth(), parent.getHeight());
				cardPane.setLocation(0, 0);
			}
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void removeLayoutComponent(Component comp) {
			// TODO Auto-generated method stub

		}

	}

	public int getTabPaneHeight() {
		return 22;
	}

	public int getTabPaneWidth() {
		return 40;
	}

	public void addCreatorListener(ContainerListener containerListener) {
		
	}

	public void setShowTab(boolean showTab) {
		this.showTab = showTab;
	}

}