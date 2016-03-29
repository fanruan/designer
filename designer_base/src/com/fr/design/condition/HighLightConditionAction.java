package com.fr.design.condition;

import com.fr.design.actions.UpdateAction;

import java.awt.event.ActionEvent;

/**
* @author richie
* @date 2015-03-26
* @since 8.0
*/
public class HighLightConditionAction extends UpdateAction {
    private ConditionAttributesPane conditionAttributesPane;
    private ConditionAttrSingleConditionPane conditionAttrSingleConditionPane;

    public HighLightConditionAction(String name, ConditionAttributesPane conditionAttributesPane, ConditionAttrSingleConditionPane conditionAttrSingleConditionPane) {
        this.setName(name);
        this.conditionAttributesPane = conditionAttributesPane;
        this.conditionAttrSingleConditionPane = conditionAttrSingleConditionPane;
    }

    /**
     * 响应事件
     * @param e 事件
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        conditionAttributesPane.addConditionAttrSingleConditionPane(conditionAttrSingleConditionPane);
        conditionAttributesPane.removeUpdateActionFromUsableList(this);
        conditionAttributesPane.updateMenuDef();
        conditionAttributesPane.checkConditionPane();
        conditionAttributesPane.redraw();
        conditionAttrSingleConditionPane.setDefault();
    }

}