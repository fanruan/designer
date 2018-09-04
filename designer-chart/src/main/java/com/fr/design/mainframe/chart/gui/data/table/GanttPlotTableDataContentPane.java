package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.GanttPlot;
import com.fr.chart.chartdata.GanttTableDefinition;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

public class GanttPlotTableDataContentPane extends AbstractTableDataContentPane{

	private UIComboBox step;
	private UIComboBox planStart;
	private UIComboBox planEnd;
	private UIComboBox finalStart;
	private UIComboBox finalEnd;
	private UIComboBox percent;
	private UIComboBox project;
	
	public GanttPlotTableDataContentPane(ChartDataPane parent) {
		step = new UIComboBox();
		planStart = new UIComboBox();
		planEnd = new UIComboBox();
		finalStart = new UIComboBox();
		finalEnd = new UIComboBox();
		percent = new UIComboBox();
		project = new UIComboBox();

		step.setPreferredSize(new Dimension(100, 20));
		planStart.setPreferredSize(new Dimension(100, 20));
		planEnd.setPreferredSize(new Dimension(100, 20));
		finalStart.setPreferredSize(new Dimension(100, 20));
		finalEnd.setPreferredSize(new Dimension(100, 20));
		percent.setPreferredSize(new Dimension(100, 20));
		project.setPreferredSize(new Dimension(100, 20));

		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { p, f};
		double[] rowSize = { p,p,p,p,p,p,p,p,p,p};

        Component[][] components = new Component[][]{
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Step_Name")),step},
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Plan_Start")),planStart},
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Plan_End")),planEnd},
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Actual_Start")),finalStart},
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Actual_End")),finalEnd},
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_Percent")),percent},
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_Items")),project},
        }  ;
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);
        
		finalStart.addItem(GanttPlot.NONE);
		finalEnd.addItem(GanttPlot.NONE);
		percent.addItem(GanttPlot.NONE);
		project.addItem(GanttPlot.NONE);
		
		finalStart.setSelectedItem(GanttPlot.NONE);
		finalEnd.setSelectedItem(GanttPlot.NONE);
		percent.setSelectedItem(GanttPlot.NONE);
		project.setSelectedItem(GanttPlot.NONE);
		
		step.addItemListener(check);
		planStart.addItemListener(check);
		planEnd.addItemListener(check);
		
		step.addItemListener(tooltipListener);
		planStart.addItemListener(tooltipListener);
		planEnd.addItemListener(tooltipListener);
		finalStart.addItemListener(tooltipListener);
		finalEnd.addItemListener(tooltipListener);
		percent.addItemListener(tooltipListener);
		project.addItemListener(tooltipListener);
	}
	
	ItemListener check = new ItemListener() {
		
		@Override
		public void itemStateChanged(ItemEvent e) {
			checkBoxUse();
		}
	};
	
	private void checkBoxUse() {
		boolean hasStep = step.getSelectedItem() != null;
		planStart.setEnabled(hasStep);
		planEnd.setEnabled(hasStep);
		finalStart.setEnabled(hasStep);
		finalEnd.setEnabled(hasStep);
		percent.setEnabled(hasStep);
		project.setEnabled(hasStep);
		
		if(planStart.isEnabled() && planEnd.isEnabled()) {
			project.setEnabled(planStart.getSelectedItem() != null && planEnd.getSelectedItem() != null);
		}
	}
	
	@Override
	public void updateBean(ChartCollection collection) {
		GanttTableDefinition data = new GanttTableDefinition();
		
		Object resultProgress = step.getSelectedItem();
		Object resultPlanStart = planStart.getSelectedItem();
		Object resultPlanEnd = planEnd.getSelectedItem();
		Object resultFinalStart = finalStart.getSelectedItem();
		Object resultFinalEnd = finalEnd.getSelectedItem();
		Object resultPercent = percent.getSelectedItem();
		Object resultProject = project.getSelectedItem();
		
		if(resultProgress != null) {
			data.setStepString(resultProgress.toString());
		}
		if(resultPlanStart != null) {
			data.setPlanStart(resultPlanStart.toString());
		}
		if(resultPlanEnd != null) {
			data.setPlanEnd(resultPlanEnd.toString());
		}
		if(resultFinalStart != null) {
			data.setRealStart(resultFinalStart.toString());
		}
		if(resultFinalEnd != null) {
			data.setRealEnd(resultFinalEnd.toString());
		}
		if(resultPercent != null) {
			data.setProgress(resultPercent.toString());
		}
		if(resultProject != null) {
			data.setItem(resultProject.toString());
		}
		collection.getSelectedChart().setFilterDefinition(data);
	}
	
	@Override
	public void populateBean(ChartCollection collection) {
		super.populateBean(collection);
		TopDefinitionProvider top = collection.getSelectedChart().getFilterDefinition();
		if(top instanceof GanttTableDefinition) {
			GanttTableDefinition data = (GanttTableDefinition)top;
			
			combineCustomEditValue(step, data.getStepString());
			combineCustomEditValue(planStart, data.getPlanStart());
			combineCustomEditValue(planEnd, data.getPlanEnd());
			combineCustomEditValue(finalStart, data.getRealStart());
			combineCustomEditValue(finalEnd, data.getRealEnd());
			combineCustomEditValue(percent, data.getProgress());
			combineCustomEditValue(project, data.getItem());
		}
		
		checkBoxUse();
	}
	
	protected void refreshBoxListWithSelectTableData(List list) {
		refreshBoxItems(step, list);
		refreshBoxItems(planStart, list);
		refreshBoxItems(planEnd, list);
		refreshBoxItems(finalStart, list);
		refreshBoxItems(finalEnd, list);
		refreshBoxItems(percent, list);
		refreshBoxItems(project, list);
		
		finalStart.addItem(GanttPlot.NONE);
		finalEnd.addItem(GanttPlot.NONE);
		percent.addItem(GanttPlot.NONE);
		project.addItem(GanttPlot.NONE);
	}

    /**
     * 清空所有的box设置
     */
    public void clearAllBoxList(){
        clearBoxItems(step);
        clearBoxItems(planStart);
        clearBoxItems(planEnd);
        clearBoxItems(finalStart);
        clearBoxItems(finalEnd);
        clearBoxItems(percent);
        clearBoxItems(project);

        finalStart.addItem(GanttPlot.NONE);
        finalEnd.addItem(GanttPlot.NONE);
        percent.addItem(GanttPlot.NONE);
        project.addItem(GanttPlot.NONE);
    }

}