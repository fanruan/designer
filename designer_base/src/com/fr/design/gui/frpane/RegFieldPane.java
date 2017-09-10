package com.fr.design.gui.frpane;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.TextEditor;
import com.fr.form.ui.reg.NoneReg;
import com.fr.form.ui.reg.RegExp;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * Created by kerry on 2017/9/4.
 */
public class RegFieldPane extends RegPane {
    protected RegErrorMsgPane regErrorMsgPane;

    public RegFieldPane(){
        this(ALL_REG_TYPE);
    }

    public RegFieldPane(RegExp[] types) {
        super(types);
        initComponents();
    }

    public void initComponents() {
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, IntervalConstants.INTERVAL_L1, 0));
        regErrorMsgPane = new RegErrorMsgPane();
        final RegChangeListener regChangeListener = new RegChangeListener() {

            @Override
            public void regChangeAction() {
                RegExp regExp = (RegExp)getRegComboBox().getSelectedItem();
                if(regExp instanceof NoneReg){
                    regErrorMsgPane.setVisible(false);
                    return;
                }
                regErrorMsgPane.setVisible(true);
            }
        };
        this.addRegChangeListener(regChangeListener);
        this.add(regErrorMsgPane, BorderLayout.CENTER);
    }

    @Override
    protected String title4PopupWindow() {
        return "RegFieldPane";
    }

    public void populate(TextEditor textEditor) {
        populate(textEditor.getRegex());
        regErrorMsgPane.populate(textEditor);
    }

    public void update(TextEditor textEditor) {
        textEditor.setRegex(update());
        regErrorMsgPane.update(textEditor);
    }

    private static class RegErrorMsgPane extends BasicPane {
        private UITextField regErrorMsgField;

        public RegErrorMsgPane() {
            this.setLayout(FRGUIPaneFactory.createBorderLayout());
            this.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L6, IntervalConstants.INTERVAL_L5, 0, 0));
            initRegErrorMsgField();
            UILabel tipLabel = new UILabel(Inter.getLocText("FR-Designer_Widget_Error_Tip"));
            tipLabel.setPreferredSize(new Dimension(60, 20));
            JPanel panel = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{tipLabel, regErrorMsgField}}, TableLayoutHelper.FILL_LASTCOLUMN, 10, LayoutConstants.VGAP_MEDIUM);
            this.add(panel);
        }

        private void initRegErrorMsgField() {
            regErrorMsgField = new UITextField();
            regErrorMsgField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    regErrorMsgField.setToolTipText(regErrorMsgField.getText());
                }

                public void insertUpdate(DocumentEvent e) {
                    regErrorMsgField.setToolTipText(regErrorMsgField.getText());
                }

                public void removeUpdate(DocumentEvent e) {
                    regErrorMsgField.setToolTipText(regErrorMsgField.getText());
                }
            });
        }

        @Override
        protected String title4PopupWindow() {
            return "RegErrorMsg";
        }

        public void populate(TextEditor textEditor) {
            regErrorMsgField.setText(textEditor.getRegErrorMessage());
        }

        public void update(TextEditor textEditor) {
            textEditor.setRegErrorMessage(regErrorMsgField.getText());
        }
    }

}
