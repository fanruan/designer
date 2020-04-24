package com.fr.van.chart.column;

import com.fr.base.background.ImageBackground;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.backgroundpane.ImageBackgroundQuickPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.utils.gui.UIComponentUtils;
import com.fr.design.widget.FRWidgetFactory;
import com.fr.plugin.chart.base.AttrSeriesImageBackground;
import com.fr.plugin.chart.column.VanChartColumnPlot;
import com.fr.stable.Constants;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.component.border.VanChartBorderPane;
import com.fr.van.chart.designer.component.border.VanChartBorderWithRadiusPane;
import com.fr.van.chart.designer.style.series.VanChartAbstractPlotSeriesPane;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 新条形图系列界面
 */
public class VanChartColumnSeriesPane extends VanChartAbstractPlotSeriesPane {

    private static final long serialVersionUID = -8875943419420081420L;
    private UIButtonGroup<Integer> isFixedWidth;//是否固定宽度
    private UISpinner columnWidth;//宽度

    private UINumberDragPane categoryGap;//分类间隔
    private UINumberDragPane seriesGap;//系列间隔
    private UIButtonGroup<Integer> isFillWithImage;//是否使用图片填充
    private ImageBackgroundQuickPane imagePane;//填充图片选择界面

    public VanChartColumnSeriesPane(ChartStylePane parent, Plot plot) {
        super(parent, plot);
    }

