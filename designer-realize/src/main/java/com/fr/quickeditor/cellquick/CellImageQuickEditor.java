package com.fr.quickeditor.cellquick;

import com.fr.base.Style;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.actions.insert.cell.ImageCellAction;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.report.SelectImagePane;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.quickeditor.CellQuickEditor;
import com.fr.report.cell.cellattr.CellImage;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 单元格元素图片编辑器
 *
 * @author yaoh.wu
 * @version 2017年8月7日10点53分
 */
public class CellImageQuickEditor extends CellQuickEditor {

    public CellImageQuickEditor() {
        super();
    }

    @Override
    public JComponent createCenterBody() {
        JPanel content = new JPanel(new BorderLayout());
        UIButton editButton = new UIButton(Inter.getLocText("Edit"));
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
                    JTemplate<?, ?> currentEditingTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                    currentEditingTemplate.setPictureElem(cellElement, cellImage);
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

    @Override
    public Object getComboBoxSelected() {
        return ActionFactory.createAction(ImageCellAction.class);
    }
}