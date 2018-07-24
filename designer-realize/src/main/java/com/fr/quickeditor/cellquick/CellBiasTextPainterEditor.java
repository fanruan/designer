package com.fr.quickeditor.cellquick;

import com.fr.design.actions.core.ActionFactory;
import com.fr.design.actions.insert.cell.BiasCellAction;
import com.fr.design.cell.editor.BiasTextPainterCellEditor.BiasTextPainterPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.ComparatorUtils;

import com.fr.quickeditor.CellQuickEditor;
import com.fr.report.cell.painter.BiasTextPainter;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 单元格元素斜线编辑器
 */
public class CellBiasTextPainterEditor extends CellQuickEditor {
    public CellBiasTextPainterEditor() {
        super();
    }

    @Override
    public JComponent createCenterBody() {
        JPanel content = new JPanel(new BorderLayout());
        UIButton editButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Edit"));
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEditingDialog();
            }
        });
        editButton.setOpaque(false);
        content.add(TableLayoutHelper.createGapTableLayoutPane(new Component[][]{
                        new Component[]{EMPTY_LABEL, editButton}},
                new double[]{TableLayout.PREFERRED},
                new double[]{TableLayout.PREFERRED, TableLayout.FILL}, HGAP, VGAP), BorderLayout.CENTER);
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

    @Override
    public boolean isScrollAll() {
        return true;
    }

}