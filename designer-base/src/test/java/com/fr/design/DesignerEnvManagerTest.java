package com.fr.design;

import com.fr.locale.InterProviderFactory;
import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Locale;

public class DesignerEnvManagerTest extends TestCase{

    public void testGetLocale() {
        DesignerEnvManager envManager = new DesignerEnvManager();
        assertEquals("默认值", envManager.getLocale(), Locale.CHINA);

        HashSet<Locale> result = new HashSet<>();
        Locale[] locales = envManager.supportLocale();
        int len = locales.length;
        assertEquals(len,  InterProviderFactory.getProvider().getSupportLocaleMap().size());
        for (int i = 0; i < len; i++) {
            envManager.setLanguage(i + 1);
            Locale locale = envManager.getLocale();
            result.add(locale);
        }
        assertEquals("每个int都有对应的locale", result.size(), len);

        envManager.setLanguage(998);
        assertEquals("上边界", envManager.getLocale(), Locale.CHINA);

        envManager.setLanguage(-998);
        assertEquals("下边界", envManager.getLocale(), Locale.CHINA);

    }
}
