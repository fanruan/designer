package com.fr.design.mainframe.chart.gui;

import com.fr.base.BaseUtils;
import com.fr.chart.base.AttrChangeConfig;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.chartx.attr.ChartProvider;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.DialogActionListener;
import com.fr.design.dialog.UIDialog;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.chart.gui.ChartTypePane.ComboBoxPane;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.chart.vanchart.VanChart;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
    private static final int P_W = 300;
    private static final int P_H = 400;

    private static Set<Class<? extends ChartProvider>> supportChangeConfigChartClassSet = new HashSet<Class<? extends ChartProvider>>();

    static {
        registerSupportChangeConfigChartClass(VanChart.class);
    }

    private UIButton addButton;
    private UIButton configButton;
    private ArrayList<ChartChangeButton> indexList = new ArrayList<ChartChangeButton>();

    private JPanel buttonPane;
    private ChartCollection editingCollection;
    private UIObserverListener uiobListener = null;
    private ComboBoxPane editChartType;
    private UITextField currentEditingEditor = null;

    private ChartTypePane parent = null;

    //配置窗口属性
    private UIMenuNameableCreator configCreator;

    //处理 编辑一个button时,选中另一个button的问题.
    //stopEditing不能直接relayout,否则click事件不响应了.
    //所以:stopEditing--选中其他button则响应click之后relayout;普通失焦则直接relayout.
    private boolean pressOtherButtonWhenEditing = false;

    public ChartTypeButtonPane(ChartTypePane chartTypePane){
        this();
        parent = chartTypePane;
    }

    public ChartTypeButtonPane() {
        this.setLayout(new BorderLayout());
        addButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/add.png"));
        configButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/config.png"));

        buttonPane = new JPanel();
        this.add(buttonPane, BorderLayout.CENTER);

        JPanel eastPane = new JPanel();
        this.add(eastPane, BorderLayout.EAST);

        eastPane.setLayout(new BorderLayout());

        eastPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 5));
        JPanel button = new JPanel();
        button.setPreferredSize(new Dimension(45, 20));
        button.setLayout(new GridLayout(1, 2, 5, 0));
        button.add(addButton);
        button.add(configButton);
        eastPane.add(button, BorderLayout.NORTH);

        initAddButton();
        initConfigButton();
        initConfigCreator();

     //   Toolkit.getDefaultToolkit().addAWTEventListener(awt, AWTEvent.MOUSE_EVENT_MASK);
    }

    public static void registerSupportChangeConfigChartClass(Class<? extends ChartProvider> cls) {
        supportChangeConfigChartClassSet.add(cls);
    }

    private void initConfigCreator() {
        configCreator = new UIMenuNameableCreator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Change_Config_Attributes"), new AttrChangeConfig(), ChangeConfigPane.class);
    }

    private void initAddButton() {
        addButton.setPreferredSize(new Dimension(20, 20));
        addButton.addActionListener(addListener);
    }

    private void initConfigButton() {
        configButton.setPreferredSize(new Dimension(20, 20));
        configButton.addActionListener(configListener);
    }

    ActionListener addListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = getNewChartName();
            ChartChangeButton button = new ChartChangeButton(name);// some set selected

            button.registerChangeListener(uiobListener);

            indexList.add(button);

            if (editingCollection != null) {
                //点击添加按钮，则会触发切换状态
                ChartProvider chart = getChangeStateNewChart();
                try {
                    ChartProvider newChart = (ChartProvider) chart.clone();
                    editingCollection.addNamedChart(name, newChart);
                } catch (CloneNotSupportedException e1) {
                    FineLoggerFactory.getLogger().error("Error in Clone");
                }
                checkoutChange();
            }
            layoutPane(buttonPane);
        }
    };

    //获取图表收集器的状态
    private void checkoutChange(){
        calculateMultiChartMode();
        if (parent != null){
            parent.relayoutChartTypePane(editingCollection);
        }
        //检查是否可以配置切换
        configButton.setEnabled(changeEnable());
    }

    /**
     * 获取切花状态下的图表
     *
     * @return
     */
    public ChartProvider getChangeStateNewChart() {
        ChartProvider chart = editingCollection.getTheSelectedChart(ChartProvider.class);
        String chartID = chart.getID();
        String priority = ChartTypeManager.getInstanceWithCheck().getPriority(chartID);
        return ChartTypeManager.getInstanceWithCheck().getFirstChart(priority);
    }

    //图表收集器模式状态改变
    private void calculateMultiChartMode() {
        //设置切换功能是否可用
        editingCollection.getChangeConfigAttr().setEnable(changeEnable());
    }

    /**
     * 是否支持图表切换的配置
     *
     * @return
     */
    private boolean changeEnable() {
        return editingCollection.getChartCount() > 1
                && supportChange();
    }

    private boolean supportChange() {
        return supportChangeConfigChartClassSet.contains(editingCollection.getTheSelectedChart(ChartProvider.class).getClass());
    }

    ActionListener configListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            UIMenuNameableCreator ui = configCreator.clone();
            final BasicBeanPane pane = ui.getPane();
            pane.populateBean(editingCollection);
            UIDialog dialog = pane.showUnsizedWindow(SwingUtilities.getWindowAncestor(parent), new DialogActionListener() {
                @Override
                public void doOk() {
                    pane.updateBean(editingCollection);
                }

                @Override
                public void doCancel() {

                }
            });
            dialog.setSize(P_W, P_H);
            dialog.setVisible(true);
        }
    };

    private String getNewChartName() {
        int count = indexList.size() + 1;
        while (true) {
            String name_test = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Module_Name") + count;
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
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Switch_Chart");
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
            //切换时重新更新整个面板
            if (parent != null) {
                parent.populate(editingCollection);
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
        checkConfigButtonVisible();
        //更新切换面板
        checkoutChange();
    }

    private void checkConfigButtonVisible() {
        addButton.setVisible(ChartTypeManager.enabledChart(editingCollection.getTheSelectedChart(ChartProvider.class).getID()));
        //新建一个collection
        if (editingCollection.getChartCount() == 1 && editingCollection.getTheSelectedChart(ChartProvider.class) != null) {
            //Chart 不支持图表切换
            configButton.setVisible(supportChange());
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
            ChartChangeButton button = indexList.get(selectedIndex);
            button.isMoveOn = false;
            if (!ComparatorUtils.equals(editingCollection.getChartName(selectedIndex), newName)) {
                editingCollection.setChartName(selectedIndex, newName);
                HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().fireTargetModified();
                button.changeChartName(newName);
            }
            buttonPane.remove(currentEditingEditor);
            currentEditingEditor = null;

            if(!pressOtherButtonWhenEditing) {
                layoutPane(buttonPane);
            }
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
            nameField.addActionListener(new ActionListener() {//enter
                @Override
                public void actionPerformed(ActionEvent e) {
                    pressOtherButtonWhenEditing = false;
                    stopEditing();
                }
            });

            nameField.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {

                }

                @Override
                public void focusLost(FocusEvent e) {//编辑状态lost才走这边
                    if (currentEditingEditor != null ) {
                        stopEditing();
                    }
                }
            });
        }

        public String getButtonName() {
            return buttonName;
        }

        private void changeChartName(String name) {
            this.setText(name);
            buttonName = name;
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
            //先重构属性，在重构面板，否则面板在重构过程中，会重新将属性中的切换图表加到indexList中，导致面板无法删除
            //记录改变前的plotID
            String lastPlotID = editingCollection == null ? StringUtils.EMPTY : editingCollection.getTheSelectedChart(ChartProvider.class).getID();
            if (editingCollection != null) {
                int count = editingCollection.getChartCount();
                for (int i = 0; i < count; i++) {
                    if (ComparatorUtils.equals(getButtonName(), editingCollection.getChartName(i))) {
                        editingCollection.removeNameObject(i);
                        break;
                    }
                }
            }

            if (indexList.contains(this) && indexList.size() > 1) {
                indexList.remove(this);
                if (this.isSelected()) {
                    indexList.get(0).setSelected(true);
                    changeCollectionSelected(indexList.get(0).getButtonName());
                }
            }

            //获取图表收集器的状态
            checkoutChange();

            relayoutPane();

            //重构面板
            if (parent != null ){
                parent.reLayoutEditPane(lastPlotID, editingCollection);
            }
        }

        private void relayoutPane() {
            layoutPane(buttonPane);
        }


        protected MouseListener getMouseListener() {
            return new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mouseClick(e);
                    if(pressOtherButtonWhenEditing){
                        relayoutPane();
                        pressOtherButtonWhenEditing = false;
                    }
                }

                public void mouseEntered(MouseEvent e) {
                    checkMoveOn(true);
                    pressOtherButtonWhenEditing = currentEditingEditor != null;
                }

                public void mouseExited(MouseEvent e) {
                    checkMoveOn(false);
                    pressOtherButtonWhenEditing = false;
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
                //记录改变前的plotID
                String lastPlotID = editingCollection == null ? StringUtils.EMPTY : editingCollection.getTheSelectedChart(ChartProvider.class).getID();
                changeCollectionSelected(getButtonName());
                setSelectedWithFireListener(true);
                fireSelectedChanged();

                //需要先更新，最后重构面板
                //重构面板
                if (parent != null ){
                    parent.reLayoutEditPane(lastPlotID, editingCollection);
                }
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