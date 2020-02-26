package com.fr.design.chartx.component;

import com.fr.base.Utils;
import com.fr.chartx.data.field.ColumnField;
import com.fr.chartx.data.field.diff.BubbleColumnField;
import com.fr.chartx.data.field.diff.BubbleColumnFieldCollection;
import com.fr.design.chartx.component.correlation.AbstractCorrelationPane;
import com.fr.design.chartx.component.correlation.FieldEditorComponentWrapper;
import com.fr.design.chartx.component.correlation.TinyFormulaPaneEditorComponent;
import com.fr.design.i18n.Toolkit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wim on 2019/11/04.
 */
public class CellDataSeriesXYValueCorrelationPane extends AbstractCorrelationPane<BubbleColumnFieldCollection> {

    @Override
    protected FieldEditorComponentWrapper[] createFieldEditorComponentWrappers() {
        return new FieldEditorComponentWrapper[]{
                new TinyFormulaPaneEditorComponent(Toolkit.i18nText("Fine-Design_Chart_Series_Name")),
                new TinyFormulaPaneEditorComponent(Toolkit.i18nText("Fine-Design_Chart_X_Axis")),
                new TinyFormulaPaneEditorComponent(Toolkit.i18nText("Fine-Design_Chart_Y_Axis")),
                new TinyFormulaPaneEditorComponent(Toolkit.i18nText("Fine-Design_Chart_Series_Value"))
        };
    }

    @Override
    protected List<Object[]> covertTBeanToTableModelList(BubbleColumnFieldCollection bubbleColumnFieldCollection) {
        List<Object[]> result = new ArrayList<>();

        List<BubbleColumnField> bubbleColumnFieldList = bubbleColumnFieldCollection.getList();
        for (BubbleColumnField field : bubbleColumnFieldList) {
            Object[] array = new Object[]{
                    field.getSeriesName().getFieldName(),
                    field.getXField().getFieldName(),
                    field.getYField().getFieldName(),
                    field.getSizeField().getFieldName()
            };
            result.add(array);
        }

        return result;
    }

    @Override
    protected void setTableModelListToTBean(List<Object[]> tableValues, BubbleColumnFieldCollection bubbleColumnFieldCollection) {
        List<BubbleColumnField> bubbleColumnFieldList = new ArrayList<>();
        for (Object[] oneLine : tableValues) {
            BubbleColumnField bubbleColumnField = new BubbleColumnField();
            ColumnField series = new ColumnField(Utils.objectToString(oneLine[0]));
            ColumnField xField = new ColumnField(Utils.objectToString(oneLine[1]));
            ColumnField yField = new ColumnField(Utils.objectToString(oneLine[2]));
            ColumnField value = new ColumnField(Utils.objectToString(oneLine[3]));
            bubbleColumnField.setSeriesName(series);
            bubbleColumnField.setXField(xField);
            bubbleColumnField.setYField(yField);
            bubbleColumnField.setSizeField(value);
            bubbleColumnFieldList.add(bubbleColumnField);
        }

        bubbleColumnFieldCollection.setList(bubbleColumnFieldList);
    }
}
