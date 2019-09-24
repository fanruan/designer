package com.fr.design.mainframe.vcs.ui;

import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.file.MutilTempalteTabPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrameFileDealerPane;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.vcs.common.VcsHelper;
import com.fr.design.mainframe.vcs.common.VcsCacheFileNodeFile;
import com.fr.file.filetree.FileNode;
import com.fr.general.ComparatorUtils;
import com.fr.report.entity.VcsEntity;
import com.fr.stable.AssistUtils;
import com.fr.stable.StringUtils;
import com.fr.workspace.WorkContext;
import com.fr.workspace.server.vcs.VcsOperator;

import javax.swing.AbstractCellEditor;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import java.awt.Component;


public class FileVersionCellEditor extends AbstractCellEditor implements TableCellEditor {
    private static final long serialVersionUID = -7299526575184810693L;
    //第一行
    private final JPanel firstRowPanel;
    //其余行
    private final FileVersionRowPanel renderAndEditor;

    public FileVersionCellEditor() {
        this.firstRowPanel = new FileVersionFirstRowPanel();
        this.renderAndEditor = new FileVersionRowPanel();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        String fileOfVersion;
        VcsOperator vcsOperator = WorkContext.getCurrent().get(VcsOperator.class);
        Component editor = row == 0 ? firstRowPanel : renderAndEditor;
        if (isSelected) {
            return editor;
        } else if (row == 0) {
            String path = DesignerFrameFileDealerPane.getInstance().getSelectedOperation().getFilePath();
            fileOfVersion = vcsOperator.getFileOfCurrent(path.replaceFirst("/", ""));
        } else {
            renderAndEditor.update((VcsEntity) value);
            fileOfVersion = vcsOperator.getFileOfFileVersion(((VcsEntity) value).getFilename(), ((VcsEntity) value).getVersion());

        }

        editor.setBackground(VcsHelper.TABLE_SELECT_BACKGROUND);
        if (StringUtils.isNotEmpty(fileOfVersion)) {
            //先关闭当前打开的模板版本
            JTemplate jt = HistoryTemplateListCache.getInstance().getCurrentEditingTemplate();
            jt.stopEditing();
            //只有模板路径一致时关闭当前模板
            if (ComparatorUtils.equals(fileOfVersion, jt.getPath())) {
                MutilTempalteTabPane.getInstance().setIsCloseCurrent(true);
                MutilTempalteTabPane.getInstance().closeFormat(jt);
                MutilTempalteTabPane.getInstance().closeSpecifiedTemplate(jt);
            }

            //再打开cache中的模板
            DesignerContext.getDesignerFrame().openTemplate(new VcsCacheFileNodeFile(new FileNode(fileOfVersion, false)));

        }

        double height = editor.getPreferredSize().getHeight();
        if (!AssistUtils.equals(table.getRowHeight(row), height)) {
            table.setRowHeight(row, (int) height + VcsHelper.OFFSET);
        }
        return editor;
    }

    @Override
    public Object getCellEditorValue() {
        return renderAndEditor.getVcsEntity();
    }
}