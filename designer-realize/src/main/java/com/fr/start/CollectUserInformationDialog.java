package com.fr.start;

import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.UIDialog;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextarea.DescriptionTextArea;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.locale.impl.UserInfoMark;
import com.fr.design.mainframe.ActiveKeyGenerator;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.CloudCenter;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralContext;
import com.fr.general.locale.LocaleCenter;
import com.fr.general.locale.LocaleMark;
import com.fr.process.engine.core.FineProcessContext;
import com.fr.process.engine.core.FineProcessEngineEvent;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.util.Locale;

/**
 * @author null
 */
public class CollectUserInformationDialog extends UIDialog {

    private static final int ONLINE_VERIFY_TIMEOUT = 30 * 1000;

    private UITextField keyTextField;

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
            getKeyAction();
        }
    };

    private ActionListener verifyActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent evt) {
            String keyValue = CollectUserInformationDialog.this.getKey();
            String message;
            if (ActiveKeyGenerator.verify(keyValue, ONLINE_VERIFY_TIMEOUT)) {
                message = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Activate_Activated_Successfully");
                JOptionPane.showMessageDialog(CollectUserInformationDialog.this, message);
                DesignerEnvManager.getEnvManager().setActivationKey(keyValue);
                DesignerEnvManager.getEnvManager().saveXMLFile();
                doOK();
            } else {
                message = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Activate_Activation_Code_Invalid");
                JOptionPane.showMessageDialog(CollectUserInformationDialog.this, message);
            }
        }
    };


    public CollectUserInformationDialog(Frame parent) {
        super(parent);
        this.initComponents();
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    protected void initComponents() {
        JPanel defaultPane = (JPanel) this.getContentPane();
        defaultPane.setLayout(FRGUIPaneFactory.createM_BorderLayout());
        defaultPane.setBorder(BorderFactory.createEmptyBorder(2, 4, 4, 4));
        this.applyClosingAction();
        JPanel centPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        defaultPane.add(centPane, BorderLayout.CENTER);

        // marks:topPane
        JPanel topPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        centPane.add(topPane, BorderLayout.NORTH);
        topPane.setBorder(BorderFactory.createTitledBorder(null,
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Activate_Enter_Your_FR_Activation_Code"),
                TitledBorder.LEADING, TitledBorder.TOP));
        JPanel keyPane = new JPanel(new BorderLayout(4, 4));
        keyPane.setBorder(BorderFactory.createEmptyBorder(32, 2, 32, 2));
        topPane.add(keyPane);

        UILabel activateCodeLabel = new UILabel();
        activateCodeLabel.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Activate_FR_Activation_Code") + ":");
        keyPane.add(activateCodeLabel, BorderLayout.WEST);
        keyTextField = new UITextField();
        keyPane.add(keyTextField, BorderLayout.CENTER);
        keyTextField.setMaximumSize(new Dimension(keyTextField.getPreferredSize().width, 25));

        UIButton getKeyButton = new UIButton(
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Activate_Get_FR_Activation_Code"));
        getKeyButton.setMnemonic('F');
        keyPane.add(getKeyButton, BorderLayout.EAST);
        getKeyButton.addActionListener(actionListener);

        DescriptionTextArea descriptionTextArea = new DescriptionTextArea();
        descriptionTextArea.setRows(5);
        descriptionTextArea.setBorder(
                BorderFactory.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Collect_Information_Description")));
        descriptionTextArea.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Activate_FR_Activation_Code_Description"));
        UIScrollPane scrollPane = new UIScrollPane(descriptionTextArea);
        scrollPane.setBorder(null);
        centPane.add(scrollPane, BorderLayout.CENTER);
        defaultPane.add(this.createControlButtonPane(), BorderLayout.SOUTH);

        this.setTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Activate_Register_Product_For_Free"));
        this.setSize(480, 300);
        this.setModal(true);
        GUICoreUtils.centerWindow(this);
    }

    @Override
    protected void applyClosingAction() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                getKeyAction();
            }
        });
    }


    private void getKeyAction() {
        LocaleMark<String> localeMark = LocaleCenter.getMark(UserInfoMark.class);
        try {
            Desktop.getDesktop().browse(new URI(localeMark.getValue()));
        } catch (Exception ignored) {

        }
    }

    public String getKey() {
        return this.keyTextField.getText().trim();
    }

    protected JPanel createControlButtonPane() {
        JPanel controlPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        JPanel buttonsPane = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
        controlPane.add(buttonsPane, BorderLayout.EAST);

        UIButton finishButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Collect_Use_Designer"));
        finishButton.setMnemonic('F');
        buttonsPane.add(finishButton);
        finishButton.addActionListener(verifyActionListener);
        UIButton exitButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Exit_Designer"));
        exitButton.setMnemonic('E');
        buttonsPane.add(exitButton);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                FineProcessContext.getChildPipe().fire(FineProcessEngineEvent.DESTROY);
            }
        });
        // set default pane.
        this.getRootPane().setDefaultButton(finishButton);

        return controlPane;
    }


    /**
     * 检测是否合法输入
     */
    @Override
    public void checkValid() {
    }
}
