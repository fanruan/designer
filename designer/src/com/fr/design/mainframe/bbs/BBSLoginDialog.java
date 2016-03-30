package com.fr.design.mainframe.bbs;

import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.UIDialog;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.ActionLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ipasswordfield.UIPassWordField;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.general.http.HttpClient;
import com.fr.stable.StringUtils;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.HttpURLConnection;
import java.net.URI;

/**
 * @author richie
 * @date 2015-04-02
 * @since 8.0
 */
public class BBSLoginDialog extends UIDialog {

    private static final int DIALOG_WIDTH = 400;
    private static final int DIALOG_HEIGHT = 200;
    private static final String URL = "http://bbs.finereport.com/member.php?mod=logging&action=login&loginsubmit=yes&infloat=yes&lssubmit=yes&inajax=1";
    private static final String PASSWORD_RESET_URL = "http://bbs.finereport.com/portal.php?mod=topic&topicid=2";
    private static final String REGISTER_URL =  "http://bbs.finereport.com/member.php?mod=register";
    private static final String LOGIN_SUCCESS_FLAG = "http://bbs.finereport.com";
    private static final Font DEFAULT_FONT = FRFont.getInstance(FRFont.DEFAULT_FONTNAME, 0, 14);
    private static final String TEST_CONNECTION_URL = "http://bbs.finereport.com/member.php?mod=logging&action=login&loginsubmit=yes&infloat=yes&lssubmit=yes&inajax=1&username=test&password=123456";
    private static final int TIME_OUT = 10000;
    
    private static final int BUTTON_WIDTH = 90;
    private static final int V_GAP = 20;
    private static final int BUTTON_H_GAP = 155;
    private static final int NORTH_PANE_HEIGHT = 24;
    private static final int FIELD_HEIGHT = 26;
    private static final int FIELD_WIDTH = 204;
    private static final int FLOWLAYOUT_H_GAP = 15;


    private UILabel userLabel;
    private UILabel passLabel;
    private UITextField nameField;
	private UIPassWordField passField;
    private UIButton loginButton;
    private UILabel tipLabel;
    private BoxCenterAligmentPane passwordReset;
    private BoxCenterAligmentPane registerLabel;
    
    private KeyListener keyListener = new KeyAdapter() {
    	public void keyPressed(KeyEvent e){
    		int code = e.getKeyCode();
    		if(KeyEvent.VK_ESCAPE == code){
    			BBSLoginDialog.this.setVisible(false);
    			return;
    		}
    		
    		if(KeyEvent.VK_ENTER == code){
    			login();
    		}
    	}
	};
    
    // 用户登录状态label
    private UserInfoLabel userInfoLabel;
    

	public UILabel getTipLabel() {
		return tipLabel;
	}

	public void setTipLabel(UILabel tipLabel) {
		this.tipLabel = tipLabel;
	}


	/**
	 * 构造函数
	 * @param parent 父窗口
	 * @param userInfoLabel 登录状态label
	 */
	public BBSLoginDialog(Frame parent,UserInfoLabel userInfoLabel) {
        super(parent);
        JPanel panel = (JPanel) getContentPane();
        initComponents(panel);
        this.userInfoLabel = userInfoLabel;
        setSize(new Dimension(DIALOG_WIDTH,DIALOG_HEIGHT));
    }

