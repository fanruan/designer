package com.fr.design.condition;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.widget.WidgetPane;
import com.fr.form.ui.*;
import com.fr.general.Inter;
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
        widgetButton = new UIButton(Inter.getLocText("FR-Designer_Edit"));
        widgetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final WidgetPane widgetPane = new WidgetPane();
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

        UILabel widgetLabel = new UILabel(Inter.getLocText("FR-Designer_Widget") + ":");
        this.add(widgetLabel);
        String[] editorTypes = new String[] {
                "",
                Inter.getLocText("FR-Designer_Text"),
                Inter.getLocText("FR-Designer_Form-TextArea"),
                Inter.getLocText("FR-Designer_Number"),
                Inter.getLocText("FR-Designer_Form-Password"),
                Inter.getLocText("FR-Designer_Form-Button"),
                Inter.getLocText("FR-Designer_Form-CheckBox"),
                Inter.getLocText("FR-Designer_Form-RadioGroup"),
                Inter.getLocText("FR-Designer_Form-CheckBoxGroup"),
                Inter.getLocText("FR-Designer_ComboBox"),
                Inter.getLocText("FR-Designer_Form-ComboCheckBox"),
                Inter.getLocText("FR-Designer_Date"),
                Inter.getLocText("FR-Designer_File"),
                Inter.getLocText("FR-Designer_Form-List"),
                Inter.getLocText("FR-Designer_Form-Iframe"),
                Inter.getLocText("FR-Designer_Tree-ComboBox"),
                Inter.getLocText("Form-View_Tree")
        };
        box = new UIComboBox(editorTypes);
        this.add(box);
        box.setEnabled(false);
        this.add(widgetButton);
        widgetButton.setEnabled(false);
        useWidget = new UICheckBox(Inter.getLocText(new String[]{"Use", "Widget"}));
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
        return Inter.getLocText("FR-Designer_Widget");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }


    public void setComboBox() {
        Widget value = this.widget;

        if (value instanceof ComboCheckBox) {
            box.setSelectedItem(Inter.getLocText("FR-Designer_Form-ComboCheckBox"));
        } else if (value instanceof ComboBox) {
            box.setSelectedItem(Inter.getLocText("FR-Designer_ComboBox"));
        } else if (value instanceof NumberEditor) {
            box.setSelectedItem(Inter.getLocText("FR-Designer_Number"));
        } else if (value instanceof IframeEditor) {
            box.setSelectedItem(Inter.getLocText("FR-Designer_Form-Iframe"));
        } else if (value instanceof FreeButton) {
            box.setSelectedItem(Inter.getLocText("FR-Designer_Form-Button"));
        } else if (value instanceof DateEditor) {
            box.setSelectedItem(Inter.getLocText("FR-Designer_Date"));
        } else if (value instanceof CheckBox) {
            box.setSelectedItem(Inter.getLocText("FR-Designer_Form-CheckBox"));
        } else if (value instanceof RadioGroup) {
            box.setSelectedItem(Inter.getLocText("FR-Designer_Form-RadioGroup"));
        } else if (value instanceof CheckBoxGroup) {
            box.setSelectedItem(Inter.getLocText("FR-Designer_Form-CheckBoxGroup"));
        } else if (value instanceof MultiFileEditor) {
            box.setSelectedItem(Inter.getLocText("FR-Designer_File"));
        } else if (value instanceof ListEditor) {
            box.setSelectedItem(Inter.getLocText("FR-Designer_Form-List"));
        } else if (value instanceof TreeComboBoxEditor) {
            box.setSelectedItem(Inter.getLocText("FR-Designer_Tree-ComboBox"));
        } else if (value instanceof TreeEditor) {
            box.setSelectedItem(Inter.getLocText("Form-View_Tree"));
        } else if (value instanceof Password) {
            box.setSelectedItem(Inter.getLocText("FR-Designer_Form-Password"));
        } else if (value instanceof TextArea) {
            box.setSelectedItem(Inter.getLocText("FR-Designer_Form-TextArea"));
        } else if (value instanceof TextEditor) {
            box.setSelectedItem(Inter.getLocText("FR-Designer_Text"));
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