package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.Title;
import com.fr.chart.charttypes.BarIndependentChart;
import com.fr.chartx.attr.ChartProvider;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.chart.series.PlotStyle.ChartSelectDemoPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.ComparatorUtils;
import com.fr.stable.Constants;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;


public abstract class AbstractChartTypePane<T extends ChartProvider> extends FurtherBasicBeanPane<T> {

    private static final int ONE_LINE_NUM = 4;

    protected static final int STYLE_SHADE = 0;
    protected static final int STYLE_TRANSPARENT = 1;
    protected static final int STYLE_PLANE3D = 2;
    protected static final int STYLE_HIGHLIGHT = 3;

    protected static final int BAIDU = 0;
    protected static final int GOOGLE = 1;

    private String plotID;

    protected List<ChartImagePane> typeDemo;
    protected List<ChartImagePane> styleList;

    protected JPanel stylePane; //样式布局的面板
    private JPanel typePane;

    protected abstract String[] getTypeIconPath();

    protected abstract String[] getTypeTipName();

    protected String[] getTypeLayoutPath() {
        return new String[0];
    }

    protected String[] getTypeLayoutTipName() {
        return new String[0];
    }

    protected int lastStyleIndex = -1;
    protected boolean typeChanged = false;//图表类型是否发生变化

    protected String[] getNormalLayoutTipName() {
        return new String[]{
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_TopDownShade"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_Transparent"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_Plane3D"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_GradientHighlight")
        };
    }

    public AbstractChartTypePane() {
    }

    public void reLayout(String chartID) {
        this.plotID = chartID;

        double vs = 4;
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        typeDemo = createTypeDemoList();
        styleList = createStyleList();

        checkDemosBackground();

        typePane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(4);
        for (int i = 0; i < typeDemo.size(); i++) {
            ChartImagePane tmp = typeDemo.get(i);
            typePane.add(tmp);
            tmp.setDemoGroup(typeDemo.toArray(new ChartSelectDemoPane[typeDemo.size()]));
        }

        JPanel layoutPane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(4);
        if (styleList != null && !styleList.isEmpty()) {
            for (int i = 0; i < styleList.size(); i++) {
                ChartImagePane tmp = styleList.get(i);
                layoutPane.add(tmp);
                tmp.setDemoGroup(styleList.toArray(new ChartSelectDemoPane[styleList.size()]));
            }
        }

        double[] columnSize = {p, vs, f};
        double[] rowSize = {p, p, p, p, p, p, p};

        if (styleList != null && !styleList.isEmpty()) {
            Component[][] styleComp = new Component[][]{
                    new Component[]{new JSeparator()},
                    new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Layout"))},
                    new Component[]{layoutPane},
            };
            stylePane = TableLayoutHelper.createTableLayoutPane(styleComp, rowSize, columnSize);
            stylePane.setVisible(false);
        }

        JPanel panel = TableLayoutHelper.createTableLayoutPane(getPaneComponents(typePane), rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
    }

    protected List<ChartImagePane> createTypeDemoList() {
        return createImagePaneList(getTypeIconPath(), getTypeTipName());
    }

    protected List<ChartImagePane> createStyleList() {
        return createImagePaneList(getTypeLayoutPath(), getTypeLayoutTipName());
    }

    private List<ChartImagePane> createImagePaneList(String[] iconPaths, String[] tipNames) {
        List<ChartImagePane> list = new ArrayList<ChartImagePane>();
        int iconLen = iconPaths.length;
        int tipLen = tipNames.length;
        for (int i = 0, len = Math.min(iconLen, tipLen); i < len; i++) {
            boolean isDrawRightLine = (i != len - 1 || (i + 1) % ONE_LINE_NUM != 0);
            ChartImagePane imagePane = new ChartImagePane(iconPaths[i], tipNames[i], isDrawRightLine);
            imagePane.isPressing = (i == 0);
            list.add(imagePane);
        }
        return list;
    }

    protected Component[][] getPaneComponents(JPanel typePane) {
        return new Component[][]{
                new Component[]{typePane},
                new Component[]{stylePane}
        };
    }

    //子类覆盖
    protected <T extends Plot> T getSelectedClonedPlot() {
        return null;
    }

    protected void checkTypeChange(Plot oldPlot) {
        if (styleList != null && !styleList.isEmpty()) {
            for (int i = 0; i < typeDemo.size(); i++) {
                if (typeDemo.get(i).isPressing && i != oldPlot.getDetailType()) {
                    typeChanged = true;
                    break;
                }
                typeChanged = false;
            }
        }
    }

    protected void checkDemosBackground() {
        if (this.styleList != null && !styleList.isEmpty()) {
            for (int i = 0; i < styleList.size(); i++) {
                styleList.get(i).checkBorder();
                styleList.get(i).repaint();
            }
        }

        if (this.typeDemo != null && !typeDemo.isEmpty()) {
            for (int i = 0; i < typeDemo.size(); i++) {
                typeDemo.get(i).checkBorder();
                typeDemo.get(i).repaint();
            }
        }
    }

    /**
     * 更新整个新的Chart类型
     */
    public T updateBean() {
        return null;
    }

    //图表区属性清空
    protected void resetChart(Chart chart) {
        chart.setTitle(new Title(chart.getTitle().getTextObject()));
        chart.setBorderStyle(Constants.LINE_NONE);
        chart.setBorderColor(new Color(150, 150, 150));
        chart.setBackground(null);
    }

    /**
     * 重置
     */
    public void reset() {
    }

    /**
     * 此接口已删除，不用实现了
     * <p>
     * 获取各图表类型界面ID, 本质是plotID
     * 使用getPlotID
     *
     * @return 图表类型界面ID
     */
    @Deprecated
    protected String getPlotTypeID() {
        return null;
    }

    public String getPlotID() {
        return plotID;
    }

    /**
     * @param ob 对象
     * @return
     */
    public boolean accept(Object ob) {
        if (ob instanceof ChartProvider) {
            return ComparatorUtils.equals(((ChartProvider) ob).getID(), getPlotID());
        }
        return false;
    }

    protected boolean needsResetChart(Chart chart) {
        return chart != null
                && chart.getPlot() != null
                && chart.getPlot().getPlotStyle() != ChartConstants.STYLE_NONE;
    }

    protected JPanel getTypePane() {
        return typePane;
    }

    public ChartProvider getDefaultChart() {
        return BarIndependentChart.barChartTypes[0];
    }
}