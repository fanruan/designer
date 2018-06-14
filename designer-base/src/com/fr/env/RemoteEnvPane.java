package com.fr.env;

import com.fr.base.FRContext;
import com.fr.design.env.RemoteEnvConfig;
import com.fr.design.DesignerEnvManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.border.UITitledBorder;
import com.fr.design.dialog.InformationWarnPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ipasswordfield.UIPassWordField;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.scrollruler.ModLineBorder;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ProductConstants;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import static com.fr.design.layout.TableLayout.FILL;
import static com.fr.design.layout.TableLayout.PREFERRED;

/**
 * @author yaohwu
 */
public class RemoteEnvPane extends BasicBeanPane<RemoteEnvConfig> {

    private static final Color TIPS_FONT_COLOR = new Color(0x8f8f92);

    /**
     * 是否启用 https 勾选框
     */
    private UICheckBox httpsCheckbox = new UICheckBox(Inter.getLocText("FR-Designer_RemoteEnv_Enable_Https"));
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
    private RemoteEnvURL remoteEnvURL = new RemoteEnvURL("");
    /**
     * https 配置面板
     */
    private JPanel httpsConfigPanel;
    /**
     * https 密钥标签
     */
    private UILabel certSecretKeyLabel = new UILabel(Inter.getLocText("FR-Designer_RemoteEnv_Https_Secret_Key"));
    /**
     * https证书路径标签
     */
    private UILabel certPathLabel = new UILabel(Inter.getLocText("FR-Designer_RemoteEnv_Https_Cert_Path"));
    /**
     * https 证书路径输入框
     */
    private JPanel httpsCertFileInputPanel;
    /**
     * 主机名，web应用，Servlet，端口监听器
     */
    private DocumentListener individualDocListener = new DocumentListener() {

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateRemoteURL();
            fillRemoteEnvURLField();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            updateRemoteURL();
            fillRemoteEnvURLField();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateRemoteURL();
            fillRemoteEnvURLField();
        }
    };
    /**
     * 路径输入框监听器
     */
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
    };
    /**
     * https checkbox listener
     */
    private ActionListener httpsCheckboxListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean isHttps = httpsCheckbox.isSelected();

            DesignerEnvManager.getEnvManager().setHttps(isHttps);

            fileChooserButton.setEnabled(isHttps);
            updateHttpsConfigPanel();

            remoteEnvURL.setHttps(isHttps);
            fillRemoteEnvURLField();
            fillIndividualField();
        }
    };


    public RemoteEnvPane() {
        // 配置内容面板
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        new EmptyBorder(6, 0, 0, 0),
                        UITitledBorder.createBorderWithTitle(Inter.getLocText("FR-Designer_RemoteEnv_Config")))
        );


        // 服务器地址地址
        JPanel configPanel = new JPanel(new BorderLayout());
        configPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        new EmptyBorder(15, 0, 0, 0),
                        BorderFactory.createTitledBorder(
                                new ModLineBorder(ModLineBorder.TOP),
                                Inter.getLocText("FR-Designer_RemoteEnv_Server")
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
                        Inter.getLocText("FR-Designer_RemoteEnv_Platform_Account")
                )
        ));

        packAccountPanel(accountPanel);


        // 测试链接按钮
        JPanel testPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        testPanel.setBorder(BorderFactory.createEmptyBorder());
        testPanel.setPreferredSize(new Dimension(437, 20));
        UIButton testConnectionButton = new UIButton(Inter.getLocText("FR-Designer_RemoteEnv_Test_Connection"));

        testConnectionButton.setToolTipText(Inter.getLocText("Datasource-Test_Connection"));
        testConnectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                if (testConnection()) {
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(RemoteEnvPane.this), Inter.getLocText("Datasource-Connection_successfully"));
                }
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
    public void populateBean(RemoteEnvConfig ob) {

        if (StringUtils.isEmpty(ob.getPath())) {
            remoteEnvURL = RemoteEnvURL.createDefaultURL();
        } else {
            remoteEnvURL = new RemoteEnvURL(ob.getPath());
        }
        fillRemoteEnvURLField();
        fillIndividualField();
        httpsCheckbox.setSelected(remoteEnvURL.getHttps());

        DesignerEnvManager.getEnvManager().setHttps(remoteEnvURL.getHttps());
        fileChooserButton.setEnabled(remoteEnvURL.getHttps());
        updateHttpsConfigPanel();

        String username = EnvConfigUtils.getUsername(ob);
        String pwd = EnvConfigUtils.getPassword(ob);
        this.usernameInput.setText(username == null ? StringUtils.EMPTY : pwd);
        this.passwordInput.setText(ob.getPassword() == null ? StringUtils.EMPTY : ob.getPassword());
    }

    @Override
    public RemoteEnvConfig updateBean() {

        String path = remoteEnvURL.getURL();
        String user = this.usernameInput.getText();
        String password = new String(this.passwordInput.getPassword());
        return null;

        //return new RemoteEnvConfig(path, user, password);
    }

    @Override
    protected String title4PopupWindow() {
        return "Remote";
    }


    private void packConfigPanel(JPanel configPanel) {


        // 主机名
        UILabel hostNameLabel = new UILabel(Inter.getLocText("FR-Designer_RemoteEnv_Host_IP"));
        hostNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        // 端口
        UILabel portLabel = new UILabel(Inter.getLocText("FR-Designer_RemoteEnv_Port"));
        portLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        // web应用
        UILabel webAppNameLabel = new UILabel(Inter.getLocText("FR-Designer_RemoteEnv_Web_Name"));
        webAppNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        // servlet
        UILabel servletNameLabel = new UILabel(Inter.getLocText("FR-Designer_RemoteEnv_Servlet_Name"));
        servletNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        // 主机位置
        UILabel remoteEnvURLLabel = new UILabel(Inter.getLocText("FR-Designer_RemoteEnv_URL"));
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
        urlTipsPane.setText(Inter.getLocText("FR-Designer_RemoteEnv_Server_Config_Tips"));
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
        httpsTipsPane.setText(Inter.getLocText("FR-Designer_RemoteEnv_Https_Tips"));
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
        UILabel userNameLabel = new UILabel(Inter.getLocText("FR-Designer_RemoteEnv_Account_Username"));
        userNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        // 密码
        UILabel passwordLabel = new UILabel(Inter.getLocText("FR-Designer_RemoteEnv_Account_Password"));
        passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        //输入密码的时候检测下大写锁定
        passwordInput.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
                    passwordInput.setToolTipText(Inter.getLocText("CapsLock"));
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


    private void setHttpsParas() {
        System.setProperty("javax.net.ssl.trustStore", this.certPathInput.getText());
        System.setProperty("javax.net.ssl.trustStorePassword", new String(this.certSecretKeyInput.getPassword()));
        DesignerEnvManager manager = DesignerEnvManager.getEnvManager();
        manager.setCertificatePath(this.certPathInput.getText());
        manager.setCertificatePass(new String(this.certSecretKeyInput.getPassword()));
        manager.setHttps(this.httpsCheckbox.isSelected());
    }

    private boolean testConnection() {
        String url = remoteEnvURL.getURL();
        //RemoteEnv env = new RemoteEnv(url, usernameInput.getText(), new String(passwordInput.getPassword()));
        RemoteEnv env = null;
        boolean connect = false;
        try {
            if (StringUtils.isNotEmpty(url)) {
                if (remoteEnvURL.getHttps()) {
                    setHttpsParas();
                }
                connect = env.testConnectionWithOutRegisteServer(this);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    Inter.getLocText("Datasource-Connection_failed"),
                    UIManager.getString("OptionPane.messageDialogTitle", this.getLocale()),
                    JOptionPane.ERROR_MESSAGE
            );
            FRContext.getLogger().error(e.getMessage(), e);
        }
        if (connect) {
            try {
                String remoteVersion = env.getDesignerVersion();
                if (StringUtils.isBlank(remoteVersion) || ComparatorUtils.compare(remoteVersion, ProductConstants.DESIGNER_VERSION) < 0) {
                    String info = Inter.getLocText("Server-version-tip") + "。";
                    String moreInfo = Inter.getLocText("Server-version-tip-moreInfo") + "。";
                    new InformationWarnPane(info, moreInfo, Inter.getLocText("Tooltips")).show();
                    return false;
                }
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
        return connect;
    }

    /**
     * 自动填充主机位置输入框
     */
    private void fillRemoteEnvURLField() {
        remoteEnvURLInput.getDocument().removeDocumentListener(overallDocListener);
        remoteEnvURLInput.setText(remoteEnvURL.getURL());
        remoteEnvURLInput.getDocument().addDocumentListener(overallDocListener);
    }

    /**
     * 自动填充子条目输入框
     */
    private void fillIndividualField() {
        if (remoteEnvURL == null) {
            return;
        }
        disableSubDocListener();
        hostNameInput.setText(remoteEnvURL.hasDefaultHostName() ? StringUtils.EMPTY : remoteEnvURL.getHost());
        portInput.setText(remoteEnvURL.getPort());
        webAppNameInput.setText(remoteEnvURL.getWeb());
        servletNameInput.setText(remoteEnvURL.getServlet());
        enableSubDocListener();
    }

    private void updateRemoteURL() {
        boolean isHttps = httpsCheckbox.isSelected();
        String host = hostNameInput.getText();
        String port = portInput.getText();
        String web = webAppNameInput.getText();
        String servlet = servletNameInput.getText();
        remoteEnvURL.setHttps(isHttps);
        remoteEnvURL.setHost(host);
        remoteEnvURL.setPort(port);
        remoteEnvURL.setWeb(web);
        remoteEnvURL.setServlet(servlet);
    }


    private void updateHttpsConfigPanel() {
        httpsConfigPanel.removeAll();
        packHttpsConfigPanel();
        httpsConfigPanel.revalidate();
        httpsConfigPanel.repaint();
    }

    private void actionURLInputChange() {
        remoteEnvURL = new RemoteEnvURL(remoteEnvURLInput.getText());
        fillIndividualField();

        httpsCheckbox.setSelected(remoteEnvURL.getHttps());
        boolean isHttps = httpsCheckbox.isSelected();
        DesignerEnvManager.getEnvManager().setHttps(isHttps);
        fileChooserButton.setEnabled(isHttps);
        updateHttpsConfigPanel();
    }
}