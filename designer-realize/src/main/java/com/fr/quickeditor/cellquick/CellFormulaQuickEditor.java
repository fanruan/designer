package com.fr.quickeditor.cellquick;

import com.fr.base.BaseFormula;
import com.fr.base.Style;
import com.fr.base.TextFormat;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.actions.insert.cell.FormulaCellAction;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.IOUtils;

import com.fr.grid.selection.CellSelection;
import com.fr.quickeditor.CellQuickEditor;
import com.fr.report.ReportHelper;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.stable.ColumnRow;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 公式快速编辑面板，同文本数字编辑拆分
 *
 * @author yaoh.wu
 * @version 2017年8月7日10点44分
 * @since 9.0
 */
public class CellFormulaQuickEditor extends CellQuickEditor {
    //文本域
    private UITextField formulaTextField;
    //编辑状态
    private boolean isEditing = false;

    //编辑的是公式,要保留公式里的这些属性,不然在公式和字符串转化时,就会丢失这些属性设置。
    private boolean reserveInResult = false;
    private boolean reserveOnWriteOrAnaly = true;

    //默认值
    private static final String DEFAULT_FORMULA = "=";

    private DocumentListener documentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            changeReportPaneCell(formulaTextField.getText().trim());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            changeReportPaneCell(formulaTextField.getText().trim());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            changeReportPaneCell(formulaTextField.getText().trim());
        }

    };

    public CellFormulaQuickEditor() {
        super();
    }

    /**
     * 详细信息面板
     */
    @Override
    public JComponent createCenterBody() {
        JPanel content = new JPanel(new BorderLayout());
        formulaTextField = new UITextField();
        formulaTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (tc != null) {
                    tc.getGrid().dispatchEvent(e);
                }
            }
        });
        JPanel textFieldPane = new JPanel(new BorderLayout());
        textFieldPane.add(formulaTextField, BorderLayout.CENTER);
        textFieldPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        UIButton formulaButton = new UIButton(IOUtils.readIcon("/com/fr/design/images/m_insert/formula.png"));
        formulaButton.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Formula") + "...");
        formulaButton.setPreferredSize(new Dimension(20, formulaTextField.getPreferredSize().height));
        formulaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String text = formulaTextField.getText();
                final UIFormula formulaPane = FormulaFactory.createFormulaPaneWhenReserveFormula();
                formulaPane.populate(BaseFormula.createFormulaBuilder().build(text));
                formulaPane.showLargeWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
                    @Override
                    public void doOk() {
                        BaseFormula fm = formulaPane.update();
                        if (fm.getContent().length() <= 1) {
                            formulaTextField.setText(DEFAULT_FORMULA);
                        } else {
                            formulaTextField.setText(fm.getContent());
                        }

                    }
                }).setVisible(true);
            }
        });

        JPanel pane = new JPanel(new BorderLayout());
        pane.add(textFieldPane, BorderLayout.CENTER);
        pane.add(formulaButton, BorderLayout.EAST);

        content.add(pane, BorderLayout.NORTH);
        return TableLayoutHelper.createGapTableLayoutPane(new Component[][]{
                        new Component[]{EMPTY_LABEL, content}},
                new double[]{TableLayout.PREFERRED},
                new double[]{TableLayout.PREFERRED, TableLayout.FILL}, HGAP, VGAP);
    }

    @Override
    public Object getComboBoxSelected() {
        return ActionFactory.createAction(FormulaCellAction.class);
    }


    private void changeReportPaneCell(String tmpText) {
        isEditing = true;
        //refresh一下，如果单元格内有新添加的控件，此时并不知道
        CellSelection cs1 = (CellSelection) tc.getSelection();
        ColumnRow columnRow = ColumnRow.valueOf(cs1.getColumn(), cs1.getRow());
        columnRowTextField.setText(columnRow.toString());
        cellElement = tc.getEditingElementCase().getTemplateCellElement(cs1.getColumn(), cs1.getRow());

        if (cellElement == null) {
            CellSelection cs = (CellSelection) tc.getSelection();
            cellElement = new DefaultTemplateCellElement(cs.getColumn(), cs.getRow());
            tc.getEditingElementCase().addCellElement(cellElement, false);
        }
        if (tmpText != null && (tmpText.length() > 0 && tmpText.charAt(0) == '=')) {
            BaseFormula textFormula = BaseFormula.createFormulaBuilder().build(tmpText);
            textFormula.setReserveInResult(reserveInResult);
            textFormula.setReserveOnWriteOrAnaly(reserveOnWriteOrAnaly);
            cellElement.setValue(textFormula);
        } else {
            Style style = cellElement.getStyle();
            if (style != null && style.getFormat() != null && style.getFormat() == TextFormat.getInstance()) {
                cellElement.setValue(tmpText);
            } else {
                cellElement.setValue(ReportHelper.convertGeneralStringAccordingToExcel(tmpText));
            }
        }
        fireTargetModified();
        formulaTextField.requestFocus();
        isEditing = false;
    }

    /**
     * 刷新详细内容
     */
    @Override
    protected void refreshDetails() {
        String str;
        if (cellElement == null) {
            str = StringUtils.EMPTY;
        } else {
            Object value = cellElement.getValue();
            if (value == null) {
                str = StringUtils.EMPTY;
            } else if (value instanceof BaseFormula) {
                BaseFormula formula = (BaseFormula) value;
                str = formula.getContent();
                reserveInResult = formula.isReserveInResult();
                reserveOnWriteOrAnaly = formula.isReserveOnWriteOrAnaly();
            } else {
                str = value.toString();
            }
        }
        showText(str);
        formulaTextField.setEditable(tc.isSelectedOneCell());
    }

    /**
     * 显示文本
     *
     * @param str 文本
     */
    public void showText(String str) {
        // 正在编辑时不处理
        if (isEditing) {
            return;
        }
        formulaTextField.getDocument().removeDocumentListener(documentListener);
        formulaTextField.setText(str);
        formulaTextField.getDocument().addDocumentListener(documentListener);
    }

    @Override
    public boolean isScrollAll() {
        return true;
    }

}