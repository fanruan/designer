package com.fr.design.mainframe.chart.gui;

import com.fr.base.BaseUtils;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.charttypes.ColumnIndependentChart;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.chart.gui.ChartTypePane.ComboBoxPane;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * 图表 类型 增删 控制按钮界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-9-26 上午09:27:49
 */
public class ChartTypeButtonPane extends BasicBeanPane<ChartCollection> implements UIObserver {
    private static final long serialVersionUID = -8130803225718028933L;
    private static final int B_W = 52;
    private static final int B_H = 20;
    private static final int COL_COUNT = 3;

    private UIButton addButton;
    private ArrayList<ChartChangeButton> indexList = new ArrayList<ChartChangeButton>();

    private JPanel buttonPane;
    private ChartCollection editingCollection;
    private UIObserverListener uiobListener = null;
    private ComboBoxPane editChartType;
    private UITextField currentEditingEditor = null;

    private boolean mouseOnChartTypeButtonPane = false;

    /**
     * 鼠标事件是否在这个面板
     * @return 返回是否
     */
    public boolean isMouseOnChartTypeButtonPane() {
        return this.mouseOnChartTypeButtonPane;
    }

    private AWTEventListener awt = new AWTEventListener() {
        public void eventDispatched(AWTEvent event) {
            //没有进行鼠标点击，则返回
            if (event instanceof MouseEvent && ((MouseEvent) event).getClickCount() > 0) {
                if (currentEditingEditor != null && !ComparatorUtils.equals(event.getSource(), currentEditingEditor)) {
                    stopEditing();
                    if (event.getSource() instanceof ChartChangeButton) {
                        ((ChartChangeButton) event.getSource()).mouseClick((MouseEvent) event);
                    }
                    populateBean(editingCollection);
                }
            }
        }
    };

