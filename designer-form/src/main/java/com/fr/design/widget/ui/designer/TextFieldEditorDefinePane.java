package com.fr.design.widget.ui.designer;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.frpane.RegFieldPane;
import com.fr.design.gui.frpane.RegPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.ui.designer.component.FormWidgetValuePane;
import com.fr.form.ui.TextEditor;

import com.fr.stable.StringUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TextFieldEditorDefinePane extends FieldEditorDefinePane<TextEditor> {
    protected RegFieldPane regPane;
    private UITextField waterMarkDictPane;
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
                    waterMarkDictPane.setText(com.fr.design.i18n.Toolkit.i18nText("Example") + ":" + e.getPhoneRegString());
                    regPane.addRegChangeListener(rl);
                }
            }
        };
        regPane.addPhoneRegListener(pl);
        waterMarkDictPane = new UITextField();
        waterMarkDictPane.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                regPane.removePhoneRegListener(pl);
                regPane.removeRegChangeListener(rl);
                waterMarkDictPane.removeKeyListener(this);
            }
        });

        formWidgetValuePane = new FormWidgetValuePane(creator.toData(), false);
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        UILabel widgetValueLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Estate_Widget_Value"), SwingConstants.LEFT);
        widgetValueLabel.setVerticalAlignment(SwingConstants.TOP);
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Label_Name")), labelNameTextField},
                new Component[]{widgetValueLabel,  formWidgetValuePane},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_WaterMark"), SwingConstants.LEFT), waterMarkDictPane},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Font_Size"), SwingConstants.LEFT), fontSizePane}
        };
        double[] rowSize = {p, p, p, p, p, p};
        double[] columnSize = {p,f};
        int[][] rowCount = {{1, 1},{1, 3},{1, 1},{1, 1}};
        final JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
        JPanel boundsPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        boundsPane.add(panel);
        return boundsPane;
    }

    public  JPanel setValidatePane(){
        return regPane;
    }



    protected RegFieldPane createRegPane() {
        return new RegFieldPane();
    }

    @Override
    public String title4PopupWindow() {
        return "text";
    }

    @Override
    protected void populateSubFieldEditorBean(TextEditor e) {
        this.regPane.populate(e);
        waterMarkDictPane.setText(e.getWaterMark());
        formWidgetValuePane.populate(e);
    }

    @Override
    protected TextEditor updateSubFieldEditorBean() {
        TextEditor ob = (TextEditor)creator.toData();
        this.regPane.update(ob);
        ob.setWaterMark(waterMarkDictPane.getText());
        formWidgetValuePane.update(ob);
        return ob;
    }


}
