package com.fr.design.formula;


import com.fr.base.BaseFormula;
import com.fr.data.util.SortOrder;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.SortOrderComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.utils.gui.GUICoreUtils;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class SortFormulaPane extends JPanel {
	protected static final String InsetText = " ";
	
	protected SortOrderComboBox sortOrderComboBox;
	protected UITextField sortFormulaTextField;
    // 屏蔽掉“自定义比较规则”和“选择”按钮，只显示公式输入文本和公式按钮
	protected UIButton sortFormulaTextFieldButton;

    public SortFormulaPane() {
    	this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        sortOrderComboBox = new SortOrderComboBox();
        sortOrderComboBox.setSortOrder(new SortOrder(SortOrder.ORIGINAL));
        sortOrderComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                checkSortButtonEnabled();
            }
        });

        sortFormulaTextField = new UITextField(16);
        //Lance:添加一公式编辑器按钮
        sortFormulaTextFieldButton = new UIButton("...");
        sortFormulaTextFieldButton.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Formula") + "...");
        sortFormulaTextFieldButton.setPreferredSize(new Dimension(25, sortFormulaTextFieldButton.getPreferredSize().height));
        sortFormulaTextFieldButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                formulaAction();
            }
        });

        this.add(GUICoreUtils.createFlowPane(new JComponent[]{new UILabel(InsetText), //new UILabel(com.fr.design.i18n.Toolkit.i18nText("Select_sort_order") + ":"),
                sortOrderComboBox, new UILabel(InsetText),
                new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Formula") + ":="), sortFormulaTextField, //selectButton,
                sortFormulaTextFieldButton}, FlowLayout.LEFT));
    }
    
    public abstract void formulaAction();
    
    public void showFormulaDialog(String[] displayNames) {
    	String text = sortFormulaTextField.getText();
    	final UIFormula formulaPane = FormulaFactory.createFormulaPaneWhenReserveFormula();
        formulaPane.populate(BaseFormula.createFormulaBuilder().build(text), new CustomVariableResolver(displayNames, true));
        formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor(SortFormulaPane.this),
            new DialogActionAdapter() {
                public void doOk() {
                    BaseFormula fm = formulaPane.update();
                    if (fm.getContent().length() <= 1) {
                        sortFormulaTextField.setText("");
                    } else {
                        sortFormulaTextField.setText(fm.getContent().substring(1));
                    }
                }
            }).setVisible(true);
    }
    
    private void checkSortButtonEnabled() {
        if (this.sortOrderComboBox.getSortOrder().getOrder() == SortOrder.ORIGINAL) {
            sortFormulaTextField.setEnabled(false);
            sortFormulaTextFieldButton.setEnabled(false);
        } else {
            sortFormulaTextField.setEnabled(true);
            sortFormulaTextFieldButton.setEnabled(true);
        }
    }
}