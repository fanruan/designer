package com.fr.design.widget.ui.designer.btn;

import com.fr.data.Dictionary;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.*;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.accessibles.AccessibleDictionaryEditor;
import com.fr.design.widget.ui.designer.ButtonGroupDictPane;
import com.fr.design.widget.ui.designer.FieldEditorDefinePane;
import com.fr.design.widget.ui.designer.component.FormWidgetValuePane;
import com.fr.form.ui.ButtonGroup;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ibm on 2017/8/5.
 */
public abstract class ButtonGroupDefinePane<T extends ButtonGroup> extends FieldEditorDefinePane<T> {
    protected AccessibleDictionaryEditor dictionaryEditor;
    private ButtonGroupDictPane buttonGroupDictPane;
    private FormWidgetValuePane formWidgetValuePane;

    public ButtonGroupDefinePane(XCreator xCreator) {
        super(xCreator);
        this.initComponents();
    }

    @Override
    protected JPanel setFirstContentPane() {
        JPanel advancePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        dictionaryEditor = new AccessibleDictionaryEditor();
        buttonGroupDictPane = new ButtonGroupDictPane();
        UILabel widgetValueLabel = new UILabel(Inter.getLocText("FR-Designer-Estate_Widget_Value"));
        widgetValueLabel.setVerticalAlignment(SwingConstants.TOP);
        formWidgetValuePane = new FormWidgetValuePane(creator.toData(), false);
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Label_Name")), labelNameTextField},
                new Component[]{widgetValueLabel, formWidgetValuePane},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_DS-Dictionary")), dictionaryEditor},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_FRFont")), fontSizePane},
                new Component[]{buttonGroupDictPane, null}
        };
        double[] rowSize = {p, p, p, p, p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 3},{1, 1},{1, 1},{1, 1}};
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
        advancePane.add(panel, BorderLayout.NORTH);
        JPanel otherPane = createOtherPane();
        if (otherPane != null) {
            advancePane.add(otherPane, BorderLayout.CENTER);
        }

        return advancePane;
    }

    public JPanel createOtherPane() {
        return null;
    }


    @Override
    protected void populateSubFieldEditorBean(T e) {
        this.buttonGroupDictPane.populate(e);
        formWidgetValuePane.populate(e);
        dictionaryEditor.setValue(e.getDictionary());
        populateSubButtonGroupBean(e);
    }

    protected abstract void populateSubButtonGroupBean(T e);

    protected abstract T updateSubButtonGroupBean();

    @Override
    protected T updateSubFieldEditorBean() {
        T e = updateSubButtonGroupBean();
        this.buttonGroupDictPane.update(e);
        formWidgetValuePane.update(e);
        e.setDictionary((Dictionary) dictionaryEditor.getValue());
        return e;
    }


    @Override
    public void checkValid() throws Exception {

    }
}
