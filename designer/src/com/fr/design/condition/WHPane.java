package com.fr.design.condition;

import com.fr.design.DesignerEnvManager;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.highlight.HighlightAction;
import com.fr.stable.Constants;
import com.fr.stable.unit.*;

import javax.swing.*;
import java.math.BigDecimal;

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
        this.add(new UILabel(Inter.getLocText(locString) + ":"));
        this.add(spinner = new UIBasicSpinner(new SpinnerNumberModel(0, 0,Integer.MAX_VALUE, 1)));
        this.add(this.unitLabel = new UILabel(getUnitString()));
        GUICoreUtils.setColumnForSpinner(spinner, 5);
        this.spinner.setValue(new Integer(0));
        this.locString = locString;
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
            unitLabel.setText(Inter.getLocText("FR-Designer_Unit_CM"));
        } else if (unitType == Constants.UNIT_INCH) {
            va = width.toINCHValue4Scale3();
            unitLabel.setText(Inter.getLocText("FR-Designer_Unit_INCH"));
        } else if (unitType == Constants.UNIT_PT) {
            va = width.toPTValue4Scale2();
            unitLabel.setText(Inter.getLocText("FR-Designer_Unit_PT"));
        } else {
            va = width.toMMValue4Scale2();
            unitLabel.setText(Inter.getLocText("FR-Designer_Unit_MM"));
        }
        // 只保留两位
        Float d = new Float(new BigDecimal(va + "").setScale(2, BigDecimal.ROUND_DOWN).floatValue());
        sp.setValue(d);
    }

    protected abstract UNIT getUnit(HighlightAction ha);

    protected String getUnitString() {
        int unitType = DesignerEnvManager.getEnvManager().getReportLengthUnit();
        if (unitType == Constants.UNIT_CM) {
            return Inter.getLocText("FR-Designer_Unit_CM");
        } else if (unitType == Constants.UNIT_INCH) {
            return Inter.getLocText("FR-Designer_Unit_INCH");
        } else if (unitType == Constants.UNIT_PT) {
            return Inter.getLocText("FR-Designer_Unit_PT");
        } else {
            return Inter.getLocText("FR-Designer_Unit_MM");
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