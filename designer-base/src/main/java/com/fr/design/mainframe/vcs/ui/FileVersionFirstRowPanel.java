package com.fr.design.mainframe.vcs.ui;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;


public class FileVersionFirstRowPanel extends JPanel {

    public FileVersionFirstRowPanel() {
        super(new BorderLayout());
        Box upPane = Box.createVerticalBox();
        upPane.setBorder(new EmptyBorder(5, 10, 5, 10));
        upPane.add(new UILabel(Toolkit.i18nText("Fine-Design_Vcs_Current")));
        add(upPane, BorderLayout.CENTER);
    }
}
