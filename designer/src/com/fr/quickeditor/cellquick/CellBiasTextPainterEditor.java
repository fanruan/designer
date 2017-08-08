package com.fr.quickeditor.cellquick;

import com.fr.design.actions.core.ActionFactory;
import com.fr.design.actions.insert.cell.BiasCellAction;
import com.fr.design.cell.editor.BiasTextPainterCellEditor.BiasTextPainterPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.ComparatorUtils;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.quickeditor.CellQuickEditor;
import com.fr.report.cell.painter.BiasTextPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 单元格元素斜线编辑器
 */
public class CellBiasTextPainterEditor extends CellQuickEditor {

    @Override
    public JComponent createCenterBody() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 15));
        UIButton editButton = new UIButton(Inter.getLocText("Edit"), IOUtils.readIcon("/com/fr/design/images/m_insert/bias.png"));
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEditingDialog();
            }
        });
        editButton.setOpaque(false);
        content.add(editButton, BorderLayout.CENTER);
        return content;
    }

    @Override
    public Object getComboBoxSelected() {
        return ActionFactory.createAction(BiasCellAction.class);
    }

    private void showEditingDialog() {
        final BiasTextPainter oldbiasTextPainter = (BiasTextPainter) cellElement.getValue();
        final BiasTextPainterPane biasTextPainterPane = new BiasTextPainterPane();
        biasTextPainterPane.populate(oldbiasTextPainter);
        biasTextPainterPane.showSmallWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

            @Override
            public void doOk() {
                BiasTextPainter newbiasTextPainter = biasTextPainterPane.update();
                if (!ComparatorUtils.equals(oldbiasTextPainter, newbiasTextPainter)) {
                    cellElement.setValue(newbiasTextPainter);
                    fireTargetModified();
                }
            }

        }).setVisible(true);
    }

    @Override
    protected void refreshDetails() {
    }

}