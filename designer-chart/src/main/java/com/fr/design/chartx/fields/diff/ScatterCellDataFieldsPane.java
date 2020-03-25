package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.BubbleColumnFieldCollection;
import com.fr.design.chartx.component.AbstractSingleFilterPane;
import com.fr.design.chartx.component.CellDataSeriesXYValueCorrelationPane;
import com.fr.design.chartx.fields.AbstractCellDataFieldsPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.i18n.Toolkit;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by Wim on 2019/11/04.
 */
public class ScatterCellDataFieldsPane extends AbstractCellDataFieldsPane<BubbleColumnFieldCollection> {

    private CellDataSeriesXYValueCorrelationPane seriesXYValueCorrelationPane;
    private AbstractSingleFilterPane filterPane;


    public CellDataSeriesXYValueCorrelationPane getSeriesXYValueCorrelationPane() {
        return seriesXYValueCorrelationPane;
    }

    public void setSeriesXYValueCorrelationPane(CellDataSeriesXYValueCorrelationPane seriesXYValueCorrelationPane) {
        this.seriesXYValueCorrelationPane = seriesXYValueCorrelationPane;
    }

    @Override
    protected void initComponents() {
        filterPane = new AbstractSingleFilterPane() {
            @Override
            public String title4PopupWindow() {
                return Toolkit.i18nText("Fine-Design_Chart_Series");
            }
        };

        this.setLayout(new BorderLayout(0, 6));
        this.add(createCenterPane(), BorderLayout.NORTH);
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(new JPanel(), BorderLayout.NORTH);
        contentPane.add(filterPane, BorderLayout.CENTER);
        this.add(TableLayout4VanChartHelper.createExpandablePaneWithTitle(Toolkit.i18nText("Fine-Design_Chart_Data_Filter"), contentPane), BorderLayout.CENTER);

    }

    @Override
    protected JPanel createCenterPane() {
        JPanel normalCenter = super.createCenterPane();
        seriesXYValueCorrelationPane = new CellDataSeriesXYValueCorrelationPane();

        if (normalCenter != null) {
            JPanel panel = new JPanel(new BorderLayout(0, 6));
            panel.add(normalCenter, BorderLayout.NORTH);
            panel.add(seriesXYValueCorrelationPane, BorderLayout.CENTER);
            return panel;
        } else {
            return seriesXYValueCorrelationPane;
        }

    }

    @Override
    protected String[] fieldLabels() {
        return new String[0];
    }

    @Override
    protected TinyFormulaPane[] formulaPanes() {
        return new TinyFormulaPane[0];
    }

    @Override
    public BubbleColumnFieldCollection updateBean() {
        BubbleColumnFieldCollection collection = new BubbleColumnFieldCollection();
        seriesXYValueCorrelationPane.updateBean(collection);
        collection.setFilterProperties(filterPane.updateBean());
        return collection;
    }

    @Override
    public void populateBean(BubbleColumnFieldCollection ob) {
        seriesXYValueCorrelationPane.populateBean(ob);
        filterPane.populateBean(ob.getFilterProperties());
    }
}
