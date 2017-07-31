package com.fr.design.dscolumn;

import com.fr.base.Formula;
import com.fr.data.TableDataSource;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.CustomVariableResolver;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.cell.CellEditorPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.core.group.DSColumn;
import com.fr.report.cell.cellattr.core.group.SelectCount;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * 单元格元素 数据列 基本设置内容面板
 * <p>
 * 复制了一些{@link DSColumnAdvancedPane}的内部类，不做复用，价值不大，进行修改后直接使用;
 * </p>
 *
 * @author yaoh.wu
 * @version 2017年7月25日
 * @since 9.0
 */
public class DSColumnAdvancedEditorPane extends CellEditorPane {

    private static final String INSET_TEXT = " ";

    //排列顺序
    private ResultSetSortConfigPane sortPane;
    //结果集筛选
    private SelectCountPane selectCountPane;
    //自定义值显示
    private ValuePane valuePane;
    //横向可扩展性
    private UICheckBox horizontalExtendableCheckBox;
    //纵向可扩展性
    private UICheckBox verticalExtendableCheckBox;
    //补充空白数据
    private UICheckBox useMultiplyNumCheckBox;
    //补充空白数据书目输入框
    private UISpinner multiNumSpinner;


    public DSColumnAdvancedEditorPane() {
        this.setLayout(new BorderLayout());
        this.add(this.createContentPane(), BorderLayout.CENTER);
    }


    @Override
    public String getIconPath() {
        return "Advanced";
    }

    @Override
    public String title4PopupWindow() {
        return "Advanced";
    }


    @Override
    public void update() {

    }

    @Override
    public void populate(TemplateCellElement cellElement) {

    }


    /**
     * 创建内容
     */
    private JPanel createContentPane() {
        this.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        this.sortPane = new ResultSetSortConfigPane();

        selectCountPane = new DSColumnAdvancedEditorPane.SelectCountPane();

        valuePane = new DSColumnAdvancedEditorPane.ValuePane();

        JPanel extendableDirectionPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        extendableDirectionPane.add(horizontalExtendableCheckBox = new UICheckBox(Inter.getLocText("ExpandD-Horizontal_Extendable")));
        extendableDirectionPane.add(verticalExtendableCheckBox = new UICheckBox(Inter.getLocText("ExpandD-Vertical_Extendable")));

        JPanel extendablePane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("ExpandD-Expandable"));
        extendablePane.setLayout(new BorderLayout());
        extendablePane.add(extendableDirectionPane, BorderLayout.CENTER);

