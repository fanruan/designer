package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.WordCloudColumnFieldCollection;
import com.fr.design.chartx.fields.AbstractCellDataFieldsPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.i18n.Toolkit;

import java.awt.Component;

/**
 * Created by shine on 2019/6/18.
 */
public class WordCloudCellDataFieldsPane extends AbstractCellDataFieldsPane<WordCloudColumnFieldCollection> {

    private UITextField name;
    private TinyFormulaPane wordName;
    private TinyFormulaPane wordValue;

    @Override
    protected void initComponents() {
        name = new UITextField();
        wordName = new TinyFormulaPane();
        wordValue = new TinyFormulaPane();

        super.initComponents();
    }

    @Override
    protected String[] fieldLabels() {
        return new String[]{
                Toolkit.i18nText("Fine-Design_Chart_MultiPie_Series_Name"),
                Toolkit.i18nText("Fine-Design_Chart_Word_Name"),
                Toolkit.i18nText("Fine-Design_Chart_Word_Value")
        };
    }

    @Override
    protected TinyFormulaPane[] formulaPanes() {
        return new TinyFormulaPane[]{
                wordName,
                wordValue
        };
    }

    @Override
    protected Component[] fieldComponents() {
        return new Component[]{
                name,
                wordName,
                wordValue
        };
    }

    @Override
    public void populateBean(WordCloudColumnFieldCollection ob) {
        name.setText(ob.getTargetName());
        populateField(wordName, ob.getWordName());
        populateField(wordValue, ob.getWordValue());
    }

    @Override
    public WordCloudColumnFieldCollection updateBean() {
        WordCloudColumnFieldCollection result = new WordCloudColumnFieldCollection();
        result.setTargetName(name.getText());
        updateField(wordName, result.getWordName());
        updateField(wordValue, result.getWordValue());
        return result;
    }
}
