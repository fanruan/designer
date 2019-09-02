package com.fr.design.condition;

import com.fr.common.annotations.Open;

/**
* @author richie
* @date 2015-03-26
* @since 8.0
*/
@Open
public abstract class ConditionAttrSingleConditionPane<T> extends SingleConditionPane<T> {
    protected HighLightConditionAction hightLighAttrUpdateAction = null;
    private ConditionAttributesPane conditionAttributesPane;

    public ConditionAttrSingleConditionPane(ConditionAttributesPane conditionAttributesPane) {
        this(conditionAttributesPane, true);
    }

    public ConditionAttrSingleConditionPane(ConditionAttributesPane conditionAttributesPane, boolean isRemove) {
        super(isRemove);
        this.conditionAttributesPane = conditionAttributesPane;
    }

    /**
     *  条目名称
     * @return 名称
     */
    public abstract String nameForPopupMenuItem();

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    /**
     * 取消
     */
    public void doCancel() {
        conditionAttributesPane.removeConditionAttrSingleConditionPane(this);
        conditionAttributesPane.resetUseAbleActionList();
        conditionAttributesPane.updateMenuDef();
        conditionAttributesPane.checkConditionPane();
        conditionAttributesPane.redraw();
    }

    public void setDefault() {
    }

    public HighLightConditionAction getHighLightConditionAction() {
        if (hightLighAttrUpdateAction == null) {
            hightLighAttrUpdateAction = new HighLightConditionAction(nameForPopupMenuItem(), conditionAttributesPane, this);
        }
        return hightLighAttrUpdateAction;
    }
}
