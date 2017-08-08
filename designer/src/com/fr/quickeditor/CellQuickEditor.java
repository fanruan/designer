package com.fr.quickeditor;

import com.fr.design.actions.UpdateAction;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.menu.ShortCut;
import com.fr.design.selection.QuickEditor;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.report.cell.TemplateCellElement;
import com.fr.stable.ColumnRow;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author zhou
 * @since 2012-7-23下午5:16:53
 */
public abstract class CellQuickEditor extends QuickEditor<ElementCasePane> {

    protected UITextField columnRowTextField;
    private UIButton cellElementEditButton;
    protected TemplateCellElement cellElement;
    protected UIComboBox comboBox;
    private UpdateAction[] cellInsertActions;
    private MenuKeySet[] cellInsertActionNames;
    private int selectedIndex;

    public CellQuickEditor() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p, p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel("  " + Inter.getLocText("Cell")), columnRowTextField = initColumnRowTextField()},
                new Component[]{new UILabel(Inter.getLocText("HF-Insert_Content") + " "), initCellElementEditComboBox()},
                new Component[]{createCenterBody(), null}
        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(panel, BorderLayout.CENTER);
    }

    /**
     * 初始化添加按钮
     *
     * @return UIButton
     */
    private UIComboBox initCellElementEditComboBox() {
        final String[] items = getDefaultComboBoxItems();
        comboBox = new UIComboBox(items);
        final Object comboBoxSelected = getComboBoxSelected();
        if (comboBoxSelected != null) {
            comboBox.setSelectedItem(((ShortCut) comboBoxSelected).getMenuKeySet().getMenuKeySetName());
        } else {
            comboBox.setSelectedIndex(1);
        }
        comboBox.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                if (cellInsertActions == null) {
                    cellInsertActions = ActionFactory.createCellInsertAction(ElementCasePane.class, tc);
                }
                // 这边重新获取是因为要根据JTemplate做一个过滤
                ArrayList<String> arrayList = new ArrayList<String>();
                for (UpdateAction action : cellInsertActions) {
                    arrayList.add(action.getMenuKeySet().getMenuKeySetName());
                }
                comboBox.setModel(new DefaultComboBoxModel(arrayList.toArray(new String[arrayList.size()])));
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedIndex = comboBox.getSelectedIndex();
                cellInsertActions[selectedIndex].actionPerformed(e);
            }
        });
        return comboBox;
    }

    private String[] getDefaultComboBoxItems() {
        cellInsertActionNames = ActionFactory.createCellInsertActionName();
        ArrayList<String> names = new ArrayList<>();
        for (MenuKeySet cellInsertActionName : cellInsertActionNames) {
            names.add(cellInsertActionName.getMenuKeySetName());
        }
        return names.toArray(new String[names.size()]);
    }

    /**
     * 初始化单元格域，存储当前选择的单元格，例A3,B4等
     *
     * @return 单元格信息文本域
     */
    private UITextField initColumnRowTextField() {
        final UITextField columnRowTextField = new UITextField(4);

        // barry:输入位置定位单元格
        columnRowTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ColumnRow columnRowEdit = ColumnRow.valueOf(columnRowTextField.getText());
                // barry:检查输入是否正确
                if (!ColumnRow.validate(columnRowEdit)) {
                    Object[] options = {Inter.getLocText("OK")};
                    JOptionPane.showOptionDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("Please_Input_Letters+Numbers(A1,AA1,A11....)"), Inter.getLocText("Warning"),
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                    // 重新选中当前的selection,把columnRowTextField
                    tc.setSelection(tc.getSelection());
                    return;
                }
                JScrollBar verticalBar = tc.getVerticalScrollBar(), horizontalBar = tc.getHorizontalScrollBar();
                int m = columnRowEdit.getColumn(), n = columnRowEdit.getRow();
                verticalBar.setMaximum(n);
                verticalBar.setValue(n < 21 ? verticalBar.getValue() : n - 20);
                horizontalBar.setMaximum(m);
                horizontalBar.setValue(m < 13 ? horizontalBar.getValue() : m - 12);
                tc.setSelection(new CellSelection(m, n, 1, 1));
                tc.requestFocus();
            }
        });
        return columnRowTextField;
    }


    /**
     * 初始化详细信息面板
     *
     * @return JComponent 待显示的详细信息面板
     */
    public abstract JComponent createCenterBody();


    /**
     * 初始化下拉框中的类型
     *
     * @return JComponent 待显示的详细信息面板
     */
    public abstract Object getComboBoxSelected();

    /**
     * 刷新
     */
    @Override
    protected void refresh() {
        CellSelection cs = (CellSelection) tc.getSelection();
        ColumnRow columnRow = ColumnRow.valueOf(cs.getColumn(), cs.getRow());
        columnRowTextField.setText(columnRow.toString());
        cellElement = tc.getEditingElementCase().getTemplateCellElement(cs.getColumn(), cs.getRow());
        refreshDetails();
    }

    /**
     * 刷新详细信息
     */
    protected abstract void refreshDetails();
}