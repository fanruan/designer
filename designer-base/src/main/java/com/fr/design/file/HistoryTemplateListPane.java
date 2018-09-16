package com.fr.design.file;

/**
 * 历史模板缓存
 *
 * 为可能存在的插件做兼容处理
 *
 * @see HistoryTemplateListCache
 * @deprecated use HistoryTemplateListCache instead
 */
@Deprecated
public class HistoryTemplateListPane {
    public static HistoryTemplateListCache getInstance() {
        return HistoryTemplateListCache.getInstance();
    }
}
