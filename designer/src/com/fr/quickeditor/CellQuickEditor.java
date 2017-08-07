package com.fr.quickeditor;

import com.fr.design.actions.utils.DeprecatedActionManager;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.selection.QuickEditor;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.report.cell.TemplateCellElement;
import com.fr.stable.ColumnRow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author zhou, yaoh.wu
 * @version 2017年8月7日16点54分
 * @since 1.0
 */
public abstract class CellQuickEditor extends QuickEditor<ElementCasePane> {

    protected UITextField columnRowTextField;
    protected TemplateCellElement cellElement;

    public CellQuickEditor() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p};
        Component[][] components = new Component[][]{
                new Component[]{initTopContent(), null},
                new Component[]{createCenterBody(), null}
        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        this.add(panel, BorderLayout.CENTER);
    }


    private JPanel initTopContent() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p};
        UILabel cellLabel = new UILabel(Inter.getLocText("Cell"));
        cellLabel.setPreferredSize(new Dimension(60, 20));
        UILabel insertContentLabel = new UILabel(Inter.getLocText("HF-Insert_Content"));
        insertContentLabel.setPreferredSize(new Dimension(60, 20));
        UIButton cellElementEditButton = initCellElementEditButton();
        Component[][] components = new Component[][]{
                new Component[]{cellLabel, columnRowTextField = initColumnRowTextField()},
                new Component[]{insertContentLabel, cellElementEditButton},
        };
        JPanel topContent = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        topContent.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 15));
        return topContent;
    }


    /**
     * 初始化添加按钮
     * TODO 9.0 换成下拉菜单后原来的快捷键不好处理，先跳过。
     *
     * @return UIButton
     */
    private UIButton initCellElementEditButton() {
        final UIButton cellElementEditButton = new UIButton(IOUtils.readIcon("/com/fr/design/images/buttonicon/add.png"));
        cellElementEditButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent evt) {
                GUICoreUtils.showPopMenuWithParentWidth(DeprecatedActionManager.getCellMenu(tc).createJPopupMenu(), cellElementEditButton, 0, cellElementEditButton.getY() - 6);
            }
        });
        return cellElementEditButton;
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