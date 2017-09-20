package com.fr.plugin.chart.designer.component;

import com.fr.base.chart.BasePlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.controlpane.UIListControlPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.stable.Nameable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by mengao on 2017/9/8.
 * 新图表UIListControlPane，基础面板。
 */
public abstract class VanChartUIListControlPane extends UIListControlPane {

    public VanChartUIListControlPane() {
        super();
        this.setBorder(null);
    }

    public VanChartUIListControlPane(BasePlot plot) {
        super(plot);
        this.setBorder(null);
    }

    @Override
    protected JPanel getLeftTopPane (UIToolbar topToolBar) {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        double[] rowSize = {p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(getAddItemText()), topToolBar},
        };
        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
    }

    @Override
    public void saveSettings() {
        if (isPopulating) {
            return;
        }
        update((Plot) plot);
        DesignerContext.getDesignerFrame().getSelectedJTemplate().fireTargetModified();
    }

    protected abstract void update(Plot plot);


    //-------------------连续弹窗问题  start-------------------//

    public void populate (Nameable[] nameableArray) {
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

            controlPane.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));

            return controlPane;
        }

        private void addCancelButton(JPanel buttonsPane) {
            cancelButton = new UIButton(Inter.getLocText("Cancel"));
            buttonsPane.add(cancelButton);
            cancelButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    doCancel();
                }
            });
        }

        private void addOkButton(JPanel buttonsPane) {
            okButton = new UIButton(Inter.getLocText("OK"));
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
