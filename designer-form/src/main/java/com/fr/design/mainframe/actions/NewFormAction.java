package com.fr.design.mainframe.actions;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.BaseJForm;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.MenuKeySet;

import com.fr.stable.bridge.StableFactory;

import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

public class NewFormAction extends UpdateAction {

    public NewFormAction() {
        this.setMenuKeySet(NEW_FORM);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/buttonicon/new_form3.png"));
        this.setAccelerator(getMenuKeySet().getKeyStroke());
    }

    /**
     * 动作
     *
     * @param e 事件
     */
    public void actionPerformed(ActionEvent e) {
        BaseJForm jform = StableFactory.getMarkedInstanceObjectFromClass(BaseJForm.XML_TAG, BaseJForm.class);
        DesignerContext.getDesignerFrame().addAndActivateJTemplate((JTemplate<?, ?>) jform);
    }


    public static final MenuKeySet NEW_FORM = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'F';
        }

        @Override
        public String getMenuName() {
            return com.fr.design.i18n.Toolkit.i18nText("M-New_FormBook");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return KeyStroke.getKeyStroke(KeyEvent.VK_F, DEFAULT_MODIFIER);
        }
    };
}