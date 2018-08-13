package com.fr.design.widget.ui.designer;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.icheckbox.UICheckBox;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itree.refreshabletree.TreeRootPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;

import com.fr.design.mainframe.widget.accessibles.AccessibleTreeModelEditor;
import com.fr.form.ui.TreeEditor;


import javax.swing.*;
import java.awt.*;


/*
 * richer:tree editor
 */
public class TreeEditorDefinePane extends CustomWritableRepeatEditorPane<TreeEditor> {
    protected TreeRootPane treeRootPane;
    private UICheckBox mutiSelect;
    private UICheckBox loadAsync;
    private UICheckBox returnLeaf;
    private UICheckBox returnPath;
    private AccessibleTreeModelEditor accessibleTreeModelEditor;

    public TreeEditorDefinePane(XCreator xCreator) {
        super(xCreator);
        treeRootPane = new TreeRootPane();
    }



    public JPanel createOtherPane() {
        mutiSelect = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Tree_Mutiple_Selection_Or_Not"));
        mutiSelect.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        loadAsync = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Widget_Load_By_Async"));
        loadAsync.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        returnLeaf = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Return_Leaf"));
        returnLeaf.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        returnPath = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Return_Path"));
        returnPath.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{mutiSelect},
                new Component[]{loadAsync},
                new Component[]{returnLeaf},
                new Component[]{returnPath}
        };
        double[] rowSize = {p, p, p, p};
        double[] columnSize = {p};
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, IntervalConstants.INTERVAL_L2, IntervalConstants.INTERVAL_L1);
        return panel;
    }

    @Override
    public String title4PopupWindow() {
        return "tree";
    }


    protected Component[] createDictPane(){
        accessibleTreeModelEditor = new AccessibleTreeModelEditor();
        return new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_DS_Dictionary")), accessibleTreeModelEditor};
    }

    @Override
    protected void populateSubCustomWritableRepeatEditorBean(TreeEditor e) {
        accessibleTreeModelEditor.setValue(e.getNodeOrDict());
        formWidgetValuePane.populate(e);
        treeRootPane.populate(e.getTreeAttr());
        mutiSelect.setSelected(e.isMultipleSelection());
        loadAsync.setSelected(e.isAjax());
        returnLeaf.setSelected(e.isSelectLeafOnly());
        returnPath.setSelected(e.isReturnFullPath());
    }


    @Override
    protected TreeEditor updateSubCustomWritableRepeatEditorBean() {
        TreeEditor editor = (TreeEditor)creator.toData();
        formWidgetValuePane.update(editor);
        editor.setTreeAttr(treeRootPane.update());
        editor.setMultipleSelection(mutiSelect.isSelected());
        editor.setAjax(loadAsync.isSelected());
        editor.setSelectLeafOnly(returnLeaf.isSelected());
        editor.setReturnFullPath(returnPath.isSelected());
        editor.setNodeOrDict(accessibleTreeModelEditor.getValue());
        return editor;
    }

    @Override
    public DataCreatorUI dataUI() {
        return null;
    }
}
