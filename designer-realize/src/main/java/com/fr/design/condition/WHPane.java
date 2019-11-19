package com.fr.design.condition;

import com.fr.design.DesignerEnvManager;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.utils.gui.GUICoreUtils;

import com.fr.report.cell.cellattr.highlight.HighlightAction;
import com.fr.stable.Constants;
import com.fr.stable.unit.*;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.text.ParseException;

/**
* @author richie
* @date 2015-03-26
* @since 8.0
*/
public abstract class WHPane extends ConditionAttrSingleConditionPane<HighlightAction> {
    protected UILabel unitLabel;
    private UIBasicSpinner spinner;
    private String locString;

    protected WHPane(ConditionAttributesPane conditionAttributesPane, String locString) {
        super(conditionAttributesPane);
        this.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText(locString) + ":"));
        this.add(spinner = new UIBasicSpinner(new SpinnerNumberModel(0, 0,Integer.MAX_VALUE, 1)));
        this.add(this.unitLabel = new UILabel(getUnitString()));
        GUICoreUtils.setColumnForSpinner(spinner, 5);
        this.spinner.setValue(new Integer(0));
        this.locString = locString;
        final JFormattedTextField textField = ((JSpinner.NumberEditor) this.spinner.getEditor()).getTextField();
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    textField.commitEdit();
                } catch (ParseException ignore) {

                }
            }
        });
    }

    @Override
    public String nameForPopupMenuItem() {
        return locString;
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public  void populate(HighlightAction ha, JSpinner sp) {
        int unitType = DesignerEnvManager.getEnvManager().getReportLengthUnit();
        UNIT width = getUnit(ha);
        double va;
        if (unitType == Constants.UNIT_CM) {
            va = width.toCMValue4Scale2();
            unitLabel.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Unit_CM"));
        } else if (unitType == Constants.UNIT_INCH) {
            va = width.toINCHValue4Scale3();
            unitLabel.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Unit_INCH"));
        } else if (unitType == Constants.UNIT_PT) {
            va = width.toPTValue4Scale2();
            unitLabel.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Unit_PT_Duplicate"));
        } else {
            va = width.toMMValue4Scale2();
            unitLabel.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Unit_MM"));
        }
        // 只保留两位
        Float d = new Float(new BigDecimal(va + "").setScale(2, BigDecimal.ROUND_DOWN).floatValue());
        sp.setValue(d);
    }

    protected abstract UNIT getUnit(HighlightAction ha);

    protected String getUnitString() {
        int unitType = DesignerEnvManager.getEnvManager().getReportLengthUnit();
        if (unitType == Constants.UNIT_CM) {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Unit_CM");
        } else if (unitType == Constants.UNIT_INCH) {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Unit_INCH");
        } else if (unitType == Constants.UNIT_PT) {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Unit_PT_Duplicate");
        } else {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Unit_MM");
        }
    }

    public HighlightAction update(UIBasicSpinner sp) {
        float newWidth = ((Number)sp.getValue()).floatValue();
        // 只保留两位
        newWidth = new Float(new BigDecimal(newWidth + "").setScale(2, BigDecimal.ROUND_DOWN).floatValue());
        int unitType = DesignerEnvManager.getEnvManager().getReportLengthUnit();
        UNIT width;
        if (unitType == Constants.UNIT_CM) {
            width = new CM(newWidth);
        } else if (unitType == Constants.UNIT_INCH) {
            width = new INCH(newWidth);
        } else if (unitType == Constants.UNIT_PT) {
            width = new PT(newWidth);
        } else {
            width = new MM(newWidth);
        }
        return returnAction(width);
    }

    public void populate(HighlightAction ha) {
        this.populate(ha, spinner);
    }

    public HighlightAction update() {
        return this.update(spinner);
    }

    protected abstract HighlightAction returnAction(UNIT unit);
}
