package com.fr.quickeditor;

import com.fr.design.actions.UpdateAction;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.iscrollbar.UIScrollBar;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.CellElementPropertyPane;
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
import java.awt.event.*;
import java.util.ArrayList;

/**
 * @author zhou, yaoh.wu
 * @version 2017年8月7日16点54分
 * @since 1.0
 */
public abstract class CellQuickEditor extends QuickEditor<ElementCasePane> {

    /*滚动条相关配置*/
    private static final int MAXVALUE = 100;
    private static final int TITLE_HEIGHT = 50;
    private static final int MOUSE_WHEEL_SPEED = 5;
    private static final int CONTENT_PANE_WIDTH_GAP = 4;
    private static final int SCROLLBAR_WIDTH = 8;
    private int maxHeight = 280;
    /*面板配置*/
    protected UITextField columnRowTextField;
    protected TemplateCellElement cellElement;
    private UIComboBox comboBox;
    private UpdateAction[] cellInsertActions;
    private int selectedIndex;
    private JPanel leftContentPane;
    private UIScrollBar scrollBar;
    /*占位label*/
    protected static UILabel emptyLabel = new UILabel();

    static {
        emptyLabel.setPreferredSize(new Dimension(60, 20));
    }

    protected static final int VGAP = 10, HGAP = 8, VGAP_INNER = 5;

    public CellQuickEditor() {

        scrollBar = new UIScrollBar(UIScrollBar.VERTICAL) {
            @Override
            public int getVisibleAmount() {
                int preferredHeight = leftContentPane.getPreferredSize().height;
                int e = MAXVALUE * (maxHeight) / preferredHeight;
                setVisibleAmount(e);
                return e;
            }

            @Override
            public int getMaximum() {
                return MAXVALUE;
            }

        };

        scrollBar.addAdjustmentListener(new AdjustmentListener() {

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                doLayout();
            }
        });
        this.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int value = scrollBar.getValue();
                value += MOUSE_WHEEL_SPEED * e.getWheelRotation();
                scrollBar.setValue(value);
                doLayout();
            }
        });

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p};
        JComponent centerBody = createCenterBody();
        centerBody.setBorder(BorderFactory.createMatteBorder(0, 10, 0, 0, this.getBackground()));
        Component[][] components = new Component[][]{
                new Component[]{initTopContent(), null},
                new Component[]{centerBody, null}
        };
        leftContentPane = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, HGAP, VGAP);
        this.setLayout(new BarLayout());
        this.add(scrollBar);
        this.add(leftContentPane);
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
        UIComboBox cellElementEditButton = initCellElementEditComboBox();
        Component[][] components = new Component[][]{
                new Component[]{cellLabel, columnRowTextField = initColumnRowTextField()},
                new Component[]{insertContentLabel, cellElementEditButton},
        };
        JPanel topContent = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, HGAP, VGAP);
        topContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
        return topContent;
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
                comboBox.setModel(new DefaultComboBoxModel<>(arrayList.toArray(new String[arrayList.size()])));
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
        MenuKeySet[] cellInsertActionNames = ActionFactory.createCellInsertActionName();
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

    /**
     * 属性面板的滚动条和内容区域的布局管理类
     * yaoh.wu 由于这边不能继承{@link com.fr.design.mainframe.AbstractAttrPane.BarLayout}，所以冗余了一份滚动条代码进来
     *
     * @see com.fr.design.mainframe.AbstractAttrPane.BarLayout
     */
    protected class BarLayout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {

        }

        @Override
        public void removeLayoutComponent(Component comp) {

        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return leftContentPane.getPreferredSize();
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return leftContentPane.getMinimumSize();
        }

        @Override
        public void layoutContainer(Container parent) {
            maxHeight = CellElementPropertyPane.getInstance().getHeight() - TITLE_HEIGHT;
            int beginY;
            if ((MAXVALUE - scrollBar.getVisibleAmount()) == 0) {
                beginY = 0;
            } else {
                int preferredHeight = leftContentPane.getPreferredSize().height;
                int value = scrollBar.getValue();
                beginY = value * (preferredHeight - maxHeight) / (MAXVALUE - scrollBar.getVisibleAmount());
            }
            int width = parent.getWidth();
            int height = parent.getHeight();
            if (leftContentPane.getPreferredSize().height > maxHeight) {
                leftContentPane.setBounds(0, -beginY, width - scrollBar.getWidth() - CONTENT_PANE_WIDTH_GAP, height + beginY);
                scrollBar.setBounds(width - scrollBar.getWidth() - 1, 0, scrollBar.getWidth(), height);
            } else {
                leftContentPane.setBounds(0, 0, width - SCROLLBAR_WIDTH - CONTENT_PANE_WIDTH_GAP, height);
            }
            leftContentPane.validate();
        }
    }
}