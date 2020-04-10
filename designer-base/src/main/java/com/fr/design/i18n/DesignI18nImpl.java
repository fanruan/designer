package com.fr.design.i18n;

import com.fr.design.DesignerEnvManager;
import com.fr.general.GeneralContext;
import com.fr.general.log.MessageFormatter;
import com.fr.locale.DesignI18nProvider;
import com.fr.locale.InterProviderFactory;
import com.fr.locale.LocaleBundle;
import com.fr.locale.LocaleFiles;
import com.fr.locale.LocaleManager;
import com.fr.locale.impl.FineLocaleManager;

import java.util.List;
import java.util.Locale;

public class DesignI18nImpl implements DesignI18nProvider {

    static {
        // GeneralContext上下文 存储本次启动的语言环境 直接使用DesignerEnvManager 会在设置语言环境后 不重启 立即生效 存在问题
        GeneralContext.setLocale(DesignerEnvManager.getEnvManager().getLanguage());
    }

    private static DesignI18nImpl instance = new DesignI18nImpl();

    public static DesignI18nImpl getInstance() {
        return instance;
    }

    private LocaleManager localeManager = FineLocaleManager.create();

    private DesignI18nImpl() {
        addResource("com/fr/design/i18n/main");
        InterProviderFactory.registerDesignI18nProvider(this);
    }

    @Override
    public String i18nText(String key) {
        return localeManager.getLocalBundle(GeneralContext.getLocale()).getText(localeManager, key);
    }

    @Override
    public String i18nText(String key, Object... args) {
        String format = this.i18nText(key);
        MessageFormatter.FormattingTuple tuple = MessageFormatter.arrayFormat(format, args);
        return localeManager.getLocalBundle(GeneralContext.getLocale()).getText(localeManager, tuple.getMessage());
    }

    @Override
    public void addResource(String path) {
        localeManager.addResource(path);
    }

    @Override
    public void removeResource(String path) {
        localeManager.removeResource(path);
    }

    @Override
    public LocaleBundle getLocalBundle(Locale locale) {
        return localeManager.getLocalBundle(locale);
    }

    @Override
    public List<LocaleFiles> getReverseList() {
        return localeManager.getReverseList();
    }

    @Override
    public List<LocaleFiles> getList() {
        return localeManager.getList();
    }

    @Override
    public void clear() {
        localeManager.clear();
    }
}
