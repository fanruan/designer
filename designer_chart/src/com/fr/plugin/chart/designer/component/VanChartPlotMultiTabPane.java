package com.fr.plugin.chart.designer.component;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;

import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.ibutton.UITabGroup;

/**
 *
 * 标准的多层Tab切换类型.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-4-22 上午10:31:03
 */
public abstract class VanChartPlotMultiTabPane<E, T> extends FurtherBasicBeanPane<T>{
    private static final long serialVersionUID = 2298609199400393886L;
    protected UITabGroup tabPane;
    protected String[] NameArray;
    protected JPanel centerPane;
    protected CardLayout cardLayout;
    protected List<JPanel> paneList;

    //提供一个可以根据plot信息创建paneList的类
    protected E plot;
    //承载该容器的父类容器
    protected BasicPane parent;

    //屬性變化的監聽器，在構造數據配置面板時用到
    protected AttributeChangeListener listener = null;

    protected abstract List<JPanel> initPaneList();

    public abstract void populateBean(T ob);
    public abstract void updateBean(T ob);

    public VanChartPlotMultiTabPane(E plot, BasicPane parent) {
        this(plot, parent, null);
    }

    public VanChartPlotMultiTabPane(E plot, BasicPane parent, AttributeChangeListener listener) {
        this.plot = plot;
        this.parent = parent;
        this.listener = listener;
        cardLayout = new CardLayout();
        paneList = initPaneList();
        relayoutWhenListChange();
    }

    public int getSelectedIndex() {
        return tabPane.getSelectedIndex();
    }

    /**
     * 当List中的界面变化时, 重新布局
     */
    public void relayoutWhenListChange() {
        centerPane = new JPanel(cardLayout) {
            @Override
            public Dimension getPreferredSize() {
                if (tabPane.getSelectedIndex() == -1) {
                    return super.getPreferredSize();
                } else {
                    return paneList.get(tabPane.getSelectedIndex()).getPreferredSize();
                }
            }
        };
        NameArray = new String[paneList.size()];
        for (int i = 0; i < paneList.size(); i++) {
            BasicPane pane = (BasicPane) paneList.get(i);
            NameArray[i] = pane.getTitle();
            centerPane.add(pane, NameArray[i]);
        }

        tabPane = new UITabGroup(NameArray) {
            @Override
            public void tabChanged(int index) {
                dealWithTabChanged(index);
            }
        };
        tabPane.setSelectedIndex(0);
        tabPane.tabChanged(0);
        initLayout();
    }

    protected void dealWithTabChanged(int index) {
        cardLayout.show(centerPane, NameArray[index]);
        tabChanged();
    }

    protected void tabChanged() {

    }

    protected void initLayout() {
        this.setLayout(new BorderLayout(0, 4));
        this.add(tabPane, BorderLayout.NORTH);
        this.add(centerPane, BorderLayout.CENTER);
    }
}