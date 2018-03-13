/**
 *
 */
package com.fr.design.mainframe.bbs;

import com.fr.general.IOUtils;
import com.fr.general.SiteCenter;
import com.fr.stable.StringUtils;

import java.util.Properties;

/**
 * @author neil
 * @date: 2015-3-10-上午9:50:13
 */
public class BBSConstants {

    //判断是否更新的关键字
    public static final String UPDATE_KEY = loadAttribute("UPDATE_KEY", "newIsPopup");


    private static final String GUEST_KEY = "USER";
    private static final String GUEST_KEY_ONLINE = "guest.user";
    private static final String LINK_KEY = "LINK";
    private static final String LINK_KEY_ONLINE = "guest.link";
    private static final String JOIN_LINK_KEY = "JOIN";
    private static final String JOIN_LINK_KEY_ONLINE = "guest.join";

    private static Properties PROP = null;

    public static String[] getAllGuest() {
        return loadArrayOnline(GUEST_KEY_ONLINE, loadAttribute(GUEST_KEY));
    }

    public static String[] getAllLink() {
        return loadArrayOnline(LINK_KEY_ONLINE, loadAttribute(LINK_KEY));
    }

    public static String getHowToJoinLink() {
        return loadAttributeOnline(JOIN_LINK_KEY_ONLINE, loadAttribute(JOIN_LINK_KEY));
    }

    private static String loadAttribute(String key) {
        return loadAttribute(key, StringUtils.EMPTY);
    }

    /**
     * 在线加载数组形式的键值，值使用|分割
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    private static String[] loadArrayOnline(String key, String defaultValue) {
        String[] allGuests = new String[0];
        String guest = SiteCenter.getInstance().acquireUrlByKind(key, defaultValue);
        if (StringUtils.isNotEmpty(guest)) {
            allGuests = guest.split("\\|");
        }
        return allGuests;
    }

    /**
     * 在线加载键值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    private static String loadAttributeOnline(String key, String defaultValue) {
        String value = SiteCenter.getInstance().acquireUrlByKind(key, defaultValue);
        if (StringUtils.isEmpty(value)) {
            return StringUtils.EMPTY;
        }
        return value;
    }

    //如果要定制, 直接改bbs.properties就行了
    private static String loadAttribute(String key, String defaultValue) {
        if (PROP == null) {
            PROP = new Properties();
            try {
                PROP.load(IOUtils.getResourceAsStream("/com/fr/design/mainframe/bbs/bbs.properties", BBSConstants.class));
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