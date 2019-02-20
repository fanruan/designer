/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.creator;

import com.fr.base.BaseUtils;
import com.fr.base.ScreenResolution;
import com.fr.base.chart.BaseChart;
import com.fr.base.chart.BaseChartCollection;
import com.fr.base.chart.BaseChartGetter;
import com.fr.base.chart.BaseChartNameID;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.chart.MiddleChartComponent;
import com.fr.design.mainframe.EastRegionContainerPane;
import com.fr.design.mainframe.JSliderPane;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.NoSupportAuthorityEdit;
import com.fr.design.mainframe.cell.QuickEditorRegion;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.poly.PolyConstants;
import com.fr.poly.PolyDesigner;
import com.fr.poly.PolyDesigner.SelectionType;
import com.fr.poly.hanlder.ColumnOperationMouseHandler;
import com.fr.poly.hanlder.RowOperationMouseHandler;
import com.fr.report.poly.PolyChartBlock;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author richer
 * @since 6.5.4 创建于2011-5-10
 */
// 图片的命名必须符合下面的代码规范（chart类别+序号的方式） 不然读取不到指定图片
public class ChartBlockEditor extends BlockEditor<MiddleChartComponent, PolyChartBlock> {
	private static final int BOUND_OFF = 21;
	private static Border buttonBorder;
	private static String[][] chartsNames;
	private static BaseChartNameID[] typeName = BaseChartGetter.getStaticAllChartBaseNames();
	private int resolution = (int) (ScreenResolution.getScreenResolution()* JSliderPane.getInstance().resolutionTimes);

	static {
		buttonBorder = new UIRoundedBorder(new Color(149, 149, 149), 1, 5);
		chartsNames = new String[typeName.length][];
		for (int i = 0; i < typeName.length; i++) {
			BaseChart[] rowCharts = BaseChartGetter.getStaticChartTypes(typeName[i].getPlotID());
			chartsNames[i] = new String[rowCharts.length];
			for (int j = 0; j < rowCharts.length; j++) {
				chartsNames[i][j] = rowCharts[j].getChartName();
			}
		}
	}

	private ChartButton[] chartButtons = null;

	public ChartBlockEditor(PolyDesigner designer, ChartBlockCreator creator) {
		super(designer, creator);
		this.resolution = creator.resolution;
        //shine:和产品商量后决定把最上面一排切换按钮去掉
//		this.initNorthBarComponent();
	}


	private void setResolution(int resolution){
		this.resolution = resolution;
	}

	private void initNorthBarComponent() {

		JPanel charttypeToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
		this.add(BlockEditorLayout.TOP, charttypeToolbar);
		BaseChart chart = editComponent.getEditingChart();
		String selectedName = chart.getChartName();
		int index = 0;
		for (int i = 0; i < typeName.length; i++) {
			String[] rowCharts = chartsNames[i];
			for (int j = 0; j < rowCharts.length; j++) {
				if (ComparatorUtils.equals(selectedName, rowCharts[j])) {
					index = i;
					break;
				}
			}
		}

		String plotID = typeName[index].getPlotID();
		BaseChart[] charts = BaseChartGetter.getStaticChartTypes(plotID);
		chartButtons = new ChartButton[charts.length];
		for (int i = 0, l = charts.length; i < l; i++) {
			chartButtons[i] = new ChartButton(charts[i], charts[i].getChartName(), typeName[index].getName(), i);
			charttypeToolbar.add(chartButtons[i]);
		}

	}

    /**
     * 检查控件是否可用
     */
	public void checkChartButtonsEnable() {
//		for (ChartButton chartButton : chartButtons) {
//			chartButton.setEnabled(!DesignerMode.isAuthorityEditing());
//		}
	}

    /**
     * 刷新组件
     */
	public void refreshChartComponent() {
		editComponent.reset();
	}

