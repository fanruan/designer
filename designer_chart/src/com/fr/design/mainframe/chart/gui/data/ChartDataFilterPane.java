package com.fr.design.mainframe.chart.gui.data;

import com.fr.base.Utils;
import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.style.AbstractChartTabPane;
import com.fr.design.mainframe.chart.gui.style.ThirdTabPane;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 图表数据 分类 系列 过滤界面.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-12-19 下午04:40:21
 */
public class ChartDataFilterPane extends ThirdTabPane<ChartCollection> {
    private static final long serialVersionUID = 3650522989381790194L;
    private static final int PAN_WIDTH = 210;
    private static final int FIL_HEIGHT = 130;

    private CategoryFilterPane categoryPane;
    private SeriesFilterPane seriesPane;
    private boolean isNeedPresent = true;
    private AbstractAttrNoScrollPane parentPane = null;
    private Plot plot4Pane = null;

    public ChartDataFilterPane(Plot plot, ChartDataPane parent) {
        super(plot, parent);
        this.isNeedPresent = true;
    }


    protected List<NamePane> initPaneList(Plot plot, AbstractAttrNoScrollPane parent) {
        plot4Pane = plot;
        parentPane = parent;
        List<NamePane> paneList = new ArrayList<NamePane>();
        if (plot == null || plot.isSupportCategoryFilter()) {
            categoryPane = new CategoryFilterPane(parent);
            paneList.add(new NamePane(categoryPane.title4PopupWindow(), categoryPane));
        }

        if (plot == null || plot.isSupportSeriesFilter()) {
            seriesPane = new SeriesFilterPane(parent);
            paneList.add(new NamePane(seriesPane.title4PopupWindow(), seriesPane));
        }
        return paneList;
    }

    protected int getContentPaneWidth() {
        return PAN_WIDTH;
    }

    /**
     * 检查界面box使用.
     */
    public void checkBoxUse() {
        if (categoryPane != null) {
            categoryPane.checkBoxUse();
        }
        if (seriesPane != null) {
            seriesPane.checkBoxUse();
        }
    }

    /**
     * 界面标题: 数据筛选.
     *
     * @return 返回标题.
     */
    public String title4PopupWindow() {
        return Inter.getLocText("FR-Chart-Data_Filter");
    }

    /**
     * 重新布局面板
     * @param isNeedPresent 是否需要形态（图表设计器不需要）
     */
    public void relayoutPane(boolean isNeedPresent) {
        if (this.isNeedPresent == isNeedPresent) {
            return;
        }
        this.isNeedPresent = isNeedPresent;
        this.removeAll();
        paneList = initPaneList4NoPresent(plot4Pane, parentPane);
        initAllPane();
        this.validate();
    }

    private List<NamePane> initPaneList4NoPresent(Plot plot, AbstractAttrNoScrollPane parent) {
        List<NamePane> paneList = new ArrayList<NamePane>();
        if (plot == null || plot.isSupportCategoryFilter()) {
            categoryPane = new CategoryFilterPaneWithOutPresentPane(parent);
            paneList.add(new NamePane(categoryPane.title4PopupWindow(), categoryPane));
        }

        if (plot == null || plot.isSupportSeriesFilter()) {
            seriesPane = new SeriesFilterWithOutPresentPane(parent);
            paneList.add(new NamePane(seriesPane.title4PopupWindow(), seriesPane));
        }
        return paneList;
    }

    public void populateBean(ChartCollection collection, boolean isNeedPresent) {
        relayoutPane(isNeedPresent);
        if (categoryPane != null) {
            categoryPane.populateBean(collection.getSelectedChart().getFilterDefinition());
        }
        if (seriesPane != null) {
            seriesPane.populateBean(collection.getSelectedChart().getFilterDefinition());
        }
        checkBoxUse();
    }

    /**
     * 更新界面 数据筛选.
     */
    public void populateBean(ChartCollection collection) {
        this.populateBean(collection, true);
    }

