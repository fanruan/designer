package com.fr.design.mainframe.chart.gui.data.report;

import com.fr.base.BaseFormula;
import com.fr.base.Formula;
import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Bar2DPlot;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.NormalReportDataDefinition;
import com.fr.chart.chartdata.SeriesDefinition;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class CategoryPlotReportDataContentPane extends AbstractReportDataContentPane {
	protected static final int PRE_WIDTH = 210;

    protected TinyFormulaPane categoryName;
    protected ChartDataFilterPane filterPane;
    
    public CategoryPlotReportDataContentPane(){
    	
    }

    public CategoryPlotReportDataContentPane(ChartDataPane parent) {
        initEveryPane();
        categoryName = initCategoryBox(Inter.getLocText("FR-Chart-Category_Name"));
        categoryName.setPreferredSize(new Dimension(246,30));
        categoryName.setBorder(BorderFactory.createEmptyBorder(0,24,0,7));
        this.add(categoryName, "0,0,2,0");
        filterPane = new ChartDataFilterPane(new Bar2DPlot(), parent);
        JPanel panel = TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("FR-Chart-Data_Filter"),filterPane);
        panel.setBorder(getSidesBorder());
        filterPane.setBorder(getFilterPaneBorder());
        this.add(panel, "0,6,2,4");    }
    
	protected TinyFormulaPane initCategoryBox(final String leftLabel) {
		TinyFormulaPane categoryName = new TinyFormulaPane() {
            @Override
            protected void initLayout() {
                this.setLayout(new BorderLayout(4, 0));
                
                if(StringUtils.isNotEmpty(leftLabel)) {
                	UILabel label1 = new UILabel(Inter.getLocText("FR-Chart-Category_Name"));
                	label1.setPreferredSize(new Dimension(75, 20));
                	this.add(label1, BorderLayout.WEST);
                }
                
                formulaTextField.setPreferredSize(new Dimension(100, 20));
                this.add(formulaTextField, BorderLayout.CENTER);
                this.add(formulaTextFieldButton, BorderLayout.EAST);
            }

            @Override
            protected void populateTextField(BaseFormula fm) {
                formulaTextField.setText(fm.getContent());
            }

            public void okEvent() {
                checkBoxUse();
            }
        };

        categoryName.getUITextField().getDocument().addDocumentListener(new DocumentListener() {
            public void removeUpdate(DocumentEvent e) {
                checkBoxUse();
            }

            public void insertUpdate(DocumentEvent e) {
                checkBoxUse();
            }

            public void changedUpdate(DocumentEvent e) {
                checkBoxUse();
            }
        });
        
        return categoryName;
	}

    @Override
    protected String[] columnNames() {
        return new String[]{
                Inter.getLocText("FR-Chart-Series_Name"),
                Inter.getLocText("Chart-Series_Value")
        };
    }

    public void populateBean(ChartCollection collection) {
        checkBoxUse();

        TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
        if (definition instanceof NormalReportDataDefinition) {
            NormalReportDataDefinition reportDefinition = (NormalReportDataDefinition) definition;
            if (reportDefinition.getCategoryName() != null) {
                categoryName.getUITextField().setText(reportDefinition.getCategoryName().toString());

                List list = getEntryList(reportDefinition);
                if (!list.isEmpty()) {
                    populateList(list);
                }
            }
            
            seriesPane.doLayout();
        }

        filterPane.populateBean(collection);
    }

    public void updateBean(ChartCollection collection) {
        collection.getSelectedChart().setFilterDefinition(new NormalReportDataDefinition());

        TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
        if (definition instanceof NormalReportDataDefinition) {
            NormalReportDataDefinition reportDefinition = (NormalReportDataDefinition) definition;
            
            reportDefinition.setCategoryName(canBeFormula(categoryName.getUITextField().getText()));

            List list = updateList();
            for (int i = 0; i < list.size(); i++) {
                Object[] value = (Object[]) list.get(i);
                SeriesDefinition sd = new SeriesDefinition();

                sd.setSeriesName(canBeFormula(value[0]));
                sd.setValue(canBeFormula(value[1]));
                reportDefinition.add(sd);
            }
        }
        filterPane.updateBean(collection);
    }
}