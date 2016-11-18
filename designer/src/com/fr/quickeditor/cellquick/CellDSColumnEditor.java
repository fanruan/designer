package com.fr.quickeditor.cellquick;

import com.fr.design.actions.columnrow.DSColumnAdvancedAction;
import com.fr.design.actions.columnrow.DSColumnBasicAction;
import com.fr.design.actions.columnrow.DSColumnConditionAction;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.dscolumn.ResultSetGroupDockingPane;
import com.fr.design.dscolumn.SelectedDataColumnPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.quickeditor.CellQuickEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class CellDSColumnEditor extends CellQuickEditor {
    private JPanel dsColumnRegion;
    private JPanel centerPane;
    private SelectedDataColumnPane dataPane;
    private ResultSetGroupDockingPane groupPane;
    private ItemListener groupListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e == null) {
                //分组-高级-自定义点确定的时候传进来null的e,但是这时候应该触发保存
                groupPane.update();
                fireTargetModified();
                return;
            }
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                if (!isEditing) {
                    return;
                }
                groupPane.update();
                fireTargetModified();
            }
        }
    };

    private ItemListener dataListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (!isEditing) {
                    return;
                }
                dataPane.update(cellElement);
                fireTargetModified();
            }
        }
    };

    private CellDSColumnEditor() {
        super();
    }

    @Override
    public JComponent createCenterBody() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p, p, p};
        Component[][] components = new Component[][]{};
        dsColumnRegion = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        centerPane = new JPanel(new BorderLayout(0, 0));
        centerPane.add(dsColumnRegion, BorderLayout.CENTER);
        return centerPane;
    }

    // august:这里面的东西都全部重新动态生成，不然容易出错
    @Override
    protected void refreshDetails() {
        JPanel pane = new JPanel(new BorderLayout(LayoutConstants.HGAP_LARGE, 0));
        pane.add(new UIButton(new DSColumnConditionAction(tc)), BorderLayout.WEST);
        pane.add(new UIButton(new DSColumnAdvancedAction(tc)), BorderLayout.CENTER);
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p, p, p};
        Component[][] components = new Component[][]{
                new Component[]{new UIButton(new DSColumnBasicAction(tc)), null},
                new Component[]{pane, null},
                new Component[]{dataPane = new SelectedDataColumnPane(false), null},
                new Component[]{groupPane = new ResultSetGroupDockingPane(tc), null}
        };
        centerPane.removeAll();
        dsColumnRegion = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        centerPane.add(dsColumnRegion, BorderLayout.CENTER);
        dataPane.addListener(dataListener);
        groupPane.addListener(groupListener);
        dataPane.populate(null, cellElement);
        groupPane.populate(cellElement);
        this.validate();
    }


    /**
     * for 关闭时候释放
     */
    public void release () {
        super.release();
        dsColumnRegion = null;
        centerPane = null;
    }

}