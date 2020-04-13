/**
 *
 */
package com.fr.design.mainframe.bbs;

import com.fr.general.IOUtils;
import com.fr.general.CloudCenter;
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

    private static Properties PROP = null;

    /**
     * 获取所有的感谢对象，无法获取在线使用默认
     * @return 感谢对象的数组
     */
    public static String[] getAllGuest() {
        return loadAllGuestsInfoOnline(GUEST_KEY_ONLINE, loadAllGuestsInfo(GUEST_KEY));
    }

    /**
     * 获取所有的链接，无法获取在线使用默认
     * @return 链接的数组
     */
    public static String[] getAllLink() {
        return loadAllGuestsInfoOnline(LINK_KEY_ONLINE, loadAllGuestsInfo(LINK_KEY));
    }

    /**
     * 获取所有的感谢对象，手动选择策略
     * @return 感谢对象的数组
     */
    public static String[] getAllGuestManual(boolean isOnline) {
        String guest;
        if (isOnline) {
            guest = CloudCenter.getInstance().acquireUrlByKind(GUEST_KEY_ONLINE, StringUtils.EMPTY);
        } else {
            guest = loadAllGuestsInfo(GUEST_KEY);
        }
        if (StringUtils.isNotEmpty(guest)) {
            return guest.split("\\|");
        }
        return new String[0];
    }

    private static String loadAllGuestsInfo(String key) {
        return loadAttribute(key, StringUtils.EMPTY);
    }

    private static String[] loadAllGuestsInfoOnline(String key, String defaultValue) {
        String guest = CloudCenter.getInstance().acquireConf(key, defaultValue);
        if (StringUtils.isNotEmpty(guest)) {
            return guest.split("\\|");
        }
        return new String[0];
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