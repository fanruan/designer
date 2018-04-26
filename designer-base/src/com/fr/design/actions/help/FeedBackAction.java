package com.fr.design.actions.help;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-3-18
 * Time: 上午9:09
 */
public class FeedBackAction extends UpdateAction {

    public FeedBackAction() {
        this.setMenuKeySet(FEED_BACK);
        this.setName(getMenuKeySet().getMenuName());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_help/feedback.png"));
    }

    /**
     * 动作
     * @param e 事件
     */
    public void actionPerformed(ActionEvent e) {
        final DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
        FeedBackPane feedBackPane = new FeedBackPane();
        BasicDialog basicDialog =feedBackPane.showWindow(designerFrame,false);
        feedBackPane.setFeedbackDialog(basicDialog);
        basicDialog.setVisible(true);
    }

    public static final MenuKeySet FEED_BACK = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 0;
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("product_feedback");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };
}