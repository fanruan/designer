package com.fr.form.ui;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.mainframe.FormSelectionUtils;
import com.fr.form.main.Form;
import com.fr.general.ComparatorUtils;
import junit.framework.TestCase;

import java.awt.Dimension;

/**
 * @author kerry
 * @date 2018/7/27
 */
public class WidgetNameCopyTest extends TestCase {

    public void testWidgetNameCopy() throws Exception {
        Widget button = new FreeButton();
        Form form = new Form();
        button.setWidgetName("button0");
        XCreator xCreator = XCreatorUtils.createXCreator(button, new Dimension(100, 100));
        XCreator copyXcreator1 = FormSelectionUtils.copyXcreator(form, xCreator);
        XCreator copyXcreator2 = FormSelectionUtils.copyXcreator(form, xCreator);
        copyXcreator1.toData().setWidgetName("button_test");
        assertTrue(!ComparatorUtils.equals(copyXcreator2.toData().getWidgetName(), copyXcreator1.toData().getWidgetName()));
    }

}
