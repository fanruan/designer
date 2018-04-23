package com.fr.design.widget.ui;

import javax.swing.JPanel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.widget.component.NumberEditorValidatePane;
import com.fr.form.ui.NumberEditor;

import java.awt.BorderLayout;

public class NumberEditorDefinePane extends FieldEditorDefinePane<NumberEditor> {
    /**
     * FieldEditorDefinePane
     */
    private static final long serialVersionUID = 8011242951911686805L;

    private WaterMarkDictPane waterMarkDictPane;
    private NumberEditorValidatePane numberEditorValidatePane;

    public NumberEditorDefinePane() {
    }


    @Override
    protected String title4PopupWindow() {
        return "number";
    }

    @Override
    protected JPanel setFirstContentPane() {
        JPanel content = FRGUIPaneFactory.createBorderLayout_S_Pane();
        waterMarkDictPane = new WaterMarkDictPane();
        content.add(waterMarkDictPane, BorderLayout.CENTER);
        return content;
    }

    public JPanel setValidatePane() {
        numberEditorValidatePane = new NumberEditorValidatePane();
        return numberEditorValidatePane;
    }

    @Override
    protected void populateSubFieldEditorBean(NumberEditor e) {
        this.numberEditorValidatePane.populate(e);
        this.waterMarkDictPane.populate(e);
    }

    @Override
    protected NumberEditor updateSubFieldEditorBean() {
        NumberEditor ob = new NumberEditor();
        this.numberEditorValidatePane.update(ob);
        this.waterMarkDictPane.update(ob);
        return ob;
    }


}