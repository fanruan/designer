package com.fr.design.gui.style;

import com.fr.base.Style;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.fun.BackgroundQuickUIProvider;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.backgroundpane.*;
import com.fr.general.Background;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author zhou
 * @since 2012-5-28下午6:22:09
 */
public class BackgroundPane extends AbstractBasicStylePane {

    private UIComboBox typeComboBox;

    protected BackgroundQuickPane[] paneList;

    //获取当前面板
    protected BackgroundQuickPane currentPane = null;


    public BackgroundPane() {
        this.initComponents();
    }

    protected void initComponents() {
        this.setLayout(new BorderLayout(0, 6));
        typeComboBox = new UIComboBox();
        final CardLayout cardlayout = new CardLayout();

        paneList = supportKindsOfBackgroundUI();

        final JPanel centerPane = new JPanel(cardlayout) {
            @Override
            public Dimension getPreferredSize() {// AUGUST:使用当前面板的的高度
                int index = typeComboBox.getSelectedIndex();
                return new Dimension(super.getPreferredSize().width, paneList[index].getPreferredSize().height);
            }
        };
        for (BackgroundQuickPane pane : paneList) {
            typeComboBox.addItem(pane.title4PopupWindow());
            centerPane.add(pane, pane.title4PopupWindow());
        }
        typeComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                cardlayout.show(centerPane, (String) typeComboBox.getSelectedItem());
                currentPane = paneList[typeComboBox.getSelectedIndex()];
                fireStateChanged();
            }
        });

        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Fill") + "     ", SwingConstants.LEFT),typeComboBox},
                new Component[]{null, centerPane}
        };
        double[] rowSize = {p, p, p};
        double[] columnSize = {p,f};
        int[][] rowCount = {{1, 1},{1, 1},{1, 1}};
        JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_LARGE, LayoutConstants.VGAP_MEDIUM);
        this.add(panel, BorderLayout.CENTER);

    }

    protected BackgroundQuickPane[] supportKindsOfBackgroundUI() {
        java.util.List<BackgroundQuickPane> kinds = new ArrayList<>();
        kinds.add(new NullBackgroundQuickPane());
        kinds.add(new ColorBackgroundQuickPane());
        kinds.add(new TextureBackgroundQuickPane());
        kinds.add(new PatternBackgroundQuickPane());
        kinds.add(new ImageBackgroundQuickPane());
        kinds.add(new GradientBackgroundQuickPane());
        Set<BackgroundQuickUIProvider> providers = ExtraDesignClassManager.getInstance().getArray(BackgroundQuickUIProvider.MARK_STRING);
        for (BackgroundQuickUIProvider provider : providers) {
            kinds.add(provider.appearanceForBackground());

        }
        return kinds.toArray(new BackgroundQuickPane[kinds.size()]);
    }


    /**
     * 事件监听
     *
     * @param changeListener 事件
     */
    public void addChangeListener(ChangeListener changeListener) {
        listenerList.add(ChangeListener.class, changeListener);
    }

    /**
     */
    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        ChangeEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (e == null) {
                    e = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(e);
            }
        }
    }

    /**
     * 名称
     *
     * @return 名称
     */
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Background");
    }

    /**
     * Populate background.
     */
    public void populateBean(Background background) {
        resetPaneList();
        for (int i = 0; i < paneList.length; i++) {
            BackgroundQuickPane pane = paneList[i];
            if (pane.accept(background)) {
                pane.populateBean(background);
                typeComboBox.setSelectedIndex(i);
                currentPane = paneList[i];
                return;
            }
        }
    }

    private void resetPaneList() {
        for (BackgroundQuickPane pane : paneList) {
            pane.reset();
        }
    }

    /**
     * Update background.
     */
    public Background update() {
        return paneList[typeComboBox.getSelectedIndex()].updateBean();
    }

    @Override
    public void populateBean(Style style) {
        this.populateBean(style.getBackground());
    }

    @Override
    public Style update(Style style) {
        return style.deriveBackground(this.update());
    }

}