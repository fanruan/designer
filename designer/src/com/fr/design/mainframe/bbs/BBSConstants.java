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