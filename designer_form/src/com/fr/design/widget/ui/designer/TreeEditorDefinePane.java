package com.fr.design.widget.ui.designer;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.frpane.TreeSettingPane;
import com.fr.design.gui.icheckbox.UICheckBox;

import com.fr.design.gui.itree.refreshabletree.TreeRootPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;

import com.fr.form.ui.TreeEditor;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;


/*
 * richer:tree editor
 */
public class TreeEditorDefinePane extends DictEditorDefinePane<TreeEditor> {
    protected TreeSettingPane treeSettingPane;
    protected TreeRootPane treeRootPane;
    private UICheckBox mutiSelect;
    private UICheckBox loadAsync;
    private UICheckBox returnLeaf;
    private UICheckBox returnPath;

    public TreeEditorDefinePane(XCreator xCreator) {
        super(xCreator);
        treeRootPane = new TreeRootPane();
        treeSettingPane = new TreeSettingPane(true);
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

    protected  void populateSubDictionaryEditorBean(TreeEditor e){
        formWidgetValuePane.populate(e);
        treeSettingPane.populate(e);
        treeRootPane.populate(e.getTreeAttr());
        mutiSelect.setSelected(e.isMultipleSelection());
        loadAsync.setSelected(e.isAjax());
        returnLeaf.setSelected(e.isSelectLeafOnly());
        returnPath.setSelected(e.isReturnFullPath());
    }

    protected  TreeEditor updateSubDictionaryEditorBean(){
        TreeEditor editor = (TreeEditor)creator.toData();
        formWidgetValuePane.update(editor);
        editor.setTreeAttr(treeRootPane.update());
        editor.setMultipleSelection(mutiSelect.isSelected());
        editor.setAjax(loadAsync.isSelected());
        editor.setSelectLeafOnly(returnLeaf.isSelected());
        editor.setReturnFullPath(returnPath.isSelected());
        return editor;
    }


    @Override
    public DataCreatorUI dataUI() {
        return treeSettingPane;
    }
}