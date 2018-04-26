package com.fr.design.present.dict;

import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.itable.UITable;
import com.fr.design.gui.itable.UITableEditor;

import javax.swing.event.ChangeEvent;

/**
 * Created with IntelliJ IDEA.
 * User: 小灰灰
 * Date: 13-7-22
 * Time: 下午2:27
 * To change this template use File | Settings | File Templates.
 */
public class CustomDictCorrelationPane extends UICorrelationPane {
    public CustomDictCorrelationPane (String... names) {
        super(names);
    }
    protected UITable initUITable() {
        return new UITable(columnCount, true) {

            public UITableEditor createTableEditor() {
                return CustomDictCorrelationPane.this.createUITableEditor();
            }

            public void tableCellEditingStopped(ChangeEvent e) {
                CustomDictCorrelationPane.this.stopPaneEditing(e);
            }
        };
    }
}