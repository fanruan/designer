package com.fr.design.widget.ui.designer.layout;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.*;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.accessibles.AccessibleElementCaseToolBarEditor;
import com.fr.design.widget.ui.designer.component.PaddingBoundPane;
import com.fr.form.ui.ElementCaseEditor;
import com.fr.form.web.FormToolBarManager;
import com.fr.general.ComparatorUtils;


import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by ibm on 2017/8/7.
 */
public class ElementEditorDefinePane extends WTitleLayoutDefinePane<ElementCaseEditor> {
    private PaddingBoundPane paddingBoundPane;
    private AccessibleElementCaseToolBarEditor elementCaseToolBarEditor;
    private PropertyGroupPane extraPropertyGroupPane;

    public ElementEditorDefinePane(XCreator xCreator) {
        super(xCreator);

    }

    protected JPanel createCenterPane() {
        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        paddingBoundPane = new PaddingBoundPane();
        elementCaseToolBarEditor = new AccessibleElementCaseToolBarEditor();
        Component[][] components = new Component[][]{
                new Component[]{paddingBoundPane, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_ EC_Toolbar")), elementCaseToolBarEditor},
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_W0, IntervalConstants.INTERVAL_L1);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        CRPropertyDescriptor[] extraTableEditor = ((XElementCase) creator).getExtraTableEditor();
        extraPropertyGroupPane = new PropertyGroupPane(extraTableEditor, creator);
        centerPane.add(panel, BorderLayout.NORTH);
        centerPane.add(extraPropertyGroupPane, BorderLayout.CENTER);
        return centerPane;
    }

    protected ElementCaseEditor updateSubBean() {
        ElementCaseEditor elementCaseEditor = (ElementCaseEditor) creator.toData();
        if (ComparatorUtils.equals(getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Layout_Padding_Duplicate"))) {
            paddingBoundPane.update(elementCaseEditor);
        }
        elementCaseEditor.setToolBars((FormToolBarManager[]) elementCaseToolBarEditor.getValue());

        return elementCaseEditor;
    }

    protected void populateSubBean(ElementCaseEditor ob) {
        paddingBoundPane.populate(ob);
        elementCaseToolBarEditor.setValue(ob.getToolBars());
        extraPropertyGroupPane.populate(ob);
    }

}
