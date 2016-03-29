package com.fr.design.condition;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.frpane.HyperlinkGroupPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.Inter;
import com.fr.js.*;
import com.fr.report.cell.cellattr.highlight.HighlightAction;
import com.fr.report.cell.cellattr.highlight.HyperlinkHighlightAction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
* @author richie
* @date 2015-03-26
* @since 8.0
*/
public class HyperlinkPane extends ConditionAttrSingleConditionPane<HighlightAction> {
    private NameJavaScriptGroup jsGroup;
    protected UITextField typeField;
    protected UICheckBox useHyperlink;
    protected UIButton hyperlinkButton;
    protected HyperlinkGroupPane pane;
    protected BasicDialog dialog;

    public HyperlinkPane(final ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
        hyperlinkButton = new UIButton(Inter.getLocText("FR-Designer_Edit"));
        hyperlinkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                NameJavaScriptGroup nameHyperlinks = jsGroup;
                pane = DesignerContext.getDesignerFrame().getSelectedJTemplate().getHyperLinkPane();
                pane.populate(nameHyperlinks);
                dialog = pane.showWindow(SwingUtilities.getWindowAncestor(conditionAttributesPane));
                dialog.addDialogActionListener(new DialogActionAdapter() {
                    @Override
                    public void doOk() {
                        jsGroup = pane.updateJSGroup();
                        setText();
                    }
                });
                dialog.setVisible(true);
            }
        });
        hyperlinkButton.setEnabled(false);
        UILabel hyperlinkLabel = new UILabel(Inter.getLocText(new String[]{"Hyperlink", "Type"}) + ":");
        typeField = new UITextField(12);
        typeField.setEditable(false);
        this.add(hyperlinkLabel);
        this.add(typeField);

        this.add(hyperlinkButton);
        useHyperlink = new UICheckBox(Inter.getLocText(new String[]{"Use", "Links"}));
        useHyperlink.addActionListener(l);
        this.add(useHyperlink);
    }

    @Override
    public String nameForPopupMenuItem() {
        return Inter.getLocText("FR-Designer_Hyperlink");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    ActionListener l = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (useHyperlink.isSelected()) {
                hyperlinkButton.setEnabled(true);
                setText();
            } else {
                hyperlinkButton.setEnabled(false);
                typeField.setText("");
            }
        }

    };



    public void setText() {
        if (jsGroup == null) {
            this.typeField.setText("");
        } else if (jsGroup.size() > 1) {
            this.typeField.setText(Inter.getLocText("FR-Designer_HyperLink_Must_Alone_Reset") + "!");
        } else if (jsGroup.size() == 1) {
            JavaScript js = jsGroup.getNameHyperlink(0).getJavaScript();
            if (js instanceof JavaScriptImpl) {
                this.typeField.setText("JavaScript");
            } else if (js instanceof ReportletHyperlink) {
                this.typeField.setText(Inter.getLocText(new String[]{"Report", "Links"}));
            } else if (js instanceof WebHyperlink) {
                this.typeField.setText(Inter.getLocText("Hyperlink-Web_link"));
            } else if (js instanceof EmailJavaScript) {
                this.typeField.setText(Inter.getLocText(new String[]{"Email", "Links"}));
            }

        }
    }

    public  void populate(HighlightAction ha) {
        this.jsGroup = ((HyperlinkHighlightAction)ha).getHperlink();
        if ( jsGroup == null || jsGroup.size() == 0) {
            this.useHyperlink.setSelected(false);
            this.hyperlinkButton.setEnabled(false);
        } else {
            this.useHyperlink.setSelected(true);
            this.hyperlinkButton.setEnabled(true);
            this.setText();
        }
    }

    public HighlightAction update() {
        if (this.useHyperlink.isSelected()) {
            return new HyperlinkHighlightAction(this.jsGroup);
        }
        return new HyperlinkHighlightAction();
    }
}