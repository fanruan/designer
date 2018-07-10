package com.fr.design.mainframe.widget.accessibles;

import javax.swing.SwingUtilities;

import com.fr.design.mainframe.widget.wrappers.TreeModelWrapper;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.frpane.TreeSettingPane;


/**
 * 用于TreeEdito和TreeComboBox的数据格式设置
 * @since 6.5.3
 */
public class AccessibleTreeModelEditor extends UneditableAccessibleEditor {

    private TreeSettingPane treeSettingPane;

    public AccessibleTreeModelEditor() {
        super(new TreeModelWrapper());
    }

    @Override
    protected void showEditorPane() {
        if (treeSettingPane == null) {
            treeSettingPane = new TreeSettingPane(false);
        }
        BasicDialog dlg = treeSettingPane.showWindow(SwingUtilities.getWindowAncestor(this));
        treeSettingPane.populate(getValue());
        dlg.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                Object nodeOrDict = treeSettingPane.updateTreeNodeAttrs();
                setValue(nodeOrDict);
                fireStateChanged();
            }
        });
        dlg.setVisible(true);
    }
}