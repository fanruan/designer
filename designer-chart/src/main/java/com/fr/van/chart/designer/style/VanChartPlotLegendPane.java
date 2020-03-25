package com.fr.van.chart.designer.style;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.UIBubbleFloatPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPane;
import com.fr.design.utils.gui.UIComponentUtils;
import com.fr.plugin.chart.attr.VanChartLegend;
import com.fr.stable.Constants;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.component.VanChartFloatPositionPane;
import com.fr.van.chart.designer.component.background.VanChartBackgroundWithOutImagePane;
import com.fr.van.chart.designer.component.border.VanChartBorderWithRadiusPane;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 属性表, 图表样式-图例 界面.
 */
public class VanChartPlotLegendPane extends BasicPane {
    private static final long serialVersionUID = 1614283200308877353L;

    private static final int WIDTH = 165;
    private static final int HEIGHT = 100;
    private static final int GAP = 20;

    protected UICheckBox isLegendVisible;

    protected JPanel legendPane;

    protected ChartTextAttrPane textAttrPane;
    protected VanChartBorderWithRadiusPane borderPane;
    protected VanChartBackgroundWithOutImagePane backgroundPane;
    protected UIButtonGroup<Integer> location;
    protected UIToggleButton customFloatPositionButton;
    protected VanChartFloatPositionPane customFloatPositionPane;

    //区域显示策略 恢复用注释。下面4行删除。
    protected UIButtonGroup<Integer> limitSize;
    protected UISpinner maxProportion;
    private UILabel limitSizeTitle;
    private JPanel maxProportionPane;
    //区域显示策略 恢复用注释。取消注释。
    //private LimitPane limitPane;

    //高亮显示的按钮
    protected UIButtonGroup<Boolean> highlightButton;
    protected JPanel highlightPane;


    protected VanChartStylePane parent;


    public VanChartPlotLegendPane() {
        initComponents();
    }

    public VanChartPlotLegendPane(VanChartStylePane parent){
        this.parent = parent;
        initComponents();
    }

    protected void initComponents(){
        isLegendVisible = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Legend_Visible"));
        legendPane = createLegendPane();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] col = {f};
        double[] row = {p, p};
        Component[][] components = new Component[][]{
                new Component[]{isLegendVisible},
                new Component[]{legendPane},
        };

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components,row,col);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);

        addLegendListener();
    }

    protected void addLegendListener(){
        isLegendVisible.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkBoxUse();
            }
        });
    }

    protected JPanel createLegendPaneWithoutHighlight(){
        borderPane = new VanChartBorderWithRadiusPane();
        backgroundPane = new VanChartBackgroundWithOutImagePane();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        double[] rowSize = { p,p,p,p,p,p,p};

        Component[][] components = new Component[][]{
                new Component[]{createTitlePositionPane(new double[]{p,p,p},columnSize),null},
                new Component[]{createTitleStylePane(),null} ,
                new Component[]{TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Border"),borderPane),null},
                new Component[]{TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Background"), backgroundPane),null},
                new Component[]{TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Display_Strategy"), createDisplayStrategy()),null}
        };
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    protected JPanel createLegendPane(){
        borderPane = new VanChartBorderWithRadiusPane();
        backgroundPane = new VanChartBackgroundWithOutImagePane();
        highlightPane = createHighlightPane();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(createDisplayStrategy(), BorderLayout.CENTER);
        panel.add(highlightPane, BorderLayout.SOUTH);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        double[] rowSize = { p,p,p,p,p,p,p,p};

        Component[][] components = new Component[][]{
                new Component[]{createTitlePositionPane(new double[]{p,p,p},columnSize),null},
                new Component[]{createTitleStylePane(),null} ,
                new Component[]{TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Border"),borderPane),null},
                new Component[]{TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Background"), backgroundPane),null},
                new Component[]{TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Display_Strategy"), panel),null},
        };
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    protected JPanel createTitlePositionPane(double[] row, double[] col){
        String[] textArray = {com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_Alignment_Top"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_Alignment_Bottom"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_Alignment_Left"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_Alignment_Right"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Right_Top")};
        Integer[] valueArray = {Constants.TOP, Constants.BOTTOM, Constants.LEFT, Constants.RIGHT, Constants.RIGHT_TOP};
        Icon[] iconArray = {BaseUtils.readIcon("/com/fr/design/images/chart/ChartLegend/layout_top.png"),
                BaseUtils.readIcon("/com/fr/design/images/chart/ChartLegend/layout_bottom.png"),
                BaseUtils.readIcon("/com/fr/design/images/chart/ChartLegend/layout_left.png"),
                BaseUtils.readIcon("/com/fr/design/images/chart/ChartLegend/layout_right.png"),
                BaseUtils.readIcon("/com/fr/design/images/chart/ChartLegend/layout_top_right.png")
        };

        location = new UIButtonGroup<Integer>(iconArray, valueArray);
        location.setAllToolTips(textArray);

        customFloatPositionButton = new UIToggleButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Custom_Float_Position"));
        UIComponentUtils.setLineWrap(customFloatPositionButton);
        customFloatPositionButton.setEventBannded(true);

        Component[][] components = new Component[][]{
                new Component[]{null,null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Layout_Position")),location},
                new Component[]{null,customFloatPositionButton}
        };

        customFloatPositionPane =  new VanChartFloatPositionPane();

        initPositionListener();

        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components,row,col);
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Form_Layout"), panel);
    }

    private void initPositionListener(){

        location.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                customFloatPositionButton.setSelected(false);
                checkDisplayStrategyUse();
            }
        });

        customFloatPositionButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!customFloatPositionButton.isSelected()){
                    customFloatPositionButton.setSelected(true);
                    checkDisplayStrategyUse();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                location.setSelectedIndex(-1);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                if(customFloatPositionPane == null) {
                    customFloatPositionPane =  new VanChartFloatPositionPane();
                }
                Point comPoint = customFloatPositionButton.getLocationOnScreen();
                Point arrowPoint = new Point(comPoint.x + customFloatPositionButton.getWidth()/2 - GAP, comPoint.y + customFloatPositionButton.getHeight());
                UIBubbleFloatPane<Style> pane = new UIBubbleFloatPane(Constants.TOP, arrowPoint, customFloatPositionPane, WIDTH, HEIGHT) {
                    @Override
                    public void updateContentPane() {
                        parent.attributeChanged();
                    }
                };
                pane.show(VanChartPlotLegendPane.this, null);
            }
        });
    }

    protected JPanel createTitleStylePane(){
        textAttrPane = new ChartTextAttrPane();
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Widget_Style"), textAttrPane);
    }

    protected JPanel createDisplayStrategy(){
        //区域显示策略 恢复用注释。开始删除。
        maxProportion = new UISpinner(0, 100, 1, 30);
        limitSize = new UIButtonGroup<Integer>(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Limit"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Not_Limit")});
        limitSizeTitle = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Max_Proportion"));


        JPanel limitSizePane = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Area_Size"), limitSize);
        maxProportionPane = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Max_Proportion"), maxProportion, TableLayout4VanChartHelper.SECOND_EDIT_AREA_WIDTH);
        maxProportionPane.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(limitSizePane, BorderLayout.NORTH);
        panel.add(maxProportionPane, BorderLayout.CENTER);

        limitSize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkMaxProPortionUse();
            }
        });
        return panel;
        //区域显示策略 恢复用注释。结束删除。

        //区域显示策略 恢复用注释。取消注释。
