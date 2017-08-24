package com.fr.design.widget.ui.designer;

import com.fr.data.Dictionary;
import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.icheckbox.UICheckBox;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itree.refreshabletree.TreeRootPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;

import com.fr.design.mainframe.widget.accessibles.AccessibleTreeModelEditor;
import com.fr.form.ui.TreeEditor;
import com.fr.general.Inter;

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
        mutiSelect = new UICheckBox(Inter.getLocText("Tree-Mutiple_Selection_Or_Not"));
        loadAsync = new UICheckBox(Inter.getLocText("Widget-Load_By_Async"));
        returnLeaf = new UICheckBox(Inter.getLocText("FR-Designer_Widget_Return_Leaf"));
        returnPath = new UICheckBox(Inter.getLocText("FR-Designer_Widget_Return_Path"));
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
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 10, 7);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        return panel;
    }

    @Override
    public String title4PopupWindow() {
        return "tree";
    }


    protected Component[] createDictPane(){
        accessibleTreeModelEditor = new AccessibleTreeModelEditor();
        return new Component[]{new UILabel(Inter.getLocText("FR-Designer_DS-Dictionary")), accessibleTreeModelEditor};
    }

    @Override
    protected void populateSubCustomWritableRepeatEditorBean(TreeEditor e) {
        accessibleTreeModelEditor.setValue(e.getDictionary());
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
        editor.setDictionary((Dictionary) accessibleTreeModelEditor.getValue());
        return editor;
    }

    @Override
    public DataCreatorUI dataUI() {
        return null;
    }
}