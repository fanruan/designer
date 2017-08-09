package com.fr.design.widget.ui.designer.layout;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.accessibles.AccessibleElementCaseToolBarEditor;
import com.fr.design.widget.ui.designer.component.PaddingBoundPane;
import com.fr.form.ui.ElementCaseEditor;
import com.fr.form.web.FormToolBarManager;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ibm on 2017/8/7.
 */
public class ElementEditorDefinePane extends WTitleLayoutDefinePane<ElementCaseEditor> {
    private PaddingBoundPane paddingBoundPane;
    private AccessibleElementCaseToolBarEditor elementCaseToolBarEditor;

    public ElementEditorDefinePane(XCreator xCreator){
        super(xCreator);

    }

    protected JPanel createCenterPane(){
        paddingBoundPane = new PaddingBoundPane();
        elementCaseToolBarEditor = new AccessibleElementCaseToolBarEditor();
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{paddingBoundPane, null},
                new Component[]{new UILabel("报表块工具栏"), elementCaseToolBarEditor}
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 20, 10);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        return panel;
    }

    protected  ElementCaseEditor updateSubBean(){
        ElementCaseEditor elementCaseEditor = (ElementCaseEditor)creator.toData();
        paddingBoundPane.update(elementCaseEditor);
        elementCaseEditor.setToolBars((FormToolBarManager[])elementCaseToolBarEditor.getValue());
        return elementCaseEditor;
    }

    protected  void populateSubBean(ElementCaseEditor ob){
        paddingBoundPane.populate(ob);
        elementCaseToolBarEditor.setValue(ob.getToolBars());
    }

}
