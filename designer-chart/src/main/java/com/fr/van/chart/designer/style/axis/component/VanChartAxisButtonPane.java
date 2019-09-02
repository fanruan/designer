package com.fr.van.chart.designer.style.axis.component;

import com.fr.base.BaseUtils;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;

import com.fr.plugin.chart.VanChartAttrHelper;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.plot.VanChartAxisPlot;
import com.fr.van.chart.designer.style.axis.VanChartAxisPane;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * 坐标轴-上方按钮界面
 */
public class VanChartAxisButtonPane extends BasicBeanPane<VanChartAxisPlot> {

    private static final long serialVersionUID = -7423971487378453235L;
    private static final int B_W = 56;
    private static final int B_H = 21;
    private static final int COL_COUNT = 3;

    private UIButton addButton;
    private UIPopupMenu popupMenu;

    private List<ChartAxisButton> indexList_X = new ArrayList<ChartAxisButton>();
    private List<ChartAxisButton> indexList_Y = new ArrayList<ChartAxisButton>();
    private JPanel buttonPane;

    private VanChartAxisPane parent;

    public VanChartAxisButtonPane(VanChartAxisPane vanChartAxisPane){
        this.parent = vanChartAxisPane;
        this.setLayout(new BorderLayout());

        addButton = new AddButton(BaseUtils.readIcon("/com/fr/design/images/control/addPopup.png"));
        JPanel eastPane = new JPanel(new BorderLayout());
        eastPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 20));
        eastPane.add(addButton, BorderLayout.NORTH);
        addButton.setVisible(false);
        this.add(eastPane, BorderLayout.EAST);

        buttonPane = new JPanel();
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 0));
        this.add(buttonPane, BorderLayout.CENTER);

        addButton.addActionListener(addListener);
    }

    private void layoutPane() {
        if (buttonPane == null) {
            return;
        }
        buttonPane.removeAll();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));

        JPanel pane = null;
        int size_X = indexList_X.size();
        int size_Y = indexList_Y.size();
        for (int i = 0; i < size_X; i++) {
            if (i % COL_COUNT == 0) {
                pane = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                buttonPane.add(pane);
            }

            pane.add(indexList_X.get(i));
        }
        for (int i = 0; i < size_Y; i++) {
            if ((i + size_X) % COL_COUNT == 0) {
                pane = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                buttonPane.add(pane);
            }
            if(pane != null) {
                pane.add(indexList_Y.get(i));
            }
        }

        if(popupMenu != null){
            popupMenu.setVisible(false);
        }

        this.revalidate();
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Switch_Chart");
    }

    @Override
    public void populateBean(VanChartAxisPlot plot) {
        indexList_X.clear();
        indexList_Y.clear();
        List<VanChartAxis> xList = plot.getXAxisList();
        for(VanChartAxis axis : xList){
            ChartAxisButton x = new ChartAxisButton(plot.getXAxisName(axis));
            x.setToolTipText(plot.getXAxisName(axis));
            indexList_X.add(x);
        }
        List<VanChartAxis> yList = plot.getYAxisList();
        for(VanChartAxis axis : yList){
            ChartAxisButton y = new ChartAxisButton(plot.getYAxisName(axis));
            y.setToolTipText(plot.getYAxisName(axis));
            indexList_Y.add(y);
        }

        if(indexList_X.isEmpty()){
            this.removeAll();
        } else {
            indexList_X.get(0).setSelected(true);

            addButton.setVisible(plot.isCustomChart());
        }

        layoutPane();
    }


    @Override
    public VanChartAxisPlot updateBean() {
        return null;
    }

    public String getNewChartName(List<ChartAxisButton> existList, String prefix){
        int count = existList.size() + 1;
        while (true) {
            String name_test = prefix + count;
            boolean repeated = false;
            for (ChartAxisButton button : existList) {
                if (ComparatorUtils.equals(button.getButtonName(), name_test)) {
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

    ActionListener addListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(popupMenu == null){
                popupMenu = new UIPopupMenu();
                UIMenuItem item_x = new UIMenuItem(VanChartAttrHelper.X_AXIS_PREFIX);
                item_x.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addXAxis();
                    }
                });
                UIMenuItem item_y = new UIMenuItem(VanChartAttrHelper.Y_AXIS_PREFIX);
                item_y.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addYAxis();
                    }
                });
                popupMenu.add(item_x);
                popupMenu.add(item_y);
            }
            popupMenu.setVisible(true);
            GUICoreUtils.showPopupMenu(popupMenu, addButton, addButton.getWidth() - popupMenu.getPreferredSize().width, addButton.getY() - 1 + addButton.getHeight());
        }
    };

    private void addXAxis(){
        String name = getNewChartName(indexList_X, VanChartAttrHelper.X_AXIS_PREFIX);
        ChartAxisButton button = new ChartAxisButton(name);

        indexList_X.add(button);
        parent.addXAxis(name);

        layoutPane();
    }

    private void addYAxis(){
        String name = getNewChartName(indexList_Y, VanChartAttrHelper.Y_AXIS_PREFIX);
        ChartAxisButton button = new ChartAxisButton(name);

        indexList_Y.add(button);
        parent.addYAxis(name);

        layoutPane();
    }

    private void changeAxisSelected(String name) {
        parent.changeAxisSelected(name);
    }


    private class AddButton extends UIButton {

        public AddButton(Icon icon){
            super(icon);
        }
        /**
         * 组件是否需要响应添加的观察者事件
         *
         * @return 如果需要响应观察者事件则返回true，否则返回false
         */
        public boolean shouldResponseChangeListener() {
            return false;
        }
    }

    private class ChartAxisButton extends UIToggleButton {
        private static final double DEL_WIDTH = 10;
        private static final long serialVersionUID = -3701452534814584608L;
        private BufferedImage closeIcon = BaseUtils.readImageWithCache("com/fr/design/images/toolbarbtn/chartChangeClose.png");
        private boolean isMoveOn = false;

        private String buttonName = "";

        public ChartAxisButton(String name) {
            super(name);
            buttonName = name;
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
            int y = 1;

            g2d.drawImage(closeIcon, x, y, closeIcon.getWidth(), closeIcon.getHeight(), null);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (isMoveOn && getButtonList().size() > 1 && !VanChartAttrHelper.isDefaultAxis(this.buttonName)) {
                paintDeleteButton(g);
            }
        }

        private boolean isXAxis() {
            return VanChartAttrHelper.isXAxis(this.buttonName);
        }

        private List<ChartAxisButton> getButtonList() {
            return isXAxis() ? indexList_X : indexList_Y;
        }

        private void noSelected() {
            for(ChartAxisButton button : indexList_X){
                button.setSelected(false);
            }
            for(ChartAxisButton button : indexList_Y){
                button.setSelected(false);
            }
        }

        private void checkMoveOn(boolean moveOn) {
            for(ChartAxisButton button : indexList_X){
                button.isMoveOn = false;
            }
            for(ChartAxisButton button : indexList_Y){
                button.isMoveOn = false;
            }

            this.isMoveOn = moveOn;
        }

        private Rectangle2D getRectBounds() {
            return this.getBounds();
        }

        private void deleteAButton() {
            List<ChartAxisButton> list = getButtonList();
            if (list.contains(this) && list.size() > 1) {

                list.remove(this);
                parent.removeAxis(this.buttonName);

                if (this.isSelected()) {
                    list.get(0).setSelected(true);
                    changeAxisSelected(list.get(0).getButtonName());
                }
            }
            reLayoutPane();
        }

        private void reLayoutPane() {
            layoutPane();
        }


        protected MouseListener getMouseListener() {
            return new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mouseClick(e);
                }

                public void mouseEntered(MouseEvent e) {
                    checkMoveOn(true);
                }

                public void mouseExited(MouseEvent e) {
                    checkMoveOn(false);
                }
            };
        }

        public void mouseClick(MouseEvent e) {
            Rectangle2D bounds = getRectBounds();
            if (bounds == null) {
                return;
            }
            if (e.getX() >= bounds.getWidth() - DEL_WIDTH && !VanChartAttrHelper.isDefaultAxis(this.buttonName)) {
                deleteAButton();
                fireSelectedChanged();
                return;
            }

            if (isEnabled()) {
                noSelected();
                changeAxisSelected(getButtonName());
                setSelectedWithFireListener(true);
                fireSelectedChanged();
            }
        }

    }

}