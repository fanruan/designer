package com.fr.van.chart.wordcloud.designer.data;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.data.util.function.AbstractDataFunction;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.mainframe.chart.gui.data.CalculateComboBox;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.wordcloud.data.WordCloudTableDefinition;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

/**
 * Created by Mitisky on 16/11/29.
 */
public class WordCloudPlotTableDataContentPane extends AbstractTableDataContentPane {

    private UITextField name;
    private UIComboBox wordName;
    private UIComboBox wordValue;
    private CalculateComboBox calculateCombox;

    public WordCloudPlotTableDataContentPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f, COMPONENT_WIDTH};
        double[] rowSize = { p, p, p, p};

        name = new UITextField();
        wordName = new UIComboBox();
        wordValue = new UIComboBox();
        calculateCombox = new CalculateComboBox();


        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_MultiPie_Series_Name")), name},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Word_Name")), wordName},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Word_Value")), wordValue},
                new Component[]{new UILabel(Inter.getLocText("Chart-Summary_Method")), calculateCombox}
        };

        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components,rowSize,columnSize);

        this.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0,24,0,15));
        this.add(panel, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(246,(int)this.getPreferredSize().getHeight()));

    }

    @Override
    public void populateBean(ChartCollection collection) {
        TopDefinitionProvider top = collection.getSelectedChart().getFilterDefinition();
        if (!(top instanceof WordCloudTableDefinition)) {
            return;
        }
        WordCloudTableDefinition definition = (WordCloudTableDefinition) top;

        name.setText(definition.getName());

        combineCustomEditValue(wordName, definition.getWordName());
        combineCustomEditValue(wordValue, definition.getWordValue());

        calculateCombox.populateBean((AbstractDataFunction) definition.getDataFunction());
    }

    @Override
    public void updateBean(ChartCollection ob) {
        WordCloudTableDefinition definition = new WordCloudTableDefinition();
        ob.getSelectedChart().setFilterDefinition(definition);

        definition.setName(name.getText());

        Object wname = wordName.getSelectedItem();
        Object wvalue = wordValue.getSelectedItem();

        if (wname != null) {
            definition.setWordName(wname.toString());
        }
        if (wvalue != null) {
            definition.setWordValue(wvalue.toString());
        }
        definition.setDataFunction(calculateCombox.updateBean());
    }

    /**
     * 检查 某些Box是否可用
     *
     * @param hasUse 是否使用.
     */
    public void checkBoxUse(boolean hasUse) {
        wordName.setEnabled(hasUse);
        wordValue.setEnabled(hasUse);
    }

    /**
     * 清空所有的box设置
     */
    @Override
    public void clearAllBoxList() {
        clearBoxItems(wordName);
        clearBoxItems(wordValue);
    }

    @Override
    protected void refreshBoxListWithSelectTableData(List columnNameList) {
        refreshBoxItems(wordName, columnNameList);
        refreshBoxItems(wordValue, columnNameList);
    }
}
