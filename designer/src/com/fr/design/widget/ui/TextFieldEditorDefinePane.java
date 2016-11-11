package com.fr.design.widget.ui;

import com.fr.design.gui.frpane.RegPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.TextEditor;
import com.fr.form.ui.reg.RegExp;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TextFieldEditorDefinePane extends FieldEditorDefinePane<TextEditor> {
    protected RegPane regPane;
    private WaterMarkDictPane waterMarkDictPane;

    public TextFieldEditorDefinePane() {
        this.initComponents();
    }

    @Override
    protected JPanel setFirstContentPane() {
        JPanel attrPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        regPane = createRegPane();
        final RegPane.RegChangeListener rl = new RegPane.RegChangeListener() {

            @Override
            public void regChangeAction() {
                waterMarkDictPane.setWaterMark("");
                regPane.removeRegChangeListener(this);
            }
        };
        final RegPane.PhoneRegListener pl = new RegPane.PhoneRegListener() {
            public void phoneRegChangeAction(RegPane.PhoneRegEvent e) {
                if (StringUtils.isNotEmpty(e.getPhoneRegString())
                        && StringUtils.isEmpty(waterMarkDictPane.getWaterMark())) {
                    waterMarkDictPane.setWaterMark(Inter.getLocText("Example") + ":" + e.getPhoneRegString());
                    regPane.addRegChangeListener(rl);
                }
            }
        };
        regPane.addPhoneRegListener(pl);
        getValidatePane().add(GUICoreUtils.createFlowPane(regPane, FlowLayout.LEFT));
        getValidatePane().add(GUICoreUtils.createFlowPane(new JComponent[]{new UILabel(Inter.getLocText(new String[]{"Error", "Tooltips"}) + ":"), getRegErrorMsgTextField()}, FlowLayout.LEFT, 24));
        JPanel advancedPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Designer_Advanced"));
        waterMarkDictPane = new WaterMarkDictPane();
        waterMarkDictPane.addInputKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                regPane.removePhoneRegListener(pl);
                regPane.removeRegChangeListener(rl);
                waterMarkDictPane.removeInputKeyListener(this);
            }
        });
        //监听填写规则下拉框的值的变化
        regPane.getRegComboBox().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegExp regExp = (RegExp) regPane.getRegComboBox().getSelectedItem();
                if (StringUtils.EMPTY.equals(regExp.toRegText())) {
                    getRegErrorMsgTextField().setEnabled(false);
                } else {
                    getRegErrorMsgTextField().setEnabled(true);
                }

            }
        });
        advancedPane.add(waterMarkDictPane);
        attrPane.add(advancedPane, BorderLayout.NORTH);
        return attrPane;
    }

    protected RegPane createRegPane() {
        return new RegPane();
    }

    @Override
    protected String title4PopupWindow() {
        return "text";
    }

    @Override
    protected void populateSubFieldEditorBean(TextEditor e) {
        this.regPane.populate(e.getRegex());
        getRegErrorMsgTextField().setText(e.getRegErrorMessage());
        waterMarkDictPane.populate(e);
    }

    @Override
    protected TextEditor updateSubFieldEditorBean() {
        TextEditor ob = newTextEditorInstance();
        ob.setRegErrorMessage(this.getRegErrorMsgTextField().getText());
        ob.setRegex(this.regPane.update());
        waterMarkDictPane.update(ob);

        return ob;
    }

    protected TextEditor newTextEditorInstance() {
        return new TextEditor();
    }

}