    private void initComponents(JPanel contentPane) {
        this.setTitle(Inter.getLocText("FR-Designer-BBSLogin_Login-Title"));
        tipLabel = new UILabel(Inter.getLocText("FR-Designer-BBSLogin_Login-Failure-Tip"));
        userLabel = new UILabel(Inter.getLocText("FR-Designer-BBSLogin_Account")+":");
        passLabel = new UILabel(Inter.getLocText("FR-Designer-BBSLogin_Password")+":");
        nameField = new UITextField();
        passField = new UIPassWordField();
        loginButton = new UIButton(Inter.getLocText("FR-Designer-BBSLogin_Login"));
        passwordReset = getURLActionLabel(PASSWORD_RESET_URL);
        registerLabel = getURLActionLabel(REGISTER_URL);
        loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				login();
			}
		});
        
        nameField.addKeyListener(keyListener);
        passField.addKeyListener(keyListener);
        loginButton.addKeyListener(keyListener);
        
        userLabel.setFont(DEFAULT_FONT);
        passLabel.setFont(DEFAULT_FONT);
        this.tipLabel.setVisible(false);
        
        contentPane.setLayout(new BorderLayout());
        
        JPanel mainPane = new JPanel();
        mainPane.setLayout(new BorderLayout(0,V_GAP));
        
        initNorthPane(mainPane);
        
        initCenterPane(mainPane);
        
        initSouthPane(mainPane);

        contentPane.add(mainPane,BorderLayout.NORTH);
        
        this.setResizable(false);
    }
    
    
    private void login(){
    	if(nameField.getText().isEmpty()){
    		tipForUsernameEmpty();
    		nameField.requestFocus();
    		return;
    	}
    	if(String.valueOf(passField.getPassword()).isEmpty()){
    		tipForPasswordEmpty();
    		passField.requestFocus();
    		return;
    	}
		if(!testConnection()){
			connectionFailue();
			return;
		}
		if(login(nameField.getText(),String.valueOf(passField.getPassword()))){
			loginSuccess();
		}else{
			loginFailure();
		}
	
    }
    
    private boolean testConnection(){
		HttpClient client = new HttpClient(TEST_CONNECTION_URL);
		return client.isServerAlive();
    }
    
    private void initNorthPane(JPanel mainPane){
        JPanel northPane = new JPanel();
        northPane.setPreferredSize(new Dimension(DIALOG_WIDTH,NORTH_PANE_HEIGHT));
        northPane.add(tipLabel);
        mainPane.add(northPane,BorderLayout.NORTH);
    }
    
    private void initCenterPane(JPanel mainPane){
        JPanel loginPane = new JPanel();
        loginPane.setLayout(new GridLayout(2,1,0,V_GAP));

        JPanel namePane = new JPanel();
        namePane.setLayout(new FlowLayout(FlowLayout.RIGHT,FLOWLAYOUT_H_GAP,0));
        namePane.add(userLabel);
        nameField.setPreferredSize(new Dimension(FIELD_WIDTH,FIELD_HEIGHT));
        namePane.add(nameField);
        namePane.add(passwordReset);
        loginPane.add(namePane);
        
        JPanel passPane = new JPanel();
        passPane.setLayout(new FlowLayout(FlowLayout.RIGHT,FLOWLAYOUT_H_GAP,0));
        passPane.add(passLabel);
        passPane.add(passField);
        passField.setPreferredSize(new Dimension(FIELD_WIDTH,FIELD_HEIGHT));
        passPane.add(registerLabel);
        loginPane.add(passPane);
        
        mainPane.add(loginPane,BorderLayout.CENTER);
    }
    
    private void initSouthPane(JPanel mainPane){
        JPanel southPane = new JPanel();
        southPane.setLayout(new FlowLayout(FlowLayout.RIGHT,BUTTON_H_GAP,0));
        loginButton.setPreferredSize(new Dimension(BUTTON_WIDTH,FIELD_HEIGHT));
        loginButton.setFont(DEFAULT_FONT);
        southPane.add(loginButton);
        mainPane.add(southPane,BorderLayout.SOUTH);
    }
    
    // 登录成功
    private void loginSuccess(){
		DesignerEnvManager.getEnvManager().setBBSPassword(String.valueOf(passField.getPassword()));
		userInfoLabel.setUserName(nameField.getText());
		userInfoLabel.getUserInfoPane().markSignIn(nameField.getText());
		userInfoLabel.getUserInfoPane().getSwitchAccountLabel().setVisible(true);
		BBSLoginDialog.this.setVisible(false);
    }
    
    // 登录失败
    private void loginFailure(){
		setLoginFailureTxt(Inter.getLocText("FR-Designer-BBSLogin_Login-Failure-Tip"));
    }
    
    private void tipForUsernameEmpty(){
    	setLoginFailureTxt(Inter.getLocText("FR-Designer-BBSLogin_Username-Empty-Tip"));
    }

    public void showTipForDownloadPluginWithoutLogin() {
        setLoginFailureTxt(Inter.getLocText("FR-Designer-BBSLogin_Download-Unlogin-Tip"));
    }
    
    private void tipForPasswordEmpty(){
    	setLoginFailureTxt(Inter.getLocText("FR-Designer-BBSLogin_Password-Empty-Tip"));
    }
    
    private void setLoginFailureTxt(String errorTxt){
		tipLabel.setText(errorTxt);
		tipLabel.setForeground(Color.RED);
		tipLabel.repaint();
		tipLabel.setVisible(true);
    }
    
    // 连接失败
    private void connectionFailue(){
		setLoginFailureTxt(Inter.getLocText("FR-Designer-BBSLogin_Connection-Failure"));
    }
    
    /**
     * 清楚登录信息
     */
    public void clearLoginInformation(){
    	tipLabel.setText(StringUtils.EMPTY);
    	nameField.setText(StringUtils.EMPTY);
    	passField.setText(StringUtils.EMPTY);
    }

	/**
     * 显示登录窗口
     */
    public void showWindow(){
        GUICoreUtils.centerWindow(this);
        setVisible(true);
    }

    /**
     * 无
     */
    @Override
    public void checkValid() throws Exception {

    }
    
    /**
     * 是否登陆成功
     * @param username 用户名
     * @param password 密码
     * @return 同上
     */
    public static boolean login(String username,String password){
		String url = URL + "&username=" + username + "&password=" + password;
		HttpClient client = new HttpClient(url);
		client.setTimeout(TIME_OUT);
		if(client.getResponseCodeNoException() == HttpURLConnection.HTTP_OK){
			try {
				String res = client.getResponseText("GBK");
				if(res.contains(LOGIN_SUCCESS_FLAG)){
					return true;
				}
			} catch (Exception e) {
				FRLogger.getLogger().error(e.getMessage());
			}
		}
    	return false;
    }
    
    private BoxCenterAligmentPane getURLActionLabel(final String url){
    	ActionLabel actionLabel = new ActionLabel(url);
    	if(ComparatorUtils.equals(url, PASSWORD_RESET_URL)){
    		actionLabel.setText(Inter.getLocText("FR-Designer-BBSLogin_Forgot-Password"));
    	}else{
    		actionLabel.setText(Inter.getLocText("FR-Designer-BBSLogin_Register-Account"));
    	}
    	
    	actionLabel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception exp) {

                }
            }
        });
        
        return new BoxCenterAligmentPane(actionLabel);
    }
    
    class BoxCenterAligmentPane extends JPanel {

        private UILabel textLabel;
    	
        public BoxCenterAligmentPane(String text) {
            this(new UILabel(text));
        }

        public BoxCenterAligmentPane(UILabel label) {
            this.setLayout(FRGUIPaneFactory.createBorderLayout());

            JPanel centerPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            this.add(centerPane, BorderLayout.CENTER);

            this.textLabel = label;
            centerPane.add(textLabel);
        }

        public void setFont(Font font) {
            super.setFont(font);

            if (textLabel != null) {
                textLabel.setFont(font);
            }
        }
    }
}