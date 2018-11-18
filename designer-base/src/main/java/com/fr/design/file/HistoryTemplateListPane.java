package com.fr.design.file;

import com.fr.base.chart.chartdata.CallbackEvent;
import com.fr.design.mainframe.JTemplate;
import com.fr.file.FILE;
import com.fr.file.FileNodeFILE;

import java.util.List;

/**
 * 历史模板缓存
 * <p>
 * 为可能存在的插件做兼容处理
 *
 * @see HistoryTemplateListCache
 * @deprecated use HistoryTemplateListCache instead
 */
@Deprecated
public class HistoryTemplateListPane implements CallbackEvent {


    private static volatile HistoryTemplateListPane THIS;

    public static HistoryTemplateListPane getInstance() {
        if (THIS == null) {
            synchronized (HistoryTemplateListPane.class) {
                if (THIS == null) {
                    THIS = new HistoryTemplateListPane();
                }
            }
        }
        return THIS;
    }

    private static HistoryTemplateListCache instead() {
        return HistoryTemplateListCache.getInstance();
    }


    /**
     * 关闭选择的文件
     *
     * @param selected 选择的
     */
    public void closeSelectedReport(JTemplate<?, ?> selected) {
        instead().closeSelectedReport(selected);
    }

    /**
     * 临时关闭选择的文件
     *
     * @param selected 选择的
     */
    public void closeVirtualSelectedReport(JTemplate<?, ?> selected) {
        instead().closeVirtualSelectedReport(selected);
    }


    public JTemplate<?, ?> getCurrentEditingTemplate() {
        return instead().getCurrentEditingTemplate();
    }

    public void setCurrentEditingTemplate(JTemplate<?, ?> jt) {
        instead().setCurrentEditingTemplate(jt);

    }

    /**
     * 添加历史记录
     */
    public void addHistory() {
        instead().addHistory();
    }


    public List<JTemplate<?, ?>> getHistoryList() {
        return instead().getHistoryList();
    }


    /**
     * 清空历史记录
     */
    public void removeAllHistory() {
        instead().removeAllHistory();
    }

    public int getHistoryCount() {
        return instead().getHistoryCount();
    }


    public JTemplate<?, ?> get(int index) {
        return instead().get(index);
    }


    public JTemplate<?, ?> getTemplate(int index) {
        return instead().getTemplate(index);
    }

    /**
     * 获取模板的index
     *
     * @param jt 模板
     * @return 位置
     */
    public int contains(JTemplate<?, ?> jt) {
        return instead().contains(jt);
    }


    /**
     * 判断是否打开过该模板
     *
     * @param filename 文件名
     * @return 文件位置
     */
    public int contains(String filename) {
        return instead().contains(filename);
    }

    /**
     * 是否是当前编辑的文件
     *
     * @param filename 文件名
     * @return 是则返回TRUE
     */
    public boolean isCurrentEditingFile(String filename) {
        return instead().isCurrentEditingFile(filename);
    }


    @Override
    public void callback() {
        instead().callback();
    }

    /**
     * 打开new模板的同时关闭old模板,优先关已保存的、先打开的
     */
    public void closeOverLineTemplate() {
        instead().closeOverLineTemplate();
    }


    public void deleteFile(FileNodeFILE file) {
        instead().deleteFile(file);
    }

    public boolean rename(FILE tplFile, String from, String to) {
        return instead().rename(tplFile, from, to);
    }
}