    /**
     * 保存界面数据筛选.
     */
    public void updateBean(ChartCollection collection) {
        if (categoryPane != null) {
            categoryPane.updateBean(collection.getSelectedChart().getFilterDefinition());
        }
        if (seriesPane != null) {
            seriesPane.updateBean(collection.getSelectedChart().getFilterDefinition());
        }
    }

    /**
     * 分类过滤.
     *
     * @author kunsnat E-mail:kunsnat@gmail.com
     * @version 创建时间：2012-12-19 下午04:41:35
     */
    private class CategoryFilterPane extends AbstractChartTabPane<TopDefinitionProvider> {
        private UICheckBox onlyPreData;
        private UITextField preDataNum;
        private UICheckBox combineOther;
        private UICheckBox notShowNull;
        private PresentComboBox present;
        private AbstractAttrNoScrollPane parent;

        public CategoryFilterPane(AbstractAttrNoScrollPane parent) {
            super(true);
            this.parent = parent;
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension dim = super.getPreferredSize();
            dim.height = FIL_HEIGHT;
            return dim;
        }

        @Override
        protected JPanel createContentPane() {
            this.setLayout(new BorderLayout());
            JPanel pane = new JPanel();
            this.add(pane, BorderLayout.NORTH);
            pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
            pane.setPreferredSize(new Dimension(200, 110));
            initOtherPane(pane);
            initPresentPane(pane);
            return pane;
        }