    protected JPanel getContentInPlotType() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f};
        double[] rowSize = {p,p,p,p,p,p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{createSeriesStylePane(new double[]{p,p}, new double[]{f,e})},
                new Component[]{createBorderPane()},
                new Component[]{createStackedAndAxisPane()},
                new Component[]{createTrendLinePane()},
        };

        contentPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);

        return contentPane;
    }

    //边框（有圆角）
    protected VanChartBorderPane createDiffBorderPane() {
        return new VanChartBorderWithRadiusPane();
    }

    private JPanel createSeriesStylePane(double[] row, double[] col) {
        isFixedWidth = new UIButtonGroup<Integer>(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_YES"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_NO")});
        columnWidth = new UISpinner(0,1000,1,0);
        columnWidth.setBorder(BorderFactory.createEmptyBorder(0, (int)TableLayout4VanChartHelper.DESCRIPTION_AREA_WIDTH + TableLayout4VanChartHelper.COMPONENT_INTERVAL,0,0));
        seriesGap = new UINumberDragPane(-100, 100);
        categoryGap = new UINumberDragPane(0, 100);
        isFillWithImage = new UIButtonGroup<Integer>(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_YES"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_NO")});
        imagePane = new ImageBackgroundQuickPane(false);
        imagePane.setBorder(BorderFactory.createEmptyBorder(0,(int)TableLayout4VanChartHelper.DESCRIPTION_AREA_WIDTH + TableLayout4VanChartHelper.COMPONENT_INTERVAL,0,0));

        JPanel panel1 = new JPanel(new BorderLayout());
        JPanel isFixedWidthPane = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Fixed_Column_Width"),isFixedWidth);
        isFixedWidthPane.setBorder(BorderFactory.createEmptyBorder(0,0,6,0));
        panel1.add(isFixedWidthPane, BorderLayout.NORTH);
        panel1.add(columnWidth, BorderLayout.CENTER);

        Component[][] components2 = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Gap_Series")),seriesGap},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Gap_Category")),categoryGap},
        };
        JPanel panel2 = TableLayout4VanChartHelper.createGapTableLayoutPane(components2, row, col);

        UILabel fillImageLabel = FRWidgetFactory.createLineWrapLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Filled_With_Image"));
        Component[][] components3 = new Component[][]{
                new Component[]{fillImageLabel, UIComponentUtils.wrapWithBorderLayoutPane(isFillWithImage)},
        };
        JPanel panel3 = TableLayout4VanChartHelper.createGapTableLayoutPane(components3, row, col);

        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.add(panel1, BorderLayout.NORTH);
        panel.add(panel2, BorderLayout.CENTER);
        panel.add(panel3, BorderLayout.SOUTH);

        JPanel borderPane = new JPanel(new BorderLayout());
        borderPane.add(panel, BorderLayout.NORTH);
        borderPane.add(imagePane, BorderLayout.CENTER);
        isFixedWidth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkColumnWidth();
            }
        });
        isFillWithImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkImagePane();
            }
        });
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Widget_Style"), borderPane);
    }

    private void checkAll() {
        checkColumnWidth();
        checkImagePane();
    }

    private void checkColumnWidth() {
        boolean b = isFixedWidth.getSelectedIndex() == 0;
        columnWidth.setVisible(b);
        seriesGap.setEnabled(!b);
    }

    private void checkImagePane() {
        imagePane.setVisible(isFillWithImage.getSelectedIndex() == 0);
    }

    public void populateBean(Plot plot) {
        if(plot == null) {
            return;
        }
        super.populateBean(plot);

        if(plot instanceof VanChartColumnPlot){
            VanChartColumnPlot columnPlot4VanChart = (VanChartColumnPlot)plot;

            isFixedWidth.setSelectedIndex(columnPlot4VanChart.isFixedWidth() ? 0 : 1);
            columnWidth.setValue(columnPlot4VanChart.getColumnWidth());
            categoryGap.populateBean(columnPlot4VanChart.getCategoryIntervalPercent());
            seriesGap.populateBean(columnPlot4VanChart.getSeriesOverlapPercent());
            isFillWithImage.setSelectedIndex(columnPlot4VanChart.isFilledWithImage() ? 0 : 1);
            ConditionAttr defaultAttr = plot.getConditionCollection().getDefaultAttr();

            if(columnPlot4VanChart.isFilledWithImage()){
                AttrSeriesImageBackground attrSeriesImageBackground = (AttrSeriesImageBackground)defaultAttr.getExisted(AttrSeriesImageBackground.class);
                if(attrSeriesImageBackground != null){
                    imagePane.populateBean(attrSeriesImageBackground.getSeriesBackground());
                }
            }
        }
        checkAll();
    }

    public void updateBean(Plot plot) {
        if(plot == null) {
            return;
        }
        super.updateBean(plot);

        if(plot instanceof VanChartColumnPlot){
            VanChartColumnPlot columnPlot4VanChart = (VanChartColumnPlot)plot;

            columnPlot4VanChart.setFixedWidth(isFixedWidth.getSelectedIndex() == 0);
            columnPlot4VanChart.setColumnWidth((int)columnWidth.getValue());
            columnPlot4VanChart.setCategoryIntervalPercent(categoryGap.updateBean());
            columnPlot4VanChart.setSeriesOverlapPercent(seriesGap.updateBean());
            columnPlot4VanChart.setFilledWithImage(isFillWithImage.getSelectedIndex() == 0);
            ConditionAttr defaultAttr = plot.getConditionCollection().getDefaultAttr();
            if(isFillWithImage.getSelectedIndex() == 0){
                AttrSeriesImageBackground attrSeriesImageBackground = (AttrSeriesImageBackground)defaultAttr.getExisted(AttrSeriesImageBackground.class);
                if(attrSeriesImageBackground == null){
                    attrSeriesImageBackground = new AttrSeriesImageBackground();
                    defaultAttr.addDataSeriesCondition(attrSeriesImageBackground);
                }
                attrSeriesImageBackground.setSeriesBackground(imagePane.updateBean());
                //设置背景图片平铺方式
                ImageBackground imageBackground = (ImageBackground) attrSeriesImageBackground.getSeriesBackground();
                if (imageBackground != null){
                    imageBackground.setLayout(Constants.IMAGE_TILED);
                }

            } else {
                AttrSeriesImageBackground attrSeriesImageBackground = (AttrSeriesImageBackground)defaultAttr.getExisted(AttrSeriesImageBackground.class);
                if(attrSeriesImageBackground != null){
                    defaultAttr.remove(attrSeriesImageBackground);
                }
            }
        }
    }
}