        JPanel multiNumPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("Fill_blank_Data"));

        useMultiplyNumCheckBox = new UICheckBox(Inter.getLocText("Column_Multiple"));
        multiNumPane.add(useMultiplyNumCheckBox);
        multiNumPane.add(new UILabel(INSET_TEXT));

        multiNumSpinner = new UISpinner(1, 10000, 1, 1);
        multiNumPane.add(multiNumSpinner);

        useMultiplyNumCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                checkButtonEnabled();
            }
        });

        double[] rowSize = {TableLayout.PREFERRED, TableLayout.PREFERRED,
                TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED};
        double[] columnSize = {TableLayout.FILL};

        Component[][] components = null;
        components = new Component[][]{
                {sortPane},
                {selectCountPane},
                {valuePane},
                {extendablePane},
                {multiNumPane}
        };
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }


    private void checkButtonEnabled() {
        if (useMultiplyNumCheckBox.isSelected()) {
            multiNumSpinner.setEnabled(true);
        } else {
            multiNumSpinner.setEnabled(false);
        }
    }

    /**
     * 单元格元素>数据集>高级设置>结果排序设置面板
     *
     * @see com.fr.design.mainframe.cell.settingpane.CellExpandAttrPane
     */
    protected static class ResultSetSortConfigPane extends JPanel {
        private UIButtonGroup sort_type_pane;
        private TinyFormulaPane tinyFormulaPane;
        private CardLayout cardLayout;
        private JPanel centerPane;


        public ResultSetSortConfigPane() {
            this.setLayout(new BorderLayout(0, 4));
            Icon[] iconArray = {
                    IOUtils.readIcon("/com/fr/design/images/expand/none16x16.png"),
                    IOUtils.readIcon("/com/fr/design/images/expand/asc.png"),
                    IOUtils.readIcon("/com/fr/design/images/expand/des.png")
            };
            String[] nameArray = {Inter.getLocText("Sort-Original"), Inter.getLocText("Sort-Ascending"), Inter.getLocText("Sort-Descending")};
            sort_type_pane = new UIButtonGroup(iconArray);
            sort_type_pane.setAllToolTips(nameArray);
            sort_type_pane.setGlobalName(Inter.getLocText("ExpandD-Sort_After_Expand"));
            this.add(sort_type_pane, BorderLayout.NORTH);

            cardLayout = new CardLayout();
            centerPane = new JPanel(cardLayout);

            tinyFormulaPane = new TinyFormulaPane();

            centerPane.add(new JPanel(), "none");
            centerPane.add(tinyFormulaPane, "content");

            this.add(centerPane, BorderLayout.CENTER);

            sort_type_pane.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    cardLayout.show(centerPane, sort_type_pane.getSelectedIndex() == 0 ? "none" : "content");
                }
            });
        }


        /**
         * 刷新面板信息
         *
         * @param dataSource  数据集对象
         * @param cellElement 单元格
         */
        public void populate(TableDataSource dataSource, TemplateCellElement cellElement) {
            //todo
        }


        /**
         * 保存面板配置信息
         *
         * @param cellElement 单元格
         */
        public void update(CellElement cellElement) {
            //todo
        }
    }

    /**
     * 单元格元素>数据集>高级设置>结果集筛选设置面板
     *
     * @see DSColumnAdvancedPane.SelectCountPane
     */
    protected static class SelectCountPane extends JPanel {

        CellElement cellElement;
        //  private Comparator sortComparator;
        private UIComboBox selectCountComboBox;
        private JPanel selectCountCardPane;
        private UITextField serialTextField;

        DSColumnAdvancedEditorPane.JFormulaField topFormulaPane;
        DSColumnAdvancedEditorPane.JFormulaField bottomFormulaPane;

        public SelectCountPane() {
            this.setLayout(FRGUIPaneFactory.createBorderLayout());

            selectCountComboBox = new UIComboBox(new String[]{
                    Inter.getLocText("Undefined"),
                    Inter.getLocText("BindColumn-Top_N"),
                    Inter.getLocText("BindColumn-Bottom_N"),
                    Inter.getLocText("Odd"),
                    Inter.getLocText("Even"),
                    Inter.getLocText("Specify"),});
            selectCountComboBox.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    int selectIndex = selectCountComboBox.getSelectedIndex();
                    CardLayout c1 = (CardLayout) selectCountCardPane.getLayout();
                    if (selectIndex == 1) {
                        c1.show(selectCountCardPane, "TOP");
                    } else if (selectIndex == 2) {
                        c1.show(selectCountCardPane, "BOTTOM");
                    } else if (selectIndex == 3) {
                        c1.show(selectCountCardPane, "ODD");
                    } else if (selectIndex == 4) {
                        c1.show(selectCountCardPane, "EVEN");
                    } else if (selectIndex == 5) {
                        c1.show(selectCountCardPane, "SPECIFY");
                    } else {
                        c1.show(selectCountCardPane, "UNDEFINE");
                    }
                }
            });

            selectCountCardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
            this.add(GUICoreUtils.createFlowPane(new JComponent[]{new UILabel(INSET_TEXT), selectCountComboBox,
                    new UILabel(INSET_TEXT), selectCountCardPane}, FlowLayout.LEFT), BorderLayout.WEST);
