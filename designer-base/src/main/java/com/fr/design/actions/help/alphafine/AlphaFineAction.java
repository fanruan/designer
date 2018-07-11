package com.fr.design.actions.help.alphafine;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dialog.DialogActionListener;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.IOUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by XiaXiang on 2017/4/1.
 */
public class AlphaFineAction extends UpdateAction {
    public AlphaFineAction() {
        this.setMenuKeySet(ALPHAFINE);
        this.setName(getMenuKeySet().getMenuName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(IOUtils.readIcon("/com/fr/design/mainframe/alphafine/images/smallsearch.png"));
        this.generateAndSetSearchText(AlphaFineConfigPane.class.getName());
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

        final AlphaFineConfigPane alphaFineConfigPane = new AlphaFineConfigPane();
        final AlphaFineConfigManager manager = DesignerEnvManager.getEnvManager().getAlphaFineConfigManager();
        if (!FRContext.isChineseEnv()) {
            manager.setSearchOnLine(false);
        }
        alphaFineConfigPane.populate(manager);
        DialogActionListener dialogActionListener = new DialogActionAdapter() {
            public void doOk() {
                alphaFineConfigPane.update();
                AlphaFineContext.fireAlphaFineEnable(manager.isEnabled());
            }
        };
        BasicDialog basicDialog = alphaFineConfigPane.showMediumWindow(designerFrame, dialogActionListener);
        basicDialog.setVisible(true);
    }
}
