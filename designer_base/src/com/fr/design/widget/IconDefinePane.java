package com.fr.design.widget;

import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.web.CustomIconPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.form.ui.WidgetInfoConfig;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class IconDefinePane extends BasicPane {
    private UILabel showIconImageLable;
    private UIButton editIconButton;
    private UIButton removeIconButton;
    private String curIconName;

    public IconDefinePane() {
        JPanel iconPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel labelPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        labelPane.setLayout(new /**/ FlowLayout(FlowLayout.LEFT, 20, 0));
        showIconImageLable = new UILabel();
        showIconImageLable.setPreferredSize(new Dimension(20, 20));
        editIconButton = new UIButton(Inter.getLocText("FR-Designer_Edit"));
        JPanel iconButtonPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        labelPane.add(showIconImageLable);
        iconPane.add(labelPane, BorderLayout.WEST);
        iconPane.add(iconButtonPane, BorderLayout.EAST);
        iconButtonPane.add(editIconButton);

        editIconButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final CustomIconPane cip = new CustomIconPane();
                BasicDialog editDialog = cip.showWindow(DesignerContext.getDesignerFrame());
                cip.populate(curIconName);
                editDialog.addDialogActionListener(new DialogActionAdapter() {
                    @Override
                    public void doOk() {
                        curIconName = cip.update();
                        setShowIconImage();
                        IconDefinePane.this.repaint();
                    }
                });
                editDialog.setVisible(true);
            }
        });

        removeIconButton = new UIButton(Inter.getLocText("FR-Designer_Delete"));
        iconButtonPane.add(removeIconButton);
        removeIconButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                curIconName = null;
                showIconImageLable.setIcon(null);
            }
        });

        this.add(this.showIconImageLable);
        this.add(this.editIconButton);
        this.add(this.removeIconButton);
    }

    @Override
    protected String title4PopupWindow() {
        return "icon";
    }

    private void setShowIconImage() {
        Image iimage = WidgetInfoConfig.getInstance().getIconManager().getIconImage(curIconName);
        if (iimage != null) {
            showIconImageLable.setIcon(new ImageIcon(iimage));
        } else {
            showIconImageLable.setIcon(null);
        }
    }

    public void populate(String iconName) {
        this.curIconName = iconName;
        setShowIconImage();
    }

    public String update() {
        return this.curIconName;
    }
}