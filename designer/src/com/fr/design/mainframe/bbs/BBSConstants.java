/**
 * 
 */
package com.fr.design.mainframe.bbs;

import com.fr.stable.StringUtils;

import java.util.Properties;

/**
 * @author neil
 *
 * @date: 2015-3-10-上午9:50:13
 */
public class BBSConstants {

	//获取当前登陆用户未读取消息条数
	public static final String GET_MESSAGE_URL = loadAttribute("GET_MESSAGE_URL", "http://feedback.finedevelop.com:3000/bbs/message/count");
	//默认打开的论坛窗口
	public static final String DEFAULT_URL = loadAttribute("DEFAULT_URL", "http://bbs.finereport.com/home.php?mod=space&do=pm");
	//默认模板分享的url
	public static final String SHARE_URL = loadAttribute("SHARE_URL", "http://bbs.finereport.com/");
	//收集设计器的信息url
	public static final String COLLECT_URL = loadAttribute("COLLECT_URL", "http://bbs.finereport.com/");
	//在线验证激活码
	public static final String VERIFY_URL = loadAttribute("VERIFY_URL", "http://bbs.finereport.com/");
    //获取论坛更新信息, 判断是否需要弹窗
    public static final String UPDATE_INFO_URL = loadAttribute("UPDATE_INFO_URL", "http://bbs.finereport.com/");
    //论坛手机版
    public static final String BBS_MOBILE_MOD = loadAttribute("BBS_MOBILE_MOD", "http://bbs.finereport.com/forum.php?mobile=1");
    //判断是否更新的关键字
    public static final String UPDATE_KEY = loadAttribute("UPDATE_KEY", "newIsPopup");
   

	private static final String GUEST_KEY = "USER";
	private static final String LINK_KEY = "LINK";
	private static final int GUEST_NUM = 5;
	
	//用户名信息数组
	public static final String[] ALL_GUEST = loadAllGuestsInfo(GUEST_KEY);
	//用户论坛链接信息
	public static final String[] ALL_LINK = loadAllGuestsInfo(LINK_KEY);
	
	private static Properties PROP = null;
	
	//加载所有用户的信息, 用户名, 论坛连接
	private static String[] loadAllGuestsInfo(String key){
		String[] allGuests = new String[GUEST_NUM];
		for (int i = 0; i < GUEST_NUM; i++) {
			allGuests[i] = loadAttribute(key + i, StringUtils.EMPTY);
		}
		
		return allGuests;
	}
	//如果要定制, 直接改bbs.properties就行了
	private static String loadAttribute(String key, String defaultValue) {
		if (PROP == null) {
			PROP = new Properties();
			try {
				PROP.load(BBSConstants.class.getResourceAsStream("/com/fr/design/mainframe/bbs/bbs.properties"));
			} catch (Exception e) {
			}
		}

		String p = PROP.getProperty(key);
		if (StringUtils.isEmpty(p)) {
			p = defaultValue;
		}
		return p;
	}
	
}