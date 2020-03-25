package com.fr.quickeditor.cellquick;

import com.fr.base.BaseFormula;
import com.fr.design.actions.columnrow.DSColumnConditionAction;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.actions.insert.cell.DSColumnCellAction;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dscolumn.DSColumnAdvancedPane;
import com.fr.design.dscolumn.ResultSetGroupDockingPane;
import com.fr.design.dscolumn.SelectedDataColumnPane;
import com.fr.design.event.UIObserverListener;
import com.fr.design.formula.CustomVariableResolver;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ibutton.UIHeadGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.MultilineLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.cell.AbstractDSCellEditorPane;
import com.fr.design.utils.gui.UIComponentUtils;
import com.fr.design.widget.FRWidgetFactory;
import com.fr.general.IOUtils;
import com.fr.quickeditor.CellQuickEditor;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.report.cell.cellattr.core.group.DSColumn;
import com.fr.report.cell.cellattr.core.group.FilterTypeEnum;
import com.fr.report.cell.cellattr.core.group.SelectCount;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import static com.fr.report.cell.cellattr.core.group.FilterTypeEnum.BOTTOM;
import static com.fr.report.cell.cellattr.core.group.FilterTypeEnum.EVEN;
import static com.fr.report.cell.cellattr.core.group.FilterTypeEnum.ODD;
import static com.fr.report.cell.cellattr.core.group.FilterTypeEnum.SPECIFY;
import static com.fr.report.cell.cellattr.core.group.FilterTypeEnum.TOP;
import static com.fr.report.cell.cellattr.core.group.FilterTypeEnum.UNDEFINE;


/**
 * 单元格元素 数据列编辑器
 *
 * @author yaoh.wu
 * @version 2017年7月24日
 * @since 9.0
 */
public class CellDSColumnEditor extends CellQuickEditor {
    private static final double P = TableLayout.PREFERRED, F = TableLayout.FILL;
    private static final Color TIP_FONT_COLOR = new Color(0x7F333334, true);


    /**
     * 基本和高级设置
     */
    private ArrayList<AbstractDSCellEditorPane> paneList;
    /**
     * 基本和高级设置 卡片布局
     */
    private CardLayout card;
    /**
     * 基本和高级设置 容器面板
     */
    private JPanel cardContainer;
    /**
     * 卡片布局TAB切换按钮
     */
    private UIHeadGroup tabsHeaderIconPane;


    /**
     * 数据列基本设置
     */
    private DSColumnBasicEditorPane cellDSColumnBasicPane;
    /**
     * 数据列高级设置
     */
    private DSColumnAdvancedEditorPane cellDSColumnAdvancedPane;

    public CellDSColumnEditor() {
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
        JPanel dsColumnRegion = new JPanel(new BorderLayout());
        dsColumnRegion.add(tabsHeaderIconPane, BorderLayout.NORTH);
        dsColumnRegion.add(cardContainer, BorderLayout.CENTER);
        JPanel centerPane = new JPanel(new BorderLayout());
        centerPane.add(dsColumnRegion, BorderLayout.CENTER);
        return centerPane;
    }

    @Override
    public boolean isScrollAll() {
        return false;
    }

    /**
     * 刷新详细信息面板
     */
    @Override
    protected void refreshDetails() {
        cellDSColumnBasicPane.populate();
        cellDSColumnAdvancedPane.populate();
        this.validate();
    }


