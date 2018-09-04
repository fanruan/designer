package com.fr.design.widget.ui.designer;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.component.NumberEditorValidatePane;
import com.fr.design.widget.ui.designer.component.FormWidgetValuePane;
import com.fr.form.ui.NumberEditor;


import javax.swing.*;
import java.awt.*;

public class NumberEditorDefinePane extends FieldEditorDefinePane<NumberEditor> {
    public NumberEditorDefinePane(XCreator xCreator){
        super(xCreator);
    }
    /**
     *
     */
    private static final long serialVersionUID = 8011242951911686805L;
    private WaterMarkDictPane waterMarkDictPane;
    private FormWidgetValuePane formWidgetValuePane;
    private NumberEditorValidatePane numberEditorValidatePane;



    @Override
    public String title4PopupWindow() {
        return "number";
    }

    @Override
    protected JPanel setFirstContentPane() {
        // richer:数字的允许直接编辑没有意义
        waterMarkDictPane = new WaterMarkDictPane();
        UILabel widgetValueLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Estate_Widget_Value"));
        widgetValueLabel.setVerticalAlignment(SwingConstants.TOP);
        formWidgetValuePane = new FormWidgetValuePane(creator.toData(), false);
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Label_Name")), labelNameTextField},
                new Component[]{widgetValueLabel,  formWidgetValuePane},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_WaterMark")), waterMarkDictPane},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Font_Size")), fontSizePane}
        };
        double[] rowSize = {p, p, p, p, p, p};
        double[] columnSize = {p,f};
        int[][] rowCount = {{1, 1}, {1, 3},{1, 1},{1, 1}};
        JPanel advancePane =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
        JPanel boundsPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        boundsPane.add(advancePane);
        return boundsPane;
    }

    public JPanel setValidatePane() {
        numberEditorValidatePane = new NumberEditorValidatePane();
        return numberEditorValidatePane;
    }

    @Override
    protected void populateSubFieldEditorBean(NumberEditor e) {
        formWidgetValuePane.populate(e);
        this.waterMarkDictPane.populate(e);
        numberEditorValidatePane.populate(e);
    }

    @Override
    protected NumberEditor updateSubFieldEditorBean() {
        NumberEditor ob = (NumberEditor)creator.toData();
        formWidgetValuePane.update(ob);
        this.waterMarkDictPane.update(ob);
        numberEditorValidatePane.update(ob);
        return ob;
    }

    public Object getValue(UIBasicSpinner jspinner){
        JComponent editor = jspinner.getEditor();
        if (editor instanceof UIBasicSpinner.DefaultEditor) {
            JFormattedTextField ftf = ((UIBasicSpinner.DefaultEditor) editor).getTextField();
            ftf.setColumns(10);
            JFormattedTextField.AbstractFormatter formatter = ftf.getFormatter();
           return ftf.getValue();
        }
        return null;
    }

}
