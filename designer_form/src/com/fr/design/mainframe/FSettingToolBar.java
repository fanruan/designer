package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by harry on 2017-3-2.
 */
public class FSettingToolBar extends JPanel {
    private Icon setIcon = BaseUtils.readIcon("com/fr/design/images/toolbarbtn/toolbarbtnsetting.png");
    private Icon delIcon = BaseUtils.readIcon("com/fr/design/images/toolbarbtn/toolbarbtnclear.png");
    private UIButton setButton;
    private UIButton delButton;
    private FToolBarPane toolBarPane;

    public FSettingToolBar(String name,FToolBarPane toolBarPane) {
        super();
        this.setBackground(Color.lightGray);
        this.add(new UILabel(name));
        this.toolBarPane = toolBarPane;
        setButton = GUICoreUtils.createTransparentButton(setIcon, setIcon, setIcon);
        setButton.setToolTipText(Inter.getLocText("Edit_Button_ToolBar"));
        setButton.setAction(new SetAction());
        delButton = GUICoreUtils.createTransparentButton(delIcon, delIcon, delIcon);
        delButton.setToolTipText(Inter.getLocText("Remove_Button_ToolBar"));
        delButton.setAction(new DelAction());
        this.add(setButton);
        this.add(delButton);
    }

    public void setEnabled(boolean b) {
        setButton.setEnabled(b);
        delButton.setEnabled(b);
    }

    public void addActionListener(ActionListener l){
        setButton.addActionListener(l);
        delButton.addActionListener(l);
    }

    private class SetAction extends AbstractAction {

        public SetAction() {
            this.putValue(Action.SMALL_ICON, setIcon);
        }

        @Override
        public void actionPerformed(ActionEvent arg0) {
            final FEditToolBar tb = new FEditToolBar();
            tb.populate(toolBarPane.getFToolBar());
            BasicDialog dialog = tb.showWindow(DesignerContext.getDesignerFrame());
            dialog.addDialogActionListener(new DialogActionAdapter() {
                public void doOk() {
                    toolBarPane.setFToolBar(tb.update());
                }
            });
            dialog.setVisible(true);
        }
    }

    private class DelAction extends AbstractAction {

        public DelAction() {
            this.putValue(Action.SMALL_ICON, delIcon);
        }

        @Override
        public void actionPerformed(ActionEvent arg0) {
            toolBarPane.removeAll();
            toolBarPane.removeButtonList();
            toolBarPane.repaint();
        }
    }
}