	@Override
	protected MiddleChartComponent createEffective() {
		if (editComponent == null) {
			initEffective(creator.getValue().getChartCollection());
			editComponent.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					designer.setChooseType(SelectionType.CHART_INNER);
				}
			});
		}
		return editComponent;
	}

	private void initEffective(BaseChartCollection chartCollection) {
		editComponent = DesignModuleFactory.getChartComponent(chartCollection);
		editComponent.addStopEditingListener(new PropertyChangeAdapter() {// 右键的 停止编辑事件 在reset的时候触发.
			@Override
			public void propertyChange() {
				QuickEditorRegion.getInstance().populate(creator.getQuickEditor(designer));
				designer.fireTargetModified();// 整个模板的响应事件
			}

		});
		editComponent.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
	}

	@Override
	public void setBounds(Rectangle r) {
        setBounds(r.x, r.y, r.width, r.height);
		//setBounds(r.x, r.y - BOUND_OFF, r.width, r.height + BOUND_OFF);
	}

	@Override
	public PolyChartBlock getValue() {
		PolyChartBlock block = creator.getValue();
		if (editComponent != null) {
			BaseChartCollection cc = editComponent.update();
			block.setChartCollection(cc);
		}

		return block;
	}

	@Override
	protected Dimension getAddHeigthPreferredSize() {
		return new Dimension(PolyConstants.OPERATION_SIZE * 2, PolyConstants.OPERATION_SIZE);
	}

	@Override
	protected Dimension getAddWidthPreferredSize() {
		return new Dimension(PolyConstants.OPERATION_SIZE, PolyConstants.OPERATION_SIZE * 2);
	}

	@Override
	protected RowOperationMouseHandler createRowOperationMouseHandler() {
		return new RowOperationMouseHandler.ChartBlockRowOperationMouseHandler(designer, this);
	}

	@Override
	protected ColumnOperationMouseHandler createColumnOperationMouseHandler() {
		return new ColumnOperationMouseHandler.ChartBlockColumnOperationMouseHandler(designer, this);
	}

	@Override
	protected void initDataChangeListener() {

	}

	private class ChartButton extends JToggleButton {
		private BaseChart chart;

		public ChartButton(BaseChart chart, String text, String pathName, int index) {
			this.chart = chart;
			this.setToolTipText(text);
			String path = "com/fr/design/images/poly/" + pathName + '/' + pathName + '-' + index + ".png";
			Icon icon = null;
			try {
				icon = BaseUtils.readIcon(path);
			} catch (Exception e) {
				icon = BaseUtils.readIcon("com/fr/design/images/poly/normal.png");
			}
			this.setIcon(icon);
			this.setBorder(null);
			this.setMargin(null);
			this.setOpaque(false);
			this.setContentAreaFilled(false);
			this.setFocusPainted(false);
			this.setRequestFocusEnabled(false);
			this.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if (DesignerMode.isAuthorityEditing()) {
						return;
					}
					BaseChart chart = null;
					try {
						chart = (BaseChart) ChartButton.this.chart.clone();
					} catch (CloneNotSupportedException ex) {
                        FineLoggerFactory.getLogger().error(ex.getMessage(), ex);
						return;
					}
					BaseChartCollection cc = creator.getValue().getChartCollection();
					cc.switchPlot(chart.getBasePlot());
					initEffective(cc);
					creator.setValue(creator.getValue());
					ChartBlockEditor.this.removeAll();
					ChartBlockEditor.this.initComponets();
					ChartBlockEditor.this.initNorthBarComponent();
					ChartBlockEditor.this.addColumnRowListeners();
					ChartBlockEditor.this.addBoundsListener();
					ChartBlockEditor.this.initDataChangeListener();
					ChartBlockEditor.this.doLayout();
					ChartBlockEditor.this.repaint();
					QuickEditorRegion.getInstance().populate(creator.getQuickEditor(designer));
					LayoutUtils.layoutRootContainer(designer);
					designer.fireTargetModified();
					designer.repaint();
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					ChartButton.this.setBorder(buttonBorder);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					ChartButton.this.setBorder(null);
				}
			});
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(22, 22);
		}
	}

	public MiddleChartComponent getEditChartComponent() {
		return createEffective();
	}

	/**
	 * 刷新图表控件.
	 */
	public void refreshChartCompon() {
		editComponent.reset();
	}

	/**
	 * 刷新选中状态.日EC
	 */
	public void resetSelectionAndChooseState() {
		designer.setChooseType(SelectionType.CHART_INNER);
//		refreshChartComponent();// 选中之后 刷新下图表编辑层
		if (DesignerMode.isAuthorityEditing()) {
			JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
			if (jTemplate.isJWorkBook()) {
				//清参数面板
				jTemplate.removeParameterPaneSelection();
			}
			EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.AUTHORITY_EDITION_DISABLED);
			EastRegionContainerPane.getInstance().replaceAuthorityEditionPane(new NoSupportAuthorityEdit());
		}
		QuickEditorRegion.getInstance().populate(creator.getQuickEditor(designer));
	}
}