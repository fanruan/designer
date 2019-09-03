package com.fr.van.chart.designer.style.axis;

import com.fr.base.BaseFormula;
import com.fr.chart.base.ChartBaseUtils;
import com.fr.design.chart.ChartSwingUtils;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.axis.VanChartValueAxis;
import com.fr.stable.StringUtils;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.style.axis.component.VanChartMinMaxValuePane;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 数值坐标轴
 */
public class VanChartValueAxisPane extends VanChartBaseAxisPane {
    private static final long serialVersionUID = 5427425193132271246L;
    protected VanChartMinMaxValuePane minMaxValuePane;
    private UICheckBox logBox;
    private UITextField logBaseField;
    private JPanel logPane;

    public VanChartValueAxisPane(){
        this(false);
    }

    public VanChartValueAxisPane(boolean isXAxis){
        super(isXAxis);
    }

    protected JPanel createContentPane(boolean isXAxis){

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double s = TableLayout4VanChartHelper.SECOND_EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        double[] column = {f, s};
        double[] rowSize = {p,p,p,p,p,p,p,p,p,p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{createTitlePane(new double[]{p, p, p, p, p, p}, columnSize, isXAxis), null},
                new Component[]{createLabelPane(new double[]{p, p}, column), null},
                new Component[]{createMinMaxValuePane(new double[]{p, p}, columnSize), null},
                new Component[]{createLineStylePane(new double[]{p, p, p, p, p}, columnSize), null},
                new Component[]{createAxisPositionPane(new double[]{p, p, p}, columnSize, isXAxis), null},
                new Component[]{createDisplayStrategy(new double[]{p, p, p}, columnSize), null},
                new Component[]{createValueStylePane(), null},
        };

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    @Override
    protected void addOverlapGroupButton(JPanel panel) {
    }

    protected JPanel createMinMaxValuePane(double[] row, double[] col){
        JPanel panel = createCommenValuePane(row,col);
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Value_Definition"), panel);
    }
    protected JPanel createCommenValuePane(double[] row, double[] col){
        initMinMaxValuePane();

        logBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Custom_LogBase_Value"));
        logBaseField = new UITextField();
        logBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logBaseField.setEnabled(logBox.isSelected());
            }
        });

        ChartSwingUtils.addListener(logBox, logBaseField);
//        JPanel logPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
//        logPane.add(logBox);
//        logPane.add(logBaseField);


        logPane = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Log_Base_Value"), logBaseField, TableLayout4VanChartHelper.SECOND_EDIT_AREA_WIDTH);

        logPane.setBorder(BorderFactory.createEmptyBorder(0, TableLayout4VanChartHelper.COMPONENT_INTERVAL, 0, 0));

        JPanel logPaneWithCheckBox = new JPanel(new BorderLayout());

        logPaneWithCheckBox.add(logBox, BorderLayout.NORTH);
        logPaneWithCheckBox.add(logPane, BorderLayout.CENTER);

        Component[][] components = new Component[][]{
                new Component[]{minMaxValuePane, null},
                new Component[]{logPaneWithCheckBox, null},
        };

        logBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkBox();
            }
        });

        return TableLayoutHelper.createTableLayoutPane(components, row, col);
    }

    protected void initMinMaxValuePane() {
        minMaxValuePane = new VanChartMinMaxValuePane();
    }

    private void checkBox() {
        logPane.setVisible(logBox.isSelected());
    }

    /**
     * title应该是一个属性，不只是对话框的标题时用到，与其他组件结合时，也会用得到
     * @return 绥化狂标题
     */
    @Override
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Value_Axis");
    }

    public void populateBean(VanChartAxis axis){
        VanChartValueAxis valueAxis = (VanChartValueAxis)axis;
        super.populateBean(valueAxis);
        if(minMaxValuePane != null){
            minMaxValuePane.populate(valueAxis);
        }
        if(logBox != null && logBaseField != null){
            logBox.setSelected(valueAxis.isLog());
            if(valueAxis.getLogBase() != null){
                logBaseField.setText(valueAxis.getLogBase().toString());
            }
            checkBox();
        }
    }

    @Override
    public void updateBean(VanChartAxis axis) {
        VanChartValueAxis valueAxis = (VanChartValueAxis)axis;
        super.updateBean(valueAxis);
        if(minMaxValuePane != null){
            minMaxValuePane.update(valueAxis);
        }
        if(logBox != null && logBaseField != null){
            updateLog(valueAxis);
        }
    }

    public VanChartValueAxis updateBean(String axisName, int position){
        VanChartValueAxis axis = new VanChartValueAxis(axisName, position);
        updateBean(axis);
        return axis;
    }

    private void updateLog(VanChartValueAxis valueAxis) {
        if (logBaseField != null && logBox.isSelected()) {
            String increment = logBaseField.getText();
            if (StringUtils.isEmpty(increment)) {
                valueAxis.setLog(false);
                valueAxis.setLogBase(null);
            } else {
                valueAxis.setLog(true);
                BaseFormula formula = BaseFormula.createFormulaBuilder().build(increment);
                Number number = ChartBaseUtils.formula2Number(formula);
                // 界面处理防止 遇到 对数增量为小于1的值.
                if (number != null && number.doubleValue() <= 1.0) {
                    valueAxis.setLogBase(BaseFormula.createFormulaBuilder().build("2"));
                } else {
                    valueAxis.setLogBase(formula);
                }
            }
        } else {
            valueAxis.setLog(false);
        }
    }
}