package com.fr.design.mainframe.chart.gui.data.report;

import com.fr.base.BaseUtils;
import com.fr.base.Utils;
import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Bar2DPlot;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartdata.NormalReportDataDefinition;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;

import com.fr.stable.StringUtils;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


/**
 * 多分类 单元格数据集 界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-9-4 下午02:23:46
 */
public class CategoryPlotMoreCateReportDataContentPane extends CategoryPlotReportDataContentPane implements UIObserver{
	private static final long serialVersionUID = -1122313353777460534L;

	private JPanel boxPane;
	private UIButton addButton;

	private ArrayList<TinyFormulaPane> formualList = new ArrayList<TinyFormulaPane>();
	private UIObserverListener uiobListener = null;

	public List<TinyFormulaPane> getFormualList() {
		return formualList;
	}

	public CategoryPlotMoreCateReportDataContentPane() {

	}

	public CategoryPlotMoreCateReportDataContentPane(ChartDataPane parent) {
		initEveryPane();
		categoryName = initCategoryBox(com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Category_Name"));

		JPanel catePane = new JPanel();
		catePane.setLayout(new BorderLayout(2, 2));

		catePane.add(categoryName);

		addButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/add.png"));
		addButton.setPreferredSize(new Dimension(20, 20));
		catePane.add(addButton, BorderLayout.EAST);

		boxPane = new JPanel();
		boxPane.setLayout(new BoxLayout(boxPane, BoxLayout.Y_AXIS));

		boxPane.setBackground(Color.red);

		catePane.add(boxPane, BorderLayout.SOUTH);

		catePane.setBorder(BorderFactory.createEmptyBorder(0,24,10,6));

		this.add(catePane, "0,0,2,0");
		filterPane = new ChartDataFilterPane(new Bar2DPlot(), parent);
		JPanel panel = TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Data_Filter"),filterPane);
		panel.setBorder(getSidesBorder());
		filterPane.setBorder(getFilterPaneBorder());
		this.add(panel, "0,6,2,4");

		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addNewCatePane();
			}
		});

	}

	/**
	 * 检查box 是否应该可用状态.
	 */
	public void checkBoxUse() {
		super.checkBoxUse();
		String text = categoryName.getUITextField().getText();
		addButton.setEnabled(StringUtils.isNotEmpty(text));
	}

	private TinyFormulaPane addNewCatePane() {
		final TinyFormulaPane pane = initCategoryBox(StringUtils.EMPTY);
		pane.setPreferredSize(new Dimension(100, 16));

		pane.registerChangeListener(uiobListener);

		formualList.add(pane);

		final JPanel newButtonPane = new JPanel();
		newButtonPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		newButtonPane.add(pane);

		UIButton delButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/toolbarbtn/close.png"));
		newButtonPane.add(delButton);

		boxPane.add(newButtonPane);

		delButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boxPane.remove(newButtonPane);
				formualList.remove(pane);
				checkComponent();
				relayoutPane();
			}
		});
		delButton.registerChangeListener(uiobListener);

		checkComponent();
		relayoutPane();

		return pane;
	}

	private void checkAddButton() {
		int size = formualList.size();
		addButton.setEnabled(size < 2);
	}

	protected void checkComponent() {
		checkAddButton();
	}

	private void relayoutPane() {
		this.revalidate();
	}

	/**
	 * 给组件登记一个观察者监听事件
	 *
	 * @param listener 观察者监听事件
	 */
	public void registerChangeListener(UIObserverListener listener) {
		uiobListener = listener;
	}

	/**
	 * 组件是否需要响应添加的观察者事件
	 *
	 * @return 如果需要响应观察者事件则返回true，否则返回false
	 */
	public boolean shouldResponseChangeListener() {
		return true;
	}

	public void populateBean(ChartCollection collection) {
		super.populateBean(collection);

		formualList.clear();

		TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
		if (definition instanceof NormalReportDataDefinition) {
			NormalReportDataDefinition reportDefinition = (NormalReportDataDefinition) definition;
			int size = reportDefinition.getMoreCateSize();
			if (reportDefinition.getCategoryName() != null && size > 0) {
				for(int i = 0; i < size; i++) {
					TinyFormulaPane pane = addNewCatePane();
					pane.populateBean(Utils.objectToString(reportDefinition.getMoreCateWithIndex(i)));
				}
			}
		}

		checkAddButton();
	}

	public void updateBean(ChartCollection collection) {
		super.updateBean(collection);

		TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
		Plot plot = collection.getSelectedChart().getPlot();
		if (definition instanceof NormalReportDataDefinition) {
			NormalReportDataDefinition reportDefinition = (NormalReportDataDefinition) definition;

			reportDefinition.clearMoreCate();
			updateMoreCate(reportDefinition, plot);

		}
	}

	protected void updateMoreCate(NormalReportDataDefinition reportDefinition, Plot plot) {
		for (int i = 0, size = formualList.size(); i < size; i++) {
			TinyFormulaPane pane = formualList.get(i);
			reportDefinition.addMoreCate(canBeFormula(pane.updateBean()));
		}
	}
}