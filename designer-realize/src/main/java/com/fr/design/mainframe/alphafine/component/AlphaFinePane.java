package com.fr.design.mainframe.alphafine.component;

import com.fr.design.actions.help.alphafine.AlphaFineContext;
import com.fr.design.actions.help.alphafine.AlphaFineListener;
import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.general.IOUtils;


import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by XiaXiang on 2017/3/21.
 */
public class AlphaFinePane extends BasicPane {
    private static AlphaFinePane alphaFinePane;

    static {
        Toolkit.getDefaultToolkit().addAWTEventListener(AlphaFineDialog.listener(), AWTEvent.KEY_EVENT_MASK);
    }

    public AlphaFinePane() {
        setPreferredSize(new Dimension(24, 24));
        setLayout(new BorderLayout());
        UIButton refreshButton = new UIButton();
        refreshButton.setIcon(IOUtils.readIcon(("/com/fr/design/mainframe/alphafine/images/smallsearch.png")));
        refreshButton.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_AlphaFine"));
        refreshButton.set4ToolbarButton();
        refreshButton.setRolloverEnabled(false);
        this.add(refreshButton);
        this.setBackground(UIConstants.TEMPLATE_TAB_PANE_BACKGROUND);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AlphaFineHelper.showAlphaFineDialog(false);
            }
        });
        AlphaFineContext.addAlphaFineListener(new AlphaFineListener() {
            @Override
            public void showDialog() {
                AlphaFineHelper.showAlphaFineDialog(true);
            }

            @Override
            public void setEnable(boolean isEnable) {
                alphaFinePane.setVisible(isEnable);

            }
        });
    }

    public static AlphaFinePane getAlphaFinePane() {
        if (alphaFinePane == null) {
            alphaFinePane = new AlphaFinePane();
        }
        return alphaFinePane;
    }

    @Override
    protected String title4PopupWindow() {
        return "AlphaFine";
    }
}
