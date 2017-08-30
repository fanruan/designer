package com.fr.plugin.chart.designer.component.background;

import com.fr.base.background.ImageBackground;
import com.fr.chart.chartglyph.GeneralInfo;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.mainframe.backgroundpane.BackgroundQuickPane;
import com.fr.design.mainframe.backgroundpane.ColorBackgroundQuickPane;
import com.fr.design.mainframe.backgroundpane.ImageBackgroundQuickPane;
import com.fr.design.mainframe.backgroundpane.NullBackgroundQuickPane;
import com.fr.general.Background;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.stable.Constants;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 图表  属性表.背景设置 界面.(包括 无, 颜色, 图片, 渐变)+开启阴影
 * 图片背景没有布局方式，默认为拉伸。
 */
public class VanChartBackgroundPane extends BasicPane {
    private static final long serialVersionUID = 6955952013135176051L;
    private static final double ALPHA_V = 100.0;
    protected List<BackgroundQuickPane> paneList;

    protected UIComboBox typeComboBox;
    protected UINumberDragPane transparent;
    protected UIButtonGroup<Boolean> shadow;

    protected JPanel centerPane;

    public VanChartBackgroundPane() {
        initComponents();
        JPanel panel = initContentPanel();
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
    }

    protected JPanel initContentPanel() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};

        double[] rowSize = {p, p, p, p, p};
        return TableLayout4VanChartHelper.createGapTableLayoutPane(getPaneComponents(), rowSize, columnSize);
    }

    protected void initComponents() {
        typeComboBox = new UIComboBox();
        final CardLayout cardlayout = new CardLayout();
        paneList = new ArrayList<BackgroundQuickPane>();

        initList();

        centerPane = new JPanel(cardlayout) {

            @Override
            public Dimension getPreferredSize() {// AUGUST:使用当前面板的的高度
                int index = typeComboBox.getSelectedIndex();
                return new Dimension(super.getPreferredSize().width, paneList.get(index).getPreferredSize().height);
            }
        };
        for (int i = 0; i < paneList.size(); i++) {
            BackgroundQuickPane pane = paneList.get(i);
            typeComboBox.addItem(pane.title4PopupWindow());
            centerPane.add(pane, pane.title4PopupWindow());
        }

        typeComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                cardlayout.show(centerPane, (String)typeComboBox.getSelectedItem());
                fireStateChanged();
            }
        });

        transparent = new UINumberDragPane(0, 100);
    }

    protected Component[][] getPaneComponents() {
        shadow = new UIButtonGroup<Boolean>(new String[]{Inter.getLocText("Plugin-ChartF_On"), Inter.getLocText("Plugin-ChartF_Off")}, new Boolean[]{true, false});

        return  new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Shape_Fill")), typeComboBox},
                new Component[]{null, centerPane},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Alpha")), transparent},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Shadow")), shadow},
        };
    }

    protected void initList() {
        paneList.add(new NullBackgroundQuickPane());
        paneList.add(new ColorBackgroundQuickPane());
        paneList.add(new ImageBackgroundQuickPane(false));
        paneList.add(new VanChartGradientPane());
    }


    private void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        ChangeEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (e == null) {
                    e = new ChangeEvent(this);
                }
                ((ChangeListener)listeners[i + 1]).stateChanged(e);
            }
        }
    }

    /**
     * 返回标题
     * @return 标题
     */
    public String title4PopupWindow() {
        return "";
    }

    public void populate(GeneralInfo attr) {
        if(attr == null) {
            return;
        }
        Background background = attr.getBackground();
        double alpha = attr.getAlpha() * ALPHA_V;
        transparent.populateBean(alpha);
        if(shadow != null){
            shadow.setSelectedIndex(attr.isShadow() == true ? 0 : 1);
        }
        for (int i = 0; i < paneList.size(); i++) {
            BackgroundQuickPane pane = paneList.get(i);
            if (pane.accept(background)) {
                pane.populateBean(background);
                typeComboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    public void update(GeneralInfo attr) {
        if (attr == null) {
            attr = new GeneralInfo();
        }
        attr.setBackground(paneList.get(typeComboBox.getSelectedIndex()).updateBean());
        if(attr.getBackground() instanceof ImageBackground){
            ((ImageBackground) attr.getBackground()).setLayout(Constants.IMAGE_EXTEND);
        }
        attr.setAlpha((float) (transparent.updateBean() / ALPHA_V));
        if(shadow != null){
            attr.setShadow(shadow.getSelectedIndex() == 0);
        }
    }
}