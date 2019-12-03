package com.fr.design.gui.ibutton;


import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 互斥的按钮
 * @author hades
 * @version 10.0
 * Created by hades on 2019/11/12
 */
public class ModeButtonGroup<T> extends ButtonGroup {

    private Map<T, AbstractButton> buttonMap = new LinkedHashMap<>();

    public void put(T t, AbstractButton button) {
        add(button);
        buttonMap.put(t, button);
    }

    public void setSelectButton(T t) {
        buttonMap.get(t).setSelected(true);
    }

    public T getCurrentSelected() {
        for (Map.Entry<T, AbstractButton> entry : buttonMap.entrySet()) {
            if (entry.getValue().isSelected()) {
                return entry.getKey();
            }
        }
        return buttonMap.entrySet().iterator().next().getKey();
    }

}
