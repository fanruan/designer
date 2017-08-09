package com.fr.plugin.chart.layout;

import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by hufan on 2016/10/25.
 */
public class VanChartCardLayoutPane extends JPanel{
    private CardLayout cardLayout;
    private String selectedKey = StringUtils.EMPTY; //选中的面板ID
    private Map<String, Component> cards = new HashMap<String, Component>();
    public VanChartCardLayoutPane(){
        super();
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);
    }

    public VanChartCardLayoutPane(Map<String, Component> cards, String selectedKey){
        this();
        addPaneList(cards);
        updatePane(selectedKey);
    }

    public void addPaneList(Map<String, Component> componentMap){
        this.cards = componentMap;
        Iterator iterator = componentMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            String paneID = (String) entry.getKey();
            Component component = (Component) entry.getValue();
            this.add(component, paneID);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (cards.containsKey(selectedKey)) {
            Component component = cards.get(selectedKey);
            return component.getPreferredSize();
        }
        return new Dimension(0, 0);
    }

    private void checkCardPane() {
        if (cards.containsKey(selectedKey)){
            cardLayout.show(this, selectedKey);
        }
    }

    public void updatePane(String selectedKey) {
        this.selectedKey = selectedKey;
        checkCardPane();
    }
}