    public ChartTypeButtonPane() {
        this.setLayout(new BorderLayout());
        addButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/add.png"));

        buttonPane = new JPanel();
        this.add(buttonPane, BorderLayout.CENTER);

        JPanel eastPane = new JPanel();
        this.add(eastPane, BorderLayout.EAST);

        eastPane.setLayout(new BorderLayout());

        eastPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 20));
        eastPane.add(addButton, BorderLayout.NORTH);

        addButton.setPreferredSize(new Dimension(20, 20));
        addButton.addActionListener(addListener);
        addButton.addMouseListener(mouseListener);
        Toolkit.getDefaultToolkit().addAWTEventListener(awt, AWTEvent.MOUSE_EVENT_MASK);
    }

    ActionListener addListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            mouseOnChartTypeButtonPane = true;
            String name = getNewChartName();
            ChartChangeButton button = new ChartChangeButton(name);// some set selected

            button.registerChangeListener(uiobListener);

            indexList.add(button);

            if (editingCollection != null) {
                Chart[] barChart = ColumnIndependentChart.columnChartTypes;
                try {
                    Chart newChart = (Chart) barChart[0].clone();
                    editingCollection.addNamedChart(name, newChart);
                    editingCollection.addFunctionRecord(newChart);
                } catch (CloneNotSupportedException e1) {
                    FRLogger.getLogger().error("Error in Clone");
                }

            }
            layoutPane(buttonPane);
        }
    };

    MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
            mouseOnChartTypeButtonPane = false;
        }
    };

    private String getNewChartName() {
        int count = indexList.size() + 1;
        while (true) {
            String name_test = Inter.getLocText("FR-Chart-Module_Name") + count;
            boolean repeated = false;
            for (int i = 0, len = indexList.size(); i < len; i++) {
                ChartChangeButton nameable = indexList.get(i);
                if (ComparatorUtils.equals(nameable.getButtonName(), name_test)) {
                    repeated = true;
                    break;
                }
            }

            if (!repeated) {
                return name_test;
            }
            count++;
        }
    }

    private void layoutPane(JPanel northPane) {
        if (northPane == null) {
            return;
        }
        northPane.removeAll();
        northPane.setLayout(new BoxLayout(northPane, BoxLayout.Y_AXIS));

        JPanel pane = null;
        for (int i = 0; i < indexList.size(); i++) {
            if (i % COL_COUNT == 0) {
                pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
                northPane.add(pane);
            }

            pane.add(indexList.get(i));
        }

        this.revalidate();
    }

    private void layoutRenamingPane(JPanel northPane, int index) {
        if (northPane == null) {
            return;
        }
        northPane.removeAll();
        northPane.setLayout(new BoxLayout(northPane, BoxLayout.Y_AXIS));

        JPanel pane = null;

        for (int i = 0; i < indexList.size(); i++) {
            if (i % COL_COUNT == 0) {
                pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
                northPane.add(pane);
            }
            if (i != index) {
                pane.add(indexList.get(i));
            } else {
                pane.add(currentEditingEditor);
            }
        }
        this.revalidate();
    }

    /**
     * 注册监听器
     * @param listener 观察者监听事件
     */
    public void registerChangeListener(UIObserverListener listener) {
        uiobListener = listener;
    }

    /**
     * 是否应该响应事件监听器
     * @return 是则返回true
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Chart-Types_Switch");
    }

    private void changeCollectionSelected(String name) {
        if (editingCollection != null) {
            int count = editingCollection.getChartCount();
            for (int i = 0; i < count; i++) {
                if (ComparatorUtils.equals(name, editingCollection.getChartName(i))) {
                    editingCollection.setSelectedIndex(i);
                    break;
                }
            }
            if (editChartType != null) {
                editChartType.populateBean(editingCollection.getSelectedChart());
            }
        }
    }

    /**
     * 设置当前对应的编辑Type
     *
     * @param chartPane
     */
    public void setEditingChartPane(ComboBoxPane chartPane) {
        editChartType = chartPane;
    }

    @Override
    public void populateBean(ChartCollection collection) {
        editingCollection = collection;

        indexList.clear();
        int count = collection.getChartCount();
        int select = collection.getSelectedIndex();
        for (int i = 0; i < count; i++) {
            ChartChangeButton button = new ChartChangeButton(collection.getChartName(i));
            indexList.add(button);
            button.setSelected(i == select);
            button.registerChangeListener(uiobListener);
        }

        layoutPane(buttonPane);
        checkAddButtonVisible();
    }

    private void checkAddButtonVisible() {
        addButton.setVisible(true);
        //新建一个collection
        if(editingCollection != null && editingCollection.getChartCount() == 1){
            //vanChart 不支持图表切换 目前
            if(!ComparatorUtils.equals(editingCollection.getSelectedChart().getClass(), Chart.class)){
                addButton.setVisible(false);
            }
        }
    }

    @Override
    public ChartCollection updateBean() {
        return null;// no use
    }

    /**
     * 保存 属性表属性.
     */
    public void update(ChartCollection collection) {
        // 什么也不做, 在button操作点击等时 已经处理.
    }


    private void stopEditing() {
        if (currentEditingEditor != null) {
            String newName = currentEditingEditor.getText();
            int selectedIndex = editingCollection.getSelectedIndex();
            if (!ComparatorUtils.equals(editingCollection.getChartName(selectedIndex), newName)) {
                editingCollection.setChartName(selectedIndex, newName);
                HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().fireTargetModified();
            }
            buttonPane.remove(currentEditingEditor);
            currentEditingEditor = null;
        }
    }


    private class ChartChangeButton extends UIToggleButton {
        private static final double DEL_WIDTH = 10;
        private BufferedImage closeIcon = BaseUtils.readImageWithCache("com/fr/design/images/toolbarbtn/chartChangeClose.png");
        private boolean isMoveOn = false;

        private String buttonName = "";
        private UITextField nameField = new UITextField();

        public ChartChangeButton(String name) {
            super(name);

            buttonName = name;
            this.setToolTipText(name);
            nameField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    stopEditing();
                    populateBean(editingCollection);
                }
            });
        }

        public String getButtonName() {
            return buttonName;
        }

        public Dimension getPreferredSize() {
            return new Dimension(B_W, B_H);
        }

        private void paintDeleteButton(Graphics g2d) {
            Rectangle2D bounds = this.getBounds();

            int x = (int) (bounds.getWidth() - DEL_WIDTH);
            int y = (int) (1);

            g2d.drawImage((Image) closeIcon, x, y, closeIcon.getWidth(), closeIcon.getHeight(), null);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (isMoveOn && indexList.size() > 1) {
                paintDeleteButton(g);
            }
        }

        private void noSelected() {
            for (int i = 0, size = indexList.size(); i < size; i++) {
                indexList.get(i).setSelected(false);
            }
        }

        private void checkMoveOn(boolean moveOn) {
            for (int i = 0; i < indexList.size(); i++) {
                indexList.get(i).isMoveOn = false;
            }

            this.isMoveOn = moveOn;
        }

        private Rectangle2D getRectBounds() {
            return this.getBounds();
        }

        private void deleteAButton() {
            if (indexList.contains(this) && indexList.size() > 1) {
                indexList.remove(this);

                if (this.isSelected()) {
                    indexList.get(0).setSelected(true);
                    changeCollectionSelected(indexList.get(0).getButtonName());
                }

                if (editingCollection != null) {
                    int count = editingCollection.getChartCount();
                    for (int i = 0; i < count; i++) {
                        if (ComparatorUtils.equals(getButtonName(), editingCollection.getChartName(i))) {
                            editingCollection.removeNameObject(i);
                            break;
                        }
                    }
                }
            }

            relayoutPane();
        }

        private void relayoutPane() {
            layoutPane(buttonPane);
        }


        protected MouseListener getMouseListener() {
            return new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mouseClick(e);
                    mouseOnChartTypeButtonPane = true;
                }

                public void mouseEntered(MouseEvent e) {
                    checkMoveOn(true);
                    mouseOnChartTypeButtonPane = true;
                }

                public void mouseExited(MouseEvent e) {
                    checkMoveOn(false);
                    mouseOnChartTypeButtonPane = false;
                }
            };
        }


        public void mouseClick(MouseEvent e) {
            Rectangle2D bounds = getRectBounds();
            if (bounds == null) {
                return;
            }
            if (e.getX() >= bounds.getWidth() - DEL_WIDTH) {
                deleteAButton();
                fireSelectedChanged();
                return;
            }

            if (isSelected()) {
                doWithRename();
                return;
            }

            //第一次选择

            if (isEnabled()) {
                noSelected();
                changeCollectionSelected(getButtonName());
                setSelectedWithFireListener(true);
                fireSelectedChanged();
            }
        }

        private void doWithRename() {
            currentEditingEditor = this.nameField;
            Rectangle bounds = this.getBounds();
            currentEditingEditor.setPreferredSize(new Dimension((int) bounds.getWidth(), (int) bounds.getHeight()));
            currentEditingEditor.setText(this.getButtonName());
            buttonPane.repaint();
            layoutRenamingPane(buttonPane, editingCollection.getSelectedIndex());
            currentEditingEditor.requestFocus();
        }
    }
}