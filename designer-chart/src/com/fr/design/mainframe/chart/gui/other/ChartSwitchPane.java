package com.fr.design.mainframe.chart.gui.other;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.fr.design.chart.ChartControlPane;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.general.Inter;

public class ChartSwitchPane extends AbstractAttrNoScrollPane{

	private UIButton changeButton;
	
	private ChartCollection editingChartCollection;
	
	private ChartEditPane currentChartEditPane;
	
	public ChartSwitchPane() {
		
	}
	
	@Override
	protected JPanel createContentPane() {
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		
		changeButton = new UIButton(Inter.getLocText("Switch"));
		
		pane.add(changeButton, BorderLayout.NORTH);
		
		changeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final ChartControlPane chartTypeManager = new ChartControlPane();
				chartTypeManager.populate(editingChartCollection);
				
				BasicDialog dlg = chartTypeManager.showWindow4ChartType(SwingUtilities.getWindowAncestor(new JPanel()), new DialogActionAdapter() {
					public void doOk() {
						chartTypeManager.update(editingChartCollection);//kunsnat: 确定刷新"chartSelectIndex"
						
						if(currentChartEditPane != null) {
							currentChartEditPane.populate(editingChartCollection);// 选中新Plot之后 刷新对应界面, 比如超级链接等, 然后才能update.
							currentChartEditPane.gotoPane(PaneTitleConstants.CHART_TYPE_TITLE);
							currentChartEditPane.gotoPane(PaneTitleConstants.CHART_OTHER_TITLE, PaneTitleConstants.CHART_OTHER_TITLE_CHANGE);
							currentChartEditPane.fire();
						}
					}
				});
				
				dlg.setVisible(true);
			}
		});
		
		return pane;
	}
	
	/**
	 * 注册 切换事件的改变 和超链不同.
	 * @param listener
	 */
	public void registerChartEditPane(ChartEditPane currentChartEditPane) {
		this.currentChartEditPane = currentChartEditPane;
	}
	
	public void populateBean(ChartCollection c) {
		this.editingChartCollection = c;
	}
	
	public void updateBean(ChartCollection c) {
		
	}
	
	/**
	 * 界面标题
	 * @param 返回标题
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("Chart-Switch");
	}

	@Override
	public String getIconPath() {
		return null;
	}
}