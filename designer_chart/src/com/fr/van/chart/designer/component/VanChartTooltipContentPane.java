package com.fr.van.chart.designer.component;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.AttrTooltipContent;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.component.format.CategoryNameFormatPaneWithCheckBox;
import com.fr.van.chart.designer.component.format.ChangedPercentFormatPaneWithCheckBox;
import com.fr.van.chart.designer.component.format.ChangedValueFormatPaneWithCheckBox;
import com.fr.van.chart.designer.component.format.PercentFormatPaneWithCheckBox;
import com.fr.van.chart.designer.component.format.SeriesNameFormatPaneWithCheckBox;
import com.fr.van.chart.designer.component.format.ValueFormatPaneWithCheckBox;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 内容界面 。数据点提示
 */
public class VanChartTooltipContentPane extends BasicBeanPane<AttrTooltipContent> {

    private static final long serialVersionUID = 8825929000117843641L;

    protected UIButtonGroup<Integer> content;
    protected ValueFormatPaneWithCheckBox valueFormatPane;
    protected PercentFormatPaneWithCheckBox percentFormatPane;
    protected CategoryNameFormatPaneWithCheckBox categoryNameFormatPane;
    protected SeriesNameFormatPaneWithCheckBox seriesNameFormatPane;

    //监控刷新时，自动数据点提示使用
    protected ChangedValueFormatPaneWithCheckBox changedValueFormatPane;
    protected ChangedPercentFormatPaneWithCheckBox changedPercentFormatPane;

    private JPanel centerPane;
    private VanChartHtmlLabelPane htmlLabelPane;

    private VanChartStylePane parent;
    private JPanel showOnPane;

    public VanChartTooltipContentPane(VanChartStylePane parent, JPanel showOnPane){
        this.parent = parent;
        this.showOnPane = showOnPane;

        this.setLayout(new BorderLayout());
        this.add(createLabelContentPane(),BorderLayout.CENTER);
    }

