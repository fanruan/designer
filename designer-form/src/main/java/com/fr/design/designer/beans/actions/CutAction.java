package com.fr.design.designer.beans.actions;

import com.fr.base.BaseUtils;
import com.fr.design.base.mode.DesignModeContext;
import com.fr.design.designer.beans.actions.behavior.ComponentEnable;
import com.fr.design.mainframe.FormDesigner;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

public class CutAction extends FormWidgetEditAction {

    public CutAction(FormDesigner t) {
        super(t);
        this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_M_Edit_Cut"));
        this.setMnemonic('T');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/cut.png"));
        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, DEFAULT_MODIFIER));
        this.setUpdateBehavior(new ComponentEnable());
        this.setEnabled(!DesignModeContext.isBanCopyAndCut());
    }

    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        FormDesigner editPane = getEditingComponent();
        if (editPane == null) {
            return false;
        }
        return editPane.cut();
    }
}