        protected void initOtherPane(JPanel pane) {
            JPanel prePane = new JPanel();
            prePane.setLayout(new FlowLayout(FlowLayout.LEFT));
            prePane.setPreferredSize(new Dimension(200, 20));
            pane.add(prePane);
            onlyPreData = new UICheckBox(Inter.getLocText("FR-Chart-Data_OnlyUseBefore"));
            preDataNum = new UITextField();
            preDataNum.setPreferredSize(new Dimension(50, 20));
            prePane.add(onlyPreData);
            prePane.add(preDataNum);
            prePane.add(new BoldFontTextLabel(Inter.getLocText("FR-Chart-Data_Records")));
            JPanel otherPane = new JPanel();
            otherPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            pane.add(otherPane);
            combineOther = new UICheckBox(Inter.getLocText("FR-Chart-Data_CombineOther"));
            combineOther.setSelected(true);
            otherPane.add(combineOther);
            JPanel catePane = new JPanel();
            pane.add(catePane);
            catePane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
            notShowNull = new UICheckBox(Inter.getLocText("FR-Chart-Data_NotShowCate"));
            catePane.add(notShowNull);

            onlyPreData.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    checkBoxUse();
                }
            });
        }

        private void initPresentPane(JPanel pane) {
            JPanel presentPane = new JPanel();
            presentPane.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));
            pane.add(presentPane);
            present = new PresentComboBox() {
                protected void fireChange() {
                    fire();
                }
            };
            present.setPreferredSize(new Dimension(70, 20));
            presentPane.add(new BoldFontTextLabel(Inter.getLocText("FR-Chart-Style_Present") + ":"));
            presentPane.add(present);
        }

        private void fire() {
            if (this.parent != null) {
                this.parent.attributeChanged();
            }
        }

        /**
         * 界面标题 "分类"
         */
        public String title4PopupWindow() {
            return Inter.getLocText("FR-Chart-Style_Category");
        }

        /**
         * 检查分类过滤界面 Box是否可用.
         */
        public void checkBoxUse() {
            preDataNum.setEnabled(onlyPreData.isSelected());
            combineOther.setEnabled(onlyPreData.isSelected());
            ;
        }

        /**
         * 更新分类过滤界面内容
         */
        public void populateBean(TopDefinitionProvider topDefinition) {
            if (topDefinition == null) {
                return;
            }
            populateBean4NoPresent(topDefinition);
            if(present != null){
                present.populate(topDefinition.getCategoryPresent());
            }
        }

        /**
         * 保存分类更新内容到TopDefinition
         */
        public void updateBean(TopDefinitionProvider topDefinition) {
            updateBean4NoPresent(topDefinition);
            topDefinition.setCategoryPresent(present.update());
        }

        @Override
        public TopDefinition updateBean() {
            return null;
        }

        protected void updateBean4NoPresent(TopDefinitionProvider topDefinition) {
            if (onlyPreData.isSelected()) {
                if (StringUtils.isNotEmpty(preDataNum.getText())) {
                    Number number = Utils.objectToNumber(preDataNum.getText(), true);
                    if (number != null) {
                        topDefinition.setTopCate(Integer.valueOf(preDataNum.getText()));
                    }
                }
                topDefinition.setDiscardOtherCate(!combineOther.isSelected());
            } else {
                topDefinition.setTopCate(Integer.valueOf(-1));
            }
            topDefinition.setDiscardNullCate(notShowNull.isSelected());
        }

        protected void populateBean4NoPresent(TopDefinitionProvider topDefinition) {
            if (topDefinition.getTopCate() == -1) {
                onlyPreData.setSelected(false);
            } else {
                onlyPreData.setSelected(true);
                preDataNum.setText(String.valueOf(topDefinition.getTopCate()));

                combineOther.setSelected(!topDefinition.isDiscardOtherCate());
            }

            notShowNull.setSelected(topDefinition.isDiscardNullCate());
        }
    }

    /**
     * 系列过滤.
     *
     * @author kunsnat E-mail:kunsnat@gmail.com
     * @version 创建时间：2012-12-19 下午04:41:24
     */
    private class SeriesFilterPane extends AbstractChartTabPane<TopDefinitionProvider> {
        private UICheckBox onlyPreData;
        private UITextField preDataNum;
        private UICheckBox notShowNull;
        private UICheckBox combineOther;
        private PresentComboBox present;

        private AbstractAttrNoScrollPane parent;

        public SeriesFilterPane(AbstractAttrNoScrollPane parent) {
            super(true);
            this.parent = parent;
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension dim = super.getPreferredSize();
            dim.height = FIL_HEIGHT;
            return dim;
        }

        @Override
        protected JPanel createContentPane() {
            this.setLayout(new BorderLayout());
            JPanel pane = new JPanel();
            this.add(pane, BorderLayout.NORTH);
            pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
            pane.setPreferredSize(new Dimension(200, 110));
            initOtherPane(pane);
            initPresentPane(pane);
            return pane;
        }


        protected void initOtherPane(JPanel pane) {
            JPanel prePane = new JPanel();
            prePane.setLayout(new FlowLayout(FlowLayout.LEFT));
            prePane.setPreferredSize(new Dimension(200, 20));
            pane.add(prePane);
            onlyPreData = new UICheckBox(Inter.getLocText("FR-Chart-Data_OnlyUseBefore"));
            preDataNum = new UITextField();
            preDataNum.setPreferredSize(new Dimension(50, 20));
            prePane.add(onlyPreData);
            prePane.add(preDataNum);
            prePane.add(new UILabel(Inter.getLocText("FR-Chart-Data_Records")));
            JPanel otherPane = new JPanel();
            otherPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            pane.add(otherPane);

            combineOther = new UICheckBox(Inter.getLocText("FR-Chart-Data_CombineOther"));
            combineOther.setSelected(true);
            otherPane.add(combineOther);
            JPanel catePane = new JPanel();
            pane.add(catePane);

            catePane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
            notShowNull = new UICheckBox(Inter.getLocText("FR-Chart-Data_NotShowSeries"));
            catePane.add(notShowNull);

            onlyPreData.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    checkBoxUse();
                }
            });
        }

        private void initPresentPane(JPanel pane) {
            JPanel presentPane = new JPanel();
            presentPane.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));
            pane.add(presentPane);
            present = new PresentComboBox() {
                protected void fireChange() {
                    fire();
                }
            };
            present.setPreferredSize(new Dimension(70, 20));
            presentPane.add(new BoldFontTextLabel(Inter.getLocText("FR-Chart-Style_Present") + ":"));
            presentPane.add(present);
        }

        private void fire() {
            if (this.parent != null) {
                parent.attributeChanged();
            }
        }


        /**
         * 界面标题
         */
        public String title4PopupWindow() {
            return Inter.getLocText("FR-Chart-Data_Series");
        }

        /**
         * 检查Box是否可用
         */
        public void checkBoxUse() {
            preDataNum.setEnabled(onlyPreData.isSelected());
            combineOther.setEnabled(onlyPreData.isSelected());
        }

        /**
         * 系列 筛选器界面 更新属性
         */
        public void populateBean(TopDefinitionProvider topDefinition) {
            if (topDefinition == null) {
                return;
            }
            populate4NoPresent(topDefinition);
            if(present != null){
                present.populate(topDefinition.getSeriesPresent());
            }
        }

        protected void populate4NoPresent(TopDefinitionProvider topDefinition) {
            if (topDefinition.getTopSeries() == -1) {
                onlyPreData.setSelected(false);
            } else {
                onlyPreData.setSelected(true);
                preDataNum.setText(String.valueOf(topDefinition.getTopSeries()));

                combineOther.setSelected(!topDefinition.isDiscardOtherSeries());
            }

            notShowNull.setSelected(topDefinition.isDiscardNullSeries());
        }

        /**
         * 系列 筛选器界面  保存属性到TopDefinition
         */
        public void updateBean(TopDefinitionProvider topDefinition) {
            update4NoPresent(topDefinition);
            if(present != null){
                topDefinition.setSeriesPresent(present.update());
            }
        }

        protected void update4NoPresent(TopDefinitionProvider topDefinition) {
            if (onlyPreData.isSelected()) {
                if (StringUtils.isNotEmpty(preDataNum.getText())) {
                    Number number = Utils.objectToNumber(preDataNum.getText(), true);
                    if (number != null) {
                        topDefinition.setTopSeries(number.intValue());
                    }
                }
                topDefinition.setDiscardOtherSeries(!combineOther.isSelected());
            } else {
                topDefinition.setTopSeries(Integer.valueOf(-1));
            }

            topDefinition.setDiscardNullSeries(notShowNull.isSelected());
        }

        @Override
        public TopDefinition updateBean() {
            return null;
        }
    }

    private class SeriesFilterWithOutPresentPane extends SeriesFilterPane {

        public SeriesFilterWithOutPresentPane(AbstractAttrNoScrollPane parent) {
            super(parent);
        }

        @Override
        protected JPanel createContentPane() {
            this.setLayout(new BorderLayout());
            JPanel pane = new JPanel();
            this.add(pane, BorderLayout.NORTH);
            pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
            pane.setPreferredSize(new Dimension(200, 110));
            initOtherPane(pane);
            return pane;
        }

        /**
         * 系列 筛选器界面 更新属性
         */
        public void populateBean(TopDefinition topDefinition) {
            if (topDefinition == null) {
                return;
            }
            populate4NoPresent(topDefinition);
        }

        /**
         * 系列 筛选器界面  保存属性到TopDefinition
         */
        public void updateBean(TopDefinition topDefinition) {
            update4NoPresent(topDefinition);
        }

    }

    private class CategoryFilterPaneWithOutPresentPane extends CategoryFilterPane {

        public CategoryFilterPaneWithOutPresentPane(AbstractAttrNoScrollPane parent) {
            super(parent);
        }

        @Override
        protected JPanel createContentPane() {
            this.setLayout(new BorderLayout());
            JPanel pane = new JPanel();
            this.add(pane, BorderLayout.NORTH);
            pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
            pane.setPreferredSize(new Dimension(200, 110));
            initOtherPane(pane);
            return pane;
        }

        /**
         * 更新分类过滤界面内容
         */
        public void populateBean(TopDefinition topDefinition) {
            if (topDefinition == null) {
                return;
            }
            populateBean4NoPresent(topDefinition);
        }

        /**
         * 保存分类更新内容到TopDefinition
         */
        public void updateBean(TopDefinitionProvider topDefinition) {
            updateBean4NoPresent(topDefinition);
        }
    }
}