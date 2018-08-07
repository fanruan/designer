package com.fr.design.actions.help;

import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.stable.ProductConstants;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Dialog displaying information about the application.
 */
public class AboutDialog extends JDialog implements ActionListener {
    /**
     * Construct a new AboutDialog.
     */
    public AboutDialog() {
    }

    public AboutDialog(Frame parent, JPanel aboutPanel) {
        super(parent, true);

        this.setTitle(ProductConstants.PRODUCT_NAME);
        this.setResizable(false);
        JPanel defaultPane=FRGUIPaneFactory.createBorderLayout_L_Pane();
        this.setContentPane(defaultPane);

        okButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("OK"));
        okButton.addActionListener(this);

        tabbedPane = new UITabbedPane();
        sysPane = new SystemInfoPane();

        tabbedPane.addTab(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_About"), aboutPanel);
        tabbedPane.addTab(com.fr.design.i18n.Toolkit.i18nText("System"), sysPane);

        buttonPanel = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
        buttonPanel.add(okButton);

        defaultPane.add(tabbedPane, BorderLayout.CENTER);
        defaultPane.add(buttonPanel, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dialogExit();
            }
        });

        //esp.
        InputMap inputMapAncestor = defaultPane.getInputMap(
        		JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap actionMap = defaultPane.getActionMap();

        //transfer focus to CurrentEditor
        inputMapAncestor.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "dialogExit");
        actionMap.put("dialogExit", new AbstractAction() {
            public void actionPerformed(ActionEvent evt) {
                dialogExit();
            }
        });

        this.getRootPane().setDefaultButton(okButton);

        this.setSize(new Dimension(660, 600));
        GUICoreUtils.centerWindow(this);
    }

    public void dialogExit() {
        this.dispose();
    }

    /**
     * Called when the ok button is clicked.
     */
    public void actionPerformed(ActionEvent e) {
        this.dialogExit();
    }

    private SystemInfoPane sysPane;
    private JPanel buttonPanel;
    private UITabbedPane tabbedPane;
    private UIButton okButton;
}
