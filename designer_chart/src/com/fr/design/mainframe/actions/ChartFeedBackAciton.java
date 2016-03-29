package com.fr.design.mainframe.actions;

import com.fr.design.actions.help.FeedBackAction;
import com.fr.design.actions.help.FeedBackPane;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public class ChartFeedBackAciton extends FeedBackAction{

    /**
     * 动作
     * @param e 事件
     */
    public void actionPerformed(ActionEvent e) {
        final DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
        ChartFeedBackPane feedBackPane = new ChartFeedBackPane();
        BasicDialog basicDialog =feedBackPane.showWindow(designerFrame,false);
        feedBackPane.setFeedbackDialog(basicDialog);
        basicDialog.setVisible(true);
    }

    private class ChartFeedBackPane extends FeedBackPane{
        protected JPanel getContactPane() {
            double f = TableLayout.FILL;
            double p = TableLayout.PREFERRED;
            Component[][] components = new Component[][]{
                    new Component[]{new UILabel(Inter.getLocText("email") + ":", SwingConstants.RIGHT), email},
                    new Component[]{new UILabel(Inter.getLocText("mobile_number") + ":", SwingConstants.RIGHT), phone}
            };
            double[] rowSize = {p, p, p};
            double[] columnSize = {p, p};
            int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}};
            return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM);
        }
    }
}