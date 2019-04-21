package com.fr.design.mainframe.vcs.ui;

import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.file.MutilTempalteTabPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrameFileDealerPane;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.vcs.proxy.VcsCacheFileNodeFileProxy;
import com.fr.file.filetree.FileNode;
import com.fr.log.FineLoggerFactory;
import com.fr.report.entity.VcsEntity;
import com.fr.stable.StringUtils;
import com.fr.workspace.server.vcs.VcsOperator;
import com.fr.workspace.server.vcs.common.Constants;

import javax.swing.AbstractCellEditor;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import java.awt.Component;


public class FileVersionCellEditor extends AbstractCellEditor implements TableCellEditor {
    private final VcsOperator vcs;
    //第一行
    private final JPanel firstRowPanel;
    //其余行
    private final FileVersionRowPanel renderAndEditor;

    public FileVersionCellEditor(FileVersionFirstRowPanel firstRowPanel, FileVersionRowPanel renderAndEditor, VcsOperator vcs) {
        this.vcs = vcs;
        this.firstRowPanel = firstRowPanel;
        this.renderAndEditor = renderAndEditor;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        String fileOfVersion = null;
        Component editor = row == 0 ? firstRowPanel : renderAndEditor;
        if (isSelected) {
            return editor;
        } else if (row == 0) {
            //TODO path "/"
            String path = DesignerFrameFileDealerPane.getInstance().getSelectedOperation().getFilePath();
            try {
                fileOfVersion = vcs.getFileOfCurrent(path.replaceFirst("/", ""));
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage());
            }
        } else {
            renderAndEditor.update((VcsEntity) value);
            try {
                fileOfVersion = vcs.getFileOfFileVersion(((VcsEntity) value).getFilename(), ((VcsEntity) value).getVersion());
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage());
            }
        }

        editor.setBackground(Constants.TABLE_SELECT_BACKGROUND);
        if (StringUtils.isNotEmpty(fileOfVersion)) {
            //先关闭当前打开的模板版本
            JTemplate<?, ?> jt = HistoryTemplateListCache.getInstance().getCurrentEditingTemplate();
            jt.stopEditing();
            MutilTempalteTabPane.getInstance().setIsCloseCurrent(true);
            MutilTempalteTabPane.getInstance().closeFormat(jt);
            MutilTempalteTabPane.getInstance().closeSpecifiedTemplate(jt);
            //再打开cache中的模板
            DesignerContext.getDesignerFrame().openTemplate(new VcsCacheFileNodeFileProxy(new FileNode(fileOfVersion, false)));

        }

        double height = editor.getPreferredSize().getHeight();
        if (table.getRowHeight(row) != height) {
            table.setRowHeight(row, (int) height);
        }
        return editor;
    }

    @Override
    public Object getCellEditorValue() {
        return renderAndEditor.getFileVersion();
    }
}