package com.fr.design.actions.help;

import com.fr.general.GeneralContext;
import com.fr.stable.ProductConstants;
import junit.framework.TestCase;

import java.util.Locale;
import java.util.UUID;

public class TutorialActionTest extends TestCase {

    public void testCreateKey() {
        TutorialAction action = new TutorialAction();
        GeneralContext.setLocale(Locale.US);
        String enKey = action.createDocKey();
        assertTrue(enKey.contains(Locale.US.toString()));
        assertTrue(enKey.contains(ProductConstants.MAIN_VERSION));

        GeneralContext.setLocale(Locale.CHINA);
        String zhKey = action.createDocKey();
        assertTrue(zhKey.contains(Locale.CHINA.toString()));

        Locale pt = new Locale("pt", "PT");
        GeneralContext.setLocale(pt);
        String ptKey = action.createDocKey();
        assertTrue(ptKey.contains(pt.toString()));
        GeneralContext.setLocale(Locale.CHINA);
    }

    public void testServerOnline() {
        TutorialAction action = new TutorialAction();
        assertFalse(action.isServerOnline(null));
        assertFalse(action.isServerOnline("中文"));
        assertTrue("百度暂时不会挂", action.isServerOnline("http://www.baidu.com"));
        assertTrue("https测试", action.isServerOnline("https://www.baidu.com"));
        assertFalse("连上是正常的", action.isServerOnline("https://www.fine-" + UUID.randomUUID() + ".com"));
    }
}
