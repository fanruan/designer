package com.fr.design.actions.help.alphafine;

import com.fr.base.BaseUtils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dialog.DialogActionListener;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.menu.MenuKeySet;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by XiaXiang on 2017/4/1.
 */
public class AlphafineAction extends UpdateAction {
    public AlphafineAction() {
        this.setMenuKeySet(ALPHAFINE);
        this.setName(getMenuKeySet().getMenuName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/mainframe/alphafine/images/smallsearch.png"));
    }

    public static final MenuKeySet ALPHAFINE = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'D';
        }



        @Override
        public String getMenuName() {
            return "AlphaFine";
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    @Override
    public void actionPerformed(ActionEvent e) {
        final DesignerFrame designerFrame = DesignerContext.getDesignerFrame();

        final AlphafineConfigPane alphafineConfigPane = new AlphafineConfigPane();
        alphafineConfigPane.populate(DesignerEnvManager.getEnvManager().getAlphafineConfigManager());
        DialogActionListener dialogActionListener = new DialogActionAdapter() {
            public void doOk() {
                alphafineConfigPane.update();
                designerFrame.refreshToolbar();
            }
        };
        BasicDialog basicDialog = alphafineConfigPane.showMediumWindow(designerFrame, dialogActionListener);
        basicDialog.setVisible(true);
    }
}
