package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.base.BaseUtils;
import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Bar2DPlot;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartdata.NormalTableDataDefinition;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.utils.gui.GUICoreUtils;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 多分类轴 的数据集定义界面.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-9-3 上午10:00:28
 */
public class CategoryPlotMoreCateTableDataContentPane extends CategoryPlotTableDataContentPane implements UIObserver {
    private static final long serialVersionUID = -3305681053750642843L;
    private static final int COMBOX_GAP = 8;
    private static final int COMBOX_WIDTH = 95;
    private static final int COMBOX_HEIGHT = 20;

    private JPanel boxPane;

    private ArrayList<UIComboBox> boxList = new ArrayList<UIComboBox>();
    private UIButton addButton;

    private UIObserverListener uiobListener = null;

    public List<UIComboBox> getBoxList() {
        return boxList;
    }

    public CategoryPlotMoreCateTableDataContentPane() {
        // do nothing
    }

    public CategoryPlotMoreCateTableDataContentPane(ChartDataPane parent) {
        categoryCombox = new UIComboBox();
        categoryCombox.setPreferredSize(new Dimension(COMBOX_WIDTH,COMBOX_HEIGHT));

        JPanel categoryPane = new JPanel(new BorderLayout(4, 0));
        categoryPane.setBorder(BorderFactory.createMatteBorder(0, 0, 6, 1, getBackground()));
        UILabel categoryLabel = new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_Category"));
        categoryLabel.setPreferredSize(new Dimension(85,COMBOX_HEIGHT));

        addButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/add.png"));
        addButton.setPreferredSize(new Dimension(20, COMBOX_HEIGHT));

        categoryPane.add(GUICoreUtils.createBorderLayoutPane(new Component[]{categoryCombox, addButton, null, categoryLabel, null}));

        categoryPane.setBorder(BorderFactory.createEmptyBorder(0,24,10,15));

        boxPane = new JPanel();
        boxPane.setLayout(new BoxLayout(boxPane, BoxLayout.Y_AXIS));

        categoryPane.add(boxPane, BorderLayout.SOUTH);

        this.setLayout(new BorderLayout());
        this.add(categoryPane, BorderLayout.NORTH);
        this.add(getJSeparator());
        seriesTypeComboxPane = new SeriesTypeUseComboxPane(parent, new Bar2DPlot());
        this.add(seriesTypeComboxPane, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (boxList.size() < 2) {
                    addNewCombox();
                    relayoutPane();
                }

                checkSeriseUse(categoryCombox.getSelectedItem() != null);
            }
        });

        categoryCombox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                checkSeriseUse(categoryCombox.getSelectedItem() != null);
                makeToolTipUse(categoryCombox);

                checkComponent();
            }
        });
    }

    protected void checkSeriseUse(boolean hasUse) {
        super.checkSeriseUse(hasUse);

        addButton.setEnabled(hasUse);
    }

    private UIComboBox addNewCombox() {
        final JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 2));

        final UIComboBox combox = new UIComboBox();
        combox.setPreferredSize(new Dimension(COMBOX_WIDTH, COMBOX_HEIGHT));

        int count = categoryCombox.getItemCount();
        for (int i = 0; i < count; i++) {
            combox.addItem(categoryCombox.getItemAt(i));
        }

        combox.registerChangeListener(uiobListener);
        combox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                makeToolTipUse(combox);
            }
        });

        combox.setSelectedItem(categoryCombox.getItemAt(0));
        makeToolTipUse(combox);

        buttonPane.add(combox);
        UIButton delButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/toolbarbtn/close.png"));
        buttonPane.add(delButton);
        boxPane.add(buttonPane);
        boxList.add(combox);

        checkComponent();

        delButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boxPane.remove(buttonPane);
                boxList.remove(combox);
                checkComponent();
                relayoutPane();
            }
        });
        delButton.registerChangeListener(uiobListener);

        return combox;
    }

    private void checkAddButton() {
        int size = boxList.size();
        addButton.setEnabled(size < 2 && categoryCombox.getSelectedItem() != null);
    }

    protected void checkComponent() {
        checkAddButton();
    }

    private void relayoutPane() {
        this.revalidate();
    }


    /**
     * 检查 某些Box是否可用
     *
     * @param hasUse 是否使用.
     */
    public void checkBoxUse(boolean hasUse) {
        super.checkBoxUse(hasUse);

        checkComponent();
    }

    protected void refreshBoxListWithSelectTableData(List list) {
        super.refreshBoxListWithSelectTableData(list);

        for (int i = 0, size = boxList.size(); i < size; i++) {
            refreshBoxItems(boxList.get(i), list);
        }
    }

    /**
     * 给组件登记一个观察者监听事件
     *
     * @param listener 观察者监听事件
     */
    public void registerChangeListener(UIObserverListener listener) {
        uiobListener = listener;
    }

    /**
     * 组件是否需要响应添加的观察者事件
     *
     * @return 如果需要响应观察者事件则返回true，否则返回false
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }

    /**
     * 更新 多分类相关界面
     *
     * @param collection
     */
    public void populateBean(ChartCollection collection) {
        super.populateBean(collection);

        boxList.clear();

        TopDefinitionProvider top = collection.getSelectedChart().getFilterDefinition();
        if (top instanceof NormalTableDataDefinition) {
            NormalTableDataDefinition normal = (NormalTableDataDefinition) top;
            int size = normal.getMoreCateSize();
            for (int i = 0; i < size; i++) {
                UIComboBox box = addNewCombox();
                box.setSelectedItem(normal.getMoreCateWithIndex(i));
            }
        }

        checkAddButton();
        checkSeriseUse(categoryCombox.getSelectedItem() != null);
    }

    /**
     * 保存多分类界面到collection
     *
     * @param collection
     */
    public void updateBean(ChartCollection collection) {
        super.updateBean(collection);

        TopDefinitionProvider top = collection.getSelectedChart().getFilterDefinition();
        Plot plot = collection.getSelectedChart().getPlot();
        if (top instanceof NormalTableDataDefinition) {
            NormalTableDataDefinition normal = (NormalTableDataDefinition) top;
            normal.clearMoreCate();
            updateMoreCate(normal, plot);
        }
    }

    protected void updateMoreCate(NormalTableDataDefinition normal, Plot plot) {
        for (int i = 0, size = boxList.size(); i < size; i++) {
            UIComboBox box = boxList.get(i);
            if (box.getSelectedItem() != null) {
                normal.addMoreCate(box.getSelectedItem().toString());
            }
        }
    }

}