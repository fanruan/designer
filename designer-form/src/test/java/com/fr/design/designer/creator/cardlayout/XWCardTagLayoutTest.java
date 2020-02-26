package com.fr.design.designer.creator.cardlayout;

import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import org.junit.Assert;
import org.junit.Test;

import java.awt.Dimension;

/**
 * Created by kerry on 2019-12-10
 */
public class XWCardTagLayoutTest {
    @Test
    public void testGetLayoutAdapter() {
        XWCardTagLayout tagLayout = new XWCardTagLayout(new WCardTagLayout(), new Dimension(100, 100));
        Assert.assertEquals("com.fr.design.designer.beans.adapters.layout.FRWCardTagLayoutAdapter", tagLayout.getLayoutAdapter().getClass().getName());
    }
}
