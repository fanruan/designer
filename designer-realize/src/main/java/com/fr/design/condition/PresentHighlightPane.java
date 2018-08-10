package com.fr.design.condition;

import com.fr.base.present.Present;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.present.PresentPane;

import com.fr.report.cell.cellattr.PresentConstants;
import com.fr.report.cell.cellattr.highlight.HighlightAction;
import com.fr.report.cell.cellattr.highlight.PresentHighlightAction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
* @author richie
* @date 2015-03-26
* @since 8.0
*/
public class PresentHighlightPane extends ConditionAttrSingleConditionPane<HighlightAction> {
    private UIComboBox presentComboBox;
    private Present present;
    private UIButton editButton;
    private ValueEditorPane valueEditor;

    public PresentHighlightPane(final ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
        this.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Present") + ":"));

        String[] typeArray = {PresentConstants.NORMAL, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Other_Present")};
        presentComboBox = new UIComboBox(typeArray);
        this.add(presentComboBox);

        editButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Edit"));
//			this.add(this.valueTextField);
//			this.add(editButton);

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (presentComboBox.getSelectedIndex() != 0) {
                    final PresentPane presentPane = new PresentPane();
                    presentPane.populateBean(present);
                    BasicDialog dialog = presentPane.showWindow(SwingUtilities.getWindowAncestor(conditionAttributesPane));
                    dialog.addDialogActionListener(new DialogActionAdapter() {
                        @Override
                        public void doOk() {
                            present = presentPane.updateBean();
                        }
                    });
                    dialog.setVisible(true);
                }
            }
        });
//			this.valueTextField.setText("");
        valueEditor = ValueEditorPaneFactory.createBasicValueEditorPane();
        this.add(valueEditor);
        presentComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (presentComboBox.getSelectedIndex() == 1){
                    if (valueEditor.getParent() == PresentHighlightPane.this) {
                        PresentHighlightPane.this.remove(valueEditor);
                    }

                    PresentHighlightPane.this.add(editButton);
                    PresentHighlightPane.this.validate();
                    PresentHighlightPane.this.repaint();
                } else {
                    if (editButton.getParent() == PresentHighlightPane.this) {
                        PresentHighlightPane.this.remove(editButton);
                    }

                    PresentHighlightPane.this.add(valueEditor);
                    PresentHighlightPane.this.validate();
                    PresentHighlightPane.this.repaint();
                }
            }
        });
    }

    @Override
    public String nameForPopupMenuItem() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Present");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public  void populate(HighlightAction ha) {
        Object obj = ((PresentHighlightAction)ha).getValue();
        present = ((PresentHighlightAction)ha).getPresent();
        if(null != obj) {
            presentComboBox.setSelectedIndex(0);
            this.valueEditor.populate(obj);
        } else if(null != present) {
            presentComboBox.setSelectedIndex(1);
            this.valueEditor.populate(present);
        }
    }

    public HighlightAction update() {
        if (presentComboBox.getSelectedIndex() == 1) {
            return new PresentHighlightAction(present);
        }
        return new PresentHighlightAction(this.valueEditor.update());
    }
}