    private JPanel createLabelContentPane() {
        content = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("Plugin-ChartF_Common"),
                Inter.getLocText("Plugin-ChartF_Custom")});

        initFormatPane(parent, showOnPane);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f, p};
        double[] rowSize = getRowSize(p);

        final JPanel commonPanel = TableLayoutHelper.createTableLayoutPane(getPaneComponents(), rowSize, columnSize);

        htmlLabelPane = createHtmlLabelPane();
        htmlLabelPane.setParent(parent);

        centerPane = new JPanel(new CardLayout()){
            @Override
            public Dimension getPreferredSize() {
                if(content.getSelectedIndex() == 0){
                    return commonPanel.getPreferredSize();
                } else {
                    return new Dimension(commonPanel.getPreferredSize().width,htmlLabelPane.getPreferredSize().height);
                }
            }
        };
        centerPane.add(htmlLabelPane, Inter.getLocText("Plugin-ChartF_Custom"));
        centerPane.add(commonPanel,Inter.getLocText("Plugin-ChartF_Common"));

        double[] column = {f, e};
        double[] row = {p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{null,null},
                new Component[]{new UILabel(getLabelContentTitle()),content},
                new Component[]{null,centerPane},
        };
        initContentListener();
        JPanel contentPane = TableLayout4VanChartHelper.createGapTableLayoutPane(components, row, column);
        return getLabelContentPane(contentPane);
    }

    protected String getLabelContentTitle () {
        return Inter.getLocText("FR-Designer_Text");
    }

    protected JPanel getLabelContentPane(JPanel contentPane) {
        return createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_Content"), contentPane);
    }

    protected VanChartHtmlLabelPane createHtmlLabelPane() {
        return new VanChartHtmlLabelPaneWithOutWidthAndHeight();
    }

    protected void initFormatPane(VanChartStylePane parent, JPanel showOnPane){
        categoryNameFormatPane = new CategoryNameFormatPaneWithCheckBox(parent, showOnPane);
        seriesNameFormatPane = new SeriesNameFormatPaneWithCheckBox(parent, showOnPane);
        valueFormatPane = new ValueFormatPaneWithCheckBox(parent, showOnPane);
        percentFormatPane = new PercentFormatPaneWithCheckBox(parent, showOnPane);
    }

    protected JPanel createTableLayoutPaneWithTitle(String title, JPanel panel) {
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(title, panel);
    }

    protected double[] getRowSize(double p){
        return new double[]{p,p,p,p};
    }

    protected Component[][] getPaneComponents(){
        return new Component[][]{
                new Component[]{categoryNameFormatPane,null},
                new Component[]{seriesNameFormatPane,null},
                new Component[]{valueFormatPane,null},
                new Component[]{percentFormatPane,null},
        };
    }

    private void initContentListener() {
        content.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               checkCardPane();
            }
        });
    }


    private void checkCardPane() {
        CardLayout cardLayout = (CardLayout) centerPane.getLayout();
        if (content.getSelectedIndex() == 1) {
            cardLayout.show(centerPane,Inter.getLocText("Plugin-ChartF_Custom"));
            if(isDirty()){
                setCustomFormatterText();
                setDirty(false);
            }
        } else {
            cardLayout.show(centerPane, Inter.getLocText("Plugin-ChartF_Common"));
        }
    }

    protected void setCustomFormatterText() {
        htmlLabelPane.setCustomFormatterText(updateBean().getFormatterTextFromCommon());
    }

    public boolean isDirty() {
        return categoryNameFormatPane.isDirty() || seriesNameFormatPane.isDirty() || valueFormatPane.isDirty() || percentFormatPane.isDirty()
                || (changedValueFormatPane != null && changedValueFormatPane.isDirty()) || (changedValueFormatPane != null && changedPercentFormatPane.isDirty());
    }

    public void setDirty(boolean isDirty) {
        categoryNameFormatPane.setDirty(isDirty);
        seriesNameFormatPane.setDirty(isDirty);
        valueFormatPane.setDirty(isDirty);
        percentFormatPane.setDirty(isDirty);

        if (changedValueFormatPane != null) {
            changedValueFormatPane.setDirty(isDirty);
        }
        if (changedPercentFormatPane != null) {
            changedPercentFormatPane.setDirty(isDirty);
        }
    }

    @Override
    protected String title4PopupWindow() {
        return "";
    }


    @Override
    public void populateBean(AttrTooltipContent attrTooltipContent){
        if(attrTooltipContent == null){
            return;
        }

        content.setSelectedIndex(attrTooltipContent.isCommon() ? 0 : 1);

        populateFormatPane(attrTooltipContent);

        htmlLabelPane.populate(attrTooltipContent.getHtmlLabel());
        if(!attrTooltipContent.isCommon()){
            setDirty(false);
        }
        checkCardPane();
    }

    protected void populateFormatPane(AttrTooltipContent attrTooltipContent) {
        categoryNameFormatPane.populate(attrTooltipContent.getCategoryFormat());
        seriesNameFormatPane.populate(attrTooltipContent.getSeriesFormat());
        valueFormatPane.populate(attrTooltipContent.getValueFormat());
        percentFormatPane.populate(attrTooltipContent.getPercentFormat());

        if (changedValueFormatPane != null) {
            changedValueFormatPane.populate(attrTooltipContent.getChangedValueFormat());
        }
        if (changedPercentFormatPane != null) {
            changedPercentFormatPane.populate(attrTooltipContent.getChangedPercentFormat());
        }
    }

    public AttrTooltipContent updateBean() {
        AttrTooltipContent attrTooltipContent = createAttrTooltip();

        attrTooltipContent.setCommon(content.getSelectedIndex() == 0);

        updateFormatPane(attrTooltipContent);

        updateFormatsWithPaneWidth(attrTooltipContent);

        htmlLabelPane.update(attrTooltipContent.getHtmlLabel());

        return attrTooltipContent;
    }

    protected AttrTooltipContent createAttrTooltip() {
        return new AttrTooltipContent();
    }

    protected void updateFormatPane(AttrTooltipContent attrTooltipContent) {
        categoryNameFormatPane.update(attrTooltipContent.getCategoryFormat());
        seriesNameFormatPane.update(attrTooltipContent.getSeriesFormat());
        valueFormatPane.update(attrTooltipContent.getValueFormat());
        percentFormatPane.update(attrTooltipContent.getPercentFormat());

        if (changedValueFormatPane != null) {
            changedValueFormatPane.update(attrTooltipContent.getChangedValueFormat());
        }
        if (changedPercentFormatPane != null) {
            changedPercentFormatPane.update(attrTooltipContent.getChangedPercentFormat());
        }
    }

    /**
     * CHART-1295
     * 通过格式的面板宽度来判断在自定义js代码中是否显示this.seriesName字符串。
     * 图表对于其他的格式有要求也可以在这里继续添加。
     * @param attrTooltipContent
     */
    private void updateFormatsWithPaneWidth (AttrTooltipContent attrTooltipContent) {
        int paneWidth = seriesNameFormatPane.getWidth();
        if (paneWidth == 0) {
            attrTooltipContent.getSeriesFormat().setEnable(false);
        }
    }
}