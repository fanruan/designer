package com.fr.quickeditor.cellquick;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.report.SelectImagePane;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.quickeditor.CellQuickEditor;
import com.fr.report.cell.cellattr.CellImage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 单元格元素图片编辑器
 * TODO 9.0大体上没有改动
 */
public class CellImageQuickEditor extends CellQuickEditor {
    private static CellImageQuickEditor THIS;

    public static final CellImageQuickEditor getInstance() {
        if (THIS == null) {
            THIS = new CellImageQuickEditor();
        }
        return THIS;
    }

    private CellImageQuickEditor() {
        super();
    }

    @Override
    public JComponent createCenterBody() {
        UIButton editbutton = new UIButton(Inter.getLocText("Edit"), BaseUtils.readIcon("/com/fr/design/images/m_insert/image.png"));
        editbutton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                showEditingDialog();
            }
        });
        editbutton.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        editbutton.setMargin(null);
        editbutton.setOpaque(false);
        return editbutton;
    }

    private void showEditingDialog() {
        final SelectImagePane imageEditorPane = new SelectImagePane();
        imageEditorPane.populate(cellElement);
        final Object oldValue = cellElement.getValue();
        final Style oldStyle = cellElement.getStyle();
        imageEditorPane.showWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

            @Override
            public void doOk() {
                CellImage cellImage = imageEditorPane.update();
                if (!ComparatorUtils.equals(cellImage.getImage(), oldValue) || !ComparatorUtils.equals(cellImage.getStyle(), oldStyle)) {
                    cellElement.setValue(cellImage.getImage());
                    cellElement.setStyle(cellImage.getStyle());
                    fireTargetModified();
                }
            }

        }).setVisible(true);
    }

    @Override
    protected void refreshDetails() {

    }

}