package com.fr.design.mainframe.alphafine.component;

import com.fr.base.BaseUtils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.help.alphafine.AlphaFineContext;
import com.fr.design.actions.help.alphafine.AlphaFineListener;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by XiaXiang on 2017/3/21.
 */
public class AlphaFinePane extends BasicPane {
    private static AlphaFinePane alphaFinePane;

    public static AlphaFinePane createAlphaFinePane() {
        if (alphaFinePane == null) {
            alphaFinePane = new AlphaFinePane();
        }
        return alphaFinePane;
    }
    public AlphaFinePane() {
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 14));
        if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isEnabled()) {
            Toolkit.getDefaultToolkit().addAWTEventListener(AlphaFineDialog.listener(), AWTEvent.KEY_EVENT_MASK);
        }
        UIButton refreshButton = new UIButton();
        refreshButton.setIcon(BaseUtils.readIcon("/com/fr/design/mainframe/alphafine/images/smallsearch.png"));
        refreshButton.setToolTipText(Inter.getLocText("FR-Designer_Alphafine"));
        refreshButton.set4ToolbarButton();
        this.add(refreshButton);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AlphaFineHelper.showAlphaFineDialog(false);
            }
        });
        AlphaFineContext.addAlphafineContextListener(new AlphaFineListener() {
            @Override
            public void showDialog() {
                AlphaFineHelper.showAlphaFineDialog(true);
            }
        });
    }

    @Override
    protected String title4PopupWindow() {
        return "AlphaFine";
    }
}
