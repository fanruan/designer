package com.fr.design.dscolumn;

import com.fr.base.BaseFormula;
import com.fr.data.util.SortOrder;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.CustomVariableResolver;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.SortFormulaPane;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.report.cell.cellattr.core.group.DSColumn;
import com.fr.report.cell.cellattr.core.group.SelectCount;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.fr.report.cell.cellattr.core.group.FilterTypeEnum.BOTTOM;
import static com.fr.report.cell.cellattr.core.group.FilterTypeEnum.SPECIFY;
import static com.fr.report.cell.cellattr.core.group.FilterTypeEnum.TOP;

public class DSColumnAdvancedPane extends BasicPane {

    private static final String InsetText = " ";
    private SortPane sortPane;
    private SelectCountPane selectCountPane;
    private ValuePane valuePane;
    private UICheckBox horizontalExtendableCheckBox;
    private UICheckBox verticalExtendableCheckBox;
    private UICheckBox useMultiplyNumCheckBox;
    private UISpinner multiNumSpinner;

    public DSColumnAdvancedPane() {
        this(DSColumnPane.SETTING_ALL);
    }

    public DSColumnAdvancedPane(int setting) {
        this.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        sortPane = new SortPane();
        sortPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Sort_Sort_Order"), null));

        if (setting > DSColumnPane.SETTING_DSRELATED) {
            selectCountPane = new SelectCountPane();
            selectCountPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Bind_Column_Results_Filter"), null));
        }

        valuePane = new ValuePane();
        valuePane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Bind_Column_Custom_Data_Appearance"), null));


        JPanel extendablePane = null;
        if (setting > DSColumnPane.SETTING_DSRELATED) {
            // extendableDirectionPane
            JPanel extendableDirectionPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();

            extendableDirectionPane.add(horizontalExtendableCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ExpandD_Horizontal_Extendable")));
            extendableDirectionPane.add(verticalExtendableCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ExpandD_Vertical_Extendable")));

            extendablePane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ExpandD_Expandable"));
            extendablePane.setLayout(new BorderLayout());
            extendablePane.add(extendableDirectionPane, BorderLayout.CENTER);
        }

        JPanel multiNumPane = null;
        if (setting > DSColumnPane.SETTING_DSRELATED) {
            multiNumPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Fill_Blank_Data"));
            useMultiplyNumCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Column_Multiple"));
            multiNumPane.add(useMultiplyNumCheckBox);
            multiNumPane.add(new UILabel(InsetText));

            multiNumSpinner = new UISpinner(1, 10000, 1, 1);
            multiNumPane.add(multiNumSpinner);

            useMultiplyNumCheckBox.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    checkButtonEnabled();
                }
            });
        }

        double[] rowSize = {TableLayout.PREFERRED, TableLayout.PREFERRED,
                TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED};
        double[] columnSize = {TableLayout.FILL};

