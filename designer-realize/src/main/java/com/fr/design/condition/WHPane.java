package com.fr.design.condition;

import com.fr.design.DesignerEnvManager;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.unit.ReportLengthUNIT;
import com.fr.design.unit.UnitConvertUtil;
import com.fr.design.utils.gui.GUICoreUtils;

import com.fr.report.cell.cellattr.highlight.HighlightAction;
import com.fr.stable.unit.UNIT;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
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
        this.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText(locString) + ":"));
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

    public void populate(HighlightAction ha, JSpinner sp) {
        int unitType = DesignerEnvManager.getEnvManager().getReportLengthUnit();
        UNIT width = getUnit(ha);
        ReportLengthUNIT lengthUNIT = UnitConvertUtil.parseLengthUNIT(unitType);
        double va = lengthUNIT.unit2Value4Scale(width);
        unitLabel.setText(lengthUNIT.unitText());
        // 只保留两位
        Float d = new Float(new BigDecimal(va + "").setScale(2, BigDecimal.ROUND_DOWN).floatValue());
        sp.setValue(d);
    }

    protected abstract UNIT getUnit(HighlightAction ha);

    protected String getUnitString() {
        int unitType = DesignerEnvManager.getEnvManager().getReportLengthUnit();
        ReportLengthUNIT lengthUNIT = UnitConvertUtil.parseLengthUNIT(unitType);
        return lengthUNIT.unitText();
    }

    public HighlightAction update(UIBasicSpinner sp) {
        float newWidth = ((Number)sp.getValue()).floatValue();
        // 只保留两位
        newWidth = new Float(new BigDecimal(newWidth + "").setScale(2, BigDecimal.ROUND_DOWN).floatValue());
        int unitType = DesignerEnvManager.getEnvManager().getReportLengthUnit();
        ReportLengthUNIT lengthUNIT = UnitConvertUtil.parseLengthUNIT(unitType);
        UNIT width = lengthUNIT.float2UNIT(newWidth);
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
