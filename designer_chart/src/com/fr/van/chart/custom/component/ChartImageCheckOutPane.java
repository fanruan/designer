package com.fr.van.chart.custom.component;

import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.plugin.chart.custom.CustomPlotFactory;
import com.fr.plugin.chart.custom.type.CustomPlotType;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.util.ArrayList;

/**
 * Created by Fangjie on 2016/4/19.
 */
public class ChartImageCheckOutPane extends BasicPane  implements UIObserver {

    private JCheckBox checkBox;
    private CustomPlotType customPlotType;
    private ArrayList<ChangeListener> changeListeners = new ArrayList<ChangeListener>();

    public ChartImageCheckOutPane(CustomPlotType type){
       this(type, false);
    }

    public ChartImageCheckOutPane( CustomPlotType type, boolean isSelected){
        this.customPlotType = type;

        initCheckBox(isSelected);

        this.add(checkBox, BorderLayout.CENTER);
    }

    public CustomPlotType getCustomPlotType() {
        return customPlotType;
    }

    private void initCheckBox(boolean isSelected) {
        this.checkBox = new JCheckBox();
        this.checkBox.setSelected(isSelected);
        //设置提示
        this.checkBox.setToolTipText(CustomPlotFactory.getTooltipText(this.customPlotType));
        //背景
        checkBox.setIcon(new ImageIcon(getClass().getResource(getIconPath(customPlotType,isSelected))));

        this.setLayout(new BorderLayout());
    }

    private String getIconPath(CustomPlotType customPlotType, boolean isSelected) {
        return isSelected ? CustomPlotFactory.getTypeIconPath(customPlotType)[0] : CustomPlotFactory.getTypeIconPath(customPlotType)[1];
    }


    public JCheckBox getCheckBox() {
        return checkBox;
    }

    public void checkIconImage(){
        checkBox.setIcon(new ImageIcon(getClass().getResource(getIconPath(customPlotType, checkBox.isSelected()))));
    }

    public void setPaneBorder(boolean isRightLine, boolean isBottomLine){
        this.setBorder(BorderFactory.createMatteBorder(1, 1, isBottomLine ? 1 : 0, isRightLine ? 1 : 0, UIConstants.LINE_COLOR));
    }

    public void setSelected(boolean isSelected){
        checkBox.setSelected(isSelected);
    }

    public boolean isSelected(){
        return checkBox.isSelected();
    }



    @Override
    protected String title4PopupWindow() {
        return null;
    }

    public void fireStateChange() {
        for (int i = 0; i < changeListeners.size(); i++) {
            changeListeners.get(i).stateChanged(new ChangeEvent(this));
        }
    }

    @Override
    public void registerChangeListener(final UIObserverListener listener) {
        changeListeners.add(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                listener.doChange();
            }
        });
    }

    @Override
    public boolean shouldResponseChangeListener() {
        return false;
    }
}
