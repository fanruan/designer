package com.fr.quickeditor.cellquick;

import com.fr.base.Formula;
import com.fr.design.actions.columnrow.DSColumnConditionAction;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dscolumn.DSColumnAdvancedPane;
import com.fr.design.dscolumn.ResultSetGroupDockingPane;
import com.fr.design.dscolumn.SelectedDataColumnPane;
import com.fr.design.formula.CustomVariableResolver;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ibutton.UIHeadGroup;
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
import com.fr.quickeditor.CellQuickEditor;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.report.cell.cellattr.core.group.DSColumn;
import com.fr.report.cell.cellattr.core.group.SelectCount;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * 单元格元素 数据列编辑器
 *
 * @author yaoh.wu
 * @version 2017年7月24日
 * @since 9.0
 */
public class CellDSColumnEditor extends CellQuickEditor {

    private static double P = TableLayout.PREFERRED, F = TableLayout.FILL;

    private enum FilterType {
        //前N个 后N个 奇数 偶数 自定义 未定义
        TOP, BOTTOM, ODD, EVEN, SPECIFY, UNDEFINE
    }

    private JPanel dsColumnRegion;
    private JPanel centerPane;

    // 基本和高级设置
    private ArrayList<CellEditorPane> paneList;
    // 基本和高级设置 卡片布局
    private CardLayout card;
    // 基本和高级设置 容器面板
    private JPanel cardContainer;
    // 卡片布局TAB切换按钮
    private UIHeadGroup tabsHeaderIconPane;


    private CellDSColumnEditor() {
        super();
    }

    /**
     * 创建面板占位
     *
     * @return JComponent 详细信息面板
     */
    @Override
    public JComponent createCenterBody() {
        this.createPanes();
        this.createSwitchTab();
        dsColumnRegion = new JPanel(new BorderLayout());
        dsColumnRegion.add(tabsHeaderIconPane, BorderLayout.NORTH);
        dsColumnRegion.add(cardContainer, BorderLayout.CENTER);
        centerPane = new JPanel(new BorderLayout());
        centerPane.add(dsColumnRegion, BorderLayout.CENTER);
        return centerPane;
    }

    /**
     * 内容全部重新动态生成，不然容易出错
     * 刷新详细信息面板
     */
    @Override
    protected void refreshDetails() {
        this.createPanes();
        this.createSwitchTab();
        dsColumnRegion = new JPanel(new BorderLayout());
        dsColumnRegion.add(tabsHeaderIconPane, BorderLayout.NORTH);
        dsColumnRegion.add(cardContainer, BorderLayout.CENTER);
        //必须removeAll之后再添加；重新再实例化一个centerJPanel，因为对象变了会显示不出来
        centerPane.removeAll();
        centerPane.add(dsColumnRegion, BorderLayout.CENTER);
        for (CellEditorPane cellEditorPane : paneList) {
            cellEditorPane.populate();
        }
        this.validate();
    }


    /**
     * 关闭时候释放
     */
    public void release() {
        super.release();
        dsColumnRegion = null;
        centerPane = null;
    }


    /**
     * 初始化基本和高级设置切换tab
     */
    private void createSwitchTab() {
        String[] iconArray = new String[paneList.size()];
        card = new CardLayout();
        cardContainer = new JPanel(card);
        cardContainer.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        for (int i = 0; i < paneList.size(); i++) {
            CellEditorPane pane = paneList.get(i);
            iconArray[i] = pane.getIconPath();
            cardContainer.add(pane, pane.title4PopupWindow());
        }
        tabsHeaderIconPane = new UIHeadGroup(iconArray) {
            @Override
            public void tabChanged(int index) {
                card.show(cardContainer, paneList.get(index).title4PopupWindow());
                paneList.get(index).populate();
            }
        };
        tabsHeaderIconPane.setNeedLeftRightOutLine(false);
    }

    /**
     * 刷新数据列基本和高级设置面板
     */
    private void createPanes() {
        paneList = new ArrayList<>();
        /*基本设置面板*/
        paneList.add(new DSColumnBasicEditorPane());
        /*高级设置面板*/
        paneList.add(new DSColumnAdvancedEditorPane());
    }

