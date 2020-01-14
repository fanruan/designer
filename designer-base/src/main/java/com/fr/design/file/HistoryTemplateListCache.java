package com.fr.design.file;

import com.fr.base.chart.chartdata.CallbackEvent;
import com.fr.base.io.BaseBook;
import com.fr.design.DesignerEnvManager;
import com.fr.design.base.mode.DesignModeContext;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrameFileDealerPane;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.JTemplateFactory;
import com.fr.design.mainframe.JVirtualTemplate;
import com.fr.design.module.DesignModuleFactory;
import com.fr.file.FILE;
import com.fr.file.FileNodeFILE;
import com.fr.file.StashedFILE;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.CoreConstants;
import com.fr.stable.StringUtils;
import com.fr.third.org.apache.commons.io.FilenameUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * 历史模板缓存
 *
 * @see HistoryTemplateListPane
 */
public class HistoryTemplateListCache implements CallbackEvent {

    //最大保存内存中面板数,为0时关闭优化内存
    private static final int DEAD_LINE = DesignerEnvManager.getEnvManager().getCachingTemplateLimit();
    private List<JTemplate<?, ?>> historyList;
    private JTemplate<?, ?> editingTemplate;

    public static HistoryTemplateListCache getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final HistoryTemplateListCache INSTANCE = new HistoryTemplateListCache();
    }

    private HistoryTemplateListCache() {
        historyList = new ArrayList<>();
    }

    /**
     * 关闭选择的文件
     *
     * @param selected 选择的
     */
    public void closeSelectedReport(JTemplate<?, ?> selected) {
        DesignModuleFactory.clearChartPropertyPane();
        DesignTableDataManager.closeTemplate(selected);
        //直接关闭模板的时候(当且仅当设计器tab上只剩一个模板)退出权限编辑
        if (DesignModeContext.isAuthorityEditing() && historyList.size() <= 1) {
            DesignerContext.getDesignerFrame().closeAuthorityEditing();
        }
        if (contains(selected) == -1) {
            return;
        }
        selected.fireJTemplateClosed();
        selected.stopEditing();
        try {
            historyList.remove(contains(selected));
            selected.getEditingFILE().closeTemplate();
            FineLoggerFactory.getLogger().info(Toolkit.i18nText("Fine-Design_Basic_Template_Closed_Warn_Text", selected.getEditingFILE().getName()));
            MutilTempalteTabPane.getInstance().refreshOpenedTemplate(historyList);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }

    }

    /**
     * 临时关闭选择的文件
     *
     * @param selected 选择的
     */
    public void closeVirtualSelectedReport(JTemplate<?, ?> selected) {
        DesignModuleFactory.clearChartPropertyPane();
        DesignTableDataManager.closeTemplate(selected);
        if (contains(selected) == -1) {
            return;
        }
        selected.fireJTemplateClosed();
        selected.stopEditing();
        try {
            selected.getEditingFILE().closeTemplate();
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }


    public JTemplate<?, ?> getCurrentEditingTemplate() {
        return this.editingTemplate;
    }

    /**
     * @param jt jt
     * @see DesignerFrameFileDealerPane#setCurrentEditingTemplate(JTemplate)
     */
    public void setCurrentEditingTemplate(JTemplate<?, ?> jt) {
        this.editingTemplate = jt;
        //如果当前历史面板中没有

        if (contains(jt) == -1) {
            addHistory();
        }
        MutilTempalteTabPane.getInstance().refreshOpenedTemplate(historyList);
        //设置tab栏为当前选中的那一栏
        if (editingTemplate != null) {
            MutilTempalteTabPane.getInstance().setSelectedIndex(contains(jt));
        }

    }

    /**
     * 添加历史记录
     */
    public void addHistory() {
        if (editingTemplate == null) {
            return;
        }
        DesignerEnvManager.getEnvManager().addRecentOpenedFilePath(editingTemplate.getPath());
        historyList.add(editingTemplate);
        closeOverLineTemplate();
    }


    public List<JTemplate<?, ?>> getHistoryList() {
        return historyList;
    }


    /**
     * 清空历史记录
     */
    public void removeAllHistory() {
        historyList.clear();
        this.editingTemplate = null;
    }

    public int getHistoryCount() {
        return historyList.size();
    }


    public JTemplate<?, ?> get(int index) {
        if (index > historyList.size() - 1 || index < 0) {
            return null;
        }
        Collections.reverse(historyList);
        JTemplate<?, ?> select = historyList.get(index);
        Collections.reverse(historyList);
        return select;
    }


    public JTemplate<?, ?> getTemplate(int index) {
        return historyList.get(index);
    }

    /**
     * 获取模板的index
     *
     * @param jt 模板
     * @return 位置
     */
    public int contains(JTemplate<?, ?> jt) {
        for (int i = 0; i < historyList.size(); i++) {
            if (ComparatorUtils.equals(historyList.get(i).getEditingFILE(), jt.getEditingFILE())) {
                return i;
            }
        }
        return -1;
    }


    /**
     * 判断是否打开过该模板
     * 由于切换环境不会关闭模板，可能存在同名的模板，所以该方法不能准确找到所选的模板，
     *
     * @param filename 文件名
     * @return 文件位置
     * @deprecated use HistoryTemplateListCache#contains(com.fr.design.mainframe.JTemplate) instead
     */
    @Deprecated
    public int contains(String filename) {
        for (int i = 0; i < historyList.size(); i++) {
            String historyPath = historyList.get(i).getPath();
            //文件路径和历史路径都是 reportlets/xxx/xxx/xxx/xx.suffix
            if (filename.equals(historyPath)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 是否是当前编辑的文件
     *
     * @param filename 文件名
     * @return 是则返回TRUE
     */
    public boolean isCurrentEditingFile(String filename) {
        String editingFileName = editingTemplate.getPath();
        return ComparatorUtils.equals(filename, editingFileName);
    }

    @Override
    public void callback() {
        getCurrentEditingTemplate().repaint();
    }

    /**
     * 打开new模板的同时关闭old模板,优先关已保存的、先打开的
     */
    public void closeOverLineTemplate() {
        int size = historyList.size();
        int vCount = size - DEAD_LINE;
        if (DEAD_LINE == 0 || vCount <= 0) {
            return;
        }
        for (int i = 0; i < vCount; i++) {
            JTemplate overTemplate = historyList.get(i);

            if (overTemplate.getEditingFILE().exists() && overTemplate.isALLSaved() && overTemplate != editingTemplate) {
                closeVirtualSelectedReport(overTemplate);
                historyList.set(i, new JVirtualTemplate(overTemplate.getEditingFILE()));
            }
        }
        MutilTempalteTabPane.getInstance().refreshOpenedTemplate(historyList);
    }


    public void deleteFile(FileNodeFILE file) {
        boolean isDir = file.isDirectory();

        String suffix = isDir ? CoreConstants.SEPARATOR : StringUtils.EMPTY;

        // path like reportlets/xx/xxx/xxx
        String path = file.getPath() + suffix;

        ListIterator<JTemplate<?, ?>> iterator = historyList.listIterator();

        while (iterator.hasNext()) {
            JTemplate<?, ?> template = iterator.next();
            String tPath = template.getPath();
            if (isDir ? tPath.startsWith(path) : tPath.equals(path)) {
                int size = getHistoryCount();
                iterator.remove();
                int index = iterator.nextIndex();
                if (size == index + 1 && index > 0) {
                    //如果删除的是后一个Tab，则定位到前一个
                    MutilTempalteTabPane.getInstance().setSelectedIndex(index - 1);
                }
            }
        }
        //如果打开过，则删除，实时刷新多tab面板
        int openFileCount = getHistoryCount();
        if (openFileCount == 0) {
            DesignerContext.getDesignerFrame().addAndActivateJTemplate();
        }

        JTemplate selectedFile = MutilTempalteTabPane.getInstance().getSelectedFile();
        if (!isCurrentEditingFile(selectedFile.getPath())) {
            //如果此时面板上的实时刷新的selectedIndex得到的和历史的不一样
            DesignerContext.getDesignerFrame().activateJTemplate(selectedFile);
        }

        MutilTempalteTabPane.getInstance().repaint();
    }


    public boolean rename(FILE tplFile, String from, String to) {
        boolean isDir = tplFile.isDirectory();

        JTemplate<?, ?> template;

        template = this.getCurrentEditingTemplate();
        if (template != null) {
            String editingPath = FilenameUtils.standard(template.getEditingFILE().getPath());
            if (isDir ? editingPath.contains(from + CoreConstants.SEPARATOR) : editingPath.equals(from)) {
                FILE renameFile = template.getEditingFILE();
                renameFile.setPath(editingPath.replace(from, to));
            }
        }

        for (int i = 0; i < this.getHistoryCount(); i++) {
            template = this.get(i);
            String editingPath = FilenameUtils.standard(template.getEditingFILE().getPath());
            if (isDir ? editingPath.contains(from + CoreConstants.SEPARATOR) : editingPath.equals(from)) {
                FILE renameFile = template.getEditingFILE();
                renameFile.setPath(editingPath.replace(from, to));
            }

        }
        return true;
    }

    /**
     * 切换环境时暂存打开的模板内容，key 是在历史中的index，value 是模板xml 内容byte[]
     */
    private Map<Integer, FILE> stashFILEMap;

    /**
     * 切换环境前将正在编辑的模板暂存起来，并且在新环境中重新读取一遍，暂存的不是模板文件的内容而是模板对象的内容
     * <p>
     * 防止新环境加载到的同名 Class 和模板对象中的 Class 不一致，在新环境存储失败
     *
     * @see HistoryTemplateListCache#load()
     */
    public void stash() {
        FineLoggerFactory.getLogger().info("Env Change Template Stashing...");
        if (stashFILEMap == null) {
            stashFILEMap = new HashMap<Integer, FILE>();
        } else {
            stashFILEMap.clear();
        }
        int size = historyList.size();
        for (int i = 0; i < size; i++) {
            JTemplate<?, ?> template = historyList.get(i);
            FILE file = template.getEditingFILE();
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                BaseBook target = template.getTarget();
                if (target != null) {
                    target.export(outputStream);
                    stashFILEMap.put(i, new StashedFILE(file, outputStream.toByteArray()));
                }
                // 如果 target == null 那么这个模板是被模板内存优化功能处理过的，不用处理
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
        FineLoggerFactory.getLogger().info("Env Change Template Stashed.");
    }

    /**
     * 切换环境前将正在编辑的模板暂存起来后，在新环境重新读取一遍
     * <p>
     * 防止新环境加载到的同名 Class 和模板对象中的 Class 不一致，在新环境存储失败
     *
     * @see HistoryTemplateListCache#stash()
     */
    public void load() {
        FineLoggerFactory.getLogger().info("Env Change Template Loading...");
        if (stashFILEMap != null && stashFILEMap.size() != 0) {
            int size = historyList.size();
            for (int i = 0; i < size; i++) {
                try {
                    FILE stashedFile = stashFILEMap.get(i);
                    // 可能有模板 stash 失败的情况，在 load 的时候不更新它
                    // 或者这个模板是被模板内存优化功能处理过的，不用处理
                    if (stashedFile == null) {
                        continue;
                    }
                    JTemplate<?, ?> template = JTemplateFactory.createJTemplate(stashedFile);
                    if (template != null) {
                        historyList.set(i, template);
                        // 替换当前正在编辑的模板，使用添加并激活的方式，以便使用统一的入口来处理监听事件
                        if (isCurrentEditingFile(template.getPath())) {
                            DesignerContext.getDesignerFrame().addAndActivateJTemplate(template);
                            setCurrentEditingTemplate(template);
                            FineLoggerFactory.getLogger().info("Env Change Current Editing Template.");
                        }
                    }
                } catch (Exception e) {
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                }
            }
            stashFILEMap.clear();
            MutilTempalteTabPane.getInstance().refreshOpenedTemplate(historyList);
            MutilTempalteTabPane.getInstance().repaint();
        }
        FineLoggerFactory.getLogger().info("Env Change Template Loaded.");
    }

    /**
     * 重新载入当前模板，刷新数据/对象
     */
    public void reloadCurrentTemplate() {
        JTemplate<?, ?> jt = getCurrentEditingTemplate();
        boolean access = jt != null && jt.getEditingFILE() != null && jt.getEditingFILE().exists();
        if (access) {
            closeSelectedReport(jt);
            DesignerContext.getDesignerFrame().openTemplate(jt.getEditingFILE());
        }
    }

}
