package com.fr.design.mainframe.chart.gui.style;

import com.fr.chart.chartglyph.GeneralInfo;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.backgroundpane.*;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Background;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 图表  属性表.背景设置 界面.(包括 无, 颜色, 图片, 渐变)
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2013-1-21 下午03:55:32
 */
public class ChartBackgroundPane extends BasicPane{
	private static final long serialVersionUID = 6955952013135176051L;
	private static final double ALPHA_V = 100.0;
	protected static final int CHART_GRADIENT_WIDTH = 150;
	protected List<BackgroundQuickPane> paneList;
	
	private UIComboBox typeComboBox;
	private UINumberDragPane transparent;
	
	public ChartBackgroundPane() {
		typeComboBox = new UIComboBox();
		final CardLayout cardlayout = new CardLayout();
		paneList = new ArrayList<BackgroundQuickPane>();
		
		initList();
		
		final JPanel centerPane = new JPanel(cardlayout) {

			@Override
			public Dimension getPreferredSize() {// AUGUST:使用当前面板的的高度
				int index = typeComboBox.getSelectedIndex();
				return new Dimension(super.getPreferredSize().width, paneList.get(index).getPreferredSize().height);
			}
		};
		for (int i = 0; i < paneList.size(); i++) {
			BackgroundQuickPane pane = paneList.get(i);
			typeComboBox.addItem(pane.title4PopupWindow());
			centerPane.add(pane, pane.title4PopupWindow());
		}
		
		typeComboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				cardlayout.show(centerPane, (String)typeComboBox.getSelectedItem());
				fireStateChanged();
			}
		});
		
		transparent = new UINumberDragPane(0, 100);


		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		
		double[] columnSize = {p, f};
		double[] rowSize = { p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{typeComboBox, null} ,
                new Component[]{centerPane, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Alpha_Degree")), null},
                new Component[]{null, transparent}
        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Fine-Design_Chart_Background"}, components,rowSize,columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);
        this.add(new JSeparator(), BorderLayout.SOUTH);
	}
	
	protected void initList() {
		paneList.add(new NullBackgroundQuickPane());
		paneList.add(new ColorBackgroundQuickPane());
		paneList.add(new ImageBackgroundQuickPane());
		paneList.add(new GradientBackgroundQuickPane(CHART_GRADIENT_WIDTH));
	}
	
	/**
     */
	private void fireStateChanged() {
		Object[] listeners = listenerList.getListenerList();
		ChangeEvent e = null;

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				if (e == null) {
					e = new ChangeEvent(this);
				}
				((ChangeListener)listeners[i + 1]).stateChanged(e);
			}
		}
	}

    /**
     * 返回标题
     * @return 标题
     */
	public String title4PopupWindow() {
		return "";
	}
	
	public void populate(GeneralInfo attr) {
		if(attr == null) {
			return;
		}
		Background background = attr.getBackground();
		double alpha = attr.getAlpha() * ALPHA_V;
		transparent.populateBean(alpha);
		for (int i = 0; i < paneList.size(); i++) {
			BackgroundQuickPane pane = paneList.get(i);
			if (pane.accept(background)) {
				pane.populateBean(background);
				typeComboBox.setSelectedIndex(i);
				return;
			}
		}
	}
	
	public void update(GeneralInfo attr) {
		if (attr == null) {
			attr = new GeneralInfo();
		}
		attr.setBackground(paneList.get(typeComboBox.getSelectedIndex()).updateBean());
		attr.setAlpha((float) (transparent.updateBean() / ALPHA_V));
	}
}