    /**
     * 单元格元素 数据列 高级设置内容面板
     *
     * @author yaoh.wu
     * @version 2017年7月25日
     * @since 9.0
     */
    class DSColumnBasicEditorPane extends CellEditorPane {

        //数据集和数据列
        private SelectedDataColumnPane dataPane;
        //数据分组设置
        private ResultSetGroupDockingPane groupPane;
        //条件过滤按钮面板
        private JPanel conditionPane;

        // 分组设置监听器
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
                    groupPane.update();
                    fireTargetModified();
                }
            }
        };
        //数据集列设置监听器
        private ItemListener dataListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    dataPane.update(cellElement);
                    fireTargetModified();
                }
            }
        };

        DSColumnBasicEditorPane() {
            this.setLayout(new BorderLayout());
            dataPane = new SelectedDataColumnPane(true, true);
            groupPane = new ResultSetGroupDockingPane(tc);
            dataPane.addListener(dataListener);
            groupPane.addListener(groupListener);

            double[] rowSize = {P}, columnSize = {P, F};
            UILabel uiLabel = new UILabel("filter");
            UIButton uiButton = new UIButton();
            if (tc != null) {
                //第一次初始化时tc为空，引发NullPointerException
                uiButton = new UIButton(new DSColumnConditionAction(tc));
            }
            Component[][] components = new Component[][]{
                    new Component[]{uiLabel, uiButton}
            };
            conditionPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);

            this.add(this.createContentPane(), BorderLayout.CENTER);
            this.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 15));
        }


        @Override
        public String getIconPath() {
            return "Basic";
        }

        @Override
        public String title4PopupWindow() {
            return "Basic";
        }


        @Override
        public void update() {
            dataPane.update(cellElement);
            groupPane.update();
        }

        @Override
        public void populate() {
            dataPane.populate(null, cellElement);
            groupPane.populate(cellElement);
        }


        /**
         * 创建有内容的面板显示信息
         *
         * @return content JPanel
         */
        private JPanel createContentPane() {

            double[] columnSize = {F};
            double[] rowSize = {P, P, P};
            Component[][] components = new Component[][]{
                    //数据集列选择
                    new Component[]{this.dataPane},
                    //数据分组设置
                    new Component[]{this.groupPane},
                    //条件过滤
                    new Component[]{this.conditionPane}
            };
            return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        }
    }


    class DSColumnAdvancedEditorPane extends CellEditorPane {

        private static final String INSET_TEXT = " ";

        //排列顺序
        private ResultSetSortConfigPane sortPane;
        //结果集筛选
        private ResultSetFilterConfigPane filterPane;
        //自定义值显示
        private CustomValuePane valuePane;
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
            this.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 15));
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
            if (cellElement != null) {
                sortPane.update(cellElement);
                valuePane.update(cellElement);
                filterPane.update(cellElement);
                CellExpandAttr cellExpandAttr = cellElement.getCellExpandAttr();
                if (cellExpandAttr == null) {
                    cellExpandAttr = new CellExpandAttr();
                    cellElement.setCellExpandAttr(cellExpandAttr);
                }
                if (horizontalExtendableCheckBox.isSelected()) {
                    if (verticalExtendableCheckBox.isSelected()) {
                        cellExpandAttr.setExtendable(CellExpandAttr.Both_EXTENDABLE);
                    } else {
                        cellExpandAttr.setExtendable(CellExpandAttr.Horizontal_EXTENDABLE);
                    }
                } else {
                    if (verticalExtendableCheckBox.isSelected()) {
                        cellExpandAttr.setExtendable(CellExpandAttr.Vertical_EXTENDABLE);
                    } else {
                        cellExpandAttr.setExtendable(CellExpandAttr.None_EXTENDABLE);
                    }
                }
                if (this.useMultiplyNumCheckBox.isSelected()) {
                    cellExpandAttr.setMultipleNumber((int) multiNumSpinner.getValue());
                } else {
                    cellExpandAttr.setMultipleNumber(-1);
                }
            }
        }

        @Override
        public void populate() {
            if (cellElement != null) {
                sortPane.populate(cellElement);
                valuePane.populate(cellElement);
                filterPane.populate(cellElement);
                CellExpandAttr cellExpandAttr = cellElement.getCellExpandAttr();
                if (cellExpandAttr == null) {
                    cellExpandAttr = new CellExpandAttr();
                    cellElement.setCellExpandAttr(cellExpandAttr);
                }
                // extendable
                switch (cellExpandAttr.getExtendable()) {
                    case CellExpandAttr.Both_EXTENDABLE:
                        horizontalExtendableCheckBox.setSelected(true);
                        verticalExtendableCheckBox.setSelected(true);
                        break;
                    case CellExpandAttr.Vertical_EXTENDABLE:
                        horizontalExtendableCheckBox.setSelected(false);
                        verticalExtendableCheckBox.setSelected(true);
                        break;
                    case CellExpandAttr.Horizontal_EXTENDABLE:
                        horizontalExtendableCheckBox.setSelected(true);
                        verticalExtendableCheckBox.setSelected(false);
                        break;
                    default: {
                        horizontalExtendableCheckBox.setSelected(false);
                        verticalExtendableCheckBox.setSelected(false);
                    }
                }
                if (cellExpandAttr.getMultipleNumber() == -1) {
                    useMultiplyNumCheckBox.setSelected(false);
                } else {
                    useMultiplyNumCheckBox.setSelected(true);
                    multiNumSpinner.setValue(cellExpandAttr.getMultipleNumber());
                }
                this.checkButtonEnabled();
            }
        }

        /**
         * 创建内容
         */

        private JPanel createContentPane() {
            this.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
            this.setLayout(FRGUIPaneFactory.createBorderLayout());
            //结果集排序

            sortPane = new ResultSetSortConfigPane();
            //结果筛选

            filterPane = new ResultSetFilterConfigPane();
            //自定义值显示
            valuePane = new CustomValuePane();
            //可扩展性
            JPanel extendableDirectionPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            extendableDirectionPane.add(horizontalExtendableCheckBox = new UICheckBox(Inter.getLocText("ExpandD-Horizontal_Extendable")));
            extendableDirectionPane.add(verticalExtendableCheckBox = new UICheckBox(Inter.getLocText("ExpandD-Vertical_Extendable")));


            //补充空白数据
            JPanel multiNumPane = new JPanel();

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


            double[] rowSize = {P, P, P, P, P, P};
            double[] columnSize = {F};

            Component[][] components = new Component[][]{
                    {sortPane},
                    {filterPane},
                    {valuePane},
                    {extendableDirectionPane},
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
         * @see com.fr.design.expand.SortExpandAttrPane
         * @see DSColumnAdvancedPane.SortPane
         */
        protected class ResultSetSortConfigPane extends JPanel {
            //单元格
            private CellElement cellElement;
            //面板
            private UIButtonGroup sort_type_pane;
            private TinyFormulaPane tinyFormulaPane;
            private CardLayout cardLayout;
            private JPanel centerPane;


            public ResultSetSortConfigPane() {
                this.setLayout(new BorderLayout(0, 0));
                Icon[] iconArray = {
                        IOUtils.readIcon("/com/fr/design/images/expand/none16x16.png"),
                        IOUtils.readIcon("/com/fr/design/images/expand/asc.png"),
                        IOUtils.readIcon("/com/fr/design/images/expand/des.png")
                };
                String[] nameArray = {Inter.getLocText("Sort-Original"), Inter.getLocText("Sort-Ascending"), Inter.getLocText("Sort-Descending")};
                sort_type_pane = new UIButtonGroup(iconArray);
                sort_type_pane.setAllToolTips(nameArray);
                sort_type_pane.setGlobalName(Inter.getLocText("ExpandD-Sort_After_Expand"));

                cardLayout = new CardLayout();
                centerPane = new JPanel(cardLayout);
                tinyFormulaPane = new TinyFormulaPane();
                centerPane.add(new JPanel(), "none");
                centerPane.add(tinyFormulaPane, "content");
                //todo 国际化
                UILabel sortLabel = new UILabel("排列顺序");
                sort_type_pane.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        boolean noContent = sort_type_pane.getSelectedIndex() == 0;
                        cardLayout.show(centerPane, noContent ? "none" : "content");
                        if (noContent) {
                            centerPane.setPreferredSize(new Dimension(0, 0));
                        } else {
                            centerPane.setPreferredSize(new Dimension(165, 20));
                        }
                    }
                });

                Component[][] components = new Component[][]{
                        new Component[]{sortLabel, sort_type_pane},
                        new Component[]{null, centerPane}
                };

                double[] rowSize = {P, P}, columnSize = {P, F};
                this.add(TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize), BorderLayout.CENTER);
            }


            /**
             * 刷新面板信息
             *
             * @param cellElement 单元格
             */
            public void populate(TemplateCellElement cellElement) {
                if (cellElement != null) {
                    this.cellElement = cellElement;
                    Object value = cellElement.getValue();
                    if (value != null && value instanceof DSColumn) {
                        DSColumn dSColumn = (DSColumn) value;
                        int sort = dSColumn.getOrder();
                        this.sort_type_pane.setSelectedIndex(sort);
                        boolean noContent = sort_type_pane.getSelectedIndex() == 0;
                        cardLayout.show(centerPane, noContent ? "none" : "content");
                        if (noContent) {
                            centerPane.setPreferredSize(new Dimension(0, 0));
                        } else {
                            centerPane.setPreferredSize(new Dimension(165, 20));
                        }
                        String sortFormula = dSColumn.getSortFormula();
                        if (sortFormula != null && sortFormula.length() >= 1) {
                            this.tinyFormulaPane.populateBean(sortFormula);
                        }
                    }
                }
            }

            /**
             * 保存面板配置信息
             *
             * @param cellElement 单元格
             */
            public void update(CellElement cellElement) {
                if (cellElement != null) {
                    Object value = cellElement.getValue();
                    if (value != null && value instanceof DSColumn) {
                        DSColumn dSColumn = (DSColumn) (cellElement.getValue());
                        dSColumn.setOrder(this.sort_type_pane.getSelectedIndex());
                        dSColumn.setSortFormula(this.tinyFormulaPane.updateBean());
                    }
                }
            }
        }

        /**
         * 单元格元素>数据集>高级设置>结果集筛选设置面板
         *
         * @see DSColumnAdvancedPane.SelectCountPane
         */
        protected class ResultSetFilterConfigPane extends JPanel {

            private CellElement cellElement;
            private UIComboBox rsComboBox;
            private JPanel setCardPane;
            private JPanel tipCardPane;
            private UITextField serialTextField;
            private JFormulaField topFormulaPane;
            private JFormulaField bottomFormulaPane;

            public ResultSetFilterConfigPane() {


                this.setLayout(FRGUIPaneFactory.createBorderLayout());
                UILabel filterLabel = new UILabel("结果集筛选");

                //结果集筛选下拉框
                rsComboBox = new UIComboBox(new String[]{
                        Inter.getLocText("Undefined"),
                        Inter.getLocText("BindColumn-Top_N"),
                        Inter.getLocText("BindColumn-Bottom_N"),
                        Inter.getLocText("Odd"),
                        Inter.getLocText("Even"),
                        Inter.getLocText("Specify")
                });
                rsComboBox.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        int selectIndex = rsComboBox.getSelectedIndex();
                        CardLayout setCardPaneLayout = (CardLayout) setCardPane.getLayout();
                        CardLayout tipCardPaneLayout = (CardLayout) tipCardPane.getLayout();
                        if (selectIndex == 1) {
                            setCardPaneLayout.show(setCardPane, FilterType.TOP.name());
                            tipCardPaneLayout.show(tipCardPane, FilterType.TOP.name());
                            //隐藏tip
                        } else if (selectIndex == 2) {
                            setCardPaneLayout.show(setCardPane, FilterType.BOTTOM.name());
                            tipCardPaneLayout.show(tipCardPane, FilterType.BOTTOM.name());
                            //隐藏tip
                        } else if (selectIndex == 3) {
                            setCardPaneLayout.show(setCardPane, FilterType.ODD.name());
                            tipCardPaneLayout.show(tipCardPane, FilterType.ODD.name());
                            //隐藏set
                        } else if (selectIndex == 4) {
                            setCardPaneLayout.show(setCardPane, FilterType.EVEN.name());
                            tipCardPaneLayout.show(tipCardPane, FilterType.EVEN.name());
                            //隐藏set
                        } else if (selectIndex == 5) {
                            setCardPaneLayout.show(setCardPane, FilterType.SPECIFY.name());
                            tipCardPaneLayout.show(tipCardPane, FilterType.SPECIFY.name());
                        } else {
                            setCardPaneLayout.show(setCardPane, FilterType.UNDEFINE.name());
                            tipCardPaneLayout.show(tipCardPane, FilterType.UNDEFINE.name());
                            //隐藏set和tip
                        }
                    }
                });
                //配置展示CardLayout
                setCardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
                //提示信息展示CardLayout
                tipCardPane = FRGUIPaneFactory.createCardLayout_S_Pane();

                //前N个
                topFormulaPane = new DSColumnAdvancedEditorPane.JFormulaField("=");
                setCardPane.add(topFormulaPane, FilterType.TOP.name());
                tipCardPane.add(new JPanel(), FilterType.TOP.name());

                //后N个
                bottomFormulaPane = new DSColumnAdvancedEditorPane.JFormulaField("=");
                setCardPane.add(bottomFormulaPane, FilterType.BOTTOM.name());
                tipCardPane.add(new JPanel(), FilterType.BOTTOM.name());

                //自定义值下方没有提示信息，也没有输入框
                setCardPane.add(new JPanel(), FilterType.UNDEFINE.name());
                tipCardPane.add(new JPanel(), FilterType.UNDEFINE.name());

                //奇数 UILabel 占一行作为提示信息
                setCardPane.add(new JPanel(), FilterType.ODD.name());
                tipCardPane.add(new UILabel(Inter.getLocText("BindColumn-Result_Serial_Number_Start_From_1")
                        + "," + Inter.getLocText("BindColumn-Odd_Selected_(1,3,5...)")), "ODD");


                //偶数 UILabel 占一行作为提示信息
                setCardPane.add(new JPanel(), FilterType.EVEN.name());
                tipCardPane.add(new UILabel(Inter.getLocText("BindColumn-Result_Serial_Number_Start_From_1")
                        + "," + Inter.getLocText("BindColumn-Even_Selected_(2,4,6...)")), "ODD");

                //输入框占用右半边，提示信息占一行
                serialTextField = new UITextField(16);
                setCardPane.add(serialTextField, FilterType.SPECIFY.name());
                tipCardPane.add(new UILabel(
                        Inter.getLocText(new String[]{
                                        "Format", "BindColumn-Result_Serial_Number_Start_From_1", "Inner_Parameter", "Group_Count"},
                                new String[]{": 1,2-3,5,8  ", ",", "$__count__"})), "SPECIFY");

                this.add(TableLayoutHelper.createTableLayoutPane(new Component[][]{
                        {filterLabel, rsComboBox},
                        {null, setCardPane},
                        {tipCardPane, null}
                }, new double[]{P, P, P}, new double[]{P, F}), BorderLayout.CENTER);
            }

            public void populate(CellElement cellElement) {
                if (cellElement != null) {
                    this.cellElement = cellElement;
                    Object value = cellElement.getValue();
                    if (value != null && value instanceof DSColumn) {
                        DSColumn dSColumn = (DSColumn) (cellElement.getValue());
                        SelectCount selectCount = dSColumn.getSelectCount();
                        this.topFormulaPane.populateElement(cellElement);
                        this.bottomFormulaPane.populateElement(cellElement);
                        if (selectCount != null) {
                            int selectCountType = selectCount.getType();
                            this.rsComboBox.setSelectedIndex(selectCountType);
                            switch (selectCountType) {
                                case SelectCount.TOP:
                                    this.topFormulaPane.populate(selectCount.getFormulaCount());
                                    break;
                                case SelectCount.BOTTOM:
                                    this.bottomFormulaPane.populate(selectCount.getFormulaCount());
                                    break;
                                case SelectCount.SPECIFY:
                                    this.serialTextField.setText(selectCount.getSerial());
                                    break;
                                case SelectCount.EVEN:
                                    break;
                                case SelectCount.ODD:
                                    break;
                                default:
                            }
                        }
                    }
                }
            }

            public void update(CellElement cellElement) {
                if (cellElement != null) {
                    Object value = cellElement.getValue();
                    if (value != null && value instanceof DSColumn) {
                        DSColumn dSColumn = (DSColumn) (cellElement.getValue());
                        int selectedFilterIndex = this.rsComboBox.getSelectedIndex();
                        if (selectedFilterIndex == 0) {
                            dSColumn.setSelectCount(null);
                        } else {
                            SelectCount selectCount = new SelectCount();
                            selectCount.setType(selectedFilterIndex);
                            dSColumn.setSelectCount(selectCount);
                            if (selectedFilterIndex == SelectCount.TOP) {
                                selectCount.setFormulaCount(this.topFormulaPane.getFormulaText());
                            } else if (selectedFilterIndex == SelectCount.BOTTOM) {
                                selectCount.setFormulaCount(this.bottomFormulaPane.getFormulaText());
                            } else if (selectedFilterIndex == SelectCount.SPECIFY) {
                                selectCount.setSerial(this.serialTextField.getText());
                            }
                        }
                    }
                }
            }
        }

        /**
         * 单元格元素>数据集>高级设置>公式输入框
         *
         * @see DSColumnAdvancedPane.JFormulaField
         */
        private class JFormulaField extends JPanel {
            private CellElement cellElement;
            private UITextField formulaTextField;
            private String defaultValue;

            public JFormulaField(String defaultValue) {

                double[] columnSize = {F};
                double[] rowSize = {P};
                this.defaultValue = defaultValue;
                formulaTextField = new UITextField();
                formulaTextField.setText(defaultValue);
                UIButton formulaButton = new UIButton(IOUtils.readIcon("/com/fr/design/images/m_insert/formula.png"));
                formulaButton.setToolTipText(Inter.getLocText("Formula") + "...");
                formulaButton.setPreferredSize(new Dimension(24, formulaTextField.getPreferredSize().height));
                formulaButton.addActionListener(formulaButtonActionListener);
                Component[] buttonComponent = new Component[]{
                        formulaButton
                };
                JPanel pane = new JPanel(new BorderLayout(0, 0));
                pane.add(formulaTextField, BorderLayout.CENTER);
                pane.add(GUICoreUtils.createFlowPane(buttonComponent, FlowLayout.LEFT, LayoutConstants.HGAP_LARGE), BorderLayout.EAST);
                Component[][] components = new Component[][]{
                        new Component[]{pane}
                };
                JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
                this.setLayout(new BorderLayout());
                this.add(panel, BorderLayout.CENTER);
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
                    if (cellElement != null) {
                        Object value = cellElement.getValue();
                        if (value != null && value instanceof DSColumn) {
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
                    }
                }
            };
        }

        /**
         * 单元格元素>数据集>高级设置>自定义值显示设置面板
         *
         * @see DSColumnAdvancedPane.ValuePane
         */
        private class CustomValuePane extends JPanel {
            private JFormulaField formulaField;

            public CustomValuePane() {

                this.setLayout(FRGUIPaneFactory.createBoxFlowLayout());
                UILabel customValueLabel = new UILabel("显示值");
                formulaField = new JFormulaField("$$$");
                formulaField.setPreferredSize(new Dimension(159, 20));
                this.add(TableLayoutHelper.createTableLayoutPane(new Component[][]{
                        {customValueLabel, formulaField},
                }, new double[]{P}, new double[]{P, F}), BorderLayout.CENTER);
            }

            public void populate(CellElement cellElement) {
                if (cellElement != null) {
                    Object value = cellElement.getValue();
                    if (value != null && value instanceof DSColumn) {
                        DSColumn dSColumn = (DSColumn) value;

                        //formula
                        String valueFormula = dSColumn.getResult();
                        if (valueFormula == null) {
                            valueFormula = "$$$";
                        }
                        formulaField.populateElement(cellElement);
                        formulaField.populate(valueFormula);
                    }
                }
            }

            public void update(CellElement cellElement) {
                if (cellElement != null) {
                    Object value = cellElement.getValue();
                    if (value != null && value instanceof DSColumn) {
                        DSColumn dSColumn = (DSColumn) (cellElement.getValue());
                        dSColumn.setResult(this.formulaField.getFormulaText());
                    }
                }
            }
        }
    }
}