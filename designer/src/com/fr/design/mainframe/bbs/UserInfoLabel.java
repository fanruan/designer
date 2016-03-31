/**
 * 
 */
package com.fr.design.mainframe.bbs;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.extra.LoginCheckContext;
import com.fr.design.extra.LoginCheckListener;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.ComparatorUtils;
import com.fr.general.DateUtils;
import com.fr.general.http.HttpClient;
import com.fr.stable.EncodeConstants;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * @author neil
 *
 * @date: 2015-3-4-上午9:05:52
 */
public class UserInfoLabel extends UILabel{
	
	//默认查询消息时间, 30s
	private static final long CHECK_MESSAGE_TIME = 30 * 1000L;
    //默认论坛检测到更新后的弹出延迟时间
	private static final long DELAY_TIME = 2 * 1000L;
	private static final String MESSAGE_KEY = "messageCount";
	
	private static final int MIN_MESSAGE_COUNT = 1;
	

	//用户名
	private String userName;
	//消息条数
	private int messageCount;
	
	private UserInfoPane userInfoPane;
	private BBSLoginDialog bbsLoginDialog;
	
	public UserInfoPane getUserInfoPane() {
		return userInfoPane;
	}

	public void setUserInfoPane(UserInfoPane userInfoPane) {
		this.userInfoPane = userInfoPane;
	}
	
	public BBSLoginDialog getBbsLoginDialog() {
		return bbsLoginDialog;
	}

	public void setBbsLoginDialog(BBSLoginDialog bbsLoginDialog) {
		this.bbsLoginDialog = bbsLoginDialog;
	}

	public UserInfoLabel(UserInfoPane userInfoPane) {
		this.userInfoPane = userInfoPane;
		
		String userName = DesignerEnvManager.getEnvManager().getBBSName();
		this.addMouseListener(userInfoAdapter);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setText(userName);
		setUserName(userName);
		LoginCheckContext.addLoginCheckListener(new LoginCheckListener() {
			@Override
			public void loginChecked() {
				if (bbsLoginDialog == null) {
					bbsLoginDialog = new BBSLoginDialog(DesignerContext.getDesignerFrame(), UserInfoLabel.this);
				}
				bbsLoginDialog.clearLoginInformation();
				bbsLoginDialog.showTipForDownloadPluginWithoutLogin();
				bbsLoginDialog.setModal(true);
				bbsLoginDialog.showWindow();
			}
		});
	}

	/**
	 * showBBSDialog 弹出BBS资讯框
	 */
	public static void showBBSDialog(){
		Thread showBBSThread = new Thread(new Runnable() {

			@Override
			public void run() {
				if(!FRContext.isChineseEnv()){
					return;
				}
				
				String lastBBSNewsTime = DesignerEnvManager.getEnvManager().getLastShowBBSNewsTime();

				try {
                    String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    if (ComparatorUtils.equals(lastBBSNewsTime, today)) {
                        return;
                    }
					Thread.sleep(DELAY_TIME);
				} catch (InterruptedException e) {
					FRContext.getLogger().error(e.getMessage());
				}

                HttpClient hc = new HttpClient(BBSConstants.UPDATE_INFO_URL);
                if (!hc.isServerAlive()){
                    return;
                }

                String res = hc.getResponseText();
                if (res.indexOf(BBSConstants.UPDATE_KEY) == -1){
                    return;
                }

				try {
					BBSDialog bbsLabel = new BBSDialog(DesignerContext.getDesignerFrame());
					bbsLabel.showWindow(BBSConstants.UPDATE_INFO_URL);
					DesignerEnvManager.getEnvManager().setLastShowBBSNewsTime(DateUtils.DATEFORMAT2.format(new Date()));
				} catch (Throwable e) {

				}

			}
		});
		showBBSThread.start();
	}

	private void sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			FRContext.getLogger().error(e.getMessage());
		}
	}

	public String getUserName() {
		return userName;
	}

	/**
	 * 重置当前用户名
	 *
	 */
	public void resetUserName(){
		this.userName = StringUtils.EMPTY;
	}

	public void setUserName(String userName) {
		if(StringUtils.isEmpty(userName)){
			return;
		}

		if(StringUtils.isEmpty(this.userName)){
			updateMessageCount();
		}

		//往designerenvmanger里写一下
		DesignerEnvManager.getEnvManager().setBBSName(userName);
		this.userName = userName;
	}

	private void updateMessageCount(){
		//启动获取消息更新的线程
		//登陆状态, 根据存起来的用户名密码, 每1分钟发起一次请求, 更新消息条数.
		Thread updateMessageThread = new Thread(new Runnable() {

			@Override
			public void run() {
                sleep(CHECK_MESSAGE_TIME);
				//从env中获取username, 因为如果注销的话, env的里username会被清空.
				while(StringUtils.isNotEmpty(DesignerEnvManager.getEnvManager().getBBSName())){
					HashMap<String, String> para = new HashMap<String, String>();
					para.put("username", encode(encode(userName)));
					HttpClient getMessage = new HttpClient(BBSConstants.GET_MESSAGE_URL, para);
					getMessage.asGet();
					if(getMessage.isServerAlive()){
						try {
                            String res = getMessage.getResponseText();
                            if (StringUtils.isNotEmpty(res)) {
                                setMessageCount(Integer.parseInt(res));
                            }
						} catch (Exception e) {
						}
					}

					sleep(CHECK_MESSAGE_TIME);
				}
			}
		});
		updateMessageThread.start();
	}

    private String encode(String str){
        try {
            return URLEncoder.encode(str, EncodeConstants.ENCODING_UTF_8);
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }

	public int getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(int messageCount) {
		// 当只有一条消息时，阅读之后，消息面板重置为只含用户名的状态
		if(this.messageCount == MIN_MESSAGE_COUNT && messageCount < MIN_MESSAGE_COUNT){
			this.setText(this.userName);
			return;
		}
		if(this.messageCount == messageCount || messageCount < MIN_MESSAGE_COUNT){
			return;
		}

		this.messageCount = messageCount;
		StringBuilder sb = new StringBuilder();
		//内容eg: aaa(11)
		sb.append(StringUtils.BLANK).append(this.userName)
		  .append("(").append(this.messageCount)
		  .append(")").append(StringUtils.BLANK);

		//更新面板Text
		this.setText(sb.toString());
	}

	private MouseAdapter userInfoAdapter = new MouseAdapter() {

		public void mouseEntered(MouseEvent e) {
			UserInfoLabel.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		};

		@Override
		public void mouseClicked(MouseEvent e) {
			if(StringUtils.isNotEmpty(userName)){
                try {
                    Desktop.getDesktop().browse(new URI(BBSConstants.DEFAULT_URL));
                } catch (Exception exp) {

                }
                return;
			}
            if(bbsLoginDialog == null){
                bbsLoginDialog = new BBSLoginDialog(DesignerContext.getDesignerFrame(),UserInfoLabel.this);
            }
            bbsLoginDialog.clearLoginInformation();
            bbsLoginDialog.setModal(true);
            bbsLoginDialog.showWindow();
		}

	};

}