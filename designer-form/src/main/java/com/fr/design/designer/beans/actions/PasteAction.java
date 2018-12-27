package com.fr.design.designer.beans.actions;

import com.fr.base.BaseUtils;
import com.fr.design.base.mode.DesignModeContext;
import com.fr.design.mainframe.FormDesigner;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

public class PasteAction extends FormWidgetEditAction {

    public PasteAction(FormDesigner t) {
        super(t);
        this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_M_Edit_Paste"));
        this.setMnemonic('P');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/paste.png"));
        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, DEFAULT_MODIFIER));
    }

    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        return DesignModeContext.doPaste(getEditingComponent());
    }

    @Override
    public void update() {
        this.setEnabled(true);
    }

}