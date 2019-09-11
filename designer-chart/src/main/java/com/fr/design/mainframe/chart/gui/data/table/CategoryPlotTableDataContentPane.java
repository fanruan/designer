package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Bar2DPlot;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.NormalTableDataDefinition;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;

import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

/**
 * 属性表, 矩形类的 数据集 数据界面.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2012-12-26 下午04:48:01
 */
public class CategoryPlotTableDataContentPane extends AbstractTableDataContentPane{
	private static final long serialVersionUID = 7284078589672079657L;
	
	protected UIComboBox categoryCombox;
	protected SeriesTypeUseComboxPane seriesTypeComboxPane;
	
	public CategoryPlotTableDataContentPane() {
		
	}
	
	public CategoryPlotTableDataContentPane(ChartDataPane parent) {
		
		categoryCombox = new UIComboBox();
		JPanel categoryPane = new JPanel(new BorderLayout(4,0));
		categoryPane.setBorder(BorderFactory.createMatteBorder(0, 0, 6, 1, getBackground()));
        UILabel label1 = new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_Category")) ;
        label1.setPreferredSize(new Dimension(ChartDataPane.LABEL_WIDTH,ChartDataPane.LABEL_HEIGHT));
        categoryCombox.setPreferredSize(new Dimension(100,20));

        categoryCombox.addItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_None"));
        categoryPane.add(GUICoreUtils.createBorderLayoutPane(new Component[]{categoryCombox,null,null,label1,null}));
		categoryPane.setPreferredSize(new Dimension(246,30));
		categoryPane.setBorder(BorderFactory.createEmptyBorder(0,24,10,15));
		this.setLayout(new BorderLayout());

		this.add(categoryPane, BorderLayout.NORTH);

		this.add(getJSeparator());
		seriesTypeComboxPane = new SeriesTypeUseComboxPane(parent, new Bar2DPlot());
		this.add(seriesTypeComboxPane, BorderLayout.SOUTH);
		
		categoryCombox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				checkSeriseUse(categoryCombox.getSelectedItem() != null);
				makeToolTipUse(categoryCombox);
			}
		});
	}
	
	protected void makeToolTipUse(UIComboBox comBox) {
		if(comBox.getSelectedItem() != null) {
			comBox.setToolTipText(comBox.getSelectedItem().toString());
		} else {
			comBox.setToolTipText(null);
		}
	}
	
	/**
	 * 检查 某些Box是否可用
     * @param hasUse  是否使用.
	 */
	public void checkBoxUse(boolean hasUse) {
		categoryCombox.setEnabled(hasUse);
		checkSeriseUse(hasUse);
	}
	
	protected void checkSeriseUse(boolean hasUse) {
		if(seriesTypeComboxPane != null) {
			seriesTypeComboxPane.checkUseBox(hasUse && categoryCombox.getSelectedItem() != null);
		}
	}
	
    protected void refreshBoxListWithSelectTableData(List list) {
    	refreshBoxItems(categoryCombox, list);
        categoryCombox.addItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_None"));

    	seriesTypeComboxPane.refreshBoxListWithSelectTableData(list);
    }

    /**
     * 清空所有的box设置
     */
    public void clearAllBoxList(){
        clearBoxItems(categoryCombox);
        categoryCombox.addItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_None"));
        seriesTypeComboxPane.clearAllBoxList();
    }
	
	/**
	 * 保存界面内容到ChartCollection
	 */
	public void updateBean(ChartCollection collection) {
		seriesTypeComboxPane.updateBean(collection);
		NormalTableDataDefinition dataDefinition = (NormalTableDataDefinition)collection.getSelectedChart().getFilterDefinition();
		if(dataDefinition == null) {
			return;
		}
		Object categoryName = categoryCombox.getSelectedItem();

        if(ArrayUtils.contains(ChartConstants.getNoneKeys(), categoryName)) {
            dataDefinition.setCategoryName(StringUtils.EMPTY);
        } else {
		    dataDefinition.setCategoryName(categoryName == null ? null : categoryName.toString());
        }

	}

	/**
	 * 根据ChartCollection 更新界面
	 */
	public void populateBean(ChartCollection collection) {
		super.populateBean(collection);
		TopDefinition top = (TopDefinition)collection.getSelectedChart().getFilterDefinition();
		
		if(!(top instanceof NormalTableDataDefinition)) {
			return;
		}
		NormalTableDataDefinition data = (NormalTableDataDefinition)top;

        if(data == null || ComparatorUtils.equals(data.getCategoryName(), StringUtils.EMPTY)) {
            categoryCombox.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_None"));
        } else if(data!= null && !this.boxItemsContainsObject(categoryCombox,data.getCategoryName())){
		     categoryCombox.setSelectedItem(null);
        }else {
			combineCustomEditValue(categoryCombox, data.getCategoryName());
		}

		seriesTypeComboxPane.populateBean(collection,this.isNeedSummaryCaculateMethod());
	}

	private boolean boxItemsContainsObject(UIComboBox box,Object item){
     if(box == null){
         return false;
     }

     ComboBoxModel dataModel = box.getModel();
     for (int i = 0; i < dataModel.getSize(); i++) {
         if(ComparatorUtils.equals(dataModel.getElementAt(i),item)){
             return true;
         }
     }
     return false;
 }

    /**
     * 重新布局整个面板
     */
    public void redoLayoutPane(){
       seriesTypeComboxPane.relayoutPane(this.isNeedSummaryCaculateMethod());
    }
}