package com.fr.env;

import com.fr.core.env.resource.RemoteEnvConfig;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.border.UITitledBorder;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ipasswordfield.UIPassWordField;
import com.fr.design.gui.itextfield.UIIntNumberField;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.log.FineLoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

/**
 * 远程环境设置界面，暂时命名为2，待做完功能直接替代掉老的RemoteEnvPane
 */
public class RemoteEnvPane2 extends BasicBeanPane<RemoteEnvConfig> {

    private UITextField hostTextField;
    private UIIntNumberField portTextField;
    private UITextField usernameTextField;
    private UIPassWordField passwordTextField;

    public RemoteEnvPane2() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        contentPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        new EmptyBorder(6, 0, 0, 0),
                        UITitledBorder.createBorderWithTitle(Inter.getLocText("Fine-Designer_Basic_Remote_Env_Config")))
        );

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] rowSize = new double[]{p, p, p, p, p};
        double[] columnSize = new double[]{p, f};
        UIButton testConnectionButton = new UIButton(Inter.getLocText("Fine-Designer_Basic_Remote_Env_Try"));
        hostTextField = new UITextField();
        hostTextField.setPlaceholder("192.168.100.200");
        portTextField = new UIIntNumberField();
        portTextField.setPlaceholder("39999");
        JPanel valuePane = TableLayoutHelper.createTableLayoutPane(
                new Component[][]{
                        {new UILabel(Inter.getLocText("Fine-Designer_Basic_Remote_Env_Host") + ":", SwingConstants.RIGHT), hostTextField},
                        {new UILabel(Inter.getLocText("Fine-Designer_Basic_Remote_Env_Port")  + ":", SwingConstants.RIGHT),portTextField},
                        {new UILabel(Inter.getLocText("Fine-Designer_Basic_Remote_Env_User") + ":", SwingConstants.RIGHT), usernameTextField = new UITextField()},
                        {new UILabel(Inter.getLocText("Fine-Designer_Basic_Remote_Env_Password") + ":", SwingConstants.RIGHT), passwordTextField = new UIPassWordField()},
                        {null, GUICoreUtils.createFlowPane(testConnectionButton, FlowLayout.LEFT)}
                },
                rowSize, columnSize
        );
        testConnectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tryConnectRemoteEnv();
            }
        });
        contentPanel.add(valuePane, BorderLayout.CENTER);
    }

    private void tryConnectRemoteEnv() {
        final RemoteEnv remoteEnv = new RemoteEnv(this.updateBean());
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                remoteEnv.connectOnce();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    showConnectMessage();
                } catch (Exception e) {
                    showCannotConnectMessage();
                }
            }
        }.execute();
    }

    private void showCannotConnectMessage() {
        JOptionPane.showMessageDialog(
                this,
                Inter.getLocText("Fine-Designer_Basic_Remote_Connect_Failed"),
                UIManager.getString("OptionPane.messageDialogTitle", this.getLocale()),
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showConnectMessage() {
        JOptionPane.showMessageDialog(
                this,
                Inter.getLocText("Fine-Designer_Basic_Remote_Connect_Successful"),
                UIManager.getString("OptionPane.messageDialogTitle", this.getLocale()),
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    protected String title4PopupWindow() {
        return "RemoteEnv";
    }

    @Override
    public void populateBean(RemoteEnvConfig config) {
        if (config == null) {
            return;
        }
        hostTextField.setText(config.getPath());
        if (config.getPort() != 0) {
            portTextField.setValue(config.getPort());
        }
        usernameTextField.setText(config.getUsername());
        passwordTextField.setText(config.getPassword());
    }

    @Override
    public RemoteEnvConfig updateBean() {
        RemoteEnvConfig config = new RemoteEnvConfig();
        config.setHost(hostTextField.getText());
        config.setPort((int) portTextField.getValue());
        config.setUsername(usernameTextField.getText());
        config.setPassword(passwordTextField.getText());
        return config;
    }
}
