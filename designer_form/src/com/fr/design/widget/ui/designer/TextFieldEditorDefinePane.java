package com.fr.design.widget.ui.designer;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.frpane.RegPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UIPropertyTextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.ui.designer.component.FormWidgetValuePane;
import com.fr.form.ui.TextEditor;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TextFieldEditorDefinePane extends FieldEditorDefinePane<TextEditor> {
    protected RegPane regPane;
    private UIPropertyTextField waterMarkDictPane;
    FormWidgetValuePane formWidgetValuePane;

    public TextFieldEditorDefinePane(XCreator xCreator) {
        super(xCreator);
    }


    @Override
    protected JPanel setFirstContentPane() {
        regPane = createRegPane();
        final RegPane.RegChangeListener rl = new RegPane.RegChangeListener() {

            @Override
            public void regChangeAction() {
                waterMarkDictPane.setText("");
                regPane.removeRegChangeListener(this);
            }
        };
        final RegPane.PhoneRegListener pl = new RegPane.PhoneRegListener() {
            public void phoneRegChangeAction(RegPane.PhoneRegEvent e) {
                if (StringUtils.isNotEmpty(e.getPhoneRegString())
                        && StringUtils.isEmpty(waterMarkDictPane.getText())) {
                    waterMarkDictPane.setText(Inter.getLocText("Example") + ":" + e.getPhoneRegString());
                    regPane.addRegChangeListener(rl);
                }
            }
        };
        regPane.addPhoneRegListener(pl);
        waterMarkDictPane = new UIPropertyTextField();
        waterMarkDictPane.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                regPane.removePhoneRegListener(pl);
                regPane.removeRegChangeListener(rl);
                waterMarkDictPane.removeKeyListener(this);
            }
        });
        //监听填写规则下拉框的值的变化
//        regPane.getRegComboBox().addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                RegExp regExp = (RegExp) regPane.getRegComboBox().getSelectedItem();
////                regErrorMsgTextField.setEnabled(regExp.errorMessageEditable());
//
//            }
//        });


        formWidgetValuePane = new FormWidgetValuePane(creator.toData(), false);
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer-Estate_Widget_Value"), SwingConstants.LEFT),  formWidgetValuePane},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_WaterMark"), SwingConstants.LEFT), waterMarkDictPane},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Font-Size"), SwingConstants.LEFT), fontSizePane}
        };
        double[] rowSize = {p, p, p, p, p};
        double[] columnSize = {p,f};
        int[][] rowCount = {{1, 3},{1, 1},{1, 1}};
        final JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 10, 10);
        JPanel boundsPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        boundsPane.add(panel);
        return boundsPane;
    }

    public  JPanel setValidatePane(){
        return regPane;
    }



    protected RegPane createRegPane() {
        return new RegPane();
    }

    @Override
    public String title4PopupWindow() {
        return "text";
    }

    @Override
    protected void populateSubFieldEditorBean(TextEditor e) {
        this.regPane.populate(e.getRegex());
        waterMarkDictPane.setText(e.getWaterMark());
        formWidgetValuePane.populate(e);
    }

    @Override
    protected TextEditor updateSubFieldEditorBean() {
        TextEditor ob = newTextEditorInstance();
        ob.setRegex(this.regPane.update());
        ob.setWaterMark(waterMarkDictPane.getText());
        formWidgetValuePane.update(ob);
        return ob;
    }

    protected TextEditor newTextEditorInstance() {
        return new TextEditor();
    }

}