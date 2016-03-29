package com.fr.design.condition;

import com.fr.base.CellBorderStyle;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.style.BorderPane;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.highlight.BorderHighlightAction;
import com.fr.report.cell.cellattr.highlight.HighlightAction;
import com.fr.stable.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
* @author richie
* @date 2015-03-26
* @since 8.0
*/
public class BorderHighlightPane extends ConditionAttrSingleConditionPane<HighlightAction> {
    private CellBorderStyle border;
    private UIButton borderButton;
    public BorderHighlightPane(final ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
        borderButton = new UIButton(Inter.getLocText("FR-Designer_Edit"));
        borderButton.setPreferredSize(new Dimension(53, 23));
        borderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final BorderPane borderPane = new BorderPane();
                int line = border == null ? Constants.LINE_NONE : border.getTopStyle();
                Color color = border == null ? Color.black : border.getTopColor();
                borderPane.populate(border, false, line, color);
                BasicDialog dialog = borderPane.showWindow(SwingUtilities.getWindowAncestor(conditionAttributesPane));
                dialog.addDialogActionListener(new DialogActionAdapter() {
                    @Override
                    public void doOk() {
                        border = borderPane.update();
                        borderButton.setBorderStyle(border);
                        borderButton.repaint();
                    }
                });
                dialog.setVisible(true);
            }
        });

        UILabel borderLabel = new UILabel(Inter.getLocText("FR-Designer_Border") + ":");
        this.add(borderLabel);
        this.add(borderButton);
    }

    @Override
    public String nameForPopupMenuItem() {
        return Inter.getLocText("FR-Designer_Border");
    }


    public void populate(HighlightAction ha) {
        this.border = ((BorderHighlightAction)ha).getCellBorder();
        borderButton.setBorderStyle(border);
        borderButton.repaint();
    }

    public HighlightAction update() {
        return new BorderHighlightAction(border);
    }


}