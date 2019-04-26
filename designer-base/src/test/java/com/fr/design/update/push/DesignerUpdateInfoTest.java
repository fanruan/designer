package com.fr.design.update.push;

import com.fr.json.JSONObject;
import com.fr.stable.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.InvalidParameterException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by plough on 2019/4/9.
 */
public class DesignerUpdateInfoTest {
    private static final String CURRENT_VERSION = "2018.09.03.xx";
    private static final String LATEST_VERSION = "2019.04.03.yy";
    private static final String LAST_IGNORED_VERSION = "2019.02.03.yy";
    private static final String PUSH_VERSION = "2019.01.03.21.11";
    private static final String PUSH_CONTENT = "the update desc content";
    private static final String PUSH_BACKGROUND = "http://image.fr.com/123.jpg";
    private static final String PUSH_MORE = "http://help.finereport.com/xxx";
    private DesignerUpdateInfo updateInfo;

    @Before
    public void setUp() {
        JSONObject pushData = JSONObject.create();

        pushData.put("version", PUSH_VERSION);
        pushData.put("content", PUSH_CONTENT);
        pushData.put("background", PUSH_BACKGROUND);
        pushData.put("more", PUSH_MORE);

        updateInfo = new DesignerUpdateInfo(CURRENT_VERSION, LATEST_VERSION, LAST_IGNORED_VERSION, pushData);
    }

    @Test
    public void testGetters() {
        assertEquals(CURRENT_VERSION, updateInfo.getCurrentVersion());
        assertEquals(LATEST_VERSION, updateInfo.getLatestVersion());
        assertEquals(LAST_IGNORED_VERSION, updateInfo.getLastIgnoredVersion());
        assertEquals(PUSH_VERSION, updateInfo.getPushVersion());
        assertEquals(PUSH_CONTENT, updateInfo.getPushContent());
        assertEquals(PUSH_BACKGROUND, updateInfo.getBackgroundUrl());
        assertEquals(PUSH_MORE, updateInfo.getMoreInfoUrl());
    }

    @Test
    public void testHasNewPushVersion() {
        // （1）最近被跳过的维护版本号 X0；
        // （2）本地版本号 Y；
        // （3）最新的推送版本号 X；
        // （4）最新的版本号 Z
        // 必须满足：Y < X <= Z && X > X0，才返回 true

        // 1 true
        assertTrue(hasNewVersion("2019.01.03.xx", "2018.05.03.xx", "2019.04.03.yy", "2018.05.03.xx"));
        assertTrue(hasNewVersion("2019.01.03.xx", "2018.05.03.xx", "2019.04.03.yy", null));
        assertTrue(hasNewVersion("2019.01.03.xx", "2018.05.03.xx", "2019.04.03.yy", StringUtils.EMPTY));

        // 2 false
        // 2.1 X <= Y && X > X0
        assertFalse(hasNewVersion("2019.01.03.xx", "2019.03.03.xx", "2019.04.03.yy", "2018.05.03.xx"));
        assertFalse(hasNewVersion("2019.03.03.xx", "2019.03.03.xx", "2019.04.03.yy", "2018.05.03.xx"));

        // 2.2 X > Z && X > X0
        assertFalse(hasNewVersion("2020.01.03.xx", "2019.03.03.xx", "2019.04.03.yy", "2018.05.03.xx"));

        // 2.3 Y < X <= Z && X <= X0
        assertFalse(hasNewVersion("2019.01.03.xx", "2018.05.03.xx", "2019.04.03.yy", "2019.02.03.xx"));
        assertFalse(hasNewVersion("2019.01.03.xx", "2018.05.03.xx", "2019.04.03.yy", "2019.01.03.xx"));
    }


    private boolean hasNewVersion(String X, String Y, String Z, String X0) {
        JSONObject pushData = JSONObject.create();
        pushData.put("version", X);
        pushData.put("content", PUSH_CONTENT);
        pushData.put("background", PUSH_BACKGROUND);
        pushData.put("more", PUSH_MORE);
        DesignerUpdateInfo updateInfo = new DesignerUpdateInfo(Y, Z, X0, pushData);
        return updateInfo.hasNewPushVersion();
    }

    @Test
    public void testParameterValidation() {
        try {
            DesignerUpdateInfo updateInfo = new DesignerUpdateInfo(null, null, null, new JSONObject());
            Assert.fail("should not reach here!");
        } catch (InvalidParameterException e) {
            // do nothing
        }
    }
}