//        limitPane = new LimitPane(false);
//        return limitPane;
    }

    private JPanel createHighlightPane(){
        highlightButton = new UIButtonGroup<Boolean>(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_On"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Off")}, new Boolean[]{true, false});

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        double[] rowSize = {p,p};
        Component[][] components = new Component[][]{
                new Component[]{null,null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Highlight")),highlightButton}
        };
        return TableLayout4VanChartHelper.createGapTableLayoutPane(components,rowSize,columnSize);
    }

    protected void checkAllUse() {
        checkBoxUse();
        checkDisplayStrategyUse();
        this.repaint();
    }

    //检查显示策略界面是否可用
    protected void checkDisplayStrategyUse() {
        //区域显示策略 恢复用注释。下面2行删除。
        limitSize.setEnabled(!customFloatPositionButton.isSelected());
        checkMaxProPortionUse();
        //区域显示策略 恢复用注释。取消注释。
//        GUICoreUtils.setEnabled(limitPane, !customFloatPositionButton.isSelected());
//        limitPane.checkMaxProPortionUse();
    }

    //区域显示策略 恢复用注释。删除下面方法。
    //检查最大显示占比是否可用
    private void checkMaxProPortionUse() {
        maxProportion.setVisible(limitSize.getSelectedIndex() == 0 && limitSize.isEnabled());
        maxProportionPane.setVisible(limitSize.getSelectedIndex() == 0 && limitSize.isEnabled());
    }

    protected void checkBoxUse() {
        isLegendVisible.setEnabled(true);
        legendPane.setVisible(isLegendVisible.isSelected());
    }

    /**
     * 标题
     * @return 标题
     */
    public String title4PopupWindow() {
        return PaneTitleConstants.CHART_STYLE_LEGNED_TITLE;
    }

    public void updateBean(VanChartLegend legend) {
        if(legend == null) {
            legend = new VanChartLegend();
        }
        legend.setLegendVisible(isLegendVisible.isSelected());
        legend.setFRFont(textAttrPane.updateFRFont());
        borderPane.update(legend);
        backgroundPane.update(legend);

        if(!customFloatPositionButton.isSelected()){
            legend.setPosition(location.getSelectedItem());
        } else {
            legend.setPosition(-1);
        }
        legend.setFloating(customFloatPositionButton.isSelected());
        //区域显示策略 恢复用注释。下面2行删除。
        legend.setLimitSize(limitSize.getSelectedIndex() == 0);
        legend.setMaxHeight(maxProportion.getValue());
        //区域显示策略 恢复用注释。取消注释。
        //legend.setLimitAttribute(limitPane.updateBean());
        legend.setFloatPercentX(customFloatPositionPane.getFloatPosition_x());
        legend.setFloatPercentY(customFloatPositionPane.getFloatPosition_y());
        if(highlightButton != null && highlightButton.getSelectedItem() != null){
            legend.setHighlight(highlightButton.getSelectedItem());
        }
    }

    public void populateBean(VanChartLegend legend) {
        if (legend != null) {
            isLegendVisible.setSelected(legend.isLegendVisible());
            textAttrPane.populate(legend.getFRFont());
            borderPane.populate(legend);
            backgroundPane.populate(legend);
            if(!legend.isFloating()){
                location.setSelectedItem(legend.getPosition());
            }
            customFloatPositionButton.setSelected(legend.isFloating());
            customFloatPositionPane.setFloatPosition_x(legend.getFloatPercentX());
            customFloatPositionPane.setFloatPosition_y(legend.getFloatPercentY());
            //区域显示策略 恢复用注释。下面2行删除。
            limitSize.setSelectedIndex(legend.isLimitSize() ? 0 : 1);
            maxProportion.setValue(legend.getMaxHeight());
            //区域显示策略 恢复用注释。取消注释。
            //limitPane.populateBean(legend.getLimitAttribute());
            if(highlightButton != null){
                highlightButton.setSelectedItem(legend.isHighlight());
            }
        }

        checkAllUse();
    }
}
