package com.fr.design.widget.ui;

import com.fr.design.gui.frpane.RegFieldPane;
import com.fr.design.gui.frpane.RegPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.TextEditor;

import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TextFieldEditorDefinePane extends FieldEditorDefinePane<TextEditor> {
    protected RegFieldPane regPane;
    private WaterMarkDictPane waterMarkDictPane;

    public TextFieldEditorDefinePane() {
        this.initComponents();
    }

    @Override
    protected JPanel setFirstContentPane() {
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
                    waterMarkDictPane.setWaterMark(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Example") + ":" + e.getPhoneRegString());
                    regPane.addRegChangeListener(rl);
                }
            }
        };
        regPane.addPhoneRegListener(pl);
        waterMarkDictPane = new WaterMarkDictPane();
        waterMarkDictPane.addInputKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                regPane.removePhoneRegListener(pl);
                regPane.removeRegChangeListener(rl);
                waterMarkDictPane.removeInputKeyListener(this);
            }
        });
        JPanel content = FRGUIPaneFactory.createBorderLayout_S_Pane();
        content.add(waterMarkDictPane, BorderLayout.CENTER);
        return content;
    }

    public JPanel setValidatePane() {
        return regPane;
    }


    protected RegFieldPane createRegPane() {
        return new RegFieldPane();
    }

    @Override
    protected String title4PopupWindow() {
        return "text";
    }

    @Override
    protected void populateSubFieldEditorBean(TextEditor e) {
        this.regPane.populate(e);
        waterMarkDictPane.populate(e);
    }

    @Override
    protected TextEditor updateSubFieldEditorBean() {
        TextEditor ob = newTextEditorInstance();
        this.regPane.update(ob);
        waterMarkDictPane.update(ob);

        return ob;
    }

    protected TextEditor newTextEditorInstance() {
        return new TextEditor();
    }

}