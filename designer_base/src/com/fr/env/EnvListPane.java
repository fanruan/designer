package com.fr.env;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.*;

import javax.swing.BorderFactory;

import com.fr.design.gui.ilable.UILabel;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.fr.base.BaseUtils;
import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.general.NameObject;
import com.fr.dav.LocalEnv;
import com.fr.design.DesignerEnvManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.InformationWarnPane;
import com.fr.design.gui.controlpane.JListControlPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.ipasswordfield.UIPassWordField;
import com.fr.design.gui.itree.filetree.JFileTree;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.scrollruler.ModLineBorder;
import com.fr.file.filter.OnlyShowDirectoryFileFilter;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.Nameable;
import com.fr.stable.ProductConstants;
import com.fr.stable.StringUtils;
import com.fr.stable.core.PropertyChangeAdapter;

public class EnvListPane extends JListControlPane {
    public EnvListPane() {
        super();
        addEditingListner(new PropertyChangeAdapter() {
            public void propertyChange() {
                String tempName = getEditingName();
                String[] allListNames = nameableList.getAllNames();
                allListNames[nameableList.getSelectedIndex()] = StringUtils.EMPTY;
                if (StringUtils.isEmpty(tempName)) {
                    String[] warning = new String[]{"NOT_NULL_Des", "Please_Rename"};
                    String[] sign = new String[]{",", "!"};
                    nameableList.stopEditing();
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(EnvListPane.this), Inter.getLocText(warning, sign));
                    setWarnigText(editingIndex);
                    return;
                }
                if (!ComparatorUtils.equals(tempName, selectedName) && isNameRepeted(new List[]{Arrays.asList(allListNames)}, tempName)) {
                    String[] waning = new String[]{"already_exists", "Utils-Report_Runtime_Env", "Please_Rename"};
                    String[] sign = new String[]{"", tempName + ",", "!"};
                    nameableList.stopEditing();
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(EnvListPane.this), Inter.getLocText(waning, sign));
                    setWarnigText(editingIndex);
                    return;
                }
            }
        });
    }

    /**
     * 生成添加按钮的NameableCreator
     *
     * @return 返回添加按钮的NameableCreator
     */
    public NameableCreator[] createNameableCreators() {
        NameableCreator local = new NameObjectCreator(Inter.getLocText("Env-Local_Directory"), "com/fr/design/images/data/bind/localconnect.png",
                LocalEnv.class, LocalEnvPane.class);
        NameableCreator remote = new NameObjectCreator(Inter.getLocText("Env-Remote_Server"), "com/fr/design/images/data/bind/distanceconnect.png",
                RemoteEnv.class, RemoteEnvPane.class);
        return new NameableCreator[]{local, remote};
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("Env-Configure_Workspace");
    }

    /**
     * 弹出选中环境的面板
     *
     * @param selectedEnv 选中的环境
     */
    public void populateEnvManager(String selectedEnv) {
        DesignerEnvManager mgr = DesignerEnvManager.getEnvManager();
        Iterator<String> nameIt = mgr.getEnvNameIterator();
        List<NameObject> nameObjectList = new ArrayList<NameObject>();
        nameIt.hasNext();
        while (nameIt.hasNext()) {
            String name = nameIt.next();
            nameObjectList.add(new NameObject(name, mgr.getEnv(name)));
        }

        this.populate(nameObjectList.toArray(new NameObject[nameObjectList.size()]));

        if (StringUtils.isBlank(selectedEnv)) {
            selectedEnv = mgr.getCurEnvName();
        }
        this.setSelectedName(selectedEnv);
    }


    /**
     * 更新designerEnvManager里面所有的Env
     *
     * @return 返回选中的环境的名字
     */
    public String updateEnvManager() {
        DesignerEnvManager mgr = DesignerEnvManager.getEnvManager();
        mgr.clearAllEnv();
        Nameable[] res = this.update();
        NameObject[] envNameObjectArray = new NameObject[res.length];
        java.util.Arrays.asList(res).toArray(envNameObjectArray);
        for (int i = 0; i < envNameObjectArray.length; i++) {
            NameObject nameObject = envNameObjectArray[i];
            mgr.putEnv(nameObject.getName(), (Env) nameObject.getObject());
        }
        
        return this.getSelectedName();
    }

	public static class LocalEnvPane extends BasicBeanPane<LocalEnv> {

        private UITextField pathTextField;
        private JFileTree localEnvTree;

        public LocalEnvPane() {
            this.setLayout(FRGUIPaneFactory.createM_BorderLayout());

            // northPane
            JPanel northPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
            this.add(northPane, BorderLayout.NORTH);

            northPane.add(new UILabel(Inter.getLocText("Location") + ":"), BorderLayout.WEST);
            northPane.add(pathTextField = new UITextField(), BorderLayout.CENTER);

            // 删除选择文件按钮 添加JFileTree

            // centerPane
            JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
            this.add(centerPane, BorderLayout.CENTER);

            // 添加JFileTree
            localEnvTree = new JFileTree();
            JScrollPane localEnvPane = new JScrollPane(localEnvTree);
            centerPane.add(localEnvPane, BorderLayout.CENTER);

            // 设置根路径File 和 文件过滤类型
            localEnvTree.setFileFilter(new OnlyShowDirectoryFileFilter());
            localEnvTree.setRootFiles(File.listRoots());
            localEnvTree.addTreeSelectionListener(new TreeSelectionListener() {

                @Override
                public void valueChanged(TreeSelectionEvent e) {
                    pathTextField.setText(localEnvTree.getSelectedFile().getPath());
                }
            });

            UITextArea description = new UITextArea();
            centerPane.add(description, BorderLayout.SOUTH);
            description.setText(Inter.getLocText("Env-Des1"));
            description.setEditable(false);
        }

        @Override
        protected String title4PopupWindow() {
            return Inter.getLocText("Location");
        }

        @Override
        public LocalEnv updateBean() {
            String path = pathTextField.getText();
            return LocalEnv.createEnv(path);
        }

        public String getPath() {
            return pathTextField.getText();
        }

        @Override
        public void populateBean(LocalEnv ob) {
            if (StringUtils.isBlank(ob.getPath())) {
                return;
            }
            pathTextField.setText(ob.getPath());

            final File tmpFile = new File(ob.getPath());
            localEnvTree.selectFile(tmpFile);
            localEnvTree.setEnabled(true);
        }
    }

    public static class RemoteEnvPane extends BasicBeanPane<RemoteEnv> {
    	public static final int HTTPS_HOST_INDEX = 8;
    	public static final int HTTP_HOST_INDEX = 7;
    	
    	private UICheckBox httpsCheckBox = new UICheckBox(Inter.getLocText("FR-Designer_Https_Enable"));
        private UITextField servletPathField = new UITextField();
        private UITextField hostNameField = new UITextField();
        private UITextField portNameField = new UITextField();
        private UITextField webApplyField = new UITextField();
        private UITextField servletField = new UITextField();
        private UITextField userTextField = new UITextField();
        private UIPassWordField passwordTextField = new UIPassWordField();
        private UITextField certificatePath = new UITextField();
        private UIPassWordField certificatePass = new UIPassWordField();
        private UIButton chooseDirBtn = new UIButton("...");
        private String envPath;
        private String hint;
        
        // 各种参数面板
        private JPanel northPane;
        // 提示面板
        private JPanel previewPane;
        // 面板的所有组件
        private Component[][]  coms;
        DocumentListener docListener = new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                showServletPathField(httpsCheckBox.isSelected());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                showServletPathField(httpsCheckBox.isSelected());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                showServletPathField(httpsCheckBox.isSelected());
            }
        };
        
        private void setHttpsState(boolean enable){
        	chooseDirBtn.setEnabled(enable);
        	DesignerEnvManager.getEnvManager().setHttps(enable);
        }

        public RemoteEnvPane() {
            this.setBorder(BorderFactory.createTitledBorder(new ModLineBorder(ModLineBorder.TOP), Inter.getLocText("Config_Servlet")));
            this.setLayout(FRGUIPaneFactory.createM_BorderLayout());
            // 位置
            JPanel servletPathPane = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
            servletPathPane.add(new UILabel(Inter.getLocText("Server_Path") + ":"));
            servletPathField.setEditable(false);

            hostNameField.getDocument().addDocumentListener(docListener);
            portNameField.getDocument().addDocumentListener(docListener);
            webApplyField.getDocument().addDocumentListener(docListener);
            servletField.getDocument().addDocumentListener(docListener);

            // 主机名
            JPanel servletNamePane = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
            servletNamePane.add(new UILabel(Inter.getLocText("Host_Name") + "/IP" + ":"));
            // 端口
            JPanel portNamePane = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
            portNamePane.add(new UILabel(Inter.getLocText("Port") + ":"));
            // web应用
            JPanel webApplyPane = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
            webApplyPane.add(new UILabel(Inter.getLocText("Web_Apply") + ":"));
            // servlet
            JPanel servletPane = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
            servletPane.add(new UILabel("Servlet" + ":"));
            // 用户名
            JPanel userNamePane = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
            userNamePane.add(new UILabel(Inter.getLocText("Username") + ":"));
            // 密码
            JPanel passWordPane = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
            passWordPane.add(new UILabel(Inter.getLocText("Password") + ":"));
            // https证书路径
            JPanel certificatePathPane = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
            certificatePathPane.add(new UILabel(Inter.getLocText("FR-Designer_Certificate_Path")+ ":"));
            // https秘钥
            JPanel certificatePassPane = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
            certificatePassPane.add(new UILabel(Inter.getLocText("FR-Designer_Certificate_Pass") + ":"));
            
            //输入密码的时候检测下大写锁定
            passwordTextField.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    if (java.awt.Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
                        hint = Inter.getLocText("CapsLock");
                    } else {
                        hint = null;
                    }
                    ToolTipManager.sharedInstance().setInitialDelay(100);
                    passwordTextField.setToolTipText(hint);
                }
            });
            
            coms = new Component[][]{
            		new Component[]{initHttpsCheckBoxPane(),new UILabel()},
                    new Component[]{servletPathPane, initServletNameToolBar()},
                    new Component[]{servletNamePane, hostNameField},
                    new Component[]{portNamePane, portNameField},
                    new Component[]{webApplyPane, webApplyField},
                    new Component[]{servletPane, servletField},
                    new Component[]{userNamePane, userTextField},
                    new Component[]{passWordPane, passwordTextField},
                    new Component[]{certificatePathPane,initHttpsPane()},
                    new Component[]{certificatePassPane,certificatePass}
            };
            
            setHttpsState(httpsCheckBox.isSelected());
            initNorthPane(true);
            initCenterPane();
            this.add(northPane, BorderLayout.NORTH);
            this.add(previewPane, BorderLayout.CENTER);
        }
        
        private JToolBar initServletNameToolBar(){
            JToolBar servletNameToolBar = new JToolBar();

            servletNameToolBar.setFloatable(false);
            servletNameToolBar.setLayout(FRGUIPaneFactory.createBorderLayout());

            servletNameToolBar.add(this.servletPathField);
            UIButton testConnctionButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/m_web/cache.png"));
            testConnctionButton.setToolTipText(Inter.getLocText("Datasource-Test_Connection"));
            servletNameToolBar.add(testConnctionButton, BorderLayout.EAST);
            
            testConnctionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ev) {
                    if (testConnection()) {
                        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(RemoteEnvPane.this), Inter.getLocText("Datasource-Connection_successfully"));
                    }
                }
            });
            
            return servletNameToolBar;
        }
        
        private void initCenterPane(){
            // centerPane
            previewPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
            previewPane.setBorder(BorderFactory.createTitledBorder(new ModLineBorder(ModLineBorder.TOP), Inter.getLocText("Note")));

            JTextPane previewTextArea = new JTextPane();

            // e:当行的长度大于所分配的宽度时，将换行.
//			previewTextArea.setLineWrap(true);
            previewTextArea.setEditable(false);
            previewTextArea.setText(Inter.getLocText("Env-Des2"));
            try {
                previewTextArea.getDocument().insertString(previewTextArea.getText().length(), Inter.getLocText("Env_Des"), this.getRedSytleAttribute());
                previewTextArea.getDocument().insertString(previewTextArea.getText().length(), "\n" + Inter.getLocText("FR-Designer_Env_Des_Https"), this.getRedSytleAttribute());
            } catch (BadLocationException e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
            previewPane.add(new JScrollPane(previewTextArea), BorderLayout.CENTER);
        }

        @Override
        protected String title4PopupWindow() {
            return "Remote";
        }
        
        private JPanel initHttpsPane(){
            JPanel httpsPane = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
            httpsPane.setLayout(FRGUIPaneFactory.createBorderLayout());
            httpsPane.add(certificatePath,BorderLayout.CENTER);
            httpsPane.add(chooseDirBtn, BorderLayout.EAST);
            chooseDirBtn.setPreferredSize(new Dimension(25, 25));
            chooseDirBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    int saveValue = fileChooser.showOpenDialog(DesignerContext.getDesignerFrame());
                    if (saveValue == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        certificatePath.setText(selectedFile.getAbsolutePath());
                    }
                }
            });
            return httpsPane;
        }
        
        private JPanel initHttpsCheckBoxPane(){
            JPanel checkBoxPane = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
            checkBoxPane.add(httpsCheckBox);
            httpsCheckBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean isHttps = httpsCheckBox.isSelected();
					DesignerEnvManager manager = DesignerEnvManager.getEnvManager();
					manager.setHttps(isHttps);
					setHttpsState(isHttps);
					updateNorthPane(isHttps);
				}
			});
            return checkBoxPane;
        }
        
        private void setHttpsParas(){
			System.setProperty("javax.net.ssl.trustStore", this.certificatePath.getText());
			System.setProperty("javax.net.ssl.trustStorePassword", new String(this.certificatePass.getPassword()));
			DesignerEnvManager manager = DesignerEnvManager.getEnvManager();
			manager.setCertificatePath(this.certificatePath.getText());
			manager.setCertificatePass(new String(this.certificatePass.getPassword()));
			manager.setHttps(this.httpsCheckBox.isSelected());
        }

        private boolean testConnection() {
            RemoteEnv env = new RemoteEnv();
            String url = servletPathField.getText();
            env.setPath(url);
            env.setUser(userTextField.getText());
            env.setPassword(new String(passwordTextField.getPassword()));
            boolean connect = false;
            try {
                if (!StringUtils.isBlank(servletPathField.getText().trim())) {
            		if(url.startsWith("https:") && !this.httpsCheckBox.isSelected()){
            			JOptionPane.showMessageDialog(this, Inter.getLocText("Datasource-Connection_failed"));
            			return false;
            		}
            		if(url.startsWith("https:")){
            			setHttpsParas();
            		}
                    connect = env.testConnectionWithOutRegisteServer(this);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, Inter.getLocText("Datasource-Connection_failed"));
                FRContext.getLogger().error(e.getMessage(), e);
            }
            if (connect) {
                try {
                    String remoteVersion = env.getDesignerVersion();
                    if (StringUtils.isBlank(remoteVersion) || ComparatorUtils.compare(remoteVersion, ProductConstants.DESIGNER_VERSION) < 0) {
                        String infor = Inter.getLocText("Server-version-tip") + "。";
                        String moreInfo = Inter.getLocText("Server-version-tip-moreInfo") + "。";
                        new InformationWarnPane(infor, moreInfo, Inter.getLocText("Tooltips")).show();
                        return false;
                    }
                } catch (Exception e) {
                    FRContext.getLogger().error(e.getMessage(), e);
                }
            	 
            }

            return connect;
        }

        private AttributeSet getRedSytleAttribute() {
            SimpleAttributeSet ds = new SimpleAttributeSet();
            StyleConstants.setForeground(ds, Color.red);
            return ds;
        }

        /**
         * 显示show the content of serverPathFiled
         * @param isHttps 是否启用https
         */
        public void showServletPathField(boolean isHttps) {
            String s = StringUtils.isBlank(webApplyField.getText()) ? "" : "/", t = StringUtils.isBlank(servletField.getText()) ? "" : "/", colon = StringUtils.isBlank(portNameField.getText()) ? ""
                    : ":";
            String prefix = isHttps ? "https://" : "http://";
            servletPathField.setText(prefix + hostNameField.getText() + colon + portNameField.getText() + s + webApplyField.getText() + t + servletField.getText());
        }

        @Override
        public void populateBean(RemoteEnv ob) {
            envPath = ob.getPath();
            boolean isHttps = false;
            if(envPath != null){
            	isHttps = envPath.startsWith("https");
                httpsCheckBox.setSelected(isHttps);
                setHttpsState(isHttps);
            }
            updateNorthPane(isHttps);
            if (envPath == null || ComparatorUtils.equals(envPath, StringUtils.EMPTY)) {
                this.hostNameField.setText(StringUtils.EMPTY);
                this.portNameField.setText(StringUtils.EMPTY);
                this.webApplyField.setText(StringUtils.EMPTY);
                this.servletField.setText(StringUtils.EMPTY);
                this.servletPathField.setText(StringUtils.EMPTY);
                this.httpsCheckBox.setSelected(false);
            } else {
        		DesignerEnvManager manager = DesignerEnvManager.getEnvManager();
        		this.certificatePath.setText(manager.getCertificatePath());
        		this.certificatePass.setText(manager.getCertificatePass());
                // 第二次出现":"的地方,port位置起始点
                int secondTime = envPath.indexOf(":", envPath.indexOf(":") + 1);
                // 第三次出现"/"的地方
                int thirdTime = envPath.indexOf("/", secondTime + 1);
                // 最后出现"/"的地方
                int lastTime = envPath.lastIndexOf("/");
                String hostName = isHttps ? envPath.substring(HTTPS_HOST_INDEX, secondTime) : envPath.substring(HTTP_HOST_INDEX, secondTime);
                this.hostNameField.setText(hostName);
                if (thirdTime < 0) {
                    this.portNameField.setText(envPath.substring(secondTime + 1));
                    this.webApplyField.setText(StringUtils.EMPTY);
                } else {
                    this.portNameField.setText(envPath.substring(secondTime + 1, thirdTime));
                    if (thirdTime == lastTime) {
                        this.webApplyField.setText(StringUtils.EMPTY);
                    } else {
                        this.webApplyField.setText(envPath.substring(thirdTime + 1, lastTime));
                    }
                    this.servletField.setText(envPath.substring(lastTime + 1));
                }
                this.servletPathField.setText(envPath);
            }

            this.userTextField.setText(ob.getUser() == null ? StringUtils.EMPTY : ob.getUser());

            this.passwordTextField.setText(ob.getPassword() == null ? StringUtils.EMPTY : ob.getPassword());
        }
        
        private void initNorthPane(boolean isHttps){
            double p = TableLayout.PREFERRED;
            double f = TableLayout.FILL;
            double[] rowSize;
            double[] size = {p,p,p,p,p,p,p,p};
            double[] httpsSize = {p,p,p,p,p,p,p,p,p,p};
            if(isHttps){
            	rowSize = httpsSize;
            }else{
            	rowSize = size;
            }
            double[] columnSize = {p, f};
            northPane = TableLayoutHelper.createTableLayoutPane(coms, rowSize, columnSize);
        }
        
        private void updateNorthPane(boolean isHttps){
        	this.removeAll();
        	initNorthPane(isHttps);
            this.add(northPane, BorderLayout.NORTH);
            this.add(previewPane,BorderLayout.CENTER);
            this.revalidate();
            this.doLayout();
            this.repaint();
        }

        @Override
        public RemoteEnv updateBean() {
            String hostName = this.hostNameField.getText();
//			String port = this.portNameField.getText();
            String webApply = this.webApplyField.getText();
            String servlet = this.servletField.getText();
            String path = this.servletPathField.getText();
            String user = this.userTextField.getText();
            String password = new String(this.passwordTextField.getPassword());
            if (isAllEmpty(new String[]{hostName, webApply, servlet})) {
                path = StringUtils.EMPTY;
            }

            return new RemoteEnv(path, user, password);
        }

        private boolean isAllEmpty(String[] strs) {
            for (int i = 0; i < strs.length; i++) {
                if (StringUtils.isNotEmpty(strs[i])) {
                    return false;
                }
            }
            return true;
        }
    }
}