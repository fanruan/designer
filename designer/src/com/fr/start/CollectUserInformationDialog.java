package com.fr.start;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.UIDialog;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextarea.DescriptionTextArea;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.ActiveKeyGenerator;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.OperatingSystem;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.util.Locale;

public class CollectUserInformationDialog extends UIDialog {

    private static final String CN_LOGIN_HTML = "http://www.finereport.com/products/frlogin";
    private static final String EN_LOGIN_HTML = "http://www.finereport.com/en/frlogin";
    private static final String TW_LOGIN_HTML = "http://www.finereport.com/tw/products/frlogin";
    private static final String JP_LOGIN_HTML = "http://www.finereport.com/jp/products/frlogin";
    private static final int ONLINE_VERIFY_TIMEOUT = 30 * 1000;
    private static final int DIALOG_WIDTH = 480;
    private static final int DIALOG_HEIGHT = 300;
    private static final int DESCRIPTION_ROWS = 5;
    private static final int KEYPANE_PADDING_TOP = 32;
    private static final int KEYPANE_PADDING_LEFT = 2;
    private static final int KEYPANE_PADDING_DOWN = 32;
    private static final int KEYPANE_PADDING_RIGHT = 2;
    private static final int DEFAULTPANE_PADDING_TOP = 2;
    private static final int DEFAULTPANE_PADDING = 4;

    private UITextField keyTextField;
    private DescriptionTextArea descriptionTextArea;


    public CollectUserInformationDialog(Frame parent) {
        super(parent);
        this.initComponents();
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    protected void initComponents() {
        JPanel defaultPane = (JPanel) this.getContentPane();
        defaultPane.setLayout(FRGUIPaneFactory.createM_BorderLayout());
        defaultPane.setBorder(BorderFactory.createEmptyBorder(DEFAULTPANE_PADDING_TOP, DEFAULTPANE_PADDING, DEFAULTPANE_PADDING, DEFAULTPANE_PADDING));
        this.applyClosingAction();
        //this.applyEscapeAction();
        JPanel centPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        defaultPane.add(centPane, BorderLayout.CENTER);

        // marks:topPane
        JPanel topPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        centPane.add(topPane, BorderLayout.NORTH);
        topPane.setBorder(BorderFactory.createTitledBorder(null,
                Inter.getLocText("Collect-Enter_your_user_information_code(It's_free_to_get_from_product's_official_website)"),
                TitledBorder.LEADING, TitledBorder.TOP));
        JPanel keyPane = new JPanel(new BorderLayout(4, 4));
        keyPane.setBorder(BorderFactory.createEmptyBorder(KEYPANE_PADDING_TOP, KEYPANE_PADDING_LEFT, KEYPANE_PADDING_DOWN, KEYPANE_PADDING_RIGHT));
        topPane.add(keyPane);

        UILabel avctivenumberLabel = new UILabel();
        avctivenumberLabel.setText(Inter.getLocText("FR-Designer-Collect_Information_free") + ":");
        keyPane.add(avctivenumberLabel, BorderLayout.WEST);
        keyTextField = new UITextField();
        keyPane.add(keyTextField, BorderLayout.CENTER);
        keyTextField.setMaximumSize(new Dimension(keyTextField.getPreferredSize().width, 25));
        macSystemHit(keyPane);

        UIButton getKeyButton = new UIButton(
                Inter.getLocText("FR-Designer_Get_Activation_Code"));
        getKeyButton.setMnemonic('F');
        keyPane.add(getKeyButton, BorderLayout.EAST);
        getKeyButton.addActionListener(actionListener);

        descriptionTextArea = new DescriptionTextArea();
        descriptionTextArea.setRows(DESCRIPTION_ROWS);
        descriptionTextArea.setBorder(
                BorderFactory.createTitledBorder(Inter.getLocText("FR-Designer-Collect_Information_Description")));
        descriptionTextArea.setText(Inter.getLocText("Collect-User_Information_DES"));
        UIScrollPane scrollPane = new UIScrollPane(descriptionTextArea);
        scrollPane.setBorder(null);
        centPane.add(scrollPane, BorderLayout.CENTER);
        defaultPane.add(this.createControlButtonPane(), BorderLayout.SOUTH);

        this.setTitle(Inter.getLocText("Collect-Collect_User_Information"));
        this.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        this.setModal(true);
        GUICoreUtils.centerWindow(this);
    }

    private void macSystemHit(JPanel keyPane) {
        if (OperatingSystem.isMacOS()) {
            UITextArea macHit = new UITextArea();
            macHit.setText(Inter.getLocText("FR-Designer-Collect_OSXTips"));
            macHit.setEditable(false);
            keyPane.add(macHit, BorderLayout.SOUTH);
        }
    }

    protected void applyClosingAction() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                getKeyAction();
            }
        });
    }

    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(final ActionEvent e) {
            getKeyAction();
        }
    };

    private void getKeyAction(){
        Locale locale = FRContext.getLocale();
        String url = EN_LOGIN_HTML;
        if (ComparatorUtils.equals(locale, Locale.TAIWAN)) {
            url = TW_LOGIN_HTML;
        }
        if (ComparatorUtils.equals(locale, Locale.CHINA)) {
            url = CN_LOGIN_HTML;
        }
        if (ComparatorUtils.equals(locale, Locale.JAPAN)) {
            url = JP_LOGIN_HTML;
        }
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception ioe) {
            // do nothing
        }
    };

    public String getKey() {
        return this.keyTextField.getText().trim();
    }

    protected JPanel createControlButtonPane() {
        JPanel controlPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        JPanel buttonsPane = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
        controlPane.add(buttonsPane, BorderLayout.EAST);

        UIButton finishButton = new UIButton(Inter.getLocText("Collect-Use_Designer"));
        finishButton.setMnemonic('F');
        buttonsPane.add(finishButton);
        finishButton.addActionListener(verifyActionListener);
        UIButton exitButton = new UIButton(Inter.getLocText("Utils-Exit_Designer"));
        exitButton.setMnemonic('E');
        buttonsPane.add(exitButton);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.exit(0);
            }
        });
        // set default pane.
        this.getRootPane().setDefaultButton(finishButton);

        return controlPane;
    }

    private ActionListener verifyActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            String keyValue = CollectUserInformationDialog.this.getKey();
            String message;
            if (ActiveKeyGenerator.verify(keyValue, ONLINE_VERIFY_TIMEOUT)) {
                message = Inter.getLocText("FR-Designer-Collect_Information_Successfully");
                JOptionPane.showMessageDialog(CollectUserInformationDialog.this, message);
                DesignerEnvManager.getEnvManager().setActivationKey(keyValue);
                doOK();
            } else {
                message = Inter.getLocText("Collect-The_user_information_code_is_invalid");
                JOptionPane.showMessageDialog(CollectUserInformationDialog.this, message);
            }
        }
    };

    /**
     * 检测是否合法输入
     *
     * @throws Exception 抛出异常
     *
     */
    public void checkValid() throws Exception {
        // do nothing
    }
}