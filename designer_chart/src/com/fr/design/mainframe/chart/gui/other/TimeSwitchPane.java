package com.fr.design.mainframe.chart.gui.other;

import com.fr.base.BaseUtils;
import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.base.TimeSwitchAttr;
import com.fr.chart.chartattr.Plot;
import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public class TimeSwitchPane extends JPanel implements UIObserver {
    private static final int TICK_WIDTH = 84;
    private static final int TICK_HEIGHT = 20;
    private static final int COM_GAP =5;
    private static final String YEAR = Inter.getLocText("Year");
    private static final String MONTH =Inter.getLocText("Month");
    private static final String DAY =Inter.getLocText("Sun");
    private static final String HOUR = Inter.getLocText("Sche-Hour");
    private static final String MINUTE = Inter.getLocText("Sche-Minute");
    private static final String SECOND = Inter.getLocText("Sche-Second");

    private static String[] TYPES = new String[]{
    	Inter.getLocText("Year"), Inter.getLocText("Month"), Inter.getLocText("Sun"),
    	Inter.getLocText("Sche-Hour"), Inter.getLocText("Sche-Minute"),
    	Inter.getLocText("Sche-Second")
    };

    private static Map<String, Integer> VALUES = new HashMap<String, Integer>();
    static {
    	VALUES.put(YEAR, ChartConstants.YEAR_TYPE);
    	VALUES.put(MONTH, ChartConstants.MONTH_TYPE);
    	VALUES.put(DAY, ChartConstants.DAY_TYPE);
    	VALUES.put(HOUR, ChartConstants.HOUR_TYPE);
    	VALUES.put(MINUTE, ChartConstants.MINUTE_TYPE);
    	VALUES.put(SECOND, ChartConstants.SECOND_TYPE);
    }

    private static Map<Integer, String> INTS = new HashMap<Integer, String>();
    static {
    	INTS.put(ChartConstants.YEAR_TYPE, Inter.getLocText("Year"));
    	INTS.put(ChartConstants.MONTH_TYPE, Inter.getLocText("Month"));
    	INTS.put(ChartConstants.DAY_TYPE, Inter.getLocText("Sun"));
    	INTS.put(ChartConstants.HOUR_TYPE, Inter.getLocText("Sche-Hour"));
    	INTS.put(ChartConstants.MINUTE_TYPE, Inter.getLocText("Sche-Minute"));
    	INTS.put(ChartConstants.SECOND_TYPE, Inter.getLocText("Sche-Second"));
    }

    private UIButton addButton;
    private JPanel tablePane;
    private UIObserverListener observerListener;

    public TimeSwitchPane(){
        initTablePane();
        initAddButton();
        this.setLayout(new Layout());
        this.add(addButton);
        this.add(tablePane);
        initSelfListener(tablePane);
        initSelfListener(addButton);
    }

    private void initTablePane(){
        tablePane = new JPanel(new TableLayout());
        tablePane.add(new TimeTickBox(new Formula("1"),ChartConstants.MONTH_TYPE));
        tablePane.add(new TimeTickBox(new Formula("3"),ChartConstants.MONTH_TYPE));
        tablePane.add(new TimeTickBox(new Formula("6"),ChartConstants.MONTH_TYPE));
        tablePane.add(new TimeTickBox(new Formula("1"), ChartConstants.YEAR_TYPE));
        tablePane.revalidate();
    }


    protected void initAddButton() {
        addButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/add.png")) {
            public boolean shouldResponseChangeListener() {
                return false;
            }
        };
        addButton.setBorderType(UIButton.OTHER_BORDER);
        addButton.setOtherBorder(UIConstants.BS, UIConstants.LINE_COLOR);
        addButton.addActionListener(getAddButtonListener());
    }

    /**
     * @return 添加按钮的事件接口
     */
    protected ActionListener getAddButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tablePane.add(new TimeTickBox(new Formula(),ChartConstants.YEAR_TYPE));
                tablePane.revalidate();
                fireChange();
            }
        };
    }


    protected void deleteTimeTick(TimeTickBox tickBox){
        tablePane.remove(tickBox);
        tablePane.revalidate();
        fireChange();
    }


    public void populate(Plot plot){
        ArrayList<TimeSwitchAttr> timeMap =plot.getxAxis().getTimeSwitchMap();
        if(timeMap == null || timeMap.isEmpty()){
            return;
        }
        tablePane.removeAll();
        for(TimeSwitchAttr attr:timeMap){
            tablePane.add(new TimeTickBox(attr.getTimeUnit(),attr.getUnit()));
        }
        tablePane.revalidate();
        initSelfListener(tablePane);
    }

    public void update(Plot plot){
        ArrayList<TimeSwitchAttr> timeMap =plot.getxAxis().getTimeSwitchMap();
        if(timeMap == null){
            return;
        }
        timeMap.clear();
        for(Component component:tablePane.getComponents()){
            if(!(component instanceof TimeTickBox)){
               continue;
            }
            TimeTickBox box =(TimeTickBox) component;
            timeMap.add(new TimeSwitchAttr(new Formula(box.mainUnitField.getText()),VALUES.get(box.mainType.getSelectedItem())));
        }
    }

    private void fireChange(){
        observerListener.doChange();
        HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().fireTargetModified();
    }


    /**
   	 * 给组件登记一个观察者监听事件
   	 *
   	 * @param listener 观察者监听事件
   	 */
   	public void registerChangeListener(final UIObserverListener listener){
         this.observerListener = listener;
    }

   	/**
   	 * 组件是否需要响应添加的观察者事件
   	 *
   	 * @return 如果需要响应观察者事件则返回true，否则返回false
   	 */
   	public boolean shouldResponseChangeListener(){
        return true;
    }

    private class TableLayout implements LayoutManager{

        @Override
        public void addLayoutComponent(String name, Component comp) {

        }

        @Override
        public void removeLayoutComponent(Component comp) {

        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return new Dimension(parent.getWidth(),(COM_GAP+TICK_HEIGHT)*parent.getComponentCount());
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return preferredLayoutSize(parent);
        }

        @Override
        public void layoutContainer(Container parent) {
            int y = 0;
            int count = parent.getComponents().length;
            for(Component component:parent.getComponents()){
                component.setEnabled(count > 1);
                component.setBounds(0,y,parent.getWidth(),TICK_HEIGHT);
                y +=COM_GAP+TICK_HEIGHT;
            }
        }
    }

    private class Layout implements LayoutManager {

        @Override
        /**
         *
         */
        public void addLayoutComponent(String name, Component comp) {

        }

        @Override
        /**
         *
         */
        public void removeLayoutComponent(Component comp) {

        }

        @Override
        /**
         *
         */
        public Dimension preferredLayoutSize(Container parent) {
            int h = addButton.getPreferredSize().height + tablePane.getPreferredSize().height;
            return new Dimension(parent.getWidth(), h + 2);
        }

        @Override
        /**
         *
         */
        public Dimension minimumLayoutSize(Container parent) {
            return preferredLayoutSize(parent);
        }

        @Override
        /**
         *
         */
        public void layoutContainer(Container parent) {
            int width = parent.getWidth();
            int y = 0;
            tablePane.setBounds(0, y, width, tablePane.getPreferredSize().height);
            y += tablePane.getPreferredSize().height + 2;
            addButton.setBounds(COM_GAP, y, width-COM_GAP*2, addButton.getPreferredSize().height);
        }
    }


    private class TimeTickBox extends JPanel{

        private UITextField mainUnitField;
        private UIComboBox mainType;
        private UIButton delButton;

         public TimeTickBox(Formula time,int unit){
             this.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 0));
             mainUnitField = new UITextField(time.toString());
             mainUnitField.setEditable(false);
             mainUnitField.setPreferredSize(new Dimension(TICK_WIDTH, TICK_HEIGHT));
             mainType = new UIComboBox(TYPES);
             mainType.setSelectedItem(INTS.get(unit));
             delButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/toolbarbtn/close.png"));
             initListeners();
             this.add(mainUnitField);
             this.add(mainType);
             this.add(delButton);
         }

        private void initListeners(){
            mainUnitField.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    showFormulaPane(mainUnitField);
                }
            });
            delButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteTimeTick(TimeTickBox.this);
                }
            });
            mainType.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    fireChange();
                }
            });
        }


        public void setEnabled(boolean b) {
            this.delButton.setEnabled(b);
        }

        private void showFormulaPane(final UITextField jTextField) {
            final UIFormula formulaPane = FormulaFactory.createFormulaPane();
            final String original = jTextField.getText();
            formulaPane.populate(new Formula(original));
            BasicDialog dlg = formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor(TimeSwitchPane.this), new DialogActionAdapter() {
                public void doOk() {
                    String newText = Utils.objectToString(formulaPane.update());
                    jTextField.setText(newText);
                    if(!ComparatorUtils.equals(original,newText)){
                        fireChange();
                    }
                }
            });
            dlg.setVisible(true);
        }
    }

    private void initSelfListener(Container parentComponent) {
        for (int i = 0; i < parentComponent.getComponentCount(); i++) {
            Component tmpComp = parentComponent.getComponent(i);
            if (tmpComp instanceof Container) {
                initSelfListener((Container) tmpComp);
            }
            if (tmpComp instanceof UIObserver) {
                ((UIObserver) tmpComp).registerChangeListener(observerListener);
            }
        }
    }
}