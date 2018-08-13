package com.fr.design.editor;

import com.fr.base.BaseFormula;
import com.fr.design.editor.editor.BooleanEditor;
import com.fr.design.editor.editor.ColumnRowEditor;
import com.fr.design.editor.editor.ColumnRowGroupEditor;
import com.fr.design.editor.editor.ColumnSelectedEditor;
import com.fr.design.editor.editor.ConstantsEditor;
import com.fr.design.editor.editor.CursorEditor;
import com.fr.design.editor.editor.DateEditor;
import com.fr.design.editor.editor.DoubleEditor;
import com.fr.design.editor.editor.Editor;
import com.fr.design.editor.editor.FormulaEditor;
import com.fr.design.editor.editor.IntegerEditor;
import com.fr.design.editor.editor.NoneEditor;
import com.fr.design.editor.editor.ParameterEditor;
import com.fr.design.editor.editor.SpinnerIntegerEditor;
import com.fr.design.editor.editor.TextEditor;
import com.fr.design.editor.editor.WidgetNameEditor;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.layout.FRGUIPaneFactory;

import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ValueEditorPaneFactory {

    /**
     * 创建带编辑器的ValueEditorPane
     *
     * @param editors 自定义的编辑器
     * @return 返回pane
     */
    public static ValueEditorPane createValueEditorPane(Editor<?>[] editors) {
        return createValueEditorPane(editors, StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * 创建编辑器 名称 弹出的ValueEditorPane
     *
     * @param editors         编辑器
     * @param popupName       弹出的名字
     * @param textEditorValue 值
     * @return 返回pane
     */
    public static ValueEditorPane createValueEditorPane(Editor<?>[] editors, String popupName, String textEditorValue) {
        return new ValueEditorPane(editors, popupName, textEditorValue);
    }

    /**
     * 创建编辑器 名称 弹出的ValueEditorPane
     *
     * @param editors             编辑器
     * @param popupName           弹出的名字
     * @param textEditorValue     值
     * @param editor_center_width 编辑器主体的宽度
     * @return 返回pane
     */
    public static ValueEditorPane createValueEditorPane(Editor<?>[] editors, String popupName, String textEditorValue, int editor_center_width) {
        return new ValueEditorPane(editors, popupName, textEditorValue, editor_center_width);
    }

    /**
     * 创建基本的值编辑器面板
     *
     * @return 返回值编辑器面板
     */
    public static ValueEditorPane createBasicValueEditorPane() {
        return createValueEditorPane(basicEditors(), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * 创建公式编辑器面板
     *
     * @return 返回公式编辑器面板
     */
    public static ValueEditorPane createFormulaValueEditorPane() {
        return createValueEditorPane(new Editor[]{new FormulaEditor(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Parameter-Formula"))},
                StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * 创建基本的值编辑器面板
     *
     * @param editor_center_width 指定值编辑器的主体宽度
     * @return 返回值编辑器面板
     */
    public static ValueEditorPane createBasicValueEditorPane(int editor_center_width) {
        return createValueEditorPane(basicEditors(), StringUtils.EMPTY, StringUtils.EMPTY, editor_center_width);
    }

    /**
     * Process用的editorPane
     *
     * @return 值编辑器面板
     */
    public static ValueEditorPane createFormEditorPane() {
        return createValueEditorPane(formEditors(), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * StoreProced用的EditorPane
     *
     * @return 值编辑器面板
     */
    public static ValueEditorPane createStoreProcedValueEditorPane() {
        return createValueEditorPane(StoreProcedureEditors(), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * 扩展的ValueEditorPane
     *
     * @return 值编辑器面板
     */
    public static ValueEditorPane createExtendedValueEditorPane() {
        return createValueEditorPane(extendedEditors(), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * URL使用的ValueEditorPane
     *
     * @param popupName       弹出的名字
     * @param textEditorValue 编辑器值
     * @return 值编辑器返回
     */
    public static ValueEditorPane createURLValueEditorPane(String popupName, String textEditorValue) {
        return createValueEditorPane(URLEditors(popupName, textEditorValue), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * 创建日期的ValueEditorPane
     *
     * @param popupName       名字
     * @param textEditorValue 值
     * @return 值编辑器面板
     */
    public static ValueEditorPane createDateValueEditorPane(String popupName, String textEditorValue) {
        return createValueEditorPane(dateEditors(popupName, textEditorValue), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * 带有所有编辑器的ValueEditorPane
     *
     * @return 值编辑器面板
     */
    public static ValueEditorPane createAllValueEditorPane() {
        return createValueEditorPane(allEditors(), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * 创建不带公式面板的pane
     *
     * @return 编辑器面板
     */
    public static ValueEditorPane createBasicEditorWithoutFormulaPane() {
        return createValueEditorPane(basicEditorsWithoutFormula(), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * 创建NoCRNoColumn
     *
     * @return 值编辑器
     */
    public static ValueEditorPane createNoCRNoColumnValueEditorPane() {
        return createValueEditorPane(noCRnoColumnEditors(), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * 创建数值编辑器
     *
     * @return 值编辑器
     */
    public static ValueEditorPane createNumberValueEditorPane() {
        return createValueEditorPane(numberEditors(), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * 创建日期编辑器
     *
     * @return 值编辑器
     */
    public static ValueEditorPane createDateValueEditorPane() {
        return createValueEditorPane(dateEditors(), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * 根据参数paraUseType 创建编辑器类型.
     *
     * @param paraUseType 参数类型
     * @return 值编辑器
     */
    public static ValueEditorPane createVallueEditorPaneWithUseType(int paraUseType) {
       return createVallueEditorPaneWithUseType(paraUseType, null);
    }

    public static ValueEditorPane createVallueEditorPaneWithUseType(int paraUseType, HashMap hyperLinkEditorMap) {
        if (paraUseType == ParameterTableModel.NO_CHART_USE) {
            return createBasicValueEditorPane();
        } else if (paraUseType == ParameterTableModel.FORM_NORMAL_USE) {
            return createFormEditorPane();
        } else {
            return createChartHotValueEditorPane(hyperLinkEditorMap);
        }
    }

    /**
     * 图表用的参数编辑器的ValueEditorPane
     *
     * @param hyperLinkEditorMap 超链下拉参数类型
     * @return 值编辑器
     */
    public static ValueEditorPane createChartHotValueEditorPane(HashMap hyperLinkEditorMap) {
        return createValueEditorPane(chartHotEditors(hyperLinkEditorMap), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * 基础的一些ValueEditorPane所用到的Editors
     *
     * @return 值编辑器
     */
    public static Editor<?>[] basicEditors() {
        FormulaEditor formulaEditor = new FormulaEditor(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Parameter-Formula"));
        return new Editor[]{
                new TextEditor(),
                new SpinnerIntegerEditor(),
                new DoubleEditor(),
                new DateEditor(true, com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Date")),
                new BooleanEditor(),
                formulaEditor
        };
    }

    /**
     * 表单的一些编辑器.
     *
     * @return 值编辑器
     */
    public static Editor<?>[] formEditors() {
        FormulaEditor formulaEditor = new FormulaEditor(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Parameter-Formula"));
        return new Editor[]{
                new TextEditor(),
                new IntegerEditor(),
                new DoubleEditor(),
                new DateEditor(true, com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Date")),
                new BooleanEditor(),
                formulaEditor,
                new WidgetNameEditor(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Widget"))
        };
    }

    /**
     * 扩展单元格的一些编辑器
     *
     * @return 值编辑器
     */
    public static Editor<?>[] extendedEditors() {
        FormulaEditor formulaEditor = new FormulaEditor(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Parameter-Formula"));
        return new Editor[]{
                new TextEditor(),
                new IntegerEditor(),
                new DoubleEditor(),
                new DateEditor(true, com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Date")),
                new BooleanEditor(),
                formulaEditor,
                new ParameterEditor(),
                new ColumnRowEditor(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Cell"))
        };
    }

    /**
     * 带单元格组的编辑器
     *
     * @return 值编辑器
     */
    public static Editor<?>[] extendedCellGroupEditors() {
        FormulaEditor formulaEditor = new FormulaEditor(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Parameter-Formula"));
        return new Editor[]{
                new TextEditor(),
                new IntegerEditor(),
                new DoubleEditor(),
                new DateEditor(true, com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Date")),
                new BooleanEditor(),
                formulaEditor,
                new ParameterEditor(),
                new ColumnRowEditor(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Cell")),
                new ColumnRowGroupEditor(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Cell_Group"))
        };
    }

    /**
     * 只有单元格和单元格组的编辑器
     *
     * @return 编辑器b
     */
    public static Editor<?>[] cellGroupEditor() {
        return new Editor[]{
                new ColumnRowEditor(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Cell")),
                new ColumnRowGroupEditor(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Cell_Group"))
        };
    }

    /**
     * URL的一些编辑器.
     *
     * @param popupName       名字
     * @param textEditorValue 值
     * @return 值编辑器
     */
    public static Editor<?>[] URLEditors(String popupName, String textEditorValue) {
        return new Editor[]{
                new NoneEditor(textEditorValue, StringUtils.isEmpty(popupName) ? com.fr.design.i18n.Toolkit.i18nText("FR-Designer_None") : popupName),
                new TextEditor()
        };
    }

    /**
     * 日期类型的一些编辑器
     *
     * @param popupName       名字
     * @param textEditorValue 值
     * @return 值编辑器
     */
    public static Editor<?>[] dateEditors(String popupName, String textEditorValue) {
        return new Editor[]{
                new NoneEditor(textEditorValue, StringUtils.isEmpty(popupName) ? com.fr.design.i18n.Toolkit.i18nText("FR-Designer_None") : popupName),
                new DateEditor(true, com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Date")),
                new FormulaEditor(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Parameter-Formula"))
        };
    }

    /**
     * 所有类型的编辑器
     *
     * @return 值编辑器
     */
    public static Editor<?>[] allEditors() {
        FormulaEditor formulaEditor = new FormulaEditor(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Parameter-Formula"));
//        formulaEditor.setEnabled(true);
        return new Editor[]{
                new TextEditor(),
                new IntegerEditor(),
                new DoubleEditor(),
                new DateEditor(true, com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Date")),
                new BooleanEditor(),
                formulaEditor,
                new ParameterEditor(),
                new ColumnRowEditor(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Cell")),
                new ColumnSelectedEditor(),
                //23328 allEditors中删除控件选项
//                new WidgetNameEditor(com.fr.design.i18n.Toolkit.i18nText("Widget"))
        };
    }

    /**
     * 不带公式编辑器
     *
     * @return 编辑器不带公式
     */
    public static Editor<?>[] basicEditorsWithoutFormula() {
        return new Editor[]{
                new TextEditor(),
                new IntegerEditor(),
                new DoubleEditor(),
                new DateEditor(true, com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Date")),
                new BooleanEditor(),
        };
    }

    /**
     * noCRnoColumn编辑器
     *
     * @return 编辑器
     */
    public static Editor<?>[] noCRnoColumnEditors() {
        FormulaEditor formulaEditor = new FormulaEditor(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Parameter-Formula"));
        return new Editor[]{
                new TextEditor(),
                new IntegerEditor(),
                new DoubleEditor(),
                new DateEditor(true, com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Date")),
                new BooleanEditor(),
                formulaEditor,
                new ParameterEditor(),
        };
    }

    /**
     * 数值编辑器
     *
     * @return 编辑器
     */
    public static Editor<?>[] numberEditors() {
        FormulaEditor formulaEditor = new FormulaEditor(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Parameter-Formula"));
        return new Editor[]{
                new IntegerEditor(),
                new DoubleEditor(),
                formulaEditor,
                new ParameterEditor(),
        };
    }

    /**
     * 日期编辑器
     *
     * @return 编辑器
     */
    public static Editor<?>[] dateEditors() {
        FormulaEditor formulaEditor = new FormulaEditor(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Parameter-Formula"));
        return new Editor[]{
                new DateEditor(true, com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Date")),
                formulaEditor,
                new ParameterEditor(),
        };
    }

    /**
     * 存储的一些编辑器
     *
     * @return 存储过程的编辑器
     */
    public static Editor<?>[] StoreProcedureEditors() {
        FormulaEditor formulaEditor = new FormulaEditor(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Parameter-Formula"));
        formulaEditor.setEnabled(true);
        return new Editor[]{
                new CursorEditor(),
                new TextEditor(),
                new IntegerEditor(),
                new DoubleEditor(),
                new DateEditor(true, com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Date")),
                new BooleanEditor(),
                formulaEditor
        };
    }

    /**
     * 图表热点的一些编辑器
     *
     * @return 值编辑器
     */
    public static Editor[] chartHotEditors(HashMap hyperLinkEditorMap) {
        List<Editor> list = createEditors4Chart(hyperLinkEditorMap);

        list.add(new TextEditor());
        list.add(new IntegerEditor());
        list.add(new DoubleEditor());
        list.add(new DateEditor(true, com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Date")));
        list.add(new BooleanEditor());

        FormulaEditor formulaEditor = new FormulaEditor(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Parameter-Formula"));
        formulaEditor.setEnabled(true);
        list.add(formulaEditor);

        return list.toArray(new Editor[list.size()]);
    }

    /**
     * 为图表创建编辑器.
     *
     * @return 值编辑器
     */
    private static List<Editor> createEditors4Chart(HashMap hyperLinkEditorMap) {
        List<Editor> lists = new ArrayList<Editor>();
        if (hyperLinkEditorMap == null) {
            return lists;
        }
        Iterator<Map.Entry<String, BaseFormula>> entries = hyperLinkEditorMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, BaseFormula> entry = entries.next();
            ConstantsEditor editor = new ConstantsEditor(entry.getKey(), entry.getValue());
            editor.setEnabled(false);
            lists.add(editor);
        }
        return lists;
    }

    /**
     * 产生一个实际值和显示值的面板
     *
     * @param keyColumnPane 实际值
     * @param valueDictPane 显示值
     * @return 产生一个实际值和显示值的面板
     */
    public static JPanel createKeyAndValuePane(ValueEditorPane keyColumnPane, ValueEditorPane valueDictPane) {
        JPanel pane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();

        JPanel paneLeft = FRGUIPaneFactory.createBorderLayout_S_Pane();
        pane.add(paneLeft);
        paneLeft.add(new UILabel(" " + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Actual_Value") + ":"), BorderLayout.NORTH);
        paneLeft.add(keyColumnPane, BorderLayout.CENTER);

        JPanel paneRight = FRGUIPaneFactory.createBorderLayout_S_Pane();
        pane.add(paneRight);
        paneRight.add(new UILabel(" " + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Display_Value") + ":"), BorderLayout.NORTH);

        paneRight.add(valueDictPane, BorderLayout.CENTER);

        return pane;
    }
}