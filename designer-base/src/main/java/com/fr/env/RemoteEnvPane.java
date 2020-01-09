package com.fr.env;

import com.fr.base.FRContext;
import com.fr.base.ServerConfig;
import com.fr.design.DesignerEnvManager;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.border.UITitledBorder;
import com.fr.design.env.RemoteDesignerWorkspaceInfo;
import com.fr.design.fun.DesignerEnvProcessor;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ipasswordfield.UIPassWordField;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.scrollruler.ModLineBorder;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;
import com.fr.third.guava.base.Strings;
import com.fr.workspace.WorkContext;
import com.fr.workspace.connect.WorkspaceConnectionInfo;
import com.fr.workspace.engine.exception.WorkspaceAuthException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import static com.fr.design.layout.TableLayout.FILL;
import static com.fr.design.layout.TableLayout.PREFERRED;
import static com.fr.env.TestConnectionResult.AUTH_FAILED;
import static com.fr.third.guava.base.Optional.fromNullable;

/**
 * @author yaohwu
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class RemoteEnvPane extends BasicBeanPane<RemoteDesignerWorkspaceInfo> {

    private static final Color TIPS_FONT_COLOR = new Color(0x8f8f92);

    private JDialog dialog;
    private UILabel message = new UILabel();
    private UIButton okButton = new UIButton(Toolkit.i18nText("Fine-Design_Report_OK"));
    private UIButton cancelButton = new UIButton(Toolkit.i18nText("Fine-Design_Basic_Cancel"));
    private UILabel uiLabel = new UILabel();

    /**
     * 是否启用 https 勾选框
     */
    private UICheckBox httpsCheckbox = new UICheckBox(Toolkit.i18nText("Fine-Design_Basic_Remote_Env_Enable_Https"));
    /**
     * 主机位置输入框
     */
    private UITextField remoteEnvURLInput = new UITextField();
    /**
     * 主机名输入框
     */
    private UITextField hostNameInput = new UITextField();
    /**
     * 端口输入框
     */
    private UITextField portInput = new UITextField();
    /**
     * Web 应用名输入框
     */
    private UITextField webAppNameInput = new UITextField();
    /**
     * Servlet 名称输入框
     */
    private UITextField servletNameInput = new UITextField();
    /**
     * 用户名
     */
    private UITextField usernameInput = new UITextField();
    /**
     * 密码
     */
    private UIPassWordField passwordInput = new UIPassWordField();
    /**
     * https证书路径
     */
    private UITextField certPathInput = new UITextField();
    /**
     * https密钥
     */
    private UIPassWordField certSecretKeyInput = new UIPassWordField();
    /**
     * 选择证书文件按钮
     */
    private UIButton fileChooserButton = new UIButton("...");
    /**
     * 主机位置
     */
    @SuppressWarnings("squid:S1948")
    private RemoteWorkspaceURL remoteWorkspaceURL = new RemoteWorkspaceURL("");
    /**
     * https 配置面板
     */
    private JPanel httpsConfigPanel;
    /**
     * https 密钥标签
     */
    private UILabel certSecretKeyLabel = new UILabel(Toolkit.i18nText("Fine-Design_Basic_Remote_Env_Https_Secret_Key"));
    /**
     * https证书路径标签
     */
    private UILabel certPathLabel = new UILabel(Toolkit.i18nText("Fine-Design_Basic_Remote_Env_Https_Cert_Path"));
    /**
     * https 证书路径输入框
     */
    private JPanel httpsCertFileInputPanel;
    /**
     * 主机名，web应用，Servlet，端口监听器
     */
    @SuppressWarnings("squid:S1948")
    private DocumentListener individualDocListener = new DocumentListener() {

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateRemoteURL();
            fillRemoteEnvURLField();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            changedUpdate(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            changedUpdate(e);
        }

        private void updateRemoteURL() {
            boolean isHttps = httpsCheckbox.isSelected();
            String host = hostNameInput.getText();
            String port = portInput.getText();
            String web = webAppNameInput.getText();
            String servlet = servletNameInput.getText();
            remoteWorkspaceURL.setHttps(isHttps);
            remoteWorkspaceURL.setHost(host);
            remoteWorkspaceURL.setPort(port);
            remoteWorkspaceURL.setWeb(web);
            remoteWorkspaceURL.setServlet(servlet);
            remoteWorkspaceURL.resetUrl();
        }
    };
    /**
     * 路径输入框监听器
     */
    @SuppressWarnings("squid:S1948")
    private DocumentListener overallDocListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            actionURLInputChange();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            actionURLInputChange();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            actionURLInputChange();
        }

        private void actionURLInputChange() {
            remoteWorkspaceURL = new RemoteWorkspaceURL(remoteEnvURLInput.getText());
            fillIndividualField();

            httpsCheckbox.setSelected(remoteWorkspaceURL.getHttps());
            boolean isHttps = httpsCheckbox.isSelected();
            DesignerEnvManager.getEnvManager().setHttps(isHttps);
            fileChooserButton.setEnabled(isHttps);
            updateHttpsConfigPanel();
        }
    };
    /**
     * https checkbox listener
     */
    @SuppressWarnings("squid:S1948")
    private ActionListener httpsCheckboxListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean isHttps = httpsCheckbox.isSelected();

            DesignerEnvManager.getEnvManager().setHttps(isHttps);

            fileChooserButton.setEnabled(isHttps);
            updateHttpsConfigPanel();

            remoteWorkspaceURL.setHttps(isHttps);
            // reset下url，将勾选状态是否htpps加到url里
            remoteWorkspaceURL.resetUrl();

            fillRemoteEnvURLField();
            fillIndividualField();
        }
    };
    /**
     * 测试链接对话框确定取消按钮面板
     */
    private JPanel dialogDownPane = new JPanel();


    public RemoteEnvPane() {
        // 配置内容面板
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        new EmptyBorder(6, 0, 0, 0),
                        UITitledBorder.createBorderWithTitle(Toolkit.i18nText("Fine-Design_Basic_Remote_Env_Config")))
        );


        // 服务器地址地址
        final JPanel configPanel = new JPanel(new BorderLayout());
        configPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        new EmptyBorder(15, 0, 0, 0),
                        BorderFactory.createTitledBorder(
                                new ModLineBorder(ModLineBorder.TOP),
                                Toolkit.i18nText("Fine-Design_Basic_Remote_Env_Server")
                        )
                )
        );


        certPathLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        certSecretKeyLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        packConfigPanel(configPanel);

        // 服务器账号配置
        JPanel accountPanel = new JPanel(new BorderLayout());


        accountPanel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(15, 0, 0, 0),
                BorderFactory.createTitledBorder(
                        new ModLineBorder(ModLineBorder.TOP),
                        Toolkit.i18nText("Fine-Design_Basic_Remote_Env_Platform_Account")
                )
        ));

        packAccountPanel(accountPanel);


        // 测试链接按钮
        JPanel testPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        testPanel.setBorder(BorderFactory.createEmptyBorder());
        testPanel.setPreferredSize(new Dimension(437, 20));
        UIButton testConnectionButton = new UIButton(Toolkit.i18nText("Fine-Design_Basic_Remote_Env_Test_Connection"));

        testConnectionButton.setToolTipText(Toolkit.i18nText("Fine-Design_Basic_Datasource_Test_Connection"));
        testConnectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                tryConnectRemoteEnv();
            }
        });
        testPanel.add(testConnectionButton);


        contentPanel.add(configPanel, BorderLayout.NORTH);
        contentPanel.add(accountPanel, BorderLayout.CENTER);

        this.setLayout(new BorderLayout());
        this.add(contentPanel, BorderLayout.NORTH);
        this.add(testPanel, BorderLayout.CENTER);
    }

    @Override
    public void populateBean(RemoteDesignerWorkspaceInfo ob) {
        WorkspaceConnectionInfo connection = ob.getConnection();
        if (connection != null) {
            this.remoteWorkspaceURL = Strings.isNullOrEmpty(connection.getUrl())
                    ? RemoteWorkspaceURL.createDefaultURL()
                    : new RemoteWorkspaceURL(connection.getUrl());
            String username = fromNullable(connection.getUserName()).or(StringUtils.EMPTY);
            String pwd = fromNullable(connection.getPassword()).or(StringUtils.EMPTY);
            String certPath = fromNullable(connection.getCertPath()).or(StringUtils.EMPTY);
            String certSecretKey = fromNullable(connection.getCertSecretKey()).or(StringUtils.EMPTY);
            this.usernameInput.setText(username);
            this.passwordInput.setText(pwd);
            this.certPathInput.setText(certPath);
            this.certSecretKeyInput.setText(certSecretKey);

        } else {
            this.remoteWorkspaceURL = RemoteWorkspaceURL.createDefaultURL();
            this.usernameInput.setText(StringUtils.EMPTY);
            this.passwordInput.setText(StringUtils.EMPTY);
        }

        fillRemoteEnvURLField();
        fillIndividualField();
        httpsCheckbox.setSelected(this.remoteWorkspaceURL.getHttps());

        DesignerEnvManager.getEnvManager().setHttps(this.remoteWorkspaceURL.getHttps());
        fileChooserButton.setEnabled(this.remoteWorkspaceURL.getHttps());
        updateHttpsConfigPanel();


    }

    @Override
    public RemoteDesignerWorkspaceInfo updateBean() {
        String url = this.remoteWorkspaceURL.getURL();
        String username = this.usernameInput.getText();
        String password = new String(this.passwordInput.getPassword());
        DesignerEnvProcessor envProcessor = ExtraDesignClassManager.getInstance().getSingle(DesignerEnvProcessor.XML_TAG);
        if (envProcessor != null) {
            url = envProcessor.changeEnvPathBeforeConnect(username, password, url);
        }
        WorkspaceConnectionInfo connection = new WorkspaceConnectionInfo(
                url,
                username,
                password,
                this.certPathInput.getText(),
                new String(this.certSecretKeyInput.getPassword()));

        return RemoteDesignerWorkspaceInfo.create(connection);
    }

    @Override
    protected String title4PopupWindow() {
        return "Remote";
    }


    private void packConfigPanel(JPanel configPanel) {


        // 主机名
        UILabel hostNameLabel = new UILabel(Toolkit.i18nText("Fine-Design_Basic_Remote_Env_Host_IP"));
        hostNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        // 端口
        UILabel portLabel = new UILabel(Toolkit.i18nText("Fine-Design_Basic_Remote_Env_Port"));
        portLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        // web应用
        UILabel webAppNameLabel = new UILabel(Toolkit.i18nText("Fine-Design_Basic_Remote_Env_Web_Name"));
        webAppNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        // servlet
        UILabel servletNameLabel = new UILabel(Toolkit.i18nText("Fine-Design_Basic_Remote_Env_Servlet_Name"));
        servletNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        // 主机位置
        UILabel remoteEnvURLLabel = new UILabel(Toolkit.i18nText("Fine-Design_Basic_Remote_Env_URL"));
        remoteEnvURLLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        enableSubDocListener();

        JPanel urlPanel = TableLayoutHelper.createGapTableLayoutPane(
                new Component[][]{
                        new Component[]{hostNameLabel, hostNameInput},
                        new Component[]{portLabel, portInput},
                        new Component[]{webAppNameLabel, webAppNameInput},
                        new Component[]{servletNameLabel, servletNameInput},
                        new Component[]{remoteEnvURLLabel, remoteEnvURLInput}
                },
                new double[]{PREFERRED, PREFERRED, PREFERRED, PREFERRED, PREFERRED},
                new double[]{PREFERRED, FILL},
                5,
                10

        );

        TableLayoutHelper.modifyTableLayoutIndexVGap(urlPanel, 0, 10);

        JTextPane urlTipsPane = new JTextPane();
        urlTipsPane.setEditable(false);
        urlTipsPane.setText(Toolkit.i18nText("Fine-Design_Basic_Remote_Env_Server_Config_Tips"));
        urlTipsPane.setBackground(urlPanel.getBackground());
        urlTipsPane.setForeground(TIPS_FONT_COLOR);


        httpsCheckbox.addActionListener(httpsCheckboxListener);
        // 初始化 https 可被刷新展示的面板
        httpsConfigPanel = new JPanel(new BorderLayout());
        // 初始化 https 证书文件输入框
        httpsCertFileInputPanel = createHttpsCertFileInputPanel();
        packHttpsConfigPanel();

        JTextPane httpsTipsPane = new JTextPane();
        httpsTipsPane.setEditable(false);
        httpsTipsPane.setText(Toolkit.i18nText("Fine-Design_Basic_Remote_Env_Https_Tips"));
        httpsTipsPane.setBackground(configPanel.getBackground());
        httpsTipsPane.setForeground(TIPS_FONT_COLOR);

        configPanel.add(TableLayoutHelper.createTableLayoutPane(
                new Component[][]{
                        new Component[]{urlPanel},
                        new Component[]{urlTipsPane},
                        new Component[]{httpsConfigPanel},
                        new Component[]{httpsTipsPane}
                },
                new double[]{PREFERRED, PREFERRED, PREFERRED, PREFERRED},
                new double[]{FILL}
        ));

        setDefaultAppAndServlet();
    }

    /**
     * 设置 app 和 servlet 默认值
     */
    private void setDefaultAppAndServlet() {
        String appName;
        String servletName;
        try {
            appName = FRContext.getCommonOperator().getAppName();
        } catch (Exception ignored) {
            appName = RemoteWorkspaceURL.DEFAULT_WEB_APP_NAME;
        }
        try {
            servletName = ServerConfig.getInstance().getServletName();
        } catch (Exception ignored) {
            servletName = RemoteWorkspaceURL.DEFAULT_SERVLET_NAME;
        }
        webAppNameInput.setText(appName);
        servletNameInput.setText(servletName);
    }


    private void enableSubDocListener() {
        hostNameInput.getDocument().addDocumentListener(individualDocListener);
        portInput.getDocument().addDocumentListener(individualDocListener);
        webAppNameInput.getDocument().addDocumentListener(individualDocListener);
        servletNameInput.getDocument().addDocumentListener(individualDocListener);
    }

    private void disableSubDocListener() {
        hostNameInput.getDocument().removeDocumentListener(individualDocListener);
        portInput.getDocument().removeDocumentListener(individualDocListener);
        webAppNameInput.getDocument().removeDocumentListener(individualDocListener);
        servletNameInput.getDocument().removeDocumentListener(individualDocListener);
    }

    private void packHttpsConfigPanel() {


        double[] rows = new double[]{PREFERRED};
        boolean httpsEnabled = httpsCheckbox.isSelected();

        if (httpsEnabled) {
            rows = new double[]{PREFERRED, PREFERRED, PREFERRED};
        }
        JPanel content = TableLayoutHelper.createGapTableLayoutPane(
                new Component[][]{
                        new Component[]{httpsCheckbox, new JPanel()},
                        new Component[]{certPathLabel, httpsCertFileInputPanel},
                        new Component[]{certSecretKeyLabel, certSecretKeyInput}
                },
                rows,
                new double[]{PREFERRED, FILL},
                5,
                10
        );
        httpsConfigPanel.add(content, BorderLayout.CENTER);
    }


    private void packAccountPanel(JPanel accountPanel) {

        // 用户名
        UILabel userNameLabel = new UILabel(Toolkit.i18nText("Fine-Design_Basic_Remote_Env_Account_Username"));
        userNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        // 密码
        UILabel passwordLabel = new UILabel(Toolkit.i18nText("Fine-Design_Basic_Remote_Env_Account_Password"));
        passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        //输入密码的时候检测下大写锁定
        passwordInput.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (java.awt.Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
                    passwordInput.setToolTipText(Toolkit.i18nText("Fine-Design_Basic_CapsLock"));
                } else {
                    passwordInput.setToolTipText(null);
                }
                ToolTipManager.sharedInstance().setInitialDelay(100);
            }
        });

        Component[][] accountComponents = new Component[][]{
                new Component[]{userNameLabel, usernameInput},
                new Component[]{passwordLabel, passwordInput}
        };

        JPanel content = TableLayoutHelper.createGapTableLayoutPane(accountComponents,
                new double[]{PREFERRED, PREFERRED},
                new double[]{PREFERRED, FILL},
                5,
                10
        );
        TableLayoutHelper.modifyTableLayoutIndexVGap(content, 0, 10);

        accountPanel.add(content, BorderLayout.CENTER);
    }


    private JPanel createHttpsCertFileInputPanel() {
        JPanel inputPanel = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
        inputPanel.setLayout(FRGUIPaneFactory.createBorderLayout());
        inputPanel.add(certPathInput, BorderLayout.CENTER);
        inputPanel.add(fileChooserButton, BorderLayout.EAST);
        fileChooserButton.setPreferredSize(new Dimension(20, 20));
        fileChooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int saveValue = fileChooser.showOpenDialog(SwingUtilities.getWindowAncestor(RemoteEnvPane.this));
                if (saveValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    certPathInput.setText(selectedFile.getAbsolutePath());
                }
            }
        });
        return inputPanel;
    }

    private void tryConnectRemoteEnv() {
        final RemoteDesignerWorkspaceInfo remoteEnv = updateBean();
        final WorkspaceConnectionInfo connection = remoteEnv.getConnection();

        final SwingWorker<TestConnectionResult, Void> worker = new SwingWorker<TestConnectionResult, Void>() {

            @Override
            protected TestConnectionResult doInBackground() throws Exception {

                DesignerEnvManager.getEnvManager().setCertificatePath(connection.getCertPath());
                DesignerEnvManager.getEnvManager().setCertificatePass(connection.getCertSecretKey());
                try {
                    return TestConnectionResult.parse(WorkContext.getConnector().testConnection(connection), connection);
                } catch (WorkspaceAuthException ignored) {
                    return AUTH_FAILED;
                }
            }

            @Override
            protected void done() {
                okButton.setEnabled(true);
                try {
                    TestConnectionResult result = get();
                    message.setText(result.getText());
                    uiLabel.setIcon(result.getIcon());
                } catch (Exception e) {
                    FineLoggerFactory.getLogger().error(e, e.getMessage());
                    message.setText(Toolkit.i18nText("Fine-Design_Basic_Remote_Connect_Failed"));
                    uiLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
                }
                dialogDownPane.remove(cancelButton);
                dialogDownPane.revalidate();
                dialogDownPane.repaint();
            }
        };
        worker.execute();
        initMessageDialog();
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                worker.cancel(true);
            }
        });

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                worker.cancel(true);
            }
        });

        dialog.setVisible(true);
        dialog.dispose();
    }

    private void initMessageDialog() {
        message.setText(Toolkit.i18nText("Fine-Design_Basic_Remote_Env_Try") + "...");
        message.setBorder(BorderFactory.createEmptyBorder(8, 5, 0, 0));
        okButton.setEnabled(false);

        dialog = new JDialog((Dialog) SwingUtilities.getWindowAncestor(RemoteEnvPane.this), UIManager.getString("OptionPane.messageDialogTitle"), true);

        dialog.setSize(new Dimension(308, 132));
        okButton.setEnabled(false);
        JPanel jp = new JPanel();
        JPanel upPane = new JPanel();
        dialogDownPane = new JPanel();
        uiLabel = new UILabel(UIManager.getIcon("OptionPane.informationIcon"));
        upPane.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        upPane.add(uiLabel);
        upPane.add(message);
        dialogDownPane.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 0));
        dialogDownPane.add(okButton);
        dialogDownPane.add(cancelButton);
        jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
        jp.add(upPane);
        jp.add(dialogDownPane);
        dialog.add(jp);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(RemoteEnvPane.this));
    }

    /**
     * 自动填充主机位置输入框
     */
    private void fillRemoteEnvURLField() {
        remoteEnvURLInput.getDocument().removeDocumentListener(overallDocListener);
        remoteEnvURLInput.setText(remoteWorkspaceURL.getURL());
        remoteEnvURLInput.getDocument().addDocumentListener(overallDocListener);
    }

    /**
     * 自动填充子条目输入框
     */
    private void fillIndividualField() {
        if (remoteWorkspaceURL == null) {
            return;
        }
        disableSubDocListener();
        hostNameInput.setText(remoteWorkspaceURL.hasDefaultHostName() ? StringUtils.EMPTY : remoteWorkspaceURL.getHost());
        portInput.setText(remoteWorkspaceURL.getPort());
        webAppNameInput.setText(remoteWorkspaceURL.getWeb());
        servletNameInput.setText(remoteWorkspaceURL.getServlet());
        enableSubDocListener();
    }

    private void updateHttpsConfigPanel() {
        httpsConfigPanel.removeAll();
        packHttpsConfigPanel();
        httpsConfigPanel.revalidate();
        httpsConfigPanel.repaint();
    }
}
