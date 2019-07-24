/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.creator;

import com.fr.base.chart.BaseChartCollection;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.chart.MiddleChartComponent;
import com.fr.design.mainframe.EastRegionContainerPane;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.NoSupportAuthorityEdit;
import com.fr.design.mainframe.cell.QuickEditorRegion;
import com.fr.design.module.DesignModuleFactory;
import com.fr.poly.PolyConstants;
import com.fr.poly.PolyDesigner;
import com.fr.poly.PolyDesigner.SelectionType;
import com.fr.poly.hanlder.ColumnOperationMouseHandler;
import com.fr.poly.hanlder.RowOperationMouseHandler;
import com.fr.report.poly.PolyChartBlock;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author richer
 * @since 6.5.4 创建于2011-5-10
 */
// 图片的命名必须符合下面的代码规范（chart类别+序号的方式） 不然读取不到指定图片
public class ChartBlockEditor extends BlockEditor<MiddleChartComponent, PolyChartBlock> {

	public ChartBlockEditor(PolyDesigner designer, ChartBlockCreator creator) {
		super(designer, creator);
		this.resolution = creator.resolution;
	}


	private void setResolution(int resolution){
		this.resolution = resolution;
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