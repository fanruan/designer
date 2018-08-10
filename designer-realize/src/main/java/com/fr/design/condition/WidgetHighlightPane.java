package com.fr.design.condition;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.widget.CellWidgetCardPane;
import com.fr.design.widget.WidgetManageCardPane;
import com.fr.design.widget.WidgetPane;
import com.fr.form.ui.*;

import com.fr.report.cell.cellattr.highlight.HighlightAction;
import com.fr.report.cell.cellattr.highlight.WidgetHighlightAction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
* @author richie
* @date 2015-03-26
* @since 8.0
*/
public class WidgetHighlightPane extends ConditionAttrSingleConditionPane<HighlightAction> {
    private static final int DIALOG_WIDTH = 700;
    private static final int DIALOG_HEIGHT = 400;

    private Widget widget;
    private UIComboBox box;
    private UICheckBox useWidget;
    private UIButton widgetButton;

    public WidgetHighlightPane(final ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
        widgetButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Edit"));
        widgetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final WidgetPane widgetPane = new WidgetPane() {
                    @Override
                    protected CellWidgetCardPane initWidgetCardPane(ElementCasePane pane) {
                        return new WidgetManageCardPane(pane);
                    }
                };
                widgetPane.populate(widget);
                BasicDialog dialog = widgetPane.showWindow(
                        SwingUtilities.getWindowAncestor(conditionAttributesPane));
                dialog.addDialogActionListener(new DialogActionAdapter() {
                    public void doOk() {
                        widget = widgetPane.update();
                        setComboBox();
                    }
                });
                dialog.setVisible(true);
            }
        });

        UILabel widgetLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Widget") + ":");
        this.add(widgetLabel);
        String[] editorTypes = new String[] {
                "",
                com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Text"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Form_TextArea"),
                com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Number"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Form_Password"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Form_Button"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Form_CheckBox"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Radio_Group"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Form_CheckBox_Group"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ComboBox"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_ComboCheckBox"),
                com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Date"),
                com.fr.design.i18n.Toolkit.i18nText("FR-Designer_File"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Form_List"),
                com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Form-Iframe"),
                com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Tree-ComboBox"),
                com.fr.design.i18n.Toolkit.i18nText("Form-View_Tree")
        };
        box = new UIComboBox(editorTypes);
        this.add(box);
        box.setEnabled(false);
        this.add(widgetButton);
        widgetButton.setEnabled(false);
        useWidget = new UICheckBox(com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"Use", "Widget"}));
        this.add(useWidget);
        useWidget.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!useWidget.isSelected()) {
                    box.setSelectedIndex(0);
                    widgetButton.setEnabled(false);
                } else {
                    setComboBox();
                    widgetButton.setEnabled(true);
                }
            }

        });
    }

    @Override
    public String nameForPopupMenuItem() {
        return com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Widget");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }


    public void setComboBox() {
        Widget value = this.widget;

        if (value instanceof ComboCheckBox) {
            box.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_ComboCheckBox"));
        } else if (value instanceof ComboBox) {
            box.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ComboBox"));
        } else if (value instanceof NumberEditor) {
            box.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Number"));
        } else if (value instanceof IframeEditor) {
            box.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Form-Iframe"));
        } else if (value instanceof FreeButton) {
            box.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Form_Button"));
        } else if (value instanceof DateEditor) {
            box.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Date"));
        } else if (value instanceof CheckBox) {
            box.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Form_CheckBox"));
        } else if (value instanceof RadioGroup) {
            box.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Radio_Group"));
        } else if (value instanceof CheckBoxGroup) {
            box.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Form_CheckBox_Group"));
        } else if (value instanceof MultiFileEditor) {
            box.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_File"));
        } else if (value instanceof ListEditor) {
            box.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Form_List"));
        } else if (value instanceof TreeComboBoxEditor) {
            box.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Tree-ComboBox"));
        } else if (value instanceof TreeEditor) {
            box.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("Form-View_Tree"));
        } else if (value instanceof Password) {
            box.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Form_Password"));
        } else if (value instanceof TextArea) {
            box.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Form_TextArea"));
        } else if (value instanceof TextEditor) {
            box.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Text"));
        }
    }

    public void populate(HighlightAction ha) {
        this.widget = ((WidgetHighlightAction)ha).getWidget();
        if (widget == null) {
            useWidget.setSelected(false);
            box.setSelectedIndex(0);
            widgetButton.setEnabled(false);
        } else {
            useWidget.setSelected(true);
            setComboBox();
            widgetButton.setEnabled(true);
        }
    }

    public HighlightAction update() {
        if (useWidget.isSelected()) {
            return new WidgetHighlightAction(widget);
        }
        return new WidgetHighlightAction();
    }

}
