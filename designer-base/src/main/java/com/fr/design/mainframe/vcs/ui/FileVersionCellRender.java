package com.fr.design.mainframe.vcs.ui;

import com.fr.design.mainframe.vcs.common.VcsHelper;
import com.fr.report.entity.VcsEntity;
import com.fr.stable.AssistUtils;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;

import static com.fr.design.constants.UIConstants.TREE_BACKGROUND;
import static com.fr.design.mainframe.vcs.common.VcsHelper.TABLE_SELECT_BACKGROUND;


public class FileVersionCellRender implements TableCellRenderer {

    //第一行
    private final JPanel firstRowPanel;
    //其余行
    private final FileVersionRowPanel render;

    public FileVersionCellRender() {
        this.render = new FileVersionRowPanel();
        this.firstRowPanel = new FileVersionFirstRowPanel();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component editor = row == 0 ? firstRowPanel : render;
        // https://stackoverflow.com/questions/3054775/jtable-strange-behavior-from-getaccessiblechild-method-resulting-in-null-point
        if (value != null) {
            render.update((VcsEntity) value);
        }
        editor.setBackground(isSelected ? TABLE_SELECT_BACKGROUND : TREE_BACKGROUND);

        double height = editor.getPreferredSize().getHeight();
        if (!AssistUtils.equals(table.getRowHeight(row), height)) {
            table.setRowHeight(row, (int) height + VcsHelper.OFFSET);
        }
        return editor;
    }


}