package com.fr.design.gui.icombocheckbox;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/4/3
 */
public class UICheckListPopupTest extends TestCase {

    @Test
    public void testGetSelectedValues() {
        Object[] values = new Object[]{"a", "b", "c"};
        Map<Object, Boolean> map = new TreeMap<>();
        map.put("a", true);
        map.put("b", false);
        map.put("c", true);
        List<Object> list = new ArrayList<>();
        for (Map.Entry<Object, Boolean> entry : map.entrySet()) {
            if (entry.getValue()) {
                list.add(entry.getKey());
            }
        }
        Object[] selectValues = list.toArray();
        UICheckListPopup uiCheckListPopup1 = new UICheckListPopup(values);
        uiCheckListPopup1.setSelectedValue(map);
        Assert.assertArrayEquals(selectValues, uiCheckListPopup1.getSelectedValues());
        UICheckListPopup uiCheckListPopup2 = new UICheckListPopup(values, false);
        uiCheckListPopup2.setSelectedValue(map);
        Assert.assertArrayEquals(selectValues, uiCheckListPopup2.getSelectedValues());
    }

}