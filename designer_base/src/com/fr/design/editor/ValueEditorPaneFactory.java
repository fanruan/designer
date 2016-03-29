package com.fr.design.editor;

import com.fr.base.Formula;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.editor.editor.*;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
        return createValueEditorPane(new Editor[]{new FormulaEditor(Inter.getLocText("Parameter-Formula"))},
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
     * @return 编辑器面板
     */
    public static ValueEditorPane  createBasicEditorWithoutFormulaPane(){
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
     * @return 值编辑器
     */
    public static ValueEditorPane createNumberValueEditorPane(){
        return createValueEditorPane(numberEditors(), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * 创建日期编辑器
     * @return 值编辑器
     */
    public static ValueEditorPane createDateValueEditorPane(){
        return createValueEditorPane(dateEditors(), StringUtils.EMPTY, StringUtils.EMPTY);
    }
    /**
     * 根据参数paraUseType 创建编辑器类型.
     *
     * @param paraUseType 参数类型
     * @return 值编辑器
     */
    public static ValueEditorPane createVallueEditorPaneWithUseType(int paraUseType) {
        if (paraUseType == ParameterTableModel.NO_CHART_USE) {
            return createBasicValueEditorPane();
        } else if (paraUseType == ParameterTableModel.FORM_NORMAL_USE) {
            return createFormEditorPane();
        } else {
            return createChartHotValueEditorPane(paraUseType);
        }
    }

    /**
     * 图表用的参数编辑器的ValueEditorPane
     *
     * @param paraUseType 参数类型
     * @return 值编辑器
     */
    public static ValueEditorPane createChartHotValueEditorPane(int paraUseType) {
        return createValueEditorPane(chartHotEditors(paraUseType), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * 基础的一些ValueEditorPane所用到的Editors
     *
     * @return 值编辑器
     */
    public static Editor<?>[] basicEditors() {
        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        return new Editor[]{
                new TextEditor(),
                new IntegerEditor(),
                new DoubleEditor(),
                new DateEditor(true, Inter.getLocText("Date")),
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
        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        return new Editor[]{
                new TextEditor(),
                new IntegerEditor(),
                new DoubleEditor(),
                new DateEditor(true, Inter.getLocText("Date")),
                new BooleanEditor(),
                formulaEditor,
                new WidgetNameEditor(Inter.getLocText("Widget"))
        };
    }

    /**
     * 扩展单元格的一些编辑器
     *
     * @return 值编辑器
     */
    public static Editor<?>[] extendedEditors() {
        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        return new Editor[]{
                new TextEditor(),
                new IntegerEditor(),
                new DoubleEditor(),
                new DateEditor(true, Inter.getLocText("Date")),
                new BooleanEditor(),
                formulaEditor,
                new ParameterEditor(),
                new ColumnRowEditor(Inter.getLocText("Cell"))
        };
    }

	/**
	 * 带单元格组的编辑器
	 * @return 值编辑器
	 */
	public static Editor<?>[] extendedCellGroupEditors() {
		FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
		return new Editor[]{
				new TextEditor(),
				new IntegerEditor(),
				new DoubleEditor(),
				new DateEditor(true, Inter.getLocText("Date")),
				new BooleanEditor(),
				formulaEditor,
				new ParameterEditor(),
				new ColumnRowEditor(Inter.getLocText("Cell")),
				new ColumnRowGroupEditor(Inter.getLocText("Cell_Group"))
		};
	}

	/**
	 * 只有单元格和单元格组的编辑器
	 * @return 编辑器b
	 */
	public static Editor<?>[] cellGroupEditor() {
		return new Editor[] {
				new ColumnRowEditor(Inter.getLocText("Cell")),
				new ColumnRowGroupEditor(Inter.getLocText("Cell_Group"))
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
                new NoneEditor(textEditorValue, StringUtils.isEmpty(popupName) ? Inter.getLocText("None") : popupName),
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
                new NoneEditor(textEditorValue, StringUtils.isEmpty(popupName) ? Inter.getLocText("None") : popupName),
                new DateEditor(true, Inter.getLocText("Date")),
                new FormulaEditor(Inter.getLocText("Parameter-Formula"))
        };
    }

    /**
     * 所有类型的编辑器
     *
     * @return 值编辑器
     */
    public static Editor<?>[] allEditors() {
        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
//        formulaEditor.setEnabled(true);
        return new Editor[]{
                new TextEditor(),
                new IntegerEditor(),
                new DoubleEditor(),
                new DateEditor(true, Inter.getLocText("Date")),
                new BooleanEditor(),
                formulaEditor,
                new ParameterEditor(),
                new ColumnRowEditor(Inter.getLocText("Cell")),
                new ColumnSelectedEditor(),
                //23328 allEditors中删除控件选项
//                new WidgetNameEditor(Inter.getLocText("Widget"))
        };
    }

    /**
     * 不带公式编辑器
     * @return 编辑器不带公式
     */
    public static Editor<?>[] basicEditorsWithoutFormula(){
        return new Editor[]{
                new TextEditor(),
                new IntegerEditor(),
                new DoubleEditor(),
                new DateEditor(true, Inter.getLocText("Date")),
                new BooleanEditor(),
        };
    }

    /**
     * noCRnoColumn编辑器
     *
     * @return 编辑器
     */
    public static Editor<?>[] noCRnoColumnEditors() {
        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        return new Editor[]{
                new TextEditor(),
                new IntegerEditor(),
                new DoubleEditor(),
                new DateEditor(true, Inter.getLocText("Date")),
                new BooleanEditor(),
                formulaEditor,
                new ParameterEditor(),
        };
    }

    /**
     * 数值编辑器
      * @return 编辑器
     */
    public static Editor<?>[] numberEditors() {
        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        return new Editor[]{
                new IntegerEditor(),
                new DoubleEditor(),
                formulaEditor,
                new ParameterEditor(),
        };
    }

    /**
     * 日期编辑器
     * @return 编辑器
     */
    public static Editor<?>[] dateEditors() {
        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        return new Editor[]{
                new DateEditor(true, Inter.getLocText("Date")),
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
        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        formulaEditor.setEnabled(true);
        return new Editor[]{
                new CursorEditor(),
                new TextEditor(),
                new IntegerEditor(),
                new DoubleEditor(),
                new DateEditor(true, Inter.getLocText("Date")),
                new BooleanEditor(),
                formulaEditor
        };
    }

    /**
     * 图表热点的一些编辑器
     *
     * @param paraUseType 参数类型
     * @return 值编辑器
     */
    public static Editor[] chartHotEditors(int paraUseType) {
        List<Editor> list = createEditors4Chart(paraUseType);

        list.add(new TextEditor());
        list.add(new IntegerEditor());
        list.add(new DoubleEditor());
        list.add(new DateEditor(true, Inter.getLocText("Date")));
        list.add(new BooleanEditor());

        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        formulaEditor.setEnabled(true);
        list.add(formulaEditor);

        return list.toArray(new Editor[list.size()]);
    }

    /**
     * 为图表创建编辑器.
     *
     * @param paraUseType 参数类型
     * @return 值编辑器
     */
    private static List<Editor> createEditors4Chart(int paraUseType) {
        if(paraUseType == ParameterTableModel.CHART_PIE_USE) {
            return getPieEditor();
        } else if(paraUseType == ParameterTableModel.CHART_MAP_USE) {
            return getMapEditor();
        } else if(paraUseType == ParameterTableModel.CHART_GIS_USE) {
            return getGisEditor();
        } else if(paraUseType == ParameterTableModel.CHART__XY_USE) {
            return getXYEditor();
        } else if(paraUseType == ParameterTableModel.CHART_BUBBLE_USE) {
            return getBubbbleEdtor();
        } else if(paraUseType == ParameterTableModel.CHART_NO_USE) {
            return getChartNoUseEditor();
        } else if(paraUseType == ParameterTableModel.CHART_METER_USE) {
            return getMeterEditor();
        } else if(paraUseType == ParameterTableModel.CHART_STOCK_USE) {
            return getStockEditor();
        } else if(paraUseType == ParameterTableModel.CHART_GANTT_USE) {
            return getGanttEditor();
        } else if(paraUseType == ParameterTableModel.FORM_ELEMENTCASE_USE) {
            return getFormElementCaseEditor();
        }   else if(paraUseType == ParameterTableModel.FORM_CHART_USE) {
           return getFormChartEditor();
        }
        else {
            return getChartEditor();
        }
    }

    private static List<Editor> getMeterEditor() {
        ConstantsEditor cate = new ConstantsEditor(Inter.getLocText("CategoryName"), new Formula("CATEGORY"));
        cate.setEnabled(false);
        ConstantsEditor value = new ConstantsEditor(Inter.getLocText("Chart-Series_Value"), new Formula("VALUE"));
        value.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(cate);
        lists.add(value);

        return lists;
    }

    private static List<Editor> getPieEditor() {
        ConstantsEditor series = new ConstantsEditor(Inter.getLocText("ChartF-Series_Name"), new Formula("SERIES"));
        series.setEnabled(false);
        ConstantsEditor value = new ConstantsEditor(Inter.getLocText("Chart-Series_Value"), new Formula("VALUE"));
        value.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(series);
        lists.add(value);
        return lists;
    }

    private static List<Editor> getGisEditor() {
        ConstantsEditor areaValue = new ConstantsEditor(Inter.getLocText("Area_Value"), new Formula("AREA_VALUE"));
        areaValue.setEnabled(false);
        ConstantsEditor chartAddress = new ConstantsEditor(Inter.getLocText("Chart-Address"), new Formula("ADDRESS"));
        chartAddress.setEnabled(false);
        ConstantsEditor addressName = new ConstantsEditor(Inter.getLocText("Chart-Address-Name"), new Formula("ADDRESS_NAME"));
        addressName.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(chartAddress);
        lists.add(addressName);
        lists.add(areaValue);

        return lists;
    }

    private static List<Editor> getGanttEditor() {
        ConstantsEditor projectid = new ConstantsEditor(Inter.getLocText("Chart_ProjectID"), new Formula("PROJECTID"));
        projectid.setEnabled(false);
        ConstantsEditor step = new ConstantsEditor(Inter.getLocText("Chart_Step_Name"), new Formula("STEP"));
        step.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(projectid);
        lists.add(step);

        return lists;
    }

    private static List<Editor> getXYEditor() {
        ConstantsEditor series = new ConstantsEditor(Inter.getLocText("ChartF-Series_Name"), new Formula("SERIES"));
        series.setEnabled(false);
        ConstantsEditor value = new ConstantsEditor(Inter.getLocText("Chart-Series_Value"), new Formula("VALUE"));
        value.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(series);
        lists.add(value);

        return lists;
    }

    private static List<Editor> getStockEditor() {
        List<Editor> lists = new ArrayList<Editor>();

        return lists;
    }

    private static List<Editor> getBubbbleEdtor() {
        ConstantsEditor series = new ConstantsEditor(Inter.getLocText("ChartF-Series_Name"), new Formula("SERIES"));
        series.setEnabled(false);
        ConstantsEditor value = new ConstantsEditor(Inter.getLocText("Chart-Series_Value"), new Formula("VALUE"));
        value.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(series);
        lists.add(value);

        return lists;
    }

    private static List<Editor> getChartNoUseEditor() {
        List<Editor> lists = new ArrayList<Editor>();

        return lists;
    }

    private static List<Editor> getMapEditor() {
        ConstantsEditor areaValue = new ConstantsEditor(Inter.getLocText("Area_Value"), new Formula("AREA_VALUE"));
        areaValue.setEnabled(false);
        ConstantsEditor areaName = new ConstantsEditor(Inter.getLocText("Area_Name"), new Formula("AREA_NAME"));
        areaName.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(areaName);
        lists.add(areaValue);

        return lists;
    }

    private static List<Editor> getChartEditor() {
        ConstantsEditor cate = new ConstantsEditor(Inter.getLocText("CategoryName"), new Formula("CATEGORY"));
        cate.setEnabled(false);
        ConstantsEditor series = new ConstantsEditor(Inter.getLocText("ChartF-Series_Name"), new Formula("SERIES"));
        series.setEnabled(false);
        ConstantsEditor value = new ConstantsEditor(Inter.getLocText("Chart-Series_Value"), new Formula("VALUE"));
        value.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(cate);
        lists.add(series);
        lists.add(value);

        return lists;
    }

    private static List<Editor> getFormElementCaseEditor() {

        List<Editor> lists = new ArrayList<Editor>();

        return lists;
    }

    private static List<Editor> getFormChartEditor() {
        ConstantsEditor cate = new ConstantsEditor(Inter.getLocText("CategoryName"), new Formula("CATEGORY"));
        cate.setEnabled(false);
        ConstantsEditor series = new ConstantsEditor(Inter.getLocText("ChartF-Series_Name"), new Formula("SERIES"));
        series.setEnabled(false);
        ConstantsEditor value = new ConstantsEditor(Inter.getLocText("Chart-Series_Value"), new Formula("VALUE"));
        value.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(cate);
        lists.add(series);
        lists.add(value);

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
        paneLeft.add(new UILabel(" " + Inter.getLocText("Actual_Value") + ":"), BorderLayout.NORTH);
        paneLeft.add(keyColumnPane, BorderLayout.CENTER);

        JPanel paneRight = FRGUIPaneFactory.createBorderLayout_S_Pane();
        pane.add(paneRight);
        paneRight.add(new UILabel(" " + Inter.getLocText("Display_Value") + ":"), BorderLayout.NORTH);

        paneRight.add(valueDictPane, BorderLayout.CENTER);

        return pane;
    }
}