    /**
     * 初始化基本和高级设置切换tab
     */
    private void createSwitchTab() {
        String[] iconArray = new String[paneList.size()];
        card = new CardLayout();
        cardContainer = new JPanel(card);
        for (int i = 0; i < paneList.size(); i++) {
            AbstractDSCellEditorPane pane = paneList.get(i);
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
     * 数据列基本和高级设置面板
     */
    private void createPanes() {
        paneList = new ArrayList<>();
        /*基本设置面板*/
        cellDSColumnBasicPane = new DSColumnBasicEditorPane();
        paneList.add(cellDSColumnBasicPane);
        /*高级设置面板*/
        cellDSColumnAdvancedPane = new DSColumnAdvancedEditorPane();
        paneList.add(cellDSColumnAdvancedPane);
    }

    @Override
    public void release() {
        super.release();
        cellDSColumnBasicPane.release();
        cellDSColumnAdvancedPane.release();
    }

    /**
     * 单元格元素 数据列 高级设置内容面板
     *
     * @author yaoh.wu
     * @version 2017年7月25日
     * @since 9.0
     */
    class DSColumnBasicEditorPane extends AbstractDSCellEditorPane {

        /**
         * 数据集和数据列
         */
        private SelectedDataColumnPane dataPane;
        /**
         * 数据分组设置
         */
        private ResultSetGroupDockingPane groupPane;
        /**
         * 条件过滤按钮面板
         */
        private JPanel conditionPane;

        /**
         * 条件过滤按钮触发动作
         */
        private DSColumnConditionAction condition;

        /**
         * 条件过滤按钮
         */
        private UIButton conditionUIButton;

        /**
         * 分组设置监听器
         */
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
        /**
         * 数据集列设置监听器
         */
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
            dataPane = new SelectedDataColumnPane(true, true);
            groupPane = new ResultSetGroupDockingPane();
            dataPane.setListener(dataListener);
            groupPane.setListener(groupListener);

            double[] rowSize = {P}, columnSize = {P, F};
            UILabel uiLabel = FRWidgetFactory.createLineWrapLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Filter_Conditions"));
            condition = new DSColumnConditionAction();
            if (tc != null) {
                condition.setEditingComponent(tc);
            }
            //丢掉icon,修改按钮名称为编辑
            condition.setSmallIcon(null);
            condition.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Edit"));
            conditionUIButton = new UIButton(condition);
            Component[][] components = new Component[][]{
                    new Component[]{uiLabel, UIComponentUtils.wrapWithBorderLayoutPane(conditionUIButton)}
            };
            conditionPane = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, HGAP, VGAP);
            this.createScrollPane();
            this.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        }


        @Override
        public String getIconPath() {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Basic");
        }

        @Override
        public String title4PopupWindow() {
            return com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Basic");
        }


        @Override
        public void update() {
            dataPane.update(cellElement);
            groupPane.update();
        }

        @Override
        public void populate() {
            dataPane.populate(DesignTableDataManager.getEditingTableDataSource(), cellElement, tc);
            groupPane.populate(cellElement);
            if (tc != null) {
                condition.setEditingComponent(tc);
            }
        }

        @Override
        protected void release() {
            condition.setEditingComponent(null);
            dataPane.release();
        }


        /**
         * 创建有内容的面板显示信息
         *
         * @return content JPanel
         */
        @Override
        protected JPanel createContentPane() {

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
            return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, HGAP, VGAP);
        }
    }


    class DSColumnAdvancedEditorPane extends AbstractDSCellEditorPane {

        /*pane begin*/
        /**
         * 排列顺序
         */
        private ResultSetSortConfigPane sortPane;
        /**
         * 结果集筛选
         */
        private ResultSetFilterConfigPane filterPane;
        /**
         * 自定义值显示
         */
        private CustomValuePane valuePane;
        /**
         * 横向可扩展性
         */
        private UICheckBox heCheckBox;
        /**
         * 纵向可扩展性
         */
        private UICheckBox veCheckBox;
        /**
         * 补充空白数据
         */
        private UICheckBox useMultiNumCheckBox;
        /**
         * 补充空白数据数目输入框
         */
        private UISpinner multiNumSpinner;
        /**
         * 补充空白数据数目面板 可隐藏
         */
        private JPanel multiPane;
        /*pane end*/


        /*listeners begin*/
        private UIObserverListener sortPaneFormulaChangeListener = new UIObserverListener() {
            @Override
            public void doChange() {
                sortPane.update(cellElement);
                fireTargetModified();
            }
        };

        private ChangeListener sortTypeBtnGroupChangeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                sortPane.update(cellElement);
                fireTargetModified();
            }
        };

        private UIObserverListener filterPaneChangeListener = new UIObserverListener() {
            @Override
            public void doChange() {
                filterPane.update(cellElement);
                fireTargetModified();
            }
        };

        private UIObserverListener customValuePaneChangeListener = new UIObserverListener() {
            @Override
            public void doChange() {
                valuePane.update(cellElement);
                fireTargetModified();
            }
        };

        private ChangeListener heCheckBoxChangeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                cellDSColumnAdvancedPane.updateExtendConfig();
                fireTargetModified();
            }
        };

        private ChangeListener veCheckBoxChangeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                cellDSColumnAdvancedPane.updateExtendConfig();
                fireTargetModified();
            }
        };

        private ActionListener useMultiNumCheckBoxChangeListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkButtonEnabled();
                cellDSColumnAdvancedPane.updateMultipleConfig();
                fireTargetModified();
            }
        };

        private ChangeListener multiNumSpinnerChangeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                cellDSColumnAdvancedPane.updateMultipleConfig();
                fireTargetModified();
            }
        };
        /*listeners end*/


        public DSColumnAdvancedEditorPane() {
            this.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            this.createScrollPane();
        }


        @Override
        public String getIconPath() {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Advanced");
        }

        @Override
        public String title4PopupWindow() {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Advanced");
        }


        @Override
        public void update() {
            if (cellElement != null) {
                sortPane.update(cellElement);
                valuePane.update(cellElement);
                filterPane.update(cellElement);
                //更新单元格扩展属性
                updateExtendConfig();
                //更新补充空白设置
                updateMultipleConfig();
            }
        }

        @Override
        public void populate() {
            if (cellElement != null) {
                disableListener();
                sortPane.populate(cellElement);
                valuePane.populate(cellElement);
                filterPane.populate(cellElement);
                CellExpandAttr cellExpandAttr = cellElement.getCellExpandAttr();
                if (cellExpandAttr == null) {
                    cellExpandAttr = new CellExpandAttr();
                    cellElement.setCellExpandAttr(cellExpandAttr);
                }
                // extendable
                //noinspection Duplicates
                switch (cellExpandAttr.getExtendable()) {
                    case CellExpandAttr.Both_EXTENDABLE:
                        heCheckBox.setSelected(true);
                        veCheckBox.setSelected(true);
                        break;
                    case CellExpandAttr.Vertical_EXTENDABLE:
                        heCheckBox.setSelected(false);
                        veCheckBox.setSelected(true);
                        break;
                    case CellExpandAttr.Horizontal_EXTENDABLE:
                        heCheckBox.setSelected(true);
                        veCheckBox.setSelected(false);
                        break;
                    default: {
                        heCheckBox.setSelected(false);
                        veCheckBox.setSelected(false);
                    }
                }
                if (cellExpandAttr.getMultipleNumber() == -1) {
                    useMultiNumCheckBox.setSelected(false);
                    // 默认值
                    multiNumSpinner.setValue(1);
                } else {
                    useMultiNumCheckBox.setSelected(true);
                    multiNumSpinner.setValue(cellExpandAttr.getMultipleNumber());
                }
                this.checkButtonEnabled();
                enableListener();
            }
        }

        @Override
        protected void release() {

        }

        /**
         * 更新单元格扩展属性
         */
        private void updateExtendConfig() {
            CellExpandAttr cellExpandAttr = cellElement.getCellExpandAttr();
            if (cellExpandAttr == null) {
                cellExpandAttr = new CellExpandAttr();
                cellElement.setCellExpandAttr(cellExpandAttr);
            }
            //noinspection Duplicates
            if (heCheckBox.isSelected()) {
                if (veCheckBox.isSelected()) {
                    cellExpandAttr.setExtendable(CellExpandAttr.Both_EXTENDABLE);
                } else {
                    cellExpandAttr.setExtendable(CellExpandAttr.Horizontal_EXTENDABLE);
                }
            } else {
                if (veCheckBox.isSelected()) {
                    cellExpandAttr.setExtendable(CellExpandAttr.Vertical_EXTENDABLE);
                } else {
                    cellExpandAttr.setExtendable(CellExpandAttr.None_EXTENDABLE);
                }
            }
        }

        /**
         * 更新补充空白处的配置
         */
        private void updateMultipleConfig() {
            CellExpandAttr cellExpandAttr = cellElement.getCellExpandAttr();
            if (this.useMultiNumCheckBox.isSelected()) {
                cellExpandAttr.setMultipleNumber((int) multiNumSpinner.getValue());
            } else {
                cellExpandAttr.setMultipleNumber(-1);
            }
        }

        /**
         * 创建内容
         *
         * @return 内容面板
         */
        @Override
        protected JPanel createContentPane() {
            this.setLayout(FRGUIPaneFactory.createBorderLayout());

            //结果集排序
            sortPane = new ResultSetSortConfigPane();

            //结果筛选
            filterPane = new ResultSetFilterConfigPane();

            //自定义值显示
            valuePane = new CustomValuePane();

            //可扩展性
            JPanel extendableDirectionPane = FRGUIPaneFactory.createYBoxEmptyBorderPane();
            extendableDirectionPane.add(heCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ExpandD_Horizontal_Extendable")));
            extendableDirectionPane.add(veCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ExpandD_Vertical_Extendable")));

            //补充空白数据
            JPanel multiNumPane = FRGUIPaneFactory.createYBoxEmptyBorderPane();
            useMultiNumCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Fill_Blank_Data"));
            JPanel checkBoxPane = new JPanel(new BorderLayout());
            checkBoxPane.add(useMultiNumCheckBox, BorderLayout.WEST);
            multiNumPane.add(checkBoxPane);
            multiNumSpinner = new UISpinner(1, 10000, 1, 1);

            //数据倍数
            UILabel multipleLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Column_Multiple"));
            multiPane = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{
                            new Component[]{
                                    multipleLabel, multiNumSpinner
                            }
                    }, new double[]{P}, new double[]{P, F}, HGAP, VGAP
            );
            multiPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
            multiNumPane.add(multiPane);

            enableListener();

            double[] rowSize = {P, P, P, P, P, P};
            double[] columnSize = {F};
            Component[][] components = new Component[][]{
                    {sortPane},
                    {filterPane},
                    {valuePane},
                    {extendableDirectionPane},
                    {multiNumPane}
            };
            return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, HGAP, VGAP);
        }

        public void enableListener() {
            sortPane.addListener(sortPaneFormulaChangeListener, sortTypeBtnGroupChangeListener);
            filterPane.addListener(filterPaneChangeListener);
            valuePane.addListener(customValuePaneChangeListener);
            heCheckBox.addChangeListener(heCheckBoxChangeListener);
            veCheckBox.addChangeListener(veCheckBoxChangeListener);
            useMultiNumCheckBox.addActionListener(useMultiNumCheckBoxChangeListener);
            multiNumSpinner.addChangeListener(multiNumSpinnerChangeListener);
        }

        public void disableListener() {
            sortPane.removeListener(sortTypeBtnGroupChangeListener);
            filterPane.removeListener();
            valuePane.removeListener();
            heCheckBox.removeChangeListener(heCheckBoxChangeListener);
            veCheckBox.removeChangeListener(veCheckBoxChangeListener);
            useMultiNumCheckBox.removeActionListener(useMultiNumCheckBoxChangeListener);
            multiNumSpinner.removeChangeListener(multiNumSpinnerChangeListener);
        }


        private void checkButtonEnabled() {
            if (useMultiNumCheckBox.isSelected()) {
                multiNumSpinner.setEnabled(true);
                multiPane.setVisible(true);
            } else {
                multiNumSpinner.setEnabled(false);
                multiPane.setVisible(false);
            }
        }

        /**
         * 单元格元素>数据集>高级设置>结果排序设置面板
         *
         * @see com.fr.design.expand.SortExpandAttrPane
         * @see DSColumnAdvancedPane.SortPane
         */
        public class ResultSetSortConfigPane extends JPanel {
            private static final String DEFAULT_VALUE = "=";
            private JPanel contentPane;
            private UIButtonGroup sortTypePane;
            private JFormulaField formulaField;
            private CardLayout cardLayout;
            private JPanel centerPane;


            public ResultSetSortConfigPane() {
                this.setLayout(new BorderLayout());
                Icon[] iconArray = {
                        IOUtils.readIcon("/com/fr/design/images/expand/none16x16.png"),
                        IOUtils.readIcon("/com/fr/design/images/expand/asc.png"),
                        IOUtils.readIcon("/com/fr/design/images/expand/des.png")
                };
                String[] nameArray = {com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Sort_Original"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Sort_Ascending"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Sort_Descending")};
                sortTypePane = new UIButtonGroup(iconArray);
                sortTypePane.setAllToolTips(nameArray);
                sortTypePane.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_ExpandD_Sort_After_Expand"));

                cardLayout = new CardLayout();
                centerPane = new JPanel(cardLayout);
                formulaField = new JFormulaField(DEFAULT_VALUE);
                centerPane.add(new JPanel(), "none");
                centerPane.add(formulaField, "content");
                UILabel sortLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Sort_Sort_Order"));
                sortLabel.setPreferredSize(LABEL_DIMENSION);
                sortTypePane.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        boolean noContent = sortTypePane.getSelectedIndex() == 0;
                        cardLayout.show(centerPane, noContent ? "none" : "content");
                        if (noContent) {
                            centerPane.setPreferredSize(new Dimension(0, 0));
                            TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 2, 0);
                        } else {
                            centerPane.setPreferredSize(new Dimension(165, 20));
                            TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 2, VGAP);
                        }
                    }
                });

                Component[][] components = new Component[][]{
                        new Component[]{sortLabel, sortTypePane},
                        new Component[]{null, centerPane}
                };

                double[] rowSize = {P, P}, columnSize = {P, F};
                contentPane = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, HGAP, VGAP);
                this.add(contentPane, BorderLayout.CENTER);
            }


            /**
             * 刷新面板信息
             *
             * @param cellElement 单元格
             */
            public void populate(TemplateCellElement cellElement) {
                if (cellElement != null) {
                    Object value = cellElement.getValue();
                    if (value instanceof DSColumn) {
                        this.formulaField.populateElement(cellElement);
                        DSColumn dSColumn = (DSColumn) value;
                        int sort = dSColumn.getOrder();
                        this.sortTypePane.setSelectedIndex(sort);
                        boolean noContent = sortTypePane.getSelectedIndex() == 0;
                        cardLayout.show(centerPane, noContent ? "none" : "content");
                        if (noContent) {
                            centerPane.setPreferredSize(new Dimension(0, 0));
                            TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 2, 0);
                        } else {
                            centerPane.setPreferredSize(new Dimension(156, 20));
                            TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 2, VGAP);
                        }
                        String sortFormula = dSColumn.getSortFormula();
                        if (sortFormula != null && sortFormula.length() >= 1) {
                            this.formulaField.populate(sortFormula);
                        } else {
                            this.formulaField.populate(DEFAULT_VALUE);
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
                    if (value instanceof DSColumn) {
                        DSColumn dSColumn = (DSColumn) value;
                        dSColumn.setOrder(this.sortTypePane.getSelectedIndex());
                        dSColumn.setSortFormula(this.formulaField.getFormulaText());
                    }
                }
            }

            /**
             * 添加事件监听器
             *
             * @param formulaChangeListener 公式输入框改动事件监听器
             * @param changeListener        排序类型下拉框改动事件监听器
             */
            public void addListener(UIObserverListener formulaChangeListener, ChangeListener changeListener) {
                formulaField.addListener(formulaChangeListener);
                sortTypePane.addChangeListener(changeListener);
            }

            /**
             * 去除事件监听器
             *
             * @param changeListener 排序类型下拉框改动事件监听器
             */
            public void removeListener(ChangeListener changeListener) {
                formulaField.removeListener();
                sortTypePane.removeChangeListener(changeListener);
            }
        }

        /**
         * 单元格元素>数据集>高级设置>结果集筛选设置面板
         *
         * @see DSColumnAdvancedPane.SelectCountPane
         */
        public class ResultSetFilterConfigPane extends JPanel {
            private static final String DEFAULT_VALUE = "=";

            private JPanel contentPane;
            private UIComboBox rsComboBox;
            private JPanel setCardPane;
            private JPanel tipCardPane;
            private UITextField serialTextField;
            private JFormulaField topFormulaPane;
            private JFormulaField bottomFormulaPane;

            private ActionListener actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    int selectIndex = rsComboBox.getSelectedIndex();
                    CardLayout setCardPaneLayout = (CardLayout) setCardPane.getLayout();
                    CardLayout tipCardPaneLayout = (CardLayout) tipCardPane.getLayout();
                    if (selectIndex == TOP.getValue()) {
                        //前N个
                        setCardPaneLayout.show(setCardPane, TOP.name());
                        tipCardPaneLayout.show(tipCardPane, TOP.name());
                        //隐藏tip 显示set
                        setCardPane.setPreferredSize(new Dimension(156, 20));
                        TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 2, VGAP);
                        tipCardPane.setPreferredSize(new Dimension(0, 0));
                        TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 4, 0);
                    } else if (selectIndex == BOTTOM.getValue()) {
                        //后N个
                        setCardPaneLayout.show(setCardPane, BOTTOM.name());
                        tipCardPaneLayout.show(tipCardPane, BOTTOM.name());
                        //隐藏tip 显示set
                        setCardPane.setPreferredSize(new Dimension(156, 20));
                        TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 2, VGAP);
                        tipCardPane.setPreferredSize(new Dimension(0, 0));
                        TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 4, 0);
                    } else if (selectIndex == ODD.getValue()) {
                        //奇数
                        setCardPaneLayout.show(setCardPane, ODD.name());
                        tipCardPaneLayout.show(tipCardPane, ODD.name());
                        //隐藏set 显示tip
                        setCardPane.setPreferredSize(new Dimension(0, 0));
                        TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 2, 0);
                        tipCardPane.setPreferredSize(new Dimension(224, 40));
                        TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 4, VGAP_INNER);
                    } else if (selectIndex == EVEN.getValue()) {
                        //偶数
                        setCardPaneLayout.show(setCardPane, EVEN.name());
                        tipCardPaneLayout.show(tipCardPane, EVEN.name());
                        //隐藏set 显示tip
                        setCardPane.setPreferredSize(new Dimension(0, 0));
                        TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 2, 0);
                        tipCardPane.setPreferredSize(new Dimension(224, 40));
                        TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 4, VGAP_INNER);
                    } else if (selectIndex == SPECIFY.getValue()) {
                        //指定
                        setCardPaneLayout.show(setCardPane, SPECIFY.name());
                        tipCardPaneLayout.show(tipCardPane, SPECIFY.name());
                        //显示set和tip
                        setCardPane.setPreferredSize(new Dimension(156, 20));
                        TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 2, VGAP);
                        tipCardPane.setPreferredSize(new Dimension(224, 40));
                        TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 4, VGAP_INNER);
                    } else {
                        //未定义
                        setCardPaneLayout.show(setCardPane, UNDEFINE.name());
                        tipCardPaneLayout.show(tipCardPane, UNDEFINE.name());
                        //隐藏set和tip
                        setCardPane.setPreferredSize(new Dimension(0, 0));
                        TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 2, 0);
                        tipCardPane.setPreferredSize(new Dimension(0, 0));
                        TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 4, 0);
                    }
                }
            };

            public ResultSetFilterConfigPane() {
                this.setLayout(FRGUIPaneFactory.createBorderLayout());
                UILabel filterLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Bind_Column_Results_Filter"));
                //结果集筛选下拉框
                rsComboBox = new UIComboBox(new String[]{
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Undefined"),
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Bind_Column_Top_N"),
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Bind_Column_Bottom_N"),
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Odd"),
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Even"),
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Specify")
                });
                rsComboBox.addActionListener(actionListener);
                //配置展示CardLayout
                setCardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
                //提示信息展示CardLayout
                tipCardPane = FRGUIPaneFactory.createCardLayout_S_Pane();

                //前N个
                topFormulaPane = new JFormulaField(DEFAULT_VALUE);
                setCardPane.add(topFormulaPane, TOP.name());
                tipCardPane.add(new JPanel(), TOP.name());

                //后N个
                bottomFormulaPane = new JFormulaField(DEFAULT_VALUE);
                setCardPane.add(bottomFormulaPane, BOTTOM.name());
                tipCardPane.add(new JPanel(), BOTTOM.name());

                //自定义值下方没有提示信息，也没有输入框
                setCardPane.add(new JPanel(), UNDEFINE.name());
                tipCardPane.add(new JPanel(), UNDEFINE.name());

                //奇数 UILabel 占一行作为提示信息
                setCardPane.add(new JPanel(), ODD.name());
                MultilineLabel oddTip = new MultilineLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_DS_Filter_Odd_Tip"));
                oddTip.setForeground(TIP_FONT_COLOR);
                tipCardPane.add(oddTip, ODD.name());

                //偶数 UILabel 占一行作为提示信息
                setCardPane.add(new JPanel(), EVEN.name());
                MultilineLabel evenTip = new MultilineLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_DS_Filter_Even_Tip"));
                evenTip.setForeground(TIP_FONT_COLOR);
                tipCardPane.add(evenTip, EVEN.name());

                //输入框占用右半边，提示信息占一行
                serialTextField = new UITextField(16);
                setCardPane.add(serialTextField, SPECIFY.name());
                MultilineLabel specifyTip = new MultilineLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_DS_Filter_Specify_Tip"));
                specifyTip.setForeground(TIP_FONT_COLOR);
                tipCardPane.add(specifyTip, SPECIFY.name());
                contentPane = TableLayoutHelper.createDiffVGapTableLayoutPane(new Component[][]{
                        {filterLabel, rsComboBox},
                        {null, setCardPane},
                        {tipCardPane, null}
                }, new double[]{P, P, P}, new double[]{P, F}, HGAP, new double[]{VGAP, VGAP_INNER});

                this.add(contentPane, BorderLayout.CENTER);
            }

            public void populate(CellElement cellElement) {
                rsComboBox.removeActionListener(actionListener);
                if (cellElement != null) {
                    Object value = cellElement.getValue();
                    if (value instanceof DSColumn) {
                        DSColumn dSColumn = (DSColumn) (cellElement.getValue());
                        SelectCount selectCount = dSColumn.getSelectCount();
                        this.topFormulaPane.populateElement(cellElement);
                        this.bottomFormulaPane.populateElement(cellElement);
                        CardLayout setCardPaneLayout = (CardLayout) setCardPane.getLayout();
                        CardLayout tipCardPaneLayout = (CardLayout) tipCardPane.getLayout();
                        // 重置默认值
                        this.topFormulaPane.populate(DEFAULT_VALUE);
                        this.bottomFormulaPane.populate(DEFAULT_VALUE);
                        this.serialTextField.setText(StringUtils.EMPTY);

                        if (selectCount != null) {
                            int selectCountType = selectCount.getType();
                            this.rsComboBox.setSelectedIndex(selectCountType);
                            switch (FilterTypeEnum.getFilterByValue(selectCountType)) {
                                case TOP:
                                    this.topFormulaPane.populate(selectCount.getFormulaCount());
                                    //前N个
                                    setCardPaneLayout.show(setCardPane, TOP.name());
                                    tipCardPaneLayout.show(tipCardPane, TOP.name());
                                    //隐藏tip 显示set
                                    setCardPane.setPreferredSize(new Dimension(156, 20));
                                    TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 2, VGAP);
                                    tipCardPane.setPreferredSize(new Dimension(0, 0));
                                    TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 4, 0);
                                    break;
                                case BOTTOM:
                                    this.bottomFormulaPane.populate(selectCount.getFormulaCount());
                                    //后N个
                                    setCardPaneLayout.show(setCardPane, BOTTOM.name());
                                    tipCardPaneLayout.show(tipCardPane, BOTTOM.name());
                                    //隐藏tip 显示set
                                    setCardPane.setPreferredSize(new Dimension(156, 20));
                                    TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 2, VGAP);
                                    tipCardPane.setPreferredSize(new Dimension(0, 0));
                                    TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 4, 0);
                                    break;
                                case SPECIFY:
                                    this.serialTextField.setText(selectCount.getSerial());
                                    //指定
                                    setCardPaneLayout.show(setCardPane, SPECIFY.name());
                                    tipCardPaneLayout.show(tipCardPane, SPECIFY.name());
                                    //显示set和tip
                                    setCardPane.setPreferredSize(new Dimension(156, 20));
                                    TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 2, VGAP);
                                    tipCardPane.setPreferredSize(new Dimension(224, 40));
                                    TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 4, VGAP_INNER);
                                    break;
                                case EVEN:
                                    //偶数
                                    setCardPaneLayout.show(setCardPane, EVEN.name());
                                    tipCardPaneLayout.show(tipCardPane, EVEN.name());
                                    //隐藏set 显示tip
                                    setCardPane.setPreferredSize(new Dimension(0, 0));
                                    TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 2, 0);
                                    tipCardPane.setPreferredSize(new Dimension(224, 40));
                                    TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 4, VGAP_INNER);
                                    break;
                                case ODD:
                                    //奇数
                                    setCardPaneLayout.show(setCardPane, ODD.name());
                                    tipCardPaneLayout.show(tipCardPane, ODD.name());
                                    //隐藏set 显示tip
                                    setCardPane.setPreferredSize(new Dimension(0, 0));
                                    TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 2, 0);
                                    tipCardPane.setPreferredSize(new Dimension(224, 40));
                                    TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 4, VGAP_INNER);
                                    break;
                                default:
                                    //未定义
                                    setCardPaneLayout.show(setCardPane, UNDEFINE.name());
                                    tipCardPaneLayout.show(tipCardPane, UNDEFINE.name());
                                    //隐藏set和tip
                                    setCardPane.setPreferredSize(new Dimension(0, 0));
                                    TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 2, 0);
                                    tipCardPane.setPreferredSize(new Dimension(0, 0));
                                    TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 4, 0);
                            }
                        } else {
                            this.rsComboBox.setSelectedIndex(0);
                            //未定义
                            setCardPaneLayout.show(setCardPane, UNDEFINE.name());
                            tipCardPaneLayout.show(tipCardPane, UNDEFINE.name());
                            //隐藏set和tip
                            setCardPane.setPreferredSize(new Dimension(0, 0));
                            TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 2, 0);
                            tipCardPane.setPreferredSize(new Dimension(0, 0));
                            TableLayoutHelper.modifyTableLayoutIndexVGap(contentPane, 4, 0);
                        }
                    }
                }
                rsComboBox.addActionListener(actionListener);
            }

            public void update(CellElement cellElement) {
                if (cellElement != null) {
                    Object value = cellElement.getValue();
                    if (value instanceof DSColumn) {
                        DSColumn dSColumn = (DSColumn) value;
                        int selectedFilterIndex = this.rsComboBox.getSelectedIndex();
                        if (selectedFilterIndex == 0) {
                            dSColumn.setSelectCount(null);
                        } else {
                            SelectCount selectCount = new SelectCount();
                            selectCount.setType(selectedFilterIndex);
                            dSColumn.setSelectCount(selectCount);
                            //noinspection Duplicates
                            if (selectedFilterIndex == TOP.getValue()) {
                                selectCount.setFormulaCount(this.topFormulaPane.getFormulaText());
                            } else if (selectedFilterIndex == BOTTOM.getValue()) {
                                selectCount.setFormulaCount(this.bottomFormulaPane.getFormulaText());
                            } else if (selectedFilterIndex == SPECIFY.getValue()) {
                                selectCount.setSerial(this.serialTextField.getText());
                            }
                        }
                    }
                }
            }

            /**
             * 添加事件监听器
             *
             * @param formulaListener 输入框改动事件监听器
             */
            public void addListener(UIObserverListener formulaListener) {
                topFormulaPane.addListener(formulaListener);
                bottomFormulaPane.addListener(formulaListener);
                rsComboBox.registerChangeListener(formulaListener);
                serialTextField.registerChangeListener(formulaListener);
            }

            /**
             * 去除事件监听器
             */
            public void removeListener() {
                topFormulaPane.removeListener();
                bottomFormulaPane.removeListener();
                rsComboBox.removeChangeListener();
                serialTextField.registerChangeListener(null);
            }
        }

        /**
         * 单元格元素>数据集>高级设置>公式输入框
         *
         * @see DSColumnAdvancedPane.JFormulaField
         */
        public class JFormulaField extends JPanel {
            private CellElement cellElement;
            private UITextField formulaTextField;
            private String defaultValue;

            public JFormulaField(String defaultValue) {

                this.defaultValue = defaultValue;
                formulaTextField = new UITextField();
                formulaTextField.setText(defaultValue);
                JPanel textFieldPane = new JPanel(new BorderLayout());
                textFieldPane.add(formulaTextField, BorderLayout.CENTER);
                textFieldPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
                UIButton formulaButton = new UIButton(IOUtils.readIcon("/com/fr/design/images/m_insert/formula.png"));
                formulaButton.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Formula") + "...");
                formulaButton.setPreferredSize(new Dimension(20, formulaTextField.getPreferredSize().height));
                formulaButton.addActionListener(formulaButtonActionListener);

                JPanel pane = new JPanel(new BorderLayout());
                pane.add(textFieldPane, BorderLayout.CENTER);
                pane.add(formulaButton, BorderLayout.EAST);
                this.setLayout(new BorderLayout());
                this.add(pane, BorderLayout.NORTH);
            }

            public void populate(String formulaContent) {
                this.formulaTextField.setText(formulaContent);
            }

            public void populateElement(CellElement cellElement) {
                this.cellElement = cellElement;
            }

            public String getFormulaText() {
                return this.formulaTextField.getText().trim();
            }

            /**
             * 添加事件监听器
             *
             * @param listener 公式文本输入框改动事件监听器
             */
            public void addListener(UIObserverListener listener) {
                this.formulaTextField.registerChangeListener(listener);
            }

            /**
             * 取消事件监听器
             */
            public void removeListener() {
                this.formulaTextField.registerChangeListener(null);
            }

            private ActionListener formulaButtonActionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    BaseFormula valueFormula = BaseFormula.createFormulaBuilder().build();
                    String text = formulaTextField.getText();
                    if (text == null || text.length() <= 0) {
                        valueFormula.setContent(defaultValue);
                    } else {
                        valueFormula.setContent(text);
                    }
                    final UIFormula formulaPane = FormulaFactory.createFormulaPaneWhenReserveFormula();
                    if (cellElement != null) {
                        Object value = cellElement.getValue();
                        if (value instanceof DSColumn) {
                            DSColumn dsColumn = (DSColumn) value;
                            String[] displayNames = DesignTableDataManager.getSelectedColumnNames(DesignTableDataManager.getEditingTableDataSource(), dsColumn.getDSName());
                            formulaPane.populate(valueFormula, new CustomVariableResolver(displayNames, true));
                            formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor(DSColumnAdvancedEditorPane.JFormulaField.this), new DialogActionAdapter() {
                                @Override
                                public void doOk() {
                                    BaseFormula valueFormula = formulaPane.update();
                                    if (valueFormula.getContent().length() <= 1) {
                                        formulaTextField.setText(defaultValue);
                                    } else {
                                        formulaTextField.setText(valueFormula.getContent());
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
        public class CustomValuePane extends JPanel {
            private static final String DEFAULT_VALUE = "=$$$";

            private JFormulaField formulaField;

            public CustomValuePane() {
                this.setLayout(new BorderLayout());
                UILabel customValueLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Display_Value"));
                customValueLabel.setPreferredSize(LABEL_DIMENSION);
                formulaField = new JFormulaField(DEFAULT_VALUE);
                this.add(TableLayoutHelper.createGapTableLayoutPane(new Component[][]{
                        new Component[]{customValueLabel, formulaField},
                }, new double[]{P}, new double[]{P, F}, HGAP, VGAP), BorderLayout.CENTER);
            }

            public void populate(CellElement cellElement) {
                if (cellElement != null) {
                    Object value = cellElement.getValue();
                    if (value instanceof DSColumn) {
                        DSColumn dSColumn = (DSColumn) value;
                        //formula
                        String valueFormula = dSColumn.getResult();
                        if (valueFormula != null) {
                            formulaField.populate(valueFormula);
                        } else {
                            formulaField.populate(DEFAULT_VALUE);
                        }
                        formulaField.populateElement(cellElement);

                    }
                }
            }

            public void update(CellElement cellElement) {
                if (cellElement != null) {
                    Object value = cellElement.getValue();
                    if (value instanceof DSColumn) {
                        DSColumn dSColumn = (DSColumn) (cellElement.getValue());
                        dSColumn.setResult(StringUtils.isEmpty(this.formulaField.getFormulaText()) ?
                                null : this.formulaField.getFormulaText());
                    }
                }
            }

            /**
             * 添加事件监听器
             *
             * @param formulaListener 公式输入框改动事件监听器
             */
            public void addListener(UIObserverListener formulaListener) {
                this.formulaField.addListener(formulaListener);
            }

            /**
             * 移除事件监听器
             */
            public void removeListener() {
                this.formulaField.removeListener();
            }
        }
    }

    @Override
    public Object getComboBoxSelected() {
        return ActionFactory.createAction(DSColumnCellAction.class);
    }
}