        Component[][] components = null;
        if (setting > DSColumnPane.SETTING_DSRELATED) {
            components = new Component[][]{
                    {sortPane},
                    {selectCountPane},
                    {valuePane},
                    {extendablePane},
                    {multiNumPane}
            };
        } else {
            components = new Component[][]{
                    {sortPane},
                    {valuePane}
            };
        }
        this.add(TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize), BorderLayout.CENTER);
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Advanced");
    }

    public void populate(TemplateCellElement cellElement) {
        if (cellElement == null) {
            return;
        }

        sortPane.populate(cellElement);
        valuePane.populate(cellElement);

        if (selectCountPane != null) {
            selectCountPane.populate(cellElement);

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
                this.useMultiplyNumCheckBox.setSelected(false);
            } else {
                this.useMultiplyNumCheckBox.setSelected(true);
                this.multiNumSpinner.setValue(cellExpandAttr.getMultipleNumber());
            }

            this.checkButtonEnabled();
        }
    }

    public void update(TemplateCellElement cellElement) {
        if (cellElement == null) {
            return;
        }

        sortPane.update(cellElement);
        valuePane.update(cellElement);

        if (selectCountPane != null) {
            selectCountPane.update(cellElement);

            CellExpandAttr cellExpandAttr = cellElement.getCellExpandAttr();
            if (cellExpandAttr == null) {
                cellExpandAttr = new CellExpandAttr();
                cellElement.setCellExpandAttr(cellExpandAttr);
            }

            // extendable
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
                cellExpandAttr.setMultipleNumber((int) this.multiNumSpinner.getValue());
            } else {
                cellExpandAttr.setMultipleNumber(-1);
            }
        }
    }

    private static class SortPane extends SortFormulaPane {
        private CellElement cellElement;

        @Override
        public void formulaAction() {
            if (cellElement == null) {
                return;
            }
            Object value = cellElement.getValue();
            if (value == null || !(value instanceof DSColumn)) {
                return;
            }

            String[] displayNames = DesignTableDataManager.getSelectedColumnNames(
                    DesignTableDataManager.getEditingTableDataSource(), ((DSColumn) value).getDSName());

            showFormulaDialog(displayNames);
        }

        void populate(CellElement cellElement) {
            if (cellElement == null) {
                return;
            }
            this.cellElement = cellElement;

            Object value = cellElement.getValue();
            if (value == null || !(value instanceof DSColumn)) {
                return;
            }
            DSColumn dSColumn = (DSColumn) value;

            int sort = dSColumn.getOrder();
            this.sortOrderComboBox.setSortOrder(new SortOrder(sort));

            String sortFormula = dSColumn.getSortFormula();
            sortFormulaTextField.setText(sortFormula);
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

            dSColumn.setOrder(this.sortOrderComboBox.getSortOrder().getOrder());
            //lance:sort formula

            String sText = null;
            if (!(sortFormulaTextField.getText() == null || sortFormulaTextField.getText().trim().equals("") || sortFormulaTextField.getText().trim().equals("$$$"))) {
                sText = new String(sortFormulaTextField.getText());
            }
            if (!(sText == null || sText.length() < 1)) {
                dSColumn.setSortFormula(sText);
            } else {
                dSColumn.setSortFormula(null);
            }
        }
    }

    private static class SelectCountPane extends JPanel {

        CellElement cellElement;
        //  private Comparator sortComparator;
        private UIComboBox selectCountComboBox;
        private JPanel selectCountCardPane;
        private UITextField serialTextField;

        JFormulaField topFormulaPane;
        JFormulaField bottomFormulaPane;

        public SelectCountPane() {
            this.setLayout(FRGUIPaneFactory.createBorderLayout());

            selectCountComboBox = new UIComboBox(new String[]{
                    com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Undefined"),
                    com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Bind_Column_Top_N"),
                    com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Bind_Column_Bottom_N"),
                    com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Odd"),
                    com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Even"),
                    com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Specify"),});
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
            this.add(GUICoreUtils.createFlowPane(new JComponent[]{new UILabel(InsetText), selectCountComboBox,
                    new UILabel(InsetText), selectCountCardPane}, FlowLayout.LEFT), BorderLayout.WEST);
//            selectCountCardPane.setLayout(new CardLayout());

            //not define pane

            JPanel undefinedPane = GUICoreUtils.createFlowPane(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Undefined")), FlowLayout.LEFT);
            topFormulaPane = new JFormulaField("-1");
            bottomFormulaPane = new JFormulaField("-1");
            serialTextField = new UITextField(18);
            JPanel oddPane = GUICoreUtils.createFlowPane(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Bind_Column_Result_Serial_Number_Start_From_1")
                    + "  " + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Bind_Column_Odd_Selected_(1,3,5...)")), FlowLayout.LEFT);
            JPanel evenPane = GUICoreUtils.createFlowPane(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Bind_Column_Result_Serial_Number_Start_From_1")
                    + "  " + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Bind_Column_Even_Selected_(2,4,6...)")), FlowLayout.LEFT);
            JPanel specifyPane = GUICoreUtils.createFlowPane(new JComponent[]{
                    serialTextField, new UILabel(
                    com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_DSColumn_Result_Group_Format", "1,2-3,5,8", "$__count__")
            )
            }, FlowLayout.LEFT);
            serialTextField.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Format") + ":=JOINARRAY(GREPARRAY(RANGE($__count__), item!=4), \",\")");
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
                if (selectCountType == TOP.getValue()) {
                    this.topFormulaPane.populate(selectCount.getFormulaCount());
                } else if (selectCountType == BOTTOM.getValue()) {
                    this.bottomFormulaPane.populate(selectCount.getFormulaCount());
                } else if (selectCountType == SPECIFY.getValue()) {
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
                if (selectCountSelectIndex == TOP.getValue()) {
                    selectCount.setFormulaCount(this.topFormulaPane.getFormulaText());
                } else if (selectCountSelectIndex == BOTTOM.getValue()) {
                    selectCount.setFormulaCount(this.bottomFormulaPane.getFormulaText());
                } else if (selectCountSelectIndex == SPECIFY.getValue()) {
                    selectCount.setSerial(this.serialTextField.getText());
                }
            }
        }
    }

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
            bottomFrmulaButton.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Formula") + "...");
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
                BaseFormula valueFormula = BaseFormula.createFormulaBuilder().build();
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
                formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor(JFormulaField.this), new DialogActionAdapter() {
                    @Override
                    public void doOk() {
                        BaseFormula valueFormula = formulaPane.update();
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

    private static class ValuePane extends JPanel {
        private JFormulaField formulaField;

        public ValuePane() {
            this.setLayout(FRGUIPaneFactory.createBoxFlowLayout());

            this.add(new UILabel(InsetText + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Value") + ":"));
            this.add(Box.createHorizontalStrut(2));
            this.add((formulaField = new JFormulaField("$$$")));
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

    private void checkButtonEnabled() {
        if (useMultiplyNumCheckBox.isSelected()) {
            multiNumSpinner.setEnabled(true);
        } else {
            multiNumSpinner.setEnabled(false);
        }
    }
}
