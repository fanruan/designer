package com.fr.design.mainframe.cell;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.selection.QuickEditor;


import javax.swing.*;
import java.awt.*;

/**
 * QuickEditorRegion
 *
 * @author zhou, yaoh.wu
 * @version 2017年7月25日
 * @since 8.0
 */

public class QuickEditorRegion extends JPanel {


    private static QuickEditorRegion singleton = new QuickEditorRegion();
    private static JPanel EMPTY;

    private QuickEditorRegion() {
        this.setLayout(new BorderLayout());
    }

    public static QuickEditorRegion getInstance() {
        return singleton;
    }

    public static JPanel getEmptyEditor() {
        if (EMPTY == null) {
            EMPTY = new JPanel(new BorderLayout());
            UILabel content = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_None_Message_Property_Table") + "!");
            content.setBorder(BorderFactory.createEmptyBorder(0, 70, 0, 0));
            EMPTY.add(content, BorderLayout.CENTER);
        }
        return EMPTY;
    }

    /**
     * 更新面板显示数据
     *
     * @param currentEditor 当前正在编辑的单元格编辑器或者默认的单元格编辑器
     */
    public void populate(final QuickEditor currentEditor) {
        this.removeAll();
        if (currentEditor == null || currentEditor.getComponentCount() == 0) {
            this.add(getEmptyEditor(), BorderLayout.CENTER);
        } else {
            this.add(currentEditor, BorderLayout.CENTER);
        }
        validate();
        repaint();
    }
}