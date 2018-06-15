package com.fr.env;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.border.UITitledBorder;
import com.fr.design.env.DesignerWorkspaceInfo;
import com.fr.design.env.DesignerWorkspaceType;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ipasswordfield.UIPassWordField;
import com.fr.design.gui.itextfield.UIIntNumberField;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.workspace.WorkContext;
import com.fr.workspace.connect.WorkspaceConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 远程环境设置界面，暂时命名为2，待做完功能直接替代掉老的RemoteEnvPane
 */
public class RemoteEnvPane2 extends BasicBeanPane<DesignerWorkspaceInfo> {

    private UITextField hostTextField;
    private UIIntNumberField portTextField;
    private UITextField usernameTextField;
    private UIPassWordField passwordTextField;
    private JDialog dialog;
    private UILabel message;
    private UIButton okButton;
    private UIButton cancelButton;

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

        message = new UILabel();
        okButton = new UIButton(Inter.getLocText("OK"));
        cancelButton = new UIButton(Inter.getLocText("Cancel"));
    }

    private void tryConnectRemoteEnv() {
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
    
                final DesignerWorkspaceInfo remoteEnv = updateBean();
                WorkContext.getConnector().testConnection(remoteEnv.getConnection());
                return null;
            }

            @Override
            protected void done() {
                okButton.setEnabled(true);
                try {
                    get();
                    message.setText(Inter.getLocText("Fine-Designer_Basic_Remote_Connect_Successful"));
                } catch (Exception e) {
                    message.setText(Inter.getLocText("Fine-Designer_Basic_Remote_Connect_Failed"));
                }
            }
        };
        worker.execute();
        initMessageDialog();
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                worker.cancel(true);
            }
        });

        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                worker.cancel(true);
            }
        });

        dialog.setVisible(true);
        dialog.dispose();
    }

    private void initMessageDialog() {
        message.setText(Inter.getLocText("Fine-Designer_Basic_Remote_Env_Try") + "...");
        message.setBorder(BorderFactory.createEmptyBorder(8, 5, 0, 0));
        okButton.setEnabled(false);

        dialog = new JDialog((Dialog) SwingUtilities.getWindowAncestor(RemoteEnvPane2.this), Inter.getLocText("Datasource-Test_Connection"), true);

        dialog.setSize(new Dimension(268, 118));
        okButton.setEnabled(false);
        JPanel jp = new JPanel();
        JPanel upPane = new JPanel();
        JPanel downPane = new JPanel();
        UILabel uiLabel = new UILabel(UIManager.getIcon("OptionPane.informationIcon"));
        upPane.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        upPane.add(uiLabel);
        upPane.add(message);
        downPane.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 0));
        downPane.add(okButton);
        downPane.add(cancelButton);
        jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
        jp.add(upPane);
        jp.add(downPane);
        dialog.add(jp);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(RemoteEnvPane2.this));
    }

    @Override
    protected String title4PopupWindow() {
        return "RemoteEnv";
    }

    @Override
    public void populateBean(DesignerWorkspaceInfo config) {
        if (config == null) {
            return;
        }
        WorkspaceConnection connection = config.getConnection();
        if (connection != null) {
            hostTextField.setText(connection.getIp());
            if (connection.getPort() != 0) {
                portTextField.setValue(connection.getPort());
            }
            usernameTextField.setText(connection.getUserName());
            passwordTextField.setText(connection.getPassword());
        }
    }

    @Override
    public DesignerWorkspaceInfo updateBean() {
    
        DesignerWorkspaceInfo config = new DesignerWorkspaceInfo();
        WorkspaceConnection connection = new WorkspaceConnection(hostTextField.getText(), (int) portTextField.getValue(), usernameTextField.getText(), passwordTextField.getText());
        config.setConnection(connection);
        config.setType(DesignerWorkspaceType.Remote);
        return config;
    }
}
