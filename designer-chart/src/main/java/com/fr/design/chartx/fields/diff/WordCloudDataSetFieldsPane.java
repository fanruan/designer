package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.WordCloudColumnFieldCollection;
import com.fr.design.chartx.fields.AbstractDataSetFieldsPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.chart.gui.data.CalculateComboBox;

import java.awt.Component;

/**
 * Created by shine on 2019/6/18.
 */
public class WordCloudDataSetFieldsPane extends AbstractDataSetFieldsPane<WordCloudColumnFieldCollection> {
    private UITextField name;
    private UIComboBox wordName;
    private UIComboBox wordValue;
    private CalculateComboBox calculateCombox;

    @Override
    protected void initComponents() {
        name = new UITextField();
        wordName = new UIComboBox();
        wordValue = new UIComboBox();
        calculateCombox = new CalculateComboBox();

        initValueAndCalComboBox(wordValue, calculateCombox);
        super.initComponents();
    }

    @Override
    protected String[] fieldLabels() {
        return new String[]{
                Toolkit.i18nText("Fine-Design_Chart_MultiPie_Series_Name"),
                Toolkit.i18nText("Fine-Design_Chart_Word_Name"),
                Toolkit.i18nText("Fine-Design_Chart_Word_Value"),
                Toolkit.i18nText("Fine-Design_Chart_Summary_Method")
        };
    }

    @Override
    protected UIComboBox[] filedComboBoxes() {
        return new UIComboBox[]{
                wordName,
                wordValue
        };
    }

    @Override
    protected Component[] fieldComponents() {
        return new Component[]{
                name,
                wordName,
                wordValue,
                calculateCombox
        };
    }

    @Override
    public void populateBean(WordCloudColumnFieldCollection ob) {
        name.setText(ob.getTargetName());
        populateField(wordName, ob.getWordName());
        populateFunctionField(wordValue, calculateCombox, ob.getWordValue());
    }

    @Override
    public WordCloudColumnFieldCollection updateBean() {
        WordCloudColumnFieldCollection result = new WordCloudColumnFieldCollection();
        result.setTargetName(name.getText());
        updateField(wordName, result.getWordName());
        updateFunctionField(wordValue, calculateCombox, result.getWordValue());
        return result;
    }
}
