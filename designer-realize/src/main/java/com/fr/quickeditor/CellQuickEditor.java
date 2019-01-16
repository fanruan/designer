package com.fr.quickeditor;

import com.fr.design.actions.UpdateAction;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.iscrollbar.UIScrollBar;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.CellElementPropertyPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.menu.ShortCut;
import com.fr.design.selection.QuickEditor;
import com.fr.design.utils.gui.UIComponentUtils;
import com.fr.design.widget.FRWidgetFactory;
import com.fr.grid.selection.CellSelection;
import com.fr.quickeditor.cellquick.layout.CellElementBarLayout;
import com.fr.report.cell.TemplateCellElement;
import com.fr.stable.ColumnRow;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

/**
 * @author zhou, yaoh.wu
 * @version 2017年8月7日16点54分
 * @since 1.0
 */
public abstract class CellQuickEditor extends QuickEditor<ElementCasePane> {

    protected static final Dimension LABEL_DIMENSION = new Dimension(60, 20);
    protected static final int VGAP = 10, HGAP = 8, VGAP_INNER = 3;
    private static final int INSERT_CONTENT_LABEL_WIDTH = 60;

    /**
     * 滚动条相关配置
     */
    private static final int MAXVALUE = 100;
    private static final int CONTENT_PANE_WIDTH_GAP = 3;
    private static final int MOUSE_WHEEL_SPEED = 5;
    private static final int SCROLLBAR_WIDTH = 7;
    private int maxHeight = 280;
    private static final int TITLE_HEIGHT = 50;

    /**
     * 面板配置
     */
    protected UITextField columnRowTextField;
    protected TemplateCellElement cellElement;

    // 占位label
    protected final UILabel EMPTY_LABEL = new UILabel();

    private UIComboBox comboBox;
    private UpdateAction[] cellInsertActions;
    private int selectedIndex;
    private int currentSelectedIndex;
    private JPanel leftContentPane;
    private UIScrollBar scrollBar;
    private ActionListener comboBoxActionListener;

    public CellQuickEditor() {
        EMPTY_LABEL.setPreferredSize(LABEL_DIMENSION);
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        JComponent centerBody = createCenterBody();
        JPanel topContent = initTopContent();
        if (isScrollAll()) {
            double[] scrollAllRowSize = {p, p};
            prepareScrollBar();
            topContent.setBorder(BorderFactory.createMatteBorder(10, 10, 0, 0, this.getBackground()));
            centerBody.setBorder(BorderFactory.createMatteBorder(0, 10, 0, 0, this.getBackground()));
            Component[][] components = new Component[][]{
                    new Component[]{topContent, null},
                    new Component[]{centerBody, null}
            };
            leftContentPane = TableLayoutHelper.createGapTableLayoutPane(components, scrollAllRowSize, columnSize, HGAP, VGAP);
            this.setLayout(new CellElementBarLayout(leftContentPane) {
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
                        leftContentPane.setBounds(0, -beginY, width - SCROLLBAR_WIDTH - CONTENT_PANE_WIDTH_GAP, height + beginY);
                        scrollBar.setBounds(width - SCROLLBAR_WIDTH - CONTENT_PANE_WIDTH_GAP, 0, SCROLLBAR_WIDTH + CONTENT_PANE_WIDTH_GAP, height);
                    } else {
                        leftContentPane.setBounds(0, 0, width - SCROLLBAR_WIDTH - CONTENT_PANE_WIDTH_GAP, height);
                    }
                    leftContentPane.validate();
                }
            });
            this.add(scrollBar);
            this.add(leftContentPane);
        } else {
            double[] scrollContentRowSize = {p, f};
            topContent.setBorder(BorderFactory.createMatteBorder(10, 10, 0, 10, this.getBackground()));
            centerBody.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, this.getBackground()));
            Component[][] components = new Component[][]{
                    new Component[]{topContent, null},
                    new Component[]{centerBody, null}
            };
            this.setLayout(new BorderLayout());
            this.add(TableLayoutHelper.createGapTableLayoutPane(components, scrollContentRowSize, columnSize, HGAP, VGAP), BorderLayout.CENTER);
        }
    }

    /**
     * 初始化详细信息面板
     *
     * @return JComponent 待显示的详细信息面板
     */
    public abstract JComponent createCenterBody();

    /**
     * 是否全局具有滚动条
     *
     * @return boolean 是否全局具有滚动条
     */
    public abstract boolean isScrollAll();


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

        JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if (jTemplate != null) {
            comboBox.removeActionListener(comboBoxActionListener);
            comboBox.removeAllItems();
            String[] items = getDefaultComboBoxItems();
            for (String item : items) {
                comboBox.addItem(item);
            }
            Object comboBoxSelected = getComboBoxSelected();
            if (comboBoxSelected != null) {
                comboBox.setSelectedItem(((ShortCut) comboBoxSelected).getMenuKeySet().getMenuKeySetName());
            } else {
                comboBox.setSelectedIndex(1);
            }
            currentSelectedIndex = comboBox.getSelectedIndex();
            comboBoxActionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cellInsertActions = ActionFactory.createCellInsertAction(ElementCasePane.class, tc);
                    selectedIndex = comboBox.getSelectedIndex();
                    comboBox.setPopupVisible(false);
                    comboBox.repaint();
                    if (selectedIndex < cellInsertActions.length) {
                        cellInsertActions[selectedIndex].actionPerformed(e);
                    }
                    comboBox.setSelectedIndex(currentSelectedIndex);
                }
            };
            comboBox.addActionListener(comboBoxActionListener);
        }
        refreshDetails();
    }

    /**
     * 刷新详细信息
     */
    protected abstract void refreshDetails();


    private JPanel initTopContent() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {INSERT_CONTENT_LABEL_WIDTH, f};
        double[] rowSize = {p, p};
        UILabel cellLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Cell"));
        UILabel insertContentLabel = FRWidgetFactory.createLineWrapLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Insert_Cell_Element"));
        initCellElementEditComboBox();
        Component[][] components = new Component[][]{
                new Component[]{cellLabel, columnRowTextField = initColumnRowTextField()},
                new Component[]{insertContentLabel, UIComponentUtils.wrapWithBorderLayoutPane(comboBox)},
        };
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, HGAP, VGAP);
    }

    private void prepareScrollBar() {
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

        scrollBar.setPreferredSize(new Dimension(SCROLLBAR_WIDTH + CONTENT_PANE_WIDTH_GAP, this.getHeight()));
        scrollBar.setBlockIncrement(SCROLLBAR_WIDTH + CONTENT_PANE_WIDTH_GAP);
        scrollBar.setBorder(BorderFactory.createMatteBorder(0, CONTENT_PANE_WIDTH_GAP, 0, 0, this.getBackground()));
    }

    /**
     * 初始化添加按钮
     */
    private void initCellElementEditComboBox() {
        JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if (jTemplate == null) {
            comboBox = new UIComboBox();
            return;
        }
        final String[] items = getDefaultComboBoxItems();
        comboBox = new UIComboBox(items);
        final Object comboBoxSelected = getComboBoxSelected();
        if (comboBoxSelected != null) {
            comboBox.setSelectedItem(((ShortCut) comboBoxSelected).getMenuKeySet().getMenuKeySetName());
        } else {
            comboBox.setSelectedIndex(1);
        }
        currentSelectedIndex = comboBox.getSelectedIndex();
        comboBox.addActionListener(comboBoxActionListener);
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
                    Object[] options = {com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_OK")};
                    JOptionPane.showOptionDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Please_Input_Letters+Numbers(A1,AA1,A11....)"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Warning"),
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
}
