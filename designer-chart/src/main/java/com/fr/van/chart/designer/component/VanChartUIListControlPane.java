package com.fr.van.chart.designer.component;

import com.fr.base.chart.BasePlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.controlpane.UIListControlPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.gui.GUICoreUtils;

import com.fr.stable.Nameable;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by mengao on 2017/9/8.
 * 新图表UIListControlPane，基础面板。
 */
public abstract class VanChartUIListControlPane extends UIListControlPane implements UIObserver {
    private UIObserverListener uiObserverListener;


    public VanChartUIListControlPane() {
        super();
        this.setBorder(null);
        iniListener();
    }

    public VanChartUIListControlPane(BasePlot plot) {
        super(plot);
        this.setBorder(null);
        iniListener();
    }

    /**
     * 注册观察者监听事件
     * @param listener 观察者监听事件
     */
    @Override
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;
    }

    @Override
    public boolean shouldResponseChangeListener() {
        return true;
    }

    private void iniListener() {
        if (shouldResponseChangeListener()) {
            this.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (uiObserverListener == null) {
                        return;
                    }
                    uiObserverListener.doChange();
                }
            });
        }
    }

    protected void fireChanged() {
        Object[] listeners = listenerList.getListenerList();

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                ((ChangeListener) listeners[i + 1]).stateChanged(new ChangeEvent(this));
            }
        }

    }

    /**
     * 增加监听事件
     * @param l 监听的对象
     */
    public void addChangeListener(ChangeListener l) {
        this.listenerList.add(ChangeListener.class, l);
    }

    @Override
    protected JPanel getLeftTopPane(UIToolbar topToolBar) {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        double[] rowSize = {p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(getAddItemText()), topToolBar},
        };
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    @Override
    public void saveSettings() {
        if (isPopulating) {
            return;
        }
        update((Plot) plot);
        fireChanged();//图表属性改变，响应事件
    }

    protected abstract void update(Plot plot);


    //-------------------连续弹窗问题  start-------------------//

    public void populate(Nameable[] nameableArray) {
        //特殊处理，使用instanceof判断，弹出不同的面板
        if (SwingUtilities.getWindowAncestor(this) instanceof JDialog) {
            popupEditDialog = new HyperDialog(cardPane);
        }
        super.populate(nameableArray);
    }

    protected void popupEditDialog(Point mousePos) {
        //特殊处理，处理连续弹窗情况，弹出面板定为方式不同
        if (SwingUtilities.getWindowAncestor(this) instanceof JDialog) {
            GUICoreUtils.centerWindow(popupEditDialog);
            popupEditDialog.setVisible(true);
            return;
        }
        super.popupEditDialog(mousePos);
    }

    // 点击"编辑"按钮，弹出面板（这个面板是弹窗里面的弹窗）
    protected class HyperDialog extends JDialog {
        private JComponent editPane;
        private static final int WIDTH = 570;
        private static final int HEIGHT = 490;

        private UIButton okButton, cancelButton;


        HyperDialog(JComponent pane) {
            super(DesignerContext.getDesignerFrame(), true);
            pane.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
            this.editPane = pane;
            JPanel editPaneWrapper = new JPanel(new BorderLayout());
            editPaneWrapper.add(editPane, BorderLayout.CENTER);
            this.getContentPane().add(editPaneWrapper, BorderLayout.CENTER);
            this.getContentPane().add(createControlButtonPane(), BorderLayout.SOUTH);
            setSize(WIDTH, HEIGHT);
            this.setVisible(false);
        }

        private JPanel createControlButtonPane() {
            JPanel controlPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

            JPanel buttonsPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            controlPane.add(buttonsPane, BorderLayout.EAST);

            //确定
            addOkButton(buttonsPane);
            //取消
            addCancelButton(buttonsPane);

            controlPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

            return controlPane;
        }

        private void addCancelButton(JPanel buttonsPane) {
            cancelButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Cancel"));
            buttonsPane.add(cancelButton);
            cancelButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    doCancel();
                }
            });
        }

        private void addOkButton(JPanel buttonsPane) {
            okButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_OK"));
            buttonsPane.add(okButton);
            okButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    doOK();
                }
            });
        }

        public void doOK() {
            saveSettings();
            setVisible(false);
        }

        public void doCancel() {
            setVisible(false);
        }
    }

}
