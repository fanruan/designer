package com.fr.design.present.dict;

import com.fr.base.BaseFormula;
import com.fr.base.BaseUtils;
import com.fr.data.impl.FormulaDictionary;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.editor.editor.FormulaEditor;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;

import com.fr.design.widget.FRWidgetFactory;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class FormulaDictPane extends FurtherBasicBeanPane<FormulaDictionary> {

    private static final int EDITOR_COLUMN = 15;
    private static final int LEFT_BORDER = 5;
    private static final int MAX_WIDTH = 30;
    private FormulaEditor keyFormulaEditor;
    private FormulaEditor valueFormulaEditor;

    public FormulaDictPane() {
        initComponents();
    }

    private void initComponents() {
        keyFormulaEditor = new FormulaEditor();
        keyFormulaEditor.setColumns(EDITOR_COLUMN);
        JPanel keyFormulaContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, LEFT_BORDER, 0));
        keyFormulaContainer.setBorder(BorderFactory.createEmptyBorder(0, -LEFT_BORDER, 0, -LEFT_BORDER));
        keyFormulaEditor.setPreferredSize(new Dimension(144, 20));
        Icon icon = BaseUtils.readIcon("/com/fr/design/images/m_insert/formula.png");
        keyFormulaContainer.add(new JLabel(icon));
        keyFormulaContainer.add(keyFormulaEditor);

        valueFormulaEditor = new FormulaEditor();
        valueFormulaEditor.setColumns(EDITOR_COLUMN);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p, p};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}};

        UILabel tag = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Formula_Dictionary_Display_Examples_Html"));
        tag.setForeground(new Color(143, 143, 146));
        tag.setPreferredSize(new Dimension(225, 80));
        JPanel t = new JPanel(new BorderLayout());
        t.add(tag, BorderLayout.CENTER);

        BaseFormula vf = BaseFormula.createFormulaBuilder().build("$$$");
        valueFormulaEditor = new FormulaEditor(StringUtils.EMPTY, vf);

        JPanel valueFormulaContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, LEFT_BORDER, 0));
        valueFormulaContainer.setBorder(BorderFactory.createEmptyBorder(0, -LEFT_BORDER, 0, -LEFT_BORDER));
        valueFormulaEditor.setPreferredSize(new Dimension(144, 20));
        valueFormulaContainer.add(new JLabel(icon));
        valueFormulaContainer.add(valueFormulaEditor);

        Component[][] components = new Component[][]{
                new Component[]{FRWidgetFactory.createLineWrapLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Actual_Value"), MAX_WIDTH, UILabel.LEFT), keyFormulaContainer},
                new Component[]{FRWidgetFactory.createLineWrapLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Display_Value"), MAX_WIDTH, UILabel.LEFT), valueFormulaContainer},
                new Component[]{tag, null}
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_LARGE, LayoutConstants.VGAP_LARGE);

        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);

    }

    public void addChangeListener(DocumentListener l) {
        keyFormulaEditor.addDocumentListener(l);
        valueFormulaEditor.addDocumentListener(l);
    }

    @Override
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Formula");
    }

    @Override
    public void populateBean(FormulaDictionary dict) {
        keyFormulaEditor.setValue(BaseFormula.createFormulaBuilder().build(dict.getProduceFormula() == null ? StringUtils.EMPTY : dict.getProduceFormula()));
        valueFormulaEditor.setValue(BaseFormula.createFormulaBuilder().build(dict.getExcuteFormula() == null ? StringUtils.EMPTY : dict.getExcuteFormula()));
    }

    @Override
    public FormulaDictionary updateBean() {
        FormulaDictionary dict = new FormulaDictionary();
        if (keyFormulaEditor.getValue() != null) {
            dict.setProduceFormula(keyFormulaEditor.getValue().getContent());
        }
        if (valueFormulaEditor.getValue() != null) {
            dict.setExcuteFormula(valueFormulaEditor.getValue().getContent());
        }

        return dict;
    }

    @Override
    public boolean accept(Object ob) {
        return ob instanceof FormulaDictionary;
    }

    @Override
    public void reset() {
        keyFormulaEditor.reset();
        valueFormulaEditor.reset();
    }
}