//            selectCountCardPane.setLayout(new CardLayout());

            //not define pane

            JPanel undefinedPane = GUICoreUtils.createFlowPane(new UILabel(Inter.getLocText("Undefined")), FlowLayout.LEFT);
            topFormulaPane = new DSColumnAdvancedEditorPane.JFormulaField("-1");
            bottomFormulaPane = new DSColumnAdvancedEditorPane.JFormulaField("-1");
            serialTextField = new UITextField(18);
            JPanel oddPane = GUICoreUtils.createFlowPane(new UILabel(Inter.getLocText("BindColumn-Result_Serial_Number_Start_From_1")
                    + "  " + Inter.getLocText("BindColumn-Odd_Selected_(1,3,5...)")), FlowLayout.LEFT);
            JPanel evenPane = GUICoreUtils.createFlowPane(new UILabel(Inter.getLocText("BindColumn-Result_Serial_Number_Start_From_1")
                    + "  " + Inter.getLocText("BindColumn-Even_Selected_(2,4,6...)")), FlowLayout.LEFT);
            JPanel specifyPane = GUICoreUtils.createFlowPane(new JComponent[]{
                    serialTextField, new UILabel(
                    Inter.getLocText(new String[]{
                                    "Format", "BindColumn-Result_Serial_Number_Start_From_1", "Inner_Parameter", "Group_Count"},
                            new String[]{": 1,2-3,5,8  ", ",", "$__count__"})
            )
            }, FlowLayout.LEFT);
            serialTextField.setToolTipText(Inter.getLocText("StyleFormat-Sample") + ":=JOINARRAY(GREPARRAY(RANGE($__count__), item!=4), \",\")");
            selectCountCardPane.add(undefinedPane, "UNDEFINE");
            selectCountCardPane.add(topFormulaPane, "TOP");
            selectCountCardPane.add(bottomFormulaPane, "BOTTOM");
            //odd
            selectCountCardPane.add(oddPane, "ODD");
            //even
            selectCountCardPane.add(evenPane, "EVEN");
            //specify
            selectCountCardPane.add(specifyPane, "SPECIFY");
        }

        public void populate(CellElement cellElement) {
            if (cellElement == null) {
                return;
            }
            this.cellElement = cellElement;

            Object value = cellElement.getValue();
            if (value == null || !(value instanceof DSColumn)) {
                return;
            }
            DSColumn dSColumn = (DSColumn) (cellElement.getValue());
            SelectCount selectCount = dSColumn.getSelectCount();
            this.topFormulaPane.populateElement(cellElement);
            this.bottomFormulaPane.populateElement(cellElement);
            if (selectCount != null) {
                int selectCountType = selectCount.getType();
                this.selectCountComboBox.setSelectedIndex(selectCountType);
                if (selectCountType == SelectCount.TOP) {
                    this.topFormulaPane.populate(selectCount.getFormulaCount());
                } else if (selectCountType == SelectCount.BOTTOM) {
                    this.bottomFormulaPane.populate(selectCount.getFormulaCount());
                } else if (selectCountType == SelectCount.SPECIFY) {
                    this.serialTextField.setText(selectCount.getSerial());
                }
            }
        }

        public void update(CellElement cellElement) {
            if (cellElement == null) {
                return;
            }
            Object value = cellElement.getValue();
            if (value == null || !(value instanceof DSColumn)) {
                return;
            }
            DSColumn dSColumn = (DSColumn) (cellElement.getValue());

            //alex:SelectCount
            int selectCountSelectIndex = this.selectCountComboBox.getSelectedIndex();
            if (selectCountSelectIndex == 0) {
                dSColumn.setSelectCount(null);
            } else {
                SelectCount selectCount = new SelectCount();
                dSColumn.setSelectCount(selectCount);
                selectCount.setType(selectCountSelectIndex);
                if (selectCountSelectIndex == SelectCount.TOP) {
                    selectCount.setFormulaCount(this.topFormulaPane.getFormulaText());
                } else if (selectCountSelectIndex == SelectCount.BOTTOM) {
                    selectCount.setFormulaCount(this.bottomFormulaPane.getFormulaText());
                } else if (selectCountSelectIndex == SelectCount.SPECIFY) {
                    selectCount.setSerial(this.serialTextField.getText());
                }
            }
        }

        private JFormattedTextField getTextField(JSpinner spinner) {
            JComponent editor = spinner.getEditor();
            if (editor instanceof JSpinner.DefaultEditor) {
                return ((JSpinner.DefaultEditor) editor).getTextField();
            } else {
                System.err.println("Unexpected editor type: "
                        + spinner.getEditor().getClass()
                        + " isn't a descendant of DefaultEditor");
                return null;
            }
        }
    }

    /**
     * 单元格元素>数据集>高级设置>公式输入框
     *
     * @see DSColumnAdvancedPane.JFormulaField
     */
    private static class JFormulaField extends JPanel {
        private CellElement cellElement;
        private UITextField formulaTextField;
        private String defaultValue;

        public JFormulaField(String defaultValue) {
            this.defaultValue = defaultValue;

            this.setLayout(FRGUIPaneFactory.createBoxFlowLayout());
            UILabel bottomLabel = new UILabel("=");
            bottomLabel.setFont(new Font("Dialog", Font.BOLD, 12));
            this.add(bottomLabel);
            formulaTextField = new UITextField(24);
            this.add(formulaTextField);
            formulaTextField.setText(defaultValue);

            UIButton bottomFrmulaButton = new UIButton("...");
            this.add(bottomFrmulaButton);
            bottomFrmulaButton.setToolTipText(Inter.getLocText("Formula") + "...");
            bottomFrmulaButton.setPreferredSize(new Dimension(25, formulaTextField.getPreferredSize().height));
            bottomFrmulaButton.addActionListener(formulaButtonActionListener);
        }

        public void populate(String formulaContent) {
            this.formulaTextField.setText(formulaContent);
        }

        public void populateElement(CellElement cellElement) {
            this.cellElement = cellElement;
        }

        public String getFormulaText() {
            return this.formulaTextField.getText();
        }

        private ActionListener formulaButtonActionListener = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                Formula valueFormula = new Formula();
                String text = formulaTextField.getText();
                if (text == null || text.length() <= 0) {
                    valueFormula.setContent(defaultValue);
                } else {
                    valueFormula.setContent(text);
                }

                final UIFormula formulaPane = FormulaFactory.createFormulaPaneWhenReserveFormula();

                if (cellElement == null) {
                    return;
                }
                Object value = cellElement.getValue();
                if (value == null || !(value instanceof DSColumn)) {
                    return;
                }
                DSColumn dsColumn = (DSColumn) value;

                String[] displayNames = DesignTableDataManager.getSelectedColumnNames(DesignTableDataManager.getEditingTableDataSource(), dsColumn.getDSName());

                formulaPane.populate(valueFormula, new CustomVariableResolver(displayNames, true));
                formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor(DSColumnAdvancedEditorPane.JFormulaField.this), new DialogActionAdapter() {
                    @Override
                    public void doOk() {
                        Formula valueFormula = formulaPane.update();
                        if (valueFormula.getContent().length() <= 1) {
                            formulaTextField.setText(defaultValue);
                        } else {
                            formulaTextField.setText(valueFormula.getContent().substring(1));
                        }
                    }
                }).setVisible(true);
            }
        };
    }

    /**
     * 单元格元素>数据集>高级设置>自定义值显示设置面板
     *
     * @see DSColumnAdvancedPane.ValuePane
     */
    private static class ValuePane extends JPanel {
        private DSColumnAdvancedEditorPane.JFormulaField formulaField;

        public ValuePane() {
            this.setLayout(FRGUIPaneFactory.createBoxFlowLayout());

            this.add(new UILabel(INSET_TEXT + Inter.getLocText("Value") + ":"));
            this.add(Box.createHorizontalStrut(2));
            this.add((formulaField = new DSColumnAdvancedEditorPane.JFormulaField("$$$")));
        }

        public void populate(CellElement cellElement) {
            if (cellElement == null) {
                return;
            }

            Object value = cellElement.getValue();
            if (value == null || !(value instanceof DSColumn)) {
                return;
            }
            DSColumn dSColumn = (DSColumn) value;

            //formula
            String valueFormula = dSColumn.getResult();
            if (valueFormula == null) {
                valueFormula = "$$$";
            }
            formulaField.populateElement(cellElement);
            formulaField.populate(valueFormula);
        }

        public void update(CellElement cellElement) {
            if (cellElement == null) {
                return;
            }
            Object value = cellElement.getValue();
            if (value == null || !(value instanceof DSColumn)) {
                return;
            }
            DSColumn dSColumn = (DSColumn) (cellElement.getValue());

            //formula
            dSColumn.setResult(this.formulaField.getFormulaText());
        }
    }

}
