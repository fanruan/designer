package com.fr.design.mainframe.chart.gui.style;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.Border;

import com.fr.chart.chartattr.Plot;
import com.fr.design.constants.UIConstants;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.gui.ibutton.UIHeadGroup;


public abstract class ThirdTabPane<T> extends BasicBeanPane<T>{
	private static final long serialVersionUID = 2298609199400393886L;
	protected UIHeadGroup tabPane;
	protected String[] nameArray;
	public JPanel centerPane;
	public CardLayout cardLayout;
	public List<NamePane> paneList;

	protected abstract List<NamePane> initPaneList(Plot plot, AbstractAttrNoScrollPane parent);
	
	public ThirdTabPane(Plot plot, AbstractAttrNoScrollPane parent) {
		paneList = initPaneList(plot, parent);
        initAllPane();
	}

    protected void initAllPane(){
        cardLayout = new CardLayout();
		centerPane = new JPanel(cardLayout);

		nameArray = new String[paneList.size()];
		for (int i = 0; i < paneList.size(); i++) {
			NamePane np = paneList.get(i);
			nameArray[i] = np.name;
			centerPane.add(np.pane, nameArray[i]);
		}
		if (!paneList.isEmpty()) {
			tabPane = new UIHeadGroup(nameArray) {
				@Override
				public void tabChanged(int index) {
					cardLayout.show(centerPane, nameArray[index]);
				}
			};
			centerPane.setBorder(myBorder);
		}
		initLayout();
    }
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (!paneList.isEmpty()) {
			Graphics2D g2d = (Graphics2D)g;
			g2d.setColor(UIConstants.LINE_COLOR);
			g2d.drawLine(getWidth() - 2, tabPane.getPreferredSize().height, getWidth() - 2, getHeight() - 1);
		}
	}
	
	protected void initLayout() {
		this.setLayout(new BorderLayout());
		if (!paneList.isEmpty()) {
			JPanel pane = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
			pane.add(tabPane);
			this.add(pane, BorderLayout.NORTH);
		}
		this.add(centerPane, BorderLayout.CENTER);
	}

	private Border myBorder = new Border() {
		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width,int height) {
			Graphics2D g2d = (Graphics2D)g;
			g2d.setColor(UIConstants.LINE_COLOR);
			g2d.drawLine(0, 0, 0, height);
			g2d.drawLine(tabPane.getPreferredSize().width - 1, 0, width - 2, 0);
			g2d.drawLine(0, height - 1, width - 2, height - 1);
		}

		@Override
		public boolean isBorderOpaque() {
			return false;
		}

		@Override
		public Insets getBorderInsets(Component c) {
			return new Insets(2, 2, 2, 2);
		}
	};

	/**
	 * 
	 * @return 中间的内容面板的指定宽度
	 */
	protected int getContentPaneWidth() {
		return centerPane.getPreferredSize().width;
	}
	

	@Override
	public T updateBean() {
		return null;
	}
	
	protected static class NamePane {
		private String name;
		private AbstractChartTabPane pane;
		public NamePane(String name, AbstractChartTabPane pane) {
			this.name = name;
			this.pane = pane;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public AbstractChartTabPane getPane() {
			return pane;
		}
		public void setPane(AbstractChartTabPane pane) {
			this.pane = pane;
		}
		
	}
}